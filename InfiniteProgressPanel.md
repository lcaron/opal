# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/infiniteprogresspanel.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/infiniteprogresspanel.png)

This widget is dedicated to long task operations, widely inspired by work of Romain Guy.

# Usage #

An Infinite Progress Panel is attached to a shell. In fact, the panel is a transparent window (shell) located on another window.

The first thing to do is to get a panel :

```
final InfiniteProgressPanel panel = InfiniteProgressPanel.getInfiniteProgressPanelFor(shell);
```

Then you can "customize" it :
```
panel.setText("Please wait...");
panel.setTextColor(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
panel.setTextFont(new Font(shell.getDisplay(), "Lucida Sans", 18, SWT.BOLD));
```

To start and stop the panel, simply use the **start** and **stop** methods :
```
panel.start();
... // Long time operation
panel.stop();
```

_Et voilà !_

You can customize the panel (there is a setter for each value) :
  * alpha : alpha value of the panel (between 0 and 255)
  * bars count : number of bars
  * default color : color of the bars
  * FPS : number of frames per second
  * line width : thickness of the bars
  * selection color : color of the highlighted bar
  * text, text color and text font

# Animation #

An animation is available at http://opal.eclipselabs.org.codespot.com/hg.wiki/wink/infinite.swf

# Example #

An example (SnippetInfiniteProgressPanel) is provided in the directory **src/test/java/org/mihalis/opal/InfinitePanel**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/infinitePanel/SnippetInfiniteProgressPanel.java