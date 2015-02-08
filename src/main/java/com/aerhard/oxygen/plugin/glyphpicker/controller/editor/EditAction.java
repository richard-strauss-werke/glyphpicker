/**
 * Copyright 2015 Alexander Erhard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aerhard.oxygen.plugin.glyphpicker.controller.editor;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JPanel;

import com.aerhard.oxygen.plugin.glyphpicker.controller.action.AbstractPickerAction;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSourceList;
import com.aerhard.oxygen.plugin.glyphpicker.view.editor.DataSourceEditor;

/**
 * An action to open the data source editor and process the results if editing
 * has occurred.
 */
public class EditAction extends AbstractPickerAction {

    private static final long serialVersionUID = 1L;

    /** The key of the action. */
    public static final String KEY = "changesInEditor";

    /** The panel from which the data source editor has been opened. */
    private final JPanel panel;

    /** The original data source list. */
    private final DataSourceList dataSourceList;

    /** The name of the class. */
    private static final String CLASS_NAME = EditAction.class.getSimpleName();

    /**
     * Instantiates a new EditAction.
     *
     * @param listener
     *            the property change listener to be added to this action
     * @param panel
     *            The panel from which the data source editor has been opened
     * @param dataSourceList
     *            The original data source list
     */
    public EditAction(PropertyChangeListener listener, JPanel panel,
            DataSourceList dataSourceList) {
        super(CLASS_NAME, "/images/database--pencil.png");
        addPropertyChangeListener(listener);
        this.panel = panel;
        this.dataSourceList = dataSourceList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
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
            firePropertyChange(KEY, null, null);
        }
    }
}
