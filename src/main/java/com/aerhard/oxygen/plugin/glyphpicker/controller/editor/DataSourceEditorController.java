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

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.view.editor.DataSourceEditor;

public class DataSourceEditorController {

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

        contentPane.getListButtonPane().add(new JButton(newAction));
        contentPane.getListButtonPane().add(new JButton(cloneAction));
        contentPane.getListButtonPane().add(new JButton(deleteAction));

        displayModes.add(DataSource.DISPLAY_MODE_VECTOR_PROPORTIONAL);
        displayModes.add(DataSource.DISPLAY_MODE_VECTOR_FIT);
        displayModes.add(DataSource.DISPLAY_MODE_BITMAP);

        contentPane.setFormEnabled(false);

        for (int i = 0; i < displayModes.size(); i++) {
            contentPane.getDisplayModeTextField().addItem(displayModes.get(i));
        }
        contentPane.getDisplayModeTextField().setSelectedItem(null);
    }

    private class NewAction extends AbstractAction {
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

    private class CloneAction extends AbstractAction {
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
            DataSource dataSource = listModel.get(index).clone();
            listModel.addElement(dataSource);
            contentPane
                    .getList()
                    .getSelectionModel()
                    .setSelectionInterval(listModel.size() - 1,
                            listModel.size() - 1);
            listEditingOccurred = true;
        }
    }

    private class DeleteAction extends AbstractAction {
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

        for (DataSource dataSource : dataSourceList) {
            listModel.addElement(dataSource.clone());
        }

        contentPane.getList().setModel(listModel);
        contentPane.getList().setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        contentPane.getList().getSelectionModel()
                .addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {

                            if (contentPane.getList().getSelectionModel()
                                    .isSelectionEmpty()) {
                                currentDataSource = null;
                                setFormValues(new DataSource());
                                contentPane.setFormEnabled(false);
                                cloneAction.setEnabled(false);
                                deleteAction.setEnabled(false);
                            } else {
                                int index = contentPane.getList()
                                        .getSelectionModel()
                                        .getAnchorSelectionIndex();
                                currentDataSource = listModel.get(index);
                                setFormValues(currentDataSource);
                                contentPane.setFormEnabled(true);
                                cloneAction.setEnabled(true);
                                deleteAction.setEnabled(true);
                            }
                        }
                    }
                });

        int result = JOptionPane.showConfirmDialog(parentPanel, contentPane,
                "Data Source Editor", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION && listEditingOccurred) {
            dataSourceList = new ArrayList<DataSource>();
            for (int i = 0; i < listModel.getSize(); i++) {
                dataSourceList.add(listModel.getElementAt(i));
            }
            return dataSourceList;
        } else {
            return null;
        }
    }

    private void setFormValues(DataSource dataSource) {

        contentPane.removePropertyChangeListener(formListener);

        contentPane.getLabelTextField().setText(dataSource.getLabel());
        contentPane.getPathTextField().setText(dataSource.getBasePath());
        contentPane.getFontNameTextField().setText(dataSource.getFontName());

        int index = displayModes.indexOf(dataSource.getDisplayMode());
        contentPane.getDisplayModeTextField().setSelectedIndex(index);

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
