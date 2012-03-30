/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.itemSelector;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.mihalis.opal.OpalItem;

/**
 * Instances of this class represents items manipulated by this DualList widget
 */
public class DLItem extends OpalItem {

	/**
	 * Constructor
	 * 
	 * @param text the text displayed in the DualList widget for this item
	 */
	public DLItem(final String text) {
		this.setText(text);
	}

	/**
	 * Constructor
	 * 
	 * @param text the text displayed in the DualList widget for this item
	 * @param image the image displayed in the DualList widget for this item
	 */
	public DLItem(final String text, final Image image) {
		this.setText(text);
		this.setImage(image);
	}

	/**
	 * Constructor
	 * 
	 * @param text the text displayed in the DualList widget for this item
	 * @param image the image displayed in the DualList widget for this item
	 * @param font the font displayed in the DualList widget for this item
	 * @param foregroundColor the foreground color displayed in the DualList
	 *            widget for this item
	 */
	public DLItem(final String text, final Image image, final Font font, final Color foregroundColor) {
		this.setText(text);
		this.setImage(image);
		this.setFont(font);
		this.setForeground(foregroundColor);
	}

	/**
	 * Constructor
	 * 
	 * @param text the text displayed in the DualList widget for this item
	 * @param image the image displayed in the DualList widget for this item
	 * @param foregroundColor the foreground color displayed in the DualList
	 *            widget for this item
	 * @param backgroundColor the background color displayed in the DualList
	 *            widget for this item
	 */
	public DLItem(final String text, final Image image, final Color foregroundColor, final Color backgroundColor) {
		this.setText(text);
		this.setImage(image);
		this.setForeground(foregroundColor);
		this.setBackground(backgroundColor);
	}

	/**
	 * Constructor
	 * 
	 * @param text the text displayed in the DualList widget for this item
	 * @param image the image displayed in the DualList widget for this item
	 * @param font the font displayed in the DualList widget for this item
	 */
	public DLItem(final String text, final Image image, final Font font) {
		this.setText(text);
		this.setImage(image);
		this.setFont(font);
	}

	/**
	 * @see org.mihalis.opal.OpalItem#getHeight()
	 */
	@Override
	public int getHeight() {
		throw new UnsupportedOperationException("DLItem does not support this method");
	}

	/**
	 * @see org.mihalis.opal.OpalItem#setHeight(int)
	 */
	@Override
	public void setHeight(final int height) {
		throw new UnsupportedOperationException("DLItem does not support this method");
	}

}
