# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/propertyTable.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/propertyTable.png)

A widget that allow user to set up properties in a table or a tree table.

# Usage #

The first thing to do is to instantiate the widget :
```
final PropertyTable table = new PropertyTable(tabFolder, SWT.NONE);
```

By default, the table shows items in categories, the "sort" and "show description" buttons are displayed, and the description
panel is present.

You can customize your widget with the following methods :
```
final PropertyTable table = new PropertyTable(tabFolder, SWT.NONE);

table.showButtons();
table.hideButtons();

table.viewAsCategories();
table.viewAsFlatList();

table.showDescription();
table.hideDescription();
```

Then you fill your table with `PTProperty` objects. These objects are composed of the following fields :
  * **String name:** The name of the property
  * **String displayName** : The label that is displayed in the PropertyTable
  * **String description** : The description (if the description panel is shown). You can use the following HTML tags for the presentation : `<b>`, `<i>` and `<u>`.
  * **Object value** : The value
  * **String category** : The category
  * **boolean enabled** : if true (default value), the property can be modified.
  * **PTEditor editor** : An editor (optional, see below)


2 constructors are available :
```
public PTProperty(final String name, final String displayName, final String description);
public PTProperty(final String name, final String displayName, final String description, final Object value):
```

You can group your properties in the same category. If you choose the view "as category", all properties will appear in a tree under the category.
Because all setters return the PTProperty itself, you can chain multiple setters :
```
table.addProperty(new PTProperty("id", "Identifier", "Description for identifier", "My id")).setCategory("General");
table.addProperty(new PTProperty("text", "Description", "Description for the description field", "blahblah...")).setCategory("General");
table.addProperty(new PTProperty("url", "URL:", "This is a nice <b>URL</b>", "http://www.google.com").setCategory("General")).setEditor(new PTURLEditor());
table.addProperty(new PTProperty("password", "Password", "Enter your <i>password</i> and keep it secret...", "password")).setCategory("General").setEditor(new PTPasswordEditor());
```

# Editors #

If no editor is set, the PropertyTable will use by default the **PTStringEditor**.

## PTStringEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptstringeditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptstringeditor.png)

**Accepted data type :** All
**Constructor :** `public PTStringEditor()`

This editor is used to modify any kind of property. It is represented by a Text widget.


---


## PTIntegerEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptintegereditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptintegereditor.png)

**Accepted data type :** Integer
**Constructor :** `public PTIntegerEditor()`

This editor is used to modify an Integer. It is represented by a Text widget that accepts only integer values.


---


## PTFloatEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptfloateditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptfloateditor.png)

**Accepted data type :** Float
**Constructor :** `public PTFloatEditor()`

This editor is used to modify a Float. It is represented by a Text widget that accepts only float values.


---


## PTURLEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/pturleditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/pturleditor.png)

**Accepted data type :** String (URL)
**Constructor :** `public PTURLEditor()`

This editor is used to modify any kind of property. It is represented by a Text widget.


---


## PTPasswordEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptpasswordeditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptpasswordeditor.png)

**Accepted data type :** All
**Constructor :** `public PTPasswordEditor()`

This editor is used to modify any kind of property. It is represented by a Text widget.


---


## PTSpinnerEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptspinnereditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptspinnereditor.png)

**Accepted data type :** Integer
**Constructor :** `public PTSpinnerEditor(int minimumValue, int maximumValue)`

This editor is used to modify an Integer. It is represented by a Spinner widget.


---


## PTFileEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptfileeditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptfileeditor.png)

**Accepted data type :** String
**Constructor :** `public PTFileEditor()`

This editor is used to modify any kind of property that correspond to a file path. It is represented by a text with 2 buttons.
The "X" button erases the value of the property (set it to null). The "..." button opens a new window that allow the user
to pick a file.


---


## PTDirectoryEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptdirectoryeditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptdirectoryeditor.png)

**Accepted data type :** String
**Constructor :** `public PTDirectoryEditor()`

This editor is used to modify any kind of property that correspond to a directory path. It is represented by a text with 2 buttons.
The "X" button erases the value of the property (set it to null). The "..." button opens a new window that allow the user
to pick a directory.



---


## PTComboEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptcomboeditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptcomboeditor.png)

**Accepted data type :** All
**Constructors :**
  * `public PTComboEditor(final boolean readOnly, final Object... data)`
  * `public PTComboEditor(final Object... data)`

This editor is used to modify any kind of property. It is represented by a Combo widget. The PropertyTable uses the toString() method of the object to display
value, so you can use a Java Bean.


---


## PTCheckboxEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptcheckboxeditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptcheckboxeditor.png)

**Accepted data type :** Boolean
**Constructor :** `public PTCheckboxEditor()`

This editor is used to modify a boolean property. It is represented by a Checkbox widget.


---


## PTColorEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptcoloreditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptcoloreditor.png)

**Accepted data type :** org.eclipse.swt.graphics.Color
**Constructor :** `public PTColorEditor()`

This editor is used to modify A Color. It is represented by a text with 2 buttons.
The "X" button erases the value of the property (set it to null). The "..." button opens a new window that allow the user
to pick a color.


---


## PTFontEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptfonteditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptfonteditor.png)

**Accepted data type :** org.eclipse.swt.graphics.FontData
**Constructor :** `public PTFontEditor()`

This editor is used to modify a Font. It is represented by a text with 2 buttons.
The "X" button erases the value of the property (set it to null). The "..." button opens a new window that allow the user
to pick a Font.



---


## PTDateEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptdateeditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptdateeditor.png)

**Accepted data type :** java.util.date
**Constructor :** `public PTDateEditor()`

This editor is used to modify a date (no time). It is represented by a DateTime widget.


---


## PTDimensionEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptdimensioneditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptdimensioneditor.png)

**Accepted data type :** java.awt.Dimension
**Constructor :** `public PTDimensionEditor()`

This editor is used to modify a Dimension. It is represented by a text with 2 buttons.
The "X" button erases the value of the property (set it to null). The "..." button opens a new window that allow the user
to enter the width and the height of a dimension.



---


## PTRectangleEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptrectangleeditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptrectangleeditor.png)

**Accepted data type :** org.eclipse.swt.graphics.Rectangle
**Constructor :** `public PTRectangleEditor()`

This editor is used to modify a Dimension. It is represented by a text with 2 buttons.
The "X" button erases the value of the property (set it to null). The "..." button opens a new window that allow the user
to enter the x coordinate, the y coordinate, the width and the height of a rectangle.




---


## PTInsetsEditor ##
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptinsetseditor.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/pt/ptinsetseditor.png)

**Accepted data type :** java.awt.Insets
**Constructor :** `public PTInsetsEditor()`

This editor is used to modify a Insets object. It is represented by a text with 2 buttons.
The "X" button erases the value of the property (set it to null). The "..." button opens a new window that allow the user
to enter the top, the left, the bottom and the right part of an insets.

# Listeners #

You can add a `PTPropertyChangeListener` that is called when the value of the property has changed.
The method is called `addChangeListener()`.

# Example #

A snippet called **PropertyTableSnippet** is available in the directory **src/test/java/org/mihalis/opal/propertyTable**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/propertyTable/PropertyTableSnippet.java