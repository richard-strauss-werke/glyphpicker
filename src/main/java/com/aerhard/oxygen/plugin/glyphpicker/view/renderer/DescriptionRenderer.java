package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;
import java.util.List;

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
        List<String> classes = model.getClasses();
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

        if (model.getClasses().size() > 0) {
            sb.append("<p><nobr>Classes: ");
            for (String cl : classes) {
                sb.append(cl);
                sb.append(" ");
            }
            sb.append("</nobr></p>");
        }

        if (model.getId() != null) {
            sb.append("<p><nobr><em>");
            sb.append(model.getId());
            sb.append("</em></nobr></p>");
        }

        sb.append("</div></html>");

        return sb.toString();

    }

    // private void adjustRowHeight(JTable table, int row, int column) {
    //
    // int cWidth = table.getTableHeader().getColumnModel().getColumn(column)
    // .getWidth();
    // setSize(new Dimension(cWidth, 1000));
    // int prefH = getPreferredSize().height;
    // while (rowColHeight.size() <= row) {
    // rowColHeight.add(new ArrayList<Integer>(column));
    // }
    // List<Integer> colHeights = rowColHeight.get(row);
    // while (colHeights.size() <= column) {
    // colHeights.add(0);
    // }
    // colHeights.set(column, prefH);
    // int maxH = prefH;
    // for (Integer colHeight : colHeights) {
    // if (colHeight > maxH) {
    // maxH = colHeight;
    // }
    // }
    // maxH+=padding;
    // if (table.getRowHeight(row) != maxH) {
    // table.setRowHeight(row, maxH);
    // }
    // }

}