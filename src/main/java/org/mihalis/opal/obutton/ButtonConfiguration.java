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
import org.eclipse.swt.graphics.Font;

/**
 * This class is a POJO used to configure a OButton widget
 */
public class ButtonConfiguration {
	private Font font;
	private Color fontColor;

	private int cornerRadius;

	private int gradientDirection;
	private Color backgroundColor;
	private Color secondBackgroundColor;

	/**
	 * @return the font
	 */
	public Font getFont() {
		return this.font;
	}

	/**
	 * @return the fontColor
	 */
	public Color getFontColor() {
		return this.fontColor;
	}

	/**
	 * @return the cornerRadius
	 */
	public int getCornerRadius() {
		return this.cornerRadius;
	}

	/**
	 * @return the gradientDirection
	 */
	public int getGradientDirection() {
		return this.gradientDirection;
	}

	/**
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * @return the secondBackgroundColor
	 */
	public Color getSecondBackgroundColor() {
		return this.secondBackgroundColor;
	}

	/**
	 * @param font the font to set
	 * @return
	 */
	public ButtonConfiguration setFont(final Font font) {
		if (font != null && font.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.font = font;
		return this;
	}

	/**
	 * @param fontColor the fontColor to set
	 */
	public ButtonConfiguration setFontColor(final Color fontColor) {
		if (fontColor != null && fontColor.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}

		this.fontColor = fontColor;
		return this;
	}

	/**
	 * @param cornerRadius the cornerRadius to set
	 */
	public ButtonConfiguration setCornerRadius(final int cornerRadius) {
		this.cornerRadius = Math.max(0, cornerRadius);
		return this;
	}

	/**
	 * @param gradientDirection the gradientDirection to set
	 */
	public ButtonConfiguration setGradientDirection(final int gradientDirection) {
		if (gradientDirection != SWT.VERTICAL && gradientDirection != SWT.HORIZONTAL) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.gradientDirection = gradientDirection;
		return this;
	}

	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public ButtonConfiguration setBackgroundColor(final Color backgroundColor) {
		if (backgroundColor != null && backgroundColor.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.backgroundColor = backgroundColor;
		return this;
	}

	/**
	 * @param secondBackgroundColor the secondBackgroundColor to set
	 * @return
	 */
	public ButtonConfiguration setSecondBackgroundColor(final Color secondBackgroundColor) {
		if (secondBackgroundColor != null && secondBackgroundColor.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.secondBackgroundColor = secondBackgroundColor;
		return this;
	}
}
