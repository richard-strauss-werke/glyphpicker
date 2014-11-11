package com.aerhard.oxygen.plugin.glyphpicker.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {

    @XmlElement(name = "paths")
    private PathComboModel paths = null;

    /**
     * @return the paths
     */
    public PathComboModel getPaths() {
        return paths;
    }

    /**
     * @param paths
     *            the paths to set
     */
    public void setPaths(PathComboModel paths) {
        this.paths = paths;
    }

}
