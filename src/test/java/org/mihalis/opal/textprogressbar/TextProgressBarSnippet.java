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
package org.mihalis.opal.textprogressbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * Snippet for TextProgressBar
 */
public final class TextProgressBarSnippet {

	/**
	 * @param args String[]
	 */
	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		final GridLayout gridLayout = new GridLayout(1, true);
		shell.setLayout(gridLayout);
		shell.setText("TextProgressBar with percentage");
		final Button button = new Button(shell, SWT.HORIZONTAL);
		button.setText("Start");
		final TextProgressBar textProgressBar = new TextProgressBar(shell, SWT.SMOOTH);
		textProgressBar.setMinimum(0);
		textProgressBar.setMaximum(100);
		final int maximum = textProgressBar.getMaximum();
		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				button.setEnabled(false);
				new Thread() {
					@Override
					public void run() {
						for (final int[] i = new int[1]; i[0] <= maximum; i[0]++) {
							try {
								Thread.sleep(50);
							} catch (final Exception e) {
							}
							if (display.isDisposed()) {
								return;
							}
							display.asyncExec(new Runnable() {
								@Override
								public void run() {
									if (!textProgressBar.isDisposed()) {
										textProgressBar.setSelection(i[0]);
									}
								}
							});
						}

						if (!display.isDisposed()) {
							display.asyncExec(new Runnable() {
								@Override
								public void run() {
									if (!button.isDisposed()) {
										button.setEnabled(true);
									}
								}
							});

						}

					}
				}.start();
			}
		});
		textProgressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textProgressBar.setState(SWT.ERROR); // It seems that under Windows
												// Seven, it is not working fine
												// if state is NORMAL, look at
												// https://bugs.eclipse.org/bugs/show_bug.cgi?id=295856

		shell.setSize(300, 100);
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(final ShellEvent se) {
				System.exit(0);
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}