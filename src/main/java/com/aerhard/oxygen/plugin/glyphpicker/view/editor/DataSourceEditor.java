package com.aerhard.oxygen.plugin.glyphpicker.view.editor;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import java.awt.Component;

import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DataSourceEditor extends JPanel {

    public static final String EDITING_OCCURRED = "editingOccurred";

    private final static int PREFERRED_WIDTH = 600;
    private final static int PREFERRED_HEIGHT = 400;

    private JList<DataSource> list;

    private static final long serialVersionUID = 1L;
    private JTextField labelTextField;
    private JTextField pathTextField;
    private JTextField fontNameTextField;
    private JComboBox<String> displayModeTextField;
    private JTextField templateTextField;
    private JTextField mappingAttNameTextField;
    private JTextField mappingAttValueTextField;
    private JPanel listPanel;
    private JPanel listButtonPane;
    private JPanel editorPanel;

    private class EditorConfigItem {
        private JComponent component;
        String label;

        private EditorConfigItem(String label, JComponent component) {
            this.component = component;
            this.label = label;
        }

        public JComponent getComponent() {
            return component;
        }

        public String getLabel() {
            return label;
        }
    }

    private List<EditorConfigItem> editorConfig = new ArrayList<EditorConfigItem>();

    public DataSourceEditor() {

        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

        setLayout(new BorderLayout(8, 0));

        listPanel = new JPanel();
        listPanel.setBorder(new CompoundBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Data Sources",
                TitledBorder.LEADING, TitledBorder.TOP, null,
                new Color(0, 0, 0)), new EmptyBorder(8, 8, 8, 8)));
        add(listPanel, BorderLayout.WEST);
        listPanel.setLayout(new BorderLayout(0, 0));

        listButtonPane = new JPanel();
        listPanel.add(listButtonPane, BorderLayout.SOUTH);
        listButtonPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        list = new JList<DataSource>();
        JScrollPane scrollPane = new JScrollPane(list);
        listPanel.add(scrollPane);

        editorPanel = new JPanel();
        editorPanel.setBorder(new CompoundBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Edit",
                TitledBorder.LEADING, TitledBorder.TOP, null,
                new Color(0, 0, 0)), new EmptyBorder(8, 8, 8, 8)));
        add(editorPanel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 102, 46 };
        gbl_panel.rowHeights = new int[] { 20, 0, 0, 0, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0 };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, Double.MIN_VALUE };
        editorPanel.setLayout(gbl_panel);

        labelTextField = new JTextField();
        pathTextField = new JTextField();
        fontNameTextField = new JTextField();
        displayModeTextField = new JComboBox<String>();
        templateTextField = new JTextField();
        mappingAttNameTextField = new JTextField();
        mappingAttValueTextField = new JTextField();

        editorConfig.add(new EditorConfigItem("Label", labelTextField));
        editorConfig.add(new EditorConfigItem("Path", pathTextField));
        editorConfig.add(new EditorConfigItem("Font Name", fontNameTextField));
        editorConfig.add(new EditorConfigItem("Display Mode",
                displayModeTextField));
        editorConfig.add(new EditorConfigItem("Template", templateTextField));
        editorConfig.add(new EditorConfigItem("Mapping Attribute Name",
                mappingAttNameTextField));
        editorConfig.add(new EditorConfigItem("Mapping Attribute Value",
                mappingAttValueTextField));

        for (int i = 0; i < editorConfig.size(); i++) {
            addToEditorPanel(i, editorConfig.get(i));
        }

    }

    public void setFormEnabled(boolean enabled) {
        for (EditorConfigItem eci : editorConfig) {
            eci.getComponent().setEnabled(enabled);
        }
    }
    
    private DocumentListener textFieldEditingListener = new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {
            firePropertyChange(EDITING_OCCURRED, null, null);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            firePropertyChange(EDITING_OCCURRED, null, null);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            firePropertyChange(EDITING_OCCURRED, null, null);
        }
    };

    private ActionListener comboChangeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            firePropertyChange(EDITING_OCCURRED, null, null);
        }
    };
    
    private void addToEditorPanel(int index, EditorConfigItem eci) {
        JLabel labelLabel = new JLabel(eci.getLabel());
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.anchor = GridBagConstraints.EAST;
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.gridx = 0;
        gbc_label.gridy = index;
        editorPanel.add(labelLabel, gbc_label);

        JComponent component = eci.getComponent();

        if (component instanceof JTextField) {
            ((JTextField) component).setColumns(10);
            ((JTextField) component).getDocument().addDocumentListener(
                    textFieldEditingListener);
        } else if (component instanceof JComboBox) {
            ((JComboBox<?>) component).addActionListener(comboChangeListener);
        }
        
        GridBagConstraints gbc_component = new GridBagConstraints();
        gbc_component.insets = new Insets(0, 0, 5, 0);
        gbc_component.fill = GridBagConstraints.HORIZONTAL;
        gbc_component.anchor = GridBagConstraints.NORTHWEST;
        gbc_component.gridx = 1;
        gbc_component.gridy = index;
        editorPanel.add(component, gbc_component);
    }

    public JList<DataSource> getList() {
        return list;
    }

    public JTextField getLabelTextField() {
        return labelTextField;
    }

    public JTextField getPathTextField() {
        return pathTextField;
    }

    public JTextField getFontNameTextField() {
        return fontNameTextField;
    }

    public JComboBox<String> getDisplayModeTextField() {
        return displayModeTextField;
    }

    public JTextField getTemplateTextField() {
        return templateTextField;
    }

    public JTextField getMappingAttNameTextField() {
        return mappingAttNameTextField;
    }

    public JTextField getMappingAttValueTextField() {
        return mappingAttValueTextField;
    }

    public JPanel getListButtonPane() {
        return listButtonPane;
    }

}
