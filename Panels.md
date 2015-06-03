# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/blurredPanel.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/blurredPanel.png)
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/darkPanel.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/darkPanel.png)

These panels can be use to put focus on a particular element.
The **blurredPanel** blurs the content of the window, the **darkPanel** makes the window darker.

# Usage #

The usage is very simple :
  * You create a DarkPanel or a BlurredPanel
  * You can change the radius value of the BlurredPanel or the alpha value of the DarkPanel
  * You open the panel with the method `show`
  * You close the panel with the method `hide`

```
final BlurredPanel p = new BlurredPanel(shell);

...

final Button ok = new Button(shell, SWT.PUSH);
ok.setText("Ok");
...
ok.addSelectionListener(new SelectionListener() {

	@Override
	public void widgetSelected(final SelectionEvent e) {

		p.show();
		Dialog.isConfirmed("Confirmation", "Are you sure you want to save this form ?");
		p.hide();
	}

	@Override
	public void widgetDefaultSelected(final SelectionEvent e) {
	}
});
```

# Animation #

Two animations are available at
  * http://opal.eclipselabs.org.codespot.com/hg.wiki/wink/blur.swf
  * http://opal.eclipselabs.org.codespot.com/hg.wiki/wink/dark.swf


# Example #

2 examples called **SnippetBlurredPanel** and **SnippetDarkPanel** are located in the directory **src/test/java/org/mihalis/opal/panels**.

These examples are also available here :
  * http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/panels/SnippetBlurredPanel.java
  * http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/panels/SnippetDarkPanel.java