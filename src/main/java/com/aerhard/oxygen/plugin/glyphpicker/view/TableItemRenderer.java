package com.aerhard.oxygen.plugin.glyphpicker.view;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;

import com.aerhard.oxygen.plugin.glyphpicker.controller.DataStore;
import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphModel;

public class TableItemRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    private DataStore dataStore = new DataStore();

    public TableItemRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        GlyphComponent c;
        if (value == null) {
            c = new GlyphComponent();
        } else {
            GlyphModel model = (GlyphModel) value;
            if (model.getComponent() == null) {
                c = new GlyphComponent(model, dataStore, true);
                c.setContainer(table);
                model.setComponent(c);
            } else {
                c = model.getComponent();
                c.checkIcon();
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