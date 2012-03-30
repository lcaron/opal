/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron@gmail.com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.rangeSlider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * Instances of this class provide a slider with 2 buttons (min value, max
 * value).
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER</dd>
 * <dd>HORIZONTAL</dd>
 * <dd>VERTICAL</dd> *
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * </p>
 */
public class RangeSlider extends Canvas {

	private enum SELECTED_KNOB {
		NONE, UPPER, LOWER
	};

	private int minimum;
	private int maximum;
	private int lowerValue;
	private int upperValue;
	private final List<SelectionListener> listeners;
	private final Image slider, sliderHover, sliderDrag, sliderSelected;
	private final Image vSlider, vSliderHover, vSliderDrag, vSliderSelected;
	private int orientation;
	private int increment;
	private int pageIncrement;
	private SELECTED_KNOB lastSelected;
	private boolean dragInProgress;
	private Point coordUpper;
	private boolean upperHover;
	private Point coordLower;
	private boolean lowerHover;
	private int previousUpperValue;
	private int previousLowerValue;

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
	 * @param parent a composite control which will be the parent of the new
	 *            instance (cannot be null)
	 * @param style the style of control to construct
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 * 
	 */
	public RangeSlider(final Composite parent, final int style) {
		super(parent, SWT.DOUBLE_BUFFERED | ((style & SWT.BORDER) == SWT.BORDER ? SWT.BORDER : SWT.NONE));
		this.minimum = this.lowerValue = 0;
		this.maximum = this.upperValue = 100;
		this.listeners = new ArrayList<SelectionListener>();
		this.increment = 1;
		this.pageIncrement = 10;
		this.lastSelected = SELECTED_KNOB.NONE;
		this.slider = new Image(getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/slider-normal.png"));
		this.sliderHover = new Image(getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/slider-hover.png"));
		this.sliderDrag = new Image(getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/slider-drag.png"));
		this.sliderSelected = new Image(getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/slider-selected.png"));

		this.vSlider = new Image(getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/h-slider-normal.png"));
		this.vSliderHover = new Image(getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/h-slider-hover.png"));
		this.vSliderDrag = new Image(getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/h-slider-drag.png"));
		this.vSliderSelected = new Image(getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/h-slider-selected.png"));

		if ((style & SWT.VERTICAL) == SWT.VERTICAL) {
			this.orientation = SWT.VERTICAL;
		} else {
			this.orientation = SWT.HORIZONTAL;
		}

		addListener(SWT.Dispose, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				SWTGraphicUtil.dispose(RangeSlider.this.slider);
				SWTGraphicUtil.dispose(RangeSlider.this.sliderHover);
				SWTGraphicUtil.dispose(RangeSlider.this.sliderDrag);
				SWTGraphicUtil.dispose(RangeSlider.this.sliderSelected);

				SWTGraphicUtil.dispose(RangeSlider.this.vSlider);
				SWTGraphicUtil.dispose(RangeSlider.this.vSliderHover);
				SWTGraphicUtil.dispose(RangeSlider.this.vSliderDrag);
				SWTGraphicUtil.dispose(RangeSlider.this.vSliderSelected);
			}
		});

		addMouseListeners();
		addListener(SWT.KeyDown, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				handleKeyDown(event);
			}
		});
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				drawWidget(e);

			}
		});

	}

	/**
	 * Add the mouse listeners (mouse up, mouse down, mouse move, mouse wheel)
	 */
	private void addMouseListeners() {
		addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				handleMouseDown(e);
			}
		});

		addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				handleMouseUp(e);
			}
		});

		addListener(SWT.MouseMove, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				handleMouseMove(e);
			}
		});

		addListener(SWT.MouseWheel, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				handleMouseWheel(e);
			}
		});

	}

	/**
	 * Code executed when the mouse is down
	 * 
	 * @param e event
	 */
	private void handleMouseDown(final Event e) {

		if (this.upperHover) {
			this.dragInProgress = true;
			this.lastSelected = SELECTED_KNOB.UPPER;
			this.previousUpperValue = this.upperValue;
			return;
		}

		if (this.lowerHover) {
			this.dragInProgress = true;
			this.lastSelected = SELECTED_KNOB.LOWER;
			this.previousLowerValue = this.lowerValue;
			return;
		}

		this.dragInProgress = false;
		this.lastSelected = SELECTED_KNOB.NONE;
	}

	/**
	 * Code executed when the mouse is up
	 * 
	 * @param e event
	 */
	private void handleMouseUp(final Event e) {
		if (!this.dragInProgress) {
			return;
		}
		this.dragInProgress = false;
		if (!fireSelectionListeners(e)) {
			if (this.lastSelected == SELECTED_KNOB.UPPER) {
				this.upperValue = this.previousUpperValue;
			} else {
				this.lowerValue = this.previousLowerValue;
			}
			redraw();
		}
	}

	/**
	 * Fire all selection listeners
	 * 
	 * @param event selection event
	 * @return <code>true</code> if no listener cancels the selection,
	 *         <code>false</code> otherwise
	 */
	private boolean fireSelectionListeners(final Event event) {
		for (final SelectionListener selectionListener : this.listeners) {
			final SelectionEvent selectionEvent = new SelectionEvent(event);
			selectionListener.widgetSelected(selectionEvent);
			if (!selectionEvent.doit) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Code executed when the mouse pointer is moving
	 * 
	 * @param e event
	 */
	private void handleMouseMove(final Event e) {
		final int x = e.x, y = e.y;
		final Image img = this.orientation == SWT.HORIZONTAL ? this.slider : this.vSlider;
		this.upperHover = x >= this.coordUpper.x && x <= this.coordUpper.x + img.getBounds().width && y >= this.coordUpper.y && y <= this.coordUpper.y + img.getBounds().height;
		this.lowerHover = x >= this.coordLower.x && x <= this.coordLower.x + img.getBounds().width && y >= this.coordLower.y && y <= this.coordLower.y + img.getBounds().height;

		if (this.dragInProgress) {
			if (this.orientation == SWT.HORIZONTAL) {
				final int mouseValue = (int) ((x - 9f) / computePixelSizeForHorizonalSlider()) + this.minimum;
				if (this.lastSelected == SELECTED_KNOB.UPPER) {
					this.upperValue = (int) (Math.ceil(mouseValue / this.increment) * this.increment) - this.increment;
					checkUpperValue();
				} else {
					this.lowerValue = (int) (Math.ceil(mouseValue / this.increment) * this.increment) - this.increment;
					checkLowerValue();
				}

			} else {
				final int mouseValue = (int) ((y - 9f) / computePixelSizeForVerticalSlider()) + this.minimum;
				if (this.lastSelected == SELECTED_KNOB.UPPER) {
					this.upperValue = (int) (Math.ceil(mouseValue / this.increment) * this.increment) - this.increment;
					checkUpperValue();
				} else {
					this.lowerValue = (int) (Math.ceil(mouseValue / this.increment) * this.increment) - this.increment;
					checkLowerValue();
				}

			}
		}

		redraw();
	}

	/**
	 * Code executed when the mouse wheel is activated
	 * 
	 * @param e event
	 */
	private void handleMouseWheel(final Event e) {
		if (this.lastSelected == SELECTED_KNOB.NONE) {
			return;
		}

		if (this.lastSelected == SELECTED_KNOB.LOWER) {
			this.lowerValue += e.count * this.increment;
			checkLowerValue();
			redraw();
		} else {
			this.upperValue += e.count * this.increment;
			checkUpperValue();
			redraw();
		}
	}

	/**
	 * Check if the lower value is in ranges
	 */
	private void checkLowerValue() {
		if (this.lowerValue < this.minimum) {
			this.lowerValue = this.minimum;
		}
		if (this.lowerValue > this.maximum) {
			this.lowerValue = this.maximum;
		}
		if (this.lowerValue > this.upperValue) {
			this.lowerValue = this.upperValue;
		}
	}

	/**
	 * Check if the upper value is in ranges
	 */
	private void checkUpperValue() {
		if (this.upperValue < this.minimum) {
			this.upperValue = this.minimum;
		}
		if (this.upperValue > this.maximum) {
			this.upperValue = this.maximum;
		}
		if (this.upperValue < this.lowerValue) {
			this.upperValue = this.lowerValue;
		}
	}

	/**
	 * Draws the widget
	 * 
	 * @param e paint event
	 */
	private void drawWidget(final PaintEvent e) {
		final Rectangle rect = this.getClientArea();
		if (rect.width == 0 || rect.height == 0) {
			return;
		}
		e.gc.setAdvanced(true);
		e.gc.setAntialias(SWT.ON);
		if (this.orientation == SWT.HORIZONTAL) {
			drawHorizontalRangeSlider(e.gc);
		} else {
			drawVerticalRangeSlider(e.gc);
		}

	}

	/**
	 * Draw the range slider (horizontal)
	 * 
	 * @param gc graphic context
	 */
	private void drawHorizontalRangeSlider(final GC gc) {
		drawBackgroundHorizontal(gc);
		drawBarsHorizontal(gc);
		this.coordUpper = drawHorizontalKnob(gc, this.upperValue, true);
		this.coordLower = drawHorizontalKnob(gc, this.lowerValue, false);
	}

	/**
	 * Draw the background
	 * 
	 * @param gc graphic context
	 */
	private void drawBackgroundHorizontal(final GC gc) {
		final Rectangle clientArea = this.getClientArea();

		gc.setBackground(getBackground());
		gc.fillRectangle(clientArea);

		if (isEnabled()) {
			gc.setForeground(getForeground());
		} else {
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		}
		gc.drawRoundRectangle(9, 9, clientArea.width - 20, clientArea.height - 20, 3, 3);

		final float pixelSize = computePixelSizeForHorizonalSlider();
		final int startX = (int) (pixelSize * this.lowerValue);
		final int endX = (int) (pixelSize * this.upperValue);
		if (isEnabled()) {
			gc.setBackground(getForeground());
		} else {
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		}
		gc.fillRectangle(12 + startX, 9, endX - startX - 6, clientArea.height - 20);

	}

	/**
	 * @return how many pixels corresponds to 1 point of value
	 */
	private float computePixelSizeForHorizonalSlider() {
		return (getClientArea().width - 20f) / (this.maximum - this.minimum);
	}

	/**
	 * Draw the bars
	 * 
	 * @param gc graphic context
	 */
	private void drawBarsHorizontal(final GC gc) {
		final Rectangle clientArea = this.getClientArea();
		if (isEnabled()) {
			gc.setForeground(getForeground());
		} else {
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		}

		final float pixelSize = computePixelSizeForHorizonalSlider();
		for (int i = 1; i < 10; i++) {
			final int x = (int) (9 + pixelSize * (this.maximum - this.minimum) / 10 * i);
			gc.drawLine(x, 4, x, 7);
			gc.drawLine(x, clientArea.height - 6, x, clientArea.height - 9);
		}

	}

	/**
	 * Draws an horizontal knob
	 * 
	 * @param gc graphic context
	 * @param value corresponding value
	 * @param upper if <code>true</code>, draws the upper knob. If
	 *            <code>false</code>, draws the lower knob
	 * @return the coordinate of the upper left corner of the knob
	 */
	private Point drawHorizontalKnob(final GC gc, final int value, final boolean upper) {
		final float pixelSize = computePixelSizeForHorizonalSlider();
		final int x = (int) (pixelSize * value);
		Image image;
		if (upper) {
			if (this.upperHover) {
				image = this.dragInProgress ? this.sliderDrag : this.sliderHover;
			} else if (this.lastSelected == SELECTED_KNOB.UPPER) {
				image = this.sliderSelected;
			} else {
				image = this.slider;
			}
		} else {
			if (this.lowerHover) {
				image = this.dragInProgress ? this.sliderDrag : this.sliderHover;
			} else if (this.lastSelected == SELECTED_KNOB.LOWER) {
				image = this.sliderSelected;
			} else {
				image = this.slider;
			}
		}
		if (isEnabled()) {
			gc.drawImage(image, x + 5, getClientArea().height / 2 - this.slider.getBounds().height / 2);
		} else {
			final Image temp = new Image(getDisplay(), image, SWT.IMAGE_DISABLE);
			gc.drawImage(temp, x + 5, getClientArea().height / 2 - this.slider.getBounds().height / 2);
			temp.dispose();
		}
		return new Point(x + 5, getClientArea().height / 2 - this.slider.getBounds().height / 2);
	}

	/**
	 * Draw the range slider (vertical)
	 * 
	 * @param gc graphic context
	 */
	private void drawVerticalRangeSlider(final GC gc) {
		drawBackgroundVertical(gc);
		drawBarsVertical(gc);
		this.coordUpper = drawVerticalKnob(gc, this.upperValue, true);
		this.coordLower = drawVerticalKnob(gc, this.lowerValue, false);
	}

	/**
	 * Draws the background
	 * 
	 * @param gc graphic context
	 */
	private void drawBackgroundVertical(final GC gc) {
		final Rectangle clientArea = this.getClientArea();
		gc.setBackground(getBackground());
		gc.fillRectangle(clientArea);

		if (isEnabled()) {
			gc.setForeground(getForeground());
		} else {
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		}
		gc.drawRoundRectangle(9, 9, clientArea.width - 20, clientArea.height - 20, 3, 3);

		final float pixelSize = computePixelSizeForVerticalSlider();
		final int startY = (int) (pixelSize * this.lowerValue);
		final int endY = (int) (pixelSize * this.upperValue);
		if (isEnabled()) {
			gc.setBackground(getForeground());
		} else {
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		}
		gc.fillRectangle(9, 12 + startY, clientArea.width - 20, endY - startY - 6);

	}

	/**
	 * @return how many pixels corresponds to 1 point of value
	 */
	private float computePixelSizeForVerticalSlider() {
		return (getClientArea().height - 20f) / (this.maximum - this.minimum);
	}

	/**
	 * Draws the bars
	 * 
	 * @param gc graphic context
	 */
	private void drawBarsVertical(final GC gc) {
		final Rectangle clientArea = this.getClientArea();
		if (isEnabled()) {
			gc.setForeground(getForeground());
		} else {
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		}

		final float pixelSize = computePixelSizeForVerticalSlider();
		for (int i = 1; i < 10; i++) {
			final int y = (int) (9 + pixelSize * (this.maximum - this.minimum) / 10 * i);
			gc.drawLine(4, y, 7, y);
			gc.drawLine(clientArea.width - 6, y, clientArea.width - 9, y);

		}

	}

	/**
	 * Draws a vertical knob
	 * 
	 * @param gc graphic context
	 * @param value corresponding value
	 * @param upper if <code>true</code>, draws the upper knob. If
	 *            <code>false</code>, draws the lower knob
	 * @return the coordinate of the upper left corner of the knob
	 */
	private Point drawVerticalKnob(final GC gc, final int value, final boolean upper) {
		final float pixelSize = computePixelSizeForVerticalSlider();
		final int y = (int) (pixelSize * value);

		Image image;
		if (upper) {
			if (this.upperHover) {
				image = this.dragInProgress ? this.vSliderDrag : this.vSliderHover;
			} else if (this.lastSelected == SELECTED_KNOB.UPPER) {
				image = this.vSliderSelected;
			} else {
				image = this.vSlider;
			}
		} else {
			if (this.lowerHover) {
				image = this.dragInProgress ? this.vSliderDrag : this.vSliderHover;
			} else if (this.lastSelected == SELECTED_KNOB.LOWER) {
				image = this.vSliderSelected;
			} else {
				image = this.vSlider;
			}
		}

		if (isEnabled()) {
			gc.drawImage(image, getClientArea().width / 2 - 8, y + 2);
		} else {
			final Image temp = new Image(getDisplay(), image, SWT.IMAGE_DISABLE);
			gc.drawImage(temp, getClientArea().width / 2 - 8, y + 2);
			temp.dispose();

		}
		return new Point(getClientArea().width / 2 - 8, y + 2);
	}

	/**
	 * Code executed when a key is typed
	 * 
	 * @param event event
	 */
	private void handleKeyDown(final Event event) {

		boolean needRedraw = false;

		if (this.lastSelected == SELECTED_KNOB.NONE) {
			this.lastSelected = SELECTED_KNOB.LOWER;
		}

		switch (event.keyCode) {
		case SWT.HOME:
			if (this.lastSelected == SELECTED_KNOB.UPPER) {
				this.upperValue = this.minimum;
			} else {
				this.lowerValue = this.minimum;
			}
			needRedraw = true;
			break;
		case SWT.END:
			if (this.lastSelected == SELECTED_KNOB.UPPER) {
				this.upperValue = this.maximum;
			} else {
				this.upperValue = this.maximum;
			}
			needRedraw = true;
			break;
		case SWT.PAGE_UP:
			if (this.lastSelected == SELECTED_KNOB.UPPER) {
				this.upperValue += this.pageIncrement;
			} else {
				this.lowerValue += this.pageIncrement;
			}
			needRedraw = true;
			break;
		case SWT.PAGE_DOWN:
			if (this.lastSelected == SELECTED_KNOB.UPPER) {
				this.upperValue -= this.pageIncrement;
			} else {
				this.lowerValue -= this.pageIncrement;
			}
			needRedraw = true;
			break;
		case SWT.ARROW_LEFT:
		case SWT.ARROW_UP:
			if (this.lastSelected == SELECTED_KNOB.UPPER) {
				this.upperValue -= this.increment;
			} else {
				this.lowerValue -= this.increment;
			}
			needRedraw = true;
			break;
		case SWT.ARROW_RIGHT:
		case SWT.ARROW_DOWN:
			if (this.lastSelected == SELECTED_KNOB.UPPER) {
				this.upperValue += this.increment;
			} else {
				this.lowerValue += this.increment;
			}
			needRedraw = true;
			break;
		}

		if (needRedraw) {
			if (this.lastSelected == SELECTED_KNOB.UPPER) {
				checkUpperValue();
			} else {
				checkLowerValue();
			}
			redraw();
		}
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the user changes the receiver's value, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the user changes the
	 * receiver's value. <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 * 
	 * @param listener the listener which should be notified
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 */
	public void addSelectionListener(final SelectionListener listener) {
		checkWidget();
		this.listeners.add(listener);
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
	 */
	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		final int width, height;
		checkWidget();
		if (this.orientation == SWT.HORIZONTAL) {
			if (wHint < 100) {
				width = 100;
			} else {
				width = wHint;
			}

			if (hHint < 30) {
				height = 30;
			} else {
				height = hHint;
			}
		} else {
			if (wHint < 30) {
				width = 30;
			} else {
				width = wHint;
			}

			if (hHint < 100) {
				height = 100;
			} else {
				height = hHint;
			}
		}

		return new Point(width, height);
	}

	/**
	 * Returns the amount that the selected receiver's value will be modified by
	 * when the up/down (or right/left) arrows are pressed.
	 * 
	 * @return the increment
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getIncrement() {
		checkWidget();
		return this.increment;
	}

	/**
	 * Returns the 'lower selection', which is the lower receiver's position.
	 * 
	 * @return the selection
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getLowerValue() {
		checkWidget();
		return this.lowerValue;
	}

	/**
	 * Returns the maximum value which the receiver will allow.
	 * 
	 * @return the maximum
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getMaximum() {
		checkWidget();
		return this.maximum;
	}

	/**
	 * Returns the minimum value which the receiver will allow.
	 * 
	 * @return the minimum
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getMinimum() {
		checkWidget();
		return this.minimum;
	}

	/**
	 * Returns the amount that the selected receiver's value will be modified by
	 * when the page increment/decrement areas are selected.
	 * 
	 * @return the page increment
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getPageIncrement() {
		checkWidget();
		return this.pageIncrement;
	}

	/**
	 * Returns the 'selection', which is an array where the first element is the
	 * lower selection, and the second element is the upper selection
	 * 
	 * @return the selection
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int[] getSelection() {
		checkWidget();
		final int[] selection = new int[2];
		selection[0] = this.lowerValue;
		selection[1] = this.upperValue;
		return selection;
	}

	/**
	 * Returns the 'upper selection', which is the upper receiver's position.
	 * 
	 * @return the selection
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getUpperValue() {
		checkWidget();
		return this.upperValue;
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the user changes the receiver's value.
	 * 
	 * @param listener the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(final SelectionListener listener) {
		checkWidget();
		this.listeners.remove(listener);
	}

	/**
	 * Sets the amount that the selected receiver's value will be modified by
	 * when the up/down (or right/left) arrows are pressed to the argument,
	 * which must be at least one.
	 * 
	 * @param increment the new increment (must be greater than zero)
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setIncrement(final int increment) {
		checkWidget();
		this.increment = increment;
		redraw();
	}

	/**
	 * Sets the 'lower selection', which is the receiver's lower value, to the
	 * argument which must be greater than or equal to zero.
	 * 
	 * @param value the new selection (must be zero or greater)
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setLowerValue(final int value) {
		checkWidget();
		if (this.minimum <= value && value <= this.maximum && value <= this.upperValue) {
			this.lowerValue = value;
		}
		redraw();
	}

	/**
	 * Sets the maximum value that the receiver will allow. This new value will
	 * be ignored if it is not greater than the receiver's current minimum
	 * value. If the new maximum is applied then the receiver's selection value
	 * will be adjusted if necessary to fall within its new range.
	 * 
	 * @param value the new maximum, which must be greater than the current
	 *            minimum
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setMaximum(final int value) {
		checkWidget();
		if (this.minimum <= value) {
			this.maximum = value;
			if (this.lowerValue >= this.maximum) {
				this.lowerValue = this.maximum;
			}
			if (this.upperValue >= this.maximum) {
				this.upperValue = this.maximum;
			}
		}
		redraw();
	}

	/**
	 * Sets the minimum value that the receiver will allow. This new value will
	 * be ignored if it is negative or is not less than the receiver's current
	 * maximum value. If the new minimum is applied then the receiver's
	 * selection value will be adjusted if necessary to fall within its new
	 * range.
	 * 
	 * @param value the new minimum, which must be nonnegative and less than the
	 *            current maximum
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setMinimum(final int value) {
		checkWidget();
		if (this.maximum >= value) {
			this.minimum = value;
			if (this.lowerValue <= this.minimum) {
				this.lowerValue = this.minimum;
			}
			if (this.upperValue <= this.minimum) {
				this.upperValue = this.minimum;
			}
		}
		redraw();
	}

	/**
	 * Sets the amount that the receiver's value will be modified by when the
	 * page increment/decrement areas are selected to the argument, which must
	 * be at least one.
	 * 
	 * @param pageIncrement the page increment (must be greater than zero)
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setPageIncrement(final int pageIncrement) {
		checkWidget();
		this.pageIncrement = pageIncrement;
	}

	/**
	 * Sets the 'selection', which is the receiver's value, to the argument
	 * which must be greater than or equal to zero.
	 * 
	 * @param value the new selection (first value is lower value, second value
	 *            is upper value)
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(final int[] values) {
		checkWidget();
		setLowerValue(values[0]);
		setUpperValue(values[1]);
		checkUpperValue();
		checkLowerValue();
		redraw();
	}

	/**
	 * Sets the 'selection', which is the receiver's value, argument which must
	 * be greater than or equal to zero.
	 * 
	 * @param lowerValue the new lower selection (must be zero or greater)
	 * @param upperValue the new upper selection (must be zero or greater)
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(final int lowerValue, final int upperValue) {
		checkWidget();
		setLowerValue(lowerValue);
		setUpperValue(upperValue);
	}

	/**
	 * Sets the 'upper selection', which is the upper receiver's value, argument
	 * which must be greater than or equal to zero.
	 * 
	 * @param value the new selection (must be zero or greater)
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setUpperValue(final int value) {
		checkWidget();
		if (this.minimum <= value && value <= this.maximum && value >= this.lowerValue) {
			this.upperValue = value;
		}
		redraw();
	}
}
