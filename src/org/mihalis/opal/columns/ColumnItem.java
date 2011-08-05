/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - Initial implementation and API
 *******************************************************************************/
package org.mihalis.opal.columns;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.mihalis.opal.OpalItem;

/**
 * Instances of this object are items manipulated by the ColumnBrowser widget.
 * ColumnItems are part of a tree structure .
 * 
 * @see OpalItem
 */
public class ColumnItem extends OpalItem {

	private final ColumnBrowserWidget widget;
	private final ColumnItem parent;
	private final List<ColumnItem> children;

	/**
	 * Constructs a new instance of this class given its parent. The item is
	 * added to the end of the items maintained by its parent.
	 * 
	 * @param widget the widget that will contain this item (can not be null)
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public ColumnItem(final ColumnBrowserWidget widget) {
		if (widget == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (widget.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}

		this.widget = widget;
		this.parent = null;
		this.children = new ArrayList<ColumnItem>();

		if (widget.getRootItem() != null) {
			widget.getRootItem().children.add(this);
		}
		widget.updateContent();
	}

	/**
	 * Constructs a new instance of this class given its parent. The item is
	 * added at a given position in the items'list maintained by its parent.
	 * 
	 * @param widget the widget that will contain this item (can not be null)
	 * @param index the position
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public ColumnItem(final ColumnBrowserWidget widget, final int index) {

		if (widget == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (widget.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}

		this.widget = widget;
		this.parent = null;
		this.children = new ArrayList<ColumnItem>();
		widget.getRootItem().children.add(index, this);
		widget.updateContent();
	}

	/**
	 * Constructs a new instance of this class given its parent. The item is
	 * added to the end of the items maintained by its parent.
	 * 
	 * @param widget the widget that will contain this item (can not be null)
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public ColumnItem(final ColumnItem parent) {

		if (parent == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (parent.widget.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}

		this.widget = parent.widget;
		this.parent = parent;
		this.children = new ArrayList<ColumnItem>();
		parent.children.add(this);
		parent.widget.updateContent();
	}

	/**
	 * Constructs a new instance of this class given its parent. The item is
	 * added at a given position in the items'list maintained by its parent.
	 * 
	 * @param widget the widget that will contain this item (can not be null)
	 * @param index the position
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public ColumnItem(final ColumnItem parent, final int index) {
		if (parent == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (parent.widget.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}

		this.widget = parent.widget;
		this.parent = parent;
		this.children = new ArrayList<ColumnItem>();
		parent.children.add(index, this);
		parent.widget.updateContent();
	}

	/**
	 * Remove a given children of this object
	 * 
	 * @param item the item to remove (can not be null)
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void remove(final ColumnItem item) {
		if (this.widget == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (this.widget.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}
		this.children.remove(item);
		this.widget.updateContent();
	}

	/**
	 * Remove a children in a given position of this object
	 * 
	 * @param index position of the children in the items'list
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void remove(final int index) {
		if (this.widget == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (this.widget.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}
		this.children.remove(index);
		this.widget.updateContent();
	}

	/**
	 * Remove all children of this object
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void removeAll() {
		if (this.widget == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (this.widget.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}
		this.children.clear();
		this.widget.updateContent();
	}

	/**
	 * Returns an item located at a given position
	 * 
	 * @param index position
	 * @return the item located at the index position
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public ColumnItem getItem(final int index) {
		if (this.widget == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (this.widget.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}
		return this.children.get(index);
	}

	/**
	 * @return the number of children
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public int getItemCount() {
		if (this.widget == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (this.widget.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}
		return this.children.size();
	}

	/**
	 * @return all children of this item
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public ColumnItem[] getItems() {
		if (this.widget == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (this.widget.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}
		return this.children.toArray(new ColumnItem[this.children.size()]);
	}

	/**
	 * @return the widget that holds this item
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public ColumnBrowserWidget getParent() {
		if (this.widget == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (this.widget.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}
		return this.widget;
	}

	/**
	 * @return the parent item, of <code>null</code> if this item is the root
	 *         node
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public ColumnItem getParentItem() {
		if (this.widget == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (this.widget.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}
		return this.parent;
	}

	/**
	 * Return the position of a given item in children's list
	 * 
	 * @param item item to find
	 * @return the position of the children, or -1 if <code>item</code> is a not
	 *         a children of this object
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public int indexOf(final ColumnItem item) {
		return this.children.indexOf(item);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.parent == null ? 0 : this.parent.hashCode());
		result = prime * result + (this.widget == null ? 0 : this.widget.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ColumnItem other = (ColumnItem) obj;
		if (this.children == null) {
			if (other.children != null) {
				return false;
			}
		} else if (!this.children.equals(other.children)) {
			return false;
		}
		if (this.parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!this.parent.equals(other.parent)) {
			return false;
		}
		if (this.widget == null) {
			if (other.widget != null) {
				return false;
			}
		} else if (!this.widget.equals(other.widget)) {
			return false;
		}
		return true;
	}

}
