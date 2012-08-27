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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.mihalis.opal.utils.AdvancedPath;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * This class is an abstract button renderer used for default, red, orange, green and purple themes
 */
public abstract class AbstractButtonRenderer implements ButtonRenderer {

	private static final int RADIUS_VALUE = 10;
	private static final Color DISABLED_FONT_COLOR = SWTGraphicUtil.createDisposableColor(119, 119, 119);
	private static final Color DISABLED_SECOND_BACKGROUND_COLOR = SWTGraphicUtil.createDisposableColor(220, 220, 220);
	private static final Color DISABLED_FIRST_BACKGROUND_COLOR = SWTGraphicUtil.createDisposableColor(237, 237, 237);

	private ButtonConfiguration normal, hover, disabled, selected, onclick;
	private GC gc;
	private ButtonConfiguration configuration;
	private int gapOnClic;
	protected OButton parent;
	private Image imageUp;
	private Image imageDown;
	private Image imageLeft;
	private Image imageRight;
	private static final int MARGIN = 5;
	private static final int GAP_ON_CLIC = 2;

	/**
	 * Constructor
	 */
	protected AbstractButtonRenderer() {
		initButtonConfiguration();
		createArrows();
	}

	private void initButtonConfiguration() {
		this.normal = createNormalConfiguration();
		this.hover = createHoverConfiguration();
		this.disabled = createDisabledConfiguration();
		this.selected = createSelectedConfiguration();
		this.onclick = createOnClickConfiguration();
	}

	/**
	 * @return the configuration when the button is not clicked, enabled, not selected, and the mouse is not hover
	 */
	protected ButtonConfiguration createNormalConfiguration() {
		final ButtonConfiguration configuration = new ButtonConfiguration();
		configuration.setCornerRadius(10);
		configuration.setFont(Display.getDefault().getSystemFont()).setFontColor(getFontColor());
		configuration.setGradientDirection(SWT.VERTICAL);
		configuration.setBackgroundColor(getFirstBackgroundColor());
		configuration.setSecondBackgroundColor(getSecondBackgroundColor());
		return configuration;
	}

	/**
	 * @return the font color
	 */
	protected abstract Color getFontColor();

	/**
	 * @return the first background color
	 */
	protected abstract Color getFirstBackgroundColor();

	/**
	 * @return the second background color
	 */
	protected abstract Color getSecondBackgroundColor();

	/**
	 * @return the configuration when the mouse is hover
	 */
	protected ButtonConfiguration createHoverConfiguration() {
		final ButtonConfiguration configuration = new ButtonConfiguration();
		configuration.setCornerRadius(10);
		configuration.setFont(Display.getDefault().getSystemFont()).setFontColor(getFontColor());
		configuration.setGradientDirection(SWT.VERTICAL);
		configuration.setBackgroundColor(getSecondBackgroundColor());
		configuration.setSecondBackgroundColor(getFirstBackgroundColor());
		return configuration;
	}

	/**
	 * @return the configuration when the button is disabled
	 */
	protected ButtonConfiguration createDisabledConfiguration() {
		final ButtonConfiguration configuration = new ButtonConfiguration();
		configuration.setCornerRadius(RADIUS_VALUE);
		configuration.setFont(Display.getDefault().getSystemFont()).setFontColor(DISABLED_FONT_COLOR);
		configuration.setGradientDirection(SWT.VERTICAL);
		configuration.setBackgroundColor(DISABLED_FIRST_BACKGROUND_COLOR);
		configuration.setSecondBackgroundColor(DISABLED_SECOND_BACKGROUND_COLOR);
		return configuration;
	}

	/**
	 * @return the configuration when the button is selected
	 */
	protected ButtonConfiguration createSelectedConfiguration() {
		return createHoverConfiguration();
	}

	/**
	 * @return the configuration when the button is clicked
	 */
	protected ButtonConfiguration createOnClickConfiguration() {
		return createHoverConfiguration();
	}

