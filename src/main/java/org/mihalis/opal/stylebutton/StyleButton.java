/*******************************************************************************
 * Copyright (c) 2011 Luis Carlos Moreira da Costa.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luis Carlos Moreira da Costa (tcljava at gmail dot com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.stylebutton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * A StyleButton is a class which wraps an SWT Button to create a widget that
 * acts as much like a native Button as possible while adding the following
 * features:
 * <ul>
 * <li>The appearance of Label when the mouse is not over it and it does not
 * have the focus or selection (if style is SWT.TOGGLE).</li>
 * <li>Can fit seemlessly into a larger visual piece - simple set the image to
 * that of its background and adjust the image's offset if necessary.</li>
 * <li>Can draw polygons and ovals.</li>
 * <li>Can center or otherwise align its visual display (text, image, polygon or
 * oval).</li>
 * </ul>
 */
public final class StyleButton extends Composite {

	// Style constants to indicate draw button.
	public static final int OK = 1 << 12;
	public static final int CANCEL = 2 << 12;
	public static final int ARROW_LEFT = 1 << 4;
	public static final int ARROW_RIGHT = 2 << 4;
	public static final int ARROW_UP = 3 << 6;
	public static final int ARROW_DOWN = 4 << 6;
	public static final int ADD = 1 << 2;
	public static final int SUBTRACT = 2 << 2;

	// Style default buttons.
	private static final int[] DRAW_BUTTON_OK = { 2, 6, 5, 9, 10, 3, 9, 2, 5, 7, 3, 5 };
	private static final int[] DRAW_BUTTON_CANCEL = { 0, 1, 3, 4, 0, 7, 1, 8, 4, 5, 7, 8, 8, 7, 5, 4, 8, 1, 7, 0, 4, 3, 1, 0 };
	private static final int[] DRAW_BUTTON_ARROW_LEFT = { 9, 0, 4, 5, 9, 10 };
	private static final int[] DRAW_BUTTON_ARROW_RIGHT = { 2, 0, 7, 5, 2, 10 };
	private static final int[] DRAW_BUTTON_ARROW_UP = { 10, 8, 5, 3, 0, 8 };
	private static final int[] DRAW_BUTTON_ARROW_DOWN = { 10, 2, 5, 7, 0, 2 };
	private static final int[] DRAW_BUTTON_ADD = { 2, 4, 4, 4, 4, 2, 5, 2, 5, 4, 7, 4, 7, 5, 5, 5, 5, 7, 4, 7, 4, 5, 2, 5 };
	private static final int[] DRAW_BUTTON_SUBTRACT = { 2, 4, 7, 4, 7, 5, 2, 5 };

	// True if the platform is win32, false otherwise.
	private static final boolean WIN32 = "win32".equalsIgnoreCase(SWT.getPlatform());

	private Button button = null;
	private String text = null;
	private Image image = null;
	private int[] points = null;
	private Color fillColor = null;

	private int marginTop = 5;
	private int marginBottom = 5;
	private int marginLeft = 5;
	private int marginRight = 5;
	private int xAlign = 0;
	private int yAlign = 0;
	private int ix = 0;
	private int iy = 0;

	private boolean square = false;

	private final Listener filter = new Listener() {
		@Override
		public void handleEvent(final Event event) {
			if (isDisposed()) {
				return;
			}
			if (StyleButton.this.getShell() == ((Control) event.widget).getShell()) {
				if (SWT.MouseMove == event.type) {
					if (event.widget.equals(StyleButton.this)) {
						StyleButton.this.button.setVisible(true);
					} else if (StyleButton.this.button.isVisible() && !StyleButton.this.button.getSelection() && !event.widget.equals(StyleButton.this.button)) {
						StyleButton.this.button.setVisible(false);
					}
				}
			}
		}
	};

