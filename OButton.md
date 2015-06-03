# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/obutton.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/obutton.png)

Customized buttons to replace the native Button widget.

# Usage #

Just replace Button by OButton :
```
final OButton button1 = new OButton(shell, SWT.PUSH);
button1.setText("Normal button");
button1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
button1.setButtonRenderer(renderer);

final OButton button2 = new OButton(shell, SWT.PUSH);
button2.setText("Text & image");
button2.setImage(icon);
button2.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
button2.setButtonRenderer(renderer);
```

And _voilà_ !

You can customize your buttons: selection image, image when not selected or disabled, font color, size...

# Examples #

An example called **OButtonSnippet.java** is located in the directory **src/test/java/org/mihalis/opal/obutton**.

This example is also available here :
  * http://opal.eclipselabs.org.codespot.com/hg/src/test/java/org/mihalis/opal/obutton/OButtonSnippet.java