package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class DescriptionRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    public DescriptionRenderer() {
    }

    // private int padding = 12;

    // public void setPadding(int padding) {
    // this.padding = padding;
    // }

    // private List<List<Integer>> rowColHeight = new ArrayList<>();

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (value == null) {
            setText("");
        } else {
            GlyphDefinition model = (GlyphDefinition) value;
            setText(formatText(model));
        }

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        setOpaque(true);
        // adjustRowHeight(table, row, column);
        return this;
    }

    public static String formatText(GlyphDefinition model) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>");

        if (model.getCharName() != null) {
            sb.append("<p><nobr><b>");
            sb.append(model.getCharName());
            sb.append("</b></nobr></p>");
        }

        if (model.getCodePoint() != null) {
            sb.append("<p><nobr>Codepoint: ");
            sb.append(model.getCodePoint());
            sb.append("</nobr></p>");
        }

        if (model.getRange() != null) {
            sb.append("<p><nobr>Range: ");
            sb.append(model.getRange());
            sb.append("</nobr></p>");
        }

//      List<String> classes = model.getClasses();
//        if (model.getClasses().size() > 0) {
//            sb.append("<p><nobr>Classes: ");
//            for (String cl : classes) {
//                sb.append(cl);
//                sb.append(" ");
//            }
//            sb.append("</nobr></p>");
//        }

        if (model.getId() != null) {
            sb.append("<p><nobr>xml:id: <em>");
            sb.append(model.getId());
            sb.append("</em></nobr></p>");
        }

        sb.append("</div></html>");

        return sb.toString();

    }

}