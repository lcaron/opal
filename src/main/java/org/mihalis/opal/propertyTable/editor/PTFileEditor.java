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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.mihalis.opal.propertyTable.PTProperty;
import org.mihalis.opal.propertyTable.PTWidget;
import org.mihalis.opal.utils.StringUtil;

/**
 * This editor allows user to select a file
 */
public class PTFileEditor extends PTChooserEditor {

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTChooserEditor#openWindow(org.mihalis.opal.propertyTable.PTWidget,
	 *      org.eclipse.swt.widgets.Item,
	 *      org.mihalis.opal.propertyTable.PTProperty)
	 */
	@Override
	protected void openWindow(final PTWidget widget, final Item item, final PTProperty property) {
		final FileDialog dialog = new FileDialog(widget.getWidget().getShell(), SWT.OPEN);
		final String result = dialog.open();
		if (result != null) {
			if (item instanceof TableItem) {
				((TableItem) item).setText(1, result);
			} else {
				((TreeItem) item).setText(1, result);
			}
			property.setValue(result);
		}

	}

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTChooserEditor#getTextFor(org.mihalis.opal.propertyTable.PTProperty)
	 */
	@Override
	protected String getTextFor(final PTProperty property) {
		return StringUtil.safeToString(property.getValue());
	}

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTChooserEditor#getBackgroundColor(org.mihalis.opal.propertyTable.PTProperty)
	 */
	@Override
	protected Color getBackgroundColor(final PTProperty property) {
		return null;
	}

}
