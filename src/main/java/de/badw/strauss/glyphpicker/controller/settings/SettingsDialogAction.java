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
package de.badw.strauss.glyphpicker.controller.settings;

import de.badw.strauss.glyphpicker.controller.action.AbstractPickerAction;
import de.badw.strauss.glyphpicker.controller.bitmap.ImageCache;
import de.badw.strauss.glyphpicker.model.Config;
import de.badw.strauss.glyphpicker.model.DataSource;
import de.badw.strauss.glyphpicker.model.DataSourceList;
import de.badw.strauss.glyphpicker.view.settings.DataSourceEditor;
import de.badw.strauss.glyphpicker.view.settings.OptionsEditor;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * An action to open the options dialog and process the results if editing
 * has occurred.
 */
public class SettingsDialogAction extends AbstractPickerAction implements PropertyChangeListener {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(SettingsDialogAction.class.getName());
    
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
    private final DataSourceList originalDataSourceList;
    private OptionsEditor optionsEditor;
    private DataSourceEditorController dataSourceEditorController;
    private ApplyAction applyAction;

    /**
     * Instantiates a new SettingsDialogAction.
     *
     * @param parentPanel    The panel from which the data source editor has been opened
     * @param listener       the property change listener to be added to this action
     * @param config         The plugin's config
     * @param imageCache     the ImageCache object
     * @param originalDataSourceList The original data source list
     */
    public SettingsDialogAction(JPanel parentPanel, PropertyChangeListener listener, Config config, ImageCache imageCache,
                                DataSourceList originalDataSourceList) {
        super(CLASS_NAME, "/images/oxygen/OptionsShortcut16_centered.png", "ctrl E");
        addPropertyChangeListener(listener);
        this.parentPanel = parentPanel;
        this.imageCache = imageCache;
        this.config = config;
        this.originalDataSourceList = originalDataSourceList;
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

        firstOriginalListItemClone = getFirstOriginalListItemClone();
        
        DataSourceEditor dataSourceEditor = new DataSourceEditor();
         optionsEditor = new OptionsEditor();

        applyAction = new ApplyAction();
        
        dataSourceEditorController = new DataSourceEditorController(
                dataSourceEditor, this);
        PluginOptionsEditorController pluginOptionsEditorController = new PluginOptionsEditorController(
                optionsEditor, config, imageCache, applyAction);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(I18N.getString("SettingsDialogAction.tables"), dataSourceEditor);
        tabbedPane.add(I18N.getString("SettingsDialogAction.plugin"), optionsEditor);

        dataSourceEditorController.initList(createDataSourceListModel());
        pluginOptionsEditorController.setImageCacheListener();

        boolean dialogSubmitted = showDialog(tabbedPane, applyAction);

        if (dialogSubmitted) {
            applyDialogResults();
        }

        if (firstOriginalListItemClone == null || originalDataSourceList.getData().size() == 0 ||
                !firstOriginalListItemClone.equals(originalDataSourceList.getData().get(0))) {
            firePropertyChange(EDITING_OCCURRED, null, null);
        }

        // clean up
        pluginOptionsEditorController.removeImageCacheListener();
        dataSourceEditorController = null;
        optionsEditor = null;
        firstOriginalListItemClone = null;
    }

    /**
     * a clone of the first data source in the original list
     */
    private DataSource firstOriginalListItemClone;

    /**
     * gets a clone of the first item in the original list or null if there is no item or an error occurred
     */
    private DataSource getFirstOriginalListItemClone() {
        List<DataSource> data = originalDataSourceList.getData();
        if (data.size() > 0) {
            try {
                return data.get(0).clone();
            } catch (CloneNotSupportedException e) {
                LOGGER.error(e);
            }
        }
        return null;
    }
    
    
    /**
    * Creates the list model for the data source editor list by cloning the
    * items in the original data source list
    * @return the list model
     */
    private DefaultListModel<DataSource> createDataSourceListModel() {
        DefaultListModel<DataSource> listModel = new DefaultListModel<DataSource>();
        try {
            for (DataSource dataSource : originalDataSourceList.getData()) {
                listModel.addElement(dataSource.clone());
            }
        } catch (CloneNotSupportedException e) {
            LOGGER.error(e);
        }
        return listModel;
    }

    /**
     * updates the original data from the dialog 
     */
    private void applyDialogResults() {

        List<DataSource> resultList = dataSourceEditorController.getList();

        originalDataSourceList.getData().clear();
        originalDataSourceList.getData().addAll(resultList);
        if (originalDataSourceList.getSize() > 0) {
            originalDataSourceList.setSelectedItem(originalDataSourceList.getElementAt(0));
        }

        config.setShortcut(optionsEditor.getShortcutTextField().getText());
        config.setTransferFocusAfterInsert(optionsEditor.getTransferFocusCheckBox().isSelected());
    }
    
    
    private boolean showDialog(JComponent contentPane, Action applyAction) {

        JButton okBtn = new JButton(I18N.getString("SettingsDialogAction.ok"));
        JButton cancelBtn = new JButton(I18N.getString("SettingsDialogAction.cancel"));
        JButton applyBtn = new JButton(applyAction);

        final JOptionPane pane = new JOptionPane(contentPane, JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,  null,
                new JButton[] {okBtn, cancelBtn, applyBtn}, okBtn);

        pane.setInitialValue(okBtn);
        pane.setComponentOrientation(JOptionPane.getRootFrame().getComponentOrientation());

        JDialog dialog = pane.createDialog(I18N.getString("SettingsDialogAction.label"));
        pane.selectInitialValue();

        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pane.setValue(JOptionPane.OK_OPTION);
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pane.setValue(JOptionPane.CANCEL_OPTION);
            }
        });

        dialog.setVisible(true);
        dialog.dispose();
        return (pane.getValue() instanceof Integer && 
                (Integer) pane.getValue() == JOptionPane.OK_OPTION);
    }

    /* (non-Javadoc)
 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
 */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (DataSourceEditor.FORM_CHANGED.equals(e.getPropertyName())) {
            dataSourceEditorController.updateCurrentListModelFromForm();
            applyAction.setEnabled(true);
        } else if (DataSourceEditorController.LIST_CHANGED.equals(e.getPropertyName())) {
            applyAction.setEnabled(true);
        }
    }

    private class ApplyAction extends AbstractAction {

        private ApplyAction(){
            super(I18N.getString("SettingsDialogAction.apply"));
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            applyDialogResults();
            setEnabled(false);
        }
    }
    
}
