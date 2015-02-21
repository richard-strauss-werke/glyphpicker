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
package de.badw.strauss.glyphpicker.view.options;

import de.badw.strauss.glyphpicker.model.DataSource;
import de.badw.strauss.glyphpicker.model.options.FormItemConfig;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * A data source editor component.
 */
public class DataSourceEditor extends JPanel {

    /**
     * The key of the "formEditingOccurred" event.
     */
    public static final String FORM_EDITING_OCCURRED = "formEditingOccurred";
    private static final long serialVersionUID = 1L;
    /**
     * The component's preferred width.
     */
    private static final int PREFERRED_WIDTH = 650;

    /**
     * The component's preferred height.
     */
    private static final int PREFERRED_HEIGHT = 400;

    /**
     * The list component's preferred width;
     */
    private static final int PREFERRED_LIST_WIDTH = 250;

    /**
     * The list component.
     */
    private final JList<DataSource> list;

    /**
     * The label's text field.
     */
    private final JTextField labelTextField;

    /**
     * The path's text field.
     */
    private final JTextField pathTextField;

    /**
     * The font name's text field.
     */
    private final JTextField fontNameTextField;

    /**
     * The display mode's combo.
     */
    private final JComboBox<String> glyphRendererCombo;

    /**
     * The size's text field.
     */
    private final JTextField sizeTextField;

    /**
     * The template's text field.
     */
    private final JTextField templateTextField;

    /**
     * The mapping type attribute's text field.
     */
    private final JTextField typeAttributeTextField;

    /**
     * The mapping subtype attribute's text field.
     */
    private final JTextField subtypeAttributeTextField;

    /**
     * The "parse mapping" check box.
     */
    private final JCheckBox parseMappingCheckBox;

    /**
     * The button pane containing list form controls.
     */
    private final JPanel listButtonPane;

    /**
     * The form panel.
     */
    private final JPanel formPanel;

    /**
     * A list of form item config objects.
     */
    private final List<FormItemConfig> formItemConfigList = new ArrayList<>();

