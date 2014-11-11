package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import java.awt.Color;

import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphModel;
import com.jidesoft.swing.AutoCompletionComboBox;
import com.jidesoft.swing.InfiniteProgressPanel;

import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ScrollPaneConstants;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MainPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    // private DetailPanel detailView;
    private GlyphTable table;
    private InfiniteProgressPanel loadingMask;
    private JButton browserButtonAdd;
    private JButton browserButtonInsert;
    private JButton userButtonInsert;
    private JButton userButtonRemove;
    // private JButton userButtonClose;
    // private JButton browserButtonClose;
    private JComboBox<String> pathCombo;
    private JButton browserButtonLoad;
    // private TableFilterHeader tableFilter;
    private JList<GlyphModel> userList;
    private JTabbedPane tabbedPane;
    private AutoCompletionComboBox rangeCombo;
    private AutoCompletionComboBox classCombo;

    public MainPanel() {
        setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new EmptyBorder(8, 8, 8, 8));
        add(tabbedPane, BorderLayout.CENTER);

        createUserListPanel();
        createBrowserPanel();

        setMinimumSize(new Dimension(200, 200));

        setLoadingMask();
    }

    public InfiniteProgressPanel getLoadingMask() {
        return loadingMask;

    }

    public void setLoadingMask() {
        loadingMask = new InfiniteProgressPanel();
        // setGlassPane(loadingMask);
    }

    private void createBrowserPanel() {
        JPanel browserPanel = new JPanel();
        tabbedPane.addTab(null, null, browserPanel, null);
        tabbedPane.setTabComponentAt(1, new JLabel("All Glyphs"));
        browserPanel.setLayout(new BorderLayout(0, 0));

        JPanel dataSourcePanel = new JPanel();
        dataSourcePanel.setBorder(new EmptyBorder(8, 8, 0, 8));
        browserPanel.add(dataSourcePanel, BorderLayout.NORTH);
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

        browserButtonLoad = new JButton("Load");
        GridBagConstraints gbc_browserButtonLoad = new GridBagConstraints();
        gbc_browserButtonLoad.insets = new Insets(3, 0, 3, 0);
        gbc_browserButtonLoad.fill = GridBagConstraints.VERTICAL;
        gbc_browserButtonLoad.anchor = GridBagConstraints.WEST;
        gbc_browserButtonLoad.gridx = 2;
        gbc_browserButtonLoad.gridy = 0;
        dataSourcePanel.add(browserButtonLoad, gbc_browserButtonLoad);

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
        browserPanel.add(tablePanel, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(11, 8, 7, 4));
        tablePanel.setLayout(new BorderLayout(0, 12));

        table = new GlyphTable();
        table.setFillsViewportHeight(true);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane jpane = new JScrollPane(table);
        jpane.setBorder(new EtchedBorder());
        tablePanel.add(jpane, BorderLayout.CENTER);

        // detailView = new DetailPanel();
        // browserPanel.add(detailView, BorderLayout.EAST);
        // detailView.setBorder(new EmptyBorder(4, 4, 4, 4));

        JPanel browserButtonPanel = new JPanel();
        browserButtonPanel.setBorder(new MatteBorder(1, 0, 0, 0,
                (Color) Color.GRAY));
        browserPanel.add(browserButtonPanel, BorderLayout.SOUTH);

        browserButtonAdd = new JButton("Add to User Collection");
        browserButtonPanel.add(browserButtonAdd);

        browserButtonInsert = new JButton("Insert XML");
        browserButtonPanel.add(browserButtonInsert);
    }

    private void createUserListPanel() {
        JPanel userPalettePanel = new JPanel();
        tabbedPane.addTab(null, null, userPalettePanel, null);
        tabbedPane.setTabComponentAt(0, new JLabel("User Collection"));
        userPalettePanel.setLayout(new BorderLayout(0, 0));

        JPanel paletteTablePanel = new JPanel();
        paletteTablePanel.setBorder(new EmptyBorder(11, 8, 7, 8));
        userPalettePanel.add(paletteTablePanel);
        paletteTablePanel.setLayout(new BorderLayout(0, 0));

        userList = new JList<GlyphModel>();
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setCellRenderer(new ListItemRenderer());

        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        paletteTablePanel.add(scrollPane);

        JPanel userButtonPanel = new JPanel();
        userPalettePanel.add(userButtonPanel, BorderLayout.SOUTH);
        userButtonPanel.setBorder(new MatteBorder(1, 0, 0, 0,
                (Color) Color.GRAY));

        userButtonRemove = new JButton("Remove from Collection");
        userButtonRemove.setEnabled(false);
        userButtonPanel.add(userButtonRemove);

        userButtonInsert = new JButton("Insert XML");
        userButtonInsert.setEnabled(false);
        userButtonPanel.add(userButtonInsert);
    }

    public void enableBrowserButtons(Boolean enable) {
        browserButtonAdd.setEnabled(enable);
        browserButtonInsert.setEnabled(enable);

    }

    public void enableUserButtons(Boolean enable) {
        userButtonInsert.setEnabled(enable);
        userButtonRemove.setEnabled(enable);

    }

    // public DetailPanel getDetailView() {
    // return detailView;
    // }

    public GlyphTable getTable() {
        return table;
    }

    public JButton getBrowserButtonAdd() {
        return browserButtonAdd;
    }

    public JButton getBrowserButtonInsert() {
        return browserButtonInsert;
    }

    public JButton getUserButtonInsert() {
        return userButtonInsert;
    }

    public JButton getUserButtonRemove() {
        return userButtonRemove;
    }

    public JComboBox<String> getPathCombo() {
        return pathCombo;
    }

    public JButton getBrowserButtonLoad() {
        return browserButtonLoad;
    }

    public JList<GlyphModel> getUserList() {
        return userList;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public AutoCompletionComboBox getRangeFilterCombo() {
        return rangeCombo;
    }

    public AutoCompletionComboBox getClassFilterCombo() {
        return classCombo;
    }

}
