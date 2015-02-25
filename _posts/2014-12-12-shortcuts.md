---
layout: page
title: "Keyboard Shortcuts"
category: doc
date: 2014-12-12 06:23:59
---

### Plugin shortcut

- Defaults to `Command+Shift+G` on a MAC / `Ctrl+Shift+G` on other systems. If this combination is already 
occupied in you oXygen, either remove the key combination in oXygen's `Options / Keyboard Shortcuts` dialog or 
adjust the plugin's shortcut by opening the plugin's `Settings` dialog, selecting the `Plugin` tab and entering a 
new key combination.
The default shortcut string is "meta shift G" on a MAC, "ctrl shift G" on other systems. You may use any of
 the following modifiers: "shift | control | ctrl | meta | alt | altGraph". For more information, 
 see the [Java 6 documentation](http://docs.oracle.com/javase/6/docs/api/javax/swing/KeyStroke.html#getKeyStroke%28java.lang.String%29).

### Keyboard actions in the plugin panel

#### Both tabs

- `Enter` - Insert the selected character into the XML document at caret position
- `Ctrl+1` / `Command+1` - Show the `User Collection` tab
- `Ctrl+2` / `Command+2` - Show the `All Characters` tab
- `Ctrl+F` / `Command+F` - Set the focus on the search field
- `Ctrl+O` / `Command+O` - Turn code point based sorting on / off
- `Ctrl+T` / `Command+T` - Switch between grid and table view
- `Ctrl+E` / `Command+E` - Open the `Settings` dialog
- `Esc` - return from the plugin panel to the previous component, e.g. the editor panel

#### `All Characters` tab 

- `Ctrl+Enter` / `Command-Enter` - Copy the selected character to the user collection

#### `User Collection` tab

- `Ctrl+UP` / `Command+UP` - Move the selected character up in the list
- `Ctrl+DOWN` / `Command+DOWN` - Move the selected character down in the list
- `Ctrl+D` / `Command+D` - Remove the selected character from the list
- `Ctrl+S` / `Command+S` - Save the list to disk
- `Ctrl+L` / `Command+L` - Reload the list from disk; any changes will be undone
