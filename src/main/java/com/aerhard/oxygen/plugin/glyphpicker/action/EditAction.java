package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ResourceBundle;

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
    private JPanel panel;
    private DataSourceList dataSourceList;

    public EditAction(PropertyChangeListener listener, JPanel panel, DataSourceList dataSourceList) {
        super(null, new ImageIcon(
                EditAction.class.getResource("/images/gear.png")));
        
        addPropertyChangeListener(listener);
        this.panel = panel;
        this.dataSourceList = dataSourceList;
        
        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
        String className = this.getClass().getSimpleName();
        String description = i18n.getString(className + ".description");
        String mnemonic = i18n.getString(className + ".mnemonic");
        
        putValue(SHORT_DESCRIPTION,
                description + " (Alt+"+mnemonic+")");
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
            firePropertyChange("editChanges", null, null);
        }
    }
}
