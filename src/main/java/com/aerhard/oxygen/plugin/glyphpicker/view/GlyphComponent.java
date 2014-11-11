package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.aerhard.oxygen.plugin.glyphpicker.controller.DataStore;
import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphModel;

public class GlyphComponent extends JLabel {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(GlyphComponent.class
            .getName());

    private GlyphModel model;
    private static final int GLYPH_SIZE = 50;
    private static final int GLYPH_BORDER = 10;

    public GlyphComponent(GlyphModel model, Boolean text) {
        this.model = model;
        if (text) {
            setText(formatText(model));
        }
        setIconTextGap(20);
        setBorder(BorderFactory.createEmptyBorder(GLYPH_BORDER, GLYPH_BORDER,
                GLYPH_BORDER, GLYPH_BORDER));
    }

    public GlyphComponent() {
    }

    private JComponent container = null;

    public void setContainer(JComponent table) {
        this.container = table;
    }

    
    private class IconLoader extends SwingWorker<GlyphIcon, Void> {
        
        @Override
        public GlyphIcon doInBackground() throws IOException {
            BufferedImage bi = loadImage(model.getBaseUrl(), model.getUrl());
            if (bi != null) {
                return new GlyphIcon(scaleToBound(bi, GLYPH_SIZE,
                        GLYPH_SIZE), GLYPH_SIZE);
            }
            return null;
        }

        @Override
        public void done() {
            try {
                GlyphIcon icon = get();
                if (icon != null) {
                    setIcon(icon);
                    if (container != null) {
                        // LOGGER.info("REPAINT " + model.getCharName());
                        container.repaint();
                    }
                }
            } catch (Exception e) {
                LOGGER.warn(e);
            }
        }
    }
    
    public void loadIcon() {
        IconLoader worker = new IconLoader();
        worker.execute();
    }

    private String formatText(GlyphModel model) {
        List<String> classes = model.getClasses();
        StringBuilder sb = new StringBuilder();

        sb.append("<html>");

        if (model.getCharName() != null) {
            sb.append("<p><nobr><b>");
            sb.append(model.getCharName());
            sb.append("</b></nobr></p>");
        }

        if (model.getCodePoint() != null) {
            sb.append("<p><nobr>Codepoint: ");
            sb.append(model.getCodePoint());
            sb.append("</nobr></p>");
        }

        if (model.getRange() != null) {
            sb.append("<p><nobr>Range: ");
            sb.append(model.getRange());
            sb.append("</nobr></p>");
        }

        if (model.getClasses().size() > 0) {
            sb.append("<p><nobr>Classes: ");
            for (String cl : classes) {
                sb.append(cl);
                sb.append(" ");
            }
            sb.append("</nobr></p>");
        }

        if (model.getId() != null) {
            sb.append("<p><nobr><em>");
            sb.append(model.getId());
            sb.append("</em></nobr></p>");
        }

        sb.append("</div></html>");

        return sb.toString();

    }

    public Image scaleToBound(BufferedImage image, int boundX, int boundY) {

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        int resultWidth = originalWidth;
        int resultHeight = originalHeight;

        if (originalWidth > boundX) {
            resultWidth = boundX;
            resultHeight = (resultWidth * originalHeight) / originalWidth;
        }

        if (resultHeight > boundY) {
            resultHeight = boundY;
            resultWidth = (resultHeight * originalWidth) / originalHeight;
        }

        // set minimum dimension of 1 px
        resultWidth = Math.max(resultWidth, 1);
        resultHeight = Math.max(resultHeight, 1);

        return image.getScaledInstance(resultWidth, resultHeight,
                Image.SCALE_AREA_AVERAGING);
    }

    public BufferedImage loadImage(String path, String relativePath) {
        BufferedImage image = null;
        if (relativePath != null) {
            if (DataStore.isLocalFile(path)) {
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
            httpGet.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials(user, password), "UTF-8",
                    false));
            response = httpclient.execute(httpGet);

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                byte[] bytes = EntityUtils.toByteArray(entity);
                image = ImageIO.read(new ByteArrayInputStream(bytes));
                return image;
            } else {
                throw new IOException("Download failed, HTTP response code "
                        + statusCode + " - " + statusLine.getReasonPhrase());
            }
        } catch (IOException e) {
            LOGGER.info("Error loading image from \"" + url + "\"", e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return image;
    };

}
