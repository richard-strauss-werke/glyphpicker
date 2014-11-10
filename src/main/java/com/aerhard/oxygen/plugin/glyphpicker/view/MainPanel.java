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
import com.jidesoft.swing.InfiniteProgressPanel;

import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ScrollPaneConstants;

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
    private JScrollPane scrollPane;

    public MainPanel() {
        init();
    }

    public InfiniteProgressPanel getLoadingMask() {
        return loadingMask;

    }

    public void setLoadingMask() {
        loadingMask = new InfiniteProgressPanel();
        // setGlassPane(loadingMask);
    }

    private void init() {

        setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new EmptyBorder(8, 8, 8, 8));
        add(tabbedPane, BorderLayout.CENTER);

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

        scrollPane = new JScrollPane(userList);
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

        // userButtonClose = new JButton("Close");
        // userButtonPanel.add(userButtonClose);

        JPanel browserPanel = new JPanel();
        tabbedPane.addTab(null, null, browserPanel, null);
        tabbedPane.setTabComponentAt(1, new JLabel("All Glyphs"));
        browserPanel.setLayout(new BorderLayout(0, 0));

        JPanel dataSourcePanel = new JPanel();
        dataSourcePanel.setBorder(new EmptyBorder(8, 8, 0, 8));
        browserPanel.add(dataSourcePanel, BorderLayout.NORTH);
        dataSourcePanel.setLayout(new BorderLayout(8, 0));

        JLabel dataSourceLabel = new JLabel("Data source:");
        dataSourcePanel.add(dataSourceLabel, BorderLayout.WEST);

        pathCombo = new JComboBox<String>();
        pathCombo.setEditable(true);
        dataSourcePanel.add(pathCombo, BorderLayout.CENTER);

        browserButtonLoad = new JButton("Load");
        dataSourcePanel.add(browserButtonLoad, BorderLayout.EAST);

        JPanel tablePanel = new JPanel();
        browserPanel.add(tablePanel, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(11, 8, 7, 4));
        tablePanel.setLayout(new BorderLayout(0, 0));

        table = new GlyphTable();
        table.setFillsViewportHeight(true);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // tableFilter = new TableFilterHeader(table, AutoChoices.ENABLED);

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

        // browserButtonClose = new JButton("Close");
        // browserButtonPanel.add(browserButtonClose);

        enableBrowserButtons(false);

        setLoadingMask();

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

    // public JButton getUserButtonClose() {
    // return userButtonClose;
    // }
    //
    // public JButton getBrowserButtonClose() {
    // return browserButtonClose;
    // }

    public JComboBox<String> getPathCombo() {
        return pathCombo;
    }

    public JButton getBrowserButtonLoad() {
        return browserButtonLoad;
    }

    // public TableFilterHeader getTableFilter() {
    // return tableFilter;
    // }

    public JList<GlyphModel> getUserList() {
        return userList;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

}
