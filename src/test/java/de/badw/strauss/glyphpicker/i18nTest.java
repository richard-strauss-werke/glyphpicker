package de.badw.strauss.glyphpicker;

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
                i18n.getString("MemorizedCharactersLoader.couldNotCreateFolder"),
                "xxx"));
        assertEquals("Loading data ...",
                i18n.getString("TabPanel.loading"));
    }

    /**
     * Tests the German language file.
     */
    @Test
    public void testGermanLanguage() {
        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker",
                Locale.GERMAN);
        assertEquals("Der Ordner \"xxx\" konnte nicht angelegt werden.", String.format(
                i18n.getString("MemorizedCharactersLoader.couldNotCreateFolder"),
                "xxx"));
        assertEquals("Lade Daten ...",
                i18n.getString("TabPanel.loading"));
    }

}
