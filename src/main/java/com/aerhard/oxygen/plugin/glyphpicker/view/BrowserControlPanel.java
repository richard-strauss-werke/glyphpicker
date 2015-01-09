package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class BrowserControlPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JComboBox<String> dataSourceCombo;
    private JButton btnLoad;
    private JTextField fulltextTextField;
    private JButton toggleBtn;

    public BrowserControlPanel() {
        setBorder(new EmptyBorder(8, 8, 0, 8));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 62, 199, 55, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0,
                Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE,
                0.0, 0.0 };
        setLayout(gridBagLayout);

        toggleBtn = new JButton();
        GridBagConstraints gbcToggleBtn = new GridBagConstraints();
        gbcToggleBtn.insets = new Insets(3, 0, 5, 0);
        gbcToggleBtn.fill = GridBagConstraints.BOTH;
        gbcToggleBtn.anchor = GridBagConstraints.WEST;
        gbcToggleBtn.gridx = 2;
        gbcToggleBtn.gridy = 0;
        add(toggleBtn, gbcToggleBtn);

        JLabel dataSourceLabel = new JLabel("Data source:");
        GridBagConstraints gbcDataSourceLabel = new GridBagConstraints();
        gbcDataSourceLabel.anchor = GridBagConstraints.WEST;
        gbcDataSourceLabel.fill = GridBagConstraints.VERTICAL;
        gbcDataSourceLabel.insets = new Insets(0, 0, 5, 5);
        gbcDataSourceLabel.gridx = 0;
        gbcDataSourceLabel.gridy = 1;
        add(dataSourceLabel, gbcDataSourceLabel);

        dataSourceCombo = new JComboBox<String>();
        dataSourceCombo.setEditable(false);
        GridBagConstraints gbcPathCombo = new GridBagConstraints();
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
        gbcBrowserButtonLoad.gridx = 2;
        gbcBrowserButtonLoad.gridy = 1;
        add(btnLoad, gbcBrowserButtonLoad);

        JLabel fulltextLabel = new JLabel("Search:");
        GridBagConstraints gbcFulltextLabel = new GridBagConstraints();
        gbcFulltextLabel.anchor = GridBagConstraints.WEST;
        gbcFulltextLabel.fill = GridBagConstraints.VERTICAL;
        gbcFulltextLabel.insets = new Insets(0, 0, 5, 5);
        gbcFulltextLabel.gridx = 0;
        gbcFulltextLabel.gridy = 2;
        add(fulltextLabel, gbcFulltextLabel);

        fulltextTextField = new JTextField();
        fulltextTextField.setEditable(true);
        GridBagConstraints gbcFulltextTextField = new GridBagConstraints();
        gbcFulltextTextField.weightx = 1.0;
        gbcFulltextTextField.fill = GridBagConstraints.BOTH;
        gbcFulltextTextField.insets = new Insets(5, 0, 5, 5);
        gbcFulltextTextField.gridx = 1;
        gbcFulltextTextField.gridy = 2;
        add(fulltextTextField, gbcFulltextTextField);

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

    public JButton getToggleBtn() {
        return toggleBtn;
    }

    // public JComboBox<String> getRangeCombo() {
    // return rangeCombo;
    // }
    //
    // public AutoCompletionComboBox getClassCombo() {
    // return classCombo;
    // }

}
