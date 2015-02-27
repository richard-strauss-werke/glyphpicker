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
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * The Class DescriptionRenderer.
 */
public class DescriptionRenderer extends JLabel implements TableCellRenderer {

    /**
     * The thickness of the component's border.
     */
    public static final int BORDER_THICKNESS = 4;
    private static final long serialVersionUID = 1L;
    /**
     * The internationalisation resource bundle.
     */
    private static final ResourceBundle I18N
            = ResourceBundle
            .getBundle("GlyphPicker");

    /**
     * The Constant className.
     */
    private static final String CLASS_NAME = DescriptionRenderer.class.getSimpleName();

    /**
     * The Constant CODEPOINT_LABEL.
     */
    private static final String CODEPOINT_LABEL = I18N.getString(CLASS_NAME
            + ".codepoint");

    /**
     * The Constant RANGE_LABEL.
     */
    private static final String RANGE_LABEL = I18N.getString(CLASS_NAME
            + ".range");

    /**
     * The Constant XML_ID_LABEL.
     */
    private static final String XML_ID_LABEL = I18N.getString(CLASS_NAME
            + ".xmlId");

    /**
     * The Constant ENTITY_LABEL.
     */
    private static final String ENTITY_LABEL = I18N.getString(CLASS_NAME
            + ".entity");
    
    /**
     * Instantiates a new description renderer.
     */
    public DescriptionRenderer() {
        setBorder(new EmptyBorder(BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS,
                BORDER_THICKNESS));
    }

    /**
     * Gets the html.
     *
     * @param d the d
     * @return the html
     */
    public static String getHTML(GlyphDefinition d) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>");

        if (d.getCharName() != null) {
            sb.append("<nobr><b>").append(d.getCharName()).append("</b></nobr><br>");
        }

        if (d.getEntity() != null) {
            sb.append("<nobr>").append(ENTITY_LABEL).append(": ").append(d.getEntity()).append("</nobr><br>");
        }

        if (d.getMappedChars() != null) {
            sb.append("<nobr>").append(CODEPOINT_LABEL).append(": ").append(d.getCodePointString()).append("</nobr><br>");
        }

        if (d.getRange() != null) {
            sb.append("<nobr>").append(RANGE_LABEL).append(": ").append(d.getRange()).append("</nobr><br>");
        }

        if (d.getId() != null) {
            sb.append("<nobr>").append(XML_ID_LABEL).append(": ").append(d.getId()).append("</nobr><br>");
        }

        return sb.toString();

    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        if (value == null) {
            setText("");
        } else {
            GlyphDefinition d = (GlyphDefinition) value;
            setText(getHTML(d));
        }

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        setOpaque(true);
        return this;
    }

}