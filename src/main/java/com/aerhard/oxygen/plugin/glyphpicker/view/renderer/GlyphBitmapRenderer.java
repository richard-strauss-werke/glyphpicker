package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JComponent;

import java.awt.Component;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphBitmapRenderer extends GlyphRenderer {

    private static final long serialVersionUID = 1L;

    private JComponent container;
    
    public GlyphBitmapRenderer(JComponent container) {
        super(container);
        this.container = container;
    }

    @Override
    public Component getRendererComponent(GlyphDefinition gd,
            boolean isSelected) {
        
//        File a = new File(gd.getDataSource().getBasePath());
//        File parentFolder = new File(a.getParent());
//        File b = new File(parentFolder, gd.getUrl());
//        try {
//            ImageIcon icon = new GlyphBitmapIcon(50, b.toURL());
//            icon.setImageObserver(container);
//            setIcon(icon);
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
        if (gd.getIcon() == null) {
            setIcon(null);
            float factor = gd.getDataSource().getSizeFactor();
            new GlyphBitmapIconLoader(gd, container, Math.round(getPreferredSize().height * factor)).execute();
        } else {
            setIcon(gd.getIcon());
        }

        configureBackground(isSelected);
        
        return this;
    }


}