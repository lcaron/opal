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
 * This is the orange theme button renderer
 */
public class OrangeButtonRenderer extends AbstractButtonRenderer {

	private static OrangeButtonRenderer instance;
	private static final Color FIRST_BACKGROUND_COLOR = SWTGraphicUtil.createDisposableColor(255, 195, 117);
	private static final Color SECOND_BACKGROUND_COLOR = SWTGraphicUtil.createDisposableColor(238, 180, 79);

	private OrangeButtonRenderer() {
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

	public static OrangeButtonRenderer getInstance() {
		if (instance == null) {
			instance = new OrangeButtonRenderer();
		}
		return instance;
	}
}
