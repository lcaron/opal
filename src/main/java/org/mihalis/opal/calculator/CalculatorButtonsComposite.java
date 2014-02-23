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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * This composite contains all buttons
 */
class CalculatorButtonsComposite extends Composite {

	private static final String LABEL_C = "C";
	private static final String LABEL_CE = "CE";
	private static final String LABEL_BACK = "Back";
	private final Color darkRedColor;
	private final Color darkBlueColor;
	private final CalculatorEngine engine;
	private Label displayArea;
	private KeyListener keyListener;
	private final List<ModifyListener> modifyListeners;

	/**
	 * Constructor
	 * 
	 * @param parent parent composite
	 * @param style style
	 */
	CalculatorButtonsComposite(final Composite parent, final int style) {
		super(parent, style);
		setLayout(new GridLayout(5, false));
		this.darkRedColor = new Color(getDisplay(), 139, 0, 0);
		this.darkBlueColor = new Color(getDisplay(), 0, 0, 139);
		createButtons();

		SWTGraphicUtil.addDisposer(this, this.darkBlueColor);
		SWTGraphicUtil.addDisposer(this, this.darkRedColor);

		this.engine = new CalculatorEngine(this);
		addKeyListeners();
		this.modifyListeners = new ArrayList<ModifyListener>();
	}

