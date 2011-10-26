/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron@gmail.com) - initial API and implementation
 *******************************************************************************/

package org.mihalis.opal.multiChoice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.mihalis.opal.utils.SimpleSelectionAdapter;

/**
 * The MultiChoice class represents a selectable user interface object that
 * combines a read-only text-field and a set of checkboxes.
 * 
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>NONE</dd>
 * <dt><b>Events:</b>
 * <dd>Selection</dd>
 * </dl>
 * 
 * @param <T> Class of objects represented by this widget
 */
public class MultiChoice<T> extends Composite {

	private Label text;
	private Button arrow;
	private Shell popup;
	private Listener listener, filter;
	private int numberOfColumns = 2;
	private List<T> elements;
	private Set<T> selection;
	private List<Button> checkboxes;
	private boolean hasFocus;
	private MultiChoiceSelectionListener<T> selectionListener;
	private T lastModified;
	private Color foreground, background;
	private Font font;
	private String separator;

	/**
	 * Constructs a new instance of this class given its parent.
	 * 
	 * @param parent a widget which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style not used
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 */
	public MultiChoice(final Composite parent, final int style) {
		this(parent, style, null);
	}

	/**
	 * Constructs a new instance of this class given its parent.
	 * 
	 * @param parent a widget which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style not used
	 * @param elements list of elements displayed by this widget
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 */
	public MultiChoice(final Composite parent, final int style, final List<T> elements) {
		super(parent, style);

		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = gridLayout.marginWidth = gridLayout.marginHeight = 0;
		this.setLayout(gridLayout);

		this.text = new Label(this, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
		this.text.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		this.text.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		this.arrow = new Button(this, SWT.ARROW | SWT.RIGHT);
		this.arrow.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));

		this.listener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (MultiChoice.this.popup == event.widget) {
					popupEvent(event);
					return;
				}

				if (MultiChoice.this.arrow == event.widget) {
					buttonEvent(event);
					return;
				}

				if (MultiChoice.this == event.widget) {
					multiChoiceEvent(event);
					return;
				}

