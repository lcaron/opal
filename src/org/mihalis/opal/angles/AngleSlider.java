/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron@gmail.com) - initial API and implementation
 *     inspired by the Swing AngleSlider by Jeremy (http://javagraphics.blogspot.com/2008/05/angles-need-gui-widget-for-angles.html)
 *******************************************************************************/
package org.mihalis.opal.angles;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * The Angle class represents a selectable user interface object that can be
 * used to pick angles.
 * 
 */
public class AngleSlider extends Canvas {

	private final Image backgroundImage;
	private final Image buttonFocus;
	private final Image buttonNoFocus;
	private int selection;
	private final List<SelectionListener> selectionListeners;
	private boolean mousePressed;

	/**
	 * Constructs a new instance of this class given its parent.
	 * 
	 * @param parent a widget which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style not used
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 */
	public AngleSlider(final Composite parent, final int style) {
		super(parent, style);

		this.backgroundImage = new Image(getDisplay(), "images/angleBackground.png");

		this.buttonFocus = new Image(getDisplay(), "images/angleButtonFocus.png");
		this.buttonNoFocus = new Image(getDisplay(), "images/angleButtonFocusLost.png");

		this.addListener(SWT.Paint, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				paintControl(event);
			}
		});

		this.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent arg0) {
				AngleSlider.this.backgroundImage.dispose();
				AngleSlider.this.buttonFocus.dispose();
				AngleSlider.this.buttonNoFocus.dispose();
			}
		});

		this.addListener(SWT.MouseDown, createMouseListener());
		this.addListener(SWT.MouseUp, createMouseListener());
		this.addListener(SWT.MouseMove, createMouseListener());
		this.addListener(SWT.KeyDown, createKeyListener());

		this.selection = 0;
		this.selectionListeners = new ArrayList<SelectionListener>();

	}

	private void paintControl(final Event event) {
		final GC gc = event.gc;

		gc.drawImage(this.backgroundImage, 0, 0);

		float angle = this.selection / 360f;
		angle = (float) (angle * 2 * Math.PI - 0.5 * Math.PI);

		final float centerX = 20f;
		final float centerY = 20f;
		final float radius = 10f;
		final float x = (float) (centerX - radius * Math.cos(angle));
		final float y = (float) (centerY - radius * Math.sin(angle));

		if (isFocusControl()) {
			gc.drawImage(this.buttonFocus, (int) x - 2, (int) y - 2);
		} else {
			gc.drawImage(this.buttonNoFocus, (int) x - 2, (int) y - 2);
		}

		if (!isEnabled()) {
			gc.setAlpha(127);
			gc.setAntialias(SWT.ON);
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			gc.fillOval(4, 4, 33, 33);
		}
	}

	private Listener createMouseListener() {
		return new Listener() {

			@Override
			public void handleEvent(final Event event) {
				if (!isEnabled()) {
					return;
				}

				if (event.type == SWT.MouseDown) {
					AngleSlider.this.mousePressed = true;
				}
				if (event.type == SWT.MouseDown || event.type == SWT.MouseMove && AngleSlider.this.mousePressed) {
					final float deltaX = event.x - 20f;
					final float deltaY = event.y - 20f;
					final double angle = Math.atan2(deltaX, deltaY);
					AngleSlider.this.selection = 360 - (int) (360 * angle / (2 * Math.PI) + 360) % 360;

					AngleSlider.this.redraw();
				}
				if (event.type == SWT.MouseUp) {
					AngleSlider.this.mousePressed = false;
					fireSelectionListeners(event);
				}
			}
		};
	}

	private void fireSelectionListeners(final Event event) {
		for (final SelectionListener selectionListener : this.selectionListeners) {
			selectionListener.widgetSelected(new SelectionEvent(event));
		}

	}

	private Listener createKeyListener() {
		return new Listener() {

			@Override
			public void handleEvent(final Event event) {
				if (!isEnabled()) {
					return;
				}
				if (event.type != SWT.KeyDown) {
					return;
				}
				if (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_LEFT) {
					setSelection(AngleSlider.this.selection + 5);
				}
				if (event.keyCode == SWT.ARROW_DOWN || event.keyCode == SWT.ARROW_RIGHT) {
					setSelection(AngleSlider.this.selection - 5);
				}
			}
		};
	}

	/**
	 * @see org.eclipse.swt.widgets.Scale#addSelectionListener(org.eclipse.swt.events.SelectionListener)
	 */
	public void addSelectionListener(final SelectionListener selectionListener) {
		checkWidget();
		this.selectionListeners.add(selectionListener);
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
	 */
	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		checkWidget();
		return new Point(40, 40);
	}

	/**
	 * @see org.eclipse.swt.widgets.Scale#getSelection()
	 */
	public int getSelection() {
		checkWidget();
		return this.selection;
	}

	/**
	 * @see org.eclipse.swt.widgets.Scale#removeSelectionListener(org.eclipse.swt.events.SelectionListener)
	 */
	public void removeSelectionListener(final SelectionListener selectionListener) {
		checkWidget();
		this.selectionListeners.remove(selectionListener);
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		redraw();
	}

	/**
	 * @see org.eclipse.swt.widgets.Scale#setSelection(int)
	 */
	public void setSelection(final int selection) {
		checkWidget();
		this.selection = selection;
		fireSelectionListeners(new Event());
		redraw();
	}

}
