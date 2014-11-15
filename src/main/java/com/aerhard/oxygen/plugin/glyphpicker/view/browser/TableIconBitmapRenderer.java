package com.aerhard.oxygen.plugin.glyphpicker.view.browser;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphComponent;

public class TableIconBitmapRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    public TableIconBitmapRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JComponent c;
        if (value == null) {
            c = new GlyphComponent();
        } else {
            GlyphDefinition model = (GlyphDefinition) value;
            if (model.getComponent() == null) {
                GlyphComponent gc = new GlyphComponent(model, false);
                gc.loadIcon();
                gc.setContainer(table);
                model.setComponent(gc);
                c = gc;
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