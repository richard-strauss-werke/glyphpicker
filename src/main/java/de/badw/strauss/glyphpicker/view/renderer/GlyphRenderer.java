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

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;

import de.badw.strauss.glyphpicker.model.GlyphDefinition;

/**
 * The abstract glyph renderer class.
 */
public abstract class GlyphRenderer extends JLabel {

    private static final long serialVersionUID = 1L;

    /**
     * The container's selection background.
     */
    private Color containerSelectionBackground;

    /**
     * The container's selection foreground.
     */
    private Color containerSelectionForeground;

    /**
     * The container's background.
     */
    private Color containerBackground;

    /**
     * The container's foreground.
     */
    private Color containerForeground;

    /**
     * Instantiates a new GlyphRenderer.
     *
     * @param container the container component
     */
    public GlyphRenderer(JComponent container) {
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);

        if (container instanceof JList) {
            containerSelectionBackground = ((JList<?>) container)
                    .getSelectionBackground();
            containerSelectionForeground = ((JList<?>) container)
                    .getSelectionForeground();
        } else if (container instanceof JTable) {
            containerSelectionBackground = ((JTable) container)
                    .getSelectionBackground();
            containerSelectionForeground = ((JTable) container)
                    .getSelectionForeground();
        } else {
            throw new ExceptionInInitializerError(
                    "Expected container to be an instance of JList or JTable");
        }
        containerBackground = container.getBackground();
        containerForeground = container.getForeground();
    }

    /**
     * Gets the renderer component to display the provided glyph definition.
     *
     * @param d          the glyph definition
     * @param isSelected the is selected
     * @return the renderer component
     */
    public abstract Component getRendererComponent(GlyphDefinition d,
                                                   boolean isSelected);

    /**
     * Sets the component's background based on the value of isSelected.
     *
     * @param isSelected the is selected
     */
    protected void configureBackground(boolean isSelected) {
        if (isSelected) {
            setBackground(containerSelectionBackground);
            setForeground(containerSelectionForeground);
        } else {
            setBackground(containerBackground);
            setForeground(containerForeground);
        }
        setOpaque(true);
    }

}
