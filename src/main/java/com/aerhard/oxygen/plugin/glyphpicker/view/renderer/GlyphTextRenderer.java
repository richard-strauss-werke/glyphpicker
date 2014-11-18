package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;
import java.awt.Font;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphTextRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    private String fontName = "BravuraText";

    public GlyphTextRenderer() {
        setFont(new Font(fontName, Font.PLAIN, 40));
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        String ch;
        if (value == null) {
            ch = null;
        } else {
            ch = ((GlyphDefinition) value).getCharString();
        }

        setText(ch);

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