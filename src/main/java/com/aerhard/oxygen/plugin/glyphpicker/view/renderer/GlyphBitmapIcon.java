package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import java.awt.Image;

import javax.swing.ImageIcon;

public class GlyphBitmapIcon extends ImageIcon {

    private static final long serialVersionUID = 1L;
    private final int size;

    public GlyphBitmapIcon(Image image, int size) {
        super(image);
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
