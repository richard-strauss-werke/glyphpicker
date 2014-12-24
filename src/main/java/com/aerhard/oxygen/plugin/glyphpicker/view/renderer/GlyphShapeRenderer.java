package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class GlyphShapeRenderer extends JLabel implements TableCellRenderer,
        ListCellRenderer<Object> {

    private static final long serialVersionUID = 1L;

    private String fontName = "BravuraText";
    private int padding = 12;

    public void setPadding(int padding) {
        this.padding = padding;
    }

    private FontRenderContext frc;

    private String ch = null;

    public GlyphShapeRenderer() {
        frc = new FontRenderContext(null, true, true);
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);
        setText(null);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null) {
            ch = null;
        } else {
            ch = ((GlyphDefinition) value).getCharString();
        }
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

        if (value == null) {
            ch = null;
        } else {
            ch = ((GlyphDefinition) value).getCharString();
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
            drawGlyph(g2, ch, fontName, padding);
            g2.dispose();
        }
    }

    private void drawGlyph(Graphics2D g2, String text, String fontName,
            int padding) {

        // frc = g2.getFontRenderContext();

        float w = getWidth() - (2 * padding);
        float h = getHeight() - (2 * padding);

        int fontSize = Math.round(h);

        Font font = new Font(fontName, Font.PLAIN, fontSize);
        GlyphVector gv = font.createGlyphVector(frc, text);
        // Rectangle visualBounds = gv.getVisualBounds().getBounds();
        Rectangle visualBounds = gv.getPixelBounds(frc, 0, 0);

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

}