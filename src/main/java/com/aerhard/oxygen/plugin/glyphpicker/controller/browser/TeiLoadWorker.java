package com.aerhard.oxygen.plugin.glyphpicker.controller.browser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.icl.saxon.aelfred.SAXParserFactoryImpl;

public class TeiLoadWorker extends SwingWorker<List<GlyphDefinition>, Void> {

    private static final Logger LOGGER = Logger.getLogger(TeiLoadWorker.class
            .getName());

    private DataSource dataSource;

    private List<GlyphDefinition> result = null;

    public List<GlyphDefinition> getResult() {
        return result;
    }

    private SAXParser parser;

    private ResourceBundle i18n;

    public TeiLoadWorker(DataSource dataSource) {
        this.dataSource = dataSource;

        i18n = ResourceBundle.getBundle("GlyphPicker");

        SAXParserFactory parserFactory = SAXParserFactoryImpl.newInstance();
        try {
            parser = parserFactory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            LOGGER.error(e);
        }
    }

    @Override
    protected List<GlyphDefinition> doInBackground() {
        return loadData(dataSource);
    }

    @Override
    protected void done() {
        try {
            result = get();
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } catch (ExecutionException e) {
            LOGGER.error(e);
        }
    }

    private Boolean isLocalFile(String path) {
        return (!path.matches("^\\w+:\\/\\/.*"));
    }

    public List<GlyphDefinition> loadData(DataSource dataSource) {
        String path = dataSource.getBasePath();
        List<GlyphDefinition> glyphList = (isLocalFile(path)) ? loadDataFromFile(dataSource)
                : loadDataFromUrl("guest", "guest", dataSource);
        return glyphList;
    }

    private class GlyphResponseHandler implements
            ResponseHandler<List<GlyphDefinition>> {

        private DataSource dataSource;

        public GlyphResponseHandler(DataSource dataSource) {
            this.dataSource = dataSource;
        }

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

            String contentType = response.getFirstHeader("Content-Type")
                    .getValue();

            if (contentType.indexOf("xml") > -1) {
                return parseXmlSax(inputStream, dataSource);
            } else {
                return parseJson(inputStream, dataSource);
            }
        }

    }

    public List<GlyphDefinition> loadDataFromUrl(String user, String password,
            final DataSource dataSource) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(dataSource.getBasePath());
            httpGet.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials(user, password), "UTF-8",
                    false));
            return httpclient.execute(httpGet, new GlyphResponseHandler(
                    dataSource));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    String.format(
                            i18n.getString(this.getClass().getSimpleName()
                                    + ".couldNotLoadData"),
                            dataSource.getBasePath()),
                    i18n.getString(this.getClass().getSimpleName() + ".error"),
                    JOptionPane.ERROR_MESSAGE);
            LOGGER.info(e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        return null;
    }

    public List<GlyphDefinition> parseXmlSax(InputStream is,
            DataSource dataSource) {

        TeiXmlHandler handler = new TeiXmlHandler(dataSource);
        try {
            parser.parse(is, handler);
        } catch (SAXException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.toString(),
                    i18n.getString(this.getClass().getSimpleName()
                            + ".xmlParsingError"), JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.toString(),
                    i18n.getString(this.getClass().getSimpleName()
                            + ".xmlParsingError"), JOptionPane.ERROR_MESSAGE);
        }

        return handler.getGlyphDefinitions();
    }

    public List<GlyphDefinition> parseJson(InputStream inputStream,
            DataSource dataSource) {

        List<GlyphDefinition> glyphList = new ArrayList<GlyphDefinition>();
        StringBuilder builder = new StringBuilder();

        try {
            int ch;
            while ((ch = inputStream.read()) != -1) {
                builder.append((char) ch);
            }

            String input = builder.toString();
            JSONObject responseJSON = new JSONObject(input);
            JSONArray dataArray = responseJSON.getJSONArray("data");
            if (dataArray != null) {
                int rows = dataArray.length();
                if (rows > 0) {
                    for (int i = 0; i < rows; i++) {
                        JSONObject obj = dataArray.getJSONObject(i);
                        glyphList.add(new GlyphDefinition(obj.getString("id"),
                                obj.getString("name"), obj
                                        .getString("codepoint"), obj
                                        .getString("range"), obj
                                        .getString("url"), dataSource));
                    }
                    return glyphList;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.toString(),
                    i18n.getString(this.getClass().getSimpleName()
                            + ".jsonParsingError"), JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public List<GlyphDefinition> loadDataFromFile(DataSource dataSource) {

        List<GlyphDefinition> glyphList = null;

        String fileName = dataSource.getBasePath();

        if (fileName != null) {
            File file = new File(fileName);
            InputStream inputStream = null;
            String mimeType = null;

            try {
                mimeType = file.toURI().toURL().openConnection()
                        .getContentType();
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
            if (inputStream != null && mimeType != null) {
                glyphList = (mimeType.indexOf("xml") > -1) ? parseXmlSax(
                        inputStream, dataSource) : parseJson(inputStream,
                        dataSource);
            }
        }
        return glyphList;
    }

}
