package fi.hu.cs.titokone;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import fi.hu.cs.ttk91.TTK91Memory;

// Display class was added by Toni Ruottu 8.4.2012

public class Display extends JPanel implements Runnable {

    static final int X = 160, Y = 120;
    static final int DEFAULT_START = 0x2000;
    static TTK91Memory mem;
    boolean updates = false;
    BufferedImage backBuffer;
    protected int baseAddress=DEFAULT_START;
    
    /* Display methods */

    public Display() {
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
        g.drawImage(backBuffer, 0, 0, Color.red, null);
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
        backBuffer = drawScreen(canvasWidth, canvasHeight, mem);
        repaint();
    }

    /* static private helpers */
   
    private BufferedImage resizeImage(BufferedImage image, int width, int height)
    {
        BufferedImage resized = new BufferedImage(width, height, image.getType());
        Graphics2D r = resized.createGraphics();
        r.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        r.drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(), image.getHeight(), null);
        r.dispose();
        return resized;
    }

    private BufferedImage resizeImage(BufferedImage image, double times) {
        int targetWidth = (int) (times * image.getWidth());
        int targetHeight = (int) (times * image.getHeight());
        BufferedImage resized = resizeImage(image, targetWidth, targetHeight);
        return resized;
    }

    private double maxFit(int targetWidth, int targetHeight, BufferedImage source)
    {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        double horizontalFit = (double) targetWidth / (double) sourceWidth;
        double verticalFit = (double) targetHeight / (double) sourceHeight;
        double maxfit = Math.min(horizontalFit, verticalFit);
        return maxfit;
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

    private void updateGfx(BufferedImage image, int address, int value) {
        if (address < baseAddress) {
            return;
        }
        int pixelno = address - baseAddress;
        int y = pixelno / X;
        int x = pixelno - (y*X);
        if (y >= Y) {
            return;
        }
        assert(x < X);
        int rgb4 = value & 0xfff;
        int rgb8 = torgb8(rgb4);
        image.setRGB(x, y, rgb8);
    }

    private BufferedImage memToImage(TTK91Memory mem) {
        BufferedImage image = new BufferedImage(X, Y, BufferedImage.TYPE_INT_RGB);
        if (mem == null) {
            return image;
        }
        int memsize = mem.getSize();
        for (int i = 0; i < (X * Y); i++) {
            int address = baseAddress + i;
            if (address >= memsize) {
                return image;
            }
            int value = mem.getValue(address);
            updateGfx(image, address, value);
        }
        return image;
    }
 
    private  BufferedImage addBorder(BufferedImage source, int width, int height, Color border) {
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int horizontalOffset = (width - source.getWidth()) / 2;
        int verticalOffset = (height - source.getHeight()) / 2;
        Graphics2D g = target.createGraphics();
        g.setBackground(border);
        g.clearRect(0, 0, width, height);
        g.drawImage(source, horizontalOffset, verticalOffset, Color.red, null);
        return target;
    }

    private BufferedImage drawScreen(int width, int height, TTK91Memory mem) {
        BufferedImage userimage = memToImage(mem);
        double times = maxFit(width, height, userimage);
        BufferedImage resized = resizeImage(userimage, times);
        return addBorder(resized, width, height, Color.gray);
    }

}
