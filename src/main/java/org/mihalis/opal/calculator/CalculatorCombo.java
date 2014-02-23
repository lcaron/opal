/*******************************************************************************
 * Copyright (c) 2012 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:  
 *     Laurent CARON (laurent.caron at gmail dot com) - Initial API and implementation
 *******************************************************************************/

package org.mihalis.opal.calculator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * The CalculatorCombo class represents a selectable user interface object that
 * combines a text field and a calculator buttons panel and issues notification
 * when an the value is modified.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER, FLAT</dd>
 * <dt><b>Events:</b>
 * <dd>Modify</dd>
 * </dl>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#ccombo">CCombo
 *      snippets</a>
 */
public class CalculatorCombo extends Composite {

	private Label label;
	private Button arrow;
	private Shell popup;
	private Listener listener, filter;
	private boolean hasFocus;
	private KeyListener keyListener;
	private CalculatorButtonsComposite composite;

	/**
	 * Constructs a new instance of this class given its parent.
	 * 
	 * @param parent a widget which will be the parent of the new instance (cannot be null)
	 * @param style not used
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 */
	public CalculatorCombo(final Composite parent, final int style) {
		super(parent, style);

		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = gridLayout.marginWidth = gridLayout.marginHeight = 0;
		this.setLayout(gridLayout);

		this.label = new Label(this, SWT.BORDER | SWT.RIGHT);
		this.label.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		this.label.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		this.arrow = new Button(this, SWT.ARROW | SWT.DOWN);
		this.arrow.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));

		this.listener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (CalculatorCombo.this.popup == event.widget) {
					handlePopupEvent(event);
					return;
				}

				if (CalculatorCombo.this.arrow == event.widget) {
					handleButtonEvent(event);
					return;
				}

				if (CalculatorCombo.this == event.widget) {
					handleMultiChoiceEvent(event);
					return;
				}

				if (getShell() == event.widget) {
					getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							if (isDisposed()) {
								return;
							}
							handleFocusEvent(SWT.FocusOut);
						}
					});
				}
			}
		};

		final int[] calculatorComboEvents = { SWT.Dispose, SWT.Move, SWT.Resize };
		for (final int calculatorComboEvent : calculatorComboEvents) {
			this.addListener(calculatorComboEvent, this.listener);
		}

		final int[] buttonEvents = { SWT.Selection, SWT.FocusIn };
		for (final int buttonEvent : buttonEvents) {
			this.arrow.addListener(buttonEvent, this.listener);
		}

		this.filter = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				final Shell shell = ((Control) event.widget).getShell();
				if (shell == CalculatorCombo.this.getShell()) {
					handleFocusEvent(SWT.FocusOut);
				}
			}
		};

		createPopupShell();
	}

	/**
	 * Handle a popup event
	 * 
	 * @param event event to handle
	 */
	private void handlePopupEvent(final Event event) {
		switch (event.type) {
			case SWT.Paint:
				final Rectangle listRect = this.popup.getBounds();
				final Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
				event.gc.setForeground(black);
				event.gc.drawRectangle(0, 0, listRect.width - 1, listRect.height - 1);
				break;
			case SWT.Close:
				event.doit = false;
				hidePopupWindow(false);
				break;
			case SWT.Deactivate:
				hidePopupWindow(false);
				break;
			case SWT.Dispose:
				if (this.keyListener != null) {
					this.label.removeKeyListener(this.keyListener);
				}
				break;
		}
	}

	private void hidePopupWindow(final boolean drop) {
		_displayHidePopupWindow(false);
	}

	private void _displayHidePopupWindow(final boolean show) {
		if (show == isPopupVisible()) {
			return;
		}

		if (!show) {
			this.popup.setVisible(false);
			if (!isDisposed()) {
				this.label.setFocus();
			}
			return;
		}

		if (getShell() != this.popup.getParent()) {
			this.popup.dispose();
			this.popup = null;
			createPopupShell();
		}

		final Point textRect = this.label.toDisplay(this.label.getLocation().x, this.label.getLocation().y);
		final int x = textRect.x;
		final int y = textRect.y + this.label.getSize().y;

		this.popup.setLocation(x, y);
		this.popup.setVisible(true);
		this.popup.setFocus();
	}

	private void createPopupShell() {
		this.popup = new Shell(getShell(), SWT.NO_TRIM | SWT.ON_TOP);
		this.popup.setLayout(new GridLayout());

		final int[] popupEvents = { SWT.Close, SWT.Paint, SWT.Deactivate, SWT.Dispose };
		for (final int popupEvent : popupEvents) {
			this.popup.addListener(popupEvent, this.listener);
		}

		this.composite = new CalculatorButtonsComposite(this.popup, SWT.NONE);
		this.composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		this.composite.setDisplayArea(this.label);
		this.keyListener = this.composite.getKeyListener();
		this.label.addKeyListener(this.keyListener);

		this.popup.pack();
	}

	private void handleButtonEvent(final Event event) {
		switch (event.type) {
			case SWT.FocusIn: {
				handleFocusEvent(SWT.FocusIn);
				break;
			}
			case SWT.Selection: {
				_displayHidePopupWindow(!isPopupVisible());
				break;
			}
		}
	}

	private void handleFocusEvent(final int eventType) {
		if (isDisposed()) {
			return;
		}
		switch (eventType) {
			case SWT.FocusIn: {
				if (this.hasFocus) {
					return;
				}
				this.hasFocus = true;
				final Shell shell = getShell();
				shell.removeListener(SWT.Deactivate, this.listener);
				shell.addListener(SWT.Deactivate, this.listener);
				final Display display = getDisplay();
				display.removeFilter(SWT.FocusIn, this.filter);
				display.addFilter(SWT.FocusIn, this.filter);
				final Event e = new Event();
				notifyListeners(SWT.FocusIn, e);
				break;
			}
			case SWT.FocusOut: {
				if (!this.hasFocus) {
					return;
				}
				final Control focusControl = getDisplay().getFocusControl();
				if (focusControl == this.arrow) {
					return;
				}
				this.hasFocus = false;
				final Shell shell = getShell();
				shell.removeListener(SWT.Deactivate, this.listener);
				final Display display = getDisplay();
				display.removeFilter(SWT.FocusIn, this.filter);
				final Event e = new Event();
				notifyListeners(SWT.FocusOut, e);
				break;
			}
		}
	}

	private boolean isPopupVisible() {
		return !this.popup.isDisposed() && this.popup.getVisible();
	}

	private void handleMultiChoiceEvent(final Event event) {
		switch (event.type) {
			case SWT.Dispose:
				if (this.popup != null && !this.popup.isDisposed()) {
					this.popup.dispose();
				}
				final Shell shell = getShell();
				shell.removeListener(SWT.Deactivate, this.listener);
				final Display display = getDisplay();
				display.removeFilter(SWT.FocusIn, this.filter);
				this.popup = null;
				this.arrow = null;
				break;
			case SWT.Move:
				hidePopupWindow(false);
				break;
			case SWT.Resize:
				if (isPopupVisible()) {
					hidePopupWindow(false);
				}
				break;
		}
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
	 */
	public void addModifyListener(final ModifyListener listener) {
		checkWidget();
		this.composite.addModifyListener(listener);
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
	 */
	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		checkWidget();
		int width = 0, height = 0;

		final GC gc = new GC(this.label);
		final int spacer = gc.stringExtent("                    ").x;
		final int textWidth = gc.stringExtent(this.label.getText()).x;
		gc.dispose();
		final Point textSize = this.label.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		final Point arrowSize = this.arrow.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		final int borderWidth = getBorderWidth();

		height = Math.max(textSize.y, arrowSize.y);
		width = textWidth + 2 * spacer + arrowSize.x + 2 * borderWidth;
		if (wHint != SWT.DEFAULT) {
			width = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			height = hHint;
		}
		return new Point(width + 2 * borderWidth, height + 2 * borderWidth);
	}

	/**
	 * @return the value of the combo
	 */
	public String getValue() {
		checkWidget();
		return this.label.getText();
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
		checkWidget();
		this.composite.removeModifyListener(listener);
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		checkWidget();
		this.arrow.setEnabled(enabled);
		this.label.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setToolTipText(java.lang.String)
	 */
	@Override
	public void setToolTipText(final String txt) {
		checkWidget();
		this.label.setToolTipText(txt);
	}

	/**
	 * @param value new value
	 * @throws NumberFormatException if <code>value</code> is not a valid float
	 *             value
	 */
	public void setValue(final String value) {
		checkWidget();
		new Float(value);
		this.label.setText(value);
	}

}
