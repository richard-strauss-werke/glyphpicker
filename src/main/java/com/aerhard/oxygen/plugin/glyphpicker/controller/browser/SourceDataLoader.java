package com.aerhard.oxygen.plugin.glyphpicker.controller.browser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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

import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphList;
import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphItem;

public class SourceDataLoader {

    private static final Logger LOGGER = Logger
            .getLogger(SourceDataLoader.class.getName());

    public static Boolean isLocalFile(String path) {
        return (!path.matches("^\\w+:\\/\\/.*"));
    }

    public List<GlyphItem> loadData(String path) {
        List<GlyphItem> glyphList = (isLocalFile(path)) ? loadDataFromFile(path)
                : loadDataFromUrl("guest", "guest", path);
        return glyphList;
    }

    private class GlyphResponseHandler implements
            ResponseHandler<List<GlyphItem>> {

        private String url;

        public GlyphResponseHandler(String url) {
            this.url = url;
        }

        @Override
        public List<GlyphItem> handleResponse(final HttpResponse response)
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
                return parseXml(inputStream, url);
            } else {
                return parseJson(inputStream, url);
            }
        }

    }

    public List<GlyphItem> loadDataFromUrl(String user, String password,
            final String url) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials(user, password), "UTF-8",
                    false));
            return httpclient.execute(httpGet, new GlyphResponseHandler(url));
        } catch (IOException e) {
            LOGGER.info("Error loading data from \"" + url + "\"", e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        return null;
    }

    private List<GlyphItem> parseXml(InputStream inputStream, String baseUrl) {
        GlyphList charDecl;
        List<GlyphList> charDeclList = new ArrayList<GlyphList>();
        List<GlyphItem> charList = new ArrayList<GlyphItem>();
        try {
            XMLInputFactory xif = XMLInputFactory.newInstance();
            XMLStreamReader xsr = xif.createXMLStreamReader(inputStream);
            JAXBContext context = JAXBContext.newInstance(GlyphList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            while (xsr.hasNext()) {
                xsr.next();
                if (xsr.getEventType() == XMLStreamReader.START_ELEMENT
                        && xsr.getLocalName().equals("charDecl")) {
                    charDecl = unmarshaller.unmarshal(xsr, GlyphList.class)
                            .getValue();
                    charDecl.addAdditionalData(baseUrl);
                    charDeclList.add(charDecl);
                }
            }
            Iterator<GlyphList> iterator = charDeclList.iterator();
            while (iterator.hasNext()) {
                charList.addAll(iterator.next().getChars());
            }
            return charList;
        } catch (XMLStreamException e) {
            LOGGER.info(e);
        } catch (JAXBException e) {
            LOGGER.info(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.info(e);
            }
        }
        return null;
    }

    public List<GlyphItem> parseJson(InputStream inputStream, String baseUrl) {

        List<GlyphItem> glyphList = new ArrayList<GlyphItem>();
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
                        List<String> classes = new ArrayList<String>();
                        JSONArray classesArray = obj.getJSONArray("classes");
                        if (classesArray != null) {
                            for (int j = 0; j < classesArray.length(); j++) {
                                classes.add(classesArray.get(j).toString());
                            }
                        }
                        glyphList.add(new GlyphItem(obj.getString("id"), obj
                                .getString("name"), obj.getString("codepoint"),
                                obj.getString("range"), obj.getString("url"),
                                baseUrl, classes));
                    }
                    return glyphList;
                }
            }
        } catch (Exception e) {
            LOGGER.info(e);
        }
        return null;
    }

    public List<GlyphItem> loadDataFromFile(String fileName) {

        List<GlyphItem> glyphList = null;

        if (fileName != null) {
            File file = new File(fileName);
            InputStream inputStream = null;
            String mimeType = null;
            // ClassLoader classLoader = getClass().getClassLoader();
            // File file = new
            // File(classLoader.getResource(fileName).getFile());

            try {
                mimeType = file.toURI().toURL().openConnection()
                        .getContentType();
                inputStream = new FileInputStream(file);

            } catch (IOException e) {
                LOGGER.info(e);
            }
            if (inputStream != null && mimeType != null) {
                glyphList = (mimeType.indexOf("xml") > -1) ? parseXml(
                        inputStream, fileName) : parseJson(inputStream,
                        fileName);
            }
        }
        return glyphList;
    }

}
