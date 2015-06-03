# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/duallist.jpg](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/duallist.jpg)

This widget allows the user to select one or many elements, and order them. I code this one because I was not satisfied by the existent widgets I found on the Internet.

# Usage #

The usage is pretty simple, because the API is very similar to the [List](http://help.eclipse.org/helios/topic/org.eclipse.platform.doc.isv/reference/api/org/eclipse/swt/widgets/List.html) API.
You can easily add or remove one or many elements, select one or many elements, clear the widget or add selection listener(s).

The widget displays **DLItems**

# DLItem #

A DLItem is a POJO where you can store :
  * A text
  * An image
  * A background color
  * A foreground color
  * Data

# Example #

An example called **DualListSnippet** is available in the directory **src/test/java/org/mihalis/opal/itemSelector**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/itemSelector/DualListSnippet.java