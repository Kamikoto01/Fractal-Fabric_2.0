package com.company;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;

public class JImageDisplay extends JComponent {


    private BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public JImageDisplay(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        super.setPreferredSize(new Dimension(width, height));
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    public void clearImage() {
        image.setRGB(0, 0, getWidth(), getHeight(), new int[getWidth() * getHeight()], 0, 1);
    /*
        setRGB(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize)
        Sets an array of integer pixels in the default RGB color model (TYPE_INT_ARGB)
        and default sRGB color space, into a portion of the image data.
    */
    }


    public void drawPixel(int x, int y, int rgbColor) {
        image.setRGB(x, y, rgbColor);
    }
}