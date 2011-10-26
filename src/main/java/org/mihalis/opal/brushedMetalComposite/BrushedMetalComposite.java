/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jerry Huxtable (http://www.jhlabs.com/index.html) - initial API and implementation (on SWING), 
 *     Laurent CARON (laurent.caron at gmail dot com) - port to SWT
 *******************************************************************************/
package org.mihalis.opal.brushedMetalComposite;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * Instances of this class are controls which background's texture is brushed
 * metal "a la Mac"
 */
public class BrushedMetalComposite extends Composite {

	private Image oldImage;
	private int radius = 10;
	private float amount = 0.1f;
	private int color = 0xff888888;
	private float shine = 0.1f;
	private boolean monochrome = true;
	private Random randomNumbers;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent a widget which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style the style of widget to construct
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 * 
	 * @see Composite#Composite(Composite, int)
	 * @see SWT#NO_BACKGROUND
	 * @see SWT#NO_FOCUS
	 * @see SWT#NO_MERGE_PAINTS
	 * @see SWT#NO_REDRAW_RESIZE
	 * @see SWT#NO_RADIO_GROUP
	 * @see SWT#EMBEDDED
	 * @see SWT#DOUBLE_BUFFERED
	 * @see Widget#getStyle
	 */
	public BrushedMetalComposite(final Composite parent, final int style) {
		super(parent, style);
		this.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				BrushedMetalComposite.this.redrawComposite();
			}
		});

		parent.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				SWTGraphicUtil.dispose(BrushedMetalComposite.this.oldImage);
			}
		});
	}

	/**
	 * Redraws the composite
	 */
	private void redrawComposite() {
		final Display display = this.getDisplay();
		final Rectangle rect = this.getClientArea();
		final ImageData imageData = this.drawBrushedMetalBackground(Math.max(1, rect.width), Math.max(1, rect.width));
		final Image newImage = new Image(display, imageData);

		this.setBackgroundImage(newImage);
		SWTGraphicUtil.dispose(this.oldImage);
		this.oldImage = newImage;
	}

	/**
	 * Create a brushed metal background
	 * 
	 * @param width width of the panel
	 * @param height height of the panel
	 * @return an image data that contains the background
	 */
	private ImageData drawBrushedMetalBackground(final int width, final int height) {

		final int[] inPixels = new int[width];
		final PaletteData palette = new PaletteData(0xFF0000, 0x00FF00, 0x0000FF);
		final ImageData data = new ImageData(width, height, 0x20, palette);

		this.randomNumbers = new Random(0);
		final int a = this.color & 0xff000000;
		final int r = this.color >> 16 & 0xff;
		final int g = this.color >> 8 & 0xff;
		final int b = this.color & 0xff;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int tr = r;
				int tg = g;
				int tb = b;
				if (this.shine != 0) {
					final int f = (int) (255 * this.shine * Math.sin((double) x / width * Math.PI));
					tr += f;
					tg += f;
					tb += f;
				}
				if (this.monochrome) {
					final int n = (int) (255 * (2 * this.randomNumbers.nextFloat() - 1) * this.amount);
					inPixels[x] = a | this.clamp(tr + n) << 16 | this.clamp(tg + n) << 8 | this.clamp(tb + n);
				} else {
					inPixels[x] = a | this.random(tr) << 16 | this.random(tg) << 8 | this.random(tb);
				}
			}

			if (this.radius != 0) {
				this.setDataElements(data, palette, 0, y, width, 1, this.blur(inPixels, width, this.radius));
			} else {
				this.setDataElements(data, palette, 0, y, width, 1, inPixels);
			}
		}

		return data;
	}

	/**
	 * Sets the data for a rectangle of pixels from a primitive array
	 * 
	 * @param data the source ImageData
	 * @param palette the palette associated to the imageData
	 * @param posX The X coordinate of the upper left pixel location.
	 * @param posY The Y coordinate of the upper left pixel location.
	 * @param width Width of the pixel rectangle.
	 * @param height Height of the pixel rectangle
	 * @param pixels An array containing the pixel data to place between x,y and
	 *            x+w-1, y+h-1.
	 */
	private void setDataElements(final ImageData data, final PaletteData palette, final int posX, final int posY, final int width, final int height, final int[] pixels) {
		int cpt = 0;
		for (int y = posY; y < posY + height; y++) {
			for (int x = posX; x < posX + width; x++) {
				final int rgb = pixels[cpt++];
				final int pixel = palette.getPixel(new RGB(rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF));
				data.setPixel(x, y, pixel);
				data.setAlpha(x, y, rgb >> 24 & 0xFF);
			}
		}
	}

	/**
	 * Add a random number to the value. The result is between 0 and 255
	 * 
	 * @param x the initial value
	 * @return
	 */
	private int random(int x) {
		x += (int) (255 * (2 * this.randomNumbers.nextFloat() - 1) * this.amount);
		if (x < 0) {
			x = 0;
		} else if (x > 0xff) {
			x = 0xff;
		}
		return x;
	}

	/**
	 * Clamp a number between 0 and 255
	 * 
	 * @param c the number to clamp
	 * @return the number. If c is negative, returns 0. If c is greater than
	 *         255, returns 255.
	 */
	private int clamp(final int c) {
		if (c < 0) {
			return 0;
		}

		if (c > 255) {
			return 255;
		}

		return c;
	}

	/**
	 * Apply a blur filter to an array of int that represents and image which
	 * size is width columns * 1 row
	 * 
	 * @param in the array of int that represents the image
	 * @param width the width of the image
	 * @param radius the "radius" blur parameter
	 */
	private int[] blur(final int[] in, final int width, final int radius) {
		final int[] out = new int[width];
		final int widthMinus1 = width - 1;
		final int r2 = 2 * radius + 1;
		int tr = 0, tg = 0, tb = 0;

		for (int i = -radius; i <= radius; i++) {
			final int rgb = in[this.mod(i, width)];
			tr += rgb >> 16 & 0xff;
			tg += rgb >> 8 & 0xff;
			tb += rgb & 0xff;
		}

		for (int x = 0; x < width; x++) {
			out[x] = 0xff000000 | tr / r2 << 16 | tg / r2 << 8 | tb / r2;

			int i1 = x + radius + 1;
			if (i1 > widthMinus1) {
				i1 = this.mod(i1, width);
			}
			int i2 = x - radius;
			if (i2 < 0) {
				i2 = this.mod(i2, width);
			}
			final int rgb1 = in[i1];
			final int rgb2 = in[i2];

			tr += (rgb1 & 0xff0000) - (rgb2 & 0xff0000) >> 16;
			tg += (rgb1 & 0xff00) - (rgb2 & 0xff00) >> 8;
			tb += (rgb1 & 0xff) - (rgb2 & 0xff);
		}
		return out;
	}

	/**
	 * Return a mod b. This differs from the % operator with respect to negative
	 * numbers.
	 * 
	 * @param a the dividend
	 * @param b the divisor
	 * @return a mod b
	 */
	private int mod(int a, final int b) {
		final int n = a / b;

		a -= n * b;
		if (a < 0) {
			return a + b;
		}
		return a;
	}

	// ------------------------------------ Getters and Setters

	/**
	 * @return the "radius" of the blur
	 */
	public int getRadius() {
		return this.radius;
	}

	/**
	 * @param radius the "radius" of the blur
	 */
	public void setRadius(final int radius) {
		this.radius = radius;
		this.redrawComposite();
	}

	/**
	 * @return the amount of noise to add
	 */
	public float getAmount() {
		return this.amount;
	}

	/**
	 * @param amount the amount of noise to add
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the value is not between 0
	 *                and 1 inclusive</li>
	 *                </ul>
	 */
	public void setAmount(final float amount) {
		if (amount < 0f || amount > 1f) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.amount = amount;
		this.redrawComposite();
	}

	/**
	 * @return the color of the metal. Please notice that this color is a new
	 *         SWT object, so it has to be disposed !
	 */
	public Color getColor() {
		return new Color(this.getDisplay(), this.color >> 16 & 0xFF, this.color >> 8 & 0xFF, this.color & 0xFF);
	}

	/**
	 * @param color the color to set
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the value is null</li>
	 *                </ul>
	 */
	public void setColor(final Color color) {
		if (color == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.color = 0xFF << 24 | color.getRed() << 16 | color.getGreen() << 8 | color.getBlue();
		this.redrawComposite();
	}

	/**
	 * @return the shine to add
	 */
	public float getShine() {
		return this.shine;
	}

	/**
	 * @param shine the shine to set
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the value is not between 0
	 *                and 1 inclusive</li>
	 *                </ul>
	 */
	public void setShine(final float shine) {
		if (this.amount < 0f || this.amount > 1f) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.shine = shine;
		this.redrawComposite();
	}

	/**
	 * @return the monochrome
	 */
	public boolean isMonochrome() {
		return this.monochrome;
	}

	/**
	 * @param monochrome the monochrome to set
	 */
	public void setMonochrome(final boolean monochrome) {
		this.monochrome = monochrome;
		this.redrawComposite();
	}

}