	/**
	 * Constructor
	 * 
	 * The composite will ignore all style bits and be constructed with
	 * SWT.NONE.
	 * <p>
	 * The button will only recognize types of either SWT.PUSH or SWT.TOGGLE.
	 * <p>
	 * Other styles: Style.OK draws a check mark - green if fillColor is not
	 * given. Style.CANCEL draws an "X" - red if fillColor is not given.
	 * Style.ADD draws a plus mark. Style.SUBTRACT draws a minus mark.
	 * Style.ARROW_UP, Style.ARROW_DOWN, Style.ARROW_LEFT or Style.ARROW_RIGHT
	 * draws an arrow.
	 * </p>
	 * 
	 * @param parent Composite
	 * @param style int
	 */
	public StyleButton(final Composite parent, final int style) {
		this(parent, style, null);
	}

	/**
	 * Constructor with fill color
	 * 
	 * @param parent Composite
	 * @param style int
	 * @param fillColor Color
	 */
	public StyleButton(final Composite parent, final int style, Color fillColor) {
		super(parent, WIN32 ? SWT.DOUBLE_BUFFERED : SWT.NONE); // GTK does not
																// seem to need
																// it...
		setLayout(new FillLayout());
		int bStyle = 0;
		if ((style & SWT.TOGGLE) != 0) {
			bStyle = SWT.TOGGLE;
		} else {
			bStyle = SWT.PUSH;
		}
		this.button = new Button(this, bStyle | (WIN32 ? SWT.DOUBLE_BUFFERED : SWT.NONE)); // GTK
																							// does
																							// not
																							// seem
																							// to
																							// need
																							// it...
		this.button.setBackground(getBackground());
		this.button.setVisible(false);
		setBackground(parent.getBackground());
		if ((style & ARROW_LEFT) != 0) {
			setPolygon(DRAW_BUTTON_ARROW_LEFT, fillColor != null ? fillColor : getForeground());
		} else if ((style & ARROW_RIGHT) != 0) {
			setPolygon(DRAW_BUTTON_ARROW_RIGHT, fillColor != null ? fillColor : getForeground());
		} else if ((style & ARROW_UP) != 0) {
			setPolygon(DRAW_BUTTON_ARROW_UP, fillColor != null ? fillColor : getForeground());
		} else if ((style & ARROW_DOWN) != 0) {
			setPolygon(DRAW_BUTTON_ARROW_DOWN, fillColor != null ? fillColor : getForeground());
		} else if ((style & ADD) != 0) {
			setPolygon(DRAW_BUTTON_ADD, fillColor != null ? fillColor : getForeground());
		} else if ((style & SUBTRACT) != 0) {
			setPolygon(DRAW_BUTTON_SUBTRACT, fillColor != null ? fillColor : getForeground());
		} else if ((style & OK) != 0) {
			setPolygon(DRAW_BUTTON_OK, fillColor != null ? fillColor : (fillColor = getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN)));
			setForeground(fillColor);
		} else if ((style & CANCEL) != 0) {
			setPolygon(DRAW_BUTTON_CANCEL, fillColor != null ? fillColor : (fillColor = getDisplay().getSystemColor(SWT.COLOR_DARK_RED)));
			setForeground(fillColor);
		}
		this.button.addListener(SWT.Paint, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (SWT.Paint == event.type && StyleButton.this.button.isVisible()) {
					paintControl(event);
				}
			}
		});
		addListener(SWT.Paint, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (SWT.Paint == event.type && !StyleButton.this.button.isVisible()) {
					paintControl(event);
				}
			}
		});
		getDisplay().addFilter(SWT.MouseMove, this.filter);
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				if (!getDisplay().isDisposed()) {
					getDisplay().removeFilter(SWT.MouseMove, StyleButton.this.filter);
				}
				disposeFillColor();
				disposeImage();
			}
		});
	}

	@Override
	public void addListener(final int eventType, final Listener listener) {
		this.button.addListener(eventType, listener);
		super.addListener(eventType, listener);
	}

	/**
	 * Add selection listener
	 * 
	 * @param listener SelectionListener
	 */
	public void addSelectionListener(final SelectionListener listener) {
		this.button.addSelectionListener(listener);
	}

	@Override
	public Point computeSize(int wHint, int hHint, final boolean changed) {
		checkWidget();
		if (wHint != SWT.DEFAULT && wHint < 0) {
			wHint = 0;
		}
		if (hHint != SWT.DEFAULT && hHint < 0) {
			hHint = 0;
		}
		Point size = null;
		if (this.text != null) {
			final GC gc = new GC(this);
			final Point tSize = gc.stringExtent(this.text);
			gc.dispose();
			size = new Point(tSize.x, tSize.y);
		} else if (this.image != null) {
			final Rectangle r = this.image.getBounds();
			size = new Point(r.width, r.height);
		} else if (this.points != null) {
			if (this.points.length > 2) {
				int minX = this.points[0];
				int maxX = this.points[0];
				int minY = this.points[1];
				int maxY = this.points[1];
				for (int i = 2; i < this.points.length - 1; i++) {
					minX = Math.min(minX, this.points[i]);
					maxX = Math.max(maxX, this.points[i]);
					minY = Math.min(minY, this.points[(i + 1)]);
					maxY = Math.max(maxY, this.points[(i + 1)]);
				}
				size = new Point((maxX - minX), (maxY - minY));
			} else {
				size = new Point(this.points[0], this.points[1]);
			}
		} else {
			size = new Point(10, 10);
		}
		size.x += this.marginLeft + this.marginRight;
		size.y += this.marginTop + this.marginBottom;
		if (wHint != SWT.DEFAULT) {
			size.x = Math.min(size.x, wHint);
		}
		if (hHint != SWT.DEFAULT) {
			size.y = Math.min(size.y, hHint);
		}
		if (this.square) {
			size.x = size.y = Math.max(size.x, size.y);
		}
		return size;
	}

	@Override
	public void dispose() {
		if (!getDisplay().isDisposed()) {
			getDisplay().removeFilter(SWT.MouseMove, this.filter);
		}
		disposeFillColor();
		disposeImage();
		super.dispose();
	}

	/**
	 * Dispose fill color
	 */
	private void disposeFillColor() {
		if (this.fillColor != null && !this.fillColor.isDisposed()) {
			this.fillColor.dispose();
		}
		this.fillColor = null;
	}

	/**
	 * Dispose image
	 */
	private void disposeImage() {
		if (this.image != null && !this.image.isDisposed()) {
			this.image.dispose();
		}
		this.image = null;
	}

	/**
	 * Draw control
	 * 
	 * @param e Event
	 */
	private void drawControl(final Event e) {
		if (this.text != null) {
			e.gc.setTextAntialias(SWT.ON);
			final Rectangle r = getClientArea();
			final Point size = e.gc.textExtent(this.text);
			int x = 0;
			if (this.xAlign == SWT.LEFT) {
				x = this.marginLeft;
			} else if (this.xAlign == SWT.RIGHT) {
				x = r.width - size.x - this.marginRight;
			} else { // CENTERED / Default
				x = (r.width - size.x) / 2;
			}
			int y = 0;
			if (this.yAlign == SWT.TOP) {
				y = this.marginTop;
			} else if (this.yAlign == SWT.BOTTOM) {
				y = r.height - size.y - this.marginBottom;
			} else { // CENTERED / Default
				y = (r.height - size.y) / 2;
			}
			if (WIN32 && e.widget == this.button) {
				final Image img = new Image(e.display, e.width, e.height);
				e.gc.copyArea(img, e.x, e.y);
				final Pattern p = new Pattern(e.display, img);
				e.gc.setBackgroundPattern(p);
				e.gc.drawText(this.text, x, y);
				img.dispose();
				p.dispose();
			} else {
				e.gc.drawString(this.text, x, y);
			}
		} else if (this.image != null) {
			final Rectangle r = getClientArea();
			final Rectangle size = this.image.getBounds();
			if (e.widget instanceof Button) {
				final int x = r.width - this.marginLeft - this.marginRight;
				final int y = r.height - this.marginTop - this.marginBottom;
				final Image img = new Image(getDisplay(), x, y);
				final GC gc = new GC(img);
				gc.drawImage(this.image, -(size.width - r.width) / 2 - this.marginLeft - this.ix, -(size.height - r.height) / 2 - this.marginTop - this.iy);
				e.gc.drawImage(img, this.marginLeft, this.marginTop);
				gc.dispose();
				img.dispose();
			} else {
				e.gc.drawImage(this.image, (r.width - size.width) / 2 - this.ix, (r.height - size.height) / 2 - this.iy);
			}
		} else if (this.points != null && this.points.length > 0) {
			e.gc.setAntialias(SWT.ON);
			final Rectangle r = getClientArea();
			int minX = this.points.length > 2 ? this.points[0] : 0;
			int maxX = this.points[0];
			int minY = this.points.length > 2 ? this.points[1] : 0;
			int maxY = this.points[1];
			for (int i = 2; i < this.points.length - 1; i++) {
				minX = Math.min(minX, this.points[i]);
				maxX = Math.max(maxX, this.points[i]);
				minY = Math.min(minY, this.points[i + 1]);
				maxY = Math.max(maxY, this.points[i + 1]);
			}
			double x = 0.0d;
			if (this.xAlign == SWT.LEFT) {
				x = this.marginLeft;
			} else if (this.xAlign == SWT.RIGHT) {
				x = r.width - maxX - this.marginRight;
			} else { // CENTERED / Default
				x = (r.width - (maxX - minX)) / 2;
			}
			double y = 0.0d;
			if (this.yAlign == SWT.TOP) {
				y = this.marginTop;
			} else if (this.yAlign == SWT.BOTTOM) {
				y = r.height - maxY - this.marginBottom;
			} else { // CENTERED / Default
				y = (r.height - (maxY - minY)) / 2;
			}
			final int[] data = new int[this.points.length];
			for (int i = 0; i < this.points.length; i += 2) {
				data[i] = this.points[i] + (int) x - minX;
			}
			for (int i = 1; i < data.length; i += 2) {
				data[i] = this.points[i] + (int) y - minY;
			}
			if (this.fillColor != null && !this.fillColor.isDisposed()) {
				e.gc.setBackground(this.fillColor);
				if (this.points.length > 2) {
					e.gc.fillPolygon(data);
				} else {
					e.gc.fillOval((int) x, (int) y, this.points[0], this.points[1]);
				}
			}
			if (this.points.length > 2) {
				e.gc.drawPolygon(data);
			} else {
				e.gc.drawOval((int) x, (int) y, this.points[0], this.points[1]);
			}
		}
	}

	/**
	 * Get button
	 * 
	 * @return Button
	 */
	public Button getButton() {
		return this.button;
	}

	/**
	 * Get selection
	 * 
	 * @return boolean
	 */
	public boolean getSelection() {
		return this.button.getSelection();
	}

	/**
	 * Get text
	 * 
	 * @return String
	 */
	public String getText() {
		return this.text;
	}

	@Override
	public boolean isDisposed() {
		return super.isDisposed() || this.button != null && this.button.isDisposed();
	}

	/**
	 * Verify is square
	 * 
	 * @return boolean
	 */
	public boolean isSquare() {
		return this.square;
	}

	/**
	 * Paint of the control
	 * 
	 * @param e Event
	 */
	private void paintControl(final Event e) {
		if (e.widget == this.button) {
			final Point point = this.button.toControl(getDisplay().getCursorLocation());
			if (this.button.getSelection() || this.button.getBounds().contains(point)) {
				if (!this.button.isVisible()) {
					this.button.setVisible(true);
				}
				drawControl(e);
			} else {
				if (this.button.isVisible()) {
					this.button.setVisible(false);
				}
				redraw();
				update();
			}
		} else {
			drawControl(e);
		}
	}

	@Override
	public void removeListener(final int eventType, final Listener handler) {
		super.removeListener(eventType, handler);
		this.button.removeListener(eventType, handler);
	}

	/**
	 * Remove selection listener
	 * 
	 * @param listener SelectionListener
	 */
	public void removeSelectionListener(final SelectionListener listener) {
		this.button.removeSelectionListener(listener);
	}

	/**
	 * Set alignment
	 * 
	 * @param x int
	 * @param y int
	 */
	public void setAlignment(final int x, final int y) {
		this.xAlign = x;
		this.yAlign = y;
	}

	@Override
	public void setBackground(final Color color) {
		this.button.setBackground(color);
		super.setBackground(color);
	}

	@Override
	public void setData(final Object data) {
		this.button.setData(data);
		super.setData(data);
	}

	@Override
	public void setData(final String key, final Object value) {
		this.button.setData(key, value);
		super.setData(key, value);
	}

	/**
	 * Set fill color
	 * 
	 * @param fillColor Color
	 */
	public void setFillColor(final Color fillColor) {
		disposeFillColor();
		this.fillColor = new Color(getDisplay(), fillColor.getRGB());
	}

	@Override
	public void setForeground(final Color color) {
		this.button.setForeground(color);
		super.setForeground(color);
	}

	/**
	 * Set image with x and y
	 * 
	 * @param image Image
	 * @param x int
	 * @param y int
	 */
	public void setImage(final Image image, final int x, final int y) {
		this.ix = x;
		this.iy = y;
		setImage(image);
	}

	/**
	 * Set image
	 * 
	 * @param image Image
	 */
	public void setImage(final Image image) {
		this.points = null;
		disposeFillColor();
		this.text = null;
		disposeImage();
		this.image = new Image(getDisplay(), image.getImageData());
		redraw();
		update();
	}

	/**
	 * Set margins
	 * 
	 * @param marginWidth int
	 * @param marginHeight int
	 */
	public void setMargins(final int marginWidth, final int marginHeight) {
		setMargins(marginWidth, marginWidth, marginHeight, marginHeight);
	}

	/**
	 * Set margins
	 * 
	 * @param left int
	 * @param right int
	 * @param top int
	 * @param bottom int
	 */
	public void setMargins(final int left, final int right, final int top, final int bottom) {
		if (left >= 0) {
			this.marginLeft = left;
		}
		if (right >= 0) {
			this.marginRight = right;
		}
		if (top >= 0) {
			this.marginTop = top;
		}
		if (bottom >= 0) {
			this.marginBottom = bottom;
		}
	}

	/**
	 * Set polygon
	 * 
	 * @param points int[]
	 */
	public void setPolygon(final int[] points) {
		setPolygon(points, (this.fillColor != null ? this.fillColor : getForeground()));
	}

	/**
	 * Set polygon
	 * 
	 * @param points int[]
	 * @param fillColor Color
	 */
	public void setPolygon(final int[] points, final Color fillColor) {
		if (points.length < 2 || points.length % 2 != 0) {
			return;
		}
		disposeImage();
		this.text = null;
		this.points = points;
		this.fillColor = fillColor;
		redraw();
		update();
	}

	/**
	 * Set selection
	 * 
	 * @param selected boolean
	 */
	public void setSelection(final boolean selected) {
		if (this.button.getVisible() != selected) {
			this.button.setVisible(selected);
		}
		if (this.button.getSelection() != selected) {
			this.button.setSelection(selected);
		}
	}

	/**
	 * Set square if parameter equal is true, the x and y sizes of this
	 * StyleButton will be forced equal, thus drawing a square button.
	 * 
	 * @param equal boolean
	 */
	public void setSquare(final boolean equal) {
		this.square = equal;
	}

	/**
	 * Set text
	 * 
	 * @param text String
	 */
	public void setText(final String text) {
		disposeImage();
		this.points = null;
		disposeFillColor();
		this.text = text;
		if (this.button.isVisible()) {
			this.button.redraw();
			this.button.update();
		} else {
			redraw();
			update();
		}
	}

	@Override
	public void setToolTipText(final String string) {
		this.button.setToolTipText(string);
		super.setToolTipText(string);
	}

	/**
	 * Selected default widget
	 * 
	 * @param e SelectionEvent
	 */
	public void widgetDefaultSelected(final SelectionEvent e) {
		if (!this.button.isVisible()) {
			this.button.setVisible(true);
		}
	}

	/**
	 * Selected widget
	 * 
	 * @param e SelectionEvent
	 */
	public void widgetSelected(final SelectionEvent e) {
		if (!this.button.isVisible()) {
			this.button.setVisible(true);
		}
	}

}