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
package com.aerhard.oxygen.plugin.glyphpicker.view.editor;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JCheckBox;
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
import java.util.ResourceBundle;

/**
 * A data source editor component.
 */
public class DataSourceEditor extends JPanel {

    private static final long serialVersionUID = 1L;

    /** The name of the "formEditingOccurred" property. */
    public static final String FORM_EDITING_OCCURRED = "formEditingOccurred";

    /** The component's preferred width. */
    private static final int PREFERRED_WIDTH = 600;
    
    /** The component's preferred height. */
    private static final int PREFERRED_HEIGHT = 400;

    /** The list component. */
    private final JList<DataSource> list;

    /** The label's text field. */
    private final JTextField labelTextField;
    
    /** The path's text field. */
    private final JTextField pathTextField;
    
    /** The font name's text field. */
    private final JTextField fontNameTextField;
    
    /** The display mode's combo. */
    private final JComboBox<String> displayModeCombo;
    
    /** The size's text field. */
    private final JTextField sizeTextField;
    
    /** The template's text field. */
    private final JTextField templateTextField;
    
    /** The mapping type's text field. */
    private final JTextField typeAttributeTextField;
    
    /** The mapping subtype's text field. */
    private final JTextField subtypeAttributeTextField;
    
    /** The mapping-as-char-string's check box. */
    private final JCheckBox parseMappingCheckBox;
    
    /** The button pane for editing the list items. */
    private final JPanel listButtonPane;
    
    /** The editor panel. */
    private final JPanel editorPanel;

    /** The editor config. */
    private final List<EditorConfigItem> editorConfig = new ArrayList<>();

    /**
     * Instantiates a new DataSourceEditor panel.
     */
    public DataSourceEditor() {

        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
        String className = this.getClass().getSimpleName();

        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

        setLayout(new BorderLayout(8, 0));

        JPanel listPanel = new JPanel();
        listPanel.setBorder(new CompoundBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), i18n.getString(className
                + ".dataSources"), TitledBorder.LEADING, TitledBorder.TOP,
                null, new Color(0, 0, 0)), new EmptyBorder(8, 8, 8, 8)));
        add(listPanel, BorderLayout.WEST);
        listPanel.setLayout(new BorderLayout(0, 0));

        listButtonPane = new JPanel();
        listPanel.add(listButtonPane, BorderLayout.SOUTH);
        listButtonPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        list = new JList<>();
        JScrollPane scrollPane = new JScrollPane(list);
        listPanel.add(scrollPane);

        editorPanel = new JPanel();
        editorPanel.setBorder(new CompoundBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), i18n.getString(className
                + ".edit"), TitledBorder.LEADING, TitledBorder.TOP, null,
                new Color(0, 0, 0)), new EmptyBorder(8, 8, 8, 8)));
        add(editorPanel);
        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[] { 102, 46 };
        gbl.rowHeights = new int[] { 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl.columnWeights = new double[] { 0.0, 1.0 };
        gbl.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, Double.MIN_VALUE };
        editorPanel.setLayout(gbl);

        labelTextField = new JTextField();
        pathTextField = new JTextField();
        fontNameTextField = new JTextField();
        displayModeCombo = new JComboBox<>();
        sizeTextField = new JTextField();
        templateTextField = new JTextField();
        typeAttributeTextField = new JTextField();
        subtypeAttributeTextField = new JTextField();
        parseMappingCheckBox = new JCheckBox();

        editorConfig.add(new EditorConfigItem(i18n.getString(className
                + ".label"), labelTextField));
        editorConfig.add(new EditorConfigItem(i18n.getString(className
                + ".path"), pathTextField));
        editorConfig.add(new EditorConfigItem(i18n.getString(className
                + ".fontName"), fontNameTextField));
        editorConfig.add(new EditorConfigItem(i18n.getString(className
                + ".displayMode"), displayModeCombo));
        editorConfig.add(new EditorConfigItem(i18n.getString(className
                + ".glyphSize"), sizeTextField));
        editorConfig.add(new EditorConfigItem(i18n.getString(className
                + ".template"), templateTextField));
        editorConfig.add(new EditorConfigItem(i18n.getString(className
                + ".typeAttributeValue"), typeAttributeTextField));
        editorConfig.add(new EditorConfigItem(i18n.getString(className
                + ".subtypeAttributeValue"), subtypeAttributeTextField));
        editorConfig.add(new EditorConfigItem(i18n.getString(className
                + ".parseMapping"), parseMappingCheckBox));

        for (int i = 0; i < editorConfig.size(); i++) {
            addToEditorPanel(i, editorConfig.get(i));
        }

    }

    /**
     * Enables/disables the form components.
     *
     * @param enabled the new form enabled
     */
    public void setFormEnabled(boolean enabled) {
        for (EditorConfigItem eci : editorConfig) {
            eci.getComponent().setEnabled(enabled);
        }
    }

