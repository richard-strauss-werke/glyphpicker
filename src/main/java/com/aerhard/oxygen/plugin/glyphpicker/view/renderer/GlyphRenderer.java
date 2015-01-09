package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public abstract class GlyphRenderer extends JLabel {

    private static final long serialVersionUID = 1L;

    private Color containerSelectionBackground;
    private Color containerSelectionForeground;
    private Color containerBackground;
    private Color containerForeground;
    
//    private int size = 0;

    public GlyphRenderer(JComponent container) {
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);

        if (container instanceof JList) {
            containerSelectionBackground = ((JList<?>) container)
                    .getSelectionBackground();
            containerSelectionForeground = ((JList<?>) container)
                    .getSelectionForeground();
        } else if (container instanceof JTable) {
            containerSelectionBackground = ((JTable) container)
                    .getSelectionBackground();
            containerSelectionForeground = ((JTable) container)
                    .getSelectionForeground();
        } else {
            throw new ExceptionInInitializerError(
                    "Expected container to be an instance of JList or JTable");
        }
        containerBackground = container.getBackground();
        containerForeground = container.getForeground();
    }

//    public void setSize(int size) {
//        this.size = size;
//        setPreferredSize(new Dimension(size, size));
//    }
    
    public abstract Component getRendererComponent(GlyphDefinition gd,
            boolean isSelected);

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
