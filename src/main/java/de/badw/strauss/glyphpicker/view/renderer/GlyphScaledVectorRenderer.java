/**
 * Copyright 2015 Alexander Erhard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.badw.strauss.glyphpicker.view.renderer;

import de.badw.strauss.glyphpicker.model.GlyphDefinition;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

import static java.awt.font.TextAttribute.*;

/**
 * A font-based GlyphRenderer rendering scaled vectors.
 */
public class GlyphScaledVectorRenderer extends GlyphRenderer {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The font render context.
     */
    private final FontRenderContext frc;
    /**
     * The font attributes.
     */
    private final Map<TextAttribute, Integer> attr;
    /**
     * The font name.
     */
    private String fontName = null;
    /**
     * The scaling factor.
     */
    private float factor = 0.73f;
    /**
     * The characters to render.
     */
    private String ch = null;

    /**
     * Instantiates a new glyph shape renderer.
     *
     * @param container the container
     */
    public GlyphScaledVectorRenderer(JComponent container) {
        super(container);
        frc = new FontRenderContext(null, true, true);
        setText(null);

        attr = new HashMap<>();
        {
            attr.put(KERNING, KERNING_ON);
            attr.put(LIGATURES, LIGATURES_ON);
        }
    }

    /* (non-Javadoc)
     * @see GlyphRenderer#getRendererComponent(GlyphDefinition, boolean)
     */
    public Component getRendererComponent(GlyphDefinition gd, boolean isSelected) {

        ch = gd.getMappedChars();
        fontName = gd.getDataSource().getFontName();

        factor = gd.getDataSource().getSizeFactor();

        configureBackground(isSelected);

        return this;
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
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

    /**
     * Draws a glyph to the component.
     *
     * @param g2       the graphic context
     * @param text     the text
     * @param fontName the font name
     */
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

        float scaledOffsetX = (visualBounds.x + (visualBounds.width / 2)) * scaleFactor;
        float scaledOffsetY = (visualBounds.y + (visualBounds.height / 2)) * scaleFactor;

        AffineTransform at = new AffineTransform();
        at.translate(centerX - scaledOffsetX, centerY - scaledOffsetY);
        at.scale(scaleFactor, scaleFactor);
        Shape outline = gv.getOutline();
        outline = at.createTransformedShape(outline);
        g2.fill(outline);
    }

}
