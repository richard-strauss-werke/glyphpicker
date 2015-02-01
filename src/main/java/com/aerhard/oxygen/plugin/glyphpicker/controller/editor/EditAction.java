package com.aerhard.oxygen.plugin.glyphpicker.controller.editor;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JPanel;

import com.aerhard.oxygen.plugin.glyphpicker.controller.action.AbstractPickerAction;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSourceList;
import com.aerhard.oxygen.plugin.glyphpicker.view.editor.DataSourceEditor;

public class EditAction extends AbstractPickerAction {
    private static final long serialVersionUID = 1L;
    private final JPanel panel;
    private final DataSourceList dataSourceList;

    private static final String className = EditAction.class.getSimpleName();

    public EditAction(PropertyChangeListener listener, JPanel panel,
            DataSourceList dataSourceList) {
        super(className, "/images/gear.png");
        addPropertyChangeListener(listener);
        this.panel = panel;
        this.dataSourceList = dataSourceList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<DataSource> result = new DataSourceEditorController(
                new DataSourceEditor(), panel).load(dataSourceList.getData());

        if (result != null) {
            dataSourceList.getData().clear();
            dataSourceList.getData().addAll(result);
            if (dataSourceList.getSize() > 0) {
                dataSourceList.setSelectedItem(dataSourceList.getElementAt(0));
            }
            firePropertyChange("editChanges", null, null);
        }
    }
}
