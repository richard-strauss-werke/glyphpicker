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

import com.jidesoft.swing.JideButton;
import de.badw.strauss.glyphpicker.controller.action.AbstractPickerAction;
import de.badw.strauss.glyphpicker.model.DataSource;
import de.badw.strauss.glyphpicker.view.settings.DataSourceEditor;
import org.apache.log4j.Logger;
import ro.sync.ui.Icons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The controller of the data source editor window.
 */
public class DataSourceEditorController implements PropertyChangeListener {

    /**
     * The name of the "listEditingOccurred" change property.
     */
    public static final String LIST_EDITING_OCCURRED = "listEditingOccurred";
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(DataSourceEditorController.class.getName());
    /**
     * The percentage factor.
     */
    private static final float PERCENTAGE_FACTOR = 100f;
    /**
     * The window's content pane.
     */
    private final DataSourceEditor contentPane;

    /**
     * The display modes added to the display mode JCombo.
     */
    private final List<String> glyphRendererLabels = new ArrayList<>();

    /**
     * The clone action.
     */
    private final CloneAction cloneAction;

    /**
     * The delete action.
     */
    private final DeleteAction deleteAction;
    /**
     * The i18n resource bundle.
     */
    private final ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
    /**
     * Indicates if there have been changes to the data source list.
     */
    private boolean listEditingOccurred = false;
    /**
     * The list model.
     */
    private DefaultListModel<DataSource> listModel;
    /**
     * The current data source.
     */
    private DataSource currentDataSource = null;

    /**
     * Instantiates a new DataSourceEditorController.
     *
     * @param contentPane The window's content pane
     */
    public DataSourceEditorController(DataSourceEditor contentPane) {

        cloneAction = new CloneAction(this);
        deleteAction = new DeleteAction(this);

        this.contentPane = contentPane;

        JideButton addButton = new JideButton(new NewAction(this));
        addButton.setHideActionText(true);
        contentPane.getListButtonPane().add(addButton);

        JideButton cloneButton = new JideButton(cloneAction);
        cloneButton.setHideActionText(true);
        contentPane.getListButtonPane().add(cloneButton);

        JideButton deleteButton = new JideButton(deleteAction);
        deleteButton.setHideActionText(true);
        contentPane.getListButtonPane().add(deleteButton);

        glyphRendererLabels.add(DataSource.GLYPH_BITMAP_RENDERER);
        glyphRendererLabels.add(DataSource.GLYPH_VECTOR_RENDERER);
        glyphRendererLabels.add(DataSource.GLYPH_SCALED_VECTOR_RENDERER);

        contentPane.setFormEnabled(false);

        for (String glyphRendererLabel : glyphRendererLabels) {
            contentPane.getGlyphRendererCombo().addItem(glyphRendererLabel);
        }
        contentPane.getGlyphRendererCombo().setSelectedItem(null);
    }

    /**
     * initializes the list model and list component
     *
     * @param dataSourceList the data source list
     */
    public void initList(List<DataSource> dataSourceList) {
        initListModel(dataSourceList);
        initListComponent(contentPane.getList());
    }

