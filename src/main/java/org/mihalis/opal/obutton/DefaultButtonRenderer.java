package org.mihalis.opal.obutton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.mihalis.opal.utils.AdvancedPath;
import org.mihalis.opal.utils.SWTGraphicUtil;

public class DefaultButtonRenderer implements ButtonRenderer {

	private static final int RADIUS_VALUE = 10;
	private static final Color DISABLED_FONT_COLOR = SWTGraphicUtil.createDisposableColor(119, 119, 119);
	private static final Color DISABLED_SECOND_BACKGROUND_COLOR = SWTGraphicUtil.createDisposableColor(220, 220, 220);
	private static final Color DISABLED_FIRST_BACKGROUND_COLOR = SWTGraphicUtil.createDisposableColor(237, 237, 237);
	private static final Color SECOND_BACKGROUND_COLOR = SWTGraphicUtil.createDisposableColor(56, 142, 229);
	private static final Color FIRST_BACKGROUND_COLOR = SWTGraphicUtil.createDisposableColor(121, 187, 255);
	private final OButton parent;
	private ButtonConfiguration normal, hover, disabled, selected, onclick;
	private GC gc;
	private ButtonConfiguration configuration;
	private int gapOnClic;
	private static final int MARGIN = 5;
	private static final int GAP_ON_CLIC = 2;

	public DefaultButtonRenderer(final OButton oButton) {
		this.parent = oButton;
		initButtonConfiguration();
	}

	private void initButtonConfiguration() {
		this.normal = createNormalConfiguration();
		this.hover = createHoverConfiguration();
		this.disabled = createDisabledConfiguration();
		this.selected = createSelectedConfiguration();
		this.onclick = createOnClickConfiguration();
	}

	protected ButtonConfiguration createNormalConfiguration() {
		final ButtonConfiguration configuration = new ButtonConfiguration();
		configuration.setCornerRadius(10);
		configuration.setFont(parent.getFont()).setFontColor(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		configuration.setGradientDirection(SWT.VERTICAL);
		configuration.setBackgroundColor(FIRST_BACKGROUND_COLOR);
		configuration.setSecondBackgroundColor(SECOND_BACKGROUND_COLOR);
		return configuration;
	}

	protected ButtonConfiguration createHoverConfiguration() {
		final ButtonConfiguration configuration = new ButtonConfiguration();
		configuration.setCornerRadius(10);
		configuration.setFont(parent.getFont()).setFontColor(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		configuration.setGradientDirection(SWT.VERTICAL);
		configuration.setBackgroundColor(SECOND_BACKGROUND_COLOR);
		configuration.setSecondBackgroundColor(FIRST_BACKGROUND_COLOR);
		return configuration;
	}

	protected ButtonConfiguration createDisabledConfiguration() {
		final ButtonConfiguration configuration = new ButtonConfiguration();
		configuration.setCornerRadius(RADIUS_VALUE);
		configuration.setFont(parent.getFont()).setFontColor(DISABLED_FONT_COLOR);
		configuration.setGradientDirection(SWT.VERTICAL);
		configuration.setBackgroundColor(DISABLED_FIRST_BACKGROUND_COLOR);
		configuration.setSecondBackgroundColor(DISABLED_SECOND_BACKGROUND_COLOR);
		return configuration;
	}

	protected ButtonConfiguration createSelectedConfiguration() {
		return createHoverConfiguration();
	}

	protected ButtonConfiguration createOnClickConfiguration() {
		return createHoverConfiguration();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void drawButtonWhenMouseHover(final GC gc) {
		this.gc = gc;
		configuration = hover;
		this.gapOnClic = 0;
		draw();
	}

	private void draw() {
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		drawBackground();
		int xPosition = computeStartingPosition();
		xPosition += drawImage(xPosition);
		if (parent.getText() != null) {
			drawText(xPosition);
		}
	}

	private void drawBackground() {
		createClipping();
		gc.setForeground(configuration.getBackgroundColor());
		gc.setBackground(configuration.getSecondBackgroundColor());
		gc.fillGradientRectangle(0, gapOnClic, parent.getWidth(), parent.getHeight() - GAP_ON_CLIC, configuration.getGradientDirection() == SWT.VERTICAL);
		gc.setClipping((Rectangle) null);
	}

	private void createClipping() {
		final AdvancedPath path = new AdvancedPath(parent.getDisplay());
		path.addRoundRectangle(0, gapOnClic, parent.getWidth(), parent.getHeight() - GAP_ON_CLIC, configuration.getCornerRadius(), configuration.getCornerRadius());
		gc.setClipping(path);
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
		if (parent.getImage() == null) {
			return 0;
		}

		final Image image;
		if (!parent.isEnabled()) {
			image = new Image(parent.getDisplay(), parent.getImage(), SWT.IMAGE_DISABLE);
			parent.addListener(SWT.Dispose, new Listener() {
				@Override
				public void handleEvent(final Event e) {
					image.dispose();
				}
			});
		} else {
			image = parent.getImage();
		}

		final int yPosition = (parent.getHeight() - image.getBounds().height - GAP_ON_CLIC) / 2;
		gc.drawImage(image, xPosition, yPosition + gapOnClic);
		return image.getBounds().width + MARGIN;
	}

	private void drawText(final int xPosition) {
		gc.setFont(configuration.getFont());
		gc.setForeground(configuration.getFontColor());

		final Point textSize = gc.stringExtent(parent.getText());
		final int yPosition = (parent.getHeight() - textSize.y - GAP_ON_CLIC) / 2;

		gc.drawText(parent.getText(), xPosition, yPosition + gapOnClic, true);
	}

	@Override
	public void drawButtonWhenDisabled(final GC gc) {
		this.gc = gc;
		configuration = disabled;
		this.gapOnClic = 0;
		draw();
	}

	@Override
	public void drawButtonWhenSelected(final GC gc) {
		this.gc = gc;
		configuration = selected;
		this.gapOnClic = 0;
		draw();
	}

	@Override
	public void drawButton(final GC gc) {
		this.gc = gc;
		configuration = normal;
		this.gapOnClic = 0;
		draw();
	}

	@Override
	public void drawButtonWhenClicked(final GC gc) {
		this.gc = gc;
		configuration = onclick;
		this.gapOnClic = GAP_ON_CLIC;
		draw();
	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		final Point sizeOfTextAndImages = computeSizeOfTextAndImages();
		return new Point(2 * MARGIN + sizeOfTextAndImages.x, 2 * MARGIN + sizeOfTextAndImages.y + GAP_ON_CLIC);
	}

	private Point computeSizeOfTextAndImages() {
		int width = 0, height = 0;
		final boolean textNotEmpty = parent.getText() != null && !parent.getText().equals("");

		if (textNotEmpty) {
			final GC gc = new GC(parent);
			if (configuration == null) {
				gc.setFont(parent.getFont());
			} else {
				gc.setFont(configuration.getFont());
			}

			final Point extent = gc.stringExtent(parent.getText());
			gc.dispose();
			width += extent.x;
			height = extent.y;
		}

		final Point imageSize = new Point(-1, -1);
		computeImageSize(parent.getImage(), imageSize);

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
