package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class GlyphBitmapIcon extends ImageIcon {

    private static final long serialVersionUID = 1L;
    private int size;

    public GlyphBitmapIcon(Image image, int size) {
        super(image);
        this.size = size;
    }

    public GlyphBitmapIcon(int size) {
        this.size = size;
    }

    public GlyphBitmapIcon(int size, URL url) {
        super(url);
        this.size = size;
    }

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }

    // @Override
    // public synchronized void paintIcon(Component component, Graphics g, int
    // x,
    // int y) {
    //
    // Image image = getImage();
    //
    //
    // g.drawImage(scaleToBound(image, 50, 50), x, y, getImageObserver());
    //
    // // super.paintIcon(component, g, x,y);
    // }
    //
    // public Image scaleToBound(Image image, int boundX, int boundY) {
    //
    // ImageObserver imageObserver = getImageObserver();
    //
    // int originalWidth = image.getWidth(imageObserver);
    // int originalHeight = image.getHeight(imageObserver);
    //
    // int resultWidth = originalWidth;
    // int resultHeight = originalHeight;
    //
    // if (originalWidth > boundX) {
    // resultWidth = boundX;
    // resultHeight = (resultWidth * originalHeight) / originalWidth;
    // }
    //
    // if (resultHeight > boundY) {
    // resultHeight = boundY;
    // resultWidth = (resultHeight * originalWidth) / originalHeight;
    // }
    //
    // // set minimum dimension of 1 px
    // resultWidth = Math.max(resultWidth, 1);
    // resultHeight = Math.max(resultHeight, 1);
    //
    // return image.getScaledInstance(resultWidth, resultHeight,
    // Image.SCALE_AREA_AVERAGING);
    // }

}