	/**
	 * Create all buttons
	 */
	private void createButtons() {
		final Button buttonBackSpace = createButton(LABEL_BACK, this.darkRedColor);
		buttonBackSpace.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false, 3, 1));
		buttonBackSpace.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.processBackSpace();
			}
		});

		final Button buttonCe = createButton(LABEL_CE, this.darkRedColor);
		buttonCe.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.clearResult();
			}
		});

		final Button buttonC = createButton(LABEL_C, this.darkRedColor);
		buttonC.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.clearWholeContent();
			}
		});

		createDigitButton(7);
		createDigitButton(8);
		createDigitButton(9);

		final Button buttonDivide = createButton(CalculatorEngine.OPERATOR_DIVIDE, getDisplay().getSystemColor(SWT.COLOR_RED));
		buttonDivide.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.processOperation(CalculatorEngine.OPERATOR_DIVIDE);
			}
		});

		final Button buttonSqrt = createButton("\u221A", this.darkRedColor);
		buttonSqrt.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.processSquareRootOperation();
			}
		});

		createDigitButton(4);
		createDigitButton(5);
		createDigitButton(6);

		final Button buttonMultiply = createButton(CalculatorEngine.OPERATOR_MULTIPLY, getDisplay().getSystemColor(SWT.COLOR_RED));
		buttonMultiply.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.processOperation(CalculatorEngine.OPERATOR_MULTIPLY);
			}
		});
		final Button buttonInverse = createButton("1/x", this.darkBlueColor);
		buttonInverse.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.processInverseOperation();
			}
		});

		createDigitButton(1);
		createDigitButton(2);
		createDigitButton(3);

		final Button buttonMinus = createButton(CalculatorEngine.OPERATOR_MINUS, getDisplay().getSystemColor(SWT.COLOR_RED));
		buttonMinus.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.processOperation(CalculatorEngine.OPERATOR_MINUS);
			}
		});

		final Button buttonPercent = createButton("%", this.darkBlueColor);
		buttonPercent.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.processPerCentageOperation();
			}
		});

		createDigitButton(0);

		final Button buttonPlusMinus = createButton("+/-", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonPlusMinus.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.processSignChange();
			}
		});
		final Button buttonDot = createButton(".", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonDot.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.addDecimalPoint();
			}
		});

		final Button buttonPlus = createButton(CalculatorEngine.OPERATOR_PLUS, getDisplay().getSystemColor(SWT.COLOR_RED));
		buttonPlus.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.processOperation(CalculatorEngine.OPERATOR_PLUS);
			}
		});

		final Button buttonEquals = createButton("=", getDisplay().getSystemColor(SWT.COLOR_RED));
		buttonEquals.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.processEquals();
			}
		});
	}

	private void createDigitButton(final int digit) {
		final Button button = createButton(" " + digit + " ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		button.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsComposite.this.engine.addDigitToDisplay(digit);
			}
		});

	}

	private Button createButton(final String label, final Color color) {
		final Button button = new Button(this, SWT.PUSH | SWT.DOUBLE_BUFFERED);
		button.setText("");
		final GridData gd = new GridData(GridData.FILL, GridData.FILL, false, false);
		gd.widthHint = 30;
		button.setLayoutData(gd);

		// Use a paint listener because setForeground is not working on Windows
		button.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(final PaintEvent e) {
				e.gc.setForeground(color);
				e.gc.setFont(getFont());
				final Point textSize = e.gc.textExtent(" " + label + " ", SWT.TRANSPARENT);
				e.gc.drawText(" " + label + " ", (button.getBounds().width - textSize.x) / 2, (button.getBounds().height - textSize.y) / 2, true);
			}
		});

		return button;
	}

	/**
	 * Add key listeners
	 */
	private void addKeyListeners() {
		this.keyListener = new KeyAdapter() {

			/**
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
			 */
			@Override
			public void keyReleased(final KeyEvent e) {
				switch (e.character) {
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
						CalculatorButtonsComposite.this.engine.addDigitToDisplay(Integer.parseInt(String.valueOf(e.character)));
						return;
					case '.':
						CalculatorButtonsComposite.this.engine.addDecimalPoint();
						return;
					case '+':
						CalculatorButtonsComposite.this.engine.processOperation(CalculatorEngine.OPERATOR_PLUS);
						return;
					case '-':
						CalculatorButtonsComposite.this.engine.processOperation(CalculatorEngine.OPERATOR_MINUS);
						return;
					case '*':
						CalculatorButtonsComposite.this.engine.processOperation(CalculatorEngine.OPERATOR_MULTIPLY);
						return;
					case '/':
						CalculatorButtonsComposite.this.engine.processOperation(CalculatorEngine.OPERATOR_DIVIDE);
						return;
					case '=':
						CalculatorButtonsComposite.this.engine.processEquals();
						return;
					case '%':
						CalculatorButtonsComposite.this.engine.processPerCentageOperation();
						return;

				}

				switch (e.keyCode) {
					case SWT.KEYPAD_0:
					case SWT.KEYPAD_1:
					case SWT.KEYPAD_2:
					case SWT.KEYPAD_3:
					case SWT.KEYPAD_4:
					case SWT.KEYPAD_5:
					case SWT.KEYPAD_6:
					case SWT.KEYPAD_7:
					case SWT.KEYPAD_8:
					case SWT.KEYPAD_9:
						final int digit = e.keyCode - SWT.KEYCODE_BIT - 47;
						CalculatorButtonsComposite.this.engine.addDigitToDisplay(digit);
						return;
					case SWT.KEYPAD_ADD:
						CalculatorButtonsComposite.this.engine.processOperation(CalculatorEngine.OPERATOR_PLUS);
						return;
					case SWT.KEYPAD_SUBTRACT:
						CalculatorButtonsComposite.this.engine.processOperation(CalculatorEngine.OPERATOR_MINUS);
						return;
					case SWT.KEYPAD_DIVIDE:
						CalculatorButtonsComposite.this.engine.processOperation(CalculatorEngine.OPERATOR_DIVIDE);
						return;
					case SWT.KEYPAD_MULTIPLY:
						CalculatorButtonsComposite.this.engine.processOperation(CalculatorEngine.OPERATOR_MULTIPLY);
						return;
					case SWT.KEYPAD_CR:
					case SWT.KEYPAD_EQUAL:
					case SWT.CR:
						CalculatorButtonsComposite.this.engine.processEquals();
						return;
					case SWT.KEYPAD_DECIMAL:
						CalculatorButtonsComposite.this.engine.addDecimalPoint();
						return;
					case SWT.BS:
						CalculatorButtonsComposite.this.engine.processBackSpace();
						return;
					case SWT.ESC:
						CalculatorButtonsComposite.this.engine.clearWholeContent();
						return;
				}
			}
		};

		for (final Control control : this.getChildren()) {
			control.addKeyListener(this.keyListener);
		}
		addKeyListener(this.keyListener);

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
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.modifyListeners.add(listener);
	}

	/**
	 * Fire the modify listeners
	 */
	void fireModifyListeners() {
		for (final ModifyListener listener : this.modifyListeners) {
			final Event e = new Event();
			e.widget = this;
			final ModifyEvent modifyEvent = new ModifyEvent(e);
			listener.modifyText(modifyEvent);
		}
	}

	/**
	 * @return the keyListener
	 */
	KeyListener getKeyListener() {
		return this.keyListener;
	}

	/**
	 * @return the text
	 */
	Label getDisplayArea() {
		return this.displayArea;
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
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.modifyListeners.remove(listener);
	}

	/**
	 * @param text the text to set
	 */
	void setDisplayArea(final Label text) {
		this.displayArea = text;
	}

}
