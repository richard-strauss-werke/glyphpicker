package com.aerhard.oxygen.plugin.glyphpicker.view.userlist;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class UserListPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JList<GlyphDefinition> userList;
    private JButton btnRemove;
    private JButton btnInsert;

    public UserListPanel() {

        setLayout(new BorderLayout(0, 0));

        JPanel paletteTablePanel = new JPanel();
        paletteTablePanel.setBorder(new EmptyBorder(11, 8, 7, 8));
        add(paletteTablePanel);
        paletteTablePanel.setLayout(new BorderLayout(0, 0));

        userList = new JList<GlyphDefinition>();
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setCellRenderer(new ListItemRenderer());

        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        paletteTablePanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.GRAY));

        btnRemove = new JButton("Remove from Collection");
        btnRemove.setEnabled(false);
        buttonPanel.add(btnRemove);

        btnInsert = new JButton("Insert XML");
        btnInsert.setEnabled(false);
        buttonPanel.add(btnInsert);

    }

    public void enableUserButtons(Boolean enable) {
        btnInsert.setEnabled(enable);
        btnRemove.setEnabled(enable);

    }

    public JButton getBtnInsert() {
        return btnInsert;
    }

    public JButton getBtnRemove() {
        return btnRemove;
    }

    public JList<GlyphDefinition> getUserList() {
        return userList;
    }

}
