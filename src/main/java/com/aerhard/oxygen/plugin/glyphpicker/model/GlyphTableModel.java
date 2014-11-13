package com.aerhard.oxygen.plugin.glyphpicker.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphItem;

public class GlyphTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private static String[] columnNames = new String[] { "Glyph", "Description" };

    private GlyphItem[] data = new GlyphItem[0];

    public GlyphTableModel() {
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public List<String> getUniqueRanges() {
        List<String> ranges = new ArrayList<String>();
        String range;
        for (int i = 0, j = data.length; i < j; i++) {
            range = data[i].getRange();
            if (!ranges.contains(range)) {
                ranges.add(range);
            }
        }
        return ranges;
    }

    public List<String> getUniqueClasses() {
        List<String> classes = new ArrayList<String>();
        List<String> itemClasses;
        for (int i = 0, j = data.length; i < j; i++) {
            itemClasses = data[i].getClasses();
            for (String itemClass : itemClasses) {
                if (!classes.contains(itemClass)) {
                    classes.add(itemClass);
                }
            }
        }
        return classes;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public GlyphItem getModelAt(int row) {
        return data[row];
    }

    public Object getValueAt(int row, int col) {
        return (data.length == 0) ? null : data[row];
    }

    public Class<? extends Object> getColumnClass(int column) {
        Object value = this.getValueAt(0, column);
        return (value == null ? Object.class : value.getClass());
    }

    public void setData(List<GlyphItem> models) {
        if (models == null) {
            data = new GlyphItem[0];
        } else {
            data = new GlyphItem[models.size()];
            data = models.toArray(data);
        }
        fireTableDataChanged();
    }

}
