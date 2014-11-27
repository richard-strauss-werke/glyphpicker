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

import com.jidesoft.swing.AutoCompletionComboBox;

public class BrowserPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private AutoCompletionComboBox classCombo;
    private AutoCompletionComboBox rangeCombo;
    private JButton btnLoad;
    private JComboBox<String> pathCombo;
    private JComboBox<String> viewCombo;
    
    private JScrollPane jPane;
    
    private JButton btnAdd;

    private HighlightButton btnInsert;
    private JTextField infoLabel;
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
        gbl_dataSourcePanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE, 0.0, 0.0 };
        dataSourcePanel.setLayout(gbl_dataSourcePanel);

        JLabel dataSourceLabel = new JLabel("Data source:");
        GridBagConstraints gbc_dataSourceLabel = new GridBagConstraints();
        gbc_dataSourceLabel.anchor = GridBagConstraints.WEST;
        gbc_dataSourceLabel.fill = GridBagConstraints.VERTICAL;
        gbc_dataSourceLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dataSourceLabel.gridx = 0;
        gbc_dataSourceLabel.gridy = 1;
        dataSourcePanel.add(dataSourceLabel, gbc_dataSourceLabel);

        pathCombo = new JComboBox<String>();
        pathCombo.setEditable(true);
        GridBagConstraints gbc_pathCombo = new GridBagConstraints();
        gbc_pathCombo.weightx = 1.0;
        gbc_pathCombo.fill = GridBagConstraints.BOTH;
        gbc_pathCombo.insets = new Insets(5, 0, 5, 5);
        gbc_pathCombo.gridx = 1;
        gbc_pathCombo.gridy = 1;
        dataSourcePanel.add(pathCombo, gbc_pathCombo);

        btnLoad = new JButton();
        GridBagConstraints gbc_browserButtonLoad = new GridBagConstraints();
        gbc_browserButtonLoad.insets = new Insets(3, 0, 5, 0);
        gbc_browserButtonLoad.fill = GridBagConstraints.VERTICAL;
        gbc_browserButtonLoad.anchor = GridBagConstraints.WEST;
        gbc_browserButtonLoad.gridx = 2;
        gbc_browserButtonLoad.gridy = 1;
        dataSourcePanel.add(btnLoad, gbc_browserButtonLoad);

        JLabel rangeLabel = new JLabel("Range:");
        GridBagConstraints gbc_rangeLabel = new GridBagConstraints();
        gbc_rangeLabel.anchor = GridBagConstraints.WEST;
        gbc_rangeLabel.fill = GridBagConstraints.VERTICAL;
        gbc_rangeLabel.insets = new Insets(0, 0, 5, 5);
        gbc_rangeLabel.gridx = 0;
        gbc_rangeLabel.gridy = 2;
        dataSourcePanel.add(rangeLabel, gbc_rangeLabel);

        rangeCombo = new AutoCompletionComboBox();
        rangeCombo.setStrict(false);
        GridBagConstraints gbc_rangeCombo = new GridBagConstraints();
        gbc_rangeCombo.insets = new Insets(5, 0, 5, 5);
        gbc_rangeCombo.anchor = GridBagConstraints.NORTH;
        gbc_rangeCombo.fill = GridBagConstraints.HORIZONTAL;
        gbc_rangeCombo.gridx = 1;
        gbc_rangeCombo.gridy = 2;
        dataSourcePanel.add(rangeCombo, gbc_rangeCombo);

        viewCombo = new JComboBox<String>();
        viewCombo.addItem("List");
        viewCombo.addItem("Grid");
        GridBagConstraints gbc_viewCombo = new GridBagConstraints();
        gbc_viewCombo.insets = new Insets(3, 0, 5, 0);
        gbc_viewCombo.fill = GridBagConstraints.BOTH;
        gbc_viewCombo.anchor = GridBagConstraints.WEST;
        gbc_viewCombo.gridx = 2;
        gbc_viewCombo.gridy = 0;
        dataSourcePanel.add(viewCombo, gbc_viewCombo);
        
        JLabel classLabel = new JLabel("Class:");
        GridBagConstraints gbc_classLabel = new GridBagConstraints();
        gbc_classLabel.anchor = GridBagConstraints.WEST;
        gbc_classLabel.fill = GridBagConstraints.VERTICAL;
        gbc_classLabel.insets = new Insets(0, 0, 0, 5);
        gbc_classLabel.gridx = 0;
        gbc_classLabel.gridy = 3;
        dataSourcePanel.add(classLabel, gbc_classLabel);

        classCombo = new AutoCompletionComboBox();
        classCombo.setStrict(false);
        GridBagConstraints gbc_classCombo = new GridBagConstraints();
        gbc_classCombo.insets = new Insets(5, 0, 0, 5);
        gbc_classCombo.anchor = GridBagConstraints.NORTH;
        gbc_classCombo.fill = GridBagConstraints.HORIZONTAL;
        gbc_classCombo.gridx = 1;
        gbc_classCombo.gridy = 3;
        dataSourcePanel.add(classCombo, gbc_classCombo);

        JPanel tablePanel = new JPanel();
        add(tablePanel, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(11, 8, 7, 8));
        tablePanel.setLayout(new BorderLayout(0, 12));

        jPane = new JScrollPane();
//        jPane.setLayout(new GridLayout(1,1));
//        jpane.setHorizontalScrollBarPolicy(JideScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        jpane.setVerticalScrollBarPolicy(JideScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        jPane.setBorder(new EtchedBorder());
        tablePanel.add(jPane, BorderLayout.CENTER);
        
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

    public JButton getBtnLoad() {
        return btnLoad;
    }

    public JComboBox<String> getPathCombo() {
        return pathCombo;
    }

    public JComboBox<String> getViewCombo() {
        return viewCombo;
    }
    
    public AutoCompletionComboBox getRangeCombo() {
        return rangeCombo;
    }

    public AutoCompletionComboBox getClassCombo() {
        return classCombo;
    }
    
    public void setListComponent(JComponent component){
        jPane.setViewportView(component);
    }
    
    public JTextField getInfoLabel() {
        return infoLabel;
    }

}
