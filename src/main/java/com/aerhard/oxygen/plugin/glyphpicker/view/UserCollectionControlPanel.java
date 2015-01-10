package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import java.awt.Component;

import javax.swing.Box;

public class UserCollectionControlPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JButton toggleBtn;
    private JToolBar toolBar;
    private Component horizontalGlue;

    public UserCollectionControlPanel() {
        setBorder(new EmptyBorder(8, 8, 0, 8));

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[] { 62, 199, 55, 0 };
        gbl.rowHeights = new int[] { 0, 0, 0, 0 };
        gbl.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
        gbl.rowWeights = new double[] { 0.0, Double.MIN_VALUE, 0.0, 0.0 };
        setLayout(gbl);

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
        
        toggleBtn = new JButton();
        toolBar.add(toggleBtn);
    }

    public JButton getToggleBtn() {
        return toggleBtn;
    }

}
