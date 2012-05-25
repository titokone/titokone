package fi.helsinki.cs.titokone.devices;
import fi.helsinki.cs.titokone.IODevice;
import fi.helsinki.cs.titokone.Interruptable;
import fi.helsinki.cs.titokone.InterruptGenerator;

import java.net.*;
import java.io.*;
/**
 *  a small UART to connect to neighboring machines.
 *      port 0 - data port,write to send/read to receive
 *               will only send/receive the lowest 8 bits
 *               ie. a byte at a time
 *      port 1 - state port read to get state, write to modify 
 *               modifiable bits.
 *              bit 0 write FIFO has data
 *              bit 1 read FIFO has data
 *              bit 2 Connected/RI (somebody is connecting to us)
 *              bit 3 Clear To Send (other end is connected and FIFO has
 *                      space 
 *              bit 4 write FIFO is full
 *              bit 5 read FIFO is full
 *              bit 29 enable RI interrupt
 *              bit 30 enable write FIFO empty interrupt
 *              bit 31 enable read FIFO full interrupt
 *      port 2 control port
 *              write 0 to disconnect
 *              write 1 followed by ipaddress followed by port in lowest
 *                      16 bits to connect
 *              write 1 followed by 0 and port in lowest 16 bits to
 *                      listen
 *              write 1 followed by two 0 to listen to a random port
 *              read to get the address of the other end
 *
 *       speed of the UART is approximately 1 bit every 10 clock cycles
 *       
 *                 
 *               
 *        
 */
