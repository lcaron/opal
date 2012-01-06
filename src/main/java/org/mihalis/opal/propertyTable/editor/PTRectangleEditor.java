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
import org.eclipse.swt.graphics.Rectangle;
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
 * Editor for {@link Rectangle} property
 */
public class PTRectangleEditor extends PTWindowEditor {

	private Text x;
	private Text y;
	private Text width;
	private Text height;

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTWindowEditor#createContent(org.eclipse.swt.widgets.Shell,
	 *      org.mihalis.opal.propertyTable.PTProperty)
	 */
	@Override
	protected void createContent(final Shell shell, final PTProperty property) {
		final Label xLabel = new Label(shell, SWT.NONE);
		xLabel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
		xLabel.setText("X");

		this.x = new Text(shell, SWT.BORDER);
		this.x.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		if (property.getValue() != null) {
			final Rectangle rect = (Rectangle) property.getValue();
			this.x.setText(String.valueOf(rect.x));
		}
		addVerifyListeners(this.x);

		final Label yLabel = new Label(shell, SWT.NONE);
		yLabel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
		yLabel.setText("Y");

		this.y = new Text(shell, SWT.BORDER);
		this.y.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		if (property.getValue() != null) {
			final Rectangle rect = (Rectangle) property.getValue();
			this.y.setText(String.valueOf(rect.y));
		}
		addVerifyListeners(this.y);

		final Label widthLabel = new Label(shell, SWT.NONE);
		widthLabel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
		widthLabel.setText(ResourceManager.getLabel(ResourceManager.WIDTH));

		this.width = new Text(shell, SWT.BORDER);
		this.width.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		if (property.getValue() != null) {
			final Rectangle rect = (Rectangle) property.getValue();
			this.x.setText(String.valueOf(rect.width));
		}
		addVerifyListeners(this.width);

		final Label heightLabel = new Label(shell, SWT.NONE);
		heightLabel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
		heightLabel.setText(ResourceManager.getLabel(ResourceManager.HEIGHT));

		this.height = new Text(shell, SWT.BORDER);
		this.height.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		if (property.getValue() != null) {
			final Rectangle rect = (Rectangle) property.getValue();
			this.y.setText(String.valueOf(rect.height));
		}
		addVerifyListeners(this.height);

	}

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTWindowEditor#fillProperty(org.eclipse.swt.widgets.Item,
	 *      org.mihalis.opal.propertyTable.PTProperty)
	 */
	@Override
	protected void fillProperty(final Item item, final PTProperty property) {
		final Rectangle r = new Rectangle(getIntValue(this.x), getIntValue(this.y), getIntValue(this.width), getIntValue(this.height));
		property.setValue(r);
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
		final Rectangle rect = (Rectangle) property.getValue();
		return "[" + rect.x + "," + rect.y + "," + rect.width + "," + rect.height + "]";
	}

}
