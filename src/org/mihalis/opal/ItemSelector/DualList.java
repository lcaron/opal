package org.mihalis.opal.ItemSelector;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.mihalis.opal.utils.SimpleSelectionAdapter;

/**
 * 
 * 
 */
public class DualList extends Composite {

	private final List<DLItem> items;
	private final List<DLItem> selection;

	private final Table itemsTable;
	private final Table selectionTable;

	public DualList(final Composite parent, final int style) {
		super(parent, style);
		this.items = new ArrayList<DLItem>();
		this.selection = new ArrayList<DLItem>();

		this.setLayout(new GridLayout(4, false));
		this.itemsTable = createPanel();
		this.itemsTable.addMouseListener(new MouseAdapter() {

			/**
			 * @see org.eclipse.swt.events.MouseAdapter#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
			 */
			@Override
			public void mouseDoubleClick(final MouseEvent event) {
				selectItem();
			}

		});
		final Button buttonSelectAll = createButton("double_right.png", true, GridData.END);
		buttonSelectAll.addSelectionListener(new SimpleSelectionAdapter() {

			@Override
			public void handle(final SelectionEvent e) {
				selectAll();
			}
		});

		this.selectionTable = createPanel();
		this.selectionTable.addMouseListener(new MouseAdapter() {

			/**
			 * @see org.eclipse.swt.events.MouseAdapter#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
			 */
			@Override
			public void mouseDoubleClick(final MouseEvent event) {
				deselectItem();
			}

		});

		final Button buttonMoveFirst = createButton("double_up.png", true, GridData.END);
		buttonMoveFirst.addSelectionListener(new SimpleSelectionAdapter() {

			@Override
			public void handle(final SelectionEvent e) {
				moveSelectionToFirstPosition();
			}
		});

		final Button buttonSelect = createButton("arrow_right.png", false, GridData.CENTER);
		buttonSelect.addSelectionListener(new SimpleSelectionAdapter() {

			@Override
			public void handle(final SelectionEvent e) {
				selectItem();
			}
		});

		final Button buttonMoveUp = createButton("arrow_up.png", false, GridData.CENTER);
		buttonMoveUp.addSelectionListener(new SimpleSelectionAdapter() {

			@Override
			public void handle(final SelectionEvent e) {
				moveUpItem();
			}
		});

		final Button buttonDeselect = createButton("arrow_left.png", false, GridData.CENTER);
		buttonDeselect.addSelectionListener(new SimpleSelectionAdapter() {

			@Override
			public void handle(final SelectionEvent e) {
				deselectItem();
			}
		});

		final Button buttonMoveDown = createButton("arrow_down.png", false, GridData.CENTER);
		buttonMoveDown.addSelectionListener(new SimpleSelectionAdapter() {

			@Override
			public void handle(final SelectionEvent e) {
				moveDownItem();
			}
		});

		final Button buttonDeselectAll = createButton("double_left.png", false, GridData.BEGINNING);
		buttonDeselectAll.addSelectionListener(new SimpleSelectionAdapter() {

			@Override
			public void handle(final SelectionEvent e) {
				deselectAll();
			}
		});

		final Button buttonMoveLast = createButton("double_down.png", true, GridData.BEGINNING);
		buttonMoveLast.addSelectionListener(new SimpleSelectionAdapter() {

			@Override
			public void handle(final SelectionEvent e) {
				moveSelectionToLastPosition();
			}
		});

	}

	private Table createPanel() {
		final Table table = new Table(this, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		table.setLinesVisible(false);
		table.setHeaderVisible(false);
		final GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 4);
		gd.widthHint = 200;
		table.setLayoutData(gd);
		return table;
	}

