/*******************************************************************************
 * Copyright (c) 2012 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation 
 *******************************************************************************/
package org.mihalis.opal.propertyTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.mihalis.opal.propertyTable.editor.PTStringEditor;
import org.mihalis.opal.utils.ResourceManager;
import org.mihalis.opal.utils.StringUtil;

/**
 * Instances of this class are table that are displayed in a PropertyTable when
 * the type of view is "Category"
 */
public class PTWidgetTree extends AbstractPTWidget {

	private Tree tree;

	/**
	 * @see org.mihalis.opal.propertyTable.AbstractPTWidget#buildWidget(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void buildWidget(final Composite parent) {
		this.tree = new Tree(parent, SWT.FULL_SELECTION);
		this.tree.setLinesVisible(true);
		this.tree.setHeaderVisible(true);
		this.tree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 3, 1));

		final TreeColumn propertyColumn = new TreeColumn(this.tree, SWT.NONE);
		propertyColumn.setText(ResourceManager.getLabel(ResourceManager.PROPERTY));

		final TreeColumn valueColumn = new TreeColumn(this.tree, SWT.NONE);
		valueColumn.setText(ResourceManager.getLabel(ResourceManager.VALUE));

		fillData();
		this.tree.addControlListener(new ControlAdapter() {

			/**
			 * @see org.eclipse.swt.events.ControlAdapter#controlResized(org.eclipse.swt.events.ControlEvent)
			 */
			@Override
			public void controlResized(final ControlEvent e) {
				final Rectangle area = PTWidgetTree.this.tree.getParent().getClientArea();
				final Point size = PTWidgetTree.this.tree.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				final ScrollBar vBar = PTWidgetTree.this.tree.getVerticalBar();
				int width = area.width - PTWidgetTree.this.tree.computeTrim(0, 0, 0, 0).width - vBar.getSize().x;
				if (size.y > area.height + PTWidgetTree.this.tree.getHeaderHeight()) {
					// Subtract the scrollbar width from the total column width
					// if a vertical scrollbar will be required
					final Point vBarSize = vBar.getSize();
					width -= vBarSize.x;
				}
				propertyColumn.pack();
				valueColumn.setWidth(width - propertyColumn.getWidth());
				PTWidgetTree.this.tree.removeControlListener(this);
			}

		});

		this.tree.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (PTWidgetTree.this.tree.getSelectionCount() == 0 || PTWidgetTree.this.tree.getSelection()[0] == null) {
					return;
				}
				updateDescriptionPanel(PTWidgetTree.this.tree.getSelection()[0].getData());
			}

		});

	}

	/**
	 * Fill the data in the widget
	 */
	private void fillData() {
		final Map<String, List<PTProperty>> data;

		if (getParentPropertyTable().sorted) {
			data = new TreeMap<String, List<PTProperty>>();
		} else {
			data = new HashMap<String, List<PTProperty>>();
		}

		for (final PTProperty p : getParentPropertyTable().getPropertiesAsList()) {
			final String category = StringUtil.safeToString(p.getCategory());
			if (!data.containsKey(category)) {
				data.put(category, new ArrayList<PTProperty>());
			}
			data.get(category).add(p);
		}

		for (final String key : data.keySet()) {

			if (data.get(key) == null || data.get(key).isEmpty()) {
				continue;
			}

			final TreeItem root = new TreeItem(this.tree, SWT.NONE);
			root.setText(0, key);
			root.setBackground(root.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			root.setForeground(root.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			root.setExpanded(true);

			for (final PTProperty p : data.get(key)) {
				final TreeItem item = new TreeItem(root, SWT.NONE);
				item.setData(p);
				item.setText(0, StringUtil.safeToString(p.getName()));
				if (p.getEditor() == null) {
					p.setEditor(new PTStringEditor());
				}

				final ControlEditor editor = p.getEditor().render(this, item, p);
				item.addDisposeListener(new DisposeListener() {

					@Override
					public void widgetDisposed(final DisposeEvent e) {
						if (editor.getEditor() != null) {
							editor.getEditor().dispose();
						}
						editor.dispose();

					}
				});

				if (!p.isEnabled()) {
					item.setForeground(item.getDisplay().getSystemColor(SWT.COLOR_GRAY));
				}
				item.setExpanded(true);
			}
			root.setExpanded(true);

		}

	}

	/**
	 * @see org.mihalis.opal.propertyTable.AbstractPTWidget#refillData()
	 */
	@Override
	public void refillData() {
		for (final TreeItem treeItem : this.tree.getItems()) {
			treeItem.dispose();
		}

		fillData();
	}

	/**
	 * @see org.mihalis.opal.propertyTable.PTWidget#getWidget()
	 */
	@Override
	public Composite getWidget() {
		return this.tree;
	}

}
