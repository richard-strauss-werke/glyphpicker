---
layout: page
title: "Development"
category: doc
date: 2014-12-15 06:23:59
---

#### Setup

- download the plugin repository at https://github.com/richard-strauss-werke/glyphpicker
- if Maven is not installed on your system, download it from http://maven.apache.org/download.cgi and follow the setup instructions in the readme file 
- install the oXygen SDK by following the steps described at http://www.oxygenxml.com/oxygen_sdk_maven.html
- open a command prompt at the project root and type `mvn install`. This should install all the project's dependencies

### Package

- package the project by typing `mvn assembly:assembly` from the project's root directory
