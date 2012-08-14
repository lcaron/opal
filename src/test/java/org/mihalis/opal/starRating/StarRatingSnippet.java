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
package org.mihalis.opal.starRating;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * A simple snippet for the StarRating component
 */
public class StarRatingSnippet {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("StarRating Snippet");

		shell.setLayout(new GridLayout(2, false));

		createHorizontal(shell, true);
		createHorizontal(shell, false);
		createVertical(shell, true);
		createVertical(shell, false);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();

	}

	private static void createHorizontal(final Shell shell, final boolean enabled) {
		for (final StarRating.SIZE size : StarRating.SIZE.values()) {
			final Label label = new Label(shell, SWT.NONE);
			label.setText("Horizontal " + (enabled ? "enabled" : "disabled") + " size=" + size.toString());
			label.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));

			final StarRating sr = new StarRating(shell, SWT.NONE);
			final GridData gd = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
			sr.setLayoutData(gd);
			sr.setSizeOfStars(size);
			sr.setEnabled(enabled);
			sr.setMaxNumberOfStars(5 + (enabled ? 1 : 0));
		}
	}

	private static void createVertical(final Shell shell, final boolean enabled) {
		for (final StarRating.SIZE size : StarRating.SIZE.values()) {
			final Label label = new Label(shell, SWT.NONE);
			label.setText("Vertical " + (enabled ? "enabled" : "disabled") + " size=" + size.toString());
			label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

			final StarRating sr = new StarRating(shell, SWT.VERTICAL | SWT.BORDER);
			sr.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
			sr.setSizeOfStars(size);
			sr.setEnabled(enabled);
			sr.setMaxNumberOfStars(5 + (enabled ? 1 : 0));
		}
	}

}