    /**
     * Initializes the list component.
     *
     * @param list the list component
     */
    private void initListComponent(JList<DataSource> list) {
        list.setModel(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (listModel.size() > 0) {
            contentPane.getList().setSelectedIndex(0);
            onListSelection();
        }
        list.getSelectionModel().addListSelectionListener(
                new DataSourceListSelectionListener());
    }

    /**
     * Initializes the list model.
     *
     * @param dataSourceList the data source list
     */
    private void initListModel(List<DataSource> dataSourceList) {
        listModel = new DefaultListModel<>();

        try {
            for (DataSource dataSource : dataSourceList) {
                listModel.addElement(dataSource.clone());
            }
        } catch (CloneNotSupportedException e) {
            LOGGER.error(e);
        }
    }

    /**
     * The data source list selection event handler.
     */
    private void onListSelection() {
        if (contentPane.getList().getSelectionModel().isSelectionEmpty()) {
            currentDataSource = null;
            setFormValues(new DataSource());
            contentPane.setFormEnabled(false);
            cloneAction.setEnabled(false);
            deleteAction.setEnabled(false);
        } else {
            int index = contentPane.getList().getSelectionModel()
                    .getAnchorSelectionIndex();
            currentDataSource = listModel.get(index);
            setFormValues(currentDataSource);
            contentPane.setFormEnabled(true);
            cloneAction.setEnabled(true);
            deleteAction.setEnabled(true);
        }
    }

    /**
     * Sets form component's values.
     *
     * @param dataSource the form values
     */
    private void setFormValues(DataSource dataSource) {

        contentPane.removePropertyChangeListener(this);

        contentPane.getLabelTextField().setText(dataSource.getLabel());
        contentPane.getPathTextField().setText(dataSource.getBasePath());
        contentPane.getFontNameTextField().setText(dataSource.getFontName());

        int index = glyphRendererLabels.indexOf(dataSource.getGlyphRenderer());
        contentPane.getGlyphRendererCombo().setSelectedIndex(index);

        Integer sizeFactor = Math.round(dataSource.getSizeFactor() * PERCENTAGE_FACTOR);
        String sizeFactorString;
        try {
            sizeFactorString = sizeFactor.toString();
        } catch (Exception e) {
            sizeFactorString = null;
        }

        contentPane.getSizeTextField().setText(sizeFactorString);
        contentPane.getTemplateTextField().setText(dataSource.getTemplate());
        contentPane.getTypeAttributeTextField().setText(
                dataSource.getTypeAttributeValue());
        contentPane.getSubtypeAttributeTextField().setText(
                dataSource.getSubtypeAttributeValue());
        contentPane.getParseMappingCheckBox().setSelected(
                dataSource.getParseMapping());

        contentPane.addPropertyChangeListener(this);
    }

    /**
     * Updates data source model from the form's values.
     */
    private void updateCurrentModelFromForm() {
        if (currentDataSource != null) {
            currentDataSource.setLabel(contentPane.getLabelTextField()
                    .getText());
            currentDataSource.setBasePath(contentPane.getPathTextField()
                    .getText());
            currentDataSource.setFontName(contentPane.getFontNameTextField()
                    .getText());
            currentDataSource.setGlyphRenderer(glyphRendererLabels.get(contentPane
                    .getGlyphRendererCombo().getSelectedIndex()));

            try {
                float sizeFactor = Float.parseFloat(contentPane
                        .getSizeTextField().getText() + "f") / PERCENTAGE_FACTOR;
                currentDataSource.setSizeFactor(sizeFactor);
            } catch (Exception e) {
                LOGGER.info("Error converting size factor "
                        + contentPane.getSizeTextField().getText());
            }

            currentDataSource.setTemplate(contentPane.getTemplateTextField()
                    .getText());
            currentDataSource.setTypeAttributeValue(contentPane
                    .getTypeAttributeTextField().getText());
            currentDataSource.setSubtypeAttributeValue(contentPane
                    .getSubtypeAttributeTextField().getText());
            currentDataSource.setParseMapping(contentPane
                    .getParseMappingCheckBox().isSelected());
        }
    }

    /**
     * gets the editing results or null if no editing has occurred
     *
     * @return the results
     */
    public List<DataSource> getEditingResults() {
        if (listEditingOccurred) {
            List<DataSource> resultList = new ArrayList<>();
            for (int i = 0; i < listModel.getSize(); i++) {
                resultList.add(listModel.getElementAt(i));
            }
            return resultList;
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (DataSourceEditor.FORM_EDITING_OCCURRED.equals(e.getPropertyName())) {
            updateCurrentModelFromForm();
            listEditingOccurred = true;
        } else if (LIST_EDITING_OCCURRED.equals(e.getPropertyName())) {
            listEditingOccurred = true;
        }
    }

    /**
     * An action to create a new data source record.
     */
    private final class NewAction extends AbstractPickerAction {

        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new NewAction.
         *
         * @param listener the property change listener to be added to this action
         */
        private NewAction(PropertyChangeListener listener) {
            super(NewAction.class.getSimpleName(), "/images/new.png");
            addPropertyChangeListener(listener);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            DataSource dataSource = new DataSource();
            dataSource.setLabel(I18N.getString(NewAction.class.getSimpleName() + ".newDataSource"));
            listModel.addElement(dataSource);
            contentPane
                    .getList()
                    .getSelectionModel()
                    .setSelectionInterval(listModel.size() - 1,
                            listModel.size() - 1);
            firePropertyChange(LIST_EDITING_OCCURRED, null, null);
        }
    }

    /**
     * An action to clone the selected data source.
     */
    private final class CloneAction extends AbstractPickerAction {

        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new CloneAction.
         *
         * @param listener the property change listener to be added to this action
         */
        private CloneAction(PropertyChangeListener listener) {
            super(CloneAction.class.getSimpleName(), "/images/document-copy.png");
            addPropertyChangeListener(listener);
            setEnabled(false);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = contentPane.getList().getSelectionModel()
                    .getAnchorSelectionIndex();
            DataSource dataSource;
            try {
                dataSource = listModel.get(index).clone();
                listModel.addElement(dataSource);
                contentPane
                        .getList()
                        .getSelectionModel()
                        .setSelectionInterval(listModel.size() - 1,
                                listModel.size() - 1);
                firePropertyChange(LIST_EDITING_OCCURRED, null, null);
            } catch (CloneNotSupportedException e1) {
                LOGGER.error(e1);
            }
        }
    }

    /**
     * An action to delete the selected data source.
     */
    private final class DeleteAction extends AbstractPickerAction {

        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new DeleteAction.
         *
         * @param listener the property change listener to be added to this action
         */
        private DeleteAction(PropertyChangeListener listener) {
            super(DeleteAction.class.getSimpleName(), "/images/bin-metal.png");
            addPropertyChangeListener(listener);
            setEnabled(false);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = contentPane.getList().getSelectionModel()
                    .getAnchorSelectionIndex();
            if (index != -1) {
                listModel.removeElementAt(index);
                index = Math.min(index, listModel.size() - 1);
                if (index >= 0) {
                    contentPane.getList().getSelectionModel()
                            .setSelectionInterval(index, index);
                }
            }
            firePropertyChange(LIST_EDITING_OCCURRED, null, null);

        }
    }

    /**
     * The data source list selection event listener
     */
    private class DataSourceListSelectionListener implements
            ListSelectionListener {

        /* (non-Javadoc)
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                onListSelection();
            }
        }
    }

}
