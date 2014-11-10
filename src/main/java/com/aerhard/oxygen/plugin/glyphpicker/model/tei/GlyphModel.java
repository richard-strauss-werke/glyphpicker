package com.aerhard.oxygen.plugin.glyphpicker.model.tei;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphComponent;

@XmlRootElement(name = "char")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphModel {

    @XmlAttribute(name = "xml:id")
    private String id;

    @XmlAttribute(name = "xml:base")
    private String baseUrl;

    // character name: SMuFL browser has <charName>, ENRICH <desc>
    @XmlElement(name = "charName")
    private String charName;
    // currently only gets the first mapping
    @XmlPath("mapping[1]/text()")
    private String codePoint;
    @XmlPath("note/list/item/text()")
    private List<String> classes = new ArrayList<String>();
    @XmlPath("tei:graphic/@url")
    private String url;

    private String htmlEntity;
    private String range;
    private String teiCode;

    @XmlTransient
    private GlyphComponent component = null;
    @XmlTransient
    private ImageIcon icon = null;

    public GlyphModel() {

    }

    public GlyphModel(GlyphModel ch) {
        this.id = ch.getId();
        this.charName = ch.getCharName();
        this.codePoint = ch.getCodePoint();
        this.range = ch.getRange();
        this.url = ch.getUrl();
        this.classes = ch.getClasses();
        this.baseUrl = ch.getBaseUrl();
    }

    public GlyphModel(String id, String name, String codepoint, String range,
            String url, String baseUrl, List<String> classes) {
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

    /**
     * @param codePoint
     *            the codePoint to set
     */
    public void setCodePoint(String codePoint) {
        this.codePoint = codePoint;
    }

    /**
     * @return the htmlEntity
     */
    public String getHtmlEntity() {
        return htmlEntity;
    }

    /**
     * @param htmlEntity
     *            the htmlEntity to set
     */
    public void setHtmlEntity(String htmlEntity) {
        this.htmlEntity = htmlEntity;
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

    /**
     * @return the teiCode
     */
    public String getTeiCode() {
        return teiCode;
    }

    /**
     * @param teiCode
     *            the teiCode to set
     */
    public void setTeiCode(String teiCode) {
        this.teiCode = teiCode;
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
    public GlyphComponent getComponent() {
        return component;
    }

    /**
     * @param c
     *            the component to set
     */
    public void setComponent(GlyphComponent c) {
        this.component = c;
    }

}
