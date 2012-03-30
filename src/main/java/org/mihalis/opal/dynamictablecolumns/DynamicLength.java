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
 * DynamicLength
 * 
 */
public class DynamicLength {
	
	private Double value = 0.0d;
	private DynamicLengthMeasure dynamicLengthMeasure = null;
	
	/**
	 * Constructor
	 * @param value Double
	 * @param dynamicLengthMeasure DynamicLengthMeasure
	 */
	public DynamicLength(final Double value, final DynamicLengthMeasure dynamicLengthMeasure) {
		this.value = value;
		this.dynamicLengthMeasure = dynamicLengthMeasure;
	}
	
	@Override
	public String toString() {
		if ((value == null) || (dynamicLengthMeasure == null)) {
			return null;
		}
		return (value + "" + dynamicLengthMeasure.getId());
	}

	/**
	 * Get value
	 * @return Double
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * Set value
	 * @param value Double
	 */
	public void setValue(final Double value) {
		this.value = value;
	}

	/**
	 * Get measure
	 * @return DynamicLengthMeasure
	 */
	public DynamicLengthMeasure getMeasure() {
		return dynamicLengthMeasure;
	}

	/**
	 * Set dynamic length measure
	 * @param dynamicLengthMeasure DynamicLengthMeasure
	 */
	public void setMeasure(final DynamicLengthMeasure dynamicLengthMeasure)	{
		this.dynamicLengthMeasure = dynamicLengthMeasure;
	}
	
}