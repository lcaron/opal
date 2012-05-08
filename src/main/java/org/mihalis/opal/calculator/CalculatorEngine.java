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

import org.mihalis.opal.utils.ResourceManager;

/**
 * This is the calculator engine
 */
class CalculatorEngine {

	private enum DISPLAY_MODE {
		INPUT, RESULT, ERROR
	}

	private DISPLAY_MODE displayMode;
	private boolean clearOnNextDigit;
	private double lastNumber;
	private String lastOperator;
	private final CalculatorButtonsPanel panel;

	/**
	 * Constructor
	 * 
	 * @param calculator calculator widget associated to this engine
	 */
	CalculatorEngine(final CalculatorButtonsPanel panel) {
		this.panel = panel;
	}

	/**
	 * Add a decimal point
	 */
	void addDecimalPoint() {
		this.displayMode = DISPLAY_MODE.INPUT;

		if (this.clearOnNextDigit) {
			setDisplayString("");
		}

		final String inputString = getDisplayString();

		// If the input string already contains a decimal point, don't do
		// anything to it.
		if (inputString.indexOf(".") < 0) {
			setDisplayString(new String(inputString + "."));
		}
	}

	/**
	 * @param value value to display
	 */
	private void setDisplayString(final String value) {
		this.panel.getDisplayArea().setText(value);
		this.panel.fireModifyListeners();
	}

	/**
	 * @param digit digit to add
	 */
	void addDigitToDisplay(final int digit) {
		if (this.clearOnNextDigit) {
			setDisplayString("");
		}

		String inputString = getDisplayString();

		if (inputString.indexOf("0") == 0) {
			inputString = inputString.substring(1);
		}

		if (!inputString.equals("0") || digit > 0) {
			setDisplayString(inputString + digit);
		}

		this.displayMode = DISPLAY_MODE.INPUT;
		this.clearOnNextDigit = false;

	}

	/**
	 * Clear content
	 */
	void clearAll() {
		setDisplayString("0");
		this.lastOperator = "0";
		this.lastNumber = 0;
		this.displayMode = DISPLAY_MODE.INPUT;
		this.clearOnNextDigit = true;
	}

	/**
	 * Clear result
	 */
	void clearExisting() {
		setDisplayString("0");
		this.clearOnNextDigit = true;
		this.displayMode = DISPLAY_MODE.INPUT;
	}

	/**
	 * Process backspace
	 */
	void processBackSpace() {
		if (this.displayMode != DISPLAY_MODE.ERROR) {
			setDisplayString(getDisplayString().substring(0, getDisplayString().length() - 1));
			if (getDisplayString().length() < 1) {
				setDisplayString("0");
			}
		}
	}

	/**
	 * @return the displayed value as string
	 */
	private String getDisplayString() {
		return this.panel.getDisplayArea().getText();
	}

	/**
	 * Process equals operation
	 */
	void processEquals() {
		double result = 0;

		if (this.displayMode != DISPLAY_MODE.ERROR) {
			try {
				result = processLastOperator();
				displayResult(result);
			} catch (final DivideByZeroException e) {
				displayError(ResourceManager.CALCULATOR_DIVIDE_BY_ZERO);
			}
			this.lastOperator = "0";
		}
	}

	/**
	 * @param result result to display
	 */
	private void displayResult(final double result) {
		if (Math.floor(result) == result) {
			setDisplayString(Integer.toString((int) result));
		} else {
			setDisplayString(Double.toString(result));
		}
		this.lastNumber = result;
		this.displayMode = DISPLAY_MODE.RESULT;
		this.clearOnNextDigit = true;
	}

	/**
	 * @param errorMessage error message
	 */
	private void displayError(final String errorMessage) {
		setDisplayString(ResourceManager.getLabel(errorMessage));
		this.lastNumber = 0;
		this.displayMode = DISPLAY_MODE.ERROR;
		this.clearOnNextDigit = true;
	}

	/**
	 * Process 1/x operation
	 */
	void processInverse() {
		if (this.displayMode != DISPLAY_MODE.ERROR) {
			try {
				if (getNumberInDisplay() == 0) {
					displayError(ResourceManager.CALCULATOR_DIVIDE_BY_ZERO);
				}
				final double result = 1 / getNumberInDisplay();
				displayResult(result);
			} catch (final Exception ex) {
				displayError(ResourceManager.CALCULATOR_DIVIDE_BY_ZERO);
				this.displayMode = DISPLAY_MODE.ERROR;
			}
		}
	}

	/**
	 * @return the displayed number
	 */
	private double getNumberInDisplay() {
		final String input = getDisplayString();
		return Double.parseDouble(input);
	}

	/**
	 * @param op operation to process
	 */
	void processOperator(final String op) {
		if (this.displayMode != DISPLAY_MODE.ERROR) {
			final double numberInDisplay = getNumberInDisplay();
			if (this.lastOperator != null && !this.lastOperator.equals("0")) {
				try {
					final double result = processLastOperator();
					displayResult(result);
					this.lastNumber = result;
				} catch (final DivideByZeroException e) {
				}
			} else {
				this.lastNumber = numberInDisplay;
			}
			this.clearOnNextDigit = true;
			this.lastOperator = op;
		}
	}

	/**
	 * @return the result of the last operation
	 * @throws DivideByZeroException
	 */
	private double processLastOperator() throws DivideByZeroException {
		double result = 0;
		final double numberInDisplay = getNumberInDisplay();

		if (this.lastOperator.equals("/")) {
			if (numberInDisplay == 0) {
				throw new DivideByZeroException();
			}

			result = this.lastNumber / numberInDisplay;
		}

		if (this.lastOperator.equals("*")) {
			result = this.lastNumber * numberInDisplay;
		}

		if (this.lastOperator.equals("-")) {
			result = this.lastNumber - numberInDisplay;
		}

		if (this.lastOperator.equals("+")) {
			result = this.lastNumber + numberInDisplay;
		}

		return result;
	}

	/**
	 * Process percentage operation
	 */
	void processPerCent() {
		if (this.displayMode != DISPLAY_MODE.ERROR) {
			try {
				final double result = getNumberInDisplay() / 100;
				displayResult(result);
			} catch (final Exception ex) {
				displayError(ResourceManager.CALCULATOR_INVALID_VALUE);
				this.displayMode = DISPLAY_MODE.ERROR;
			}
		}
	}

	/**
	 * Process +/- operation
	 */
	void processSignChange() {
		if (this.displayMode == DISPLAY_MODE.INPUT) {
			final String input = getDisplayString();

			if (input.length() > 0 && !input.equals("0")) {
				if (input.indexOf("-") == 0) {
					setDisplayString(input.substring(1));
				} else {
					setDisplayString("-" + input);
				}
			}
		} else if (this.displayMode == DISPLAY_MODE.RESULT) {
			final double numberInDisplay = getNumberInDisplay();

			if (numberInDisplay != 0) {
				displayResult(-numberInDisplay);
			}
		}
	}

	/**
	 * Process square root operation
	 */
	void processSqrt() {
		if (this.displayMode != DISPLAY_MODE.ERROR) {
			try {
				if (getDisplayString().indexOf("-") == 0) {
					displayError(ResourceManager.CALCULATOR_INVALID_VALUE);
				}

				final double result = Math.sqrt(getNumberInDisplay());
				displayResult(result);
			} catch (final Exception ex) {
				displayError(ResourceManager.CALCULATOR_INVALID_VALUE);
				this.displayMode = DISPLAY_MODE.ERROR;
			}
		}
	}

}
