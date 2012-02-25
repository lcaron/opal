/*******************************************************************************
 * Copyright (c) 2011 Luis Carlos Moreira da Costa.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luis Carlos Moreira da Costa (tcljava at gmail dot com) - initial API and implementation
 *     Laurent CARON (laurent.caron at gmail dot com) - code review
 *******************************************************************************/
package org.mihalis.opal.stylebutton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Snippet for StyleButton widget
 */
public final class StyleButtonSnippet {

	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		final RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		rowLayout.justify = true;
		shell.setLayout(rowLayout);
		final RowData rowData = new RowData();
		rowData.width = 50;

		// Style button SWT.NONE
		final StyleButton styleButton = new StyleButton(shell, SWT.NONE);
		styleButton.setLayoutData(rowData);
		styleButton.setVisible(true);
		styleButton.setSquare(true);
		styleButton.setSelection(true);
		styleButton.setPolygon(new int[] { 10, 10, 20, 20 }, shell.getDisplay().getSystemColor(SWT.COLOR_GREEN));
		styleButton.setPolygon(new int[] { 10, 20 }, shell.getDisplay().getSystemColor(SWT.COLOR_GREEN));

		// Style button StyleButton.ARROW_LEFT
		final StyleButton styleButton2 = new StyleButton(shell, StyleButton.ARROW_LEFT);
		styleButton2.setLayoutData(rowData);
		styleButton2.setVisible(true);
		styleButton2.setSquare(true);
		styleButton2.setSelection(true);

		// Style button StyleButton.ARROW_RIGHT
		final StyleButton styleButton3 = new StyleButton(shell, StyleButton.ARROW_RIGHT);
		styleButton3.setLayoutData(rowData);
		styleButton3.setVisible(true);
		styleButton3.setSquare(true);
		styleButton3.setSelection(true);

		// Style button StyleButton.ARROW_UP
		final StyleButton styleButton4 = new StyleButton(shell, StyleButton.ARROW_UP);
		styleButton4.setLayoutData(rowData);
		styleButton4.setVisible(true);
		styleButton4.setSquare(true);
		styleButton4.setSelection(true);

		// Style button StyleButton.ARROW_DOWN
		final StyleButton styleButton5 = new StyleButton(shell, StyleButton.ARROW_DOWN);
		styleButton5.setLayoutData(rowData);
		styleButton5.setVisible(true);
		styleButton5.setSquare(true);
		styleButton5.setSelection(true);

		// Style button StyleButton.ADD
		final StyleButton styleButton6 = new StyleButton(shell, StyleButton.ADD);
		styleButton6.setLayoutData(rowData);
		styleButton6.setVisible(true);
		styleButton6.setSquare(true);
		styleButton6.setSelection(true);

		// Style button StyleButton.SUBTRACT
		final StyleButton styleButton7 = new StyleButton(shell, StyleButton.SUBTRACT);
		styleButton7.setLayoutData(rowData);
		styleButton7.setVisible(true);
		styleButton7.setSquare(true);
		styleButton7.setSelection(true);

		// Style button StyleButton.OK
		final StyleButton styleButton8 = new StyleButton(shell, StyleButton.OK);
		styleButton8.setLayoutData(rowData);
		styleButton8.setVisible(true);
		styleButton8.setSquare(true);
		styleButton8.setSelection(true);

		// Style button StyleButton.CANCEL
		final StyleButton styleButton9 = new StyleButton(shell, StyleButton.CANCEL);
		styleButton9.setLayoutData(rowData);
		styleButton9.setVisible(true);
		styleButton9.setSquare(true);
		styleButton9.setSelection(true);

		// Main event loop.
		shell.setSize(320, 70);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}