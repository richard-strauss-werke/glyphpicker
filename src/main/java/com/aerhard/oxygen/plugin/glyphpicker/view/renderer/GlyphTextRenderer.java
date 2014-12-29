package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Font;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

//TODO add error message if the font is not present

// TODO try to add graphic as html image

public class GlyphTextRenderer extends GlyphRenderer {

    private static final long serialVersionUID = 1L;

    private String previousFontName = null;


    public GlyphTextRenderer(JComponent container) {
        super(container);
    }

    
    public Component getRendererComponent(GlyphDefinition gd, boolean isSelected) {

        String fontName = gd.getDataSource().getFontName();

        if (fontName != null && !fontName.equals(this.previousFontName)) {
            
            float factor = gd.getDataSource().getSizeFactor();
            
            setFont(new Font(fontName, Font.PLAIN, Math.round(getPreferredSize().height * factor)));
            
            this.previousFontName = fontName;
        }

        setText(gd.getCharString());

        configureBackground(isSelected);

        return this;
    }
    
    

}