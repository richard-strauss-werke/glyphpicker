package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import java.awt.Component;
import java.awt.Dimension;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public interface GlyphRenderer {

    public Component getRendererComponent(GlyphDefinition gd, boolean isSelected);
    
    public void setPreferredSize(Dimension d);
    
}
