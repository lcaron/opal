# Introduction #

![http://opal.eclipselabs.org.codespot.com/hg.wiki/images/columnBrowser.png](http://opal.eclipselabs.org.codespot.com/hg.wiki/images/columnBrowser.png)

A widget that displays a tree structure _a la mac_ in miller columns (look at http://en.wikipedia.org/wiki/Miller_Columns).

# Usage #

## ColumnBrowserWidget ##

The widget itself contains the following method :
  * `public ColumnBrowserWidget(final Composite parent, final int style)` : Constructor the widget
  * `public void addSelectionListener(final SelectionListener listener)` : Add a selection listener
  * `public void clear(final boolean needPacking)` : remove all data of the widget
  * `public ColumnItem getSelection()` : get the **ColumnItem** selected (or null)
  * `public void removeSelectionListener(final SelectionListener listener)`
  * `public void select(final ColumnItem item)` : select a given item




## Column item ##

The elements displayed in the widget are called **ColumnItem**

  * `public ColumnItem(final ColumnBrowserWidget widget)` : create an item and associate it to a given widget
  * `public ColumnItem(final ColumnBrowserWidget widget, final int index)` : idem, but the item is at a zero-based given position
  * `public ColumnItem(final ColumnItem parent)` : create a item that is a child of another **ColumnItem**
  * `public ColumnItem(final ColumnItem parent, final int index)` : idem, but the item is at a zero-based given position
  * `public void remove(final ColumnItem item)` : remove a child of the current item
  * `public void remove(final int index)` : remove a child of the current item
  * `public void removeAll()` : remove all childs of the current item
  * `public ColumnItem getItem(final int index)` : get a children at a given position
  * `public int getItemCount()` : get the number of children of the current item
  * `public ColumnItem[] getItems()` : get all children of the current item
  * `public ColumnBrowserWidget getParent()` : get the widget that stores the current item
  * `public ColumnItem getParentItem()` : get the parent of the current item, or **null** if there is not
  * `public int indexOf(final ColumnItem item)` : returns the position of a children


You can notice that the behaviour of this widget is very similar to the couple [Tree](http://help.eclipse.org/helios/topic/org.eclipse.platform.doc.isv/reference/api/org/eclipse/swt/widgets/Tree.html)/[TreeItem](http://help.eclipse.org/helios/topic/org.eclipse.platform.doc.isv/reference/api/org/eclipse/swt/widgets/TreeItem.html).

# Example #

An example called **ColumnsSnippet.java** is located in the directory **src/test/java/org/mihalis/opal/columns**.

This example is also available here : http://code.google.com/a/eclipselabs.org/p/opal/source/browse/src/test/java/org/mihalis/opal/columns/ColumnsSnippet.java