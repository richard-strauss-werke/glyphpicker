package com.aerhard.oxygen.plugin.glyphpicker.controller;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

import ca.odell.glazedlists.matchers.TextMatcherEditor;

public class GlyphSelect extends TextMatcherEditor<GlyphDefinition>
        implements DocumentListener {

    public void performUpdate(final DocumentEvent e) {

        String q;
        try {
            q = e.getDocument().getText(0, e.getDocument().getLength());
        } catch (BadLocationException e1) {
            q = "";
        }

        final String query = q;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setFilterText(new String[] {query});
            }
        });

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        performUpdate(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        performUpdate(e);
    }
}
