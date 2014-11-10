package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.aerhard.oxygen.plugin.glyphpicker.controller.DataStore;
import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphModel;

class ListItemRenderer extends JLabel implements ListCellRenderer<Object> {
    private static final long serialVersionUID = 1L;

    private DataStore dataStore = new DataStore();

    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        GlyphComponent c;
        if (value == null) {
            c = new GlyphComponent();
        } else {
            GlyphModel model = (GlyphModel) value;
            if (model.getComponent() == null) {
                c = new GlyphComponent(model, dataStore, true);
                c.setContainer(list);
                model.setComponent(c);
            } else {
                c = model.getComponent();
                c.checkIcon();
            }
        }

        if (isSelected) {
            c.setBackground(list.getSelectionBackground());
            c.setForeground(list.getSelectionForeground());
        } else {
            c.setBackground(list.getBackground());
            c.setForeground(list.getForeground());
        }

        c.setOpaque(true);

        return c;
    }
}