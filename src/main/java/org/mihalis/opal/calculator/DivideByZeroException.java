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

class DivideByZeroException extends Exception {
	private static final long serialVersionUID = 1764265506499117961L;

	/**
	 * Constructor
	 */
	public DivideByZeroException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param s error message
	 */
	public DivideByZeroException(final String s) {
		super(s);
	}
}