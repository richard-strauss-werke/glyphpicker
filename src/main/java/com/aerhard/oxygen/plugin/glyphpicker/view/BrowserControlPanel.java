package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;

import java.awt.Component;

import javax.swing.Box;

public class BrowserControlPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JComboBox<String> dataSourceCombo;
    private JButton btnLoad;
    private JTextField fulltextTextField;
    private JButton viewBtn;
    private JToggleButton sortBtn;
    private JToolBar toolBar;
    private Component horizontalGlue;
    private JComboBox<String> autoCompleteCombo;
    private JComboBox<String> autoCompleteScopeCombo;

    public BrowserControlPanel() {
        setBorder(new EmptyBorder(8, 8, 0, 8));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 62, 199, 55, 55 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0 };
        gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE, 0.0,
                0.0 };
        setLayout(gridBagLayout);

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        GridBagConstraints gbc_toolBar = new GridBagConstraints();
        gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
        gbc_toolBar.gridwidth = 4;
        gbc_toolBar.insets = new Insets(0, 0, 0, 5);
        gbc_toolBar.gridx = 0;
        gbc_toolBar.gridy = 0;
        add(toolBar, gbc_toolBar);

        horizontalGlue = Box.createHorizontalGlue();
        toolBar.add(horizontalGlue);

        sortBtn = new JToggleButton();
        toolBar.add(sortBtn);
        // GridBagConstraints gbcSortBtn = new GridBagConstraints();
        // gbcSortBtn.insets = new Insets(5, 0, 5, 5);
        // gbcSortBtn.fill = GridBagConstraints.BOTH;
        // gbcSortBtn.anchor = GridBagConstraints.WEST;
        // gbcSortBtn.gridx = 2;
        // gbcSortBtn.gridy = 0;
        // add(sortBtn, gbcSortBtn);

        toolBar.addSeparator();

        viewBtn = new JButton();
        toolBar.add(viewBtn);
        // GridBagConstraints gbcViewBtn = new GridBagConstraints();
        // gbcViewBtn.insets = new Insets(5, 0, 5, 0);
        // gbcViewBtn.fill = GridBagConstraints.BOTH;
        // gbcViewBtn.anchor = GridBagConstraints.WEST;
        // gbcViewBtn.gridx = 3;
        // gbcViewBtn.gridy = 0;
        // add(viewBtn, gbcViewBtn);

        JLabel dataSourceLabel = new JLabel("Data source:");
        GridBagConstraints gbcDataSourceLabel = new GridBagConstraints();
        gbcDataSourceLabel.anchor = GridBagConstraints.WEST;
        gbcDataSourceLabel.fill = GridBagConstraints.VERTICAL;
        gbcDataSourceLabel.insets = new Insets(5, 0, 5, 5);
        gbcDataSourceLabel.gridx = 0;
        gbcDataSourceLabel.gridy = 1;
        add(dataSourceLabel, gbcDataSourceLabel);

        dataSourceCombo = new JComboBox<String>();
        dataSourceCombo.setEditable(false);
        GridBagConstraints gbcPathCombo = new GridBagConstraints();
        gbcPathCombo.gridwidth = 2;
        gbcPathCombo.weightx = 1.0;
        gbcPathCombo.fill = GridBagConstraints.BOTH;
        gbcPathCombo.insets = new Insets(5, 0, 5, 5);
        gbcPathCombo.gridx = 1;
        gbcPathCombo.gridy = 1;
        add(dataSourceCombo, gbcPathCombo);

        btnLoad = new JButton();
        GridBagConstraints gbcBrowserButtonLoad = new GridBagConstraints();
        gbcBrowserButtonLoad.insets = new Insets(3, 0, 5, 0);
        gbcBrowserButtonLoad.fill = GridBagConstraints.BOTH;
        gbcBrowserButtonLoad.anchor = GridBagConstraints.WEST;
        gbcBrowserButtonLoad.gridx = 3;
        gbcBrowserButtonLoad.gridy = 1;
        add(btnLoad, gbcBrowserButtonLoad);

        JLabel fulltextLabel = new JLabel("Search:");
        GridBagConstraints gbcFulltextLabel = new GridBagConstraints();
        gbcFulltextLabel.anchor = GridBagConstraints.WEST;
        gbcFulltextLabel.fill = GridBagConstraints.VERTICAL;
        gbcFulltextLabel.insets = new Insets(5, 0, 5, 5);
        gbcFulltextLabel.gridx = 0;
        gbcFulltextLabel.gridy = 2;
        add(fulltextLabel, gbcFulltextLabel);

        fulltextTextField = new JTextField();
        fulltextTextField.setEditable(true);
        GridBagConstraints gbcFulltextTextField = new GridBagConstraints();
        gbcFulltextTextField.gridwidth = 3;
        gbcFulltextTextField.weightx = 1.0;
        gbcFulltextTextField.fill = GridBagConstraints.BOTH;
        gbcFulltextTextField.insets = new Insets(5, 0, 5, 0);
        gbcFulltextTextField.gridx = 1;
        gbcFulltextTextField.gridy = 2;
        add(fulltextTextField, gbcFulltextTextField);

        
        autoCompleteScopeCombo = new JComboBox<String>();
        GridBagConstraints gbcAutoCompleteScopeCombo = new GridBagConstraints();
        gbcAutoCompleteScopeCombo.anchor = GridBagConstraints.WEST;
        gbcAutoCompleteScopeCombo.fill = GridBagConstraints.VERTICAL;
        gbcAutoCompleteScopeCombo.insets = new Insets(5, 0, 5, 5);
        gbcAutoCompleteScopeCombo.gridx = 0;
        gbcAutoCompleteScopeCombo.gridy = 3;
        add(autoCompleteScopeCombo, gbcAutoCompleteScopeCombo);

        autoCompleteCombo = new JComboBox<String>();
        autoCompleteCombo.setEditable(true);
        GridBagConstraints gbcAutoCompleteCombo = new GridBagConstraints();
        gbcAutoCompleteCombo.gridwidth = 3;
        gbcAutoCompleteCombo.weightx = 1.0;
        gbcAutoCompleteCombo.fill = GridBagConstraints.BOTH;
        gbcAutoCompleteCombo.insets = new Insets(5, 0, 5, 0);
        gbcAutoCompleteCombo.gridx = 1;
        gbcAutoCompleteCombo.gridy = 3;
        add(autoCompleteCombo, gbcAutoCompleteCombo);
        
        
        // JLabel rangeLabel = new JLabel("Range:");
        // GridBagConstraints gbc_rangeLabel = new GridBagConstraints();
        // gbc_rangeLabel.anchor = GridBagConstraints.WEST;
        // gbc_rangeLabel.fill = GridBagConstraints.VERTICAL;
        // gbc_rangeLabel.insets = new Insets(0, 0, 5, 5);
        // gbc_rangeLabel.gridx = 0;
        // gbc_rangeLabel.gridy = 3;
        // dataSourcePanel.add(rangeLabel, gbc_rangeLabel);
        //
        // rangeCombo = new JComboBox<String>();
        // rangeCombo.setEditable(true);
        // // rangeCombo.setStrict(false);
        // GridBagConstraints gbc_rangeCombo = new GridBagConstraints();
        // gbc_rangeCombo.insets = new Insets(5, 0, 5, 5);
        // gbc_rangeCombo.anchor = GridBagConstraints.NORTH;
        // gbc_rangeCombo.fill = GridBagConstraints.HORIZONTAL;
        // gbc_rangeCombo.gridx = 1;
        // gbc_rangeCombo.gridy = 3;
        // dataSourcePanel.add(rangeCombo, gbc_rangeCombo);
        //
        // JLabel classLabel = new JLabel("Class:");
        // GridBagConstraints gbc_classLabel = new GridBagConstraints();
        // gbc_classLabel.anchor = GridBagConstraints.WEST;
        // gbc_classLabel.fill = GridBagConstraints.VERTICAL;
        // gbc_classLabel.insets = new Insets(0, 0, 0, 5);
        // gbc_classLabel.gridx = 0;
        // gbc_classLabel.gridy = 4;
        // dataSourcePanel.add(classLabel, gbc_classLabel);
        //
        // classCombo = new AutoCompletionComboBox();
        // classCombo.setStrict(false);
        // GridBagConstraints gbc_classCombo = new GridBagConstraints();
        // gbc_classCombo.insets = new Insets(5, 0, 0, 5);
        // gbc_classCombo.anchor = GridBagConstraints.NORTH;
        // gbc_classCombo.fill = GridBagConstraints.HORIZONTAL;
        // gbc_classCombo.gridx = 1;
        // gbc_classCombo.gridy = 4;
        // dataSourcePanel.add(classCombo, gbc_classCombo);

    }

    public JButton getBtnConfigure() {
        return btnLoad;
    }

    public JComboBox<String> getDataSourceCombo() {
        return dataSourceCombo;
    }

    public JTextField getFulltextTextField() {
        return fulltextTextField;
    }

    public JComboBox<String> getAutoCompleteScopeCombo() {
        return autoCompleteScopeCombo;
    }
    
    public JComboBox<String> getAutoCompleteCombo() {
        return autoCompleteCombo;
    }
    
    public JToggleButton getSortBtn() {
        return sortBtn;
    }

    public JButton getViewBtn() {
        return viewBtn;
    }

    // public JComboBox<String> getRangeCombo() {
    // return rangeCombo;
    // }
    //
    // public AutoCompletionComboBox getClassCombo() {
    // return classCombo;
    // }

}
