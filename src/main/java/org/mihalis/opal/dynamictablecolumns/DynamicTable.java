/*******************************************************************************
 * Copyright (c) 2011 Luis Carlos Moreira da Costa.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luis Carlos Moreira da Costa (tcljava at gmail dot com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.dynamictablecolumns;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * 
 * DynamicTable
 * 
 */
public class DynamicTable extends Table {
	
	private Composite parent = null;
	private Composite container = null;
	
	private Menu headerMenu = null;
	private DynamicTableColumnLayout dynamicLayout = null;
	
	/**
	 * Constructor
	 * @param parent Composite
	 * @param style int
	 */
	public DynamicTable(final Composite parent, final int style) {
		super(new Composite(parent, SWT.NONE) {
			@Override
			public void reskin(final int flags) {
				super.reskin(flags);
			}
		}, style);
		
		this.parent  = parent;
		this.container = super.getParent();
		
		dynamicLayout = new DynamicTableColumnLayout() {
			@Override
			public void setColumnData(final DynamicColumnData column) {
				super.setColumnData(column);
				createMenuItem(headerMenu, column);
			}
		};
		
		container.setLayout(dynamicLayout);
		headerMenu = new Menu(container.getShell(), SWT.POP_UP);
		addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(final Event event) {
				setMenu(((isMouseOverHeader(event.x, event.y)) ? headerMenu : null));
			}
		});
		
		addListener(SWT.Dispose, new Listener() {
			public void handleEvent(final Event event) {
				headerMenu.dispose();
			}
		});
	}
	
	/**
	 * Verify is mouse over header
	 * @param x int
	 * @param y int
	 * @return boolean
	 */
	protected boolean isMouseOverHeader(final int x, final int y) {
		final Point pt = Display.getDefault().map(null, DynamicTable.this, new Point(x, y));
		final Rectangle clientArea = getClientArea();
		return (clientArea.y <= pt.y) && (pt.y < (clientArea.y+getHeaderHeight()));
	}

	/**
	 * Create menu item
	 * @param menu Menu
	 * @param dynamicColumnData DynamicColumnData
	 */
	private void createMenuItem(final Menu menu, final DynamicColumnData dynamicColumnData) {
		final TableColumn tableColumn = dynamicColumnData.getTableColumn();
		final MenuItem itemName = new MenuItem(menu, SWT.CHECK);
		itemName.setText(tableColumn.getText());
		itemName.setSelection(tableColumn.getResizable());
		itemName.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event event) {
				final boolean checked = itemName.getSelection();
				dynamicColumnData.setVisible(checked);
				tableColumn.setResizable(checked);
				layout();
			}
		});
	}
	
	@Override
	public DynamicTableColumnLayout getLayout() {
		return (DynamicTableColumnLayout)container.getLayout();
	}
	
	@Override
	public void setLayout(final Layout layout) {
		throw new IllegalStateException();
	}
	
	@Override
	public void setLayoutData(final Object layoutData) {
		container.setLayoutData(layoutData);
	}
	
	@Override
	public void layout() {
		container.layout();
	}
	
	@Override
	public Composite getParent() {
		return this.parent;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components.	
	}
	
}