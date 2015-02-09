---
layout: page
title: "Configuration"
category: doc
date: 2014-12-17 21:33:40
---

By default, the GlyphPicker plugin contains a list of standard data source declaratations, allowing immediate access to glyph definitions specified by 
the gBank and SMuFL-Browser web services, the first one containing medieval 
characters, the second one musical symbols. 
You can adjust the data source settings by selecting `Edit` next to the data 
source combo box. Each data source contains the following fields:

#### Label

The label of the data source as displayed in the data source combo

#### Path

The path to the XML file containing the glyph definitions

#### Glyph Renderer

Specifies how glyphs of this data source should be rendered.

- `bitmap`: Renders a bitmap image whose relative path to the XML file is specified in a <graphic> element. 
- `vector`: Renders a glyph vector from a system font; the relative size of all glyphs is preserved.
- `scaled vector`: Like `vector`, but each glyph is scaled to fit its list/table cell.
- `text`: Renders the glyph as simple text with a system font; rendering quality can be inferior to `vector` / `scaled vector`.

#### Font Name

The name of the system font used to render glyphs with the `vector`, `scaled vector` or `text` renderer.

#### Glyph Size in %

The size of the rendered glyphs relative to their container (which is 40px x 40px)

#### Template

Specifies in which way the glyph data should be inserted into the document. You can specify three variables in the template:

- ${basePath}: equals the content of the `path` field
- ${id}: the xml:id of the glyph definition
- ${cp}: the glyph's codepoints

Examples:

Use both base path and id to create an absolute path:

```
<g ref="${basePath}#${id}"/>
```

Only set the id in the ref attribute, additionally add the codepoints to the XML document:

```
<g ref="#${id}">${cp}</g>
```

Set prefix+id in the ref attribute; the prefix should be resolved in the TEI header of the document:

```
<g ref="g:${id}"/>
```

#### Value of mapping/@type

A TEI `<char>` element may contain multiple mappings; by providing the value of a type attribute, you can select the mapping element which should be
used by GlyphPicker to display the glyphs.

#### Value of mapping/@subtype

See Value of mapping/@type

#### Parse mapping/text()

If this option is selected, the text content of the mapping element will be assumed to have the form `U+xxxx`. In this case, it will be parsed to a character.
