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
package org.mihalis.opal.obutton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Snippet for the OButton widget
 */
public class OButtonSnippet {
	private static Shell shell;
	private static Image icon;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Display display = new Display();
		shell = new Shell(display);
		shell.setText("OButton Snippet");
		shell.setLayout(new GridLayout(10, false));

		icon = new Image(display, OButtonSnippet.class.getClassLoader().getResourceAsStream("org/mihalis/opal/obutton/user.png"));

		createButtons(DefaultButtonRenderer.getInstance(), "Defaut theme:");
		createButtons(RedButtonRenderer.getInstance(), "Red theme:");
		createButtons(GreenButtonRenderer.getInstance(), "Green theme:");
		createButtons(OrangeButtonRenderer.getInstance(), "Orange theme:");
		createButtons(PurpleButtonRenderer.getInstance(), "Purple theme:");

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		icon.dispose();
		display.dispose();

	}

	private static void createButtons(final ButtonRenderer renderer, final String text) {
		final Label label = new Label(shell, SWT.NONE);
		label.setText(text);
		label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

		final OButton button1 = new OButton(shell, SWT.PUSH);
		button1.setText("Normal button");
		button1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
		button1.setButtonRenderer(renderer);

		final OButton button2 = new OButton(shell, SWT.PUSH);
		button2.setText("Text & image");
		button2.setImage(icon);
		button2.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
		button2.setButtonRenderer(renderer);

		final OButton button3 = new OButton(shell, SWT.PUSH);
		button3.setImage(icon);
		button3.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
		button3.setButtonRenderer(renderer);

		final OButton button4 = new OButton(shell, SWT.TOGGLE);
		button4.setText("Toggle button");
		button4.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
		button4.setButtonRenderer(renderer);

		final OButton button5 = new OButton(shell, SWT.TOGGLE);
		button5.setText("Disabled");
		button5.setEnabled(false);
		button5.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
		button5.setButtonRenderer(renderer);

		final int[] arrows = new int[] { SWT.LEFT, SWT.UP, SWT.RIGHT, SWT.DOWN };
		for (final int arrow : arrows) {
			final OButton button = new OButton(shell, SWT.ARROW | arrow);
			button.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
			button.setButtonRenderer(renderer);
		}

	}
}
