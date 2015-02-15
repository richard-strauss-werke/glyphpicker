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
import java.util.Map;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

/**
 * A font-based GlyphRenderer rendering simple text.
 */
public class GlyphTextRenderer extends GlyphRenderer {

    private static final long serialVersionUID = 1L;

    /**
     * The font attributes.
     */
    private final Map<TextAttribute, Integer> attr;

    /**
     * Instantiates a new GlyphTextRenderer.
     *
     * @param container the container
     */
    public GlyphTextRenderer(JComponent container) {
        super(container);
        attr = new HashMap<>();
        {
            attr.put(KERNING, KERNING_ON);
            attr.put(LIGATURES, LIGATURES_ON);
        }
    }

    /* (non-Javadoc)
     * @see com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphRenderer#getRendererComponent(com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition, boolean)
     */
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

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintComponent(g2);
    }

}