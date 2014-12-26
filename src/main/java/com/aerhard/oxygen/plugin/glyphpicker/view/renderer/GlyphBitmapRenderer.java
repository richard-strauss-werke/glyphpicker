package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;

import java.awt.Color;
import java.awt.Component;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphComponent;

public class GlyphBitmapRenderer extends JLabel implements GlyphRenderer {

    private static final long serialVersionUID = 1L;

    private Color containerSelectionBackground;
    private Color containerSelectionForeground;
    private Color containerBackground;
    private Color containerForeground;

    private JComponent container;

    public GlyphBitmapRenderer(JComponent container) {
        this.container = container;
        if (container instanceof JList) {
            containerSelectionBackground = ((JList<?>) container)
                    .getSelectionBackground();
            containerSelectionForeground = ((JList<?>) container)
                    .getSelectionForeground();
        }
        if (container instanceof JTable) {
            containerSelectionBackground = ((JTable) container)
                    .getSelectionBackground();
            containerSelectionForeground = ((JTable) container)
                    .getSelectionForeground();
        }
        containerBackground = container.getBackground();
        containerForeground = container.getForeground();
    }

    @Override
    public Component getRendererComponent(GlyphDefinition gd,
            boolean isSelected) {
        JComponent c;
        if (gd.getComponent() == null) {
            GlyphComponent gc = new GlyphComponent(gd, false);
            gc.loadIcon();
            gc.setContainer(container);
            gd.setComponent(gc);
            c = gc;
        } else {
            c = gd.getComponent();
        }

        if (isSelected) {
            c.setBackground(containerSelectionBackground);
            c.setForeground(containerSelectionForeground);
        } else {
            c.setBackground(containerBackground);
            c.setForeground(containerForeground);
        }

        c.setOpaque(true);
        return c;
    }


}