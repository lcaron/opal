# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/launcher.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/launcher.png)

A widget used to launch applications, modules or anything, with a simple and fancy zoom effect, widely inspired by work of Romain Guy.

# Usage #

This launcher contains 4 methods :
  * 2 methods to add and remove selection listeners (`addSelectionListener` and `removeSelectionListener`)
  * One method to add and item : `void addItem(final String title, final String image)` where **title** is the text located under the **image**
  * A method to get the selected item : `int getSelection()`

```
final Launcher l = new Launcher(shell, SWT.NONE);
l.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
l.addItem("Address Book", "org/mihalis/opal/launcher/icons/x-office-address-book.png");
l.addItem("Calendar", "org/mihalis/opal/launcher/icons/x-office-calendar.png");
l.addItem("Presentation", "org/mihalis/opal/launcher/icons/x-office-presentation.png");
l.addItem("Spreadsheet", "org/mihalis/opal/launcher/icons/x-office-spreadsheet.png");

l.addSelectionListener(new SelectionAdapter() {

	/**
	 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(final SelectionEvent e) {
		Dialog.inform("Selection", "You have selected item #" + l.getSelection());
	}

});
```


# Animation #

An animation is available at http://opal.eclipselabs.org.codespot.com/hg.wiki/wink/launcher.swf

# Example #

A example called **LauncherSnippet** is located in the directory **src/test/java/org/mihalis/opal/launcher**

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/launcher/LauncherSnippet.java