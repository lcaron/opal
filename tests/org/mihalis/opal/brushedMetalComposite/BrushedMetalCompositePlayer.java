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
package org.mihalis.opal.brushedMetalComposite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;

/**
 * This snippet demonstrates the brushed metal composite and allows user to set
 * up the parameters
 * 
 */
public class BrushedMetalCompositePlayer {

	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(3, false));

		final BrushedMetalComposite bmc = new BrushedMetalComposite(shell, SWT.NONE);
		final GridData gdBmc = new GridData(GridData.FILL, GridData.FILL, true, true, 3, 1);
		gdBmc.widthHint = gdBmc.heightHint = 300;
		bmc.setLayoutData(gdBmc);

		// Displays the control used to set up the parameters
		createSlider(shell, bmc, "Blur", 10f, false, null);
		createSlider(shell, bmc, "Amount of noise", 0.1f, true, null);
		createSlider(shell, bmc, "Shine", 0.1f, true, null);

		createColorSelector(shell, bmc);
		createMonochromeButton(shell, bmc);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void createSlider(final Shell shell, final BrushedMetalComposite bmc, final String text, final float defaultValue, final boolean isDecimal, final Listener listener) {
		final Label label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		label.setText(text);

		final Slider slider = new Slider(shell, SWT.HORIZONTAL);
		slider.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		slider.setMinimum(0);
		slider.setMaximum(110);

		final Text txt = new Text(shell, SWT.SINGLE | SWT.READ_ONLY);
		final GridData gd = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gd.widthHint = 50;
		txt.setLayoutData(gd);

		if (isDecimal) {
			slider.setSelection((int) (defaultValue * 100));
			txt.setText("" + defaultValue);
		} else {
			slider.setSelection((int) defaultValue);
			txt.setText("" + (int) defaultValue);
		}

		slider.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				if (isDecimal) {
					final float newValue = slider.getSelection() / 100f;
					if (text.startsWith("Amount")) {
						bmc.setAmount(newValue);
					} else {
						bmc.setShine(newValue);
					}
					txt.setText("" + newValue);
				} else {
					bmc.setRadius(slider.getSelection());
					txt.setText("" + slider.getSelection());
				}
			}
		});

		if (listener != null) {
			slider.addListener(SWT.Selection, listener);
		}
	}

	private static void createColorSelector(final Shell shell, final BrushedMetalComposite bmc) {
		final Label label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		label.setText("Color :");

		final Text colorSelector = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		colorSelector.setText("Click to pick a color");
		final Cursor cursor = new Cursor(shell.getDisplay(), SWT.CURSOR_ARROW);
		colorSelector.setCursor(cursor);
		shell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				cursor.dispose();
			}
		});

		final Color color = bmc.getColor();
		shell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				color.dispose();
			}
		});

		colorSelector.setBackground(color);
		colorSelector.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

		colorSelector.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				final ColorDialog dialog = new ColorDialog(shell);
				final RGB rgb = dialog.open();
				if (rgb == null) {
					return;
				}

				final Color color = new Color(shell.getDisplay(), rgb);
				shell.addDisposeListener(new DisposeListener() {

					@Override
					public void widgetDisposed(final DisposeEvent e) {
						color.dispose();
					}
				});
				bmc.setColor(color);
				colorSelector.setBackground(color);
			}
		});

	}

	private static void createMonochromeButton(final Shell shell, final BrushedMetalComposite bmc) {
		final Button button = new Button(shell, SWT.CHECK);
		button.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 3, 1));
		button.setText("Monochrome ?");
		button.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				bmc.setMonochrome(!button.getSelection());
			}
		});

	}

}
