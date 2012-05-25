package fi.helsinki.cs.titokone;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import fi.helsinki.cs.ttk91.TTK91Memory;

// Display class was added by Toni Ruottu 8.4.2012

public class Display extends JPanel implements Runnable {

    static final int X = 160, Y = 120;
    static final int DEFAULT_START = 0x2000;
    static TTK91Memory mem;
    boolean updates = false;
    //BufferedImage backBuffer;
    BufferedImage compatible;
    protected int baseAddress=DEFAULT_START;
    protected int lastX=0;
    protected int lastY=0;    
    /* Display methods */

    public Display() {
    }
    protected void checkBuffers()
    {
        if(lastX!=getWidth()||lastY!=getHeight())
        {
            Graphics2D g=(Graphics2D)getGraphics();
            GraphicsConfiguration gc=g.getDeviceConfiguration();
            lastX=getWidth();
            lastY=getHeight();
            /*backBuffer=new BufferedImage(
                            lastX, 
                            lastY, 
                            BufferedImage.TYPE_INT_RGB);*/
            compatible=gc.createCompatibleImage(lastX,lastY);
            clearBuffer();
        }
    }

    public void setMem(TTK91Memory mem) {
        this.mem = mem;
    }

    public void setUpdates(boolean updates) {
        this.updates = updates;
    }
    public void setBaseAddress(int base)
    {
        if(base>0)
            this.baseAddress=base;
    }

    /* Runnable methods */

    public void run() {
        while(true) {
            if (updates) {
                updateBuffer();
            }
            try {
                Thread.sleep(40); /* limit to max 25fps*/
            } catch (InterruptedException e) {

            }
        }
    }
 
    /* Canvas methods */   

    public void update(Graphics g) {
        g.drawImage(compatible, 0, 0, Color.red, null);
    }

    public void paint(Graphics g)
    {
        update(g);
    }

    /* private helpers */

    private void updateBuffer() {
        JPanel canvas = this;        
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int canvasArea = canvasWidth * canvasHeight;
        if (canvasArea < 1) {
            return;
        }
        /*  make sure we have something valid to draw on*/
        checkBuffers();        
        drawMode();//draw using current mode..            
        
        repaint();
    }
    /**
     *  see which mode we are in and use it to draw 
     */
    protected void drawMode()
    {
        Graphics2D g=(Graphics2D)compatible.getGraphics();
        if(mem!=null)
        {
            draw12bit(g);
        }
    }
    /**
     *  gray screen out
     */
    protected void clearBuffer()
    {
        Graphics2D g=(Graphics2D)compatible.getGraphics();
        g.setColor(Color.GRAY);
        g.fillRect(0,0,getWidth(),getHeight());
    }
    /**
     *  12bit color mode
     */
    protected void draw12bit(Graphics2D g)
    {
        //xscale,yscale set so that we always hit full pixels
        int xscale=roundUp(((double)lastY)/Y);
        int yscale=roundUp(((double)lastX)/X);
        for(int i=0;i<lastY;i++)
        {
            for(int j=0;j<lastX;j++)
            {
                int x=j/xscale;
                int y=i/yscale;
                
                if(x<X&&y<Y)
                {
                    int color=mem.getValue(baseAddress+(y*X+x));
                    compatible.setRGB(j,i,0xff000000|torgb8(color&0xfff));
                }
            }
        }
    }
    protected int roundUp(double d)
    {
        return (int)(d+0.5);
    }

    static private int torgb8(int rgb4i) {
        long rgb4l = (long) rgb4i;
        long r4 = (rgb4l & 0xf00) >>> 8;
        long g4 = (rgb4l & 0x0f0) >>> 4;
        long b4 = rgb4l & 0x00f;
        long r8 = (r4 << 4) | r4;
        long g8 = (g4 << 4) | g4;
        long b8 = (b4 << 4) | b4;
        long rgb8l = ((r8 << 16) & 0xff0000) | ((g8 << 8) & 0x00ff00) | (b8 & 0x0000ff);
        int rgb8i = (int) rgb8l;
        return rgb8i;
    }
}
