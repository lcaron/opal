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
package org.mihalis.opal.imageSelector;

import org.eclipse.swt.graphics.Point;
import org.mihalis.opal.OpalItem;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * Instances of this class represents items manipulated by the ImageSelector
 * widget
 */
public class ISItem extends OpalItem implements Comparable<ISItem> {

	private double zPosition;
	private Point upperLeftCorner;
	private Point lowerRightCorner;

	/**
	 * Constructor
	 * 
	 * @param fileName file name of the image that will be displayed
	 */
	public ISItem(final String fileName) {
		setImage(SWTGraphicUtil.createImage(fileName));
	}

	/**
	 * Constructor
	 * 
	 * @param title the title of the image
	 * @param fileName file name of the image that will be displayed
	 */
	public ISItem(final String title, final String fileName) {
		setImage(SWTGraphicUtil.createImage(fileName));
		setText(title);
	}

	/**
	 * @return the zPosition
	 */
	double getzPosition() {
		return this.zPosition;
	}

	/**
	 * @param zPosition the zPosition to set
	 */
	ISItem setzPosition(final double zPosition) {
		this.zPosition = zPosition;
		return this;
	}

	/**
	 * @return the upperLeftCorner
	 */
	Point getUpperLeftCorner() {
		return this.upperLeftCorner;
	}

	/**
	 * @param x the upperLeftCorner.x to set
	 * @param y the upperLeftCorner.y to set
	 */
	void setUpperLeftCorner(final int x, final int y) {
		this.upperLeftCorner = new Point(x, y);
	}

	/**
	 * @return the lowerRightCorner
	 */
	Point getLowerRightCorner() {
		return this.lowerRightCorner;
	}

	/**
	 * @param x the lowerRightCorner.x to set
	 * @param y the lowerRightCorner.y to set
	 */
	void setLowerRightCorner(final int x, final int y) {
		this.lowerRightCorner = new Point(x, y);
	}

	void resetCornerToNull() {
		this.upperLeftCorner = null;
		this.lowerRightCorner = null;

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ISItem [getText()=" + this.getText() + "]";
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final ISItem o) {
		return new Double(Math.abs(this.zPosition)).compareTo(Math.abs(o.getzPosition())) * -1;
	}

}
