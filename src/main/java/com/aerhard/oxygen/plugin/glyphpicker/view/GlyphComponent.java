package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import com.aerhard.oxygen.plugin.glyphpicker.controller.DataStore;
import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphModel;

public class GlyphComponent extends JLabel {

    private static final long serialVersionUID = 1L;

    private GlyphModel model;
    private DataStore dataStore;
    private static final int GLYPH_SIZE = 50;
    private static final int GLYPH_BORDER = 10;

    private SwingWorker<ImageIcon, Void> worker = null;

    public GlyphComponent(GlyphModel model, DataStore dataStore, Boolean text) {

        this.model = model;
        this.dataStore = dataStore;

        if (text) {
            setText(formatText(model));
        }
        // setVerticalAlignment(SwingConstants.TOP);
        setBorder(BorderFactory.createEmptyBorder(GLYPH_BORDER, GLYPH_BORDER,
                GLYPH_BORDER, GLYPH_BORDER));

        loadIcon();
    }

    public GlyphComponent() {
    }

    public void checkIcon() {
        if (getIcon() == null && worker == null) {
            loadIcon();
        }
    }

    private JComponent container = null;

    public void setContainer(JComponent table) {
        this.container = table;
    }

    private void loadIcon() {

        // LOGGER.info("Starting image loading worker for " +
        // model.getCharName());

        worker = new SwingWorker<ImageIcon, Void>() {
            public ImageIcon doInBackground() throws IOException {
                ImageIcon icon = null;

                BufferedImage bi = dataStore.loadImage(model.getBaseUrl(),
                        model.getUrl());
                if (bi != null) {
                    icon = new ImageIcon(scaleToBound(bi, GLYPH_SIZE,
                            GLYPH_SIZE));
                }

                return icon;
            }

            public void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        setIcon(icon);
                        setIconTextGap(GLYPH_SIZE + 20
                                - getIcon().getIconWidth());
                        if (container != null) {
                            // LOGGER.info("REPAINT " + model.getCharName());
                            container.repaint();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
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

}
