package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;
import java.util.ResourceBundle;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class DescriptionRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    private static ResourceBundle i18n = ResourceBundle
            .getBundle("GlyphPicker");
    private static String className = DescriptionRenderer.class.getSimpleName();

    private static final String codepointLabel = i18n.getString(className
            + ".codepoint");
    private static final String rangeLabel = i18n.getString(className
            + ".range");
    private static final String xmlIdLabel = i18n.getString(className
            + ".xmlId");

    public DescriptionRenderer() {

    }

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

    public static String getHTML(GlyphDefinition d) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><p>");

        if (d.getCharName() != null) {
            sb.append("<nobr><b>");
            sb.append(d.getCharName());
            sb.append("</b></nobr><br>");
        }

        if (d.getCodePoint() != null) {
            sb.append("<nobr>" + codepointLabel + ": ");
            sb.append(d.getCodePoint());
            sb.append("</nobr><br>");
        }

        if (d.getRange() != null) {
            sb.append("<nobr>" + rangeLabel + ": ");
            sb.append(d.getRange());
            sb.append("</nobr><br>");
        }

        if (d.getId() != null) {
            sb.append("<nobr>" + xmlIdLabel + ": <em>");
            sb.append(d.getId());
            sb.append("</em></nobr><br>");
        }

        sb.append("</p></html>");

        return sb.toString();

    }

}