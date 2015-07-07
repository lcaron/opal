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
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 *
	 */
	public RangeSlider(final Composite parent, final int style) {
		super(parent, SWT.DOUBLE_BUFFERED | ((style & SWT.BORDER) == SWT.BORDER ? SWT.BORDER : SWT.NONE));
		minimum = lowerValue = 0;
		maximum = upperValue = 100;
		listeners = new ArrayList<SelectionListener>();
		increment = 1;
		pageIncrement = 10;
		lastSelected = SELECTED_KNOB.NONE;
		final ClassLoader loader = org.mihalis.opal.rangeSlider.RangeSlider.class.getClassLoader();
		slider = new Image(getDisplay(), loader.getResourceAsStream("images/slider-normal.png"));
		sliderHover = new Image(getDisplay(), loader.getResourceAsStream("images/slider-hover.png"));
		sliderDrag = new Image(getDisplay(), loader.getResourceAsStream("images/slider-drag.png"));
		sliderSelected = new Image(getDisplay(), loader.getResourceAsStream("images/slider-selected.png"));

		vSlider = new Image(getDisplay(), loader.getResourceAsStream("images/h-slider-normal.png"));
		vSliderHover = new Image(getDisplay(), loader.getResourceAsStream("images/h-slider-hover.png"));
		vSliderDrag = new Image(getDisplay(), loader.getResourceAsStream("images/h-slider-drag.png"));
		vSliderSelected = new Image(getDisplay(), loader.getResourceAsStream("images/h-slider-selected.png"));

		if ((style & SWT.VERTICAL) == SWT.VERTICAL) {
			orientation = SWT.VERTICAL;
		} else {
			orientation = SWT.HORIZONTAL;
		}

		addListener(SWT.Dispose, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				SWTGraphicUtil.safeDispose(slider);
				SWTGraphicUtil.safeDispose(sliderHover);
				SWTGraphicUtil.safeDispose(sliderDrag);
				SWTGraphicUtil.safeDispose(sliderSelected);

				SWTGraphicUtil.safeDispose(vSlider);
				SWTGraphicUtil.safeDispose(vSliderHover);
				SWTGraphicUtil.safeDispose(vSliderDrag);
				SWTGraphicUtil.safeDispose(vSliderSelected);

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

		if (upperHover) {
			dragInProgress = true;
			lastSelected = SELECTED_KNOB.UPPER;
			previousUpperValue = upperValue;
			return;
		}

		if (lowerHover) {
			dragInProgress = true;
			lastSelected = SELECTED_KNOB.LOWER;
			previousLowerValue = lowerValue;
			return;
		}

		dragInProgress = false;
		lastSelected = SELECTED_KNOB.NONE;
	}

	/**
	 * Code executed when the mouse is up
	 *
	 * @param e event
	 */
	private void handleMouseUp(final Event e) {
		if (!dragInProgress) {
			return;
		}
		dragInProgress = false;
		if (!fireSelectionListeners(e)) {
			if (lastSelected == SELECTED_KNOB.UPPER) {
				upperValue = previousUpperValue;
			} else {
				lowerValue = previousLowerValue;
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
		for (final SelectionListener selectionListener : listeners) {
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
		final Image img = orientation == SWT.HORIZONTAL ? slider : vSlider;
		upperHover = x >= coordUpper.x && x <= coordUpper.x + img.getBounds().width && y >= coordUpper.y && y <= coordUpper.y + img.getBounds().height;
		lowerHover = x >= coordLower.x && x <= coordLower.x + img.getBounds().width && y >= coordLower.y && y <= coordLower.y + img.getBounds().height;

		if (dragInProgress) {
			if (orientation == SWT.HORIZONTAL) {
				final int mouseValue = (int) ((x - 9f) / computePixelSizeForHorizonalSlider()) + minimum;
				if (lastSelected == SELECTED_KNOB.UPPER) {
					upperValue = (int) (Math.ceil(mouseValue / increment) * increment) - increment;
					checkUpperValue();
				} else {
					lowerValue = (int) (Math.ceil(mouseValue / increment) * increment) - increment;
					checkLowerValue();
				}

			} else {
				final int mouseValue = (int) ((y - 9f) / computePixelSizeForVerticalSlider()) + minimum;
				if (lastSelected == SELECTED_KNOB.UPPER) {
					upperValue = (int) (Math.ceil(mouseValue / increment) * increment) - increment;
					checkUpperValue();
				} else {
					lowerValue = (int) (Math.ceil(mouseValue / increment) * increment) - increment;
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
		if (lastSelected == SELECTED_KNOB.NONE) {
			return;
		}

		if (lastSelected == SELECTED_KNOB.LOWER) {
			lowerValue += e.count * increment;
			checkLowerValue();
			redraw();
		} else {
			upperValue += e.count * increment;
			checkUpperValue();
			redraw();
		}
	}

	/**
	 * Check if the lower value is in ranges
	 */
	private void checkLowerValue() {
		if (lowerValue < minimum) {
			lowerValue = minimum;
		}
		if (lowerValue > maximum) {
			lowerValue = maximum;
		}
		if (lowerValue > upperValue) {
			lowerValue = upperValue;
		}
	}

	/**
	 * Check if the upper value is in ranges
	 */
	private void checkUpperValue() {
		if (upperValue < minimum) {
			upperValue = minimum;
		}
		if (upperValue > maximum) {
			upperValue = maximum;
		}
		if (upperValue < lowerValue) {
			upperValue = lowerValue;
		}
	}

	/**
	 * Draws the widget
	 *
	 * @param e paint event
	 */
	private void drawWidget(final PaintEvent e) {
		final Rectangle rect = getClientArea();
		if (rect.width == 0 || rect.height == 0) {
			return;
		}
		e.gc.setAdvanced(true);
		e.gc.setAntialias(SWT.ON);
		if (orientation == SWT.HORIZONTAL) {
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
		coordUpper = drawHorizontalKnob(gc, upperValue, true);
		coordLower = drawHorizontalKnob(gc, lowerValue, false);
	}

	/**
	 * Draw the background
	 *
	 * @param gc graphic context
	 */
	private void drawBackgroundHorizontal(final GC gc) {
		final Rectangle clientArea = getClientArea();

		gc.setBackground(getBackground());
		gc.fillRectangle(clientArea);

		if (isEnabled()) {
			gc.setForeground(getForeground());
		} else {
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		}
		gc.drawRoundRectangle(9, 9, clientArea.width - 20, clientArea.height - 20, 3, 3);

		final float pixelSize = computePixelSizeForHorizonalSlider();
		final int startX = (int) (pixelSize * lowerValue);
		final int endX = (int) (pixelSize * upperValue);
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
		return (getClientArea().width - 20f) / (maximum - minimum);
	}

	/**
	 * Draw the bars
	 *
	 * @param gc graphic context
	 */
	private void drawBarsHorizontal(final GC gc) {
		final Rectangle clientArea = getClientArea();
		if (isEnabled()) {
			gc.setForeground(getForeground());
		} else {
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		}

		final float pixelSize = computePixelSizeForHorizonalSlider();
		for (int i = 1; i < 10; i++) {
			final int x = (int) (9 + pixelSize * (maximum - minimum) / 10 * i);
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
			if (upperHover) {
				image = dragInProgress ? sliderDrag : sliderHover;
			} else if (lastSelected == SELECTED_KNOB.UPPER) {
				image = sliderSelected;
			} else {
				image = slider;
			}
		} else {
			if (lowerHover) {
				image = dragInProgress ? sliderDrag : sliderHover;
			} else if (lastSelected == SELECTED_KNOB.LOWER) {
				image = sliderSelected;
			} else {
				image = slider;
			}
		}
		if (isEnabled()) {
			gc.drawImage(image, x + 5, getClientArea().height / 2 - slider.getBounds().height / 2);
		} else {
			final Image temp = new Image(getDisplay(), image, SWT.IMAGE_DISABLE);
			gc.drawImage(temp, x + 5, getClientArea().height / 2 - slider.getBounds().height / 2);
			temp.dispose();
		}
		return new Point(x + 5, getClientArea().height / 2 - slider.getBounds().height / 2);
	}

	/**
	 * Draw the range slider (vertical)
	 *
	 * @param gc graphic context
	 */
	private void drawVerticalRangeSlider(final GC gc) {
		drawBackgroundVertical(gc);
		drawBarsVertical(gc);
		coordUpper = drawVerticalKnob(gc, upperValue, true);
		coordLower = drawVerticalKnob(gc, lowerValue, false);
	}

	/**
	 * Draws the background
	 *
	 * @param gc graphic context
	 */
	private void drawBackgroundVertical(final GC gc) {
		final Rectangle clientArea = getClientArea();
		gc.setBackground(getBackground());
		gc.fillRectangle(clientArea);

		if (isEnabled()) {
			gc.setForeground(getForeground());
		} else {
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		}
		gc.drawRoundRectangle(9, 9, clientArea.width - 20, clientArea.height - 20, 3, 3);

		final float pixelSize = computePixelSizeForVerticalSlider();
		final int startY = (int) (pixelSize * lowerValue);
		final int endY = (int) (pixelSize * upperValue);
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
		return (getClientArea().height - 20f) / (maximum - minimum);
	}

	/**
	 * Draws the bars
	 *
	 * @param gc graphic context
	 */
	private void drawBarsVertical(final GC gc) {
		final Rectangle clientArea = getClientArea();
		if (isEnabled()) {
			gc.setForeground(getForeground());
		} else {
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		}

		final float pixelSize = computePixelSizeForVerticalSlider();
		for (int i = 1; i < 10; i++) {
			final int y = (int) (9 + pixelSize * (maximum - minimum) / 10 * i);
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
			if (upperHover) {
				image = dragInProgress ? vSliderDrag : vSliderHover;
			} else if (lastSelected == SELECTED_KNOB.UPPER) {
				image = vSliderSelected;
			} else {
				image = vSlider;
			}
		} else {
			if (lowerHover) {
				image = dragInProgress ? vSliderDrag : vSliderHover;
			} else if (lastSelected == SELECTED_KNOB.LOWER) {
				image = vSliderSelected;
			} else {
				image = vSlider;
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

		if (lastSelected == SELECTED_KNOB.NONE) {
			lastSelected = SELECTED_KNOB.LOWER;
		}

		switch (event.keyCode) {
		case SWT.HOME:
			if (lastSelected == SELECTED_KNOB.UPPER) {
				upperValue = minimum;
			} else {
				lowerValue = minimum;
			}
			needRedraw = true;
			break;
		case SWT.END:
			if (lastSelected == SELECTED_KNOB.UPPER) {
				upperValue = maximum;
			} else {
				lowerValue = maximum;
			}
			needRedraw = true;
			break;
		case SWT.PAGE_UP:
			if (lastSelected == SELECTED_KNOB.UPPER) {
				upperValue += pageIncrement;
			} else {
				lowerValue += pageIncrement;
			}
			needRedraw = true;
			break;
		case SWT.PAGE_DOWN:
			if (lastSelected == SELECTED_KNOB.UPPER) {
				upperValue -= pageIncrement;
			} else {
				lowerValue -= pageIncrement;
			}
			needRedraw = true;
			break;
		case SWT.ARROW_LEFT:
		case SWT.ARROW_UP:
			if (lastSelected == SELECTED_KNOB.UPPER) {
				upperValue -= increment;
			} else {
				lowerValue -= increment;
			}
			needRedraw = true;
			break;
		case SWT.ARROW_RIGHT:
		case SWT.ARROW_DOWN:
			if (lastSelected == SELECTED_KNOB.UPPER) {
				upperValue += increment;
			} else {
				lowerValue += increment;
			}
			needRedraw = true;
			break;
		}

		if (needRedraw) {
			if (lastSelected == SELECTED_KNOB.UPPER) {
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
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
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
		listeners.add(listener);
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
	 */
	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		final int width, height;
		checkWidget();
		if (orientation == SWT.HORIZONTAL) {
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
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getIncrement() {
		checkWidget();
		return increment;
	}

	/**
	 * Returns the 'lower selection', which is the lower receiver's position.
	 *
	 * @return the selection
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getLowerValue() {
		checkWidget();
		return lowerValue;
	}

	/**
	 * Returns the maximum value which the receiver will allow.
	 *
	 * @return the maximum
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getMaximum() {
		checkWidget();
		return maximum;
	}

	/**
	 * Returns the minimum value which the receiver will allow.
	 *
	 * @return the minimum
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getMinimum() {
		checkWidget();
		return minimum;
	}

	/**
	 * Returns the amount that the selected receiver's value will be modified by
	 * when the page increment/decrement areas are selected.
	 *
	 * @return the page increment
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getPageIncrement() {
		checkWidget();
		return pageIncrement;
	}

	/**
	 * Returns the 'selection', which is an array where the first element is the
	 * lower selection, and the second element is the upper selection
	 *
	 * @return the selection
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int[] getSelection() {
		checkWidget();
		final int[] selection = new int[2];
		selection[0] = lowerValue;
		selection[1] = upperValue;
		return selection;
	}

	/**
	 * Returns the 'upper selection', which is the upper receiver's position.
	 *
	 * @return the selection
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getUpperValue() {
		checkWidget();
		return upperValue;
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the user changes the receiver's value.
	 *
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
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
		listeners.remove(listener);
	}

	/**
	 * Sets the amount that the selected receiver's value will be modified by
	 * when the up/down (or right/left) arrows are pressed to the argument,
	 * which must be at least one.
	 *
	 * @param increment the new increment (must be greater than zero)
	 *
	 * @exception SWTException
	 *                <ul>
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
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setLowerValue(final int value) {
		checkWidget();
		if (minimum <= value && value <= maximum && value <= upperValue) {
			lowerValue = value;
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
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setMaximum(final int value) {
		checkWidget();
		if (minimum <= value) {
			maximum = value;
			if (lowerValue >= maximum) {
				lowerValue = maximum;
			}
			if (upperValue >= maximum) {
				upperValue = maximum;
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
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setMinimum(final int value) {
		checkWidget();
		if (maximum >= value) {
			minimum = value;
			if (lowerValue <= minimum) {
				lowerValue = minimum;
			}
			if (upperValue <= minimum) {
				upperValue = minimum;
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
	 * @exception SWTException
	 *                <ul>
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
	 * @exception SWTException
	 *                <ul>
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
	 * @exception SWTException
	 *                <ul>
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
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setUpperValue(final int value) {
		checkWidget();
		if (minimum <= value && value <= maximum && value >= lowerValue) {
			upperValue = value;
		}
		redraw();
	}
}
