package com.aerhard.oxygen.plugin.glyphpicker.model.tei;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "charDecl")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphList {

    @XmlElement(name = "desc")
    private String desc;
    @XmlElement(name = "char")
    private List<GlyphModel> chars = new ArrayList<GlyphModel>();

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc
     *            the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the chars
     */
    public List<GlyphModel> getChars() {
        return chars;
    }

    /**
     * @param chars
     *            the chars to set
     */
    public void setChars(List<GlyphModel> chars) {
        this.chars = chars;
    }

    public GlyphList() {

    }

    public void addAdditionalData(String baseUrl) {
        for (GlyphModel model : chars) {
            model.setBaseUrl(baseUrl);
            model.setRange(desc);
        }

    }

}
