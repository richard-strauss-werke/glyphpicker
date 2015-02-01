package com.aerhard.oxygen.plugin.glyphpicker.controller.editor;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.aerhard.oxygen.plugin.glyphpicker.controller.action.AbstractPickerAction;
import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.view.editor.DataSourceEditor;
import com.jidesoft.swing.JideButton;

public class DataSourceEditorController implements PropertyChangeListener {

    private static final float PERCENTAGE_FACTOR = 100f;

    private static final Logger LOGGER = Logger
            .getLogger(DataSourceEditorController.class.getName());

    public static final String LIST_EDITING_OCCURRED = "listEditingOccurred";

    private final DataSourceEditor contentPane;
    private final JPanel parentPanel;

    private final List<String> displayModes = new ArrayList<>();

    private final CloneAction cloneAction;
    private final DeleteAction deleteAction;

    private boolean listEditingOccurred = false;

    private DefaultListModel<DataSource> listModel;
    private DataSource currentDataSource = null;

    private final ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");

    public DataSourceEditorController(DataSourceEditor contentPane,
            JPanel parentPanel) {

        cloneAction = new CloneAction(this);
        deleteAction = new DeleteAction(this);

        this.contentPane = contentPane;
        this.parentPanel = parentPanel;

        contentPane.getListButtonPane().add(new JideButton(new NewAction(this)));
        contentPane.getListButtonPane().add(new JideButton(cloneAction));
        contentPane.getListButtonPane().add(new JideButton(deleteAction));

        displayModes.add(DataSource.DISPLAY_MODE_VECTOR_PROPORTIONAL);
        displayModes.add(DataSource.DISPLAY_MODE_VECTOR_FIT);
        displayModes.add(DataSource.DISPLAY_MODE_BITMAP);

        contentPane.setFormEnabled(false);

        for (String displayMode : displayModes) {
            contentPane.getDisplayModeTextField().addItem(displayMode);
        }
        contentPane.getDisplayModeTextField().setSelectedItem(null);
    }

    private final class NewAction extends AbstractPickerAction {
        private static final long serialVersionUID = 1L;

        private NewAction(PropertyChangeListener listener) {
            super(NewAction.class.getSimpleName());
            addPropertyChangeListener(listener);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DataSource dataSource = new DataSource();
            dataSource.setLabel("new data source");
            listModel.addElement(dataSource);
            contentPane
                    .getList()
                    .getSelectionModel()
                    .setSelectionInterval(listModel.size() - 1,
                            listModel.size() - 1);
            firePropertyChange(LIST_EDITING_OCCURRED, null, null);
        }
    }

    private final class CloneAction extends AbstractPickerAction {
        private static final long serialVersionUID = 1L;

        private CloneAction(PropertyChangeListener listener) {
            super(CloneAction.class.getSimpleName());
            addPropertyChangeListener(listener);
            setEnabled(false);
        }

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

    private final class DeleteAction extends AbstractPickerAction {
        private static final long serialVersionUID = 1L;

        private DeleteAction(PropertyChangeListener listener) {
            super(DeleteAction.class.getSimpleName());
            addPropertyChangeListener(listener);
            setEnabled(false);
        }

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

    public List<DataSource> load(List<DataSource> dataSourceList) {

        initListModel(dataSourceList);
        initListComponent(contentPane.getList());

        int result = JOptionPane.showConfirmDialog(parentPanel, contentPane,
                i18n.getString("DataSourceEditorController.frameTitle"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION && listEditingOccurred) {
            List<DataSource> resultList = new ArrayList<>();
            for (int i = 0; i < listModel.getSize(); i++) {
                resultList.add(listModel.getElementAt(i));
            }
            return resultList;
        } else {
            return null;
        }
    }

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

    private class DataSourceListSelectionListener implements
            ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                onListSelection();
            }
        }
    }

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

    private void setFormValues(DataSource dataSource) {

        contentPane.removePropertyChangeListener(this);

        contentPane.getLabelTextField().setText(dataSource.getLabel());
        contentPane.getPathTextField().setText(dataSource.getBasePath());
        contentPane.getFontNameTextField().setText(dataSource.getFontName());

        int index = displayModes.indexOf(dataSource.getDisplayMode());
        contentPane.getDisplayModeTextField().setSelectedIndex(index);

        Integer sizeFactor = Math.round(dataSource.getSizeFactor() * PERCENTAGE_FACTOR);
        String sizeFactorString;
        try {
            sizeFactorString = sizeFactor.toString();
        } catch (Exception e) {
            sizeFactorString = null;
        }

        contentPane.getSizeTextField().setText(sizeFactorString);
        contentPane.getTemplateTextField().setText(dataSource.getTemplate());
        contentPane.getMappingAttNameTextField().setText(
                dataSource.getMappingTypeValue());
        contentPane.getMappingAttValueTextField().setText(
                dataSource.getMappingSubTypeValue());
        contentPane.getMappingAsCharStringCheckBox().setSelected(
                dataSource.getMappingAsCharString());

        contentPane.addPropertyChangeListener(this);
    }

    private void updateCurrentModelFromForm() {
        if (currentDataSource != null) {
            currentDataSource.setLabel(contentPane.getLabelTextField()
                    .getText());
            currentDataSource.setBasePath(contentPane.getPathTextField()
                    .getText());
            currentDataSource.setFontName(contentPane.getFontNameTextField()
                    .getText());
            currentDataSource.setDisplayMode(displayModes.get(contentPane
                    .getDisplayModeTextField().getSelectedIndex()));

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
            currentDataSource.setMappingTypeValue(contentPane
                    .getMappingAttNameTextField().getText());
            currentDataSource.setMappingSubTypeValue(contentPane
                    .getMappingAttValueTextField().getText());
            currentDataSource.setMappingAsCharString(contentPane
                    .getMappingAsCharStringCheckBox().isSelected());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

        if (DataSourceEditor.FORM_EDITING_OCCURRED.equals(e.getPropertyName())) {
            updateCurrentModelFromForm();
            listEditingOccurred = true;
        }

        else if (LIST_EDITING_OCCURRED.equals(e.getPropertyName())) {
            listEditingOccurred = true;
        }

    }

}
