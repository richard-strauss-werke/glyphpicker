package com.aerhard.oxygen.plugin.glyphpicker;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

public class TogglePickerWindowAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private StandalonePluginWorkspace workspace;

    private String viewId;

    TogglePickerWindowAction(StandalonePluginWorkspace workspace, String icon, String viewId) {
        super("GyphPicker", new ImageIcon(
                GlyphPickerPluginExtension.class.getResource(icon)));

        this.workspace = workspace;
        this.viewId=viewId;

        putValue(SHORT_DESCRIPTION, "Shows / hides the GlyphPicker window");

        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (workspace.isViewShowing(viewId)) {
            workspace.hideView(viewId);
        } else {
            workspace.showView(viewId, true);
        }
    }
}