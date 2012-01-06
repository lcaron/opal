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

import java.awt.Insets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.mihalis.opal.propertyTable.PTProperty;
import org.mihalis.opal.utils.ResourceManager;

/**
 * Editor for {@link Insets} values
 */
public class PTInsetsEditor extends PTWindowEditor {

	private Text top;
	private Text left;
	private Text right;
	private Text bottom;

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTWindowEditor#createContent(org.eclipse.swt.widgets.Shell,
	 *      org.mihalis.opal.propertyTable.PTProperty)
	 */
	@Override
	protected void createContent(final Shell shell, final PTProperty property) {
		final Label topLabel = new Label(shell, SWT.NONE);
		topLabel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
		topLabel.setText(ResourceManager.getLabel(ResourceManager.TOP));

		this.top = new Text(shell, SWT.BORDER);
		this.top.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		if (property.getValue() != null) {
			final Insets insets = (Insets) property.getValue();
			this.top.setText(String.valueOf(insets.top));
		}
		addVerifyListeners(this.top);

		final Label heightLabel = new Label(shell, SWT.NONE);
		heightLabel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
		heightLabel.setText(ResourceManager.getLabel(ResourceManager.LEFT));

		this.left = new Text(shell, SWT.BORDER);
		this.left.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		if (property.getValue() != null) {
			final Insets insets = (Insets) property.getValue();
			this.left.setText(String.valueOf(insets.left));
		}
		addVerifyListeners(this.left);

		final Label bottomLabel = new Label(shell, SWT.NONE);
		bottomLabel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
		bottomLabel.setText(ResourceManager.getLabel(ResourceManager.BOTTOM));

		this.bottom = new Text(shell, SWT.BORDER);
		this.bottom.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		if (property.getValue() != null) {
			final Insets insets = (Insets) property.getValue();
			this.bottom.setText(String.valueOf(insets.bottom));
		}
		addVerifyListeners(this.bottom);

		final Label rightLabel = new Label(shell, SWT.NONE);
		rightLabel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
		rightLabel.setText(ResourceManager.getLabel(ResourceManager.RIGHT));

		this.right = new Text(shell, SWT.BORDER);
		this.right.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		if (property.getValue() != null) {
			final Insets insets = (Insets) property.getValue();
			this.right.setText(String.valueOf(insets.bottom));
		}
		addVerifyListeners(this.right);

	}

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTWindowEditor#fillProperty(org.eclipse.swt.widgets.Item,
	 *      org.mihalis.opal.propertyTable.PTProperty)
	 */
	@Override
	protected void fillProperty(final Item item, final PTProperty property) {
		final Insets i = new Insets(getIntValue(this.top), getIntValue(this.left), getIntValue(this.bottom), getIntValue(this.right));
		property.setValue(i);
		if (item instanceof TableItem) {
			((TableItem) item).setText(1, getTextFor(property));
		} else {
			((TreeItem) item).setText(1, getTextFor(property));
		}
	}

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTChooserEditor#getTextFor(org.mihalis.opal.propertyTable.PTProperty)
	 */
	@Override
	protected String getTextFor(final PTProperty property) {
		if (property.getValue() == null) {
			return "(null)";
		}
		final Insets insets = (Insets) property.getValue();
		return "[" + insets.top + "," + insets.left + "," + insets.bottom + "," + insets.right + "]";
	}

}
