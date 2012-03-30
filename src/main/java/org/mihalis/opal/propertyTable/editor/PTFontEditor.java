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
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.mihalis.opal.propertyTable.PTProperty;
import org.mihalis.opal.propertyTable.PTWidget;
import org.mihalis.opal.utils.ResourceManager;

/**
 * This editor is a font editor
 */
public class PTFontEditor extends PTChooserEditor {

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTChooserEditor#openWindow(org.mihalis.opal.propertyTable.PTWidget,
	 *      org.eclipse.swt.widgets.Item,
	 *      org.mihalis.opal.propertyTable.PTProperty)
	 */
	@Override
	protected void openWindow(final PTWidget widget, final Item item, final PTProperty property) {
		final FontDialog dialog = new FontDialog(widget.getWidget().getShell());
		final FontData result = dialog.open();
		if (result != null && result.getName() != null && !"".equals(result.getName().trim())) {
			property.setValue(result);
			if (item instanceof TableItem) {
				((TableItem) item).setText(1, getTextFor(property));
			} else {
				((TreeItem) item).setText(1, getTextFor(property));
			}
		}
	}

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTChooserEditor#getTextFor(org.mihalis.opal.propertyTable.PTProperty)
	 */
	@Override
	protected String getTextFor(final PTProperty property) {
		if (property.getValue() == null) {
			return "";
		}

		final FontData fontData = (FontData) property.getValue();

		final StringBuilder sb = new StringBuilder();
		if (fontData != null) {
			sb.append(fontData.getName()).append(",").append(fontData.getHeight()).append(" pt");
			if ((fontData.getStyle() & SWT.BOLD) == SWT.BOLD) {
				sb.append(", ").append(ResourceManager.getLabel(ResourceManager.BOLD));
			}
			if ((fontData.getStyle() & SWT.ITALIC) == SWT.ITALIC) {
				sb.append(", ").append(ResourceManager.getLabel(ResourceManager.ITALIC));
			}
		}
		return sb.toString();
	}

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTChooserEditor#getBackgroundColor(org.mihalis.opal.propertyTable.PTProperty)
	 */
	@Override
	protected Color getBackgroundColor(final PTProperty property) {
		return null;
	}

}
