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
package org.mihalis.opal.systemMonitor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * Instances of this class are system monitors.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 */
public class SystemMonitor extends Canvas {

	private final Map<String, SampleWrapper> samples;
	private boolean captionVisible;
	private GC gc;
	private final Color borderColor;
	private final Color gridColorBackground;
	private final Color gridColor;
	private final int gridSize;
	private final int refreshTime;
	private boolean keepRunning;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'in together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from super
	 * classes.
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
	public SystemMonitor(final Composite parent, final int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);
		this.samples = new LinkedHashMap<String, SampleWrapper>();
		this.captionVisible = true;
		this.borderColor = new Color(getDisplay(), 96, 96, 96);
		this.gridColor = new Color(getDisplay(), 89, 89, 89);
		this.gridColorBackground = new Color(getDisplay(), 50, 50, 50);
		this.gridSize = 12;
		this.refreshTime = 300;
		this.keepRunning = true;

		createListeners();
		launchDataCollecting();
	}

	/**
	 * Create the listeners
	 */
	private void createListeners() {
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				SystemMonitor.this.paintControl(e);
			}
		});
		addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				SystemMonitor.this.borderColor.dispose();
				SystemMonitor.this.gridColor.dispose();
				SystemMonitor.this.gridColorBackground.dispose();
			}
		});
		addControlListener(new ControlAdapter() {

			/**
			 * @see org.eclipse.swt.events.ControlAdapter#controlResized(org.eclipse.swt.events.ControlEvent)
			 */
			@Override
			public void controlResized(final ControlEvent e) {
				for (final SampleWrapper sample : SystemMonitor.this.samples.values()) {
					sample.resize(getClientArea().width / SystemMonitor.this.gridSize - 1);
				}
			}
		});
	}

	/**
	 * Draws the widget
	 * 
	 * @param e paint event
	 */
	private void paintControl(final PaintEvent e) {
		this.gc = e.gc;
		e.gc.setAdvanced(true);
		e.gc.setAntialias(SWT.ON);
		drawBackground();
		drawGrid();

		for (final SampleWrapper sample : this.samples.values()) {
			drawData(sample);
		}

		if (this.captionVisible && this.samples.size() == 1) {
			drawCaption();
		}

	}

	/**
	 * Draws the background
	 */
	private void drawBackground() {
		final Rectangle clientArea = getClientArea();
		this.gc.setForeground(this.borderColor);
		this.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		this.gc.fillRoundRectangle(clientArea.x, clientArea.y, clientArea.width, clientArea.height, 5, 5);
		this.gc.drawRoundRectangle(clientArea.x, clientArea.y, clientArea.width, clientArea.height, 5, 5);
	}

	/**
	 * Draw the grid
	 */
	private void drawGrid() {
		final Rectangle clientArea = getClientArea();
		this.gc.setClipping(clientArea.x + 3, clientArea.y + 3, clientArea.width - 6, clientArea.height - 6);
		this.gc.setForeground(this.gridColor);
		this.gc.setBackground(this.gridColorBackground);
		this.gc.fillRectangle(getClientArea());
		for (int x = this.gridSize / 2; x < clientArea.x + clientArea.width; x += this.gridSize) {
			this.gc.drawLine(x, clientArea.x, x, clientArea.height);
		}
		for (int y = this.gridSize / 2; y < clientArea.y + clientArea.height; y += this.gridSize) {
			this.gc.drawLine(clientArea.x, y, clientArea.width, y);
		}

		this.gc.setAlpha(180);
		this.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		this.gc.fillRoundRectangle(clientArea.x + 10, clientArea.y + 10, clientArea.width + 20, clientArea.width + 20, 5, 5);
		this.gc.setAlpha(255);

		this.gc.setClipping(clientArea);
	}

	/**
	 * Draw the data
	 * 
	 * @param sample sample that contains data
	 */
	private void drawData(final SampleWrapper sample) {
		final List<Double> data = sample.getData();

		if (data == null || data.size() < 2) {
			return;
		}

		final Rectangle clientArea = getClientArea();
		final double maxValue = sample.getMaxValue();
		this.gc.setClipping(clientArea);
		final Color borderColor = new Color(getDisplay(), sample.getBorderColor());
		final Color color = new Color(getDisplay(), sample.getColor());
		final int[] pointArray = new int[2 * (data.size() + 2)];

		final int availableWidth = clientArea.width - this.gridSize;
		final int availableHeight = (int) ((clientArea.height - this.gridSize) * 0.98f);

		int x = this.gridSize / 2 + availableWidth - (data.size() - 1) * this.gridSize;

		// First point
		pointArray[0] = x;
		pointArray[1] = clientArea.y + clientArea.height + this.gridSize / 2 - (this.captionVisible ? 25 : 0);

		// Following points
		int index = 2;
		double maxDisplayedValue = -1d;
		for (final Double datum : data) {
			pointArray[index++] = x;
			pointArray[index++] = clientArea.height - (int) (this.gridSize / 2 + availableHeight * datum / maxValue);
			x += this.gridSize;
			maxDisplayedValue = Math.max(maxDisplayedValue, datum);
		}

		// Last point
		pointArray[index++] = x - this.gridSize;
		pointArray[index++] = clientArea.y + clientArea.height + this.gridSize / 2 - (this.captionVisible ? 25 : 0);

		// Draw a gradient rectangle
		this.gc.setAlpha(this.samples.size() == 1 ? 210 : 150);
		final Region region = new Region(getDisplay());
		region.add(pointArray);
		this.gc.setClipping(region);
		this.gc.setForeground(borderColor);
		this.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		this.gc.fillGradientRectangle(this.gridSize / 2, clientArea.height - (int) (this.gridSize / 2 + availableHeight * maxDisplayedValue / maxValue), availableWidth, (int) (availableHeight * maxDisplayedValue / maxValue), true);

		// Draw the polyline
		this.gc.setClipping(clientArea);
		this.gc.setForeground(borderColor);
		this.gc.drawPolygon(pointArray);

		region.dispose();
		borderColor.dispose();
		color.dispose();
		this.gc.setAlpha(255);

	}

	private void drawCaption() {
		for (final SampleWrapper sample : this.samples.values()) {
			if (sample.getCaption() != null && !sample.getCaption().equals("")) {
				drawCaptionForSample(sample);
				return;
			}
		}
	}

	/**
	 * Draws a caption for a given sample
	 * 
	 * @param sample sample
	 */
	private void drawCaptionForSample(final SampleWrapper sample) {
		final Rectangle clientArea = getClientArea();
		this.gc.setClipping(clientArea);
		this.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		this.gc.fillRectangle(clientArea.x, clientArea.y + clientArea.height - 19, clientArea.width, 19);

		final Color color = new Color(getDisplay(), sample.getBorderColor());
		this.gc.setForeground(color);
		final FontData[] fontData = getFont().getFontData();
		for (final FontData f : fontData) {
			f.setHeight(9);
		}
		final Font font = new Font(getDisplay(), fontData);
		this.gc.setFont(getFont());

		final String format = sample.getFormatPattern().replace("{value}", "1$").replace("{maxValue}", "2$").replace("{percentValue}", "3$");
		final String formattedCaption = String.format(format, //
				new Object[] { Double.valueOf(sample.getLastValue()), //
						Double.valueOf(sample.getLastMaxValue()), //
						Double.valueOf(sample.getLastValue() / sample.getLastMaxValue() * 100.0D) });

		this.gc.drawString(sample.getCaption() + " : " + formattedCaption, clientArea.x + this.gridSize, clientArea.y + clientArea.height - 19);

		font.dispose();
		color.dispose();

	}

	/**
	 * Launch the data collecting process
	 */
	private void launchDataCollecting() {
		getDisplay().timerExec(this.refreshTime, new Runnable() {

			@Override
			public void run() {
				collect();
				if (!SystemMonitor.this.isDisposed() && SystemMonitor.this.keepRunning && !getDisplay().isDisposed()) {
					getDisplay().timerExec(SystemMonitor.this.refreshTime, this);
				}
			}
		});
	}

	/**
	 * Collect data
	 */
	private void collect() {
		for (final SampleWrapper sample : this.samples.values()) {
			sample.collect();
		}
		if (!this.isDisposed() && !getDisplay().isDisposed()) {
			getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!SystemMonitor.this.isDisposed()) {
						redraw();
					}
				}
			});
		}
	}

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'in together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from super
	 * classes.
	 * </p>
	 * 
	 * @param parent a composite control which will be the parent of the new
	 *            instance (cannot be null)
	 * @param style the style of control to construct
	 * @param identier Sample identifier
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
	public SystemMonitor(final Composite parent, final int style, final SampleIdentifier identifier) {
		this(parent, style);
		final SampleWrapper wrapper = SampleFactory.getInstance().getSample(identifier);
		addSample(identifier.name(), wrapper);
		this.captionVisible = !wrapper.getCaption().trim().equals("");
	}

	/**
	 * Add a sample
	 * 
	 * @param id identifier
	 * @param sample sample to add
	 */
	public void addSample(final String id, final Sample sample) {
		this.samples.put(id, new SampleWrapper(sample));
	}

	/**
	 * Add a sample
	 * 
	 * @param id identifier
	 * @param sampleWrapper sample wrapper
	 */
	private void addSample(final String id, final SampleWrapper sampleWrapper) {
		this.samples.put(id, sampleWrapper);
	}

	/**
	 * Displays all built-in samples
	 */
	public void displayAll() {
		this.samples.clear();
		addSample(SampleIdentifier.CPU_USAGE.name(), SampleFactory.getInstance().getSample(SampleIdentifier.CPU_USAGE));
		addSample(SampleIdentifier.HEAP_MEMORY.name(), SampleFactory.getInstance().getSample(SampleIdentifier.HEAP_MEMORY));
		addSample(SampleIdentifier.PHYSICAL_MEMORY.name(), SampleFactory.getInstance().getSample(SampleIdentifier.PHYSICAL_MEMORY));
		addSample(SampleIdentifier.THREADS.name(), SampleFactory.getInstance().getSample(SampleIdentifier.THREADS));
	}

	/**
	 * @return <code>true</code> if the caption is visible, <code>false</code>
	 *         otherwise
	 */
	public boolean isCaptionVisible() {
		return this.captionVisible;
	}

	/**
	 * Set the caption for a given sample
	 * 
	 * @param id sample identifier
	 * @param caption caption to set
	 */
	public void setCaption(final String id, final String caption) {
		if (!this.samples.containsKey(id)) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.samples.get(id).setCaption(caption);
	}

	/**
	 * @param captionVisible if true, the caption is visible
	 */
	public void setCaptionVisible(final boolean captionVisible) {
		this.captionVisible = captionVisible;
	}

	/**
	 * Set the color for a given sample
	 * 
	 * @param id sample identifier
	 * @param color color to set
	 */
	public void setColor(final String id, final RGB color) {
		if (!this.samples.containsKey(id)) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.samples.get(id).setColor(color);
	}

	/**
	 * Set the pattern for a given sample
	 * 
	 * @param id sample identifier
	 * @param pattern pattern to set
	 */
	public void setFormatPattern(final String id, final String pattern) {
		if (!this.samples.containsKey(id)) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.samples.get(id).setFormatPattern(pattern);
	}

	/**
	 * Stop the data collecting process
	 */
	public void stop() {
		this.keepRunning = false;
	}

}
