package de.badw.strauss.glyphpicker;

import com.icl.saxon.aelfred.SAXParserFactoryImpl;
import de.badw.strauss.glyphpicker.controller.alltab.TeiXmlHandler;
import de.badw.strauss.glyphpicker.model.DataSource;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class TeiXmlHandlerTest {

    private List<GlyphDefinition> transform(String file, boolean parseMapping, String type) throws ParserConfigurationException, SAXException, IOException, TeiXmlHandler.RecursionException {
        SAXParserFactory parserFactory = SAXParserFactoryImpl.newInstance();
        SAXParser parser = parserFactory.newSAXParser();

        InputStream res = TeiXmlHandler.class.getResourceAsStream(file);

        DataSource ds = new DataSource();
        ds.setParseMapping(parseMapping);
        ds.setTypeAttributeValue(type);

        TeiXmlHandler handler = new TeiXmlHandler(ds);
        parser.parse(res, handler);
        handler.resolveReferences();
        return handler.getGlyphDefinitions();
    }
    
    @Test
    public void basicTest() throws ParserConfigurationException, SAXException, IOException, TeiXmlHandler.RecursionException {
        List<GlyphDefinition> result = transform("/charDecl.xml", true, "MUFI");
        assertNull(result.get(2).getId());
        assertEquals("LATIN SMALL LIGATURE AA CLOSED FORM", result.get(2).getCharName());
        assertEquals("\uF204", result.get(2).getMappedChars());
        assertEquals("U+F204 ", result.get(2).getCodePointString());
    }

    @Test
    public void referencesTest() throws ParserConfigurationException, SAXException, IOException, TeiXmlHandler.RecursionException {
        List<GlyphDefinition> result = transform("/charDecl.xml", false, null);
        assertEquals("abCDefX", result.get(5).getMappedChars());
        assertEquals("0abCDefX", result.get(4).getMappedChars());
        assertEquals("0abCDefX", result.get(8).getMappedChars());
        assertEquals("ok", result.get(9).getMappedChars());
    }

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void recursionExceptionTest1() throws ParserConfigurationException, SAXException, IOException, TeiXmlHandler.RecursionException {
        thrown.expect(TeiXmlHandler.RecursionException.class);
        transform("/charDecl.xml", false, "infinite-recursion-1");
    }

    @Test
    public void recursionExceptionTest2() throws ParserConfigurationException, SAXException, IOException, TeiXmlHandler.RecursionException {
        thrown.expect( TeiXmlHandler.RecursionException.class );
        transform("/charDecl.xml", false, "infinite-recursion-2");
    }
    
}
