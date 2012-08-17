package org.mihalis.opal.obutton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

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

		createButtons();

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

	private static void createButtons() {
		final Label label = new Label(shell, SWT.NONE);
		label.setText("Defaut theme:");
		label.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

		final OButton button1 = new OButton(shell, SWT.PUSH);
		button1.setText("Normal button");
		button1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));

		final OButton button2 = new OButton(shell, SWT.PUSH);
		button2.setText("Text & image");
		button2.setImage(icon);
		button2.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));

		final OButton button3 = new OButton(shell, SWT.PUSH);
		button3.setImage(icon);
		button3.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));

		final OButton button4 = new OButton(shell, SWT.TOGGLE);
		button4.setText("Toggle button");
		button4.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));

		final OButton button5 = new OButton(shell, SWT.TOGGLE);
		button5.setText("Disabled");
		button5.setEnabled(false);
		button5.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));

		final int[] arrows = new int[] { SWT.LEFT, SWT.UP, SWT.RIGHT, SWT.DOWN };
		for (final int arrow : arrows) {
			final OButton button = new OButton(shell, SWT.ARROW | arrow);
			button.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
		}

	}
}
