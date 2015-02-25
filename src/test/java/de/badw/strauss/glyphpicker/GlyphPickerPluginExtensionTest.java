package de.badw.strauss.glyphpicker;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GlyphPickerPluginExtensionTest {

    /**
     * Tests the insertNs function.
     */
    @Test
    public void insertNsTest() {

        GlyphPickerPluginExtension ext = new GlyphPickerPluginExtension();

        String ns = " xmlns=\"http://www.tei-c.org/ns/1.0\"";

        Map<String, String> tests = new HashMap<String, String>();
        tests.put("<a/>", "<a"+ns+"/>");
        tests.put("<a><b/></a>", "<a"+ns+"><b/></a>");
        tests.put("<a s=\"n\"/>", "<a"+ns+" s=\"n\"/>");
        tests.put("<a s=\"n\"><b/></a>", "<a"+ns+" s=\"n\"><b/></a>");
        tests.put("<a s=\"n>\"/>", "<a"+ns+" s=\"n>\"/>");
        tests.put("<a s=\"n>\"><b/></a>", "<a"+ns+" s=\"n>\"><b/></a>");
        tests.put("", "");
        tests.put(null, null);
        tests.put("&#xE001;", "&#xE001;");

        for (String key : tests.keySet()) {
            String result = ext.addNamespace(key);
            assertEquals(tests.get(key), result);
        }
    }


}
