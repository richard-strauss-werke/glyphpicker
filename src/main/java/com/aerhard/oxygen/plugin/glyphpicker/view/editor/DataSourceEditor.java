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

    private static final long serialVersionUID = 1L;
    
    public static final String EDITING_OCCURRED = "editingOccurred";

    private static final int PREFERRED_WIDTH = 600;
    private static final int PREFERRED_HEIGHT = 400;

    private JList<DataSource> list;

    private JTextField labelTextField;
    private JTextField pathTextField;
    private JTextField fontNameTextField;
    private JComboBox<String> displayModeTextField;
    private JTextField sizeTextField;
    private JTextField templateTextField;
    private JTextField mappingAttNameTextField;
    private JTextField mappingAttValueTextField;
    private JPanel listButtonPane;
    private JPanel editorPanel;

    private List<EditorConfigItem> editorConfig = new ArrayList<EditorConfigItem>();

    public DataSourceEditor() {

        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

        setLayout(new BorderLayout(8, 0));

        JPanel listPanel = new JPanel();
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
        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[] { 102, 46 };
        gbl.rowHeights = new int[] { 20, 0, 0, 0, 0, 0, 0, 0 };
        gbl.columnWeights = new double[] { 0.0, 1.0 };
        gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, Double.MIN_VALUE };
        editorPanel.setLayout(gbl);

        labelTextField = new JTextField();
        pathTextField = new JTextField();
        fontNameTextField = new JTextField();
        displayModeTextField = new JComboBox<String>();
        sizeTextField = new JTextField();
        templateTextField = new JTextField();
        mappingAttNameTextField = new JTextField();
        mappingAttValueTextField = new JTextField();

        editorConfig.add(new EditorConfigItem("Label", labelTextField));
        editorConfig.add(new EditorConfigItem("Path", pathTextField));
        editorConfig.add(new EditorConfigItem("Font Name", fontNameTextField));
        editorConfig.add(new EditorConfigItem("Display Mode",
                displayModeTextField));
        editorConfig
                .add(new EditorConfigItem("Glyph Size in %", sizeTextField));
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
    
    private class TextFieldEditingListener implements DocumentListener {

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

    private class ComboChangeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            firePropertyChange(EDITING_OCCURRED, null, null);
        }
    };

    private void addToEditorPanel(int index, EditorConfigItem eci) {
        JLabel label = new JLabel(eci.getLabel());
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.anchor = GridBagConstraints.EAST;
        gbcLabel.insets = new Insets(0, 0, 5, 5);
        gbcLabel.gridx = 0;
        gbcLabel.gridy = index;
        editorPanel.add(label, gbcLabel);

        JComponent component = eci.getComponent();
        if (component instanceof JTextField) {
            ((JTextField) component).setColumns(10);
            ((JTextField) component).getDocument().addDocumentListener(
                    new TextFieldEditingListener());
        } else if (component instanceof JComboBox) {
            ((JComboBox<?>) component).addActionListener(new ComboChangeListener());
        }
        GridBagConstraints gbcComponent = new GridBagConstraints();
        gbcComponent.insets = new Insets(0, 0, 5, 0);
        gbcComponent.fill = GridBagConstraints.HORIZONTAL;
        gbcComponent.anchor = GridBagConstraints.NORTHWEST;
        gbcComponent.gridx = 1;
        gbcComponent.gridy = index;
        editorPanel.add(component, gbcComponent);
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

    public JTextField getSizeTextField() {
        return sizeTextField;
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
