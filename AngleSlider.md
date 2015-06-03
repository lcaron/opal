# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/AngleSlider.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/AngleSlider.png)

The AngleSlider is just a widget designed to pick angles.

# Usage #

Currently this widget is very very simple, and not customizable.
For example, its size is fixed because the "wheel" is an image.

You can just :
  * select a value with the method `setSelection(int value)`
  * get the selection with the method `getSelection`, which returns the angle in degrees between 0 and 360.
  * Add a selection listener, fired when the selection changed.

# Example #

A snippet called **AngleSliderSnippet** is available in the directory **src/test/java/org/mihalis/opal/angles**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/angles/AngleSliderSnippet.java

# Future enhancements #

Future enhancements are planned :
  * Vectorial drawing, so the size of the widget will be dynamic
  * Orientation : currently the value "0" is located in the bottom of the circle, it should be anywere
  * Add a reference mark for 0 degree or common angles (45°, 90°...)

