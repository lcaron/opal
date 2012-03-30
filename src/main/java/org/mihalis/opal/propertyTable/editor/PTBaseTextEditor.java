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
package org.mihalis.opal.propertyTable.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.mihalis.opal.propertyTable.PTProperty;
import org.mihalis.opal.propertyTable.PTWidget;
import org.mihalis.opal.utils.StringUtil;

/**
 * This abstract class represents all text-based editors (float editor, integer
 * editor, password editor, String editor, URL editor)
 * 
 */
public abstract class PTBaseTextEditor extends PTEditor {

	protected Text text;

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTEditor#render(org.mihalis.opal.propertyTable.PTWidget,
	 *      org.eclipse.swt.widgets.Item,
	 *      org.mihalis.opal.propertyTable.PTProperty)
	 */
	@Override
	public ControlEditor render(final PTWidget widget, final Item item, final PTProperty property) {

		ControlEditor editor;
		if (widget.getWidget() instanceof Table) {
			editor = new TableEditor((Table) widget.getWidget());
		} else {
			editor = new TreeEditor((Tree) widget.getWidget());
		}

		widget.updateDescriptionPanel(property);

		this.text = new Text(widget.getWidget(), getStyle());

		addVerifyListeners();

		this.text.setText(StringUtil.safeToString(property.getValue()));
		this.text.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				property.setValue(convertValue());
			}
		});

		this.text.addListener(SWT.FocusIn, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				widget.updateDescriptionPanel(property);
			}
		});

		editor.grabHorizontal = true;
		if (widget.getWidget() instanceof Table) {
			((TableEditor) editor).setEditor(this.text, (TableItem) item, 1);
		} else {
			((TreeEditor) editor).setEditor(this.text, (TreeItem) item, 1);
		}

		this.text.setEnabled(property.isEnabled());

		return editor;
	}

	/**
	 * Add the verify listeners
	 */
	public abstract void addVerifyListeners();

	/**
	 * @return the value of the data typed by the user in the correct format
	 */
	public abstract Object convertValue();

	/**
	 * @return the style (SWT.NONE or SWT.PASSWORD)
	 */
	public abstract int getStyle();
}
