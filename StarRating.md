# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/starrating.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/starrating.png)

A simple star rating toolbar that allows the user to rate anything.

# Usage #

This is very simple : you instantiate a StarRating widget, you set the size of stars (SMALL,BIG) and the number of stars :

```
final StarRating sr = new StarRating(shell, SWT.NONE);
sr.setSizeOfStars(SIZE.SMALL);
sr.setMaxNumberOfStars(10);
```

And _voilà_ !

# Examples #

An example called **StarRatingSnippet.java** is located in the directory **src/test/java/org/mihalis/opal/starRating/**.

This example is also available here :
  * http://opal.eclipselabs.org.codespot.com/hg/src/test/java/org/mihalis/opal/starRating/StarRatingSnippet.java