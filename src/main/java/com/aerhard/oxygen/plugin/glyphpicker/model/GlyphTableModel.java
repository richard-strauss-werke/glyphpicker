package com.aerhard.oxygen.plugin.glyphpicker.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class GlyphTableModel extends AbstractTableModel implements GlyphListModel {

    private static final long serialVersionUID = 1L;

    private static String[] columnNames = new String[] { "Glyph", "Description" };

    private List<GlyphDefinition> data = new ArrayList<GlyphDefinition>();

    public GlyphTableModel() {
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public List<String> getUniqueRanges() {
        List<String> ranges = new ArrayList<String>();
        String range;
        for (int i = 0, j = data.size(); i < j; i++) {
            range = data.get(i).getRange();
            if (!ranges.contains(range)) {
                ranges.add(range);
            }
        }
        return ranges;
    }

    public List<String> getUniqueClasses() {
        List<String> classes = new ArrayList<String>();
        List<String> itemClasses;
        for (int i = 0, j = data.size(); i < j; i++) {
            itemClasses = data.get(i).getClasses();
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

    public GlyphDefinition getModelAt(int row) {
        return data.get(row);
    }

    public Object getValueAt(int row, int col) {
        return (data.size() == 0) ? null : data.get(row);
    }

    public Class<? extends Object> getColumnClass(int column) {
        Object value = this.getValueAt(0, column);
        return (value == null ? Object.class : value.getClass());
    }

    public void setData(List<GlyphDefinition> data) {
        if (data == null) {
            this.data = new ArrayList<GlyphDefinition>();
        } else {
            this.data = data;
        }
        fireTableDataChanged();
    }
    
    public void applyModel(GlyphListModel model) {
        data = model.getData();
    }
    
    public List<GlyphDefinition> getData() {
        return data;
    }

}
