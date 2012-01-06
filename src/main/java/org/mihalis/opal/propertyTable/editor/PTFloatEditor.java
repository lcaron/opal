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
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.mihalis.opal.utils.StringUtil;

/**
 * This editor is used to edit float values
 */
public class PTFloatEditor extends PTBaseTextEditor {

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTBaseTextEditor#addVerifyListeners()
	 */
	@Override
	public void addVerifyListeners() {
		this.text.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(final VerifyEvent e) {
				if (e.character != 0 && !Character.isDigit(e.character) && e.keyCode != SWT.BS && e.keyCode != SWT.DEL && e.character != '.' && e.character != ',') {
					e.doit = false;
					return;
				}

				e.doit = verifyEntry(e.text, e.keyCode);

			}
		});
	}

	/**
	 * Check if an entry is a float
	 * 
	 * @param entry text typed by the user
	 * @param keyCode key code
	 * @return true if the user typed a float value, false otherwise
	 */
	private boolean verifyEntry(final String entry, final int keyCode) {
		final String work;
		if (keyCode == SWT.DEL) {
			work = StringUtil.removeCharAt(this.text.getText(), this.text.getCaretPosition());
		} else if (keyCode == SWT.BS && this.text.getCaretPosition() == 0) {
			work = StringUtil.removeCharAt(this.text.getText(), this.text.getCaretPosition() - 1);
		} else if (keyCode == 0) {
			work = entry;
		} else {
			work = StringUtil.insertString(this.text.getText(), entry, this.text.getCaretPosition());
		}

		try {
			Double.parseDouble(work.replace(',', '.'));
		} catch (final NumberFormatException nfe) {
			return false;
		}

		return true;
	}

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTBaseTextEditor#convertValue()
	 */
	@Override
	public Object convertValue() {
		return Float.parseFloat(this.text.getText());
	}

	/**
	 * @see org.mihalis.opal.propertyTable.editor.PTBaseTextEditor#getStyle()
	 */
	@Override
	public int getStyle() {
		return SWT.NONE;
	}

}
