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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;

/**
 * 
 * DynamicTableColumn
 * 
 */
public class DynamicTableColumn extends TableColumn {
	
	private boolean	originalResizable = false;

	/**
	 * Constructor
	 * @param parent DynamicTable
	 * @param style int
	 */
	public DynamicTableColumn(final DynamicTable parent, final int style) {
		super(parent, style);
		
		// Config the preferred length of columns.
		addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (getResizable()) {
					final DynamicTableColumnLayout dynamicLayout = getParent().getLayout();
					final DynamicColumnData data = dynamicLayout.getColumnData(DynamicTableColumn.this);
					final DynamicLength preferredfLength = data.getPreferredLength();
					if (preferredfLength.getMeasure() == DynamicLengthMeasure.PIXEL) {
						preferredfLength.setValue((double)getWidth());
					}
					getParent().layout();
				}
			}
		});
	}

	/**
	 * Constructor
	 * @param parent DynamicTable
	 * @param style int
	 * @param index int
	 */
	public DynamicTableColumn(final DynamicTable parent, final int style, final int index) {
		super(parent, style, index);
	}
	
	/**
	 * Set width
	 * @param preferredLength String
	 */
	public void setWidth(final String preferredLength) {
		getParent().getLayout().setColumnData(this, preferredLength);
	}

	/**
	 * Set width
	 * @param preferredLength String
	 * @param minLength String
	 */
	public void setWidth(final String preferredLength, final String minLength) {
		final DynamicColumnData columnData = new DynamicColumnData(this, preferredLength, minLength);
		getParent().getLayout().setColumnData(columnData);
		setResizable(originalResizable);
	}
	
	@Override
	public void setResizable(final boolean resizable) {
		this.originalResizable = resizable;
		final DynamicTableColumnLayout dynamicLayout = getParent().getLayout();
		final DynamicColumnData data = dynamicLayout.getColumnData(DynamicTableColumn.this);
		super.setResizable(originalResizable && data.getPreferredLength().getMeasure() != DynamicLengthMeasure.PERCENTAGE);
	}
	
	@Override
	public DynamicTable getParent() {
		return (DynamicTable)super.getParent();
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components.
	}
	
}