# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/brushedmetal.jpg](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/brushedmetal.jpg)

This composite displays a brushed metal texture "à la mac". It is based on the article **Creating a Brushed Metal Texture** (http://www.jhlabs.com/ip/brushed_metal.html) by Jerry Huxtable.

# Usage #

You can use this control like a SWT composite :

```
// Displays the composite
final BrushedMetalComposite bmc = new BrushedMetalComposite(shell, SWT.NONE);

// And the content
final RowLayout layout2 = new RowLayout(SWT.VERTICAL);
layout2.marginWidth = layout2.marginHeight = layout2.spacing = 10;
bmc.setLayout(layout2);
for (int i = 0; i < 8; i++) {
    final Button button = new Button(bmc, SWT.RADIO);
    button.setText("Button " + i);
}
```

# Parameters #

You can play with 5 parameters :
  * **Radius** - the "radius" of the blur.
  * **Amount** - the amount of noise to add (range 0-1)
  * **Color** - the color of the metal
  * **Shine** - the amount of shine to add (range 0-1)
  * **Monochrome** - true if the noise should be monochrome

For a better understanding of the influence of these parameters, you can play with the application"BrushedMetalCompositePlayer" (file src/test/java/org/mihalis/opal/brushedMetalComposite/BrushedMetalCompositePlayer.java).

# Examples #

2 examples are provided :
  * A snippet that use the composite : File src/test/java/org/mihalis/opal/brushedMetalComposite/SnippetBrushedMetalComposite.java
  * A tiny application to play with parameters : File src/test/java/org/mihalis/opal/brushedMetalComposite/BrushedMetalCompositePlayer.java

These examples are also available here :
  * http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/brushedMetalComposite/SnippetBrushedMetalComposite.java
  * http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/brushedMetalComposite/BrushedMetalCompositePlayer.java