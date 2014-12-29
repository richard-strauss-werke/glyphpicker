package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

abstract public class GlyphRenderer extends JLabel {

    private static final long serialVersionUID = 1L;

    protected Color containerSelectionBackground;
    protected Color containerSelectionForeground;
    protected Color containerBackground;
    protected Color containerForeground;

    public GlyphRenderer(JComponent container) {
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);

        if (container instanceof JList) {
            containerSelectionBackground = ((JList<?>) container)
                    .getSelectionBackground();
            containerSelectionForeground = ((JList<?>) container)
                    .getSelectionForeground();
        }
        else if (container instanceof JTable) {
            containerSelectionBackground = ((JTable) container)
                    .getSelectionBackground();
            containerSelectionForeground = ((JTable) container)
                    .getSelectionForeground();
        }
        else {
            throw new ExceptionInInitializerError("Expected container to be an instance of JList or JTable");
        }
        containerBackground = container.getBackground();
        containerForeground = container.getForeground();
    }
    
    
    abstract public Component getRendererComponent(GlyphDefinition gd, boolean isSelected);

    protected void configureBackground(boolean isSelected) {
        if (isSelected) {
            setBackground(containerSelectionBackground);
            setForeground(containerSelectionForeground);
        } else {
            setBackground(containerBackground);
            setForeground(containerForeground);
        }
        setOpaque(true);
    }
    
}
