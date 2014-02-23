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

import org.mihalis.opal.utils.ResourceManager;

/**
 * This is the calculator engine
 */
class CalculatorEngine {

	static final String OPERATOR_PLUS = "+";
	static final String OPERATOR_MINUS = "-";
	static final String OPERATOR_MULTIPLY = "*";
	static final String OPERATOR_DIVIDE = "/";
	static final String CHARACTER_ZERO = "0";
	static final String DOT_CHARACTER = ".";
	static final String EMPTY_STRING = "";

	private enum DISPLAY_MODE {
		INPUT, RESULT, ERROR
	}

	private DISPLAY_MODE displayMode;
	private boolean clearOnNextDigit;
	private double lastNumber;
	private String lastOperator;
	private final CalculatorButtonsComposite composite;

	/**
	 * Constructor
	 * 
	 * @param calculator calculator widget associated to this engine
	 */
	CalculatorEngine(final CalculatorButtonsComposite composite) {
		this.composite = composite;
	}

	/**
	 * Add a decimal point
	 */
	void addDecimalPoint() {
		this.displayMode = DISPLAY_MODE.INPUT;

		if (this.clearOnNextDigit) {
			setDisplayString(EMPTY_STRING);
		}

		final String inputString = getDisplayString();

		if (inputString.indexOf(DOT_CHARACTER) < 0) {
			setDisplayString(inputString + DOT_CHARACTER);
		}
	}

	/**
	 * @param value value to display
	 */
	private void setDisplayString(final String value) {
		this.composite.getDisplayArea().setText(value);
		this.composite.fireModifyListeners();
	}

	/**
	 * @param digit digit to add
	 */
	void addDigitToDisplay(final int digit) {
		if (this.clearOnNextDigit) {
			setDisplayString(EMPTY_STRING);
		}

		String inputString = getDisplayString();

		if (inputString.indexOf(CHARACTER_ZERO) == 0) {
			inputString = inputString.substring(1);
		}

		if (!inputString.equals(CHARACTER_ZERO) || digit > 0) {
			setDisplayString(inputString + digit);
		}

		this.displayMode = DISPLAY_MODE.INPUT;
		this.clearOnNextDigit = false;
	}

	/**
	 * Clear content
	 */
	void clearWholeContent() {
		setDisplayString(CHARACTER_ZERO);
		this.lastOperator = CHARACTER_ZERO;
		this.lastNumber = 0;
		this.displayMode = DISPLAY_MODE.INPUT;
		this.clearOnNextDigit = true;
	}

	/**
	 * Clear result
	 */
	void clearResult() {
		setDisplayString(CHARACTER_ZERO);
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
				setDisplayString(CHARACTER_ZERO);
			}
		}
	}

	/**
	 * @return the displayed value as string
	 */
	private String getDisplayString() {
		return this.composite.getDisplayArea().getText();
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
				displayErrorMessage(ResourceManager.CALCULATOR_DIVIDE_BY_ZERO);
			}
			this.lastOperator = CHARACTER_ZERO;
		}
	}

	/**
	 * @return the result of the last operation
	 * @throws DivideByZeroException
	 */
	private double processLastOperator() throws DivideByZeroException {
		double result = 0;
		final double numberInDisplay = getDisplayedNumber();

		if (this.lastOperator.equals(OPERATOR_DIVIDE)) {
			if (numberInDisplay == 0) {
				throw new DivideByZeroException();
			}
			result = this.lastNumber / numberInDisplay;
		}

		if (this.lastOperator.equals(OPERATOR_MULTIPLY)) {
			result = this.lastNumber * numberInDisplay;
		}

		if (this.lastOperator.equals(OPERATOR_MINUS)) {
			result = this.lastNumber - numberInDisplay;
		}

		if (this.lastOperator.equals(OPERATOR_PLUS)) {
			result = this.lastNumber + numberInDisplay;
		}

		return result;
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
	private void displayErrorMessage(final String errorMessage) {
		setDisplayString(ResourceManager.getLabel(errorMessage));
		this.lastNumber = 0;
		this.displayMode = DISPLAY_MODE.ERROR;
		this.clearOnNextDigit = true;
	}

	/**
	 * Process 1/x operation
	 */
	void processInverseOperation() {
		if (this.displayMode != DISPLAY_MODE.ERROR) {
			try {
				if (getDisplayedNumber() == 0) {
					displayErrorMessage(ResourceManager.CALCULATOR_DIVIDE_BY_ZERO);
					return;
				}
				final double result = 1 / getDisplayedNumber();
				displayResult(result);
			} catch (final Exception ex) {
				displayErrorMessage(ResourceManager.CALCULATOR_DIVIDE_BY_ZERO);
				this.displayMode = DISPLAY_MODE.ERROR;
			}
		}
	}

	/**
	 * @return the displayed number
	 */
	private double getDisplayedNumber() {
		final String input = getDisplayString();
		return Double.parseDouble(input);
	}

	/**
	 * @param operator operation to process
	 */
	void processOperation(final String operator) {
		if (this.displayMode != DISPLAY_MODE.ERROR) {
			final double numberInDisplay = getDisplayedNumber();
			if (this.lastOperator != null && !this.lastOperator.equals(CHARACTER_ZERO)) {
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
			this.lastOperator = operator;
		}
	}

	/**
	 * Process percentage operation
	 */
	void processPerCentageOperation() {
		if (this.displayMode != DISPLAY_MODE.ERROR) {
			try {
				final double result = getDisplayedNumber() / 100;
				displayResult(result);
			} catch (final Exception ex) {
				displayErrorMessage(ResourceManager.CALCULATOR_INVALID_VALUE);
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
			if (input.length() > 0 && !input.equals(CHARACTER_ZERO)) {
				if (input.indexOf(OPERATOR_MINUS) == 0) {
					setDisplayString(input.substring(1));
				} else {
					setDisplayString(OPERATOR_MINUS + input);
				}
			}
		} else if (this.displayMode == DISPLAY_MODE.RESULT) {
			final double numberInDisplay = getDisplayedNumber();

			if (numberInDisplay != 0) {
				displayResult(-numberInDisplay);
			}
		}
	}

	/**
	 * Process square root operation
	 */
	void processSquareRootOperation() {
		if (this.displayMode != DISPLAY_MODE.ERROR) {
			try {
				if (getDisplayString().indexOf(OPERATOR_MINUS) == 0) {
					displayErrorMessage(ResourceManager.CALCULATOR_INVALID_VALUE);
				}

				final double result = Math.sqrt(getDisplayedNumber());
				displayResult(result);
			} catch (final Exception ex) {
				displayErrorMessage(ResourceManager.CALCULATOR_INVALID_VALUE);
				this.displayMode = DISPLAY_MODE.ERROR;
			}
		}
	}

}
