/*******************************************************************************
 * Copyright (c) 2011 Luis Carlos Moreira da Costa.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luis Carlos Moreira da Costa (tcljava at gmail dot com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.dynamictablecolumns;

/**
 * 
 * DynamicLengthFormat
 * 
 */
public class DynamicLengthFormat {
	
	/**
	 * Parse
	 * @param str String
	 * @return DynamicLength
	 */
	public static DynamicLength parse(final String str) {
		for (final DynamicLengthMeasure measure : DynamicLengthMeasure.values()) {
			final int indexOf = str.indexOf(measure.getId());
			
			if (indexOf >= 0) {
				final String valuePart = str.substring(0, indexOf).trim();
				final double value = Double.parseDouble(valuePart);
				return new DynamicLength(value, measure);
			}
		}
		throw new IllegalArgumentException("Format invalid length");
	}
	
	/**
	 * TODO
	 * @param dynamicLength DynamicLength
	 * @return String
	 */
	public static String format(final DynamicLength dynamicLength) {
		return "";
	}
	
}