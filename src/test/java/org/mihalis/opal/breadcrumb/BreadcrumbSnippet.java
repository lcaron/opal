/*******************************************************************************
 * Copyright (c) 2012 Laurent CARON.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.breadcrumb;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * A simple snippet for the breadcrumb Widget
 */
public class BreadcrumbSnippet {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("BreakCrumb Snippet");
		shell.setLayout(new GridLayout(2, false));

		createLabelsBreadCrumb(shell);
		createButtonsBreadCrumb(shell);
		createToggleButtonsBreadCrumb(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();

	}

	private static void createLabelsBreadCrumb(final Shell shell) {
		final Label label = new Label(shell, SWT.NONE);
		label.setText("Label breadcrumb:");
		label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

		createBreadcrumb(shell, SWT.BORDER, SWT.CENTER);
		new Label(shell, SWT.NONE);

		createBreadcrumb(shell, SWT.NONE, SWT.CENTER);
	}

	private static void createBreadcrumb(final Shell shell, final int breadCrumbArgument, final int itemArgument) {
		final Breadcrumb bc = new Breadcrumb(shell, breadCrumbArgument);
		bc.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));

		for (int i = 1; i < 5; i++) {
			final BreadcrumbItem item = new BreadcrumbItem(bc, itemArgument);
			item.setText(String.valueOf(i));
		}
	}

	private static void createButtonsBreadCrumb(final Shell shell) {
		final Label label = new Label(shell, SWT.NONE);
		label.setText("Buttons breadcrumb:");
		label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

		createBreadcrumb(shell, SWT.BORDER, SWT.CENTER | SWT.PUSH);
		new Label(shell, SWT.NONE);

		createBreadcrumb(shell, SWT.NONE, SWT.CENTER | SWT.PUSH);
	}

	private static void createToggleButtonsBreadCrumb(final Shell shell) {
		final Label label = new Label(shell, SWT.NONE);
		label.setText("Toggle buttons breadcrumb:");
		label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

		createBreadcrumb(shell, SWT.BORDER, SWT.CENTER | SWT.TOGGLE);
		new Label(shell, SWT.NONE);

		createBreadcrumb(shell, SWT.NONE, SWT.CENTER | SWT.TOGGLE);
	}

}
