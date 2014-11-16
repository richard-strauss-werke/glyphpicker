package com.aerhard.oxygen.plugin.glyphpicker.view.browser;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.apache.log4j.Logger;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class ListItemShapeRenderer extends JLabel implements
        ListCellRenderer<Object> {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger
            .getLogger(TableIconShapeRenderer.class.getName());

    private String fontName = "BravuraText";
    private int padding = 12;

    public void setPadding(int padding) {
        this.padding = padding;
    }

    private FontRenderContext frc = new FontRenderContext(null, true, true);

    private String ch = null;

    public ListItemShapeRenderer() {
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);
        setText(null);
        setPreferredSize(new Dimension(90, 90));
    }

    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        if (value == null) {
            ch = null;
        } else {
            ch = getGlyph(((GlyphDefinition) value).getCodePoint());
        }

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




    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ch != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            drawGlyph(g2, ch, fontName, padding, g);
            g2.dispose();
        }
    }

    private void drawGlyph(Graphics2D g2, String text, String fontName,
            int padding, Graphics g) {

        float w = getWidth() - (2 * padding);
        float h = getHeight() - (2 * padding);

        int fontSize = Math.round(h);

        Font font = new Font(fontName, Font.PLAIN, fontSize);
        GlyphVector gv = font.createGlyphVector(frc, text);
        Rectangle visualBounds = gv.getVisualBounds().getBounds();

        float scaleFactor = Math.min(w / visualBounds.width, h
                / visualBounds.height);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int x1 = (visualBounds.x + visualBounds.width);
        int glyphCenterX = (visualBounds.x + x1) / 2;
        float offsetX = (glyphCenterX) * scaleFactor;

        int y1 = (visualBounds.y + visualBounds.height);
        int glyphCenterY = (visualBounds.y + y1) / 2;
        float offsetY = (glyphCenterY) * scaleFactor;

        AffineTransform at = new AffineTransform();
        at.translate(centerX - offsetX, centerY - offsetY);
        at.scale(scaleFactor, scaleFactor);
        Shape outline = gv.getOutline();
        outline = at.createTransformedShape(outline);
        g2.fill(outline);

    }

    private String getGlyph(String code) {
        try {
            String cp = code.substring(2);
            int c = Integer.parseInt(cp, 16);
            String nn = Character.toString((char) c);
            return nn;    
        } catch (Exception e) {
            LOGGER.info("Could not convert \"" + code
                    + "\" to code point");
            return null;
        }
    }
    
}

