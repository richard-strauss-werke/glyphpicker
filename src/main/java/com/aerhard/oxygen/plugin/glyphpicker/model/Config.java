package com.aerhard.oxygen.plugin.glyphpicker.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {

    @XmlElement
    private int tabIndex = 1;

    @XmlElement
    private int userSearchFieldScopeIndex = 0;

    @XmlElement
    private int browserSearchFieldScopeIndex = 0;

    @XmlElement
    private int userViewIndex = 0;

    @XmlElement
    private int browserViewIndex = 0;

    @XmlElement(name = "dataSources")
    private DataSourceList dataSources = null;

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public int getUserSearchFieldScopeIndex() {
        return userSearchFieldScopeIndex;
    }

    public void setUserSearchFieldScopeIndex(int userSearchFieldScopeIndex) {
        this.userSearchFieldScopeIndex = userSearchFieldScopeIndex;
    }

    public int getBrowserSearchFieldScopeIndex() {
        return browserSearchFieldScopeIndex;
    }

    public void setBrowserSearchFieldScopeIndex(int browserSearchFieldScopeIndex) {
        this.browserSearchFieldScopeIndex = browserSearchFieldScopeIndex;
    }

    public int getUserViewIndex() {
        return userViewIndex;
    }

    public void setUserViewIndex(int userViewIndex) {
        this.userViewIndex = userViewIndex;
    }

    public int getBrowserViewIndex() {
        return browserViewIndex;
    }

    public void setBrowserViewIndex(int browserViewIndex) {
        this.browserViewIndex = browserViewIndex;
    }

    public DataSourceList getDataSources() {
        return dataSources;
    }

    public void setDataSources(DataSourceList dataSources) {
        this.dataSources = dataSources;
    }

}
