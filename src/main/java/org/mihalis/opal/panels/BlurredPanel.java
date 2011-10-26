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
package org.mihalis.opal.panels;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * Instances of this class are controls located on the top of a shell. They
 * display a blurred version of the content of the shell
 */
public class BlurredPanel {
	private final Shell parent;
	private static final String BLURED_PANEL_KEY = "org.mihalis.opal.Panels.DarkPanel";
	private int radius;
	private Shell panel;
	private Canvas canvas;

	/**
	 * Constructs a new instance of this class given its parent.
	 * 
	 * @param shell a shell that will be the parent of the new instance (cannot
	 *            be null)
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the parent has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 */
	public BlurredPanel(final Shell shell) {
		if (shell == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (shell.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}

		this.parent = shell;
		if (shell.getData(BLURED_PANEL_KEY) != null) {
			throw new IllegalArgumentException("This shell has already an infinite panel attached on it !");
		}
		shell.setData(BLURED_PANEL_KEY, this);
		this.radius = 2;
	}

	/**
	 * Show the blurred panel
	 */
	public void show() {
		if (this.parent.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}

		this.panel = new Shell(this.parent, SWT.APPLICATION_MODAL | SWT.NO_TRIM);
		this.panel.setLayout(new FillLayout());

		this.panel.addListener(SWT.KeyUp, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				event.doit = false;
			}
		});

		this.canvas = new Canvas(this.panel, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);
		this.canvas.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(final PaintEvent e) {
				paintCanvas(e);
			}
		});

		this.panel.setBounds(this.panel.getDisplay().map(this.parent, null, this.parent.getClientArea()));
		this.panel.open();

	}

	/**
	 * Paint the canvas that holds the panel
	 * 
	 * @param e {@link PaintEvent}
	 */
	private void paintCanvas(final PaintEvent e) {
		// Paint the panel
		e.gc.drawImage(createBlurredImage(), 0, 0);
	}

	private Image createBlurredImage() {
		final GC gc = new GC(this.parent);
		final Image image = new Image(this.parent.getDisplay(), this.parent.getSize().x, this.parent.getSize().y);
		gc.copyArea(image, 0, 0);
		gc.dispose();

		return new Image(this.parent.getDisplay(), SWTGraphicUtil.blur(image.getImageData(), this.radius));

	}

	/**
	 * Hide the panel
	 */
	public void hide() {
		if (this.parent.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}

		if (this.panel == null || this.panel.isDisposed()) {
			return;
		}

		this.panel.dispose();
	}

	/**
	 * @return the radius of the blur effect
	 */
	public int getRadius() {
		return this.radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(final int radius) {
		this.radius = radius;
	}

}
