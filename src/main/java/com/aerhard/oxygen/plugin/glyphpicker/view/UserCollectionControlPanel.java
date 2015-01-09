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

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[] { 62, 199, 55, 0 };
        gbl.rowHeights = new int[] { 0, 0, 0, 0 };
        gbl.columnWeights = new double[] { 0.0, 1.0, 0.0,
                Double.MIN_VALUE };
        gbl.rowWeights = new double[] { 0.0, Double.MIN_VALUE,
                0.0, 0.0 };
        setLayout(gbl);

        toggleBtn = new JButton();
        GridBagConstraints gbcToggleBtn = new GridBagConstraints();
        gbcToggleBtn.insets = new Insets(5, 0, 5, 0);
        gbcToggleBtn.fill = GridBagConstraints.BOTH;
        gbcToggleBtn.anchor = GridBagConstraints.WEST;
        gbcToggleBtn.gridx = 2;
        gbcToggleBtn.gridy = 0;
        add(toggleBtn, gbcToggleBtn);

        Component horizontalGlue = Box.createHorizontalGlue();
        GridBagConstraints gbcHorizontalGlue = new GridBagConstraints();
        gbcHorizontalGlue.fill = GridBagConstraints.BOTH;
        gbcHorizontalGlue.insets = new Insets(0, 0, 5, 5);
        gbcHorizontalGlue.gridx = 1;
        gbcHorizontalGlue.gridy = 0;
        add(horizontalGlue, gbcHorizontalGlue);

    }

    public JButton getToggleBtn() {
        return toggleBtn;
    }

}
