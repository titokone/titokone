// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import fi.helsinki.cs.ttk91.TTK91Memory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// Display class was added by Toni Ruottu 8.4.2012

public class Display extends JPanel implements Runnable {
    static final int fontWidth=5,fontHeight=6;
    static final int X = 160, Y = 120;
    static final int MARGIN=50; //30 minimum real pixel margin 
                                //on every side
    static final int DEFAULT_START = 0x2000;
    static TTK91Memory mem;
    boolean updates = false;
    //BufferedImage backBuffer;
    BufferedImage compatible;
    protected int baseAddress = DEFAULT_START;
    protected int lastX = 0;
    protected int lastY = 0;
    protected int left=0; //screen corner in real pixels
    protected int top=0;
    protected int xscale,yscale; //integer scaling factors
    protected Color margin=Color.GRAY;
    
    protected int mode=0;
    /* Display methods */

    public Display() {
    }

    protected void checkBuffers() {
        if (lastX != getWidth() || lastY != getHeight()) {
            Graphics2D g = (Graphics2D) getGraphics();
            GraphicsConfiguration gc = g.getDeviceConfiguration();
            lastX = getWidth();
            lastY = getHeight();
            compatible = gc.createCompatibleImage(lastX, lastY);
            xscale = roundUp(((double) (lastX-2*MARGIN)) / X);
            yscale = roundUp(((double) (lastY-2*MARGIN)) / Y);
            left=(lastX-xscale*X)/2;
            top=(lastY-yscale*Y)/2;
            
            clearBuffer();
        }
    }

    public void setMem(TTK91Memory mem) {
        this.mem = mem;
    }

    public void setUpdates(boolean updates) {
        this.updates = updates;
    }

    public void setBaseAddress(int base) {
        if (base > 0) {
            this.baseAddress = base;
        }
    }
    public void setMarginColor(int i)
    {
        margin=new Color(i);
    }
    public void setMode(int i)
    {        
        mode=i;
        if(mode<0||mode>2)
            mode=0;
        System.out.println("mode set:"+i);
    }

    /* Runnable methods */

    public void run() {
        while (true) {
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

    public void paint(Graphics g) {
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
     * see which mode we are in and use it to draw
     */
    protected void drawMode() {
        Graphics2D g = (Graphics2D) compatible.getGraphics();
        if (mem != null) {
            switch(mode)
            {
                case 0:draw12bit(g);return;
                case 1:drawText(g);return;
                case 2:drawIndexed(g);return;
            }
        }
    }

    /**
     * gray screen out
     */
    protected void clearBuffer() {
        Graphics2D g = (Graphics2D) compatible.getGraphics();
        g.setColor(margin);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * 12bit color mode
     */
    protected void draw12bit(Graphics2D g) {
        //xscale,yscale set so that we always hit full pixels
        for (int i = 0; i < (yscale*Y); i++) {
            for (int j = 0; j < (xscale*X); j++) {
                int x = j / xscale;
                int y = i / yscale;

                if (x < X && y < Y) {
                    int color = mem.getValue(baseAddress + (y * X + x));
                    compatible.setRGB(left+j, top+i, 0xff000000 | torgb8(color & 0xfff));
                }
            }
        }
    }
    /**
     * text mode
     */
    protected void drawText(Graphics2D g) {
        int charsX=X/fontWidth;
        int charsY=Y/fontHeight;
        int charMemStart=baseAddress+charsX*charsY;
        int paletteStart=charMemStart+256;
        boolean svits=(System.currentTimeMillis()/1000%2)==0;
        for (int i=0;i<(yscale*Y);i+=(fontHeight*yscale))
        {
            for(int j=0;j<(xscale*X);j+=(fontWidth*xscale))
            {
                int x=j/(xscale*fontWidth); //which char..
                int y=i/(yscale*fontHeight);
                int ch=mem.getValue(baseAddress+(y*charsX+x));
                int chDescr=mem.getValue(charMemStart+(ch&0xff));
                int bColor=mem.getValue(((ch>>16)&0xff)+paletteStart);
                int fColor=mem.getValue(((ch>>8)&0xff)+paletteStart);
                int effect=((ch>>24)&0xff);
                boolean blink=svits&&((effect&0x1)!=0);
                for(int k=0;k<(fontHeight*yscale);k++)
                {
                    for(int l=0;l<(fontWidth*xscale);l++)
                    {
                        boolean fg=((1<<((k/yscale)*fontWidth+(l/xscale)))&chDescr)>0;
                        if(blink)
                            fg=!fg;
                        compatible.setRGB(
                                left+j+l,
                                top+i+k,
                                0xff000000 | (fg?fColor:bColor));
                    }
                }
                
                
            }
        }
    }
    /**
     * 256 color indexed mode
     */
    protected void drawIndexed(Graphics2D g) {
        //xscale,yscale set so that we always hit full pixels
        for (int i = 0; i < lastY; i++) {
            for (int j = 0; j < lastX; j++) {
                int x = j / xscale;
                int y = i / yscale;

                if (x < X && y < Y) {
                    int color = mem.getValue(
                            mem.getValue(baseAddress + (y * X + x)) % 
                                    256 + baseAddress + X * Y);
                    compatible.setRGB(left+j, top+i, 0xff000000 | color);
                }
            }
        }
    }

    protected int roundUp(double d) {
        return (int) (d + 0.5);
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
