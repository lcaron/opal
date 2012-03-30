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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.mihalis.opal.propertyTable.PTProperty;
import org.mihalis.opal.propertyTable.PTWidget;

/**
 * This editor is a check box for boolean values
 * 
 */
public class PTCheckboxEditor extends PTEditor {

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

		final Button button = new Button(widget.getWidget(), SWT.CHECK);

		if (property.getValue() == null) {
			button.setSelection(false);
		} else {
			button.setSelection(((Boolean) property.getValue()).booleanValue());
		}

		button.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				property.setValue(new Boolean(button.getSelection()));
			}
		});

		button.addListener(SWT.FocusIn, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				widget.updateDescriptionPanel(property);
			}
		});

		button.pack();
		editor.minimumWidth = button.getSize().x;
		editor.horizontalAlignment = SWT.LEFT;
		if (widget.getWidget() instanceof Table) {
			((TableEditor) editor).setEditor(button, (TableItem) item, 1);
		} else {
			((TreeEditor) editor).setEditor(button, (TreeItem) item, 1);
		}

		button.setEnabled(property.isEnabled());

		return editor;

	}

}
