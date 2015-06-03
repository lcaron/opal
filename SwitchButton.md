# Introduction #
![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/switchbutton.jpg](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/switchbutton.jpg)

This is a switch button to pick one value.

# Usage #

A switch button is a different graphic representation of a checkbox button. So the API is similar to the [Button](http://help.eclipse.org/helios/topic/org.eclipse.platform.doc.isv/reference/api/org/eclipse/swt/widgets/Button.html) widget. You can get or set the selection,add selection listeners, enable or disable the button...

```
final SwitchButton button = new SwitchButton(shell, SWT.NONE);
button.setText("Switch button...");
button.setTextForSelect("Selected...");
button.setTextForUnselect("Unselected...");
button.addSelectionListener(new SelectionListener() {

	@Override
	public void widgetSelected(final SelectionEvent e) {
		System.out.println("Before clicking, the selection was... " + button.getSelection());
	}

	@Override
	public void widgetDefaultSelected(final SelectionEvent e) {
	}
});
```

# Customisation #

By default, the button is grey, without border, with a blue background and 2 labels "On" and "Off". You can customize almost all elements of a switch button by setting the following properties :
  * `textForSelect` : text associated to the selected state of the button
  * `textForUnselect` : text associated to the unselected state of the button
  * `text` : text displayed besides the button
  * `round` : if true, the switch button's corners are round. If false, the switch button's corners are square.
  * `borderColor` : if set, a border is displayed around the widget.
  * `focusColor` : Color of the button when the focus is on.
  * `selectedForegroundColor` and `selectedBackgroundColor` : Color of the button (text color and background color) of the selected state of the button.
  * `unselectedForegroundColor` and `unselectedBackgroundColor` :  : text associated to the select state of the button
  * `buttonBorderColor`, `buttonBackgroundColor1` and `buttonBackgroundColor1` : Colors of the button (border and gradient)
  * `gap` : gap between the button and the associated text
  * `foreground, background` : foreground and background colors of the whole widget.
  * `font` : font of the widget


# Examples #

An example called **SwitchButtonSnippet** is available in the directory **src/test/java/org/mihalis/opal/switchButton**

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/switchButton/SwitchButtonSnippet.java