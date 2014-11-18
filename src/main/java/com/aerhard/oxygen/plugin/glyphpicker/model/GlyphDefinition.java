package com.aerhard.oxygen.plugin.glyphpicker.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "char", namespace="http://www.tei-c.org/ns/1.0")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphDefinition {
    
    @XmlAttribute(name = "id", namespace = "http://www.w3.org/XML/1998/namespace")
    private String id;

    @XmlAttribute(name = "base", namespace = "http://www.w3.org/XML/1998/namespace")
    private String baseUrl;

    @XmlElement(name = "charName", namespace="http://www.tei-c.org/ns/1.0")
    private String charName;
    
    @XmlElement(name="mapping", namespace="http://www.tei-c.org/ns/1.0")
    private String codePoint;
    
    @XmlElementWrapper(name="list", namespace="http://www.tei-c.org/ns/1.0")
    private List<String> classes = new ArrayList<String>();
    
    @XmlElement(name="graphic", namespace="http://www.tei-c.org/ns/1.0")
    private String url;

    @XmlElement(name="range")
    private String range;

    @XmlTransient
    private JComponent component = null;
    @XmlTransient
    private ImageIcon icon = null;

    public GlyphDefinition() {

    }

    public GlyphDefinition(GlyphDefinition ch) {
        this.id = ch.getId();
        this.charName = ch.getCharName();
        this.codePoint = ch.getCodePoint();
        this.range = ch.getRange();
        this.url = ch.getUrl();
        this.classes = ch.getClasses();
        this.baseUrl = ch.getBaseUrl();
    }

    public GlyphDefinition(String id, String name, String codepoint,
            String range, String url, String baseUrl, List<String> classes) {
        this.id = id;
        this.charName = name;
        this.codePoint = codepoint;
        this.range = range;
        this.url = url;
        this.baseUrl = baseUrl;
        this.classes = classes;
    }

    /**
     * @return the label
     */
    public String getCharName() {
        return charName;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setCharName(String label) {
        this.charName = label;
    }

    /**
     * @return the codePoint
     */
    public String getCodePoint() {
        return codePoint;
    }
    
    public String getCharString() {
        try {
            String cp = codePoint.substring(2);
            int c = Integer.parseInt(cp, 16);
            return Character.toString((char) c);    
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param codePoint
     *            the codePoint to set
     */
    public void setCodePoint(String codePoint) {
        this.codePoint = codePoint;
    }

    /**
     * @return the range
     */
    public String getRange() {
        return range;
    }

    /**
     * @param range
     *            the range to set
     */
    public void setRange(String range) {
        this.range = range;
    }

    /**
     * @return the classes
     */
    public List<String> getClasses() {
        return classes;
    }

    /**
     * @param classes
     *            the classes to set
     */
    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setIcon(ImageIcon image) {
        this.icon = image;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * @return the component
     */
    public JComponent getComponent() {
        return component;
    }

    /**
     * @param c
     *            the component to set
     */
    public void setComponent(JComponent c) {
        this.component = c;
    }

}
