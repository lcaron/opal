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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;

/**
 * This class is a singleton that provides useful methodes
 */
public class SWTGraphicUtil {
	private static SWTGraphicUtil instance;

	/**
	 * Constructor
	 */
	private SWTGraphicUtil() {
	}

	/**
	 * @return the instance of this singleton
	 */
	public static SWTGraphicUtil getInstance() {
		if (instance == null) {
			instance = new SWTGraphicUtil();
		}

		return instance;
	}

	/**
	 * Dispose safely any SWT resource
	 * 
	 * @param r the resource to dispose
	 */
	public void dispose(final Resource r) {
		if (r != null && !r.isDisposed()) {
			r.dispose();
		}
	}

	/**
	 * Loads an image and create a SWT Image corresponding to this file
	 * 
	 * @param fileName file name of the image
	 * @return an image
	 * @see org.eclipse.swt.graphics.Image
	 */
	public Image createImage(final String fileName) {
		return new Image(Display.getCurrent(), SWTGraphicUtil.class.getClassLoader().getResourceAsStream(fileName));
	}

	/**
	 * Create a reflected image of a source Inspired by Daniel Spiewak
	 * (http://www.eclipsezone.com/eclipse/forums/t91013.html)
	 * 
	 * @param source source to be reflected
	 * @return the source image with a reflection
	 */
	public Image createReflectedImage(final Image source) {
		if (source == null) {
			return null;
		}

		if (source.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}

		// Create a new image
		final Rectangle sourceBounds = source.getBounds();
		final Image newImage = new Image(source.getDevice(), new Rectangle(0, 0, sourceBounds.width, (int) (sourceBounds.height * 1.5)));
		final GC gc = new GC(newImage);
		gc.setAdvanced(true);

		gc.drawImage(source, 0, 0);

		// Add the reflection
		final Transform t = new Transform(source.getDevice());
		t.setElements(1, 0, 0, -.5f, 0, sourceBounds.height + sourceBounds.height / 2);
		gc.setTransform(t);

		gc.drawImage(source, 0, 0);

		t.dispose();
		gc.dispose();

		// And add the alpha mask
		final ImageData imgData = newImage.getImageData();
		final int width = imgData.width;
		final int height = imgData.height;
		final byte[] alphaData = new byte[height * width];

		final byte[] noAlpha = new byte[width];
		for (int x = 0; x < width; x++) {
			noAlpha[x] = (byte) 255;
		}

		for (int y = 0; y < height; y++) {
			final byte[] alphaRow = new byte[width];
			if (y < sourceBounds.height) {
				System.arraycopy(noAlpha, 0, alphaData, y * width, width);
			} else {
				for (int x = 0; x < width; x++) {
					alphaRow[x] = (byte) (255 - 255 * y / height);
				}
				System.arraycopy(alphaRow, 0, alphaData, y * width, width);
			}

		}
		imgData.alphaData = alphaData;
		return new Image(source.getDevice(), imgData);
	}

	/**
	 * Returns a new scaled image.
	 * 
	 * @param source the image to be scaled
	 * @param newWidth new width of the image
	 * @param newHeight new height of the image
	 * @return a scaled image of the source
	 */
	public Image resize(final Image source, final int newWidth, final int newHeight) {

		if (source == null) {
			return null;
		}

		if (source.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}

		final Image scaledImage = new Image(source.getDevice(), newWidth, newHeight);
		final GC gc = new GC(scaledImage);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(source, 0, 0, source.getBounds().width, source.getBounds().height, 0, 0, newWidth, newHeight);
		gc.dispose();

		return scaledImage;
	}

	/**
	 * Create a reflected and resized version of an image
	 * 
	 * @param source source image to be scaled and reflected
	 * @param newWidth new width of the scaled image
	 * @param newHeight new height of the scaled image
	 * @return the resized and reflected image
	 */
	public Image createReflectedResizedImage(final Image source, final int newWidth, final int newHeight) {
		if (source == null) {
			return null;
		}

		if (source.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}

		final Image newImage = new Image(source.getDevice(), newWidth, (int) (newHeight * 1.5));
		final GC gc = new GC(newImage);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(source, 0, 0, source.getBounds().width, source.getBounds().height, 0, 0, newWidth, newHeight);

		// Add the reflection
		final Transform t = new Transform(source.getDevice());
		t.setElements(1, 0, 0, -.5f, 0, (float) (newHeight * 1.5));
		gc.setTransform(t);

		gc.drawImage(source, 0, 0, source.getBounds().width, source.getBounds().height, 0, 0, newWidth, newHeight);

		t.dispose();
		gc.dispose();

		// And add the alpha mask
		final ImageData imgData = newImage.getImageData();
		final int width = imgData.width;
		final int height = imgData.height;
		final byte[] alphaData = new byte[height * width];

		final byte[] noAlpha = new byte[width];
		for (int x = 0; x < width; x++) {
			noAlpha[x] = (byte) 255;
		}

		for (int y = 0; y < height; y++) {
			final byte[] alphaRow = new byte[width];
			if (y < newHeight) {
				System.arraycopy(noAlpha, 0, alphaData, y * width, width);
			} else {
				for (int x = 0; x < width; x++) {
					alphaRow[x] = (byte) (255 - 255 * y / height);
				}
				System.arraycopy(alphaRow, 0, alphaData, y * width, width);
			}

		}
		imgData.alphaData = alphaData;
		return new Image(source.getDevice(), imgData);

	}

}
