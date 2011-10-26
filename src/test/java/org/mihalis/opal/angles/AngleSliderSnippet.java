/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron@gmail.com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.angles;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.mihalis.opal.utils.SimpleSelectionAdapter;

/**
 * A simple snipper for the AngleSlider widget
 * 
 */
public class AngleSliderSnippet {

	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));

		final AngleSlider angleSlider = new AngleSlider(shell, SWT.NONE);
		angleSlider.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 2, 1));

		final Button button = new Button(shell, SWT.CHECK);
		button.setText("Enabled");
		button.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false, 2, 1));
		button.setSelection(true);
		button.addSelectionListener(new SimpleSelectionAdapter() {

			@Override
			public void handle(final SelectionEvent e) {
				angleSlider.setEnabled(button.getSelection());
			}
		});

		final Label label = new Label(shell, SWT.NONE);
		label.setText("Value :");
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));

		final Text text = new Text(shell, SWT.READ_ONLY | SWT.BORDER);
		text.setText("" + angleSlider.getSelection());
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		angleSlider.addSelectionListener(new SimpleSelectionAdapter() {

			@Override
			public void handle(final SelectionEvent e) {
				text.setText("" + angleSlider.getSelection());

			}
		});

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}
}
