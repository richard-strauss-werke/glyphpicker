package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JComponent;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

import static java.awt.font.TextAttribute.*;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphShapeRenderer extends GlyphRenderer {

    private static final long serialVersionUID = 1L;

    private String fontName = null;

    private float factor = 66f / 90f;

    private FontRenderContext frc;

    private String ch = null;

    private Map<TextAttribute, Integer> attr;

    public GlyphShapeRenderer(JComponent container) {
        super(container);
        frc = new FontRenderContext(null, true, true);
        setText(null);

        attr = new HashMap<TextAttribute, Integer>();
        {
            attr.put(KERNING, KERNING_ON);
            attr.put(LIGATURES, LIGATURES_ON);
        }        
    }

    public Component getRendererComponent(GlyphDefinition gd, boolean isSelected) {

        ch = gd.getCodePoint();
        fontName = gd.getDataSource().getFontName();

        factor = gd.getDataSource().getSizeFactor();

        configureBackground(isSelected);

        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ch != null && fontName != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            drawGlyph(g2, ch, fontName);
        }
    }

    private void drawGlyph(Graphics2D g2, String text, String fontName) {

        float w = getWidth() * factor;
        float h = getHeight() * factor;

        int fontSize = Math.round(h);

        Font baseFont = new Font(fontName, Font.PLAIN, fontSize);
        Font font = baseFont.deriveFont(attr);
        
        GlyphVector gv = font.createGlyphVector(frc, text);
        Rectangle visualBounds = gv.getPixelBounds(frc, 0, 0);

        float scaleFactor = Math.min(w / visualBounds.width, h
                / visualBounds.height);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int x1 = (visualBounds.x + visualBounds.width);
        int glyphCenterX = (visualBounds.x + x1) / 2;
        float offsetX = (glyphCenterX) * scaleFactor;

        int y1 = (visualBounds.y + visualBounds.height);
        int glyphCenterY = (visualBounds.y + y1) / 2;
        float offsetY = (glyphCenterY) * scaleFactor;

        AffineTransform at = new AffineTransform();
        at.translate(centerX - offsetX, centerY - offsetY);
        at.scale(scaleFactor, scaleFactor);
        Shape outline = gv.getOutline();
        outline = at.createTransformedShape(outline);
        g2.fill(outline);
    }

}
