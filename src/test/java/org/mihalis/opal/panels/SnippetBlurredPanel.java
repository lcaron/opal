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
package org.mihalis.opal.panels;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.mihalis.opal.opalDialog.Dialog;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * This snippet demonstrates the blured panel
 * 
 */
public class SnippetBlurredPanel {
	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell();
		shell.setText("Blured Panel Sample");
		shell.setLayout(new GridLayout(2, false));

		createRow(shell, "First Name");
		createRow(shell, "Last Name");
		createRow(shell, "E-mail");
		createRow(shell, "Phone number");

		createButtons(shell);

		shell.setSize(shell.computeSize(400, 400));
		SWTGraphicUtil.centerShell(shell);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void createRow(final Shell shell, final String label) {
		final Label lbl = new Label(shell, SWT.NONE);
		lbl.setText(label);
		lbl.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));

		final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}

	private static void createButtons(final Shell shell) {
		final BlurredPanel p = new BlurredPanel(shell);

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		composite.setLayout(new GridLayout(2, false));

		final Button ok = new Button(composite, SWT.PUSH);
		ok.setText("Ok");
		ok.setLayoutData(new GridData(SWT.END, SWT.END, true, true));
		ok.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				p.show();
				Dialog.isConfirmed("Confirmation", "Are you sure you want to save this form ?");
				p.hide();
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});

		final Button cancel = new Button(composite, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.setLayoutData(new GridData(SWT.CENTER, SWT.END, false, true));
		cancel.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
	}

}
