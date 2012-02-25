/*******************************************************************************
 * Copyright (c) 2011 Luis Carlos Moreira da Costa.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luis Carlos Moreira da Costa (tcljava at gmail dot com) - initial API and implementation
 *     Laurent CARON (laurent.caron at gmail dot com) - code review
 *******************************************************************************/
package org.mihalis.opal.textprogressbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Widget;

/**
 * Instances of the receiver represent an unselectable user interface object
 * that is used to display progress, typically in the form of a bar. A text that
 * represent the percentage of selected value is displayed on the widget.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SMOOTH, HORIZONTAL, VERTICAL, INDETERMINATE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#progressbar">ProgressBar
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */
public final class TextProgressBar extends ProgressBar {

	// Default text color
	private Color textColor;

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
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT#SMOOTH
	 * @see SWT#HORIZONTAL
	 * @see SWT#VERTICAL
	 * @see SWT#INDETERMINATE
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TextProgressBar(final Composite parent, final int style) {
		super(parent, style);

		this.textColor = getDisplay().getSystemColor(SWT.COLOR_BLACK);

		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				final Point widgetSize = getSize();
				final int percentage = (int) (100f * getSelection() / (getMaximum() - getMinimum()));
				final String text = percentage + "%";
				final Point textSize = e.gc.stringExtent(text);
				e.gc.setForeground(TextProgressBar.this.textColor);
				e.gc.drawString(text, ((widgetSize.x - textSize.x) / 2), ((widgetSize.y - textSize.y) / 2), true);
			}
		});
	}

	/**
	 * Set text color
	 * 
	 * @param textColor int
	 */
	public void setTextColor(final Color textColor) {
		checkWidget();
		this.textColor = textColor;
	}

	/**
	 * Return text color
	 * 
	 * @return int
	 */
	public Color getTextColor() {
		checkWidget();
		return this.textColor;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components.
	}

}