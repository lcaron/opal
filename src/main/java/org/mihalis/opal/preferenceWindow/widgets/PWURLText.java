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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.mihalis.opal.opalDialog.Dialog;
import org.mihalis.opal.preferenceWindow.PreferenceWindow;
import org.mihalis.opal.utils.ResourceManager;

/**
 * Instances of this class are text box used to type URL
 */
public class PWURLText extends PWText {

	/**
	 * Constructor
	 * 
	 * @param label associated label
	 * @param propertyKey associated key
	 */
	public PWURLText(final String label, final String propertyKey) {
		super(label, propertyKey);
		setWidth(200);
	}

	/**
	 * @see org.mihalis.opal.preferenceWindow.widgets.PWText#addVerifyListeners()
	 */
	@Override
	public void addVerifyListeners() {
		this.text.addListener(SWT.FocusOut, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				try {
					new URL(PWURLText.this.text.getText());
				} catch (final MalformedURLException e) {
					Dialog.error(ResourceManager.getLabel(ResourceManager.APPLICATION_ERROR), ResourceManager.getLabel(ResourceManager.VALID_URL));
					event.doit = false;
					PWURLText.this.text.forceFocus();
				}

			}
		});

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
				throw new UnsupportedOperationException("The property '" + getPropertyKey() + "' has to be a String because it is associated to a URL text box");
			}

			final String str = (String) value;
			if (str.equals("")) {
				PreferenceWindow.getInstance().setValue(getPropertyKey(), "");
				return;
			}

			try {
				new URL(str);
			} catch (final MalformedURLException e) {
				throw new UnsupportedOperationException("The property '" + getPropertyKey() + "' has a value (" + value + ") that is not an URL");
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
		return SWT.NONE;
	}

}