	private Button createButton(final String fileName, final boolean verticalExpand, final int alignment) {
		final Button button = new Button(this, SWT.PUSH);
		final Image image = new Image(getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/" + fileName));
		button.setImage(image);
		button.setLayoutData(new GridData(GridData.CENTER, alignment, false, verticalExpand));
		button.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				image.dispose();
			}
		});
		return button;
	}

	/**
	 * Adds the argument to the end of the receiver's list.
	 * 
	 * @param item the new item
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #add(DLItem,int)
	 */
	public void add(final DLItem item) {
		checkWidget();
		if (item == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		this.items.add(item);

	}

	/**
	 * Adds the argument to the receiver's list at the given zero-relative
	 * index.
	 * <p>
	 * Note: To add an item at the end of the list, use the result of calling
	 * <code>getItemCount()</code> as the index or use <code>add(DLItem)</code>.
	 * </p>
	 * 
	 * @param item the new item
	 * @param index the index for the item
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list (inclusive)</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #add(String)
	 */
	public void add(final DLItem item, final int index) {
		checkWidget();
		if (item == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (index <= 0 || index >= this.items.size()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.items.add(index, item);
	}

	/**
	 * Deselects the item at the given zero-relative index in the receiver. If
	 * the item at the index was already deselected, it remains deselected.
	 * Indices that are out of range are ignored.
	 * 
	 * @param index the index of the item to deselect
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void deselect(final int index) {
		checkWidget();
		if (index <= 0 || index >= this.items.size()) {
			return;
		}
		this.selection.remove(index);
		redrawTables();
	}

	/**
	 * Deselects the items at the given zero-relative indices in the receiver.
	 * If the item at the given zero-relative index in the receiver is selected,
	 * it is deselected. If the item at the index was not selected, it remains
	 * deselected. Indices that are out of range and duplicate indices are
	 * ignored.
	 * 
	 * @param indices the array of indices for the items to deselect
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the set of indices is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void deselect(final int[] indices) {
		checkWidget();
		if (indices == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		final List<DLItem> toBeRemoved = new ArrayList<DLItem>();

		for (final int index : indices) {
			if (index <= 0 || index >= this.items.size()) {
				continue;
			}
			toBeRemoved.add(this.selection.get(index));
		}

		for (final DLItem item : toBeRemoved) {
			this.selection.remove(item);
		}
		toBeRemoved.clear();
		redrawTables();
	}

	/**
	 * Deselects the items at the given zero-relative indices in the receiver.
	 * If the item at the given zero-relative index in the receiver is selected,
	 * it is deselected. If the item at the index was not selected, it remains
	 * deselected. The range of the indices is inclusive. Indices that are out
	 * of range are ignored.
	 * 
	 * @param start the start index of the items to deselect
	 * @param end the end index of the items to deselect
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_RANGE - if start is greater than end</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void deselect(final int start, final int end) {
		checkWidget();
		if (start > end) {
			SWT.error(SWT.ERROR_INVALID_RANGE);
		}
		final List<DLItem> toBeRemoved = new ArrayList<DLItem>();

		for (int index = start; index <= end; index++) {
			if (index <= 0 || index >= this.items.size()) {
				continue;
			}
			toBeRemoved.add(this.selection.get(index));
		}

		for (final DLItem item : toBeRemoved) {
			this.selection.remove(item);
		}
		toBeRemoved.clear();
		redrawTables();
	}

	/**
	 * Deselects all selected items in the receiver.
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
		this.items.addAll(this.selection);
		this.selection.clear();
		redrawTables();
	}

	/**
	 * Returns the item at the given, zero-relative index in the receiver.
	 * Throws an exception if the index is out of range.
	 * 
	 * @param index the index of the item to return
	 * @return the item at the given index
	 * 
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

	public DLItem getItem(final int index) {
		checkWidget();
		if (index <= 0 || index >= this.items.size()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		return this.items.get(index);
	}

	/**
	 * Returns the number of items contained in the receiver.
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
		return this.items.size();
	}

	/**
	 * Returns a (possibly empty) array of <code>DLItem</code>s which are the
	 * items in the receiver.
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
	public DLItem[] getItems() {
		checkWidget();
		return this.items.toArray(new DLItem[this.items.size()]);
	}

	/**
	 * Returns a (possibly empty) list of <code>DLItem</code>s which are the
	 * items in the receiver.
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
	public List<DLItem> getItemsAsList() {
		checkWidget();
		return new ArrayList<DLItem>(this.items);
	}

	/**
	 * Returns an array of <code>DLItem</code>s that are currently selected in
	 * the receiver. An empty array indicates that no items are selected.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its selection, so modifying the array will not affect the receiver.
	 * </p>
	 * 
	 * @return an array representing the selection
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public DLItem[] getSelection() {
		checkWidget();
		return this.selection.toArray(new DLItem[this.items.size()]);
	}

	/**
	 * Returns a list of <code>DLItem</code>s that are currently selected in the
	 * receiver. An empty array indicates that no items are selected.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain
	 * its selection, so modifying the array will not affect the receiver.
	 * </p>
	 * 
	 * @return an array representing the selection
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public List<DLItem> getSelectionAsList() {
		checkWidget();
		return new ArrayList<DLItem>(this.selection);
	}

	/**
	 * Returns the number of selected items contained in the receiver.
	 * 
	 * @return the number of selected items
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getSelectionCount() {
		checkWidget();
		return this.selection.size();
	}

	/**
	 * Removes the item from the receiver at the given zero-relative index.
	 * 
	 * @param index the index for the item
	 * 
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
	public void remove(final int index) {
		checkWidget();
		if (index <= 0 || index >= this.items.size()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.items.remove(index);
		redrawTables();
	}

	/**
	 * Removes the items from the receiver at the given zero-relative indices.
	 * 
	 * @param indices the array of indices of the items
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void remove(final int[] indices) {
		checkWidget();
		for (final int index : indices) {
			if (index <= 0 || index >= this.items.size()) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			this.items.remove(index);
		}
		redrawTables();
	}

	/**
	 * Removes the items from the receiver which are between the given
	 * zero-relative start and end indices (inclusive).
	 * 
	 * @param start the start of the range
	 * @param end the end of the range
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_RANGE - if either the start or end are
	 *                not between 0 and the number of elements in the list minus
	 *                1 (inclusive) or if start>end</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void remove(final int start, final int end) {
		checkWidget();
		if (start > end) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		for (int index = start; index <= end; index++) {
			if (index <= 0 || index >= this.items.size()) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			this.items.remove(index);
		}
		redrawTables();
	}

	/**
	 * Searches the receiver's list starting at the first item until an item is
	 * found that is equal to the argument, and removes that item from the list.
	 * 
	 * @param item the item to remove
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the item is not found in
	 *                the list</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */

	public void remove(final DLItem item) {
		checkWidget();
		if (item == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (!this.items.contains(item)) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.items.remove(item);
		redrawTables();
	}

	/**
	 * Removes all of the items from the receiver.
	 * <p>
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void removeAll() {
		checkWidget();
		this.items.clear();
		redrawTables();
	}

	/**
	 * Selects the item at the given zero-relative index in the receiver's list.
	 * If the item at the index was already selected, it remains selected.
	 * Indices that are out of range are ignored.
	 * 
	 * @param index the index of the item to select
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void select(final int index) {
		checkWidget();
		if (index <= 0 || index >= this.items.size()) {
			return;
		}
		this.selection.add(this.items.get(index));
		redrawTables();

	}

	/**
	 * Selects the items at the given zero-relative indices in the receiver. The
	 * current selection is not cleared before the new items are selected.
	 * <p>
	 * If the item at a given index is not selected, it is selected. If the item
	 * at a given index was already selected, it remains selected. Indices that
	 * are out of range and duplicate indices are ignored. If the receiver is
	 * single-select and multiple indices are specified, then all indices are
	 * ignored.
	 * 
	 * @param indices the array of indices for the items to select
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the array of indices is null
	 *                </li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void select(final int[] indices) {
		checkWidget();
		if (indices == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		for (final int index : indices) {
			if (index <= 0 || index >= this.items.size()) {
				continue;
			}
			this.selection.add(this.items.get(index));
		}
		redrawTables();
	}

	/**
	 * Selects the items in the range specified by the given zero-relative
	 * indices in the receiver. The range of indices is inclusive. The current
	 * selection is not cleared before the new items are selected.
	 * <p>
	 * If an item in the given range is not selected, it is selected. If an item
	 * in the given range was already selected, it remains selected. Indices
	 * that are out of range are ignored and no items will be selected if start
	 * is greater than end. If the receiver is single-select and there is more
	 * than one item in the given range, then all indices are ignored.
	 * 
	 * @param start the start of the range
	 * @param end the end of the range
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see List#setSelection(int,int)
	 */
	public void select(final int start, final int end) {
		checkWidget();
		if (start > end) {
			SWT.error(SWT.ERROR_INVALID_RANGE);
		}
		for (int index = start; index <= end; index++) {
			if (index <= 0 || index >= this.items.size()) {
				continue;
			}
			this.selection.add(this.items.get(index));
		}
		redrawTables();
	}

	/**
	 * Selects all of the items in the receiver.
	 * <p>
	 * If the receiver is single-select, do nothing.
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void selectAll() {
		checkWidget();
		this.selection.addAll(this.items);
		this.items.clear();
		redrawTables();
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setBounds(int, int, int, int)
	 */
	@Override
	public void setBounds(final int x, final int y, final int width, final int height) {
		super.setBounds(x, y, width, height);
		final boolean itemsContainImage = itemsContainImage();
		final Point itemsTableDefaultSize = this.itemsTable.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		final Point selectionTableDefaultSize = this.selectionTable.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		int itemsTableSize = this.itemsTable.getSize().x;
		if (itemsTableDefaultSize.y > this.itemsTable.getSize().y) {
			itemsTableSize -= this.itemsTable.getVerticalBar().getSize().x;
		}

		int selectionTableSize = this.selectionTable.getSize().x;
		if (selectionTableDefaultSize.y > this.selectionTable.getSize().y) {
			selectionTableSize -= this.selectionTable.getVerticalBar().getSize().x;
		}

		if (itemsContainImage) {
			this.itemsTable.getColumn(0).pack();
			this.itemsTable.getColumn(1).setWidth(itemsTableSize - this.itemsTable.getColumn(0).getWidth());

			this.selectionTable.getColumn(0).pack();
			this.selectionTable.getColumn(1).setWidth(selectionTableSize - this.selectionTable.getColumn(0).getWidth());

		} else {
			this.itemsTable.getColumn(0).setWidth(itemsTableSize);
			this.selectionTable.getColumn(0).setWidth(selectionTableSize);
		}
	}

	/**
	 * Sets the item in the receiver's list at the given zero-relative index to
	 * the item argument.
	 * 
	 * @param index the index for the item
	 * @param item the new item
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setItem(final int index, final DLItem item) {
		checkWidget();
		if (item == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (index <= 0 || index >= this.items.size()) {
			SWT.error(SWT.ERROR_INVALID_RANGE);
		}
		this.items.set(index, item);
		redrawTables();
	}

	/**
	 * Sets the receiver's items to be the given array of items.
	 * 
	 * @param items the array of items
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if an item in the items array
	 *                is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */

	public void setItems(final DLItem[] items) {
		checkWidget();
		if (items == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		final List<DLItem> temp = new ArrayList<DLItem>();
		for (final DLItem item : items) {
			if (item == null) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			temp.add(item);
		}
		this.items.clear();
		this.items.addAll(temp);
		redrawTables();
	}

	/**
	 * Sets the receiver's items to be the given list of items.
	 * 
	 * @param items the list of items
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the items list is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if an item in the items list
	 *                is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setItems(final List<DLItem> items) {
		checkWidget();
		checkWidget();
		if (items == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		final List<DLItem> temp = new ArrayList<DLItem>();
		for (final DLItem item : items) {
			if (item == null) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			temp.add(item);
		}
		this.items.clear();
		this.items.addAll(temp);
		redrawTables();
	}

	public void redrawTables() {
		setRedraw(false);
		redrawTable(this.itemsTable, false);
		redrawTable(this.selectionTable, true);
		setRedraw(true);
		this.setBounds(this.getBounds());
	}

	private void redrawTable(final Table table, final boolean isSelected) {
		clean(table);
		final boolean itemsContainImage = itemsContainImage();
		if (itemsContainImage) {
			new TableColumn(table, SWT.CENTER);
		}
		new TableColumn(table, SWT.LEFT);
		fillData(table, isSelected ? this.selection : this.items, itemsContainImage);
	}

	private void clean(final Table table) {
		if (table == null) {
			return;
		}

		for (final TableItem item : table.getItems()) {
			item.dispose();
		}

		for (final TableColumn tc : table.getColumns()) {
			tc.dispose();
		}

	}

	private boolean itemsContainImage() {
		for (final DLItem item : this.items) {
			if (item.getImage() != null) {
				return true;
			}
		}
		return false;
	}

	private void fillData(final Table table, final List<DLItem> list, final boolean hasImage) {
		for (final DLItem item : list) {
			final TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setData(item);
			if (hasImage) {
				if (item.getImage() != null) {
					tableItem.setImage(0, item.getImage());
				}
			}
			tableItem.setText(1, item.getText());
		}

	}

	private void moveSelectionToFirstPosition() {
		if (this.selectionTable.getSelectionCount() == 0) {
			return;
		}

	}

	private void selectItem() {
		if (this.itemsTable.getSelectionCount() == 0) {
			return;
		}
		for (final TableItem tableItem : this.itemsTable.getSelection()) {
			final DLItem item = (DLItem) tableItem.getData();
			this.selection.add(item);
			this.items.remove(item);
		}
		redrawTables();
	}

	private void moveUpItem() {
		if (this.selectionTable.getSelectionCount() == 0) {
			return;
		}

	}

	private void deselectItem() {
		if (this.selectionTable.getSelectionCount() == 0) {
			return;
		}
		for (final TableItem tableItem : this.selectionTable.getSelection()) {
			final DLItem item = (DLItem) tableItem.getData();
			this.items.add(item);
			this.selection.remove(item);
		}
		redrawTables();
	}

	private void moveDownItem() {
		if (this.selectionTable.getSelectionCount() == 0) {
			return;
		}

	}

	private void moveSelectionToLastPosition() {
		if (this.selectionTable.getSelectionCount() == 0) {
			return;
		}

	}
}
