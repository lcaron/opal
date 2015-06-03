# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/totd1.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/totd1.png)
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/totd2.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/totd2.png)
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/totd3.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/totd3.png)

# Usage #

This component is a widget that displays a "Tip of The Day". It contains the area to display the tip, buttons to navigate through the list of tips, a close button and optionnaly a checkbox "show tips on startup".

The first thing to do if to create an instance of TipOfTheDay and add tips :
```
final TipOfTheDay tip = new TipOfTheDay();
tip.addTip("This is the first tip<br/> " + "<b>This is the first tip</b> " + "<u>This is the first tip</u> " + "<i>This is the first tip</i> " + "This is the first tip " + "This is the first tip<br/>" + "This is the first tip "
		+ "This is the first tip");
tip.addTip("This is the second tip<br/> " + "<b>This is the second tip</b> " + "<u>This is the second tip</u> <br/>" + "<i>This is the second tip</i> " + "This is the second tip " + "This is the second tip <br/>" + "This is the second tip "
		+ "This is the second tip");
```

**Please notice that tips can contain HTML code !**


You can set the style and customize the image :
```
tip.setStyle(TipStyle.TWO_COLUMNS_LARGE);
tip.setImage(myImage);
```

Finally, you open the window :
```
tip.open(mainShell);
```

You can get the status of the checkbox "show tip on startup" by calling the method `showOnStartup`.

# Example #

An example called **TipOfTheDaySnippet** is available in the directory **src/test/java/org/mihalis/opal/tipOfTheDay**

You can also have a look here : [http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/tipOfTheDay/TipOfTheDaySnippet.java](http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/tipOfTheDay/TipOfTheDaySnippet.java)