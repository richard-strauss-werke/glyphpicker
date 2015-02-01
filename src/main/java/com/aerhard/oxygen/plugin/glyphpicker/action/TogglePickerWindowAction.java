package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

public class TogglePickerWindowAction extends AbstractPickerAction {
    private static final long serialVersionUID = 1L;

    private StandalonePluginWorkspace workspace;

    private String viewId;

    private static String className = TogglePickerWindowAction.class
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