    /**
     * Instantiates a new DataSourceEditor panel.
     */
    public DataSourceEditor() {

        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
        String className = this.getClass().getSimpleName();

        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

        setLayout(new BorderLayout(8, 0));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel listPanel = new JPanel();
        listPanel.setBorder(new CompoundBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), i18n.getString(className
                + ".tables"), TitledBorder.LEADING, TitledBorder.TOP,
                null, new Color(0, 0, 0)), new EmptyBorder(8, 8, 8, 8)));
        add(listPanel, BorderLayout.WEST);
        listPanel.setLayout(new BorderLayout(0, 0));

        listPanel.setPreferredSize(new Dimension(PREFERRED_LIST_WIDTH, listPanel.getPreferredSize().height));

        listButtonPane = new JPanel();
        listPanel.add(listButtonPane, BorderLayout.SOUTH);
        listButtonPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        list = new JList<>();
        JScrollPane scrollPane = new JScrollPane(list);
        listPanel.add(scrollPane);

        formPanel = new JPanel();
        formPanel.setBorder(new CompoundBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), i18n.getString(className
                + ".edit"), TitledBorder.LEADING, TitledBorder.TOP, null,
                new Color(0, 0, 0)), new EmptyBorder(5, 2, 2, 2)));
        add(formPanel);
        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[]{100, 100, 100, 100, 100, 100};
        gbl.rowHeights = new int[]{20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, Double.MIN_VALUE};
        formPanel.setLayout(gbl);

        labelTextField = new JTextField();
        pathTextField = new JTextField();
        fontNameTextField = new JTextField();
        glyphRendererCombo = new JComboBox<>();
        sizeTextField = new JTextField();
        templateTextField = new JTextField();
        typeAttributeTextField = new JTextField();
        subtypeAttributeTextField = new JTextField();
        parseMappingCheckBox = new JCheckBox();

        formItemConfigList.add(new FormItemConfig(i18n.getString(className
                + ".name"), labelTextField, 0, 0, 6));
        formItemConfigList.add(new FormItemConfig(i18n.getString(className
                + ".path"), pathTextField, 0, 1, 6));

        formItemConfigList.add(new FormItemConfig(i18n.getString(className
                + ".renderer"), glyphRendererCombo, 0, 2, 2));
        formItemConfigList.add(new FormItemConfig(i18n.getString(className
                + ".glyphSize"), sizeTextField, 2, 2, 2));
        formItemConfigList.add(new FormItemConfig(i18n.getString(className
                + ".font"), fontNameTextField, 4, 2, 2));

        formItemConfigList.add(new FormItemConfig(i18n.getString(className
                + ".template"), templateTextField, 0, 3, 6));

        formItemConfigList.add(new FormItemConfig(i18n.getString(className
                + ".typeAttributeValue"), typeAttributeTextField, 0, 4, 3));
        formItemConfigList.add(new FormItemConfig(i18n.getString(className
                + ".subtypeAttributeValue"), subtypeAttributeTextField, 3, 4, 3));

        formItemConfigList.add(new FormItemConfig(i18n.getString(className
                + ".parseMapping"), parseMappingCheckBox, 0, 5, 3));

        for (FormItemConfig formItemConfig : formItemConfigList) {
            addToFormPanel(formItemConfig);
        }

    }

    /**
     * Enables/disables the form components.
     *
     * @param enabled the new form enabled
     */
    public void setFormEnabled(boolean enabled) {
        for (FormItemConfig formItemConfig : formItemConfigList) {
            formItemConfig.getComponent().setEnabled(enabled);
        }
    }

    /**
     * Adds a component to the form panel.
     *
     * @param config the FormItemConfig object specifying the component's properties
     */
    private void addToFormPanel(FormItemConfig config) {

        int labelY = config.getY() * 2;
        int editorY = labelY + 1;

        JLabel label = new JLabel(config.getLabel());
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.anchor = GridBagConstraints.SOUTHWEST;
        gbcLabel.insets = new Insets(5, 5, 5, 5);
        gbcLabel.gridx = config.getX();
        gbcLabel.gridwidth = config.getWidth();
        gbcLabel.gridy = labelY;
        formPanel.add(label, gbcLabel);

        JComponent component = config.getComponent();
        if (component instanceof JTextField) {
            ((JTextField) component).getDocument().addDocumentListener(
                    new TextFieldEditingListener());
        } else if (component instanceof JComboBox) {
            ((JComboBox<?>) component)
                    .addActionListener(new ComboChangeListener());
        } else if (component instanceof JCheckBox) {
            ((JCheckBox) component)
                    .addActionListener(new ComboChangeListener());
        }

        GridBagConstraints gbcComponent = new GridBagConstraints();
        gbcComponent.insets = new Insets(0, 5, 5, 5);
        gbcComponent.fill = GridBagConstraints.HORIZONTAL;
        gbcComponent.anchor = GridBagConstraints.NORTHWEST;
        gbcComponent.gridx = config.getX();
        gbcComponent.gridwidth = config.getWidth();
        gbcComponent.gridy = editorY;
        formPanel.add(component, gbcComponent);
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
     * Gets the glyph renderer combo box.
     *
     * @return the glyph renderer combo box
     */
    public JComboBox<String> getGlyphRendererCombo() {
        return glyphRendererCombo;
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
     * Gets the mapping type attribute's text field.
     *
     * @return the mapping type attribute's text field
     */
    public JTextField getTypeAttributeTextField() {
        return typeAttributeTextField;
    }

    /**
     * Gets the mapping subtype attribute's text field.
     *
     * @return the mapping subtype attribute's text field
     */
    public JTextField getSubtypeAttributeTextField() {
        return subtypeAttributeTextField;
    }

    /**
     * Gets the "parse mapping" check box.
     *
     * @return the "parse mapping" check box
     */
    public JCheckBox getParseMappingCheckBox() {
        return parseMappingCheckBox;
    }

    /**
     * Gets the list button pane.
     *
     * @return The button pane containing list form controls
     */
    public JPanel getListButtonPane() {
        return listButtonPane;
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

}