				if (getShell() == event.widget) {
					getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							if (isDisposed()) {
								return;
							}
							handleFocus(SWT.FocusOut);
						}
					});
				}
			}
		};

		final int[] multiChoiceEvent = { SWT.Dispose, SWT.Move, SWT.Resize };
		for (int i = 0; i < multiChoiceEvent.length; i++) {
			this.addListener(multiChoiceEvent[i], this.listener);
		}

		final int[] buttonEvents = { SWT.Selection, SWT.FocusIn };
		for (int i = 0; i < buttonEvents.length; i++) {
			this.arrow.addListener(buttonEvents[i], this.listener);
		}

		this.filter = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				final Shell shell = ((Control) event.widget).getShell();
				if (shell == MultiChoice.this.getShell()) {
					handleFocus(SWT.FocusOut);
				}
			}
		};

		this.selection = new LinkedHashSet<T>();
		this.elements = elements;
		this.separator = ",";

		createPopup();
		setLabel();
	}

	/**
	 * Adds the argument to the end of the receiver's list.
	 * 
	 * @param values new item
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void add(final T value) {
		checkWidget();
		if (value == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (this.elements == null) {
			this.elements = new ArrayList<T>();
		}
		this.elements.add(value);
		refresh();
	}

	/**
	 * Adds the argument to the receiver's list at the given zero-relative
	 * index.
	 * 
	 * @param values new item
	 * @param index the index for the item
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void add(final T value, final int index) {
		checkWidget();
		checkNullElement();
		if (value == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		checkRange(index);

		this.elements.add(index, value);
		refresh();
	}

	/**
	 * Adds the argument to the end of the receiver's list.
	 * 
	 * @param values new items
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void addAll(final List<T> values) {
		checkWidget();
		if (values == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (this.elements == null) {
			this.elements = new ArrayList<T>();
		}
		this.elements.addAll(values);
		refresh();
	}

	/**
	 * Adds the argument to the end of the receiver's list.
	 * 
	 * @param values new items
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void addAll(final T[] values) {
		checkWidget();
		if (values == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (this.elements == null) {
			this.elements = new ArrayList<T>();
		}
		for (final T value : values) {
			this.elements.add(value);
		}
		refresh();
	}

	/**
	 * Returns the item at the given, zero-relative index in the receiver's
	 * list. Throws an exception if the index is out of range.
	 * 
	 * @param index the index of the item to return
	 * @return the item at the given index
	 * 
	 * @exception NullPointerException if there is no item in the receiver
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public T getItem(final int index) {
		checkWidget();
		checkNullElement();
		checkRange(index);

		return this.elements.get(index);
	}

	/**
	 * Returns the number of items contained in the receiver's list.
	 * 
	 * @return the number of items
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getItemCount() {
		checkWidget();
		if (this.elements == null) {
			return 0;
		}

		return this.elements.size();

	}

	/**
	 * Returns the list of items in the receiver's list.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its list of items, so modifying the array will not affect the receiver.
	 * </p>
	 * 
	 * @return the items in the receiver's list
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public List<T> getItems() {
		checkWidget();
		if (this.elements == null) {
			return null;
		}
		return new ArrayList<T>(this.elements);
	}

	/**
	 * Removes the item from the receiver's list at the given zero-relative
	 * index.
	 * 
	 * @param index the index for the item
	 * @exception NullPointerException if there is no item in the receiver
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void removeAt(final int index) {
		checkWidget();
		checkNullElement();
		checkRange(index);
		final Object removedElement = this.elements.remove(index);
		this.selection.remove(removedElement);
		refresh();
	}

	/**
	 * Searches the receiver's list starting at the first item until an item is
	 * found that is equal to the argument, and removes that item from the list.
	 * 
	 * @param object the item to remove
	 * @exception NullPointerException if there is no item in the receiver
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the object is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void remove(final T object) {
		if (object == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		checkWidget();
		checkNullElement();
		this.elements.remove(object);
		this.selection.remove(object);
		refresh();
	}

	/**
	 * Remove all items of the receiver
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void removeAll() {
		checkWidget();
		checkNullElement();
		if (this.elements != null) {
			this.elements.clear();
		}
		this.selection.clear();
		refresh();
	}

	/**
	 * Sets the selection of the receiver. If the item was already selected, it
	 * remains selected.
	 * 
	 * @param selection the new selection
	 * @exception NullPointerException if there is no item in the receiver
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the selection is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */

	public void setSelection(final Set<T> selection) {
		checkWidget();
		checkNullElement();
		if (selection == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.selection = selection;
		updateSelection();
	}

	/**
	 * Selects all selected items in the receiver's list.
	 * 
	 * @exception NullPointerException if there is no item in the receiver
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void selectAll() {
		checkWidget();
		checkNullElement();
		this.selection.addAll(this.elements);
		updateSelection();
	}

	/**
	 * Selects the item at the given zero-relative index in the receiver's list.
	 * If the item at the index was already selected, it remains selected.
	 * 
	 * @param index the index of the item to select
	 * @exception NullPointerException if there is no item in the receiver
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void selectAt(final int index) {
		checkWidget();
		checkNullElement();
		checkRange(index);
		this.selection.add(this.elements.get(index));
		updateSelection();
	}

	/**
	 * Selects an item the receiver's list. If the item was already selected, it
	 * remains selected.
	 * 
	 * @param index the index of the item to select
	 * @exception NullPointerException if there is no item in the receiver
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the selection is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void select(final T value) {
		checkWidget();
		checkNullElement();
		if (!this.elements.contains(value)) {
			throw new IllegalArgumentException("Value not present in the widget");
		}
		this.selection.add(value);
		updateSelection();
	}

	/**
	 * Selects items in the receiver. If the items were already selected, they
	 * remain selected.
	 * 
	 * @param index the indexes of the items to select
	 * @exception NullPointerException if there is no item in the receiver
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the selection is null</li>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelectedIndex(final int[] index) {
		checkWidget();
		checkNullElement();
		for (final int i : index) {
			checkRange(i);
			this.selection.add(this.elements.get(i));
		}
		updateSelection();
	}

	/**
	 * Returns the zero-relative indices of the items which are currently
	 * selected in the receiver. The order of the indices is unspecified. The
	 * array is empty if no items are selected.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its selection, so modifying the array will not affect the receiver.
	 * </p>
	 * 
	 * @return the array of indices of the selected items
	 * @exception NullPointerException if there is no item in the receiver
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int[] getSelectedIndex() {
		checkWidget();
		checkNullElement();
		final List<Integer> selectedIndex = new ArrayList<Integer>();
		for (int i = 0; i < this.elements.size(); i++) {
			if (this.selection.contains(this.elements.get(i))) {
				selectedIndex.add(i);
			}
		}

		final int[] returned = new int[selectedIndex.size()];
		for (int i = 0; i < selectedIndex.size(); i++) {
			returned[i] = selectedIndex.get(i);
		}

		return returned;
	}

	/**
	 * Returns an array of <code>Object</code>s that are currently selected in
	 * the receiver. The order of the items is unspecified. An empty array
	 * indicates that no items are selected.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its selection, so modifying the array will not affect the receiver.
	 * </p>
	 * 
	 * @return an array representing the selection
	 * @exception NullPointerException if there is no item in the receiver
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public List<T> getSelection() {
		checkWidget();
		checkNullElement();
		return new ArrayList<T>(this.selection);
	}

	/**
	 * Deselects the item at the given zero-relative index in the receiver's
	 * list. If the item at the index was already deselected, it remains
	 * deselected. Indices that are out of range are ignored.
	 * 
	 * @param index the index of the item to deselect
	 * @exception NullPointerException if there is no item in the receiver
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void deselectAt(final int index) {
		checkWidget();
		checkNullElement();

		if (index < 0 || index >= this.elements.size()) {
			SWT.error(SWT.ERROR_INVALID_RANGE);
		}

		this.selection.remove(index);
		updateSelection();
	}

	/**
	 * Deselects the item in the receiver's list. If the item at the index was
	 * already deselected, it remains deselected.
	 * 
	 * @param value the item to deselect
	 * @exception NullPointerException if there is no item in the receiver
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void deselect(final T value) {
		checkWidget();
		checkNullElement();
		this.selection.remove(value);
		updateSelection();
	}

	/**
	 * Deselects all items in the receiver's list.
	 * 
	 * @param value the item to deselect
	 * @exception NullPointerException if there is no item in the receiver
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void deselectAll() {
		checkWidget();
		checkNullElement();
		this.selection.clear();
		updateSelection();
	}

	/**
	 * @return the number of columns
	 */
	public int getNumberOfColumns() {
		checkWidget();
		return this.numberOfColumns;
	}

	/**
	 * @param numberOfColumns the number of columns
	 */
	public void setNumberOfColumns(final int numberOfColumns) {
		checkWidget();
		this.numberOfColumns = numberOfColumns;
		this.popup.dispose();
		this.popup = null;
		createPopup();
	}

	/**
	 * @return the separator used in the text field. Default value is ","
	 */
	public String getSeparator() {
		return this.separator;
	}

	/**
	 * @param separator the new value of the separator
	 */
	public void setSeparator(final String separator) {
		this.separator = separator;
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#getForeground()
	 */
	@Override
	public Color getForeground() {
		return this.foreground;
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setForeground(org.eclipse.swt.graphics.Color)
	 */
	@Override
	public void setForeground(final Color foreground) {
		this.foreground = foreground;
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#getBackground()
	 */
	@Override
	public Color getBackground() {
		return this.background;
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setBackground(org.eclipse.swt.graphics.Color)
	 */
	@Override
	public void setBackground(final Color background) {
		this.background = background;
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#getFont()
	 */
	@Override
	public Font getFont() {
		return this.font;
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setFont(org.eclipse.swt.graphics.Font)
	 */
	@Override
	public void setFont(final Font font) {
		this.font = font;
	}

	/**
	 * Refresh the widget (after the add of a new element for example)
	 */
	public void refresh() {
		checkWidget();
		this.popup.dispose();
		this.popup = null;
		createPopup();
		updateSelection();
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
	 */
	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		checkWidget();
		int width = 0, height = 0;

		final GC gc = new GC(this.text);
		final int spacer = gc.stringExtent(" ").x;
		final int textWidth = gc.stringExtent(this.text.getText()).x;
		gc.dispose();
		final Point textSize = this.text.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		final Point arrowSize = this.arrow.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		final int borderWidth = getBorderWidth();

		height = Math.max(textSize.y, arrowSize.y);
		width = textWidth + 2 * spacer + arrowSize.x + 2 * borderWidth;
		if (wHint != SWT.DEFAULT) {
			width = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			height = hHint;
		}
		return new Point(width + 2 * borderWidth, height + 2 * borderWidth);
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		checkWidget();
		this.arrow.setEnabled(enabled);
		this.text.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setToolTipText(java.lang.String)
	 */
	@Override
	public void setToolTipText(final String txt) {
		checkWidget();
		this.text.setToolTipText(txt);
	}

	/**
	 * @return the selection listener
	 */
	public SelectionListener getSelectionListener() {
		checkWidget();
		return this.selectionListener;
	}

	/**
	 * @param selectionListener the new selection listener
	 */
	public void setSelectionListener(final MultiChoiceSelectionListener<T> selectionListener) {
		checkWidget();
		this.selectionListener = selectionListener;
		refresh();
	}

	/**
	 * Update the selection
	 */
	public void updateSelection() {
		checkWidget();
		if (isDisposed()) {
			return;
		}

		if (this.popup == null || this.popup.isDisposed()) {
			return;
		}

		for (int i = 0; i < this.checkboxes.size(); i++) {
			final Button currentButton = this.checkboxes.get(i);
			if (!currentButton.isDisposed()) {
				final Object content = currentButton.getData();
				currentButton.setSelection(this.selection.contains(content));
			}
		}
		setLabel();

	}

	/**
	 * @return the last modified item
	 */
	T getLastModified() {
		return this.lastModified;
	}

	/**
	 * @return the popup
	 */
	Shell getPopup() {
		return this.popup;
	}

	/**
	 * Create the popup that contains all checkboxes
	 */
	private void createPopup() {
		this.popup = new Shell(getShell(), SWT.NO_TRIM | SWT.ON_TOP);
		this.popup.setLayout(new GridLayout(this.numberOfColumns, true));

		final int[] popupEvents = { SWT.Close, SWT.Paint, SWT.Deactivate, SWT.Dispose };
		for (int i = 0; i < popupEvents.length; i++) {
			this.popup.addListener(popupEvents[i], this.listener);
		}

		if (this.elements == null) {
			return;
		}

		this.checkboxes = new ArrayList<Button>(this.elements.size());
		for (final T o : this.elements) {
			final Button checkBoxButton = new Button(this.popup, SWT.CHECK);

			if (this.font != null) {
				checkBoxButton.setFont(this.font);
			}
			if (this.foreground != null) {
				checkBoxButton.setForeground(this.foreground);
			}
			if (this.background != null) {
				checkBoxButton.setBackground(this.background);
			}

			checkBoxButton.setData(o);
			checkBoxButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
			checkBoxButton.setText(o.toString());
			checkBoxButton.addSelectionListener(new SimpleSelectionAdapter() {
				@Override
				public void handle(final SelectionEvent e) {
					if (checkBoxButton.getSelection()) {
						MultiChoice.this.selection.add(o);
					} else {
						MultiChoice.this.selection.remove(o);
					}
					MultiChoice.this.lastModified = o;
					setLabel();
				}
			});

			if (this.selectionListener != null) {
				checkBoxButton.addSelectionListener(this.selectionListener);
			}

			checkBoxButton.setSelection(this.selection.contains(o));
			this.checkboxes.add(checkBoxButton);
		}
		this.popup.layout();
	}

	/**
	 * Set the value of the label, based on the selected items
	 */
	private void setLabel() {
		if (this.checkboxes == null) {
			this.text.setText("");
			return;
		}

		final List<String> values = new ArrayList<String>();
		for (final Button current : this.checkboxes) {
			if (current.getSelection()) {
				values.add(current.getText());
			}
		}

		final StringBuffer sb = new StringBuffer();
		final Iterator<String> it = values.iterator();
		while (it.hasNext()) {
			sb.append(it.next());
			if (it.hasNext()) {
				sb.append(this.separator);
			}
		}

		this.text.setText(sb.toString());
	}

	/**
	 * Handle a focus event
	 * 
	 * @param type type of the event to handle
	 */
	private void handleFocus(final int type) {
		if (isDisposed()) {
			return;
		}
		switch (type) {
		case SWT.FocusIn: {
			if (this.hasFocus) {
				return;
			}
			this.hasFocus = true;
			final Shell shell = getShell();
			shell.removeListener(SWT.Deactivate, this.listener);
			shell.addListener(SWT.Deactivate, this.listener);
			final Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, this.filter);
			display.addFilter(SWT.FocusIn, this.filter);
			final Event e = new Event();
			notifyListeners(SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			if (!this.hasFocus) {
				return;
			}
			final Control focusControl = getDisplay().getFocusControl();
			if (focusControl == this.arrow) {
				return;
			}
			this.hasFocus = false;
			final Shell shell = getShell();
			shell.removeListener(SWT.Deactivate, this.listener);
			final Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, this.filter);
			final Event e = new Event();
			notifyListeners(SWT.FocusOut, e);
			break;
		}
		}
	}

	/**
	 * Handle a multichoice event
	 * 
	 * @param event event to handle
	 */
	private void multiChoiceEvent(final Event event) {
		switch (event.type) {
		case SWT.Dispose:
			if (this.popup != null && !this.popup.isDisposed()) {
				this.popup.dispose();
			}
			final Shell shell = getShell();
			shell.removeListener(SWT.Deactivate, this.listener);
			final Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, this.filter);
			this.popup = null;
			this.arrow = null;
			break;
		case SWT.Move:
			dropDown(false);
			break;
		case SWT.Resize:
			if (isDropped()) {
				dropDown(false);
			}
			break;
		}

	}

	/**
	 * Handle a button event
	 * 
	 * @param event event to hangle
	 */
	private void buttonEvent(final Event event) {
		switch (event.type) {
		case SWT.FocusIn: {
			handleFocus(SWT.FocusIn);
			break;
		}
		case SWT.Selection: {
			dropDown(!isDropped());
			break;
		}
		}
	}

	/**
	 * @return <code>true</code> if the popup is visible and not dropped,
	 *         <code>false</code> otherwise
	 */
	private boolean isDropped() {
		return !this.popup.isDisposed() && this.popup.getVisible();
	}

	/**
	 * Handle a popup event
	 * 
	 * @param event event to handle
	 */
	private void popupEvent(final Event event) {
		switch (event.type) {
		case SWT.Paint:
			final Rectangle listRect = this.popup.getBounds();
			final Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
			event.gc.setForeground(black);
			event.gc.drawRectangle(0, 0, listRect.width - 1, listRect.height - 1);
			break;
		case SWT.Close:
			event.doit = false;
			dropDown(false);
			break;
		case SWT.Deactivate:
			dropDown(false);
			break;
		case SWT.Dispose:
			if (this.checkboxes != null) {
				this.checkboxes.clear();
			}
			this.checkboxes = null;
			break;
		}

	}

	/**
	 * Display/Hide the popup window
	 * 
	 * @param drop if <code>true</code>, displays the popup window. If
	 *            <code>false</code>, hide the popup window
	 */
	private void dropDown(final boolean drop) {
		if (drop == isDropped()) {
			return;
		}

		if (!drop) {
			this.popup.setVisible(false);
			if (!isDisposed()) {
				this.text.setFocus();
			}
			return;
		}

		if (getShell() != this.popup.getParent()) {
			this.popup.dispose();
			this.popup = null;
			createPopup();
		}

		final Point arrowRect = this.arrow.toDisplay(this.arrow.getSize().x - 5, this.arrow.getSize().y + this.arrow.getBorderWidth() - 3);
		int x = arrowRect.x;
		int y = arrowRect.y;

		final Rectangle displayRect = getMonitor().getClientArea();
		final Rectangle parentRect = getDisplay().map(getParent(), null, getBounds());
		this.popup.pack();
		final int width = this.popup.getBounds().width;
		final int height = this.popup.getBounds().height;

		if (y + height > displayRect.y + displayRect.height) {
			y = parentRect.y - height;
		}
		if (x + width > displayRect.x + displayRect.width) {
			x = displayRect.x + displayRect.width - width;
		}

		this.popup.setLocation(x, y);
		this.popup.setVisible(true);
		this.popup.setFocus();
	}

	/**
	 * Check if the elements attributes is not null
	 * 
	 * @exception NullPointerException if there is no item in the receiver
	 */
	private void checkNullElement() {
		if (this.elements == null) {
			throw new NullPointerException("There is no element associated to this widget");
		}
	}

	/**
	 * @param index
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	private void checkRange(final int index) throws NullPointerException {
		checkNullElement();
		if (index < 0 || index >= this.elements.size()) {
			SWT.error(SWT.ERROR_INVALID_RANGE);
		}
	}

}
