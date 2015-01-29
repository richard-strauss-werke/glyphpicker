package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JComponent;

import java.awt.Component;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphBitmapRenderer extends GlyphRenderer {

    private static final long serialVersionUID = 1L;

    public GlyphBitmapRenderer(JComponent container) {
        super(container);
    }

    @Override
    public Component getRendererComponent(GlyphDefinition gd, boolean isSelected) {

        // File a = new File(gd.getDataSource().getBasePath());
        // File parentFolder = new File(a.getParent());
        // File b = new File(parentFolder, gd.getUrl());
        // try {
        // ImageIcon icon = new GlyphBitmapIcon(50, b.toURL());
        // icon.setImageObserver(container);
        // setIcon(icon);
        // } catch (MalformedURLException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // sets either the initial value null or the loaded icon
        setIcon(gd.getIcon());

        configureBackground(isSelected);

        return this;
    }

}