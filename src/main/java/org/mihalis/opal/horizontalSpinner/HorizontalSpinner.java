/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - Implementation
 *******************************************************************************/
package org.mihalis.opal.horizontalSpinner;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.mihalis.opal.utils.StringUtil;

/**
 * Instances of this class are selectable user interface objects that allow the
 * user to enter and modify numeric values.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>READ_ONLY, FLAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, Modify</dd>
 * </dl>
 * </p>
 */
public class HorizontalSpinner extends Composite {

	private enum ALIGNMENT {
		LEFT, RIGHT, BOTH
	};

	private final List<ModifyListener> modifyListeners = new ArrayList<ModifyListener>();
	private final List<SelectionListener> selectionListeners = new ArrayList<SelectionListener>();

	private Button leftButton;
	private Button rightButton;
	private Text text;
	private int digits = 0;
	private int increment = 1;
	private int maximum = 0;
	private int minimum = 255;
	private int pageIncrement = 10;
	private int storedValue = 0;
	private ALIGNMENT alignment = ALIGNMENT.BOTH;

	private final char decimalFormatSeparator;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent a composite control which will be the parent of the new
	 *            instance (cannot be null)
	 * @param style the style of control to construct
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT#READ_ONLY
	 * @see SWT#FLAT
	 */
	public HorizontalSpinner(final Composite parent, final int style) {
		super(parent, style);

		if ((style & SWT.LEFT) == SWT.LEFT) {
			this.alignment = ALIGNMENT.LEFT;
		}

		if ((style & SWT.RIGHT) == SWT.RIGHT) {
			this.alignment = ALIGNMENT.RIGHT;
		}

		final GridLayout gd = new GridLayout(3, false);
		gd.horizontalSpacing = gd.verticalSpacing = 0;
		gd.marginWidth = gd.marginHeight = 0;
		this.setLayout(gd);

		this.createContent(style);
		this.addTextListeners();
		this.addButtonsListener();
		this.addModifyListeners();

		this.decimalFormatSeparator = new DecimalFormatSymbols().getDecimalSeparator();
	}

	/**
	 * Create the content of the widget
	 * 
	 * @param style style of the widget
	 */
	private void createContent(final int style) {
		final boolean readOnly = (style & SWT.READ_ONLY) == SWT.READ_ONLY;
		final boolean flat = (style & SWT.FLAT) == SWT.FLAT;
		final int buttonStyle = SWT.ARROW | (flat ? SWT.FLAT : SWT.NONE);

		if (this.alignment == ALIGNMENT.BOTH) {
			createMinusButton(buttonStyle);
			createText(readOnly);
			createPlusButton(buttonStyle);
		} else if (this.alignment == ALIGNMENT.LEFT) {
			createMinusButton(buttonStyle);
			createPlusButton(buttonStyle);
			createText(readOnly);
		} else {
			createText(readOnly);
			createMinusButton(buttonStyle);
			createPlusButton(buttonStyle);

		}

	}

	/**
	 * Create minus button
	 * 
	 * @param buttonStyle button style
	 */
	private void createMinusButton(final int buttonStyle) {
		this.leftButton = new Button(this, buttonStyle | SWT.LEFT);
		this.leftButton.setFont(this.getFont());
		this.leftButton.setBackground(this.getBackground());
		this.leftButton.setCursor(this.getCursor());
		this.leftButton.setEnabled(this.getEnabled());
		this.leftButton.setFont(this.getFont());
		this.leftButton.setForeground(this.getForeground());
		this.leftButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
	}

	/**
	 * Create the text zone
	 * 
	 * @param readOnly if <code>true</code>, the text is read only
	 */
	private void createText(final boolean readOnly) {
		this.text = new Text(this, readOnly ? SWT.READ_ONLY : SWT.NONE);
		final GridData gd = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gd.minimumWidth = 40;
		this.text.setLayoutData(gd);
	}

	/**
	 * Create plus button
	 * 
	 * @param buttonStyle button style
	 */
	private void createPlusButton(final int buttonStyle) {
		this.rightButton = new Button(this, buttonStyle | SWT.RIGHT);
		this.rightButton.setFont(this.getFont());
		this.rightButton.setBackground(this.getBackground());
		this.rightButton.setCursor(this.getCursor());
		this.rightButton.setEnabled(this.getEnabled());
		this.rightButton.setFont(this.getFont());
		this.rightButton.setForeground(this.getForeground());
		this.rightButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
	}

