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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

/**
 * Instances of this class represent a selectable user interface object that issues notification when pressed and released.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ARROW, PUSH, TOGGLE</dd>
 * <dd>UP, DOWN, LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ARROW, PUSH and TOGGLE may be specified.
 * </p>
 * <p>
 * Note: Only one of the styles LEFT, RIGHT, and CENTER may be specified.
 * </p>
 * <p>
 * Note: Only one of the styles UP, DOWN, LEFT, and RIGHT may be specified when the ARROW style is specified.
 * </p>
 */
public class OButton extends Canvas {

	private static final String IS_BUTTON_PRESSED = OButton.class.toString() + "_pressed";
	private final List<SelectionListener> selectionListeners;
	int alignment = SWT.LEFT;
	private ButtonRenderer buttonRenderer;
	boolean selected;
	boolean hover;
	String text;
	Image image;
	private int width;
	private int height;
	private boolean clicked;

	/**
	 * Constructs a new instance of this class given its parent and a style value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class <code>SWT</code> which is applicable to instances of this class, or must be built by <em>bitwise OR</em>'ing together (that is, using the <code>int</code> "|" operator) two or
	 * more of those <code>SWT</code> style constants. The class description lists the style constants that are applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent a composite control which will be the parent of the new instance (cannot be null)
	 * @param style the style of control to construct
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT#ARROW
	 * @see SWT#CHECK
	 * @see SWT#PUSH
	 * @see SWT#RADIO
	 * @see SWT#TOGGLE
	 * @see SWT#FLAT
	 * @see SWT#UP
	 * @see SWT#DOWN
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public OButton(final Composite parent, final int style) {
		super(parent, checkStyle(style) | SWT.DOUBLE_BUFFERED);
		this.selectionListeners = new ArrayList<SelectionListener>();
		this.buttonRenderer = DefaultButtonRenderer.getInstance();
		this.width = this.height = -1;
		addListeners();
	}

	private static int checkStyle(int style) {
		style = checkBits(style, SWT.PUSH, SWT.ARROW, SWT.TOGGLE, 0);
		if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
			return checkBits(style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0);
		}
		if ((style & SWT.ARROW) != 0) {
			return checkBits(style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT);
		}
		return style;
	}

	private static int checkBits(int style, final int int0, final int int1, final int int2, final int int3) {
		final int mask = int0 | int1 | int2 | int3;
		if ((style & mask) == 0) {
			style |= int0;
		}
		if ((style & int0) != 0) {
			style = style & ~mask | int0;
		}
		if ((style & int1) != 0) {
			style = style & ~mask | int1;
		}
		if ((style & int2) != 0) {
			style = style & ~mask | int2;
		}
		if ((style & int3) != 0) {
			style = style & ~mask | int3;
		}
		return style;
	}

	private void addListeners() {
		final Listener listener = new Listener() {

			@Override
			public void handleEvent(final Event event) {
				switch (event.type) {
					case SWT.MouseEnter:
						OButton.this.hover = true;
						redraw();
						update();
						break;
					case SWT.MouseExit:
						OButton.this.hover = false;
						redraw();
						update();
						break;
					case SWT.MouseDown:
						OButton.this.clicked = true;
						OButton.this.setData(IS_BUTTON_PRESSED, "*");
						redraw();
						update();
						break;
					case SWT.MouseUp:
						if (OButton.this.getData(IS_BUTTON_PRESSED) == null) {
							return;
						}
						OButton.this.setData(IS_BUTTON_PRESSED, null);
						OButton.this.clicked = false;
						OButton.this.selected = !OButton.this.selected;
						redraw();
						update();
						fireSelectionEvent();
						break;
					case SWT.Paint:
						handlePaintEvent(event);
						break;
					case SWT.KeyDown:
						if (event.keyCode == SWT.TAB) {
							if (event.stateMask == SWT.SHIFT) {
								OButton.this.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
							} else {
								OButton.this.traverse(SWT.TRAVERSE_TAB_NEXT);
							}
						}
						break;
					case SWT.Dispose:
						OButton.this.buttonRenderer.dispose();
						break;

				}

			}
		};

		final int[] events = new int[] { SWT.MouseEnter, SWT.MouseExit, SWT.MouseDown, SWT.MouseUp, SWT.Paint, SWT.Dispose };
		for (final int event : events) {
			addListener(event, listener);
		}
	}

	protected void fireSelectionEvent() {
		final Event event = new Event();
		event.widget = this;
		event.display = getDisplay();
		event.type = SWT.Selection;
		for (final SelectionListener selectionListener : this.selectionListeners) {
			selectionListener.widgetSelected(new SelectionEvent(event));
		}
	}

	private void handlePaintEvent(final Event event) {
		if (!isEnabled()) {
			this.buttonRenderer.drawButtonWhenDisabled(event.gc, this);
			return;
		}

		if (this.clicked) {
			this.buttonRenderer.drawButtonWhenClicked(event.gc, this);
			return;
		}

		if (this.hover) {
			this.buttonRenderer.drawButtonWhenMouseHover(event.gc, this);
			return;
		}

		final boolean isToggleButton = (getStyle() & SWT.TOGGLE) == SWT.TOGGLE;
		if (isToggleButton && this.selected) {
			this.buttonRenderer.drawButtonWhenSelected(event.gc, this);
			return;
		}

		this.buttonRenderer.drawButton(event.gc, this);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when the control is selected by the user, by sending it one of the messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the control is selected by the user. <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 * 
	 * @param listener the listener which should be notified
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(final SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.selectionListeners.add(listener);
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
	 */
	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		checkWidget();
		final Point computedSize = this.buttonRenderer.computeSize(this, wHint, hHint, changed);

