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
package de.badw.strauss.glyphpicker.model;

import javax.xml.bind.annotation.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

/**
 * The plugin's config model.
 */
@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {

    /**
     * The change event key of the shortcut property
     */
    @XmlTransient
    public static final String SHORTCUT_KEY = "shortcut";
    /**
     * The property change support.
     */
    @XmlTransient
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    /**
     * The version of the config file
     */
    @XmlElement
    private String version;
    /**
     * The plugin's shortcut in oXygen, for possible values see
     * http://docs.oracle.com/javase/7/docs/api/javax/swing/KeyStroke.html#getKeyStroke%28java.lang.String%29
     */
    @XmlElement
    private String shortcut;
    /**
     * Indicates if the previous component's focus should be restored when a glyph gets inserted.
     */
    @XmlElement
    private boolean transferFocusAfterInsert = true;
    /**
     * The tab index to select when the plug-in window is shown the first time in a session.
     */
    @XmlElement
    private int tabIndex = 1;
    /**
     * The user search field scope index.
     */
    @XmlElement
    private int userSearchFieldScopeIndex = 0;
    /**
     * The browser search field scope index.
     */
    @XmlElement
    private int browserSearchFieldScopeIndex = 0;
    /**
     * The user view index.
     */
    @XmlElement
    private int userViewIndex = 0;
    /**
     * The browser view index.
     */
    @XmlElement
    private int browserViewIndex = 0;
    /**
     * The data sources.
     */
    @XmlElement(name = "glyphTables")
    private DataSourceList glyphTables = null;
    /**
     * The plugin's config directory
     */
    @XmlTransient
    private File configDir;

    /**
     * Adds a property change listener.
     *
     * @param l the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * Removes a property change listener.
     *
     * @param l the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /**
     * gets the version of the config file
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * sets the version of the config file
     *
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the plugin's shortcut in oXygen.
     *
     * @return the shortcut
     */
    public String getShortcut() {
        return shortcut;
    }

    /**
     * Sets the plugin's shortcut in oXygen.
     *
     * @param shortcut the shortcut
     */
    public void setShortcut(String shortcut) {
        String oldShortcut = this.shortcut;
        this.shortcut = shortcut;
        pcs.firePropertyChange(SHORTCUT_KEY, oldShortcut, shortcut);
    }

    /**
     * Indicates if the previous component's focus should be restored when a glyph gets inserted.
     *
     * @return the value of removeFocusOnInsert
     */
    public boolean shouldTransferFocusAfterInsert() {
        return transferFocusAfterInsert;
    }

    /**
     * Specifies if the previous component's focus should be restored when a glyph gets inserted.
     *
     * @param transferFocusAfterInsert indicates if the focus should be transferred
     */
    public void setTransferFocusAfterInsert(boolean transferFocusAfterInsert) {
        this.transferFocusAfterInsert = transferFocusAfterInsert;
    }

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
    public DataSourceList getGlyphTables() {
        return glyphTables;
    }

    /**
     * Sets the data sources.
     *
     * @param glyphTables the new data sources
     */
    public void setGlyphTables(DataSourceList glyphTables) {
        this.glyphTables = glyphTables;
    }

    /**
     * Gets the plugin's config directory.
     *
     * @return the directory
     */
    public File getConfigDir() {
        return configDir;
    }

    /**
     * Sets the plugin's config directory.
     *
     * @param configDir the directory
     */
    public void setConfigDir(File configDir) {
        this.configDir = configDir;
    }
}
