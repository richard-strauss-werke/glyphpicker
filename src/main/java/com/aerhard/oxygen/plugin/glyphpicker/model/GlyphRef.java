package com.aerhard.oxygen.plugin.glyphpicker.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "g")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphRef {

    @XmlAttribute(name = "n")
    private int index;

    @XmlAttribute(name = "ref")
    private String targetId;

    @XmlAttribute(name = "length")
    private String length;

    @XmlTransient
    private GlyphDefinition target = null;

    public GlyphRef(int index, String targetId) {
        this.index = index;
        this.targetId = targetId;
    }

    public GlyphRef() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void incrementIndex(int offset) {
        this.index += offset;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public GlyphDefinition getTarget() {
        return target;
    }

    public void setTarget(GlyphDefinition target) {
        this.target = target;
    }
}
