package de.badw.strauss.glyphpicker;

import com.icl.saxon.aelfred.SAXParserFactoryImpl;
import de.badw.strauss.glyphpicker.controller.browser.TeiXmlHandler;
import de.badw.strauss.glyphpicker.model.DataSource;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import de.badw.strauss.glyphpicker.model.GlyphDefinitions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class TeiXmlHandlerTest {

    private List<GlyphDefinition> transform(String file)  throws ParserConfigurationException, SAXException, IOException{

        SAXParserFactory parserFactory = SAXParserFactoryImpl.newInstance();
        SAXParser parser = parserFactory.newSAXParser();

        InputStream res = TeiXmlHandler.class.getResourceAsStream(file);

        DataSource ds = new DataSource();
        ds.setParseMapping(true);

        TeiXmlHandler handler = new TeiXmlHandler(ds);
        parser.parse(res, handler);
        return handler.getGlyphDefinitions();
    }
    
    @Test
    public void testHandler() throws ParserConfigurationException, SAXException, IOException {

        List<GlyphDefinition> result;
        
        result = transform("/charDecl.xml");
        
        assertNull(result.get(2).getId());
        assertEquals("LATIN SMALL LIGATURE AA CLOSED FORM", result.get(2).getCharName());
        assertEquals("\uF204", result.get(2).getMappedChars());
        assertEquals("U+F204 ", result.get(2).getCodePointString());

    }


}
