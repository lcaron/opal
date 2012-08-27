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
package org.mihalis.opal.obutton;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * This interface contains methods used to render a button
 */
public interface ButtonRenderer {

	/**
	 * Dispose the elements when the button is disposed
	 */
	void dispose();

	/**
	 * Draw the button when the mouse is hover
	 * @param gc Graphical context
	 * @param button button displayed
	 */
	void drawButtonWhenMouseHover(GC gc, OButton button);

	/**
	 * Draw the button when the button is disabled
	 * @param gc Graphical context
	 * @param button button displayed
	 */
	void drawButtonWhenDisabled(GC gc, OButton button);

	/**
	 * Draw the button when the button (toggle button) is selected
	 * @param gc Graphical context
	 * @param button button displayed
	 */
	void drawButtonWhenSelected(GC gc, OButton button);

	/**
	 * @param gc
	 * @param button
	 */
	void drawButton(GC gc, OButton button);

	/**
	 * Draw the button when the button is clicked
	 * @param gc Graphical context
	 * @param button button displayed
	 */
	void drawButtonWhenClicked(GC gc, OButton parent);

	/**
	 * Compute the size of the button
	 * @param button button associated to this renderer
	 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
	 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
	 * @param changed <code>true</code> if the control's contents have changed, and <code>false</code> otherwise
	 * @return the size of the button
	 */
	Point computeSize(OButton button, int wHint, int hHint, boolean changed);
}
