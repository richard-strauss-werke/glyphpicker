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

}
