package com.aerhard.oxygen.plugin.glyphpicker;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.aerhard.oxygen.plugin.glyphpicker.controller.action.AbstractPickerAction;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

public class TogglePickerWindowAction extends AbstractPickerAction {
    private static final long serialVersionUID = 1L;

    private final StandalonePluginWorkspace workspace;

    private final String viewId;

    private static final String className = TogglePickerWindowAction.class
            .getSimpleName();

    public TogglePickerWindowAction(StandalonePluginWorkspace workspace,
            String icon, String viewId) {
        super("GyphPicker", new ImageIcon(
                TogglePickerWindowAction.class.getResource(icon)));

        this.workspace = workspace;
        this.viewId = viewId;

        String description = I18N.getString(className + ".description");

        putValue(SHORT_DESCRIPTION, description);

        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
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