	private void createArrows() {
		imageUp = new Image(Display.getCurrent(), this.getClass().getClassLoader().getResourceAsStream("images/arrow_up.png"));
		imageDown = new Image(Display.getCurrent(), this.getClass().getClassLoader().getResourceAsStream("images/arrow_down.png"));
		imageLeft = new Image(Display.getCurrent(), this.getClass().getClassLoader().getResourceAsStream("images/arrow_left.png"));
		imageRight = new Image(Display.getCurrent(), this.getClass().getClassLoader().getResourceAsStream("images/arrow_right.png"));

		Display.getCurrent().addListener(SWT.Dispose, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				imageUp.dispose();
				imageDown.dispose();
				imageLeft.dispose();
				imageRight.dispose();
			}
		});
	}

	/**
	 * @see org.mihalis.opal.obutton.ButtonRenderer#dispose()
	 */
	@Override
	public void dispose() {
	}

	/**
	 * @see org.mihalis.opal.obutton.ButtonRenderer#drawButtonWhenMouseHover(org.eclipse.swt.graphics.GC, org.mihalis.opal.obutton.OButton)
	 */
	@Override
	public void drawButtonWhenMouseHover(final GC gc, final OButton parent) {
		this.gc = gc;
		this.configuration = this.hover;
		this.gapOnClic = 0;
		this.parent = parent;
		draw();
	}

	private void draw() {
		this.gc.setAdvanced(true);
		this.gc.setAntialias(SWT.ON);
		drawBackground();
		int xPosition = computeStartingPosition();
		xPosition += drawImage(xPosition);
		if (parent.getText() != null) {
			drawText(xPosition);
		}
	}

	private void drawBackground() {
		createClipping();
		this.gc.setForeground(this.configuration.getBackgroundColor());
		this.gc.setBackground(this.configuration.getSecondBackgroundColor());
		this.gc.fillGradientRectangle(0, this.gapOnClic, parent.getWidth(), parent.getHeight() - GAP_ON_CLIC, this.configuration.getGradientDirection() == SWT.VERTICAL);
		this.gc.setClipping((Rectangle) null);
	}

	private void createClipping() {
		final AdvancedPath path = new AdvancedPath(parent.getDisplay());
		path.addRoundRectangle(0, this.gapOnClic, parent.getWidth(), parent.getHeight() - GAP_ON_CLIC, this.configuration.getCornerRadius(), this.configuration.getCornerRadius());
		this.gc.setClipping(path);
	}

	private int computeStartingPosition() {
		final int widthOfTextAndImage = computeSizeOfTextAndImages().x;
		switch (parent.alignment) {
			case SWT.CENTER:
				return (parent.getWidth() - widthOfTextAndImage) / 2;
			case SWT.RIGHT:
				return parent.getWidth() - widthOfTextAndImage - MARGIN;
			default:
				return MARGIN;
		}
	}

	private int drawImage(final int xPosition) {
		final Image image = extractImage();

		if (image == null) {
			return 0;
		}

		final int yPosition = (parent.getHeight() - image.getBounds().height - GAP_ON_CLIC) / 2;
		this.gc.drawImage(image, xPosition, yPosition + this.gapOnClic);
		return image.getBounds().width + MARGIN;
	}

	private Image extractImage() {
		if ((parent.getStyle() & SWT.ARROW) != 0) {
			if ((parent.getStyle() & SWT.DOWN) != 0) {
				return imageDown;
			}
			if ((parent.getStyle() & SWT.UP) != 0) {
				return imageUp;
			}
			if ((parent.getStyle() & SWT.LEFT) != 0) {
				return imageLeft;
			}
			if ((parent.getStyle() & SWT.RIGHT) != 0) {
				return imageRight;
			}
		}

		if (parent.getImage() == null) {
			return null;
		}

		final Image image;
		if (!parent.isEnabled()) {
			image = new Image(parent.getDisplay(), parent.getImage(), SWT.IMAGE_DISABLE);
			parent.addListener(SWT.Dispose, new Listener() {
				@Override
				public void handleEvent(final Event e) {
					if (!image.isDisposed()) {
						image.dispose();
					}
				}
			});
		} else {
			image = parent.getImage();
		}

		return image;
	}

	private void drawText(final int xPosition) {
		this.gc.setFont(this.configuration.getFont());
		this.gc.setForeground(this.configuration.getFontColor());

		final Point textSize = this.gc.stringExtent(parent.getText());
		final int yPosition = (parent.getHeight() - textSize.y - GAP_ON_CLIC) / 2;

		this.gc.drawText(parent.getText(), xPosition, yPosition + this.gapOnClic, true);
	}

	/**
	 * @see org.mihalis.opal.obutton.ButtonRenderer#drawButtonWhenDisabled(org.eclipse.swt.graphics.GC, org.mihalis.opal.obutton.OButton)
	 */
	@Override
	public void drawButtonWhenDisabled(final GC gc, final OButton parent) {
		this.gc = gc;
		this.configuration = this.disabled;
		this.gapOnClic = 0;
		this.parent = parent;
		draw();
	}

	/**
	 * @see org.mihalis.opal.obutton.ButtonRenderer#drawButtonWhenSelected(org.eclipse.swt.graphics.GC, org.mihalis.opal.obutton.OButton)
	 */
	@Override
	public void drawButtonWhenSelected(final GC gc, final OButton parent) {
		this.gc = gc;
		this.configuration = this.selected;
		this.gapOnClic = 0;
		this.parent = parent;
		draw();
	}

	/**
	 * @see org.mihalis.opal.obutton.ButtonRenderer#drawButton(org.eclipse.swt.graphics.GC, org.mihalis.opal.obutton.OButton)
	 */
	@Override
	public void drawButton(final GC gc, final OButton parent) {
		this.gc = gc;
		this.configuration = this.normal;
		this.gapOnClic = 0;
		this.parent = parent;
		draw();
	}

	/**
	 * @see org.mihalis.opal.obutton.ButtonRenderer#drawButtonWhenClicked(org.eclipse.swt.graphics.GC, org.mihalis.opal.obutton.OButton)
	 */
	@Override
	public void drawButtonWhenClicked(final GC gc, final OButton parent) {
		this.gc = gc;
		this.configuration = this.onclick;
		this.gapOnClic = GAP_ON_CLIC;
		this.parent = parent;
		draw();
	}

	/**
	 * @see org.mihalis.opal.obutton.ButtonRenderer#computeSize(org.mihalis.opal.obutton.OButton, int, int, boolean)
	 */
	@Override
	public Point computeSize(final OButton button, final int wHint, final int hHint, final boolean changed) {
		parent = button;
		final Point sizeOfTextAndImages = computeSizeOfTextAndImages();
		return new Point(2 * MARGIN + sizeOfTextAndImages.x, 2 * MARGIN + sizeOfTextAndImages.y + GAP_ON_CLIC);
	}

	private Point computeSizeOfTextAndImages() {
		int width = 0, height = 0;
		final boolean textNotEmpty = parent.getText() != null && !parent.getText().equals("");

		if (textNotEmpty) {
			final GC gc = new GC(parent);
			if (this.configuration == null) {
				gc.setFont(parent.getFont());
			} else {
				gc.setFont(this.configuration.getFont());
			}

			final Point extent = gc.stringExtent(parent.getText());
			gc.dispose();
			width += extent.x;
			height = extent.y;
		}

		final Point imageSize = new Point(-1, -1);
		computeImageSize(extractImage(), imageSize);

		if (imageSize.x != -1) {
			width += imageSize.x;
			height = Math.max(imageSize.y, height);
			if (textNotEmpty) {
				width += MARGIN;
			}
		}
		return new Point(width, height);
	}

	private void computeImageSize(final Image image, final Point imageSize) {
		if (image == null) {
			return;
		}
		final Rectangle imageBounds = image.getBounds();
		imageSize.x = Math.max(imageBounds.width, imageSize.x);
		imageSize.y = Math.max(imageBounds.height, imageSize.y);
	}
}
