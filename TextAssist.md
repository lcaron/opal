# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/TextAssist.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/TextAssist.png)

This widget is very similar to the [Text](http://help.eclipse.org/helios/topic/org.eclipse.platform.doc.isv/reference/api/org/eclipse/swt/widgets/Text.html). It has the same look, the same methods, the same behaviour.
The difference appears when one types something : some proposals are displayed under the widget (like the code assist function in Eclipse).

# Constructor #

There is a big difference with other SWT widget : there is no constructor TextAssist(Composite parent, int style).

The only constructor available is `public TextAssist(final Composite parent, final int style, final TextAssistContentProvider contentProvider)`
where
  * parent is the composite control which will be the parent of the new instance (cannot be null)
  * style is the style of the control (see the style available for Text)
  * contentProvider is your content provider (see below)

# Number of proposal #

You can change the maximum number of displayed proposal (default value is 10) with the method `public void setNumberOfLines(final int numberOfLines)`.

# Content provider #

A content provider is an object that returns a list of proposal depending of what the user typed.
It has to extend the class **org.mihalis.opal.TextAssist.TextAssistContentProvider** and extends the method `public abstract List<String> getContent(final String entry)`.

The entry is a string that contains what the user typed. This string is never null.
The result is a list of strings that match the entry. This list can be empty or null. In this case, no choice is proposed to the user.

If the size is bigger than the maximum number of proposals (set in the widget, default value:10), the list is truncated.
To get the max number of proposal, you can use the method **getMaxNumberOfLines**.

```
final TextAssistContentProvider contentProvider = new TextAssistContentProvider() {

	private final String[] EUROZONE = new String[] { "Austria", "Belgium", "Cyprus", 
          "Estonia", "Finland", "France", "Germany", "Greece", "Ireland", "Italy", 
          "Luxembourg", "Malta", "Netherlands", "Portugal", "Slovakia", "Slovenia", "Spain" };

	@Override
	public List<String> getContent(final String entry) {
		final List<String> returnedList = new ArrayList<String>();

		for (final String country : this.EUROZONE) {
			if (country.toLowerCase().startsWith(entry.toLowerCase())) {
				returnedList.add(country);
			}
		}

		return returnedList;
	}
};
```

# Example #

An example called **TextAssistSnippet.java** is available under the directory **src/test/java/org/mihalis/opal/TextAssist**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/textAssist/TextAssistSnippet.java