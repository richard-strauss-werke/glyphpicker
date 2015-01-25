package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JToolBar;

import java.awt.Component;

import javax.swing.Box;

import com.jidesoft.swing.JideButton;

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
    private JPanel dataSourcePanel;
    private JPanel searchPanel;

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

        viewBtn = new JideButton();
        toolBar.add(viewBtn);

        if (setDataSourceCombo) {
            
            row++;
            
            dataSourcePanel = new JPanel();
            
            dataSourcePanel.setBorder(new TitledBorder(null, "Data source", TitledBorder.LEADING, TitledBorder.TOP, null, null));

            GridBagConstraints gbcDataSourcePanel = new GridBagConstraints();
            gbcDataSourcePanel.anchor = GridBagConstraints.WEST;
            gbcDataSourcePanel.fill = GridBagConstraints.HORIZONTAL;
            gbcDataSourcePanel.gridwidth = 4;
            gbcDataSourcePanel.insets = new Insets(0, 0, 5, 0);
            gbcDataSourcePanel.gridx = 0;
            gbcDataSourcePanel.gridy = row;
            add(dataSourcePanel, gbcDataSourcePanel);
            
            
            GridBagLayout panelLayout = new GridBagLayout();
            panelLayout.columnWidths = new int[] { 311, 55 };
            panelLayout.rowHeights = new int[] { 0 };
            panelLayout.columnWeights = new double[] {1.0, 0.0 };
            panelLayout.rowWeights = new double[] { 0.0 };
            dataSourcePanel.setLayout(panelLayout);

            dataSourceCombo = new JComboBox<String>();
            dataSourceCombo.setEditable(false);
            GridBagConstraints gbcPathCombo = new GridBagConstraints();
            gbcPathCombo.weightx = 1.0;
            gbcPathCombo.fill = GridBagConstraints.BOTH;
            gbcPathCombo.insets = new Insets(5, 5, 5, 3);
            gbcPathCombo.gridx = 0;
            gbcPathCombo.gridy = 0;
            dataSourcePanel.add(dataSourceCombo, gbcPathCombo);

            btnLoad = new JideButton();
            GridBagConstraints gbcBrowserButtonLoad = new GridBagConstraints();
            gbcBrowserButtonLoad.insets = new Insets(4, 5, 4, 5);
            gbcBrowserButtonLoad.fill = GridBagConstraints.BOTH;
            gbcBrowserButtonLoad.gridx = 1;
            gbcBrowserButtonLoad.gridy = 0;
            dataSourcePanel.add(btnLoad, gbcBrowserButtonLoad);
        }

        
        row++;
        
        searchPanel = new JPanel();
        
        searchPanel.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        
        GridBagConstraints gbcSearchPanel = new GridBagConstraints();
        gbcSearchPanel.anchor = GridBagConstraints.WEST;
        gbcSearchPanel.fill = GridBagConstraints.HORIZONTAL;
        gbcSearchPanel.gridwidth = 4;
        gbcSearchPanel.insets = new Insets(0,0,0, 0);
        gbcSearchPanel.gridx = 0;
        gbcSearchPanel.gridy = row;
        add(searchPanel, gbcSearchPanel);
        
        
        GridBagLayout panelLayout = new GridBagLayout();
        panelLayout.columnWidths = new int[] { 55,311 };
        panelLayout.rowHeights = new int[] { 0 };
        panelLayout.columnWeights = new double[] {0.0, 1.0 };
        panelLayout.rowWeights = new double[] { 0.0 };
        searchPanel.setLayout(panelLayout);
        
        autoCompleteScopeCombo = new JComboBox<String>();
        GridBagConstraints gbcAutoCompleteScopeCombo = new GridBagConstraints();
        gbcAutoCompleteScopeCombo.anchor = GridBagConstraints.WEST;
        gbcAutoCompleteScopeCombo.fill = GridBagConstraints.BOTH;
        gbcAutoCompleteScopeCombo.insets = new Insets(5,5,5,5);
        gbcAutoCompleteScopeCombo.gridx = 0;
        gbcAutoCompleteScopeCombo.gridy = row;
        searchPanel.add(autoCompleteScopeCombo, gbcAutoCompleteScopeCombo);

        autoCompleteCombo = new JComboBox<String>();
        autoCompleteCombo.setEditable(true);
        GridBagConstraints gbcAutoCompleteCombo = new GridBagConstraints();
        gbcAutoCompleteCombo.gridwidth = 3;
        gbcAutoCompleteCombo.weightx = 1.0;
        gbcAutoCompleteCombo.fill = GridBagConstraints.BOTH;
        gbcAutoCompleteCombo.insets = new Insets(5, 3,5,5);
        gbcAutoCompleteCombo.gridx = 1;
        gbcAutoCompleteCombo.gridy = row;
        searchPanel.add(autoCompleteCombo, gbcAutoCompleteCombo);

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
