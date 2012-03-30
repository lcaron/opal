/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - Initial implementation and API
 *******************************************************************************/
package org.mihalis.opal.preferenceWindow.widgets;

import org.eclipse.swt.SWT;
import org.mihalis.opal.preferenceWindow.PreferenceWindow;

/**
 * Instances of this class are text box to type password
 */
public class PWPasswordText extends PWText {

	/**
	 * Constructor
	 * 
	 * @param label associated label
	 * @param propertyKey associated key
	 */
	public PWPasswordText(final String label, final String propertyKey) {
		super(label, propertyKey);
	}

	/**
	 * @see org.mihalis.opal.preferenceWindow.widgets.PWText#addVerifyListeners()
	 */
	@Override
	public void addVerifyListeners() {
	}

	/**
	 * @see org.mihalis.opal.preferenceWindow.widgets.PWWidget#check()
	 */
	@Override
	public void check() {
		final Object value = PreferenceWindow.getInstance().getValueFor(getPropertyKey());
		if (value == null) {
			PreferenceWindow.getInstance().setValue(getPropertyKey(), "");
		} else {
			if (!(value instanceof String)) {
				throw new UnsupportedOperationException("The property '" + getPropertyKey() + "' has to be a String because it is associated to a password text box");
			}
		}

	}

	/**
	 * @see org.mihalis.opal.preferenceWindow.widgets.PWText#convertValue()
	 */
	@Override
	public Object convertValue() {
		return this.text.getText();
	}

	/**
	 * @see org.mihalis.opal.preferenceWindow.widgets.PWText#getStyle()
	 */
	@Override
	public int getStyle() {
		return SWT.PASSWORD;
	}
}
