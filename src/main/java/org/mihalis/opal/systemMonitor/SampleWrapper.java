/*******************************************************************************
 * Copyright (c) 2012 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.systemMonitor;

import java.util.List;

import org.eclipse.swt.graphics.RGB;
import org.mihalis.opal.utils.FixedSizeQueue;

/**
 * Instances of this class are wrapper that contains a sample, its data, color,
 * caption, formatPattern...
 */
public class SampleWrapper {
	private RGB color;
	private RGB borderColor;
	private String caption;
	private String formatPattern;
	private final Sample sample;
	private final FixedSizeQueue<Double> data;
	private Double lastValue;
	private Double lastMaxValue;
	private Double maxValue;

	/**
	 * Constructor
	 * 
	 * @param sample associated sample
	 */
	SampleWrapper(final Sample sample) {
		super();
		this.sample = sample;
		this.color = new RGB(255, 255, 216);
		this.caption = "";
		this.formatPattern = "";
		this.data = new FixedSizeQueue<Double>(1000);
		this.lastValue = 0d;
		this.lastMaxValue = 0d;
		this.maxValue = 0d;
		createBorderColor();
	}

	/**
	 * Create the border color
	 */
	private void createBorderColor() {
		this.borderColor = new RGB(Math.min(this.color.red * 2, 255), Math.min(this.color.green * 2, 255), Math.min(this.color.blue * 2, 255));
	}

	/**
	 * Collect a sample
	 */
	void collect() {
		this.lastValue = this.sample.getValue();
		this.maxValue = Math.max(this.lastMaxValue, this.sample.getMaxValue());
		this.lastMaxValue = this.sample.getMaxValue();
		this.data.put(this.lastValue);
	}

	/**
	 * @return the border color
	 */
	public RGB getBorderColor() {
		return this.borderColor;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return this.caption;
	}

	/**
	 * @return the color
	 */
	RGB getColor() {
		return this.color;
	}

	/**
	 * @return all data
	 */
	public List<Double> getData() {
		return this.data.getValues();
	}

	/**
	 * @return the format pattern
	 */
	String getFormatPattern() {
		return this.formatPattern;
	}

	/**
	 * @return the last max value collected
	 */
	public Double getLastMaxValue() {
		return this.lastMaxValue;
	}

	/**
	 * @return the last collected value
	 */
	public Double getLastValue() {
		return this.lastValue;
	}

	/**
	 * @return the max value
	 */
	public double getMaxValue() {
		return this.maxValue;
	}

	/**
	 * @return the number of collected elements
	 */
	public int getNumberOfCollectedElements() {
		return this.data.getSize();
	}

	/**
	 * @return the sample
	 */
	Sample getSample() {
		return this.sample;
	}

	/**
	 * @param newSize new size of the data collector array
	 */
	public void resize(final int newSize) {
		this.data.resizeTo(newSize);
	}

	/**
	 * @param caption the caption to set
	 */
	SampleWrapper setCaption(final String caption) {
		this.caption = caption;
		return this;
	}

	/**
	 * @param color the color to set
	 */
	SampleWrapper setColor(final RGB color) {
		this.color = color;
		createBorderColor();
		return this;
	}

	/**
	 * @param formatPattern the format pattern to set
	 */
	SampleWrapper setFormatPattern(final String formatPattern) {
		this.formatPattern = formatPattern;
		return this;
	}
}
