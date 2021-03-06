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
import de.badw.strauss.glyphpicker.model.DataSource;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A glyph rendering adapter class which selects the GlyphRenderer based on the
 * glyphRenderer property of the GlyphDefinition's DataSource. .
 */
public class GlyphRendererAdapter extends JLabel implements TableCellRenderer,
        ListCellRenderer<Object> {

    private static final long serialVersionUID = 1L;

    /**
     * The GlyphRenderer objects.
     */
    private final Map<String, GlyphRenderer> glyphRenderers = new HashMap<String, GlyphRenderer>();

    /**
     * Instantiates a new GlyphRendererAdapter.
     *
     * @param container the container
     */
    public GlyphRendererAdapter(JComponent container) {
        setText(null);
        glyphRenderers.put(DataSource.GLYPH_SCALED_VECTOR_RENDERER,
                new GlyphScaledVectorRenderer(container));
        glyphRenderers.put(DataSource.GLYPH_VECTOR_RENDERER, new GlyphVectorRenderer(
                container));
        glyphRenderers.put(DataSource.GLYPH_BITMAP_RENDERER, new GlyphBitmapRenderer(
                container));
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
     */
    @Override
    public void setPreferredSize(Dimension d) {
        super.setPreferredSize(d);
        for (GlyphRenderer renderer : glyphRenderers.values()) {
            renderer.setPreferredSize(d);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
     * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            GlyphDefinition glyphDefinition = ((GlyphDefinition) value);
            GlyphRenderer renderer = glyphRenderers.get(glyphDefinition
                    .getDataSource().getGlyphRenderer());

            if (renderer != null) {
                return renderer.getRendererComponent(glyphDefinition,
                        isSelected);
            }
        }
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing
     * .JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            GlyphDefinition glyphDefinition = ((GlyphDefinition) value);
            GlyphRenderer renderer = glyphRenderers.get(glyphDefinition
                    .getDataSource().getGlyphRenderer());

            if (renderer != null) {
                return renderer.getRendererComponent(glyphDefinition,
                        isSelected);
            }
        }
        return this;
    }
}