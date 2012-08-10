/*******************************************************************************
 * Copyright (c) 2012 Laurent CARON. All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Path;

/**
 * AdvancedPath
 * 
 * @author Waldimiro Rossi
 * @version 1.0
 * @since 15.12.2005
 * @see Path
 */
public class AdvancedPath extends Path {

	/**
	 * Contructor
	 * 
	 * @param device
	 */
	public AdvancedPath(Device device) {
		super(device);
	}

	/**
	 * Adds to the receiver the circle specified by x, y, radius
	 * 
	 * @param x the x coordinate of the rectangle to add
	 * @param y the y coordinate of the rectangle to add
	 * @param radius the width of the radius
	 * 
	 * @exception SWTException
	 * <ul>
	 * <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public void addCircle(float x, float y, float radius) {
		if (this.isDisposed()) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		this.addArc(x, y, radius, radius, 0, 360);
	}

	/**
	 * Adds to the receiver the round-cornered rectangle specified by x, y, width and height.
	 * 
	 * @param x the x coordinate of the rectangle to add
	 * @param y the y coordinate of the rectangle to add
	 * @param width the width of the rectangle to add
	 * @param height the height of the rectangle to add
	 * @param arcWidth the width of the arc
	 * @param arcHeight the height of the arc
	 * @exception SWTException
	 * <ul>
	 * <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public void addRoundRectangle(float x, float y, float width, float height, float arcWidth, float arcHeight) {
		if (this.isDisposed()) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		float cx = 0;
		float cy = 0;

		// Top left corner
		cx = x;
		cy = y;

		this.cubicTo(cx, cy, cx, cy, x, y + arcHeight);
		this.cubicTo(cx, cy, cx, cy, x + arcWidth, y);

		// Top right corner
		cx = width;
		cy = y;

		this.cubicTo(cx, cy, cx, cy, width - arcWidth, y);
		this.cubicTo(cx, cy, cx, cy, width, y + arcHeight);

		// Bottom right corner
		cx = width;
		cy = height;

		this.cubicTo(cx, cy, cx, cy, width, height - arcHeight);
		this.cubicTo(cx, cy, cx, cy, width - arcWidth, height);

		// Bottom left corner
		cx = x;
		cy = height;

		this.cubicTo(cx, cy, cx, cy, x + arcWidth, height);
		this.cubicTo(cx, cy, cx, cy, x, height - arcHeight);
	}

	/**
	 * Adds to the receiver the rectangle specified by x, y, width and height.<br/>
	 * This rectangle is round-cornered on the left, and straight on the right.
	 * 
	 * @param x the x coordinate of the rectangle to add
	 * @param y the y coordinate of the rectangle to add
	 * @param width the width of the rectangle to add
	 * @param height the height of the rectangle to add
	 * @param arcWidth the width of the arc
	 * @param arcHeight the height of the arc
	 * @exception SWTException
	 * <ul>
	 * <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public void addRoundRectangleStraightRight(float x, float y, float width, float height, float arcWidth, float arcHeight) {
		if (this.isDisposed()) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		float cx = 0;
		float cy = 0;

		// Top left corner
		cx = x;
		cy = y;

		this.cubicTo(cx, cy, cx, cy, x, y + arcHeight);
		this.cubicTo(cx, cy, cx, cy, x + arcWidth, y);

		// Top right corner
		this.lineTo(x + width, y);

		// Bottom right corner
		this.lineTo(x + width, y + height);

		// Bottom left corner
		cx = x;
		cy = height;

		this.cubicTo(cx, cy, cx, cy, x + arcWidth, height);
		this.cubicTo(cx, cy, cx, cy, x, height - arcHeight);
	}
}