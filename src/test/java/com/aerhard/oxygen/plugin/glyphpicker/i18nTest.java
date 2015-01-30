package com.aerhard.oxygen.plugin.glyphpicker;

import static org.junit.Assert.*;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class i18nTest {

    /**
     * Tests the default language file.
     */
    @Test
    public void testDefaultLanguage() {
        Locale.setDefault(Locale.ENGLISH);
        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");

        assertEquals("Could not create folder \"xxx\".", String.format(
                i18n.getString("UserCollectionLoader.couldNotCreateFolder"),
                "xxx"));
        assertEquals("Loading data ...",
                i18n.getString("ContainerPanel.loading"));
    }

    /**
     * Tests the German language file.
     */
    @Test
    public void testGermanLanguage() {
        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker",
                Locale.GERMAN);
        assertEquals("Konfigurations-Datei 'xxx' nicht gefunden.",
                String.format(i18n.getString("configStore.fileNotFoundError"),
                        "xxx"));
    }

}
