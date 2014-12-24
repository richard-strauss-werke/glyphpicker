package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;

import com.jidesoft.swing.DefaultOverlayable;

public class BrowserPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // private AutoCompletionComboBox classCombo;
    // private JComboBox<String> rangeCombo;
    private JButton btnLoad;
    private JComboBox<String> dataSourceCombo;
    private JComboBox<String> viewCombo;

    private JScrollPane jPane;

    private DefaultOverlayable overlayable;

    private JButton btnAdd;

    private HighlightButton btnInsert;
    private JTextField infoLabel;

    private JTextField ftTextField;

    public BrowserPanel() {
        setLayout(new BorderLayout(0, 0));

        JPanel dataSourcePanel = new JPanel();
        dataSourcePanel.setBorder(new EmptyBorder(8, 8, 0, 8));
        add(dataSourcePanel, BorderLayout.NORTH);
        GridBagLayout gbl_dataSourcePanel = new GridBagLayout();
        gbl_dataSourcePanel.columnWidths = new int[] { 62, 199, 55, 0 };
        gbl_dataSourcePanel.rowHeights = new int[] { 0, 0, 0, 0 };
        gbl_dataSourcePanel.columnWeights = new double[] { 0.0, 0.0, 0.0,
                Double.MIN_VALUE };
        gbl_dataSourcePanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE,
                0.0, 0.0 };
        dataSourcePanel.setLayout(gbl_dataSourcePanel);

        viewCombo = new JComboBox<String>();
        viewCombo.addItem("Grid");
        viewCombo.addItem("List");
        GridBagConstraints gbc_viewCombo = new GridBagConstraints();
        gbc_viewCombo.insets = new Insets(3, 0, 5, 0);
        gbc_viewCombo.fill = GridBagConstraints.BOTH;
        gbc_viewCombo.anchor = GridBagConstraints.WEST;
        gbc_viewCombo.gridx = 2;
        gbc_viewCombo.gridy = 0;
        dataSourcePanel.add(viewCombo, gbc_viewCombo);

        JLabel dataSourceLabel = new JLabel("Data source:");
        GridBagConstraints gbc_dataSourceLabel = new GridBagConstraints();
        gbc_dataSourceLabel.anchor = GridBagConstraints.WEST;
        gbc_dataSourceLabel.fill = GridBagConstraints.VERTICAL;
        gbc_dataSourceLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dataSourceLabel.gridx = 0;
        gbc_dataSourceLabel.gridy = 1;
        dataSourcePanel.add(dataSourceLabel, gbc_dataSourceLabel);

        dataSourceCombo = new JComboBox<String>();
        dataSourceCombo.setEditable(false);
        GridBagConstraints gbc_pathCombo = new GridBagConstraints();
        gbc_pathCombo.weightx = 1.0;
        gbc_pathCombo.fill = GridBagConstraints.BOTH;
        gbc_pathCombo.insets = new Insets(5, 0, 5, 5);
        gbc_pathCombo.gridx = 1;
        gbc_pathCombo.gridy = 1;
        dataSourcePanel.add(dataSourceCombo, gbc_pathCombo);

        btnLoad = new JButton();
        GridBagConstraints gbc_browserButtonLoad = new GridBagConstraints();
        gbc_browserButtonLoad.insets = new Insets(3, 0, 5, 0);
        gbc_browserButtonLoad.fill = GridBagConstraints.VERTICAL;
        gbc_browserButtonLoad.anchor = GridBagConstraints.WEST;
        gbc_browserButtonLoad.gridx = 2;
        gbc_browserButtonLoad.gridy = 1;
        dataSourcePanel.add(btnLoad, gbc_browserButtonLoad);

        JLabel ftLabel = new JLabel("Search:");
        GridBagConstraints gbc_ftLabel = new GridBagConstraints();
        gbc_ftLabel.anchor = GridBagConstraints.WEST;
        gbc_ftLabel.fill = GridBagConstraints.VERTICAL;
        gbc_ftLabel.insets = new Insets(0, 0, 5, 5);
        gbc_ftLabel.gridx = 0;
        gbc_ftLabel.gridy = 2;
        dataSourcePanel.add(ftLabel, gbc_ftLabel);

        ftTextField = new JTextField();
        ftTextField.setEditable(true);
        GridBagConstraints gbc_ftTextField = new GridBagConstraints();
        gbc_ftTextField.weightx = 1.0;
        gbc_ftTextField.fill = GridBagConstraints.BOTH;
        gbc_ftTextField.insets = new Insets(5, 0, 5, 5);
        gbc_ftTextField.gridx = 1;
        gbc_ftTextField.gridy = 2;
        dataSourcePanel.add(ftTextField, gbc_ftTextField);

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

        JPanel tablePanel = new JPanel();
        add(tablePanel, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(11, 8, 7, 8));
        tablePanel.setLayout(new BorderLayout(0, 12));

        jPane = new JScrollPane();
        // jPane.setLayout(new GridLayout(1,1));
        // jpane.setHorizontalScrollBarPolicy(JideScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // jpane.setVerticalScrollBarPolicy(JideScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jPane.setBorder(new EtchedBorder());

        overlayable = new DefaultOverlayable(jPane, new JLabel(
                "Loading data ..."));
        tablePanel.add(overlayable, BorderLayout.CENTER);

        infoLabel = new JTextField(null);
        infoLabel.setEditable(false);
        tablePanel.add(infoLabel, BorderLayout.SOUTH);

        JPanel browserButtonPanel = new JPanel();
        browserButtonPanel.setBorder(new MatteBorder(1, 0, 0, 0,
                (Color) Color.GRAY));
        add(browserButtonPanel, BorderLayout.SOUTH);

        btnInsert = new HighlightButton();
        browserButtonPanel.add(btnInsert);

        btnAdd = new JButton();
        browserButtonPanel.add(btnAdd);

    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public HighlightButton getBtnInsert() {
        return btnInsert;
    }

    public JButton getBtnConfigure() {
        return btnLoad;
    }

    public JComboBox<String> getDataSourceCombo() {
        return dataSourceCombo;
    }

    public JComboBox<String> getViewCombo() {
        return viewCombo;
    }

    public JTextField getFtTextField() {
        return ftTextField;
    }

    // public JComboBox<String> getRangeCombo() {
    // return rangeCombo;
    // }
    //
    // public AutoCompletionComboBox getClassCombo() {
    // return classCombo;
    // }

    public void setListComponent(JComponent component) {
        jPane.setViewportView(component);
        // jPane.getViewport().removeAll();
        // jPane.getViewport().add(component);
    }

    public JTextField getInfoLabel() {
        return infoLabel;
    }

    /**
     * @return the jPane
     */
    public JScrollPane getjPane() {
        return jPane;
    }

    public DefaultOverlayable getOverlayable() {
        return overlayable;
    }

}
