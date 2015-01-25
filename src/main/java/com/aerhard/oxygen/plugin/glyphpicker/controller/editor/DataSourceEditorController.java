package com.aerhard.oxygen.plugin.glyphpicker.controller.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.view.editor.DataSourceEditor;
import com.jidesoft.swing.JideButton;

public class DataSourceEditorController {

    private static final Logger LOGGER = Logger
            .getLogger(DataSourceEditorController.class.getName());

    private DataSourceEditor contentPane;
    private JPanel parentPanel;

    private List<String> displayModes = new ArrayList<String>();

    private NewAction newAction = new NewAction();
    private CloneAction cloneAction = new CloneAction();
    private DeleteAction deleteAction = new DeleteAction();

    private boolean listEditingOccurred = false;

    private DefaultListModel<DataSource> listModel;
    private DataSource currentDataSource = null;

    public DataSourceEditorController(DataSourceEditor contentPane,
            JPanel parentPanel) {

        this.contentPane = contentPane;
        this.parentPanel = parentPanel;

        contentPane.getListButtonPane().add(new JideButton(newAction));
        contentPane.getListButtonPane().add(new JideButton(cloneAction));
        contentPane.getListButtonPane().add(new JideButton(deleteAction));

        displayModes.add(DataSource.DISPLAY_MODE_VECTOR_PROPORTIONAL);
        displayModes.add(DataSource.DISPLAY_MODE_VECTOR_FIT);
        displayModes.add(DataSource.DISPLAY_MODE_BITMAP);

        contentPane.setFormEnabled(false);

        for (int i = 0; i < displayModes.size(); i++) {
            contentPane.getDisplayModeTextField().addItem(displayModes.get(i));
        }
        contentPane.getDisplayModeTextField().setSelectedItem(null);
    }

    private final class NewAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private NewAction() {
            super("New");
            putValue(SHORT_DESCRIPTION, "Create a new data source.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_N));
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
            listEditingOccurred = true;
        }
    }

    private final class CloneAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private CloneAction() {
            super("Clone");
            setEnabled(false);
            putValue(SHORT_DESCRIPTION, "Clone the selected data source.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_C));
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
                listEditingOccurred = true;
            } catch (CloneNotSupportedException e1) {
                LOGGER.error(e1);
            }
        }
    }

    private final class DeleteAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private DeleteAction() {
            super("Delete");
            setEnabled(false);
            putValue(SHORT_DESCRIPTION, "Delete the selected data source.");
            putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_D));
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
            listEditingOccurred = true;

        }
    }

    public List<DataSource> load(List<DataSource> dataSourceList) {

        listModel = new DefaultListModel<DataSource>();

        try {
            for (DataSource dataSource : dataSourceList) {
                listModel.addElement(dataSource.clone());
            }
        } catch (CloneNotSupportedException e) {
            LOGGER.error(e);
        }

        contentPane.getList().setModel(listModel);
        contentPane.getList().setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        if (listModel.size() > 0) {
            contentPane.getList().setSelectedIndex(0);
            onListSelection();
        }
        
        contentPane.getList().getSelectionModel()
                .addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            onListSelection();
                        }
                    }
                });

        int result = JOptionPane.showConfirmDialog(parentPanel, contentPane,
                "Data Source Editor", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION && listEditingOccurred) {
            LOGGER.info("List editing occurred.");
            List<DataSource> resultList = new ArrayList<DataSource>();
            for (int i = 0; i < listModel.getSize(); i++) {
                resultList.add(listModel.getElementAt(i));
            }
            return resultList;
        } else {
            return null;
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

        contentPane.removePropertyChangeListener(formListener);

        contentPane.getLabelTextField().setText(dataSource.getLabel());
        contentPane.getPathTextField().setText(dataSource.getBasePath());
        contentPane.getFontNameTextField().setText(dataSource.getFontName());

        int index = displayModes.indexOf(dataSource.getDisplayMode());
        contentPane.getDisplayModeTextField().setSelectedIndex(index);

        Integer sizeFactor = Math.round(dataSource.getSizeFactor() * 100);
        String sizeFactorString;
        try {
            sizeFactorString = sizeFactor.toString();
        } catch (Exception e) {
            sizeFactorString = null;
        }

        contentPane.getSizeTextField().setText(sizeFactorString);
        contentPane.getTemplateTextField().setText(dataSource.getTemplate());
        contentPane.getMappingAttNameTextField().setText(
                dataSource.getMappingAttName());
        contentPane.getMappingAttValueTextField().setText(
                dataSource.getMappingAttValue());

        contentPane.addPropertyChangeListener(formListener);
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
                        .getSizeTextField().getText() + "f") / 100f;
                currentDataSource.setSizeFactor(sizeFactor);
            } catch (Exception e) {
                LOGGER.info("Error converting size factor "
                        + contentPane.getSizeTextField().getText());
            }

            currentDataSource.setTemplate(contentPane.getTemplateTextField()
                    .getText());
            currentDataSource.setMappingAttName(contentPane
                    .getMappingAttNameTextField().getText());
            currentDataSource.setMappingAttValue(contentPane
                    .getMappingAttValueTextField().getText());
        }
    }

    private PropertyChangeListener formListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if (e.getPropertyName() == DataSourceEditor.EDITING_OCCURRED) {
                updateCurrentModelFromForm();
                listEditingOccurred = true;
            }
        }
    };

}
