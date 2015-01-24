package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;

import java.awt.Component;

import javax.swing.Box;

public class ControlPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JComboBox<String> dataSourceCombo;
    private JButton btnLoad;
    private JButton viewBtn;
    private JToggleButton sortBtn;
    private JToolBar toolBar;
    private Component horizontalGlue;
    private JComboBox<String> autoCompleteCombo;
    private JComboBox<String> autoCompleteScopeCombo;

    public ControlPanel(boolean setDataSourceCombo) {
        setBorder(new EmptyBorder(8, 8, 0, 8));

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
        GridBagConstraints gbc_toolBar = new GridBagConstraints();
        gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
        gbc_toolBar.gridwidth = 4;
        gbc_toolBar.insets = new Insets(0, 0, 0, 5);
        gbc_toolBar.gridx = 0;
        gbc_toolBar.gridy = row;
        add(toolBar, gbc_toolBar);

        horizontalGlue = Box.createHorizontalGlue();
        toolBar.add(horizontalGlue);

        sortBtn = new JToggleButton();
        toolBar.add(sortBtn);

        toolBar.addSeparator();

        viewBtn = new JButton();
        toolBar.add(viewBtn);

        if (setDataSourceCombo) {
            
            row++;
            
            JLabel dataSourceLabel = new JLabel("Data source:");
            GridBagConstraints gbcDataSourceLabel = new GridBagConstraints();
            gbcDataSourceLabel.anchor = GridBagConstraints.WEST;
            gbcDataSourceLabel.fill = GridBagConstraints.VERTICAL;
            gbcDataSourceLabel.insets = new Insets(5, 0, 5, 5);
            gbcDataSourceLabel.gridx = 0;
            gbcDataSourceLabel.gridy = row;
            add(dataSourceLabel, gbcDataSourceLabel);

            dataSourceCombo = new JComboBox<String>();
            dataSourceCombo.setEditable(false);
            GridBagConstraints gbcPathCombo = new GridBagConstraints();
            gbcPathCombo.gridwidth = 2;
            gbcPathCombo.weightx = 1.0;
            gbcPathCombo.fill = GridBagConstraints.BOTH;
            gbcPathCombo.insets = new Insets(5, 0, 5, 5);
            gbcPathCombo.gridx = 1;
            gbcPathCombo.gridy = row;
            add(dataSourceCombo, gbcPathCombo);

            btnLoad = new JButton();
            GridBagConstraints gbcBrowserButtonLoad = new GridBagConstraints();
            gbcBrowserButtonLoad.insets = new Insets(3, 0, 5, 0);
            gbcBrowserButtonLoad.fill = GridBagConstraints.BOTH;
            gbcBrowserButtonLoad.anchor = GridBagConstraints.WEST;
            gbcBrowserButtonLoad.gridx = 3;
            gbcBrowserButtonLoad.gridy = row;
            add(btnLoad, gbcBrowserButtonLoad);
        }


//        JLabel fulltextLabel = new JLabel("Search:");
//        GridBagConstraints gbcFulltextLabel = new GridBagConstraints();
//        gbcFulltextLabel.anchor = GridBagConstraints.WEST;
//        gbcFulltextLabel.fill = GridBagConstraints.VERTICAL;
//        gbcFulltextLabel.insets = new Insets(5, 0, 5, 5);
//        gbcFulltextLabel.gridx = 0;
//        gbcFulltextLabel.gridy = 2;
//        add(fulltextLabel, gbcFulltextLabel);
        
        row++;
        
        autoCompleteScopeCombo = new JComboBox<String>();
        GridBagConstraints gbcAutoCompleteScopeCombo = new GridBagConstraints();
        gbcAutoCompleteScopeCombo.anchor = GridBagConstraints.WEST;
        gbcAutoCompleteScopeCombo.fill = GridBagConstraints.VERTICAL;
        gbcAutoCompleteScopeCombo.insets = new Insets(5, 0, 5, 5);
        gbcAutoCompleteScopeCombo.gridx = 0;
        gbcAutoCompleteScopeCombo.gridy = row;
        add(autoCompleteScopeCombo, gbcAutoCompleteScopeCombo);

        autoCompleteCombo = new JComboBox<String>();
        autoCompleteCombo.setEditable(true);
        GridBagConstraints gbcAutoCompleteCombo = new GridBagConstraints();
        gbcAutoCompleteCombo.gridwidth = 3;
        gbcAutoCompleteCombo.weightx = 1.0;
        gbcAutoCompleteCombo.fill = GridBagConstraints.BOTH;
        gbcAutoCompleteCombo.insets = new Insets(5, 0, 5, 0);
        gbcAutoCompleteCombo.gridx = 1;
        gbcAutoCompleteCombo.gridy = row;
        add(autoCompleteCombo, gbcAutoCompleteCombo);

    }

    public JButton getBtnConfigure() {
        return btnLoad;
    }

    public JComboBox<String> getDataSourceCombo() {
        return dataSourceCombo;
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

}
