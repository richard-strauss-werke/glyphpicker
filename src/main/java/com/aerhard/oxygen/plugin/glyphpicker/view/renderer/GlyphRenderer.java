package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

import java.awt.Component;
import java.awt.Dimension;

public class GlyphRenderer extends JLabel implements TableCellRenderer,
        ListCellRenderer<Object> {

    GlyphBitmapRenderer glyphBitmapRenderer = new GlyphBitmapRenderer();
    GlyphShapeRenderer glyphShapeRenderer = new GlyphShapeRenderer();
    GlyphTextRenderer glyphTextRenderer = new GlyphTextRenderer();

    private static final long serialVersionUID = 1L;

    public GlyphRenderer() {
        setText(null);
    }

    @Override
    public void setPreferredSize(Dimension d) {
        glyphBitmapRenderer.setPreferredSize(d);
        glyphShapeRenderer.setPreferredSize(d); 
        glyphTextRenderer.setPreferredSize(d);
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (value == null) {
            return this;
        }

        GlyphDefinition gd = ((GlyphDefinition) value);

        String displayMode = gd.getDataSource().getDisplayMode();

        switch (displayMode) {
        case DataSource.DISPLAY_MODE_BITMAP:
            return glyphBitmapRenderer.getTableCellRendererComponent(table,
                    value, isSelected, hasFocus, row, column);
        case DataSource.DISPLAY_MODE_VECTOR_FIT:
            return glyphShapeRenderer.getTableCellRendererComponent(table,
                    value, isSelected, hasFocus, row, column);
        case DataSource.DISPLAY_MODE_VECTOR_PROPORTIONAL:
            return glyphTextRenderer.getTableCellRendererComponent(table,
                    value, isSelected, hasFocus, row, column);
        default:
            return this;
        }

    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        if (value == null) {
            return this;
        }

        GlyphDefinition gd = ((GlyphDefinition) value);

        String displayMode = gd.getDataSource().getDisplayMode();

        switch (displayMode) {
        case DataSource.DISPLAY_MODE_BITMAP:
            return glyphBitmapRenderer.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);
        case DataSource.DISPLAY_MODE_VECTOR_FIT:
            return glyphShapeRenderer.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);
        case DataSource.DISPLAY_MODE_VECTOR_PROPORTIONAL:
            return glyphTextRenderer.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);
        default:
            return this;
        }
        
    }
}