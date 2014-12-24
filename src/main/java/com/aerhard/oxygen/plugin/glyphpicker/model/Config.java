package com.aerhard.oxygen.plugin.glyphpicker.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {

    @XmlElement(name = "dataSources")
    private DataSourceList dataSources = null;

    /**
     * @return the dataSources
     */
    public DataSourceList getDataSources() {
        return dataSources;
    }

    /**
     * @param dataSources
     *            the dataSources to set
     */
    public void setDataSources(DataSourceList dataSources) {
        this.dataSources = dataSources;
    }

}
