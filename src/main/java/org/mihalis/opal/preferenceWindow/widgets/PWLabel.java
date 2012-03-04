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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * Instances of this class are labels, that could contain some HTML tags (B,I,U)
 */
public class PWLabel extends PWWidget {

	private StyledText labelWidget;

	/**
	 * Constructor
	 * 
	 * @param label associated label
	 */
	public PWLabel(final String label) {
		super(label, null, 1, true);
		setAlignment(GridData.FILL);
		setGrabExcessSpace(true);
	}

	/**
	 * @see org.mihalis.opal.preferenceWindow.widgets.PWWidget#build(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control build(final Composite parent) {
		if (getLabel() == null) {
			throw new UnsupportedOperationException("You need to set a description for a PWLabel object");
		}
		this.labelWidget = new StyledText(parent, SWT.WRAP | SWT.READ_ONLY);
		this.labelWidget.setEnabled(false);
		this.labelWidget.setBackground(parent.getBackground());
		this.labelWidget.setText(getLabel());
		SWTGraphicUtil.applyHTMLFormating(this.labelWidget);
		return this.labelWidget;
	}

	/**
	 * @see org.mihalis.opal.preferenceWindow.widgets.PWWidget#check()
	 */
	@Override
	public void check() {
	}

	/**
	 * @see org.mihalis.opal.preferenceWindow.widgets.PWWidget#enableOrDisable()
	 */
	@Override
	public boolean enableOrDisable() {
		if (this.enabler == null) {
			return true;
		}

		final boolean enabled = this.enabler.isEnabled();
		if (!this.labelWidget.isDisposed()) {
			if (enabled) {
				this.labelWidget.setForeground(this.labelWidget.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			} else {
				this.labelWidget.setForeground(this.labelWidget.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
			}
		}
		return enabled;
	}

}
