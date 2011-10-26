/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation, 
 *     inspired by Romain Guy's work (http://www.curious-creature.org)
 *******************************************************************************/
package org.mihalis.opal.infinitePanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * Instances of this class are controls located on the top of a shell. They
 * display a ticker that indicates to the user that a long task operation is
 * running
 */
public class InfiniteProgressPanel {

	private static final String INFINITE_PANEL_KEY = "org.mihalis.opal.InfinitePanel.InfiniteProgressPanel";
	private static final int NUMBER_OF_STEPS = 10;

	private final Shell parent;
	private Shell panel;
	private String text;
	private Font textFont;
	private Color textColor;
	private float fps;
	private int barsCount;
	private int lineWidth;
	private int alpha;
	private Color defaultColor;
	private Color selectionColor;
	private int currentPosition;
	private Thread animatorThread;
	private Canvas canvas;
	private boolean fadeIn;
	private boolean fadeOut;
	private int fadeOutCounter;

	/**
	 * Constructs a new instance of this class given its parent.
	 * 
	 * @param shell a shell that will be the parent of the new instance (cannot
	 *            be null)
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the parent has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 */
	private InfiniteProgressPanel(final Shell shell) {
		if (shell == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (shell.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}

		this.parent = shell;
		if (shell.getData(INFINITE_PANEL_KEY) != null) {
			throw new IllegalArgumentException("This shell has already an infinite panel attached on it !");
		}

		this.text = null;
		this.textFont = null;
		this.barsCount = 14;
		this.fps = 15.0f;
		this.lineWidth = 16;
		this.alpha = 200;
		this.fadeIn = false;
		this.fadeOut = false;
		this.fadeOutCounter = 0;
		shell.setData(INFINITE_PANEL_KEY, this);

		this.parent.addListener(SWT.Activate, new Listener() {

			@Override
			public void handleEvent(final Event e) {
				if (InfiniteProgressPanel.this.panel != null && //
						!InfiniteProgressPanel.this.panel.isDisposed() && !InfiniteProgressPanel.this.panel.isVisible()) {
					InfiniteProgressPanel.this.panel.setVisible(true);
					InfiniteProgressPanel.this.panel.setActive();
				}
			}
		});
	}

	/**
	 * Starts the ticker
	 */
	public void start() {
		if (this.parent.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}

		this.panel = new Shell(this.parent, SWT.APPLICATION_MODAL | SWT.NO_TRIM | SWT.ON_TOP);
		this.panel.setLayout(new FillLayout());
		this.panel.setAlpha(0);

		this.panel.addListener(SWT.KeyUp, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				event.doit = false;
			}
		});

		if (this.defaultColor == null) {
			this.defaultColor = new Color(this.parent.getDisplay(), 200, 200, 200);
		}

		if (this.selectionColor == null) {
			this.selectionColor = new Color(this.parent.getDisplay(), 0, 0, 0);
		}

