---
layout: page
title: "Adjusting the XML output"
category: doc
date: 2014-12-17 21:33:40
---

You can adjust the XML code inserted from the GlyphPicker plugin into XML documents by changing the template of the 
  character table in concern. For this, open the "Settings" dialog of the plugin and, on the left, select the character table you
  want to change.
  
  Besides regular text, the "Template" field may contain the following placeholders which get resolved when a character gets inserted into an XML document:

- ${basePath}: the content of the field "Path to Character Declarations"
- ${id}: the xml:id of the character declaration
- ${char}: characters obtained from a `<mapping>` element in the character declaration 
- ${num}: numeric character references obtained from a `<mapping>` element in the character declaration 

Examples:

Use a custom base url and the xml:id in order to provide the full path to the character declaration in each `<g>` element:

```
Template:
<g ref="http://edirom.de/smufl-browser/${id}.xml"/>

Result:
<g ref="http://edirom.de/smufl-browser/luteFrenchFretG.xml"/>
```

Set prefix and id in the ref attribute:

```
Template:
<g ref="s:${id}"/>

Result:
<g ref="s:luteFrenchFretG"/>
```

This reduces the number of characters in the `<g>` elements. The prefix can be resolved in the TEI header of the document, see http://www.tei-c.org/release/doc/tei-p5-doc/de/html/SA.html#SAPU:

```
<listPrefixDef>
 <prefixDef ident="s"
  matchPattern="([A-Za-z]+)"
  replacementPattern="http://edirom.de/smufl-browser/$1.xml">
  <p> xxx </p>
 </prefixDef>
 <prefixDef ident="g"
  matchPattern="([A-Za-z0-9]+)"
  replacementPattern="http://www.example.com/mychardecl.xml#$1">
  <p> xxx </p>
 </prefixDef>
</listPrefixDef>
```

Set only the id in the ref attribute and add a numeric character reference as text content:

```
Template:
<g ref="#${id}">${num}</g>

Result:
<g ref="#luteFrenchFretG">&#xEBC6;</g>
```

Adding characters or numeric character reference to the document can simplify displaying the characters at the cost of encoding a specific character mapping.
Since id and code point references can be altered independently, projects might consider adding automatic text
 quality checks to be sure there mismatching combinations.

The resulting XML code fragment references an element with the xml:id `luteFrenchFretG` in the same document (or included via xinclude).
