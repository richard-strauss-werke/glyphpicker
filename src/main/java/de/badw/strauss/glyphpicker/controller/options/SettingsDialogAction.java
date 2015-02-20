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
package de.badw.strauss.glyphpicker.controller.options;

import de.badw.strauss.glyphpicker.controller.action.AbstractPickerAction;
import de.badw.strauss.glyphpicker.controller.bitmap.ImageCache;
import de.badw.strauss.glyphpicker.model.Config;
import de.badw.strauss.glyphpicker.model.GlyphTable;
import de.badw.strauss.glyphpicker.model.GlyphTableList;
import de.badw.strauss.glyphpicker.view.options.DataSourceEditor;
import de.badw.strauss.glyphpicker.view.options.OptionsEditor;
import ro.sync.ui.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * An action to open the options dialog and process the results if editing
 * has occurred.
 */
public class SettingsDialogAction extends AbstractPickerAction {

    /**
     * The key of the "editing occurred" event;
     */
    public static final String EDITING_OCCURRED = "editingOccurred";
    private static final long serialVersionUID = 1L;
    /**
     * The name of the class.
     */
    private static final String CLASS_NAME = SettingsDialogAction.class.getSimpleName();
    /**
     * The panel from which the data source editor has been opened.
     */
    private final JPanel parentPanel;
    /**
     * the ImageCache object
     */
    private final ImageCache imageCache;

    /**
     * The plugin's config
     */
    private final Config config;

    /**
     * The original data source list.
     */
    private final GlyphTableList glyphTableList;

    /**
     * Instantiates a new SettingsDialogAction.
     *
     * @param parentPanel    The panel from which the data source editor has been opened
     * @param listener       the property change listener to be added to this action
     * @param config         The plugin's config
     * @param imageCache     the ImageCache object
     * @param glyphTableList The original data source list
     */
    public SettingsDialogAction(JPanel parentPanel, PropertyChangeListener listener, Config config, ImageCache imageCache,
                                GlyphTableList glyphTableList) {
        super(CLASS_NAME, Icons.OPTIONS_SHORTCUT_CENTERED, "ctrl P");
        addPropertyChangeListener(listener);
        this.parentPanel = parentPanel;
        this.imageCache = imageCache;
        this.config = config;
        this.glyphTableList = glyphTableList;
        bindAcceleratorToComponent(this, parentPanel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        DataSourceEditor dataSourceEditor = new DataSourceEditor();
        OptionsEditor optionsEditor = new OptionsEditor();

        DataSourceEditorController dataSourceEditorController = new DataSourceEditorController(
                dataSourceEditor);
        PluginOptionsEditorController pluginOptionsEditorController = new PluginOptionsEditorController(
                optionsEditor, config, imageCache);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(I18N.getString("SettingsDialogAction.tables"), dataSourceEditor);
        tabbedPane.add(I18N.getString("SettingsDialogAction.plugin"), optionsEditor);

        dataSourceEditorController.initList(glyphTableList.getData());
        pluginOptionsEditorController.setImageCacheListener();

        int dialogResult = JOptionPane.showConfirmDialog(parentPanel, tabbedPane,
                I18N.getString("SettingsDialogAction.label"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        pluginOptionsEditorController.removeImageCacheListener();


        List<GlyphTable> resultList;
        if (dialogResult == JOptionPane.OK_OPTION) {
            resultList = dataSourceEditorController.getEditingResults();
        } else {
            resultList = null;
        }

        if (resultList != null) {
            glyphTableList.getData().clear();
            glyphTableList.getData().addAll(resultList);
            if (glyphTableList.getSize() > 0) {
                glyphTableList.setSelectedItem(glyphTableList.getElementAt(0));
            }
            firePropertyChange(EDITING_OCCURRED, null, null);
        }


    }
}
