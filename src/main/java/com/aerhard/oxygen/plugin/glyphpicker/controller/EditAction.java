package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.aerhard.oxygen.plugin.glyphpicker.controller.editor.DataSourceEditorController;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSourceList;
import com.aerhard.oxygen.plugin.glyphpicker.view.editor.DataSourceEditor;

public class EditAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private Controller controller;
    private JPanel panel;
    private DataSourceList dataSourceList;

    public EditAction(Controller controller, JPanel panel, DataSourceList dataSourceList) {
        super(null, new ImageIcon(
                BrowserController.class.getResource("/images/gear.png")));
        
        this.controller = controller;
        this.panel = panel;
        this.dataSourceList = dataSourceList;
        
        String mnemonic = "E";
        
        putValue(SHORT_DESCRIPTION, "Edit data sources (Alt+"+mnemonic+")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<DataSource> result = new DataSourceEditorController(
                new DataSourceEditor(), panel).load(dataSourceList
                .getData());

        if (result != null) {
            dataSourceList.getData().clear();
            dataSourceList.getData().addAll(result);
            if (dataSourceList.getSize() > 0) {
                dataSourceList.setSelectedItem(dataSourceList
                        .getElementAt(0));
            }
            controller.loadData();
        }
    }
}
