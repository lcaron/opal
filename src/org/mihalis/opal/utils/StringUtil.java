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
package org.mihalis.opal.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class provides useful String manipulation methods
 * 
 */
public class StringUtil {
	/**
	 * Check if a string is empty or null
	 * 
	 * @param source source string
	 * @return <code>true</code> is the string is empty or null,
	 *         <code>false</code> otherwise
	 */
	public static boolean isEmpty(final String source) {
		return source == null || source.trim().isEmpty();
	}

	/**
	 * Converts exception stack trace as string
	 * 
	 * @param exception exception to convert
	 * @return a string that contains the exception
	 */
	public static final String stackStraceAsString(final Throwable exception) {
		final StringWriter stringWriter = new StringWriter();
		exception.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}