/**
 * A DocumentListener added to the form text fields, firing the change event of the property FORM_EDITING_OCCURRED
 */
    private class TextFieldEditingListener implements DocumentListener {

        /* (non-Javadoc)
         * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
         */
        @Override
        public void removeUpdate(DocumentEvent e) {
            firePropertyChange(FORM_EDITING_OCCURRED, null, null);
        }

        /* (non-Javadoc)
         * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
         */
        @Override
        public void insertUpdate(DocumentEvent e) {
            firePropertyChange(FORM_EDITING_OCCURRED, null, null);
        }

        /* (non-Javadoc)
         * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
         */
        @Override
        public void changedUpdate(DocumentEvent e) {
            firePropertyChange(FORM_EDITING_OCCURRED, null, null);
        }
    }

    /**
     * An ActionListener added to the form combo, firing the change event of the property FORM_EDITING_OCCURRED
     */
    private class ComboChangeListener implements ActionListener {
        
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            firePropertyChange(FORM_EDITING_OCCURRED, null, null);
        }
    }

    /**
     * Adds a component to editor panel.
     *
     * @param index the component's index
     * @param eci the EditorConfigItem object specifying the component's properties
     */
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
        }

        else if (component instanceof JComboBox) {
            ((JComboBox<?>) component)
                    .addActionListener(new ComboChangeListener());
        }

        else if (component instanceof JCheckBox) {
            ((JCheckBox) component)
                    .addActionListener(new ComboChangeListener());
        }

        GridBagConstraints gbcComponent = new GridBagConstraints();
        gbcComponent.insets = new Insets(0, 0, 5, 0);
        gbcComponent.fill = GridBagConstraints.HORIZONTAL;
        gbcComponent.anchor = GridBagConstraints.NORTHWEST;
        gbcComponent.gridx = 1;
        gbcComponent.gridy = index;
        editorPanel.add(component, gbcComponent);
    }

    /**
     * Gets the data source list component.
     *
     * @return the list component
     */
    public JList<DataSource> getList() {
        return list;
    }

    /**
     * Gets the label's text field.
     *
     * @return the label's text field
     */
    public JTextField getLabelTextField() {
        return labelTextField;
    }

    /**
     * Gets the path's text field.
     *
     * @return the path's text field
     */
    public JTextField getPathTextField() {
        return pathTextField;
    }

    /**
     * Gets the font name's text field.
     *
     * @return the font name's text field
     */
    public JTextField getFontNameTextField() {
        return fontNameTextField;
    }

    /**
     * Gets the display mode's text field.
     *
     * @return the display mode's text field
     */
    public JComboBox<String> getDisplayModeTextField() {
        return displayModeCombo;
    }

    /**
     * Gets the size's text field.
     *
     * @return the size's text field
     */
    public JTextField getSizeTextField() {
        return sizeTextField;
    }

    /**
     * Gets the template's text field.
     *
     * @return the template's text field
     */
    public JTextField getTemplateTextField() {
        return templateTextField;
    }

    /**
     * Gets the mapping type's text field.
     *
     * @return the mapping type's text field
     */
    public JTextField getTypeAttributeTextField() {
        return typeAttributeTextField;
    }

    /**
     * Gets the mapping sub type's text field.
     *
     * @return the mapping sub type's text field
     */
    public JTextField getSubtypeAttributeTextField() {
        return subtypeAttributeTextField;
    }

    /**
     * Gets the mapping-as-char-string check box.
     *
     * @return the mapping-as-char-string check box
     */
    public JCheckBox getParseMappingCheckBox() {
        return parseMappingCheckBox;
    }

    /**
     * Gets the list button pane.
     *
     * @return The button pane for editing the list items
     */
    public JPanel getListButtonPane() {
        return listButtonPane;
    }

}
