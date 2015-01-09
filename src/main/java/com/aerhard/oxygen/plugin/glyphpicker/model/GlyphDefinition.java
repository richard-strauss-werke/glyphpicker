package com.aerhard.oxygen.plugin.glyphpicker.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "char", namespace = "http://www.tei-c.org/ns/1.0")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphDefinition implements Cloneable {

    @XmlAttribute(name = "id", namespace = "http://www.w3.org/XML/1998/namespace")
    private String id;

    @XmlElement(name = "charName", namespace = "http://www.tei-c.org/ns/1.0")
    private String charName;

    @XmlElement(name = "mapping", namespace = "http://www.tei-c.org/ns/1.0")
    private String codePoint;

    @XmlElementWrapper(name = "list", namespace = "http://www.tei-c.org/ns/1.0")
    private List<String> classes = new ArrayList<String>();

    @XmlElement(name = "graphic", namespace = "http://www.tei-c.org/ns/1.0")
    private String url;

    @XmlElement(name = "range")
    private String range;

    @XmlElement(name = "dataSource")
    private DataSource dataSource;

    @XmlTransient
    private ImageIcon icon = null;

    @XmlTransient
    private boolean iconLoadingLaunched = false;


    public GlyphDefinition() {

    }

    public GlyphDefinition(String id, String name, String codepoint,
            String range, String url, DataSource dataSource,
            List<String> classes) {
        this.id = id;
        this.charName = name;
        this.codePoint = codepoint;
        this.range = range;
        this.url = url;
        this.dataSource = dataSource;
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

    public boolean isIconLoadingLaunched() {
        return iconLoadingLaunched;
    }

    public void setIconLoadingLaunched(boolean iconLoadingLaunched) {
        this.iconLoadingLaunched = iconLoadingLaunched;
    }
    
    public String toString() {
        return codePoint + ": " + charName + " (" + range + ")";
    }

    /**
     * @return the dataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource
     *            the dataSource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getRefString() {

        String template = getDataSource().getTemplate();

        // if no template is specified, use default template
        if (template == null) {
            return getDataSource().getBasePath() + "#" + getId();
        }

        // TODO make template apply

        return template.replaceAll("\\$\\{basePath\\}",
                getDataSource().getBasePath()).replaceAll("\\$\\{id\\}",
                getId());

        // return getDataSource().getPath() + "#" + getId();
    }
    
    @Override
    public GlyphDefinition clone() throws CloneNotSupportedException {
        return (GlyphDefinition) super.clone();
    }

}
