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
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for glyph definition models.
 */
@XmlRootElement(name = "charDecl")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphDefinitions {

    @XmlTransient
    private static final long serialVersionUID = 1L;

    /**
     * The version of the user data
     */
    @XmlElement
    private String version;

    /**
     * The glyph definition list.
     */
    @XmlElement(name = "char")
    private List<GlyphDefinition> data = new ArrayList<>();

    /**
     * Instantiates a new GlyphDefinitions object.
     *
     * @param data the glyph definition list
     */
    public GlyphDefinitions(List<GlyphDefinition> data) {
        this.data = data;
    }

    /**
     * Instantiates a new glyph definitions.
     */
    public GlyphDefinitions() {
    }

    /**
     * Instantiates a new glyph definitions.
     * @param version the plugin version
     */
    public GlyphDefinitions(String version) {
        this.version = version;
    }

    /**
     * gets the version of the user data
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * sets the version of the user data
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Sets the glyph definition list.
     *
     * @param data the new glyph definition list
     */
    public void setData(List<GlyphDefinition> data) {
        this.data = data;
    }

    /**
     * Gets the glyph definition list.
     *
     * @return the glyph definition list
     */
    public List<GlyphDefinition> getData() {
        return data;
    }

}
