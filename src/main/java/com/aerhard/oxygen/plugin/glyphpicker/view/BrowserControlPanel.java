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
    private JTextField ftTextField;
    private JButton toggleBtn;

    public BrowserControlPanel() {
        setBorder(new EmptyBorder(8, 8, 0, 8));

        GridBagLayout gbl_dataSourcePanel = new GridBagLayout();
        gbl_dataSourcePanel.columnWidths = new int[] { 62, 199, 55, 0 };
        gbl_dataSourcePanel.rowHeights = new int[] { 0, 0, 0, 0 };
        gbl_dataSourcePanel.columnWeights = new double[] { 0.0, 0.0, 0.0,
                Double.MIN_VALUE };
        gbl_dataSourcePanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE,
                0.0, 0.0 };
        setLayout(gbl_dataSourcePanel);
        
        
        toggleBtn = new JButton();
        GridBagConstraints gbc_toggleBtn = new GridBagConstraints();
        gbc_toggleBtn.insets = new Insets(3, 0, 5, 0);
        gbc_toggleBtn.fill = GridBagConstraints.BOTH;
        gbc_toggleBtn.anchor = GridBagConstraints.WEST;
        gbc_toggleBtn.gridx = 2;
        gbc_toggleBtn.gridy = 0;
        add(toggleBtn, gbc_toggleBtn);

        
        JLabel dataSourceLabel = new JLabel("Data source:");
        GridBagConstraints gbc_dataSourceLabel = new GridBagConstraints();
        gbc_dataSourceLabel.anchor = GridBagConstraints.WEST;
        gbc_dataSourceLabel.fill = GridBagConstraints.VERTICAL;
        gbc_dataSourceLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dataSourceLabel.gridx = 0;
        gbc_dataSourceLabel.gridy = 1;
        add(dataSourceLabel, gbc_dataSourceLabel);

        dataSourceCombo = new JComboBox<String>();
        dataSourceCombo.setEditable(false);
        GridBagConstraints gbc_pathCombo = new GridBagConstraints();
        gbc_pathCombo.weightx = 1.0;
        gbc_pathCombo.fill = GridBagConstraints.BOTH;
        gbc_pathCombo.insets = new Insets(5, 0, 5, 5);
        gbc_pathCombo.gridx = 1;
        gbc_pathCombo.gridy = 1;
        add(dataSourceCombo, gbc_pathCombo);

        btnLoad = new JButton();
        GridBagConstraints gbc_browserButtonLoad = new GridBagConstraints();
        gbc_browserButtonLoad.insets = new Insets(3, 0, 5, 0);
        gbc_browserButtonLoad.fill = GridBagConstraints.BOTH;
        gbc_browserButtonLoad.anchor = GridBagConstraints.WEST;
        gbc_browserButtonLoad.gridx = 2;
        gbc_browserButtonLoad.gridy = 1;
        add(btnLoad, gbc_browserButtonLoad);

        JLabel ftLabel = new JLabel("Search:");
        GridBagConstraints gbc_ftLabel = new GridBagConstraints();
        gbc_ftLabel.anchor = GridBagConstraints.WEST;
        gbc_ftLabel.fill = GridBagConstraints.VERTICAL;
        gbc_ftLabel.insets = new Insets(0, 0, 5, 5);
        gbc_ftLabel.gridx = 0;
        gbc_ftLabel.gridy = 2;
        add(ftLabel, gbc_ftLabel);

        ftTextField = new JTextField();
        ftTextField.setEditable(true);
        GridBagConstraints gbc_ftTextField = new GridBagConstraints();
        gbc_ftTextField.weightx = 1.0;
        gbc_ftTextField.fill = GridBagConstraints.BOTH;
        gbc_ftTextField.insets = new Insets(5, 0, 5, 5);
        gbc_ftTextField.gridx = 1;
        gbc_ftTextField.gridy = 2;
        add(ftTextField, gbc_ftTextField);

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

    public JTextField getFtTextField() {
        return ftTextField;
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
