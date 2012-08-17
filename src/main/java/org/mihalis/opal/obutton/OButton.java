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

	public OButton(final Composite parent, final int style) {
		super(parent, checkStyle(style) | SWT.DOUBLE_BUFFERED);
		this.selectionListeners = new ArrayList<SelectionListener>();
		this.buttonRenderer = new DefaultButtonRenderer(this);
		width = height = -1;
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
						redraw();
						update();
						break;
					case SWT.MouseUp:
						OButton.this.clicked = false;
						OButton.this.selected = !OButton.this.selected;
						redraw();
						update();
						fireSelectionEvent();
						break;
					case SWT.Paint:
						onPaint(event);
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

	protected void onPaint(final Event event) {
		if (!isEnabled()) {
			this.buttonRenderer.drawButtonWhenDisabled(event.gc);
			return;
		}

		if (this.clicked) {
			this.buttonRenderer.drawButtonWhenClicked(event.gc);
			return;
		}

		if (this.hover) {
			this.buttonRenderer.drawButtonWhenMouseHover(event.gc);
			return;
		}

		final boolean isToggleButton = (getStyle() & SWT.TOGGLE) == SWT.TOGGLE;
		if (isToggleButton && this.selected) {
			this.buttonRenderer.drawButtonWhenSelected(event.gc);
			return;
		}

		this.buttonRenderer.drawButton(event.gc);
	}

	public void addSelectionListener(final SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.selectionListeners.add(listener);
	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		checkWidget();
		final Point size = buttonRenderer.computeSize(wHint, hHint, changed);
		return new Point(Math.max(size.x, wHint), Math.max(size.y, hHint));
	}

	public int getAlignment() {
		checkWidget();
		return this.alignment;
	}

	/**
	 * @return the buttonRenderer
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
	 *     <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *     <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public int getHeight() {
		checkWidget();
		if (this.height == -1) {
			return this.computeSize(SWT.DEFAULT, SWT.DEFAULT, false).y;
		}
		return this.height;
	}

	public Image getImage() {
		checkWidget();
		return this.image;
	}

	public boolean getSelection() {
		checkWidget();
		return this.selected;
	}

	public String getText() {
		checkWidget();
		return this.text;
	}

	/**
	 * Returns the whole height of the widget.
	 * 
	 * @return the receiver's height
	 * 
	 * @exception SWTException <ul>
	 *     <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *     <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public int getWidth() {
		checkWidget();
		if (this.width == -1) {
			return computeSize(SWT.DEFAULT, SWT.DEFAULT, false).x;
		}
		return width;
	}

	public void removeSelectionListener(final SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.selectionListeners.remove(listener);
	}

	public void setAlignment(final int alignment) {
		checkWidget();
		if (alignment != SWT.LEFT && alignment != SWT.RIGHT && alignment != SWT.CENTER) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.alignment = alignment;
	}

	/**
	 * @param buttonRenderer the buttonRenderer to set
	 */
	public void setButtonRenderer(final ButtonRenderer buttonRenderer) {
		this.buttonRenderer = buttonRenderer;
	}

	/**
	 * Sets the height of the receiver.
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative number will cause that value to be set to zero 
	 * instead.
	 * </p>
	 * @param height the new width
	 * 
	 * @exception SWTException <ul>
	 *     <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *     <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void setHeight(final int height) {
		checkWidget();
		this.height = Math.max(height, 0);
	}

	public void setImage(final Image image) {
		checkWidget();
		if (image != null && image.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.image = image;
	}

	public void setSelection(final boolean selected) {
		checkWidget();
		this.selected = selected;
	}

	public void setText(final String string) {
		checkWidget();
		this.text = string;
	}

	/**
	 * Sets the width of the receiver.
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative number will cause that value to be set to zero 
	 * instead.
	 * </p>
	 * @param width the new width
	 * 
	 * @exception SWTException <ul>
	 *     <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *     <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void setWidth(final int width) {
		checkWidget();
		this.width = Math.max(0, width);
	}
}
