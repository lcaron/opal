# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/RoundedToolbar.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/RoundedToolbar.png)

A simple rounded toolbar with grey icons.

# Usage #

This is very simple : you instantiate a RoundedToolbar and then you create RoundedToolItems :

```
final RoundedToolbar roundedToolBar = new RoundedToolbar(shell, SWT.NONE);

RoundedToolItem item = new RoundedToolItem(roundedToolBar);
item.setTooltipText("Simple item");
item.setSelectionImage(iconBubble1w);
item.setImage(iconBubble1b);
item.setWidth(40);

```

And _voilà_ !

You can customize your buttons: selection image, image when not selected or disabled, font color, size...

# Examples #

An example called **RoundedToolbarSnippet.java** is located in the directory **src/test/java/org/mihalis/opal/roundedToolbar**.

This example is also available here :
  * http://opal.eclipselabs.org.codespot.com/hg/src/test/java/org/mihalis/opal/roundedToolbar/RoundedToolbarSnippet.java