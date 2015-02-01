/**
 * Copyright 2015 Alexander Erhard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aerhard.oxygen.plugin.glyphpicker.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The plugin's config model.
 */
@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {

    /** The tab index. */
    @XmlElement
    private int tabIndex = 1;

    /** The user search field scope index. */
    @XmlElement
    private int userSearchFieldScopeIndex = 0;

    /** The browser search field scope index. */
    @XmlElement
    private int browserSearchFieldScopeIndex = 0;

    /** The user view index. */
    @XmlElement
    private int userViewIndex = 0;

    /** The browser view index. */
    @XmlElement
    private int browserViewIndex = 0;

    /** The data sources. */
    @XmlElement(name = "dataSources")
    private DataSourceList dataSources = null;

    /**
     * Gets the tab index.
     *
     * @return the tab index
     */
    public int getTabIndex() {
        return tabIndex;
    }

    /**
     * Sets the tab index.
     *
     * @param tabIndex the new tab index
     */
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * Gets the user search field scope index.
     *
     * @return the user search field scope index
     */
    public int getUserSearchFieldScopeIndex() {
        return userSearchFieldScopeIndex;
    }

    /**
     * Sets the user search field scope index.
     *
     * @param userSearchFieldScopeIndex the new user search field scope index
     */
    public void setUserSearchFieldScopeIndex(int userSearchFieldScopeIndex) {
        this.userSearchFieldScopeIndex = userSearchFieldScopeIndex;
    }

    /**
     * Gets the browser search field scope index.
     *
     * @return the browser search field scope index
     */
    public int getBrowserSearchFieldScopeIndex() {
        return browserSearchFieldScopeIndex;
    }

    /**
     * Sets the browser search field scope index.
     *
     * @param browserSearchFieldScopeIndex the new browser search field scope index
     */
    public void setBrowserSearchFieldScopeIndex(int browserSearchFieldScopeIndex) {
        this.browserSearchFieldScopeIndex = browserSearchFieldScopeIndex;
    }

    /**
     * Gets the user view index.
     *
     * @return the user view index
     */
    public int getUserViewIndex() {
        return userViewIndex;
    }

    /**
     * Sets the user view index.
     *
     * @param userViewIndex the new user view index
     */
    public void setUserViewIndex(int userViewIndex) {
        this.userViewIndex = userViewIndex;
    }

    /**
     * Gets the browser view index.
     *
     * @return the browser view index
     */
    public int getBrowserViewIndex() {
        return browserViewIndex;
    }

    /**
     * Sets the browser view index.
     *
     * @param browserViewIndex the new browser view index
     */
    public void setBrowserViewIndex(int browserViewIndex) {
        this.browserViewIndex = browserViewIndex;
    }

    /**
     * Gets the data sources.
     *
     * @return the data sources
     */
    public DataSourceList getDataSources() {
        return dataSources;
    }

    /**
     * Sets the data sources.
     *
     * @param dataSources the new data sources
     */
    public void setDataSources(DataSourceList dataSources) {
        this.dataSources = dataSources;
    }

}
