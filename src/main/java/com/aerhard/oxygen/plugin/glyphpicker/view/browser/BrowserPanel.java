package com.aerhard.oxygen.plugin.glyphpicker.view.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;

import com.jidesoft.swing.AutoCompletionComboBox;

public class BrowserPanel extends JPanel  {

    private static final long serialVersionUID = 1L;

    private AutoCompletionComboBox classCombo;
    private AutoCompletionComboBox rangeCombo;
    private GlyphTable table;
    private JButton btnLoad;
    private JComboBox<String> pathCombo;

    private JButton btnAdd;

    private JButton btnInsert;

    public BrowserPanel() {
        setLayout(new BorderLayout(0, 0));

        JPanel dataSourcePanel = new JPanel();
        dataSourcePanel.setBorder(new EmptyBorder(8, 8, 0, 8));
        add(dataSourcePanel, BorderLayout.NORTH);
        GridBagLayout gbl_dataSourcePanel = new GridBagLayout();
        gbl_dataSourcePanel.columnWidths = new int[] { 62, 199, 55, 0 };
        gbl_dataSourcePanel.rowHeights = new int[] { 0, 0 };
        gbl_dataSourcePanel.columnWeights = new double[] { 0.0, 0.0, 0.0,
                Double.MIN_VALUE };
        gbl_dataSourcePanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        dataSourcePanel.setLayout(gbl_dataSourcePanel);

        JLabel dataSourceLabel = new JLabel("Data source:");
        GridBagConstraints gbc_dataSourceLabel = new GridBagConstraints();
        gbc_dataSourceLabel.anchor = GridBagConstraints.WEST;
        gbc_dataSourceLabel.fill = GridBagConstraints.VERTICAL;
        gbc_dataSourceLabel.insets = new Insets(0, 0, 0, 5);
        gbc_dataSourceLabel.gridx = 0;
        gbc_dataSourceLabel.gridy = 0;
        dataSourcePanel.add(dataSourceLabel, gbc_dataSourceLabel);

        pathCombo = new JComboBox<String>();
        pathCombo.setEditable(true);
        GridBagConstraints gbc_pathCombo = new GridBagConstraints();
        gbc_pathCombo.weightx = 1.0;
        gbc_pathCombo.fill = GridBagConstraints.BOTH;
        gbc_pathCombo.insets = new Insets(5, 0, 5, 5);
        gbc_pathCombo.gridx = 1;
        gbc_pathCombo.gridy = 0;
        dataSourcePanel.add(pathCombo, gbc_pathCombo);

        btnLoad = new JButton("Load");
        GridBagConstraints gbc_browserButtonLoad = new GridBagConstraints();
        gbc_browserButtonLoad.insets = new Insets(3, 0, 3, 0);
        gbc_browserButtonLoad.fill = GridBagConstraints.VERTICAL;
        gbc_browserButtonLoad.anchor = GridBagConstraints.WEST;
        gbc_browserButtonLoad.gridx = 2;
        gbc_browserButtonLoad.gridy = 0;
        dataSourcePanel.add(btnLoad, gbc_browserButtonLoad);

        JLabel rangeLabel = new JLabel("Range:");
        GridBagConstraints gbc_rangeLabel = new GridBagConstraints();
        gbc_rangeLabel.anchor = GridBagConstraints.WEST;
        gbc_rangeLabel.fill = GridBagConstraints.VERTICAL;
        gbc_rangeLabel.insets = new Insets(0, 0, 0, 5);
        gbc_rangeLabel.gridx = 0;
        gbc_rangeLabel.gridy = 1;
        dataSourcePanel.add(rangeLabel, gbc_rangeLabel);

        rangeCombo = new AutoCompletionComboBox();
        rangeCombo.setStrict(false);
        GridBagConstraints gbc_rangeCombo = new GridBagConstraints();
        gbc_rangeCombo.insets = new Insets(5, 0, 5, 5);
        gbc_rangeCombo.anchor = GridBagConstraints.NORTH;
        gbc_rangeCombo.fill = GridBagConstraints.HORIZONTAL;
        gbc_rangeCombo.gridx = 1;
        gbc_rangeCombo.gridy = 1;
        dataSourcePanel.add(rangeCombo, gbc_rangeCombo);

        JLabel classLabel = new JLabel("Class:");
        GridBagConstraints gbc_classLabel = new GridBagConstraints();
        gbc_classLabel.anchor = GridBagConstraints.WEST;
        gbc_classLabel.fill = GridBagConstraints.VERTICAL;
        gbc_classLabel.insets = new Insets(0, 0, 0, 5);
        gbc_classLabel.gridx = 0;
        gbc_classLabel.gridy = 2;
        dataSourcePanel.add(classLabel, gbc_classLabel);

        classCombo = new AutoCompletionComboBox();
        classCombo.setStrict(false);
        GridBagConstraints gbc_classCombo = new GridBagConstraints();
        gbc_classCombo.insets = new Insets(5, 0, 5, 5);
        gbc_classCombo.anchor = GridBagConstraints.NORTH;
        gbc_classCombo.fill = GridBagConstraints.HORIZONTAL;
        gbc_classCombo.gridx = 1;
        gbc_classCombo.gridy = 2;
        dataSourcePanel.add(classCombo, gbc_classCombo);

        JPanel tablePanel = new JPanel();
        add(tablePanel, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(11, 8, 7, 8));
        tablePanel.setLayout(new BorderLayout(0, 12));

        table = new GlyphTable();
        table.setFillsViewportHeight(true);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane jpane = new JScrollPane(table);
        jpane.setBorder(new EtchedBorder());
        tablePanel.add(jpane, BorderLayout.CENTER);

        JPanel browserButtonPanel = new JPanel();
        browserButtonPanel.setBorder(new MatteBorder(1, 0, 0, 0,
                (Color) Color.GRAY));
        add(browserButtonPanel, BorderLayout.SOUTH);

        btnAdd = new JButton("Add to User Collection");
        browserButtonPanel.add(btnAdd);

        btnInsert = new JButton("Insert XML");
        browserButtonPanel.add(btnInsert);

    }

    public void enableBrowserButtons(Boolean enable) {
        btnAdd.setEnabled(enable);
        btnInsert.setEnabled(enable);

    }

    public GlyphTable getTable() {
        return table;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnInsert() {
        return btnInsert;
    }

    public JButton getBtnLoad() {
        return btnLoad;
    }

    public JComboBox<String> getPathCombo() {
        return pathCombo;
    }

    public AutoCompletionComboBox getRangeCombo() {
        return rangeCombo;
    }

    public AutoCompletionComboBox getClassCombo() {
        return classCombo;
    }

}
