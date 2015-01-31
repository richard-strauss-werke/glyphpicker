package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import static java.awt.font.TextAttribute.KERNING;
import static java.awt.font.TextAttribute.KERNING_ON;
import static java.awt.font.TextAttribute.LIGATURES;
import static java.awt.font.TextAttribute.LIGATURES_ON;

import javax.swing.JComponent;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.util.HashMap;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphTextRenderer extends GlyphRenderer {

    private static final long serialVersionUID = 1L;
    private HashMap<TextAttribute, Integer> attr;

    public GlyphTextRenderer(JComponent container) {
        super(container);
        attr = new HashMap<TextAttribute, Integer>();
        {
            attr.put(KERNING, KERNING_ON);
            attr.put(LIGATURES, LIGATURES_ON);
        } 
    }

    public Component getRendererComponent(GlyphDefinition gd, boolean isSelected) {

        String fontName = gd.getDataSource().getFontName();

        if (fontName != null) {
            float factor = gd.getDataSource().getSizeFactor();
            
            Font baseFont = new Font(fontName, Font.PLAIN,
                    Math.round(getPreferredSize().height * factor));

            Font font = baseFont.deriveFont(attr);
            setFont(font);
        }

        setText(gd.getCodePoint());

        configureBackground(isSelected);

        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintComponent(g2);
    }

}