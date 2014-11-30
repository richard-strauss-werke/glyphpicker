package com.aerhard.oxygen.plugin.glyphpicker.model;

import java.awt.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {

    @XmlElement(name = "paths")
    private PathComboModel paths = null;

    @XmlElementWrapper(name="dataSources")
    private ArrayList<DataSource> dataSources = new ArrayList<DataSource>();
    

    
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

    public ArrayList<DataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(ArrayList<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

}
