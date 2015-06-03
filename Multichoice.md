# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/multichoice1.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/multichoice1.png)

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/multichoice2.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/multichoice2.png)

The Multichoice widget looks like a combo box in which you can select multiple items.
This widget has been designed to replace a set of comboboxes in a form where I ran out of space.

You can "fill" the widget with an array of String or some POJO. In this case, the widget uses the `toString` method of the POJO. Please notice that your POJO has to implement the `hashCode` and `equals` methods.

# Constructors #

This widget provides 2 constructors :
`public MultiChoice(final Composite parent, final int style)`

The "classical" constructor of a SWT widget. Please notice that once the widget has been created, you have to add data.

`MultiChoice(final Composite parent, final int style, final List<T> elements)`

This constructor builds the widget and initialises it with a list of elements.

# Add elements #

To add one or many elements, you can use one of the following methods :
  * `public void add(final T value)` : add a value at the end of the list
  * `public void add(final T value, final int index)` : add a value at a specified index
  * `public void addAll(final List<T> values)` : add a list of elements at the end of the widget list of elements.
  * `public void addAll(final T[] values)` : add an array of elements at the end of the widget list of elements.

# Remove elements #

To remove one or many elements, you can use one of the following methods :
  * `public void removeAt(final int index)` : remove a specified element
  * `public void remove(final T object)` : remove a specified element
  * `public void removeAll()` : remove all elements


# Retrieve selection #

To retrieve information about the elements stored by the widget, you can use one of the following methods :
  * `public T getItem(final int index)`, to get an item at a given position
  * `public int getItemCount()`, to get the number of items
  * `public List<T> getItems()`, to retrieve _a copy_ of the items present in the widget

# Select/Deselect elements #

To select one or many elements, you can use one of the following methods :
  * `public void setSelection(final Set<T> selection)` : set the selection to a given set of selection
  * `public void selectAll()` : select all elements
  * `public void selectAt(final int index)` and `public void select(final T value)` : select a given element
  * `public void setSelectedIndex(final int[] index)` : set the selected elements (based on their index)

To add one or many elements, you can use one of the following methods :
  * `public void deselectAt(final int index)` and `public void deselect(final T value)` : de-select an element
  * `public void deselectAll()`

# Retrieve selected elements #

To get the selected elements, you can use one of the following methods :
  * `public int[] getSelectedIndex()` : returns the indexes of the selected elements
  * `public List<T> getSelection()` : returns _a copy_ of the selected elements

# Change Look #

You can change the look of the widget by using the following methods :
  * `public void setNumberOfColumns(final int numberOfColumns)` : set the number of columns displayed in the pop-up window. (default value is 2)
  * `public void setSeparator(final String separator)` : set the separator between elements (default value is a comma [,]).

You can also use overrided methods (setForeground, setBackground, setFont...)

# Listeners #

This widget has its own selection listener called **MultiChoiceSelectionListener**, which contains only one method :

`public abstract void handle(MultiChoice<T> parent, T receiver, boolean selected, Shell popup)`

This method is called when an element is selected or de-selected. The arguments are the following :
  * parent : the parent widget
  * receiver : the element that has been selected or de-selected
  * selected : if **true**, the element has been selected. If **false**, the element has been de-selected
  * popup : the pop-up window. You can play with it (close the window, change the background color...).

# Example #

You can find an example of use of this widget called **MultiChoiceSnippet** in the directory **src/test/java/org/mihalis/opal/multiChoice**.
It uses a POJO called "Country".

```
final String[] euroZone = new String[] { "Austria", "Belgium", "Cyprus", "Estonia", "Finland", "France", 
"Germany", "Greece", "Ireland", "Italy", "Luxembourg", "Malta", "Netherlands", "Portugal", 
"Slovakia", "Slovenia", "Spain" };
final MultiChoice<String> mcSimple = new MultiChoice<String>(shell, SWT.None);
mcSimple.addAll(euroZone);
```

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/multiChoice/MultiChoiceSnippet.java