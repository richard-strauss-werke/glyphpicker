package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;

public class SortAction extends AbstractPickerAction {
    private static final long serialVersionUID = 1L;

    private static String className = SortAction.class.getSimpleName();
    
    public SortAction() {
        super(className, "/images/sort-number.png");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
