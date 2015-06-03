# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/checkBoxGroup1.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/checkBoxGroup1.png)
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/checkBoxGroup2.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/checkBoxGroup2.png)

A group with a checkbox. When one clicks on the checkbox, the whole content of the group is deactivated.

# Usage #

This widget is divided in 2 parts :
  * The checkbox that activates/deactivates the content
    * You can set/get the text and the font of the checkbox
    * You can get the value of the selection by using the getter `boolean isActivated()`
    * You can set the state of this checkbox (and thus enable or disable the content of the group) by using the methode `activate()` and `deactivate()`
  * The content, which is a `Composite`, accessible with the getter `getContent()`
    * Use this composite to store elements

# Example #

An example called **SnippetCheckBoxGroup** is available in the directory **src/test/java/org/mihalis/opal/checkBoxGroup**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/checkBoxGroup/SnippetCheckBoxGroup.java