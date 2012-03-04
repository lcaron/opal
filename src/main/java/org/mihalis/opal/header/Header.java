package org.mihalis.opal.header;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * Instances of this class provide a header, which is composed of a text, a
 * description and an image.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 */
public class Header extends Composite {

	private Image image;
	private String title;
	private String description;
	private Font titleFont;
	private Color titleColor;

	private Image oldImage;
	private Color gradientEnd;
	private Color gradientStart;
	private Color separatorColor;

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
	public Header(final Composite parent, final int style) {
		super(parent, style);

		final FontData[] fontData = getFont().getFontData();
		if (fontData != null && fontData.length > 0) {
			final FontData fd = fontData[0];
			fd.setStyle(SWT.BOLD);
			fd.setHeight(fd.getHeight() + 2);
			this.titleFont = new Font(getDisplay(), fd);
		} else {
			this.titleFont = null;
		}

		this.titleColor = new Color(getDisplay(), 0, 88, 150);

		this.gradientEnd = new Color(this.getDisplay(), 239, 239, 239);
		this.gradientStart = new Color(this.getDisplay(), 255, 255, 255);
		this.separatorColor = new Color(this.getDisplay(), 229, 229, 229);

		this.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				redrawComposite();
			}
		});

		this.addListener(SWT.Dispose, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				SWTGraphicUtil.dispose(Header.this.titleColor);
				SWTGraphicUtil.dispose(Header.this.titleFont);
				SWTGraphicUtil.dispose(Header.this.oldImage);
				SWTGraphicUtil.dispose(Header.this.gradientEnd);
				SWTGraphicUtil.dispose(Header.this.gradientStart);
				SWTGraphicUtil.dispose(Header.this.separatorColor);
			}
		});
		setBackgroundMode(SWT.INHERIT_FORCE);
	}

	/**
	 * Redraw the composite
	 */
	private void redrawComposite() {
		// Dispose previous content
		for (final Control c : this.getChildren()) {
			c.dispose();
		}

		int numberOfColumns = 1;
		if (this.image != null) {
			numberOfColumns++;
		}

		super.setLayout(new GridLayout(numberOfColumns, false));
		createContent();
		drawBackground();
	}

	/**
	 * Create the content (title, image, description)
	 */
	private void createContent() {
		if (this.title != null) {
			createTitle();
		}

		if (this.image != null) {
			createImage();
		}

		if (this.description != null) {
			createDescription();
		}
	}

	/**
	 * Create the title
	 */
	private void createTitle() {
		final Label labelTitle = new Label(this, SWT.NONE);
		labelTitle.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		labelTitle.setFont(this.titleFont);
		labelTitle.setForeground(this.titleColor);
		labelTitle.setText(this.title);
	}

	/**
	 * Create the image
	 */
	private void createImage() {

		int numberOfLines = 1;

		if (this.title != null && this.description != null) {
			numberOfLines++;
		}
		final Label labelImage = new Label(this, SWT.NONE);
		labelImage.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, true, 1, numberOfLines));
		labelImage.setImage(this.image);
	}

	/**
	 * Create the description
	 */
	private void createDescription() {
		final StyledText labelDescription = new StyledText(this, SWT.WRAP | SWT.READ_ONLY);
		labelDescription.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		labelDescription.setEnabled(false);
		labelDescription.setFont(getFont());
		labelDescription.setForeground(getForeground());
		labelDescription.setText(this.description);
		SWTGraphicUtil.applyHTMLFormating(labelDescription);
	}

	/**
	 * Draw the background (a gradient+a separator)
	 */
	private void drawBackground() {
		final Display display = this.getDisplay();
		final Rectangle rect = this.getClientArea();
		final Image newImage = new Image(display, Math.max(1, rect.width), Math.max(1, rect.height));

		final GC gc = new GC(newImage);
		gc.setForeground(this.gradientStart);
		gc.setBackground(this.gradientEnd);

		gc.fillGradientRectangle(rect.x, rect.y, rect.width, rect.height, false);

		gc.setForeground(this.separatorColor);
		gc.drawLine(rect.x, rect.y + rect.height - 1, rect.x + rect.width, rect.y + rect.height - 1);

		gc.dispose();

		this.setBackgroundImage(newImage);
		if (this.oldImage != null) {
			this.oldImage.dispose();
		}
		this.oldImage = newImage;
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#setLayout(org.eclipse.swt.widgets.Layout)
	 */
	@Override
	public void setLayout(final Layout layout) {
		throw new UnsupportedOperationException("Not supported");
	}

	// ------------------------------------ Getters and Setters

	/**
	 * Returns the receiver's description if it has one, or null if it does not.
	 * 
	 * @return the receiver's description if it has one, or null if it does not
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public String getDescription() {
		checkWidget();
		return this.description;
	}

	/**
	 * Returns the receiver's gradient end color.
	 * 
	 * @return the receiver's gradient end color
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Color getGradientEnd() {
		checkWidget();
		return this.gradientEnd;
	}

	/**
	 * Returns the receiver's gradient start color.
	 * 
	 * @return the receiver's gradient start color
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Color getGradientStart() {
		checkWidget();
		return this.gradientStart;
	}

	/**
	 * Returns the receiver's image if it has one, or null if it does not.
	 * 
	 * @return the receiver's image if it has one, or null if it does not
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Image getImage() {
		checkWidget();
		return this.image;
	}

	/**
	 * Returns the receiver's separator color.
	 * 
	 * @return the receiver's separator color
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Color getSeparatorColor() {
		checkWidget();
		return this.separatorColor;
	}

	/**
	 * Returns the receiver's title if it has one, or null if it does not.
	 * 
	 * @return the receiver's title if it has one, or null if it does not
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public String getTitle() {
		checkWidget();
		return this.title;
	}

	/**
	 * Returns the title's color.
	 * 
	 * @return the title's color
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Color getTitleColor() {
		checkWidget();
		return this.titleColor;
	}

	/**
	 * Returns the title's font.
	 * 
	 * @return the title's font.
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Font getTitleFont() {
		checkWidget();
		return this.titleFont;
	}

	/**
	 * Sets the receiver's description to the argument, which may be null
	 * indicating that no description should be displayed.
	 * 
	 * @param description the description of the header (may be null)
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setDescription(final String description) {
		checkWidget();
		this.description = description;
	}

	/**
	 * Sets the receiver's gradient end color.
	 * 
	 * @param gradientEnd the receiver's gradient end color
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setGradientEnd(final Color gradientEnd) {
		checkWidget();
		this.gradientEnd = gradientEnd;
	}

	/**
	 * Sets the receiver's gradient start color.
	 * 
	 * @param gradientStart the receiver's gradient start color
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setGradientStart(final Color gradientStart) {
		checkWidget();
		this.gradientStart = gradientStart;
	}

	/**
	 * Sets the receiver's image to the argument, which may be null indicating
	 * that no image should be displayed.
	 * 
	 * @param image the image to display on the receiver (may be null)
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setImage(final Image image) {
		checkWidget();
		this.image = image;
	}

	/**
	 * Sets the receiver's separator color.
	 * 
	 * @param separatorColor the receiver's separator color
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSeparatorColor(final Color separatorColor) {
		this.separatorColor = separatorColor;
	}

	/**
	 * Sets the receiver's title to the argument, which may be null indicating
	 * that no title should be displayed.
	 * 
	 * @param title the title
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setTitle(final String title) {
		checkWidget();
		this.title = title;
	}

	/**
	 * Sets the receiver's title color.
	 * 
	 * @param headerColor the receiver's title color
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setTitleColor(final Color headerColor) {
		checkWidget();
		this.titleColor = headerColor;
	}

	/**
	 * Sets the receiver's title font.
	 * 
	 * @param headerFont the receiver's title font
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setTitleFont(final Font headerFont) {
		checkWidget();
		this.titleFont = headerFont;
	}

}
