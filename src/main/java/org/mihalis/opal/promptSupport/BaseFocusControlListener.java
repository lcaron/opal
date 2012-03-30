/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Peter Weishapl - Inspiration
 *     Laurent CARON (laurent.caron@gmail.com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.promptSupport;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.mihalis.opal.promptSupport.PromptSupport.FocusBehavior;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * Abstract class that contains code for the FocusLost, FocusGained and
 * ControlResized events
 * 
 */
abstract class BaseFocusControlListener implements FocusListener, ControlListener {

	protected Control control;
	private boolean firstDraw;
	private Font initialFont;
	private Color initialBackgroundColor;
	private Color initialForegroundColor;

	/**
	 * Constructor
	 * 
	 * @param control control on which this listener will be attached
	 */
	BaseFocusControlListener(final Control control) {
		this.control = control;
		this.firstDraw = true;
	}

	/**
	 * @see org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events.FocusEvent)
	 */
	@Override
	public void focusGained(final FocusEvent e) {
		if (isFilled()) {
			// Widget not empty
			return;
		}

		applyInitialLook();
		if (PromptSupport.getFocusBehavior(this.control) == FocusBehavior.HIDE_PROMPT) {
			hidePrompt();
		} else {
			highLightPrompt();
		}
	}

	/**
	 * Apply the initial look of the widget
	 */
	private void applyInitialLook() {
		this.control.setFont(this.initialFont);
		this.control.setBackground(this.initialBackgroundColor);
		this.control.setForeground(this.initialForegroundColor);
	}

	/**
	 * Code when the focus behiaviour is "Hide"
	 */
	protected abstract void hidePrompt();

	/**
	 * Code when the focus behiaviour is "Highlight"
	 */
	protected abstract void highLightPrompt();

	/**
	 * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
	 */
	@Override
	public void focusLost(final FocusEvent e) {
		if (isFilled()) {
			return;
		}

		storeInitialLook();
		applyForegroundColor();
		applyBackgroundColor();
		applyFontStyle();
		fillPromptText();
	}

	/**
	 * @return <code>true</code> if the widget is filled, <code>false</code>
	 *         otherwise
	 */
	protected abstract boolean isFilled();

	/**
	 * Apply the foreground color for the prompt
	 */
	private void applyForegroundColor() {
		this.control.setForeground(PromptSupport.getForeground(this.control));
	}

	/**
	 * Apply the background color for the prompt
	 */
	private void applyBackgroundColor() {
		this.control.setBackground(PromptSupport.getBackground(this.control));
	}

	/**
	 * Apply the font style to the prompt
	 */
	private void applyFontStyle() {
		final Font font = SWTGraphicUtil.buildFontFrom(this.control, PromptSupport.getFontStyle(this.control));
		this.control.setFont(font);
		this.control.addListener(SWT.Dispose, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				SWTGraphicUtil.dispose(font);
			}
		});
	}

	/**
	 * Fill the prompt text
	 */
	protected abstract void fillPromptText();

	/**
	 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
	 */
	@Override
	public void controlMoved(final ControlEvent e) {
	}

	/**
	 * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.ControlEvent)
	 */
	@Override
	public void controlResized(final ControlEvent e) {
		if (this.firstDraw) {
			storeInitialLook();
			this.firstDraw = true;
			focusLost(null);
		}
	}

	/**
	 * Store the initial look of the widget
	 */
	private void storeInitialLook() {
		this.initialFont = this.control.getFont();
		this.initialBackgroundColor = this.control.getBackground();
		this.initialForegroundColor = this.control.getForeground();
	}
}
