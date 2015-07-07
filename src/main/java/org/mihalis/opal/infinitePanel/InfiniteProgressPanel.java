/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.infinitePanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
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
 * running. The design is inspired by Romain Guy's work
 * (http://www.curious-creature.org)
 */
public class InfiniteProgressPanel {

	private static final String INFINITE_PANEL_KEY = "org.mihalis.opal.InfinitePanel.InfiniteProgressPanel";
	private static final int NUMBER_OF_STEPS = 10;

	private final Shell parent;
	private Shell shellHover;
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
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
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

		parent = shell;
		if (shell.getData(INFINITE_PANEL_KEY) != null) {
			throw new IllegalArgumentException("This shell has already an infinite panel attached on it !");
		}

		text = null;
		textFont = null;
		barsCount = 14;
		fps = 15.0f;
		lineWidth = 16;
		alpha = 200;
		fadeIn = false;
		fadeOut = false;
		fadeOutCounter = 0;
		shell.setData(INFINITE_PANEL_KEY, this);

		parent.addListener(SWT.Activate, new Listener() {

			@Override
			public void handleEvent(final Event e) {
				if (shellHover != null && //
						!shellHover.isDisposed() && !shellHover.isVisible()) {
					shellHover.setVisible(true);
					shellHover.setActive();
				}
			}
		});
	}

	/**
	 * Starts the ticker
	 */
	public void start() {
		if (parent.isDisposed()) {
			SWT.error(SWT.ERROR_WIDGET_DISPOSED);
		}

		currentPosition = 0;
		fadeIn = true;
		fadeOut = false;
		fadeOutCounter = 0;

		if (defaultColor == null) {
			defaultColor = SWTGraphicUtil.getDefaultColor(parent, 200, 200, 200);
		}

		if (selectionColor == null) {
			selectionColor = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		}

		createShell();
		createAndRunAnimatorThread();
	}

	private void createShell() {
		shellHover = new Shell(parent, SWT.APPLICATION_MODAL | SWT.NO_TRIM | SWT.ON_TOP);
		shellHover.setLayout(new FillLayout());
		shellHover.setAlpha(0);

		shellHover.addListener(SWT.KeyUp, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				event.doit = false;
			}
		});

		shellHover.addListener(SWT.Deactivate, new Listener() {

			@Override
			public void handleEvent(final Event arg0) {
				shellHover.setVisible(false);
			}
		});

		shellHover.setBounds(shellHover.getDisplay().map(parent, null, parent.getClientArea()));

		canvas = new Canvas(shellHover, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);
		canvas.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(final PaintEvent e) {
				InfiniteProgressPanel.this.paintCanvas(e);
			}
		});

		shellHover.open();
	}

	private void createAndRunAnimatorThread() {
		animatorThread = new Thread() {

			/**
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				while (!Thread.interrupted()) {
					currentPosition = (currentPosition + 1) % barsCount;
					if (fadeOut) {
						fadeOutCounter++;
					}
					shellHover.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							canvas.redraw();
						}
					});

					try {
						sleep(fadeOut ? 20 : (long) (1000 / fps));
					} catch (final InterruptedException e) {
						break;
					}
				}
			}
		};
		animatorThread.start();
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

		handleFadeIn();
		handleFadeOut();
		drawBackground(clientArea, gc);
		drawTicker(clientArea, gc);
		drawText(clientArea, gc);

	}

	/**
	 * Handle the fade in effect of the hover shell
	 */
	private void handleFadeIn() {
		if (fadeIn) {
			if (currentPosition == NUMBER_OF_STEPS) {
				fadeIn = false;
				shellHover.setAlpha(alpha);
			} else {
				shellHover.setAlpha(currentPosition * alpha / NUMBER_OF_STEPS);
			}
		}
	}

	/**
	 * Handle the fade out effect of the hover shell
	 */
	private void handleFadeOut() {
		if (fadeOut) {
			if (fadeOutCounter >= NUMBER_OF_STEPS) {
				if (animatorThread != null) {
					animatorThread.interrupt();
					animatorThread = null;
				}
				if (!shellHover.isDisposed()) {
					shellHover.getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							if (!shellHover.isDisposed()) {
								shellHover.dispose();
							}
						}
					});
				}
			}
			shellHover.setAlpha(255 - fadeOutCounter * alpha / NUMBER_OF_STEPS);
		}
	}

	/**
	 * Draw the background of the panel
	 *
	 * @param gc GC on with the background is drawn
	 * @param clientArea client area of the canvas
	 */
	private void drawBackground(final Rectangle clientArea, final GC gc) {
		gc.setBackground(shellHover.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(clientArea);
	}

	/**
	 * Draw the ticker
	 *
	 * @param gc GC on with the ticker is drawn
	 * @param clientArea client area of the canvas
	 */
	private void drawTicker(final Rectangle clientArea, final GC gc) {
		final int centerX = clientArea.width / 2;
		final int centerY = clientArea.height / 2;
		final int maxRay = (int) (Math.min(clientArea.width, clientArea.height) * 0.6f) / 2;
		final int minRay = (int) (maxRay * 0.5f);

		double angle = Math.PI / 2;

		gc.setLineCap(SWT.CAP_ROUND);
		gc.setLineWidth(lineWidth);
		gc.setAntialias(SWT.ON);

		final double angleStep = 2 * Math.PI / barsCount;
		for (int i = 0; i < barsCount; i++) {
			if (i == currentPosition) {
				gc.setForeground(selectionColor);
			} else {
				gc.setForeground(defaultColor);
			}
			gc.drawLine((int) (centerX + minRay * Math.cos(angle)), //
					(int) (centerY - minRay * Math.sin(angle)), //
					(int) (centerX + maxRay * Math.cos(angle)), //
					(int) (centerY - maxRay * Math.sin(angle)));
			angle -= angleStep;
		}
	}

	/**
	 * Draw the text over the ticker
	 *
	 * @param gc GC on with the text is drawn
	 * @param clientArea client area of the canvas
	 */
	private void drawText(final Rectangle clientArea, final GC gc) {
		if (text == null || "".equals(text)) {
			return;
		}

		final Font font;
		if (textFont == null) {
			font = parent.getDisplay().getSystemFont();
		} else {
			font = textFont;
		}

		final Color color;
		if (textColor == null) {
			color = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		} else {
			color = textColor;
		}

		gc.setForeground(color);
		gc.setFont(font);
		gc.setTextAntialias(SWT.ON);
		final Point textSize = gc.textExtent(text, SWT.DRAW_TRANSPARENT);
		final int textWidth = textSize.x;
		final int textHeight = textSize.y;

		gc.drawString(text, (clientArea.width - textWidth) / 2, (clientArea.height - textHeight) / 2, true);

	}

	/**
	 * Stop the animation and dispose the panel
	 */
	public void stop() {
		if (shellHover.isDisposed() || shellHover.getDisplay().isDisposed()) {
			return;
		}
		fadeOut = true;
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
		return alpha;
	}

	/**
	 * @param alpha the alpha value of the panel, between 0 and 255
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running
	 *                </li>
	 *                </ul>
	 */
	public void setAlpha(final int alpha) {
		if (alpha < 0 || alpha > 255) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		checkIfAnimationIsRunning();
		this.alpha = alpha;
	}

	/**
	 * Check if the animation is running
	 */
	private void checkIfAnimationIsRunning() {
		if (animatorThread != null) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, "Can not change this value when an animation is running");
		}
	}

	/**
	 * @return the number of bars displayed in the ticker
	 */
	public int getBarsCount() {
		return barsCount;
	}

	/**
	 * @param barsCount the number of bars displayed in the ticker
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running
	 *                </li>
	 *                </ul>
	 */
	public void setBarsCount(final int barsCount) {
		checkIfAnimationIsRunning();
		this.barsCount = barsCount;
	}

	/**
	 * @return the default color for the ticker's bars
	 */
	public Color getDefaultColor() {
		return defaultColor;
	}

	/**
	 * @param defaultColor the new default color for the ticker's bars. Please
	 *            notice that the previous color is disposed.
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running
	 *                </li>
	 *                </ul>
	 */
	public void setDefaultColor(final Color defaultColor) {
		checkIfAnimationIsRunning();
		SWTGraphicUtil.safeDispose(this.defaultColor);
		this.defaultColor = defaultColor;
	}

	/**
	 * @return the number of frame per second for the animation
	 */
	public float getFps() {
		return fps;
	}

	/**
	 * @param fps the new frame per second value
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running
	 *                </li>
	 *                </ul>
	 */
	public void setFps(final float fps) {
		checkIfAnimationIsRunning();
		this.fps = fps;
	}

	/**
	 * @return the line width of the bars that compose the ticker
	 */
	public int getLineWidth() {
		return lineWidth;
	}

	/**
	 * @param lineWidth the line width of the bars that compose the ticker
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running
	 *                </li>
	 *                </ul>
	 */
	public void setLineWidth(final int lineWidth) {
		checkIfAnimationIsRunning();
		this.lineWidth = lineWidth;
	}

	/**
	 * @return the selection color of the ticker's bars
	 */
	public Color getSelectionColor() {
		return selectionColor;
	}

	/**
	 * @param selectionColor the new selection color for the ticker's bars.
	 *            Please notice that the previous color is disposed.
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running
	 *                </li>
	 *                </ul>
	 */
	public void setSelectionColor(final Color selectionColor) {
		checkIfAnimationIsRunning();
		this.selectionColor = selectionColor;
	}

	/**
	 * @return the displayed text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text set the text to display
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running
	 *                </li>
	 *                </ul>
	 */
	public void setText(final String text) {
		checkIfAnimationIsRunning();
		this.text = text;
	}

	/**
	 * @return the text color
	 */
	public Color getTextColor() {
		return textColor;
	}

	/**
	 * @param textColor the text color. Please notice that the previous color is
	 *            disposed.
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running
	 *                </li>
	 *                </ul>
	 */
	public void setTextColor(final Color textColor) {
		checkIfAnimationIsRunning();
		this.textColor = textColor;
	}

	/**
	 * @return the text font
	 */
	public Font getTextFont() {
		return textFont;
	}

	/**
	 * @param textFont the new text font. Please notice that the previous font
	 *            set is disposed.
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the animation is running
	 *                </li>
	 *                </ul>
	 */
	public void setTextFont(final Font textFont) {
		checkIfAnimationIsRunning();
		this.textFont = textFont;
	}

}
