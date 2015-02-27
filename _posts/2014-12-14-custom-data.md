---
layout: page
title: "Incorporating custom character declarations"
category: doc
date: 2014-12-14 06:23:59
---

The GlyphPicker plugin gets its input data from the contents of a TEI file's `<charDecl>` elements. In order to provide custom
 character declarations, create a TEI file containing all the character declarations and add a new entry in the plugin's 
  settings dialog. Provide a reference to your TEI file in the field `Path to Character Declarations`. 

For example files containing multiple character declarations, see the
 [ENRICH gBank](http://www.manuscriptorium.com/apps/gbank/) (get the ZIP file) and the [SMuFL-Browser](http://edirom.de/smufl-browser/index.tei).

The plugin maps the content of the `<charDecl>` elements in the following way:

- The content of the `<desc>` child element is interpreted as the 'range' description of all characters
 defined in that `<charDecl>`. If you want to distinguish between different ranges, put your characters in separate `<charDecl>` elements,
 and provide the name of the range in the `<desc>` child element.

- Each `<char>` or `<glyph>` child element is interpreted as a separate character / glyph declaration.
An `xml:id` on `<char>` and `<glyph>` is necessary to provide an id reference to the character declaration in the plugin's XML output.

- The character name is read from the content of the last ancestor element with the name `desc`, `charName` or `glyphName`

- When a `<charProp>` element containing a child element `<localName>` with the value `entity` is present, the text content of the following child element `<value>` gets
written to the "entity" field.

- As described in the TEI Guidelines, the `<mapping>` child element of `<char>` / `<glyph>` 
"contains one or more characters which are related to the parent character or glyph in 
some respect, as specified by the type attribute" (see [guidelines, mapping](http://www.tei-c.org/release/doc/tei-p5-doc/de/html/ref-mapping.html)).
The TEI schema allows single character / glyph declarations to contain multiple `<mapping>` child elements, but font-based character rendering in the
 GlyphPicker plugin depends on a single mapping. If there are multiple mappings in the character declaration, it's necessary to specify which one to use
 for rendering in the GlyphPicker settings dialog. Currently, specific mappings can be selected by providing a `@type` or `@subtype` attribute (or both). 
  If there are multiple `<mapping>` elements matching the criteria, the plugin only takes the first one into account. If the `@type` or `@subtype` field is left empty in
  the table settings of the plugin, the attribute in concern will not be evaluated by the plugin.

- The content of the `<mapping>` element may either consist of characters and numeric character reference like `&#xE002;` or of unicode code point references like `U+E002`. There is
first require the "parse mapping/text()" table option to be unchecked; the the second needs it to be checked. There is
basic support for `<g>` references in `<mapping>`, allowing to declare characters composed of other characters declared in the same file. When text content is provided in
a `<g>` element, the plugin includes the text content instead of evaluating the reference target's character mapping.

- If characters should be rendered as bitmap images, a `<graphic>` child element of `<char>` / `<glyph>` is required, containing in its `@url` attribute the absolute or relative path
of the image file. Relative paths are assumed to be relative to the XML file containing the character declarations. (If you intend to extract glyph images from a font file, the
`otf2png` converter used in the [SMuFL-Browser project](https://github.com/Edirom/SMuFL-Browser) might be helpful.)

Extending the plugin's TEI data mapping capabilities should be simple in most cases; for additions / modifications, please send a feature or pull request at https://github.com/richard-strauss-werke/glyphpicker.
