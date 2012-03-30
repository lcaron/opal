/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - Initial implementation and API
 *******************************************************************************/
package org.mihalis.opal.launcher;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.mihalis.opal.opalDialog.Dialog;

/**
 * A simple snippet for the Launcher Widget
 * 
 */
public class LauncherSnippet {

	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));

		final Label title = new Label(shell, SWT.NONE);
		title.setText("Launcher");
		title.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		final Launcher l = new Launcher(shell, SWT.NONE);
		l.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		l.addItem("Address Book", "org/mihalis/opal/launcher/icons/x-office-address-book.png");
		l.addItem("Calendar", "org/mihalis/opal/launcher/icons/x-office-calendar.png");
		l.addItem("Presentation", "org/mihalis/opal/launcher/icons/x-office-presentation.png");
		l.addItem("Spreadsheet", "org/mihalis/opal/launcher/icons/x-office-spreadsheet.png");

		l.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				Dialog.inform("Selection", "You have selected item #" + l.getSelection());
			}

		});

		final Label under = new Label(shell, SWT.NONE);
		under.setText("Double-click an icon to launch the program");
		under.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		shell.setSize(new Point(436, 546));
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}

}
