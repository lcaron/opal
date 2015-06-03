# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/transitionComposite.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/transitionComposite.png)

This is a composite that hold multiple controls, like CTabFolder. You can programmaticaly switch from Control to Control with a little animation.

# Usage #

Once the widget is initialised :
```
TransitionComposite transitionComposite = new TransitionComposite(shell, SWT.NONE);
```

You can add control by calling the method `addControl`.

Then, you choose your transition effect (by calling `setTransition`) which is one of the component of the `Transition` enumeration : LEFT\_TO\_RIGHT, LEFT\_TO\_RIGHT\_AND\_APPEAR, RIGHT\_TO\_LEFT, RIGHT\_TO\_LEFT\_AND\_APPEAR, UP\_TO\_DOWN, UP\_TO\_DOWN\_AND\_APPEAR, DOWN\_TO\_UP, DOWN\_TO\_UP\_AND\_APPEAR, NONE.

Then, you can navigate through the controls with the methods `moveToFirst`,`moveToLast`,`moveToPrevious`,`moveToNext` or `setSelection`.


# Animation #

An animation is available at http://opal.eclipselabs.org.codespot.com/hg.wiki/wink/transitionComposite.swf

# Example #

An example called **TransitionCompositeSnippet** is available in the directory **src/test/java/org/mihalis/opal/transitionComposite**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/transitionComposite/TransitionCompositeSnippet.java