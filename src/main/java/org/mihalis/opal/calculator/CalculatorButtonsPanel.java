/*******************************************************************************
 * Copyright (c) 2012 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Hermant (http://www.javabeginner.com/java-swing/java-swing-calculator) - Initial Version in SWING
 *     Laurent CARON (laurent.caron at gmail dot com) - Port to SWT, improvements
 *******************************************************************************/
package org.mihalis.opal.calculator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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

/**
 * This composite contains all buttons
 */
class CalculatorButtonsPanel extends Composite {

	private final Color DARK_RED;
	private final Color DARK_BLUE;
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
	CalculatorButtonsPanel(final Composite parent, final int style) {
		super(parent, style);
		setLayout(new GridLayout(5, false));
		this.DARK_RED = new Color(getDisplay(), 139, 0, 0);
		this.DARK_BLUE = new Color(getDisplay(), 0, 0, 139);
		createButtons();

		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				CalculatorButtonsPanel.this.DARK_RED.dispose();
				CalculatorButtonsPanel.this.DARK_BLUE.dispose();
			}
		});

		this.engine = new CalculatorEngine(this);
		addKeyListeners();
		this.modifyListeners = new ArrayList<ModifyListener>();

	}

	/**
	 * Create all buttons
	 */
	private void createButtons() {
		final Button buttonBackSpace = createButton(" Back ", this.DARK_RED);
		buttonBackSpace.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false, 3, 1));
		buttonBackSpace.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.processBackSpace();
			}
		});

		final Button buttonCe = createButton(" CE ", this.DARK_RED);
		buttonCe.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.clearExisting();
			}
		});

		final Button buttonC = createButton(" C ", this.DARK_RED);
		buttonC.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.clearAll();
			}
		});
		final Button buttonSeven = createButton(" 7 ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonSeven.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.addDigitToDisplay(7);
			}
		});
		final Button buttonHeight = createButton(" 8 ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonHeight.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.addDigitToDisplay(8);
			}
		});
		final Button buttonNine = createButton(" 9 ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonNine.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.addDigitToDisplay(9);
			}
		});

		final Button buttonDivide = createButton(" / ", getDisplay().getSystemColor(SWT.COLOR_RED));
		buttonDivide.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.processOperator("/");
			}
		});

		final Button buttonSqrt = createButton(" \u221A ", this.DARK_RED);
		buttonSqrt.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.processSqrt();
			}
		});

		final Button buttonFour = createButton(" 4 ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonFour.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.addDigitToDisplay(4);
			}
		});
		final Button buttonFive = createButton(" 5 ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonFive.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.addDigitToDisplay(5);
			}
		});
		final Button buttonSix = createButton(" 6 ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonSix.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.addDigitToDisplay(6);
			}
		});
		final Button buttonMultiply = createButton(" * ", getDisplay().getSystemColor(SWT.COLOR_RED));
		buttonMultiply.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.processOperator("*");
			}
		});
		final Button buttonInverse = createButton(" 1/x ", this.DARK_BLUE);
		buttonInverse.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.processInverse();
			}
		});

		final Button buttonOne = createButton(" 1 ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonOne.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.addDigitToDisplay(1);
			}
		});
		final Button buttonTwo = createButton(" 2 ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonTwo.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.addDigitToDisplay(2);
			}
		});

		final Button buttonThree = createButton(" 3 ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonThree.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.addDigitToDisplay(3);
			}
		});

		final Button buttonMinus = createButton(" - ", getDisplay().getSystemColor(SWT.COLOR_RED));
		buttonMinus.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.processOperator("-");
			}
		});

		final Button buttonPercent = createButton(" % ", this.DARK_BLUE);
		buttonPercent.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.processPerCent();
			}
		});

		final Button buttonZero = createButton(" 0 ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonZero.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.addDigitToDisplay(0);
			}
		});
		final Button buttonPlusMinus = createButton(" +/- ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonPlusMinus.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.processSignChange();
			}
		});
		final Button buttonDot = createButton(" . ", getDisplay().getSystemColor(SWT.COLOR_BLUE));
		buttonDot.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.addDecimalPoint();
			}
		});

		final Button buttonPlus = createButton(" + ", getDisplay().getSystemColor(SWT.COLOR_RED));
		buttonPlus.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.processOperator("+");
			}
		});

		final Button buttonEquals = createButton(" = ", getDisplay().getSystemColor(SWT.COLOR_RED));
		buttonEquals.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				CalculatorButtonsPanel.this.engine.processEquals();
			}
		});
	}

	/**
	 * Create a button
	 * 
	 * @param label label to display
	 * @param color font color
	 * @return the button
	 */
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
				final Point textSize = e.gc.textExtent(label, SWT.TRANSPARENT);
				e.gc.drawText(label, (button.getBounds().width - textSize.x) / 2, (button.getBounds().height - textSize.y) / 2, true);
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
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(0);
						return;
					case '1':
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(1);
						return;
					case '2':
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(2);
						return;
					case '3':
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(3);
						return;
					case '4':
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(4);
						return;
					case '5':
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(5);
						return;
					case '6':
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(6);
						return;
					case '7':
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(7);
						return;
					case '8':
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(8);
						return;
					case '9':
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(9);
						return;
					case '.':
						CalculatorButtonsPanel.this.engine.addDecimalPoint();
						return;
					case '+':
						CalculatorButtonsPanel.this.engine.processOperator("+");
						return;
					case '-':
						CalculatorButtonsPanel.this.engine.processOperator("-");
						return;
					case '*':
						CalculatorButtonsPanel.this.engine.processOperator("*");
						return;
					case '/':
						CalculatorButtonsPanel.this.engine.processOperator("/");
						return;
					case '=':
						CalculatorButtonsPanel.this.engine.processEquals();
						return;
					case '%':
						CalculatorButtonsPanel.this.engine.processPerCent();
						return;

				}
				switch (e.keyCode) {
					case SWT.KEYPAD_0:
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(0);
						return;
					case SWT.KEYPAD_1:
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(1);
						return;
					case SWT.KEYPAD_2:
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(2);
						return;
					case SWT.KEYPAD_3:
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(3);
						return;
					case SWT.KEYPAD_4:
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(4);
						return;
					case SWT.KEYPAD_5:
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(5);
						return;
					case SWT.KEYPAD_6:
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(6);
						return;
					case SWT.KEYPAD_7:
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(7);
						return;
					case SWT.KEYPAD_8:
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(8);
						return;
					case SWT.KEYPAD_9:
						CalculatorButtonsPanel.this.engine.addDigitToDisplay(9);
						return;
					case SWT.KEYPAD_ADD:
						CalculatorButtonsPanel.this.engine.processOperator("+");
						return;
					case SWT.KEYPAD_SUBTRACT:
						CalculatorButtonsPanel.this.engine.processOperator("-");
						return;
					case SWT.KEYPAD_DIVIDE:
						CalculatorButtonsPanel.this.engine.processOperator("/");
						return;
					case SWT.KEYPAD_MULTIPLY:
						CalculatorButtonsPanel.this.engine.processOperator("*");
						return;
					case SWT.KEYPAD_CR:
					case SWT.KEYPAD_EQUAL:
					case SWT.CR:
						CalculatorButtonsPanel.this.engine.processEquals();
						return;
					case SWT.KEYPAD_DECIMAL:
						CalculatorButtonsPanel.this.engine.addDecimalPoint();
						return;
					case SWT.BS:
						CalculatorButtonsPanel.this.engine.processBackSpace();
						return;
					case SWT.ESC:
						CalculatorButtonsPanel.this.engine.clearAll();
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
