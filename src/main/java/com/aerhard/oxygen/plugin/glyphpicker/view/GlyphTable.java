package com.aerhard.oxygen.plugin.glyphpicker.view;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GlyphTable extends JTable {

    private static final long serialVersionUID = 1L;

    private TableItemRenderer tableIconRenderer;

    public GlyphTable() {
        tableIconRenderer = new TableItemRenderer();
        setRowHeight(90);
    }

    public TableItemRenderer getTableIconRenderer() {
        return tableIconRenderer;
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 0) {
            return tableIconRenderer;
        }
        return super.getCellRenderer(row, column);
    }

    // @Override
    // public String getToolTipText(MouseEvent e) {
    // String tip = null;
    // java.awt.Point p = e.getPoint();
    // int rowIndex = rowAtPoint(p);
    // int colIndex = columnAtPoint(p);
    //
    //
    // try {
    // //comment row, exclude heading
    // if(rowIndex >= 0){
    // tip = getValueAt(rowIndex, colIndex).toString() + rowIndex;
    // }
    // } catch (RuntimeException e1) {
    // //catch null pointer exception if mouse is over an empty line
    // }
    //
    // return tip;
    // }

}
