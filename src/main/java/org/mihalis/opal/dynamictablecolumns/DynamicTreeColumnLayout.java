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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * 
 * DynamicTreeColumnLayout
 * 
 * <p>Manages the length of a set of columns.</p>
 * 
 */
public class DynamicTreeColumnLayout extends Layout {
	
	private List<DynamicColumnData> mcolumns = new LinkedList<DynamicColumnData>();
	private Map<Composite, Listener> installedListeners = new HashMap<Composite, Listener>();
	private int totalFixedWidth = 0;
	private double totalPerctWidth = 0.0d;
	private int percentageMargin = 18;
	private boolean	relayout = true;
	
	/**
	 * Set column data
	 * @param dynamicColumnData DynamicColumnData
	 */
	public void setColumnData(final DynamicColumnData dynamicColumnData) {
		mcolumns.add(dynamicColumnData);
	}
	
	/**
	 * Set column data
	 * @param tableColumn TableColumn
	 * @param preferredLength String
	 * @param minLength String
	 */
	public void setColumnData(final TableColumn tableColumn, final String preferredLength, final String minLength) {
		mcolumns.add(new DynamicColumnData(tableColumn, preferredLength, minLength));
	}

	/**
	 * Set column data
	 * @param tableColumn TableColumn
	 * @param preferredLength String
	 */
	public void setColumnData(final TableColumn tableColumn, final String preferredLength) {
		mcolumns.add(new DynamicColumnData(tableColumn, preferredLength));
	}
	
	@Override
	protected Point computeSize(final Composite composite, final int wHint, final int hHint, final boolean flushCache) {
		if (installedListeners.get(composite) == null) {
			final Listener listener = new Listener() {
				@Override
				public void handleEvent(final Event event) {
					event.doit = false;
					calculate(composite.getBounds().width);
				}
			};
			composite.addListener(SWT.Resize, listener);
			installedListeners.put(composite, listener);
		}
		final Point result = ((Scrollable)composite.getChildren()[0]).computeSize(wHint, hHint);
		calculate(result.x);
		return result;
	}
	
	/**
	 * Calculate
	 * @param containerWidth int
	 */
	public void calculate(final int containerWidth)	{
		totalFixedWidth = 0;
		totalPerctWidth = 0;
		
		// Calculate Totals
		for (final DynamicColumnData column : mcolumns) {
			final DynamicLength preferredLength = column.getPreferredLength();
			
			switch (preferredLength.getMeasure()) {
				case PIXEL:
					totalFixedWidth += preferredLength.getValue();
					break;
				case PERCENTAGE:
					totalPerctWidth += preferredLength.getValue();
			}
		}
		
		final Integer precentPixelWidth = (containerWidth-totalFixedWidth-percentageMargin);
		
		// Set widths
		for (final DynamicColumnData mcolumn : mcolumns) {
			final DynamicLength prefLength = mcolumn.getPreferredLength();
			final Double prefLengthValue = prefLength.getValue();
			final DynamicLengthMeasure prefLengthMeasure = prefLength.getMeasure();
			final DynamicLength minLength = mcolumn.getMinLength();
			final Double minLengthValue = minLength.getValue();
			
			switch (prefLengthMeasure) {
				case PIXEL:
					updateColumnWidth(mcolumn, Math.max(prefLengthValue, minLengthValue));
					break;
				case PERCENTAGE:
					final double percentFromTotal = (prefLengthValue/totalPerctWidth);
					final int width = (int)(percentFromTotal*precentPixelWidth);
					updateColumnWidth(mcolumn, Math.max(width, minLengthValue));
			}
		}
	}

	/**
	 * Update column width
	 * @param dynamicColumnData DynamicColumnData
	 * @param width Double
	 */
	private void updateColumnWidth(final DynamicColumnData dynamicColumnData, final Double width)	{
		final TableColumn tableColumn = dynamicColumnData.getTableColumn();
		final TreeColumn treeColumn = dynamicColumnData.getTreeColumn();
		
		if (tableColumn != null) {
			tableColumn.setWidth(width.intValue());
		} else if (treeColumn != null) {
			treeColumn.setWidth(width.intValue());
		} else {
			throw new IllegalStateException("No valid to set the column width!");
		}
	}

	/**
	 * Get managed columns
	 * @return List<DynamicColumnData>
	 */
	public List<DynamicColumnData> getManagedColumns()	{
		return mcolumns;
	}

	@Override
	protected void layout(final Composite composite, final boolean flushCache) {
		final Rectangle rect = composite.getClientArea();
		final Control[] children = composite.getChildren();
		final int count = children.length;
		
		if (count == 0) {
			return;
		}
		
		final int width = rect.width;
		final int height = rect.height;
		final int x = rect.x;
		final int extra = (width%count);
		final int y = rect.y;
		final int cellWidth = (width/count);
		final Control child = children[0];
		int childWidth = cellWidth;
		childWidth += (extra/2);
		child.setBounds(x, y, childWidth, height);
		
		// For the first time we need to relayout because Scrollbars are not calculate appropriately.
		if (relayout) {
			relayout = false;
			composite.layout();
		}
	}
	
}