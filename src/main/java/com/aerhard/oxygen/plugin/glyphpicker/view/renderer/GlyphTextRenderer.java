package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

//TODO add error message if the font is not present

public class GlyphTextRenderer extends JLabel implements GlyphRenderer {

    private static final long serialVersionUID = 1L;

    private String fontName = null;

    private Color containerSelectionBackground;
    private Color containerSelectionForeground;
    private Color containerBackground;
    private Color containerForeground;

    public GlyphTextRenderer(JComponent container) {
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);

            if (container instanceof JList) {
                containerSelectionBackground = ((JList<?>) container).getSelectionBackground();
                containerSelectionForeground= ((JList<?>) container).getSelectionForeground();
            }
            if (container instanceof JTable) {
                containerSelectionBackground = ((JTable) container).getSelectionBackground();
                containerSelectionForeground= ((JTable) container).getSelectionForeground();
            }
        containerBackground = container.getBackground();
        containerForeground = container.getForeground();
    }

    public Component getRendererComponent(GlyphDefinition gd, boolean isSelected) {

        String ch, fontName;

        ch = gd.getCharString();
        fontName = gd.getDataSource().getFontName();

        // if (this.fontName != null && !this.fontName.equals(fontName)) {
        if (fontName != null && !fontName.equals(this.fontName)) {
            setFont(new Font(fontName, Font.PLAIN, 40));
            this.fontName = fontName;
        }

        setText(ch);

        if (isSelected) {
            setBackground(containerSelectionBackground);
            setForeground(containerSelectionForeground);
        } else {
            setBackground(containerBackground);
            setForeground(containerForeground);
        }

        setOpaque(true);
        return this;
    }

}