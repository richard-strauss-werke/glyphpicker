package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Component;

import javax.swing.Box;

public class UserCollectionControlPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JButton toggleBtn;

    public UserCollectionControlPanel() {
        setBorder(new EmptyBorder(8, 8, 0, 8));

        GridBagLayout gbl_dataSourcePanel = new GridBagLayout();
        gbl_dataSourcePanel.columnWidths = new int[] { 62, 199, 55, 0 };
        gbl_dataSourcePanel.rowHeights = new int[] { 0, 0, 0, 0 };
        gbl_dataSourcePanel.columnWeights = new double[] { 0.0, 1.0, 0.0,
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
        
        
        Component horizontalGlue = Box.createHorizontalGlue();
        GridBagConstraints gbc_horizontalGlue = new GridBagConstraints();
        gbc_horizontalGlue.fill = GridBagConstraints.BOTH;
        gbc_horizontalGlue.insets = new Insets(0, 0, 5, 5);
        gbc_horizontalGlue.gridx = 1;
        gbc_horizontalGlue.gridy = 0;
        add(horizontalGlue, gbc_horizontalGlue);

    }

    public JButton getToggleBtn() {
        return toggleBtn;
    }
    
}
