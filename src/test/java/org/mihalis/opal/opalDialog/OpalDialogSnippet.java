/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation
 *     Eugene Ryzhikov - Author of the Oxbow Project (http://code.google.com/p/oxbow/) - Inspiration
 *******************************************************************************/
package org.mihalis.opal.opalDialog;

import java.math.BigDecimal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mihalis.opal.opalDialog.Dialog.OpalDialogType;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * This snippet demonstrates the OpalDialog component
 * 
 */
public class OpalDialogSnippet {
	public static void main(final String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setText("Dialog Sample");
		shell.setLayout(new GridLayout(3, true));

		final Button button1 = new Button(shell, SWT.PUSH);
		button1.setText("Hello world !");
		button1.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button1.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displayHelloWorld();
			}
		});

		final Button button2 = new Button(shell, SWT.PUSH);
		button2.setText("Crash and burn");
		button2.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button2.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displayCrashAndBurn();
			}
		});

		final Button button3 = new Button(shell, SWT.PUSH);
		button3.setText("You won !");
		button3.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button3.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displayYouWon();
			}
		});

		final Button button4 = new Button(shell, SWT.PUSH);
		button4.setText("Confirm exit");
		button4.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button4.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displayConfirmExit();
			}
		});

		final Button button5 = new Button(shell, SWT.PUSH);
		button5.setText("Radio choice");
		button5.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button5.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displayRadioChoice();
			}
		});

		final Button button6 = new Button(shell, SWT.PUSH);
		button6.setText("Exception viewer");
		button6.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button6.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displayException();
			}
		});

		final Button button7 = new Button(shell, SWT.PUSH);
		button7.setText("Input box");
		button7.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button7.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displayInput();
			}
		});

		final Button button8 = new Button(shell, SWT.PUSH);
		button8.setText("Choice...");
		button8.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button8.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displayChoice();
			}
		});

		final Button button9 = new Button(shell, SWT.PUSH);
		button9.setText("Delayed quit");
		button9.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button9.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displayDelayedQuit();
			}
		});

		final Button button10 = new Button(shell, SWT.PUSH);
		button10.setText("Progress bar");
		button10.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button10.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displayProgressBar();
			}
		});

		final Button button11 = new Button(shell, SWT.PUSH);
		button11.setText("Complex Example 1");
		button11.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button11.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displaySecurityWarning();
			}
		});

		final Button button12 = new Button(shell, SWT.PUSH);
		button12.setText("Complex Example 2");
		button12.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button12.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displayComplex();
			}
		});

		final Button button13 = new Button(shell, SWT.PUSH);
		button13.setText("Large Text Example");
		button13.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		button13.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				displayLargeText();
			}
		});

		// Open the shell
		shell.pack();
		SWTGraphicUtil.centerShell(shell);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}

	private static void displayHelloWorld() {
		final Dialog dialog = new Dialog();
		dialog.getMessageArea().setText("Hello world");
		dialog.setButtonType(OpalDialogType.OK);
		dialog.show();

	}

	private static void displayCrashAndBurn() {
		Dialog.error("CRASH AND BURN !", "The application has performed an illegal action. This action has been logged and reported.");
	}

	private static void displayYouWon() {
		Dialog.inform("You've won!", "The game is over with the 15:3 score");

	}

	private static void displayConfirmExit() {
		final boolean confirm = Dialog.isConfirmed("Are you sure you want to quit?", "Please do not quit yet!");
		System.out.println("Choice is..." + confirm);
	}

	private static void displayRadioChoice() {
		final int choice = Dialog.radioChoice("You've got selection to make", "Go ahead", 1, "Yes", "No", "May be");
		System.out.println("Choice is..." + choice);
	}

	private static void displayException() {

		try {
			new BigDecimal("seven");
		} catch (final Throwable ex) {
			Dialog.showException(ex);
		}
	}

	private static void displayChoice() {
		final int choice = Dialog.choice("What do you want to do with your game in\nprogress?", "", 1, new ChoiceItem("Exit and save my game", "Save your game in progress, then exit. " + "This will\noverwrite any previously saved games."),
				new ChoiceItem("Exit and don't save", "Exit without saving your game. " + "This is counted\nas a loss in your statistics."), new ChoiceItem("Don't exit", "Return to your game progress"));
		System.out.println("Choice is..." + choice);
	}

	private static void displayDelayedQuit() {
		final boolean choice = Dialog.isConfirmed("Are you sure you want to quit?", "Please do not quit yet!", 10);
		System.out.println("Choice is..." + choice);

	}

	private static void displaySecurityWarning() {
		final Dialog dialog = new Dialog();
		dialog.setTitle("Security Warning");
		dialog.setMinimumWidth(400);
		dialog.getMessageArea().setTitle("The publisher cannot be verified.\nDo you want to run this software?") //
				.setIcon(Display.getCurrent().getSystemImage(SWT.ICON_WARNING)) //
				.setText("Name: C:\\Program Files\\eclipse\\eclipse.exe<br/>" + //
						"Publisher: <b>Unknown Publisher</b><br/>" + //
						"Type: Application<br/>");

		dialog.getFooterArea().addCheckBox("Always ask before opening this file", false).setButtonLabels("Run", "Cancel");
		dialog.show();

		System.out.println("The choice is " + dialog.getSelectedButton() + ", the checkbox value is " + dialog.getCheckboxValue());

	}

	private static void displayProgressBar() {
		final Dialog dialog = new Dialog();
		dialog.setTitle("Copying...");
		dialog.setMinimumWidth(400);
		dialog.getMessageArea().setTitle("Copying files") //
				.setIcon(Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION)) //
				.setText("Location : from 'Others' to 'Others'<br/>" + //
						"File Name : <b>photo.jpg</b>").//
				addProgressBar(0, 100, 0);

		final int[] counter = new int[1];
		counter[0] = 10;

		Display.getCurrent().timerExec(500, new Runnable() {

			@Override
			public void run() {
				dialog.getMessageArea().setProgressBarValue(counter[0]);
				counter[0] += 10;
				if (counter[0] < 120) {
					Display.getCurrent().timerExec(500, this);
				} else {
					dialog.close();
				}
			}
		});

		dialog.show();

	}

	private static void displayInput() {
		final String input = Dialog.ask("Enter you name", "or any other text if you prefer", "Laurent CARON");
		System.out.println("Choice is..." + input);

	}

	private static void displayComplex() {
		final Dialog dialog = new Dialog();
		dialog.setTitle("Application Error");
		dialog.getMessageArea().setTitle("CRASH AND BURN !").//
				setText("The application has performed an illegal action. This action has been logged and reported.").//
				setIcon(Display.getCurrent().getSystemImage(SWT.ICON_ERROR));
		dialog.setButtonType(OpalDialogType.OK);
		dialog.getFooterArea().setExpanded(false).addCheckBox("Don't show me this error next time", true).setDetailText("More explanations to come...");
		dialog.getFooterArea().setFooterText("Your application crashed because a developer forgot to write a unit test").//
				setIcon(SWTGraphicUtil.createImage("org/mihalis/opal/OpalDialog/warning.png"));
		dialog.show();

	}

	private static void displayLargeText() {
		final StringBuilder stringBuilder = new StringBuilder();
		for (int t = 0; t < 20; t++) {
			stringBuilder.append("A <b>very</b> long tekst " + t + "");
			stringBuilder.append("A <b>very</b> long tekst " + t + "");
			stringBuilder.append("A <b>very</b> long tekst " + t + "");
			stringBuilder.append("A <b>very</b> long tekst " + t + "");
			stringBuilder.append("A <b>very</b> long tekst " + t + "");
			stringBuilder.append("A <b>very</b> long tekst " + t + "");
			stringBuilder.append("A <b>very</b> long tekst " + t + "");
			stringBuilder.append("A <b>very</b> long tekst " + t + "");
			stringBuilder.append("A <b>very</b> long tekst " + t + "");
			stringBuilder.append("A very long tekst " + t + "<br/>");
			stringBuilder.append("..." + "<br/>");
		}

		final Dialog dialog = new Dialog(true);
		dialog.getMessageArea().setVerticalScrollbar(true);
		dialog.getMessageArea().setHeight(200);
		dialog.getMessageArea().setText(stringBuilder.toString());
		dialog.setButtonType(OpalDialogType.OK);
		dialog.show();
	}

}
