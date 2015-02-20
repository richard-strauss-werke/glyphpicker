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
package de.badw.strauss.glyphpicker.controller.browser;

import com.icl.saxon.aelfred.SAXParserFactoryImpl;
import de.badw.strauss.glyphpicker.model.GlyphTable;
import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * A worker loading glyph definitions from a document or online resource.
 */
public class TeiLoadWorker extends SwingWorker<List<GlyphDefinition>, Void> {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(TeiLoadWorker.class
            .getName());

    /**
     * The data source object providing the loading parameters.
     */
    private final GlyphTable glyphTable;

    /**
     * The loading result, a list of GlyphDefinition objects.
     */
    private List<GlyphDefinition> result = null;

    /**
     * returns the resulting glyph list.
     *
     * @return the result list
     */
    public List<GlyphDefinition> getResult() {
        return result;
    }

    /**
     * The SAX parser.
     */
    private SAXParser parser;

    /**
     * The i18n resource bundle.
     */
    private final ResourceBundle i18n;

    /**
     * Instantiates a new TeiLoadWorker.
     *
     * @param glyphTable The data source object providing the loading parameters
     */
    public TeiLoadWorker(GlyphTable glyphTable) {
        this.glyphTable = glyphTable;

        i18n = ResourceBundle.getBundle("GlyphPicker");

        SAXParserFactory parserFactory = SAXParserFactoryImpl.newInstance();
        try {
            parser = parserFactory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            LOGGER.error(e);
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.SwingWorker#doInBackground()
     */
    @Override
    protected List<GlyphDefinition> doInBackground() {
        return loadData();
    }

    /* (non-Javadoc)
     * @see javax.swing.SwingWorker#done()
     */
    @Override
    protected void done() {
        try {
            result = get();
        } catch (InterruptedException | ExecutionException | CancellationException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Load data.
     *
     * @return the list
     */
    public List<GlyphDefinition> loadData() {
        String path = glyphTable.getBasePath();
        return (isLocalFile(path)) ? loadDataFromFile()
                : loadDataFromUrl();
    }

    /**
     * Checks if the provided path points to a local file.
     *
     * @param path the path
     * @return the result
     */
    private Boolean isLocalFile(String path) {
        return (!path.matches("^\\w+://.*"));
    }


    /**
     * Loads TEI data from a URL.
     *
     * @return the resulting GlyphDefinition list
     */
    public List<GlyphDefinition> loadDataFromUrl() {
        SystemDefaultHttpClient httpClient = new SystemDefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(glyphTable.getBasePath());
            return httpClient.execute(httpGet, new XMLResponseHandler());
        } catch (IOException e) {
            String message = String.format(
                    i18n.getString(this.getClass().getSimpleName()
                            + ".couldNotLoadData"),
                    glyphTable.getBasePath());
            if (e instanceof UnknownHostException) {
                message += " Unknown host";
            }
            JOptionPane.showMessageDialog(
                    null,
                    message,
                    i18n.getString(this.getClass().getSimpleName() + ".error"),
                    JOptionPane.ERROR_MESSAGE);
            LOGGER.info(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return null;
    }

    /**
     * The XML response handler.
     */
    private class XMLResponseHandler implements
            ResponseHandler<List<GlyphDefinition>> {

        /* (non-Javadoc)
         * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
         */
        @Override
        public List<GlyphDefinition> handleResponse(final HttpResponse response)
                throws IOException {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() >= 300) {
                throw new HttpResponseException(statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());
            }
            if (entity == null) {
                throw new ClientProtocolException(
                        "Response contains no content");
            }

            InputStream inputStream = entity.getContent();

            return parseXmlSax(inputStream);
        }
    }

    /**
     * Loads data from a file.
     *
     * @return the resulting GlyphDefinition list
     */
    public List<GlyphDefinition> loadDataFromFile() {

        String fileName = glyphTable.getBasePath();

        if (fileName != null) {
            File file = new File(fileName);
            InputStream inputStream = null;

            try {
                inputStream = new FileInputStream(file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        null,
                        String.format(
                                i18n.getString(this.getClass().getSimpleName()
                                        + ".couldNotLoadData"), fileName),
                        i18n.getString(this.getClass().getSimpleName()
                                + ".error"), JOptionPane.ERROR_MESSAGE);
                LOGGER.info(e);
            }
            if (inputStream != null) {
                return parseXmlSax(inputStream);
            }
        }
        return null;
    }

    /**
     * Triggers parsing of the XML input stream.
     *
     * @param is the input stream
     * @return the resulting GlyphDefinition list
     */
    public List<GlyphDefinition> parseXmlSax(InputStream is) {

        TeiXmlHandler handler = new TeiXmlHandler(glyphTable);
        try {
            parser.parse(is, handler);
        } catch (SAXException | IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.toString(),
                    i18n.getString(this.getClass().getSimpleName()
                            + ".xmlParsingError"), JOptionPane.ERROR_MESSAGE);
        }

        return handler.getGlyphDefinitions();
    }

}
