/**
 * Copyright 2015 Alexander Erhard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.badw.strauss.glyphpicker.controller.autocomplete;

import ca.odell.glazedlists.matchers.TextMatcherEditor;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * A TextMatcherEditor handling glyph list filtering triggered by changes in the
 * autocomplete combo's editor box.
 */
public class GlyphSelect extends TextMatcherEditor<GlyphDefinition> implements
        DocumentListener {

    /**
     * Instantiates a new GlyphSelect object.
     */
    public GlyphSelect() {
        setMode(TextMatcherEditor.CONTAINS);
    }

    /**
     * Perform update.
     *
     * @param e the event
     */
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
                setFilterText(new String[]{query});
            }
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.
     * DocumentEvent)
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.
     * DocumentEvent)
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        performUpdate(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.
     * DocumentEvent)
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        performUpdate(e);
    }
}
