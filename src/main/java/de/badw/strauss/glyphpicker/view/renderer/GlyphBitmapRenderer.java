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

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Component;

import de.badw.strauss.glyphpicker.model.GlyphDefinition;

/**
 * A GlyphRenderer rendering bitmap images.
 */
public class GlyphBitmapRenderer extends GlyphRenderer {

    private static final long serialVersionUID = 1L;

    /**
     * The background color of list items which haven't been loaded yet.
     */
    private final Color emptyColor = UIManager
            .getColor("TextField.inactiveBackground");

    /**
     * Instantiates a new GlyphBitmapRenderer.
     *
     * @param container the container component
     */
    public GlyphBitmapRenderer(JComponent container) {
        super(container);
    }

    /* (non-Javadoc)
     * @see GlyphRenderer#getRendererComponent(GlyphDefinition, boolean)
     */
    @Override
    public Component getRendererComponent(GlyphDefinition gd, boolean isSelected) {

        ImageIcon icon = gd.getIcon();

        setIcon(gd.getIcon());

        if (icon == null) {
            setBackground(emptyColor);
        } else {
            configureBackground(isSelected);
        }

        return this;
    }

}