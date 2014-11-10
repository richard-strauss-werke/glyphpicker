package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
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
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphList;
import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphModel;

public class DataStore {

    private static final Logger LOGGER = Logger.getLogger(DataStore.class
            .getName());

    private Boolean isLocalFile(String path) {
        return (!path.matches("^\\w+:\\/\\/.*"));
    }

    public GlyphModel[] loadData(String path) {
        List<GlyphModel> glyphList = (isLocalFile(path)) ? loadDataFromFile(path)
                : loadDataFromUrl("guest", "guest", path);
        GlyphModel[] data = null;
        if (glyphList != null) {
            data = new GlyphModel[glyphList.size()];
            data = glyphList.toArray(data);
        }
        return data;
    }

    public BufferedImage loadImage(String path, String relativePath) {
        BufferedImage image = null;
        if (relativePath != null) {
            if (isLocalFile(path)) {
                File a = new File(path);
                File parentFolder = new File(a.getParent());
                File b = new File(parentFolder, relativePath);
                image = getImageFromFile(b);
            } else {
                try {
                    String imagePath = (new URL(new URL(path), relativePath))
                            .toString();
                    image = getImageFromUrl("guest", "guest", imagePath);
                } catch (MalformedURLException e) {
                    LOGGER.info(e);
                }

            }
        }
        return image;
    }

    public List<GlyphModel> loadDataFromUrl(String user, String password,
            final String url) {

        ResponseHandler<List<GlyphModel>> rh = new ResponseHandler<List<GlyphModel>>() {
            @Override
            public List<GlyphModel> handleResponse(final HttpResponse response)
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
        };

        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(url);
            if (httpGet != null) {
                httpGet.addHeader(BasicScheme.authenticate(
                        new UsernamePasswordCredentials(user, password),
                        "UTF-8", false));

                return httpclient.execute(httpGet, rh);
            }
        } catch (IOException e) {
            LOGGER.info("Error loading data from \"" + url + "\"", e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        return null;
    }

    private List<GlyphModel> parseXml(InputStream inputStream, String baseUrl) {
        GlyphList charDecl;
        List<GlyphList> charDeclList = new ArrayList<GlyphList>();
        List<GlyphModel> charList = new ArrayList<GlyphModel>();
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

    public List<GlyphModel> parseJson(InputStream inputStream, String baseUrl) {

        List<GlyphModel> glyphList = new ArrayList<GlyphModel>();
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
                        glyphList.add(new GlyphModel(obj.getString("id"), obj
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

    public List<GlyphModel> loadDataFromFile(String fileName) {

        List<GlyphModel> glyphList = null;

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

    public BufferedImage getImageFromFile(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            LOGGER.info(e);
        }
        return image;
    };

    public BufferedImage getImageFromUrl(String user, String password,
            String url) {
        HttpResponse response = null;
        BufferedImage image = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(url);
            if (httpGet != null) {
                httpGet.addHeader(BasicScheme.authenticate(
                        new UsernamePasswordCredentials(user, password),
                        "UTF-8", false));
                response = httpclient.execute(httpGet);

                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    byte[] bytes = EntityUtils.toByteArray(entity);
                    image = ImageIO.read(new ByteArrayInputStream(bytes));
                    return image;
                } else {
                    throw new IOException(
                            "Download failed, HTTP response code " + statusCode
                                    + " - " + statusLine.getReasonPhrase());
                }

            }
        } catch (IOException e) {
            LOGGER.info("Error loading image from \"" + url + "\"", e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return image;
    };

}
