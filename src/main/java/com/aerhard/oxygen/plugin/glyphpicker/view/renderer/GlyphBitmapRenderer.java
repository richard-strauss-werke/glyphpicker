package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Component;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphBitmapRenderer extends GlyphRenderer {

    private static final long serialVersionUID = 1L;

    private final Color inactiveColor = UIManager
            .getColor("TextField.inactiveBackground");

    public GlyphBitmapRenderer(JComponent container) {
        super(container);
    }

    @Override
    public Component getRendererComponent(GlyphDefinition gd, boolean isSelected) {

        ImageIcon icon = gd.getIcon();

        setIcon(gd.getIcon());

        if (icon == null) {
            setBackground(inactiveColor);
        } else {
            configureBackground(isSelected);
        }

        return this;
    }

}