		this.parent.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				SWTGraphicUtil.dispose(InfiniteProgressPanel.this.defaultColor);
				SWTGraphicUtil.dispose(InfiniteProgressPanel.this.selectionColor);
			}
		});

		this.currentPosition = 0;
		this.fadeIn = true;
		this.fadeOut = false;
		this.fadeOutCounter = 0;

		this.canvas = new Canvas(this.panel, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);
		this.canvas.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(final PaintEvent e) {
				InfiniteProgressPanel.this.paintCanvas(e);
			}
		});

		this.panel.setBounds(this.panel.getDisplay().map(this.parent, null, this.parent.getClientArea()));
		this.panel.open();

		this.animatorThread = new Thread() {

			/**
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				while (!Thread.interrupted()) {
					InfiniteProgressPanel.this.currentPosition = (InfiniteProgressPanel.this.currentPosition + 1) % InfiniteProgressPanel.this.barsCount;
					if (InfiniteProgressPanel.this.fadeOut) {
						InfiniteProgressPanel.this.fadeOutCounter++;
					}
					InfiniteProgressPanel.this.panel.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							InfiniteProgressPanel.this.canvas.redraw();
						}
					});

					try {
						sleep(InfiniteProgressPanel.this.fadeOut ? 20 : (long) (1000 / InfiniteProgressPanel.this.fps));
					} catch (final InterruptedException e) {
						break;
					}
				}
			}
		};
		this.animatorThread.start();

		this.panel.addListener(SWT.Deactivate, new Listener() {

			@Override
			public void handleEvent(final Event arg0) {
				InfiniteProgressPanel.this.panel.setVisible(false);
			}
		});

	}

	/**
	 * Paint the canvas that holds the ticker
	 * 
	 * @param e
	 */
	private void paintCanvas(final PaintEvent e) {
		// Paint the panel
		final Rectangle clientArea = ((Canvas) e.widget).getClientArea();
		final GC gc = e.gc;

		this.handleFadeIn();
		this.handleFadeOut();
		this.drawBackground(clientArea, gc);
		this.drawTicker(clientArea, gc);
		this.drawText(clientArea, gc);

	}

	/**
	 * Handle the fade in effect of the panel
	 */
	private void handleFadeIn() {
		if (this.fadeIn) {
			if (this.currentPosition == NUMBER_OF_STEPS) {
				this.fadeIn = false;
				this.panel.setAlpha(this.alpha);
			} else {
				this.panel.setAlpha(this.currentPosition * this.alpha / NUMBER_OF_STEPS);
			}
		}
	}

	/**
	 * Handle the fade out effect of the panel
	 */
	private void handleFadeOut() {
		if (this.fadeOut) {
			if (this.fadeOutCounter == NUMBER_OF_STEPS) {
				if (this.animatorThread != null) {
					this.animatorThread.interrupt();
					this.animatorThread = null;
				}
				if (!this.panel.isDisposed()) {
					this.panel.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							if (!InfiniteProgressPanel.this.panel.isDisposed()) {
								InfiniteProgressPanel.this.panel.dispose();
							}
						}
					});
				}
			}
			this.panel.setAlpha(255 - this.fadeOutCounter * this.alpha / NUMBER_OF_STEPS);
		}
	}

	/**
	 * Draw the background of the panel
	 * 
	 * @param gc GC on with the background is drawn
	 * @param clientArea client area of the canvas
	 */
	private void drawBackground(final Rectangle clientArea, final GC gc) {
		// Draw the background
		gc.setBackground(this.panel.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(clientArea);
	}

	/**
	 * Draw the ticker
	 * 
	 * @param gc GC on with the ticker is drawn
	 * @param clientArea client area of the canvas
	 */
	private void drawTicker(final Rectangle clientArea, final GC gc) {
		// Draw the ticker
		final int centerX = clientArea.width / 2;
		final int centerY = clientArea.height / 2;
		final int maxRay = (int) (Math.min(clientArea.width, clientArea.height) * 0.6f) / 2;
		final int minRay = (int) (maxRay * 0.5f);

		double angle = Math.PI / 2;

		gc.setLineCap(SWT.CAP_ROUND);
		gc.setLineWidth(this.lineWidth);
		gc.setAntialias(SWT.ON);

		for (int i = 0; i < this.barsCount; i++) {
			if (i == this.currentPosition) {
				gc.setForeground(this.selectionColor);
			} else {
				gc.setForeground(this.defaultColor);
			}
			gc.drawLine((int) (centerX + minRay * Math.cos(angle)), //
					(int) (centerY - minRay * Math.sin(angle)), //
					(int) (centerX + maxRay * Math.cos(angle)), //
					(int) (centerY - maxRay * Math.sin(angle)));
			angle -= 2 * Math.PI / this.barsCount;
		}
	}

	/**
	 * Draw the text over the ticker
	 * 
	 * @param gc GC on with the text is drawn
	 * @param clientArea client area of the canvas
	 */
	private void drawText(final Rectangle clientArea, final GC gc) {
		if (this.text == null || "".equals(this.text)) {
			return;
		}

		final Font font;
		if (this.textFont == null) {
			font = this.parent.getDisplay().getSystemFont();
		} else {
			font = this.textFont;
		}

		final Color color;
		if (this.textColor == null) {
			color = this.parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		} else {
			color = this.textColor;
		}

		gc.setForeground(color);
		gc.setFont(font);
		gc.setTextAntialias(SWT.ON);
		final Point textSize = gc.textExtent(this.text, SWT.DRAW_TRANSPARENT);
		final int textWidth = textSize.x;
		final int textHeight = textSize.y;

		gc.drawString(this.text, (clientArea.width - textWidth) / 2, (clientArea.height - textHeight) / 2, true);

	}

	/**
	 * Stop the animation and dispose the panel
	 */
	public void stop() {
		if (this.panel.isDisposed() || this.panel.getDisplay().isDisposed()) {
			return;
		}
		this.fadeOut = true;
	}

	/**
	 * Returns the infinite progress panel for the shell. If no infinite panel
	 * has been declared, returns null.
	 * 
	 * @param shell the shell for which we are trying to get the associated
	 *            progess panel
	 * @return the progress panel associated to shell, or null if there is no
	 *         progress panel
	 */
	public static InfiniteProgressPanel getInfiniteProgressPanelFor(final Shell shell) {
		if (shell == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		if (shell.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}

		if (shell.getDisplay().isDisposed()) {
			SWT.error(SWT.ERROR_DEVICE_DISPOSED);
		}

		final InfiniteProgressPanel[] temp = new InfiniteProgressPanel[1];
		shell.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				final Object data = shell.getData(INFINITE_PANEL_KEY);
				if (data != null && data instanceof InfiniteProgressPanel) {
					temp[0] = (InfiniteProgressPanel) data;
				}
			}
		});

		if (temp[0] == null) {
			return new InfiniteProgressPanel(shell);
		} else {
			return temp[0];
		}
	}

	/**
	 * Check if a shell has an associated progress panel
	 * 
	 * @param shell the shell
	 * @return <code>true</code> if the shell has an associated panel,
	 *         <code>false</code> otherwise
	 */
	public static boolean hasInfiniteProgressPanel(final Shell shell) {
		return getInfiniteProgressPanelFor(shell) != null;
	}

	// ------------------------------------------------- Getters and Setters

	/**
	 * @return the alpha value of the panel
	 */
	public int getAlpha() {
		return this.alpha;
	}

	/**
	 * @param alpha the alpha value of the panel, between 0 and 255
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running</li>
	 *                </ul>
	 */
	public void setAlpha(final int alpha) {
		if (alpha < 0 || alpha > 255) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.checkIfAnimationIsRunning();
		this.alpha = alpha;
	}

	/**
	 * Check if the animation is running
	 */
	private void checkIfAnimationIsRunning() {
		if (this.animatorThread != null) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, "Can not change this value when an animation is running");
		}
	}

	/**
	 * @return the number of bars displayed in the ticker
	 */
	public int getBarsCount() {
		return this.barsCount;
	}

	/**
	 * @param barsCount the number of bars displayed in the ticker
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running</li>
	 *                </ul>
	 */
	public void setBarsCount(final int barsCount) {
		this.checkIfAnimationIsRunning();
		this.barsCount = barsCount;
	}

	/**
	 * @return the default color for the ticker's bars
	 */
	public Color getDefaultColor() {
		return this.defaultColor;
	}

	/**
	 * @param defaultColor the new default color for the ticker's bars. Please
	 *            notice that the previous color is disposed.
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running</li>
	 *                </ul>
	 */
	public void setDefaultColor(final Color defaultColor) {
		this.checkIfAnimationIsRunning();
		SWTGraphicUtil.dispose(this.defaultColor);
		this.defaultColor = defaultColor;
	}

	/**
	 * @return the number of frame per second for the animation
	 */
	public float getFps() {
		return this.fps;
	}

	/**
	 * @param fps the new frame per second value
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running</li>
	 *                </ul>
	 */
	public void setFps(final float fps) {
		this.checkIfAnimationIsRunning();
		this.fps = fps;
	}

	/**
	 * @return the line width of the bars that compose the ticker
	 */
	public int getLineWidth() {
		return this.lineWidth;
	}

	/**
	 * @param lineWidth the line width of the bars that compose the ticker
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running</li>
	 *                </ul>
	 */
	public void setLineWidth(final int lineWidth) {
		this.checkIfAnimationIsRunning();
		this.lineWidth = lineWidth;
	}

	/**
	 * @return the selection color of the ticker's bars
	 */
	public Color getSelectionColor() {
		return this.selectionColor;
	}

	/**
	 * @param selectionColor the new selection color for the ticker's bars.
	 *            Please notice that the previous color is disposed.
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running</li>
	 *                </ul>
	 */
	public void setSelectionColor(final Color selectionColor) {
		this.checkIfAnimationIsRunning();
		SWTGraphicUtil.dispose(this.selectionColor);
		this.selectionColor = selectionColor;
	}

	/**
	 * @return the displayed text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * @param text set the text to display
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running</li>
	 *                </ul>
	 */
	public void setText(final String text) {
		this.checkIfAnimationIsRunning();
		this.text = text;
	}

	/**
	 * @return the text color
	 */
	public Color getTextColor() {
		return this.textColor;
	}

	/**
	 * @param textColor the text color. Please notice that the previous color is
	 *            disposed.
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running</li>
	 *                </ul>
	 */
	public void setTextColor(final Color textColor) {
		this.checkIfAnimationIsRunning();
		SWTGraphicUtil.dispose(this.textColor);
		this.textColor = textColor;
	}

	/**
	 * @return the text font
	 */
	public Font getTextFont() {
		return this.textFont;
	}

	/**
	 * @param textFont the new text font. Please notice that the previous font
	 *            set is disposed.
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running</li>
	 *                </ul>
	 */
	public void setTextFont(final Font textFont) {
		this.checkIfAnimationIsRunning();
		if (this.textFont != null && !this.textFont.isDisposed()) {
			this.textFont.dispose();
		}
		this.textFont = textFont;

	}

}
