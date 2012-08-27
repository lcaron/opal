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
import org.eclipse.swt.widgets.Display;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * This is the red theme button renderer
 */
public class RedButtonRenderer extends AbstractButtonRenderer {

	private static RedButtonRenderer instance;
	private static final Color FIRST_BACKGROUND_COLOR = SWTGraphicUtil.createDisposableColor(254, 26, 0);
	private static final Color SECOND_BACKGROUND_COLOR = SWTGraphicUtil.createDisposableColor(208, 2, 0);

	private RedButtonRenderer() {
		super();
	}

	@Override
	protected Color getFontColor() {
		return Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	}

	@Override
	protected Color getFirstBackgroundColor() {
		return FIRST_BACKGROUND_COLOR;
	}

	@Override
	protected Color getSecondBackgroundColor() {
		return SECOND_BACKGROUND_COLOR;
	}

	public static RedButtonRenderer getInstance() {
		if (instance == null) {
			instance = new RedButtonRenderer();
		}
		return instance;
	}
}
