package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphTableModel;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.DescriptionRenderer;

public class GlyphTable extends JTable {

    private static final long serialVersionUID = 1L;

    private TableCellRenderer tableIconRenderer;
    private TableCellRenderer tableDescriptionRenderer;

    public GlyphTable(GlyphTableModel glyphTableModel) {
        tableDescriptionRenderer = new DescriptionRenderer();
        setRowHeight(90);
        setShowVerticalLines(false);
        setIntercellSpacing(new Dimension(0, 1));

        setFillsViewportHeight(true);
        // setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setModel(glyphTableModel);

        getColumnModel().getColumn(0).setPreferredWidth(70);
        getColumnModel().getColumn(0).setMinWidth(30);
        getColumnModel().getColumn(1).setPreferredWidth(600);
        getColumnModel().getColumn(1).setMinWidth(10);
        
    }

    public void setTableIconRenderer(TableCellRenderer renderer) {
        tableIconRenderer = renderer;
    }

    public TableCellRenderer getTableIconRenderer() {
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
