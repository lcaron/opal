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
package org.mihalis.opal.imageSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
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
 * Instances of this class are controls that allow the user to select images.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * Work inspired by Romain Guy's work
 * (http://www.curious-creature.org/2005/07/09/a-music-shelf-in-java2d/)
 */
public class ImageSelector extends Canvas {
	private List<ISItem> items;
	private List<ISItem> originalItems;
	private Font font;
	private static final int DEFAULT_WIDTH = 148;
	private int maxItemWidth = DEFAULT_WIDTH;
	private int index = -1;
	private double sigma;
	private double rho;
	private double expMultiplier;
	private double expMember;
	private float spacing = 0.4f;
	private Color gradientStart;
	private Color gradientEnd;
	private double animationStep = -1d;
	private static final int TIMER_INTERVAL = 50;
	private int pageIncrement = 5;

	private Image cachedImage;
	private GC cachedGC;

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
	public ImageSelector(final Composite parent, final int style) {
		super(parent, style | SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);
		final Font defaultFont = new Font(getDisplay(), "Lucida Sans", 24, SWT.NONE);
		font = defaultFont;
		SWTGraphicUtil.addDisposer(this, defaultFont);

		setSigma(0.5);
		gradientStart = getDisplay().getSystemColor(SWT.COLOR_BLACK);
		gradientEnd = SWTGraphicUtil.getDefaultColor(this, 110, 110, 110);

		addListeners();
		SWTGraphicUtil.addDisposer(this, cachedGC);
		SWTGraphicUtil.addDisposer(this, cachedImage);
	}

	private void addListeners() {
		addKeyListener();
		addMouseListeners();

		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				ImageSelector.this.paintControl(e);
			}
		});

		addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (cachedGC == null) {
					return;
				}
				cachedGC.dispose();
				cachedImage.dispose();
				cachedImage = new Image(getDisplay(), getClientArea());
				cachedGC = new GC(cachedImage);
				cachedGC.setAntialias(SWT.ON);
			}
		});
	}

	/**
	 * Add the key listener
	 */
	private void addKeyListener() {
		this.addKeyListener(new KeyAdapter() {
			/**
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
			 */
			@Override
			public void keyReleased(final KeyEvent e) {
				switch (e.keyCode) {
				case SWT.ARROW_LEFT:
				case SWT.ARROW_UP:
					scrollAndAnimateBy(-1);
					break;
				case SWT.ARROW_RIGHT:
				case SWT.ARROW_DOWN:
					scrollAndAnimateBy(1);
					break;
				case SWT.HOME:
					scrollBy(-1 * index);
					break;
				case SWT.END:
					scrollBy(index);
					break;
				case SWT.PAGE_UP:
					scrollBy(-1 * pageIncrement);
					break;
				case SWT.PAGE_DOWN:
					scrollBy(pageIncrement);
					break;
				}
			}
		});
	}

	/**
	 * Add mouse listeners
	 */
	private void addMouseListeners() {
		addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(final MouseEvent e) {
				for (final ISItem item : items) {
					if (item.getUpperLeftCorner() != null && item.getLowerRightCorner() != null && e.x >= item.getUpperLeftCorner().x && e.x <= item.getLowerRightCorner().x && e.y >= item.getUpperLeftCorner().y
							&& e.y <= item.getLowerRightCorner().y) {
						setCursor(getDisplay().getSystemCursor(SWT.CURSOR_HAND));
						return;
					}
				}
				setCursor(getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
			}
		});

		addMouseListener(new MouseAdapter() {
			/**
			 * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
			 */
			@Override
			public void mouseUp(final MouseEvent e) {
				for (final ISItem item : items) {
					if (item.getUpperLeftCorner() != null && item.getLowerRightCorner() != null && e.x >= item.getUpperLeftCorner().x && e.x <= item.getLowerRightCorner().x && e.y >= item.getUpperLeftCorner().y
							&& e.y <= item.getLowerRightCorner().y) {
						scrollAndAnimateBy(originalItems.indexOf(item) - index);
						return;
					}
				}
			}
		});

		addListener(SWT.MouseWheel, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				scrollBy(-1 * event.count);
			}
		});
	}

	/**
	 * Set the sigma value for the gaussian curve
	 *
	 * @param sigma new sigma parameter
	 */
	public void setSigma(final double sigma) {
		this.sigma = sigma;
		rho = 1.0;
		computeEquationParts();
		rho = computeModifierUnbounded(0.0);
		computeEquationParts();
		redraw();
	}

	/**
	 * Compute the value of the modifier. The value is bounded between -1 and +1
	 *
	 * @param x input value
	 * @return the value of the modifier between -1 and +1
	 */
	private double computeModifierBounded(final double x) {
		double result = computeModifierUnbounded(x);
		if (result > 1.0) {
			result = 1.0;
		} else if (result < -1.0) {
			result = -1.0;
		}
		return result;
	}

	/**
	 * Compute the value of the modifier
	 *
	 * @param x input value
	 * @return the value of the function
	 */
	private double computeModifierUnbounded(final double x) {
		return expMultiplier * Math.exp(-x * x / expMember);
	}

	/**
	 * Computer both members of the equation
	 */
	private void computeEquationParts() {
		expMultiplier = Math.sqrt(2.0 * Math.PI) / sigma / rho;
		expMember = 4.0 * sigma * sigma;
	}

	/**
	 * Draw the widget
	 *
	 * @param e the paintEvent
	 */
	private void paintControl(final PaintEvent e) {

		if (cachedImage == null) {
			cachedImage = new Image(getDisplay(), getClientArea());
			cachedGC = new GC(cachedImage);
			cachedGC.setAntialias(SWT.ON);
		}

		// Draw gradient
		drawBackground();

		// Draw the items
		drawItems();

		// Draw the title
		if (animationStep < 0d) {
			drawTitle();
		}

		// Draw the offscreen buffer to the screen
		e.gc.drawImage(cachedImage, 0, 0);
	}

	/**
	 * Draw the background
	 *
	 * @param gc graphical context
	 */
	private void drawBackground() {
		final Rectangle rect = getClientArea();

		cachedGC.setForeground(gradientStart);
		cachedGC.setBackground(gradientEnd);
		cachedGC.fillGradientRectangle(rect.x, rect.y, rect.width, rect.height / 2, true);

		cachedGC.setForeground(gradientEnd);
		cachedGC.setBackground(gradientStart);
		cachedGC.fillGradientRectangle(rect.x, rect.height / 2, rect.width, rect.height / 2, true);
	}

	/**
	 * Draw the items
	 *
	 * @param gc graphical context
	 */
	private void drawItems() {
		if (animationStep < 0d) {
			items.clear();
			items.addAll(originalItems);
			for (int i = 0; i < items.size(); i++) {
				final ISItem item = items.get(i);
				item.setzPosition((i - index) * spacing);
			}
			Collections.sort(items);
		}

		for (final ISItem item : items) {
			drawItem(item);
		}
	}

	/**
	 * Draw a given item
	 *
	 * @param item item to draw
	 */
	private void drawItem(final ISItem item) {

		final int size = computeSize(item);
		final int centerX = computeZPosition(item);
		final int centerY = getClientArea().height / 2;

		if (size <= 0 || centerX < 0 || centerX > getBounds().width) {
			item.resetCornerToNull();
			return;
		}

		final int alpha = computeAlpha(item);

		final Image newImage = SWTGraphicUtil.createReflectedResizedImage(item.getImage(), size, size);
		cachedGC.setAlpha(alpha);

		final int x = centerX - newImage.getBounds().width / 2;
		final int y = centerY - newImage.getBounds().height / 2;

		cachedGC.drawImage(newImage, x, y);

		item.setUpperLeftCorner(x, y);
		item.setLowerRightCorner(x + newImage.getBounds().width, (int) (y + newImage.getBounds().height / 1.5));

		newImage.dispose();
	}

	/**
	 * Compute the z position for a given item
	 *
	 * @param item item
	 * @return the z position of the item
	 */
	private int computeZPosition(final ISItem item) {
		final int totalWidth = getClientArea().width / 2;
		final int centerX = getClientArea().width / 2;
		return (int) (centerX + item.getzPosition() * totalWidth);
	}

	/**
	 * Compute size for a given item
	 *
	 * @param item item
	 * @return the size of the item
	 */
	private int computeSize(final ISItem item) {
		return (int) (computeModifierBounded(item.getzPosition()) * maxItemWidth);
	}

	/**
	 * Compute the alpha value of a given item
	 *
	 * @param item item
	 * @return the alpha value of the item
	 */
	private int computeAlpha(final ISItem item) {
		return (int) (255 - 150 * Math.abs(item.getzPosition()));
	}

	/**
	 * Draw the title under the selected item
	 *
	 * @param gc graphical context
	 */
	private void drawTitle() {
		final String title = originalItems.get(index).getText();
		if (title == null || title.trim().equals("")) {
			return;
		}
		cachedGC.setFont(getFont());
		final Point textSize = cachedGC.stringExtent(title);

		cachedGC.setFont(getFont());
		cachedGC.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		cachedGC.setAlpha(255);

		final int centerX = getClientArea().width / 2;
		final int centerY = (getClientArea().height + maxItemWidth) / 2;

		cachedGC.drawString(title, centerX - textSize.x / 2, centerY - textSize.y / 2, true);

	}

	/**
	 * Scroll the selected item
	 *
	 * @param increment increment value
	 */
	private void scrollBy(final int increment) {
		index += increment;
		if (index < 0) {
			index = 0;
		}

		if (index >= items.size()) {
			index = items.size() - 1;
		}
		redraw();
	}

	/**
	 * Scroll the selected item with an animation
	 *
	 * @param increment increment value
	 */
	private void scrollAndAnimateBy(final int increment) {
		if (increment == 0 || index == 0 && increment < 0 || index == items.size() - 1 && increment > 0) {
			return;
		}

		final double step = Math.abs(increment) / (300d / TIMER_INTERVAL);
		ImageSelector.this.animationStep = step;
		setCursor(getDisplay().getSystemCursor(SWT.CURSOR_WAIT));

		getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				startAnimation(increment, step);
			}

			private void startAnimation(final int increment, final double step) {
				items.clear();
				items.addAll(originalItems);
				for (int i = 0; i < items.size(); i++) {
					final ISItem item = items.get(i);
					item.setzPosition((i - index + animationStep * (increment > 0 ? -1d : 1d)) * spacing);
				}
				Collections.sort(items);
				if (!isDisposed()) {
					redraw();
				}

				animationStep += step;
				if (animationStep >= 1d) {
					animationStep = -1d;
					index += increment;
					setCursor(getDisplay().getSystemCursor(SWT.CURSOR_ARROW));
				} else {
					if (!isDisposed()) {
						getDisplay().timerExec(TIMER_INTERVAL, this);
					}
				}
			}
		});

	}

	/**
	 * @return the items displayed by this widget
	 */
	public List<ISItem> getItems() {
		return originalItems;
	}

	/**
	 * @param items the items that are displayed in this widget to set
	 */
	public void setItems(final List<ISItem> items) {
		this.items = new ArrayList<ISItem>(items);
		originalItems = items;
		index = this.items.size() / 2;
		redraw();
	}

	/**
	 * @return the font used for the title
	 */
	@Override
	public Font getFont() {
		return font;
	}

	/**
	 * @param font the font used for the title to set
	 */
	@Override
	public void setFont(final Font font) {
		this.font = font;
		redraw();
	}

	/**
	 * @return the index of the selected image
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index of the selected image to set
	 */
	public void setIndex(final int index) {
		this.index = index;
		redraw();
	}

	/**
	 * @return the maximum items width
	 */
	public int getMaxItemWidth() {
		return maxItemWidth;
	}

	/**
	 * @param maxItemWidth the the maximum items width to set
	 */
	public void setMaxItemWidth(final int maxItemWidth) {
		this.maxItemWidth = maxItemWidth;
		redraw();
	}

	/**
	 * @return the sigma value
	 */
	public double getSigma() {
		return sigma;
	}

	/**
	 * @return the spacing between 2 items
	 */
	public float getSpacing() {
		return spacing;
	}

	/**
	 * @param spacing the the spacing between 2 items to set
	 */
	public void setSpacing(final float spacing) {
		this.spacing = spacing;
		redraw();
	}

	/**
	 * @return the gradient start color
	 */
	public Color getGradientStart() {
		return gradientStart;
	}

	/**
	 * @param gradientStart the the gradient start color to set
	 */
	public void setGradientStart(final Color gradientStart) {
		this.gradientStart = gradientStart;
		redraw();
	}

	/**
	 * @return the the gradient end color
	 */
	public Color getGradientEnd() {
		return gradientEnd;
	}

	/**
	 * @param gradientEnd the the gradient end color to set
	 */
	public void setGradientEnd(final Color gradientEnd) {
		this.gradientEnd = gradientEnd;
		redraw();
	}

	/**
	 * @return the page increment when the user uses PgUp and PgDown
	 */
	public int getPageIncrement() {
		return pageIncrement;
	}

	/**
	 * @param pageIncrement the page increment to set
	 */
	public void setPageIncrement(final int pageIncrement) {
		this.pageIncrement = pageIncrement;
	}

}
