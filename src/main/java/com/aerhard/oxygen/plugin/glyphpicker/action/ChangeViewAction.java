package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.aerhard.oxygen.plugin.glyphpicker.view.ContainerPanel;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphTable;

public class ChangeViewAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private ContainerPanel panel;
    private GlyphTable table;
    private GlyphGrid list;

    public ChangeViewAction(ContainerPanel panel, GlyphTable table,
            GlyphGrid list) {
        super(null, new ImageIcon(
                ChangeViewAction.class.getResource("/images/grid.png")));
        this.panel = panel;
        this.table = table;
        this.list = list;
        
        String mnemonic = "T";
        
        putValue(SHORT_DESCRIPTION, "Toggle between grid and table view (Alt+"+mnemonic+")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (panel.getListComponent() instanceof GlyphGrid) {
            // NB get the old component's top row before the component
            // is replaced!
            int row = list.getTopVisibleRow();
            panel.setListComponent(table);
            panel.getInfoPanel().setVisible(false);
            panel.revalidate();
            table.setTopVisibleRow(row);
        } else {
            // NB get the old component's top row before the component
            // is replaced!
            int row = table.getTopVisibleRow();
            panel.setListComponent(list);
            panel.getInfoPanel().setVisible(true);
            panel.revalidate();
            list.setTopVisibleRow(row);
        }
    }

}