public class UART
implements IODevice,
        InterruptGenerator
{
    protected Interruptable pic;
    protected int cyclesPerBit;
    protected int accumulatedCycles=0;
    protected int accumulatedReadCycles=0;
    protected int[] fifoIn=new int[16];
    protected int[] fifoOut=new int[16];    
    protected int inLeft=0,inRight=0,outLeft=0,outRight=0;
    protected CommandState state=CommandState.Normal;
    protected int remoteAddress=0xffffffff;
    protected int remotePort=0xffffffff;
    protected Socket socket;
    protected ServerSocket sSock;
    protected InputStream in;
    protected OutputStream out;
    protected int status;
    protected int modifiable=(0x80000000|0x40000000|0x20000000);
    
    
    public UART(int speed)
    {
        cyclesPerBit=speed;
    }
    public int getPortCount()
    {
        return 3;
    }
    @Override
    public int getPort(int n)
    {
        if(n==0)
        {
            if(inEmpty())
                return 0;
            int ret=fifoIn[inLeft];
            inLeft=(inLeft+1)%16;
            updateBits();            
            return ret;
        }
        if(n==1)
        {
            return status;
        }
        if(n==2)
        {
            return remoteAddress;
        }
        throw new RuntimeException("should not be possible "+n);
    }
    @Override
    public void setPort(int n,int value)
    {try{
        switch(state)
        {
            case Connect:
                remoteAddress=value;
                state=CommandState.Port;
                return;
            case Port:
                remotePort=value;
                state=CommandState.Normal;
                return;
        }
        if(n==0)
        {
            if(!outFull())
            {
                fifoOut[outRight]=value;
                outRight=(outRight+1)%16;
                updateBits();
            }
            return;
        }
        if(n==1)
        {
            value=modifiable&value;
            status=(((0xffffffff^modifiable)&status)|(value));
            return;
        }
        if(n==2)
        {
            switch(value)
            {
                case 0:socket.close();sSock=null;return;
                case 1:state=CommandState.Connect;return;
            }
        }
        }catch(IOException ioe){ioe.printStackTrace();}
        throw new RuntimeException("should not be possible "+n);
    }
    public void reset()
    {
        accumulatedCycles=0;
        accumulatedReadCycles=0;
        fifoIn=new int[16];
        fifoOut=new int[16];
        sSock=null;
        socket=null;
        state=CommandState.Normal;
        remotePort=remoteAddress=0xffffffff;
    }
    public void update()
    {try{
        if(socket==null&&remoteAddress!=0xffffffff&&remotePort!=0xffffffff)
        {
            if(remoteAddress==0)
            {
                sSock=new ServerSocket(remotePort);
                new Thread(new Runnable()
                    {                        
                        public void run()
                        {
                            try{
                                while(sSock!=null)
                                {
                                    socket=sSock.accept();
                                    in=socket.getInputStream();
                                    out=socket.getOutputStream();
                                    remoteAddress=intify(socket.getInetAddress().getAddress());
                                    remotePort=socket.getPort();
                                    setBit(2);
                                    if(getBit(29))
                                        pic.flagInterrupt(UART.this);
                                    while(socket!=null)
                                        try{Thread.sleep(200);}catch(InterruptedException ie){}
                                    unsetBit(2);
                                }
                            }catch(IOException ioe)
                            {
                                ioe.printStackTrace();
                                unsetBit(2);
                            }                            
                        }
                    }).start();
            }else{
                socket=new Socket(InetAddress.getByAddress(bytify(remoteAddress)),remotePort);
                in=socket.getInputStream();
                out=socket.getOutputStream();
                setBit(2);
            }
        }        
        if(socket!=null)
        {
            if(!socket.isConnected())
            {
                socket=null;
                in=null;
                out=null;
                unsetBit(2);
                accumulatedReadCycles=accumulatedCycles=0;                
                remoteAddress=0xffffffff;
                remotePort=0xffffffff;
            }else{  
                /*  accumulate cycles and read*/
                if(in.available()>0)
                    accumulatedReadCycles++;
                if(!inFull()&& 
                    in.available()>0 && 
                    (accumulatedReadCycles/cyclesPerBit)>=8)
                {
                    fifoIn[inRight]=in.read();                        
                    inRight=(inRight+1)%16;
                    accumulatedReadCycles-=(cyclesPerBit*8);
                    if(inFull())
                        bufferFull();
                }
                /*  accumulate cycles and write*/
                if(!outEmpty())
                {
                    accumulatedCycles++;
                    if((accumulatedCycles/cyclesPerBit)>=8)
                    {
                        accumulatedCycles-=(cyclesPerBit*8);
                        out.write(fifoOut[outLeft]);
                        outLeft=(outLeft+1)%16;                        
                    }
                    if(outEmpty())
                        bufferEmpty();
                }
            }
        }
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    protected void updateBits()
    {
        setBit(1);
        unsetBit(5);
        setBit(0);
        unsetBit(4);
        if(inLeft==inRight)
        {
            unsetBit(1);
        }
        if(((inLeft+1)%16)==inRight)
        {
            setBit(5);
        }
        if(outLeft==outRight)
        {
            unsetBit(0);
        }
        if(((outLeft+1)%16)==outRight)
        {
            setBit(4);
        }        
    }
    protected boolean inEmpty()
    {
        updateBits();
        return !getBit(1);
    }
    protected boolean outEmpty()
    {
        updateBits();
        return !getBit(0);
    }
    protected boolean outFull()
    {
        updateBits();
        return getBit(4);
    }
    protected boolean inFull()
    {
        updateBits();
        return getBit(5);
    }
    protected void setBit(int i)
    {
        status|=(1<<i);
    }
    protected void unsetBit(int i)
    {
        setBit(i);
        status^=(1<<i);
    }
    protected boolean getBit(int i)
    {
        return (status&(1<<i))>0;
    }
    protected void bufferEmpty()
    {        
        if(getBit(30))
            pic.flagInterrupt(this);
    }
    protected void bufferFull()
    {
        if(getBit(31))
            pic.flagInterrupt(this);
    }
    public void link(Interruptable il)
    {
        if(pic==null)
            pic=il;
    }
    protected int intify(byte[] four)
    {
        return (four[3]<<24)|(four[2]<<16)|(four[1]<<8)|four[0];        
    }
    protected byte[] bytify(int addr)
    {
        byte[] ret=new byte[4];
        for(int i=0;i<4;i++)
        {
            ret[0]=(byte)(0xff&(addr>>(i*8)));
        }
        return ret;
    }
    enum CommandState
    {
        Normal,
        Connect, //waiting for connect address
        Port; //waiting for port
    }
}