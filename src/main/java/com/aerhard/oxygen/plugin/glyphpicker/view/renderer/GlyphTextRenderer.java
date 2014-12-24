package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;
import java.awt.Font;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

//TODO add error message if the font is not present

// TODO test in table view with gbank data!!

public class GlyphTextRenderer extends JLabel implements TableCellRenderer,
        ListCellRenderer<Object> {

    private static final long serialVersionUID = 1L;

    private String fontName = null;

    public GlyphTextRenderer() {
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        String ch, fontName;
        if (value == null) {
            ch = null;
            fontName = null;
        } else {
            GlyphDefinition gd = ((GlyphDefinition) value); 
            ch = gd.getCharString();
            fontName = gd.getDataSource().getFontName();
        }

        if (this.fontName != null && !this.fontName.equals(fontName)) {
            setFont(new Font(fontName, Font.PLAIN, 40));
            this.fontName = fontName;
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
    
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        String ch, fontName;
        if (value == null) {
            ch = null;
            fontName = null;
        } else {
            GlyphDefinition gd = ((GlyphDefinition) value); 
            ch = gd.getCharString();
            fontName = gd.getDataSource().getFontName();
        }

        if (fontName != null && !fontName.equals(this.fontName)) {
            setFont(new Font(fontName, Font.PLAIN, 40));
            this.fontName = fontName;
        }
        
        setText(ch);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setOpaque(true);
        return this;
    }
}