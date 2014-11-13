package com.aerhard.oxygen.plugin.glyphpicker.view.browser;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GlyphTable extends JTable {

    private static final long serialVersionUID = 1L;

    private TableIconRenderer tableIconRenderer;
    private TableDescriptionRenderer tableDescriptionRenderer;

    public GlyphTable() {
        tableIconRenderer = new TableIconRenderer();
        tableDescriptionRenderer = new TableDescriptionRenderer();
        setRowHeight(90);
        setShowVerticalLines(false);
        setIntercellSpacing(new Dimension(0, 1));

    }

    public TableIconRenderer getTableIconRenderer() {
        return tableIconRenderer;
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 0) {
            return tableIconRenderer;
        }
        if (column == 1) {
            return tableDescriptionRenderer;
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
