package com.aerhard.oxygen.plugin.glyphpicker.view.browser;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;

import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphItem;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphComponent;

public class TableIconRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    public TableIconRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        GlyphComponent c;
        if (value == null) {
            c = new GlyphComponent();
        } else {
            GlyphItem model = (GlyphItem) value;
            if (model.getComponent() == null) {
                c = new GlyphComponent(model, false);
                c.loadIcon();
                c.setContainer(table);
                model.setComponent(c);
            } else {
                c = model.getComponent();
            }
        }

        // if(c.getPreferredSize().height > 1) {
        // table.setRowHeight(row, c.getPreferredSize().height);
        // }

        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        } else {
            c.setBackground(table.getBackground());
            c.setForeground(table.getForeground());
        }

        c.setOpaque(true);
        return c;
    }
}