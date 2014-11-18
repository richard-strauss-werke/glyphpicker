package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphComponent;

public class GlyphBitmapRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    public GlyphBitmapRenderer() {
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