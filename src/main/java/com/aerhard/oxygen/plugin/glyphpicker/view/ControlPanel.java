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
package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JToolBar;
import javax.swing.Box;

import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideToggleButton;

/**
 * The ControlPanel class.
 */
public class ControlPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    /** The data source combo. */
    private JComboBox<String> dataSourceCombo;
    
    /** The edit button. */
    private JButton editBtn;
    
    /** The view button. */
    private final JButton viewBtn;
    
    /** The sort button. */
    private final JideToggleButton sortBtn;
    
    /** The auto complete combo. */
    private final JComboBox<String> autoCompleteCombo;
    
    /** The auto complete scope combo. */
    private final JComboBox<String> autoCompleteScopeCombo;
    
    /** The toolbar. */
    private final JToolBar toolBar;

    /**
     * Instantiates a new ControlPanel.
     *
     * @param setDataSourceCombo the set data source combo
     */
    public ControlPanel(boolean setDataSourceCombo) {

        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
        String className = this.getClass().getSimpleName();

        setBorder(new EmptyBorder(0, 8, 0, 8));

        int row = 0;

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 62, 199, 55, 55 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0 };
        gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE, 0.0,
                0.0 };
        setLayout(gridBagLayout);

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        GridBagConstraints gbcToolBar = new GridBagConstraints();
        gbcToolBar.fill = GridBagConstraints.HORIZONTAL;
        gbcToolBar.gridwidth = 4;
        gbcToolBar.insets = new Insets(5, 0, 5, 5);
        gbcToolBar.gridx = 0;
        gbcToolBar.gridy = row;
        add(toolBar, gbcToolBar);

        toolBar.add(Box.createHorizontalGlue());

        sortBtn = new JideToggleButton();
        sortBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        sortBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        toolBar.add(sortBtn);

        viewBtn = new JideButton();
        viewBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        viewBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        toolBar.add(viewBtn);

        if (setDataSourceCombo) {

            row++;

            JPanel dataSourcePanel = new JPanel();

            dataSourcePanel.setBorder(new TitledBorder(null, i18n
                    .getString(className + ".dataSource"),
                    TitledBorder.LEADING, TitledBorder.TOP, null, null));

            GridBagConstraints gbcDataSourcePanel = new GridBagConstraints();
            gbcDataSourcePanel.anchor = GridBagConstraints.WEST;
            gbcDataSourcePanel.fill = GridBagConstraints.HORIZONTAL;
            gbcDataSourcePanel.gridwidth = 4;
            gbcDataSourcePanel.insets = new Insets(0, 0, 5, 0);
            gbcDataSourcePanel.gridx = 0;
            gbcDataSourcePanel.gridy = row;
            add(dataSourcePanel, gbcDataSourcePanel);

            GridBagLayout panelLayout = new GridBagLayout();
            panelLayout.columnWidths = new int[] { 311, 24 };
            panelLayout.rowHeights = new int[] { 0 };
            panelLayout.columnWeights = new double[] { 1.0, 0.0 };
            panelLayout.rowWeights = new double[] { 0.0 };
            dataSourcePanel.setLayout(panelLayout);

            dataSourceCombo = new JComboBox<>();
            dataSourceCombo.setEditable(false);
            GridBagConstraints gbcPathCombo = new GridBagConstraints();
            gbcPathCombo.weightx = 1.0;
            gbcPathCombo.fill = GridBagConstraints.BOTH;
            gbcPathCombo.insets = new Insets(5, 5, 5, 3);
            gbcPathCombo.gridx = 0;
            gbcPathCombo.gridy = 0;
            dataSourcePanel.add(dataSourceCombo, gbcPathCombo);

            editBtn = new JideButton();
            GridBagConstraints gbcBrowserButtonLoad = new GridBagConstraints();
            gbcBrowserButtonLoad.insets = new Insets(4, 5, 4, 5);
            gbcBrowserButtonLoad.fill = GridBagConstraints.BOTH;
            gbcBrowserButtonLoad.gridx = 1;
            gbcBrowserButtonLoad.gridy = 0;
            dataSourcePanel.add(editBtn, gbcBrowserButtonLoad);
        }

        row++;

        JPanel searchPanel = new JPanel();

        searchPanel.setBorder(new TitledBorder(null, i18n.getString(className
                + ".search"), TitledBorder.LEADING, TitledBorder.TOP, null,
                null));

        GridBagConstraints gbcSearchPanel = new GridBagConstraints();
        gbcSearchPanel.anchor = GridBagConstraints.WEST;
        gbcSearchPanel.fill = GridBagConstraints.HORIZONTAL;
        gbcSearchPanel.gridwidth = 4;
        gbcSearchPanel.insets = new Insets(0, 0, 0, 0);
        gbcSearchPanel.gridx = 0;
        gbcSearchPanel.gridy = row;
        add(searchPanel, gbcSearchPanel);

        GridBagLayout panelLayout = new GridBagLayout();
        panelLayout.columnWidths = new int[] { 55, 311 };
        panelLayout.rowHeights = new int[] { 0 };
        panelLayout.columnWeights = new double[] { 0.0, 1.0 };
        panelLayout.rowWeights = new double[] { 0.0 };
        searchPanel.setLayout(panelLayout);

        autoCompleteScopeCombo = new JComboBox<>();
        GridBagConstraints gbcAutoCompleteScopeCombo = new GridBagConstraints();
        gbcAutoCompleteScopeCombo.anchor = GridBagConstraints.WEST;
        gbcAutoCompleteScopeCombo.fill = GridBagConstraints.BOTH;
        gbcAutoCompleteScopeCombo.insets = new Insets(5, 5, 5, 5);
        gbcAutoCompleteScopeCombo.gridx = 0;
        gbcAutoCompleteScopeCombo.gridy = row;
        searchPanel.add(autoCompleteScopeCombo, gbcAutoCompleteScopeCombo);

        autoCompleteCombo = new JComboBox<>();
        autoCompleteCombo.setEditable(true);
        GridBagConstraints gbcAutoCompleteCombo = new GridBagConstraints();
        gbcAutoCompleteCombo.gridwidth = 3;
        gbcAutoCompleteCombo.weightx = 1.0;
        gbcAutoCompleteCombo.fill = GridBagConstraints.BOTH;
        gbcAutoCompleteCombo.insets = new Insets(5, 3, 5, 5);
        gbcAutoCompleteCombo.gridx = 1;
        gbcAutoCompleteCombo.gridy = row;
        searchPanel.add(autoCompleteCombo, gbcAutoCompleteCombo);

    }

    /**
     * Adds a new component at the specified index to the toolbar.
     *
     * @param component the component
     * @param index the index
     */
    public void addToToolbar(JComponent component, int index) {
        toolBar.add(component, index);
    }

    /**
     * Adds a new JideButton with the specified action at the specified index to the toolbar.
     *
     * @param action the action
     * @param index the index
     */
    public void addToToolbar(Action action, int index) {
        JideButton button = new JideButton(action);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        toolBar.add(button, index);
    }

    /**
     * Gets the edit button.
     *
     * @return the edit button
     */
    public JButton getEditBtn() {
        return editBtn;
    }

    /**
     * Gets the data source combo.
     *
     * @return the data source combo
     */
    public JComboBox<String> getDataSourceCombo() {
        return dataSourceCombo;
    }

    /**
     * Gets the auto complete scope combo.
     *
     * @return the auto complete scope combo
     */
    public JComboBox<String> getAutoCompleteScopeCombo() {
        return autoCompleteScopeCombo;
    }

    /**
     * Gets the auto complete combo.
     *
     * @return the auto complete combo
     */
    public JComboBox<String> getAutoCompleteCombo() {
        return autoCompleteCombo;
    }

    /**
     * Gets the sort button.
     *
     * @return the sort button
     */
    public JideToggleButton getSortBtn() {
        return sortBtn;
    }

    /**
     * Gets the view button.
     *
     * @return the view button
     */
    public JButton getViewBtn() {
        return viewBtn;
    }

}
