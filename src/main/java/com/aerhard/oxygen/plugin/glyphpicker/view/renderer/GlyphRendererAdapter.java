package com.aerhard.oxygen.plugin.glyphpicker.view.renderer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

public class GlyphRendererAdapter extends JLabel implements TableCellRenderer,
        ListCellRenderer<Object> {

    private static final long serialVersionUID = 1L;

    private Map<String, GlyphRenderer> renderers = new HashMap<String, GlyphRenderer>();

    public GlyphRendererAdapter(JComponent container) {
        setText(null);
        renderers.put(DataSource.DISPLAY_MODE_VECTOR_FIT,
                new GlyphShapeRenderer(container));
        renderers.put(DataSource.DISPLAY_MODE_VECTOR_PROPORTIONAL,
                new GlyphTextRenderer(container));
        renderers.put(DataSource.DISPLAY_MODE_BITMAP, new GlyphBitmapRenderer(
                container));
    }

    @Override
    public void setPreferredSize(Dimension d) {
        for (GlyphRenderer renderer : renderers.values()) {
            renderer.setPreferredSize(d);
        }
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            GlyphDefinition glyphDefinition = ((GlyphDefinition) value);
            GlyphRenderer renderer = renderers.get(glyphDefinition
                    .getDataSource().getDisplayMode());
            if (renderer != null) {
                return renderer.getRendererComponent(glyphDefinition,
                        isSelected);
            }
        }
        return this;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            GlyphDefinition glyphDefinition = ((GlyphDefinition) value);
            GlyphRenderer renderer = renderers.get(glyphDefinition
                    .getDataSource().getDisplayMode());
            if (renderer != null) {
                return renderer.getRendererComponent(glyphDefinition,
                        isSelected);
            }
        }
        return this;
    }
}