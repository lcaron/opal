# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/header.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/header.png)

A widget that display a header, composed of a title, an image and a description.

# Usage #

The header is composed of :
  * A title (cf. `setTitle`), displayed in bold and in blue by default.
    * You can change the font and the color by setting the 2 properties `titleFont` and `titleColor`
  * A description (cf. `setDescription`
  * An image (optional, cf. `setImage`)
  * A gradient background
    * You can change the color by setting the properties `gradientStart` and `gradientEnd`

The description can contains some pseudo-HTML tags for formatting :
  * `<br/>` for adding a line break
  * `<i>`...`</i>` to render text in italic
  * `<u>`...`</u>` to render text in underline
  * `<b>`...`</b>` to render text in bold
  * `<size>`...`</size>` to increase/decrease text size. You can use the following syntaxes : <size=10> (10px), <size=+4>, <size=-4>
  * `<color>`...`</color>` to change foreground color. You can use the following syntaxes : `<color=#FFCCAA>` (HTML color code), `<color=9,255,10>` (RGB values) and `<color=aliceblue>` (HTML color code)
  * `<backgroundcolor>`...`</backgroundcolor>` to change background color. You can use the following syntaxes : `<backgroundcolor=#FFCCAA>` (HTML color code), `<backgroundcolor=9,255,10>` (RGB values) and `<backgroundcolor=aliceblue>` (HTML color code)

```
final Header header = new Header(shell, SWT.NONE);
header.setTitle("Header title");
header.setImage(icon);
header.setDescription("Description area for the header. You can put all <b>additional</b>, <i>relevant information</i> to the description panel (or <u>jokes</u>, citations, ... what you want !)");
```

# Example #

An example called **HeaderSnippet.java** is located in the directory **src/test/java/org/mihalis/opal/header**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/header/HeaderSnippet.java