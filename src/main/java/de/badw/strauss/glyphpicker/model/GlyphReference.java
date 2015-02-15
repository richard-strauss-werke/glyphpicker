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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Glyph reference model.
 */
@XmlRootElement(name = "g")
@XmlAccessorType(XmlAccessType.FIELD)
public class GlyphReference {

    /**
     * The string index of this reference's <g> attribute in its <mapping> parent.
     */
    @XmlAttribute(name = "n")
    private int index;

    /**
     * The target id.
     */
    @XmlAttribute(name = "ref")
    private String targetId;

    /**
     * The string length of this reference's mapped characters.
     */
    @XmlAttribute(name = "length")
    private String length;

    /**
     * The referenced glyph definition.
     */
    @XmlTransient
    private GlyphDefinition target = null;

    /**
     * Instantiates a new glyphRef object.
     *
     * @param index    The string index of this reference's <g> attribute in its <mapping> parent
     * @param targetId The target id
     */
    public GlyphReference(int index, String targetId) {
        this.index = index;
        this.targetId = targetId;
    }

    /**
     * Instantiates a new glyph ref.
     */
    public GlyphReference() {
    }

    /**
     * Gets the index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index.
     *
     * @param index the new index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Increments the index property by the provided value.
     *
     * @param offset the offset
     */
    public void incrementIndex(int offset) {
        this.index += offset;
    }

    /**
     * Gets the target id.
     *
     * @return the target id
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * Sets the target id.
     *
     * @param targetId the new target id
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    /**
     * Gets the length.
     *
     * @return the length
     */
    public String getLength() {
        return length;
    }

    /**
     * Sets the length.
     *
     * @param length the new length
     */
    public void setLength(String length) {
        this.length = length;
    }

    /**
     * Gets the target.
     *
     * @return the target
     */
    public GlyphDefinition getTarget() {
        return target;
    }

    /**
     * Sets the target.
     *
     * @param target the new target
     */
    public void setTarget(GlyphDefinition target) {
        this.target = target;
    }
}
