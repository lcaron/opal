/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Laurent CARON (laurent.caron at gmail dot com) - initial API
 * and implementation
 *******************************************************************************/

package org.mihalis.opal.notify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * This class creates the colors associated to a given theme
 * 
 */
public class NotifierColorsFactory {

	public enum NotifierTheme {
		YELLOW_THEME, GRAY_THEME, BLUE_THEME
	};

	/**
	 * Constructor
	 */
	private NotifierColorsFactory() {

	}

	/**
	 * @param theme a theme for the notifier widget
	 * @return the color set for the given theme
	 */
	static NotifierColors getColorsForTheme(final NotifierTheme theme) {
		final NotifierColors colors = new NotifierColors();
		switch (theme) {
		case BLUE_THEME:
			colors.textColor = new Color(Display.getDefault(), 4, 64, 140);
			colors.titleColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
			colors.borderColor = new Color(Display.getDefault(), 153, 188, 232);
			colors.leftColor = new Color(Display.getDefault(), 210, 225, 244);
			colors.rightColor = new Color(Display.getDefault(), 182, 207, 238);
			break;
		case GRAY_THEME:
			colors.textColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
			colors.titleColor = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
			colors.borderColor = new Color(Display.getDefault(), 208, 208, 208);
			colors.leftColor = new Color(Display.getDefault(), 255, 255, 255);
			colors.rightColor = new Color(Display.getDefault(), 208, 208, 208);
			break;
		default:
			colors.textColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
			colors.titleColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
			colors.borderColor = new Color(Display.getDefault(), 218, 178, 85);
			colors.leftColor = new Color(Display.getDefault(), 220, 220, 160);
			colors.rightColor = new Color(Display.getDefault(), 255, 255, 191);
			break;
		}
		return colors;
	}

}