	/**
	 * Add the text listeners
	 */
	private void addTextListeners() {
		this.text.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(final VerifyEvent e) {
				if (e.character != 0 && !Character.isDigit(e.character) && e.keyCode != SWT.BS && e.keyCode != SWT.DEL) {
					e.doit = false;
					return;
				}

				e.doit = HorizontalSpinner.this.verifyEntryAndStoreValue(e.text, e.keyCode);

			}
		});

		this.text.addKeyListener(new KeyAdapter() {

			/**
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
			 */
			@Override
			public void keyReleased(final KeyEvent e) {
				if (e.keyCode == SWT.ARROW_UP) {
					HorizontalSpinner.this.increaseValue(HorizontalSpinner.this.increment);
				}
				if (e.keyCode == SWT.ARROW_DOWN) {
					HorizontalSpinner.this.decreaseValue(HorizontalSpinner.this.increment);
				}
				if (e.keyCode == SWT.PAGE_UP) {
					HorizontalSpinner.this.increaseValue(HorizontalSpinner.this.pageIncrement);
				}
				if (e.keyCode == SWT.PAGE_DOWN) {
					HorizontalSpinner.this.decreaseValue(HorizontalSpinner.this.pageIncrement);
				}
			}

		});

		this.text.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {

			/**
			 * @see org.eclipse.swt.events.FocusAdapter#focusLost(org.eclipse.swt.events.FocusEvent)
			 */
			@Override
			public void focusLost(final org.eclipse.swt.events.FocusEvent e) {
				if (HorizontalSpinner.this.text.getText().trim().equals("")) {
					HorizontalSpinner.this.setSelection(HorizontalSpinner.this.storedValue);
				}
			}

		});

	}

	/**
	 * Verify the entry and store the value in the field storedValue
	 * 
	 * @param entry entry to check
	 * @param keyCode code of the typed key
	 * @return <code>true</code> if the entry if correct, <code>false</code>
	 *         otherwise
	 */
	private boolean verifyEntryAndStoreValue(final String entry, final int keyCode) {
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
			final double d = Double.parseDouble(work.replace(this.decimalFormatSeparator, '.'));
			this.storedValue = (int) (d * Math.pow(10, this.getDigits()));
		} catch (final NumberFormatException nfe) {
			return false;
		}

		for (final SelectionListener s : HorizontalSpinner.this.selectionListeners) {
			s.widgetSelected(null);
		}

		return true;
	}

	/**
	 * Add the listener to the buttons
	 */
	private void addButtonsListener() {
		this.leftButton.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				HorizontalSpinner.this.decreaseValue(HorizontalSpinner.this.increment);
			}
		});

		this.rightButton.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				HorizontalSpinner.this.increaseValue(HorizontalSpinner.this.increment);
			}
		});

	}

	/**
	 * Increase the value stored in this snippet
	 * 
	 * @param value value to increase
	 */
	private void increaseValue(final int value) {
		this.setSelection(this.getSelection() + value);

	}

	/**
	 * Decrease the value stored in this snippet
	 * 
	 * @param value value to decrease
	 */
	private void decreaseValue(final int value) {
		this.setSelection(this.getSelection() - value);
	}

	/**
	 * Add the modify listeners
	 */
	private void addModifyListeners() {
		this.text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				for (final ModifyListener m : HorizontalSpinner.this.modifyListeners) {
					m.modifyText(e);
				}

			}
		});

	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the receiver's text is modified, by sending it one of the messages
	 * defined in the <code>ModifyListener</code> interface.
	 * 
	 * @param listener the listener which should be notified
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ModifyListener
	 * @see #removeModifyListener
	 * @see org.eclipse.swt.widgets.Spinner#addModifyListener(org.eclipse.swt.events.ModifyListener)
	 */

	public void addModifyListener(final ModifyListener listener) {
		this.checkWidget();
		this.modifyListeners.add(listener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is not called for texts.
	 * <code>widgetDefaultSelected</code> is typically called when ENTER is
	 * pressed in a single-line text.
	 * </p>
	 * 
	 * @param listener the listener which should be notified when the control is
	 *            selected by the user
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(final SelectionListener listener) {
		this.checkWidget();
		this.selectionListeners.add(listener);
	}

	/**
	 * Copies the selected text.
	 * <p>
	 * The current selection is copied to the clipboard.
	 * </p>
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void copy() {
		this.checkWidget();
		this.text.copy();
	}

	/**
	 * Cuts the selected text.
	 * <p>
	 * The current selection is first copied to the clipboard and then deleted
	 * from the widget.
	 * </p>
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void cut() {
		this.checkWidget();
		this.text.cut();
	}

	/**
	 * Returns the number of decimal places used by the receiver.
	 * 
	 * @return the digits
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getDigits() {
		this.checkWidget();
		return this.digits;
	}

	/**
	 * Returns the amount that the receiver's value will be modified by when the
	 * up/down arrows are pressed.
	 * 
	 * @return the increment
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getIncrement() {
		this.checkWidget();
		return this.increment;
	}

	/**
	 * Returns the maximum value which the receiver will allow.
	 * 
	 * @return the maximum
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getMaximum() {
		this.checkWidget();
		return this.maximum;
	}

	/**
	 * Returns the minimum value which the receiver will allow.
	 * 
	 * @return the minimum
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getMinimum() {
		this.checkWidget();
		return this.minimum;
	}

	/**
	 * Returns the amount that the receiver's position will be modified by when
	 * the page up/down keys are pressed.
	 * 
	 * @return the page increment
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getPageIncrement() {
		this.checkWidget();
		return this.pageIncrement;
	}

	/**
	 * Returns the <em>selection</em>, which is the receiver's position.
	 * 
	 * @return the selection
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getSelection() {
		this.checkWidget();
		return this.storedValue;

	}

	/**
	 * Returns a string containing a copy of the contents of the receiver's text
	 * field, or an empty string if there are no contents.
	 * 
	 * @return the receiver's text
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 */
	public String getText() {
		this.checkWidget();
		return this.text.getText();
	}

	/**
	 * Returns the maximum number of characters that the receiver's text field
	 * is capable of holding. If this has not been changed by
	 * <code>setTextLimit()</code>, it will be the constant
	 * <code>Spinner.LIMIT</code>.
	 * 
	 * @return the text limit
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #LIMIT
	 */
	public int getTextLimit() {
		this.checkWidget();
		return this.text.getTextLimit();
	}

	/**
	 * Pastes text from clipboard.
	 * <p>
	 * The selected text is deleted from the widget and new text inserted from
	 * the clipboard.
	 * </p>
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void paste() {
		this.checkWidget();
		this.text.paste();
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the receiver's text is modified.
	 * 
	 * @param listener the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ModifyListener
	 * @see #addModifyListener
	 */
	public void removeModifyListener(final ModifyListener listener) {
		this.checkWidget();
		this.modifyListeners.remove(listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the control is selected by the user.
	 * 
	 * @param listener the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(final SelectionListener listener) {
		this.checkWidget();
		this.selectionListeners.remove(listener);
	}

	/**
	 * Sets the number of decimal places used by the receiver.
	 * <p>
	 * The digit setting is used to allow for floating point values in the
	 * receiver. For example, to set the selection to a floating point value of
	 * 1.37 call setDigits() with a value of 2 and setSelection() with a value
	 * of 137. Similarly, if getDigits() has a value of 2 and getSelection()
	 * returns 137 this should be interpreted as 1.37. This applies to all
	 * numeric APIs.
	 * </p>
	 * 
	 * @param value the new digits (must be greater than or equal to zero)
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the value is less than
	 *                zero</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setDigits(final int value) {
		this.checkWidget();
		this.digits = value;
		this.convertSelection();
	}

	/**
	 * Sets the amount that the receiver's value will be modified by when the
	 * up/down arrows are pressed to the argument, which must be at least one.
	 * 
	 * @param value the new increment (must be greater than zero)
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setIncrement(final int value) {
		this.checkWidget();
		this.increment = value;
	}

	/**
	 * Sets the maximum value that the receiver will allow. This new value will
	 * be ignored if it is less than the receiver's current minimum value. If
	 * the new maximum is applied then the receiver's selection value will be
	 * adjusted if necessary to fall within its new range.
	 * 
	 * @param value the new maximum, which must be greater than or equal to the
	 *            current minimum
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setMaximum(final int value) {
		this.checkWidget();
		this.maximum = value;
	}

	/**
	 * Sets the minimum value that the receiver will allow. This new value will
	 * be ignored if it is greater than the receiver's current maximum value. If
	 * the new minimum is applied then the receiver's selection value will be
	 * adjusted if necessary to fall within its new range.
	 * 
	 * @param value the new minimum, which must be less than or equal to the
	 *            current maximum
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setMinimum(final int value) {
		this.checkWidget();
		this.minimum = value;
	}

	/**
	 * Sets the amount that the receiver's position will be modified by when the
	 * page up/down keys are pressed to the argument, which must be at least
	 * one.
	 * 
	 * @param value the page increment (must be greater than zero)
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setPageIncrement(final int value) {
		this.checkWidget();
		this.pageIncrement = value;
	}

	/**
	 * Sets the <em>selection</em>, which is the receiver's position, to the
	 * argument. If the argument is not within the range specified by minimum
	 * and maximum, it will be adjusted to fall within this range.
	 * 
	 * @param value the new selection (must be zero or greater)
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(int selection) {
		this.checkWidget();
		if (selection < this.minimum) {
			selection = this.minimum;
		} else if (selection > this.maximum) {
			selection = this.maximum;
		}

		this.storedValue = selection;
		this.text.setText(this.convertSelection());
		this.text.selectAll();
		this.text.setFocus();

		for (final SelectionListener s : HorizontalSpinner.this.selectionListeners) {
			s.widgetSelected(null);
		}

	}

	/**
	 * Convert the selection into a string
	 * 
	 * @return the string representation of the selection
	 */
	private String convertSelection() {
		if (this.getDigits() == 0) {
			return String.valueOf(this.storedValue);
		}
		final StringBuilder unformatted = new StringBuilder(String.valueOf(this.storedValue * Math.pow(10, -1 * this.getDigits())));
		for (int i = 0; i < digits; i++) {
			unformatted.append("0");
		}
		final int position = unformatted.indexOf(".");
		final String temp = unformatted.substring(0, position + 1 + digits);
		return temp.replace('.', this.decimalFormatSeparator);

	}

	/**
	 * Sets the maximum number of characters that the receiver's text field is
	 * capable of holding to be the argument.
	 * <p>
	 * To reset this value to the default, use
	 * <code>setTextLimit(Spinner.LIMIT)</code>. Specifying a limit value larger
	 * than <code>Spinner.LIMIT</code> sets the receiver's limit to
	 * <code>Spinner.LIMIT</code>.
	 * </p>
	 * 
	 * @param limit new text limit
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #LIMIT
	 */
	public void setTextLimit(final int limit) {
		this.checkWidget();
		this.text.setTextLimit(limit);
	}

	/**
	 * Sets the receiver's selection, minimum value, maximum value, digits,
	 * increment and page increment all at once.
	 * <p>
	 * Note: This is similar to setting the values individually using the
	 * appropriate methods, but may be implemented in a more efficient fashion
	 * on some platforms.
	 * </p>
	 * 
	 * @param selection the new selection value
	 * @param minimum the new minimum value
	 * @param maximum the new maximum value
	 * @param digits the new digits value
	 * @param increment the new increment value
	 * @param pageIncrement the new pageIncrement value
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setValues(final int selection, final int minimum, final int maximum, final int digits, final int increment, final int pageIncrement) {
		this.setMinimum(minimum);
		this.setMaximum(maximum);
		this.setDigits(digits);
		this.setIncrement(increment);
		this.setPageIncrement(pageIncrement);
		this.setSelection(selection);
	}

	/**
	 * Sets the receiver's drag detect state. If the argument is
	 * <code>true</code>, the receiver will detect drag gestures, otherwise
	 * these gestures will be ignored.
	 * 
	 * @param dragDetect the new drag detect state
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public boolean setFocus() {
		this.checkWidget();
		return this.text.setFocus();
	}

	/**
	 * Forces the receiver to have the <em>keyboard focus</em>, causing all
	 * keyboard events to be delivered to it.
	 * 
	 * @return <code>true</code> if the control got focus, and
	 *         <code>false</code> if it was unable to.
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setFocus
	 */
	@Override
	public boolean forceFocus() {
		this.checkWidget();
		return this.text.forceFocus();
	}

	/**
	 * Sets the receiver's background color to the color specified by the
	 * argument, or to the default system color for the control if the argument
	 * is null.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform. For
	 * example, on Windows the background of a Button cannot be changed.
	 * </p>
	 * 
	 * @param color the new color (or null)
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setBackground(final Color color) {
		super.setBackground(color);
		this.leftButton.setBackground(color);
		this.rightButton.setBackground(color);
		this.text.setBackground(color);
	}

	/**
	 * Sets the receiver's background image to the image specified by the
	 * argument, or to the default system color for the control if the argument
	 * is null. The background image is tiled to fill the available space.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform. For
	 * example, on Windows the background of a Button cannot be changed.
	 * </p>
	 * 
	 * @param image the new image (or null)
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument is not a
	 *                bitmap</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setBackgroundImage(final Image image) {
		super.setBackgroundImage(image);
		this.leftButton.setBackgroundImage(image);
		this.rightButton.setBackgroundImage(image);
		this.text.setBackgroundImage(image);

	}

	/**
	 * Sets the receiver's cursor to the cursor specified by the argument, or to
	 * the default cursor for that kind of control if the argument is null.
	 * <p>
	 * When the mouse pointer passes over a control its appearance is changed to
	 * match the control's cursor.
	 * </p>
	 * 
	 * @param cursor the new cursor (or null)
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setCursor(final Cursor cursor) {
		super.setCursor(cursor);
		this.leftButton.setCursor(cursor);
		this.rightButton.setCursor(cursor);
		this.text.setCursor(cursor);

	}

	/**
	 * Enables the receiver if the argument is <code>true</code>, and disables
	 * it otherwise. A disabled control is typically not selectable from the
	 * user interface and draws with an inactive or "grayed" look.
	 * 
	 * @param enabled the new enabled state
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		this.leftButton.setEnabled(enabled);
		this.rightButton.setEnabled(enabled);
		this.text.setEnabled(enabled);

	}

	/**
	 * Sets the font that the receiver will use to paint textual information to
	 * the font specified by the argument, or to the default font for that kind
	 * of control if the argument is null.
	 * 
	 * @param font the new font (or null)
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setFont(final Font font) {
		super.setFont(font);
		this.text.setFont(font);
	}

	/**
	 * Sets the receiver's foreground color to the color specified by the
	 * argument, or to the default system color for the control if the argument
	 * is null.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform.
	 * </p>
	 * 
	 * @param color the new color (or null)
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setForeground(final Color color) {
		super.setForeground(color);
		this.leftButton.setForeground(color);
		this.rightButton.setForeground(color);
		this.text.setForeground(color);

	}

	/**
	 * Sets the receiver's pop up menu to the argument. All controls may
	 * optionally have a pop up menu that is displayed when the user requests
	 * one for the control. The sequence of key strokes, button presses and/or
	 * button releases that are used to request a pop up menu is platform
	 * specific.
	 * <p>
	 * Note: Disposing of a control that has a pop up menu will dispose of the
	 * menu. To avoid this behavior, set the menu to null before the control is
	 * disposed.
	 * </p>
	 * 
	 * @param menu the new pop up menu
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_MENU_NOT_POP_UP - the menu is not a pop up menu</li>
	 *                <li>ERROR_INVALID_PARENT - if the menu is not in the same
	 *                widget tree</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed
	 *                </li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setMenu(final Menu menu) {
		super.setMenu(menu);
		this.leftButton.setMenu(menu);
		this.rightButton.setMenu(menu);
		this.text.setMenu(menu);
	}

	/**
	 * Sets the receiver's tool tip text to the argument, which may be null
	 * indicating that the default tool tip for the control will be shown. For a
	 * control that has a default tool tip, such as the Tree control on Windows,
	 * setting the tool tip text to an empty string replaces the default,
	 * causing no tool tip text to be shown.
	 * <p>
	 * The mnemonic indicator (character '&amp;') is not displayed in a tool
	 * tip. To display a single '&amp;' in the tool tip, the character '&amp;'
	 * can be escaped by doubling it in the string.
	 * </p>
	 * 
	 * @param string the new tool tip text (or null)
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setToolTipText(final String tooltipText) {
		super.setToolTipText(tooltipText);
		this.leftButton.setToolTipText(tooltipText);
		this.rightButton.setToolTipText(tooltipText);
		this.text.setToolTipText(tooltipText);
	}

}
