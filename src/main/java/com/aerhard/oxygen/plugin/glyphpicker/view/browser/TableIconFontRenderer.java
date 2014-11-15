package com.aerhard.oxygen.plugin.glyphpicker.view.browser;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;

import java.awt.Component;
import java.awt.Font;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class TableIconFontRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger
            .getLogger(TableIconFontRenderer.class.getName());

    private String fontName = "BravuraText";

    public TableIconFontRenderer() {
        setFont(new Font(fontName, Font.PLAIN, 40));
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);
    }

    private String getGlyph(String code) {
        try {
            String cp = code.substring(2);
            int c = Integer.parseInt(cp, 16);
            String nn = Character.toString((char) c);
            return nn;
        } catch (Exception e) {
            LOGGER.info("Could not convert \"" + code + "\" to code point");
            return null;
        }
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        String ch;

        if (value == null) {
            ch = null;
        } else {
            ch = getGlyph(((GlyphDefinition) value).getCodePoint());
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