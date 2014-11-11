package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.Image;

import javax.swing.ImageIcon;

public class GlyphIcon extends ImageIcon {

    private static final long serialVersionUID = 1L;
    private int size;
    
    public GlyphIcon(Image image, int size) {
        super(image);
        this.size = size;
    }
    
    public GlyphIcon(int size) {
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
