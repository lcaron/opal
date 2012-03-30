/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - Initial implementation and API
 *     Eugene Ryzhikov - Author of the Oxbow Project (http://code.google.com/p/oxbow/) - Inspiration
 *******************************************************************************/
package org.mihalis.opal.opalDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * This abstract class if the mother of MessageArea and FooterArea classes
 */
abstract class DialogArea {
	protected final Dialog parent;
	private boolean initialised;

	/**
	 * Constructor
	 * 
	 * @param parent parent dialog
	 */
	public DialogArea(final Dialog parent) {
		this.parent = parent;
	}

	/**
	 * Render the content of an area
	 */
	abstract void render();

	/**
	 * @return the initialised field
	 */
	boolean isInitialised() {
		return this.initialised;
	}

	/**
	 * @param initialised the initialised value to set
	 */
	void setInitialised(final boolean initialised) {
		this.initialised = initialised;
	}

	/**
	 * @return the normal font used by the dialog box
	 */
	protected Font getNormalFont() {
		return getFont("Segoe UI", 9, SWT.NONE);
	}

	/**
	 * @return the bigger font used by the dialog box
	 */
	protected Font getBiggerFont() {
		return getFont("Segoe UI", 11, SWT.NONE);
	}

	/**
	 * Build a font
	 * 
	 * @param name name of the font
	 * @param size size of the font
	 * @param style style of the font
	 * @return the font
	 */
	private Font getFont(final String name, final int size, final int style) {
		final Font font = new Font(Display.getCurrent(), name, size, style);
		this.parent.shell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				SWTGraphicUtil.dispose(font);
			}
		});
		return font;
	}

	/**
	 * @return the title's color (blue)
	 */
	protected Color getTitleColor() {
		final Color color = new Color(Display.getCurrent(), 35, 107, 178);
		this.parent.shell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				SWTGraphicUtil.dispose(color);
			}
		});
		return color;
	}

	/**
	 * @return the grey color
	 */
	protected Color getGreyColor() {
		final Color color = new Color(Display.getCurrent(), 240, 240, 240);
		this.parent.shell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				SWTGraphicUtil.dispose(color);
			}
		});
		return color;
	}

	/**
	 * @return the image "fewer details"
	 */
	protected Image getFewerDetailsImage() {
		return loadImage("images/fewerDetails.png");
	}

	/**
	 * @return the image "more details"
	 */
	protected Image getMoreDetailsImage() {
		return loadImage("images/moreDetails.png");
	}

	/**
	 * Loads an image
	 * 
	 * @param fileName file name of the image
	 * @return the image
	 */
	private Image loadImage(final String fileName) {
		final Image image = SWTGraphicUtil.createImage(fileName);
		this.parent.shell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				SWTGraphicUtil.dispose(image);
			}
		});
		return image;
	}

}