		if (wHint != SWT.DEFAULT) {
			computedSize.x = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			computedSize.y = hHint;
		}
		setWidth(computedSize.x);
		setHeight(computedSize.y);
		return computedSize;
	}

	/**
	 * Returns a value which describes the position of the text or image in the receiver. The value will be one of <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code> unless the receiver is an <code>ARROW</code> button, in which case, the
	 * alignment will indicate the direction of the arrow (one of <code>LEFT</code>, <code>RIGHT</code>, <code>UP</code> or <code>DOWN</code>).
	 * 
	 * @return the alignment
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public int getAlignment() {
		checkWidget();
		return this.alignment;
	}

	/**
	 * @return the associated button renderer
	 */
	public ButtonRenderer getButtonRenderer() {
		checkWidget();
		return this.buttonRenderer;
	}

	/**
	 * Returns the whole height of the widget.
	 * 
	 * @return the receiver's height
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public int getHeight() {
		checkWidget();
		if (this.height == -1) {
			return this.computeSize(SWT.DEFAULT, SWT.DEFAULT, false).y;
		}
		return this.height;
	}

	/**
	 * Returns the receiver's image if it has one, or null if it does not.
	 * 
	 * @return the receiver's image
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public Image getImage() {
		checkWidget();
		return this.image;
	}

	/**
	 * Returns <code>true</code> if the receiver is selected, and false otherwise.
	 * 
	 * @return the selection state
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getSelection() {
		checkWidget();
		return this.selected;
	}

	/**
	 * Returns the receiver's text, which will be an empty string if it has never been set or if the receiver is an <code>ARROW</code> button.
	 * 
	 * @return the receiver's text
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public String getText() {
		checkWidget();
		return this.text;
	}

	/**
	 * Returns the whole width of the widget.
	 * 
	 * @return the receiver's height
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public int getWidth() {
		checkWidget();
		if (this.width == -1) {
			return computeSize(SWT.DEFAULT, SWT.DEFAULT, false).x;
		}
		return this.width;
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified when the control is selected by the user.
	 * 
	 * @param listener the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(final SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.selectionListeners.remove(listener);
	}

	/**
	 * Controls how text, images and arrows will be displayed in the receiver. The argument should be one of <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code> unless the receiver is an <code>ARROW</code> button, in which case, the argument
	 * indicates the direction of the arrow (one of <code>LEFT</code>, <code>RIGHT</code>, <code>UP</code> or <code>DOWN</code>).
	 * 
	 * @param alignment the new alignment
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void setAlignment(final int alignment) {
		checkWidget();
		if (alignment != SWT.LEFT && alignment != SWT.RIGHT && alignment != SWT.CENTER) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.alignment = alignment;
		redraw();
		update();
	}

	/**
	 * @param buttonRenderer the button renderer to set
	 */
	public void setButtonRenderer(final ButtonRenderer buttonRenderer) {
		this.buttonRenderer = buttonRenderer;
	}

	/**
	 * Sets the height of the receiver.
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative number will cause that value to be set to zero instead.
	 * </p>
	 * 
	 * @param height the new width
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void setHeight(final int height) {
		checkWidget();
		this.height = Math.max(height, 0);
		redraw();
		update();
	}

	/**
	 * Sets the receiver's image to the argument, which may be <code>null</code> indicating that no image should be displayed.
	 * 
	 * @param image the image to display on the receiver (may be <code>null</code>)
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void setImage(final Image image) {
		checkWidget();
		if (image != null && image.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.image = image;
		this.buttonRenderer.createDisabledImage();
		redraw();
		update();
	}

	/**
	 * Sets the selection state of the receiver, if it is of type <code>TOGGLE</code>.
	 * 
	 * @param selected the new selection state
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(final boolean selected) {
		checkWidget();
		this.selected = selected;
		redraw();
		update();
	}

	/**
	 * Sets the receiver's text.
	 * 
	 * @param string the new text
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the text is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void setText(final String string) {
		checkWidget();
		this.text = string;
		redraw();
		update();
	}

	/**
	 * Sets the width of the receiver.
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative number will cause that value to be set to zero instead.
	 * </p>
	 * 
	 * @param width the new width
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void setWidth(final int width) {
		checkWidget();
		this.width = Math.max(0, width);
		redraw();
		update();
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		redraw();
		update();
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setSize(int, int)
	 */
	@Override
	public void setSize(final int width, final int height) {
		super.setSize(width, height);
		redraw();
		update();
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setSize(org.eclipse.swt.graphics.Point)
	 */
	@Override
	public void setSize(final Point size) {
		super.setSize(size);
		redraw();
		update();
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean visible) {
		super.setVisible(visible);
		redraw();
		update();
	}

}
