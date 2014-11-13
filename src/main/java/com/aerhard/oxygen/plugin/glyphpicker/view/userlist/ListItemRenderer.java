package com.aerhard.oxygen.plugin.glyphpicker.view.userlist;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphItem;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphComponent;

public class ListItemRenderer extends JLabel implements
        ListCellRenderer<Object> {
    private static final long serialVersionUID = 1L;

    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        GlyphComponent c;
        if (value == null) {
            c = new GlyphComponent();
        } else {
            GlyphItem model = (GlyphItem) value;
            if (model.getComponent() == null) {
                c = new GlyphComponent(model, true);
                c.loadIcon();
                c.setContainer(list);
                model.setComponent(c);
            } else {
                c = model.getComponent();
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