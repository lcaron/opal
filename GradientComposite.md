# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/gradient.jpg](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/gradient.jpg)

This is a composite that displays a gradient in its background.

# Usage #

The gradient composite is a composite with just a little taste of color behind :) By default, the gradient behind is grey to white.
You can change the colors by modifying the "gradientEnd" and "gradientStart" properties.

The usage is similar to the Composite widget :
```
final GradientComposite composite = new GradientComposite(shell, SWT.NONE);
composite.setGradientEnd(display.getSystemColor(SWT.COLOR_WHITE));
composite.setGradientStart(display.getSystemColor(SWT.COLOR_DARK_RED));
```

# Example #
An example called **SnippetGradientComposite.java** is avalaible in the directory **src/test/java/org/mihalis/opal/gradientComposite**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/gradientComposite/SnippetGradientComposite.java