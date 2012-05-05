package fi.hu.cs.titokone;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import fi.hu.cs.ttk91.TTK91Memory;

// Display class was added by Toni Ruottu 8.4.2012

public class Display extends Canvas {

    static final int X = 160, Y = 120;
    static final int START = 0x100;
    static TTK91Memory mem;
    static BufferedImage I = new BufferedImage(X, Y, BufferedImage.TYPE_INT_RGB);
    
    public Display() {
    }
    
    private static BufferedImage resizeImage(BufferedImage image, int width, int height)
    {
        BufferedImage resized = new BufferedImage(width, height, image.getType());
        Graphics2D r = resized.createGraphics();
        r.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        r.drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(), image.getHeight(), null);
        r.dispose();
        return resized;
    }

    private static double maxFit(Canvas canvas, BufferedImage image)
    {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int pictureWidth = image.getWidth();
        int pictureHeight = image.getHeight();
        double horizontalFit = (double) canvasWidth / (double) pictureWidth;
        double verticalFit = (double) canvasHeight / (double) pictureHeight;
        double maxfit = Math.min(horizontalFit, verticalFit);
        return maxfit;
    }

    private static BufferedImage maxResize(Canvas canvas, BufferedImage picture)
    {
        double times = maxFit(canvas, picture);
        int targetWidth = (int) (times * picture.getWidth());
        int targetHeight = (int) (times * picture.getHeight());
        BufferedImage resized = resizeImage(picture, targetWidth, targetHeight);
        return resized;
    }

    public void paint(Graphics g)
    {
        BufferedImage resized = maxResize(this, I);
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        int horizontalOffset = (screenWidth - resized.getWidth()) / 2;
        int verticalOffset = (screenHeight - resized.getHeight()) / 2;
        g.drawImage(resized, horizontalOffset, verticalOffset, Color.red, null);
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

    private void updateGfx(int address, int value) {
        if (address < START) {
            return;
        }
        int pixelno = address - START;
        int y = pixelno / X;
        int x = pixelno - (y*X);
        if (y >= Y) {
            return;
        }
        assert(x < X);
        int rgb4 = value & 0xfff;
        int rgb8 = torgb8(rgb4);
        I.setRGB(x, y, rgb8);
    }

    private void updateBuffer() {
        int memsize = mem.getSize();
        for (int i = 0; i < (X * Y); i++) {
            int address = START + i;
            if (address >= memsize) {
                return;
            }
            int value = mem.getValue(address);
            updateGfx(address, value);
        }
    }

    public void updateScreen() {
        updateBuffer();
        repaint();
    }

    public void quickUpdate(RunInfo info) {
        LinkedList changedMemoryLines = info.getChangedMemoryLines();

        Iterator changedMemoryLinesListIterator = changedMemoryLines.iterator();
        while (changedMemoryLinesListIterator.hasNext()) {
            Object[] listItem = (Object[])changedMemoryLinesListIterator.next();
            int address = ((Integer)listItem[0]).intValue();
            MemoryLine line = (MemoryLine)listItem[1];
            int value = line.getBinary();
            updateGfx(address, value);
        }
        repaint();
    }

    public void init (TTK91Memory mem) {
        this.mem = mem;
        updateScreen();
    }
    
}
