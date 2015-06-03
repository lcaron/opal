# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/imageselector.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/imageselector.png)

A fancy image selector, widely inspired by work of Romain Guy.

# Usage #

Items displayed in the selector are **ISItems**. 2 constructors are available :
  * `public ISItem(final String fileName)`, to initialize an item without title. You just give the path to the image.
  * `public ISItem(final String title, final String fileName)`, to initialize an item with a title.

The first thing to do is to prepare a list of items :
```
final List<ISItem> items = new LinkedList<ISItem>();
items.add(new ISItem("Black Eyed Peas", "org/mihalis/opal/imageSelector/images/Black Eyed Peas.jpg"));
items.add(new ISItem("Coldplay", "org/mihalis/opal/imageSelector/images/Coldplay.jpg"));
items.add(new ISItem("Foo Fighters", "org/mihalis/opal/imageSelector/images/Foo Fighters.jpg"));
items.add(new ISItem("Gorillaz", "org/mihalis/opal/imageSelector/images/Gorillaz.jpg"));
items.add(new ISItem("Green Day", "org/mihalis/opal/imageSelector/images/Green Day.jpg"));
items.add(new ISItem("Moby", "org/mihalis/opal/imageSelector/images/Moby.jpg"));
items.add(new ISItem("Norah Jones", "org/mihalis/opal/imageSelector/images/Norah Jones.jpg"));
items.add(new ISItem("Shivaree", "org/mihalis/opal/imageSelector/images/Shivaree.jpg"));
items.add(new ISItem("Sin City", "org/mihalis/opal/imageSelector/images/Sin City.jpg"));
```

Then you create your widget and you set the list :
```
final ImageSelector imageSelector = new ImageSelector(shell, SWT.NONE);
imageSelector.setItems(items);
```

Finalement, you can get the index of the selected image :
```
System.out.println("Index is " + imageSelector.getIndex());
```

You can use the mouse or the keyboard to select your image, by pressing the arrows or the PageUp and PageDown keys.

# Customization #
You can change the look of the widget by setting the following properties :
  * items (list of items)
  * font (font used to display the title)
  * index (index of the selected image)
  * maxItemWidth (maximum number of images that are displayed)
  * gradientStart and gradientEnd : gradient colors for the background
  * pageIncrement : increment when ones press the PageUp and PageDown keys.

# Animation #

An animation is available at http://opal.eclipselabs.org.codespot.com/hg.wiki/wink/imageSelector.swf


# Example #

An example called **ImageSelectorSnippet.java** is lcoated in the directory **src/test/java/org/mihalis/opal/imageSelector**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/imageSelector/ImageSelectorSnippet.java