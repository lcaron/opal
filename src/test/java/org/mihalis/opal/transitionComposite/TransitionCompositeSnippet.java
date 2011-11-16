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
package org.mihalis.opal.transitionComposite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * A simple snipper for the TransitionComposite widget
 * 
 */
public class TransitionCompositeSnippet {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));

		final Label changeTransition = new Label(shell, SWT.NONE);
		changeTransition.setText("Select your transition");
		changeTransition.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

		final Combo combo = new Combo(shell, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		for (final TRANSITIONS t : TRANSITIONS.values()) {
			combo.add(t.toString());
		}

		final TransitionComposite transitionComposite = new TransitionComposite(shell, SWT.NONE);
		final GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
		gd.widthHint = 800;
		gd.heightHint = 800;
		transitionComposite.setLayoutData(gd);

		combo.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				transitionComposite.setTransition(TRANSITIONS.valueOf(combo.getText()));
			}
		});

		combo.setText(TRANSITIONS.NONE.toString());
		transitionComposite.setTransition(TRANSITIONS.NONE);

		final String[] fileNames = { "org/mihalis/opal/imageSelector/images/Black Eyed Peas.jpg",//
				"org/mihalis/opal/imageSelector/images/Coldplay.jpg",//
				"org/mihalis/opal/imageSelector/images/Foo Fighters.jpg",//
				"org/mihalis/opal/imageSelector/images/Gorillaz.jpg",//
				"org/mihalis/opal/imageSelector/images/Green Day.jpg" };

		for (int i = 1; i < 6; i++) {
			final Composite c = new Composite(transitionComposite, SWT.BORDER);
			c.setLayout(new GridLayout(4, false));

			final Label lbl = new Label(c, SWT.NONE);
			lbl.setText("You are on step #" + i);
			lbl.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true, 4, 1));

			final Label img = new Label(c, SWT.NONE);
			final Image image = new Image(display, TransitionCompositeSnippet.class.getClassLoader().getResourceAsStream(fileNames[i - 1]));
			img.setImage(image);
			final GridData imgGd = new GridData(GridData.CENTER, GridData.CENTER, true, true, 4, 1);
			gd.widthHint = gd.heightHint = 600;
			img.setLayoutData(imgGd);

			img.addListener(SWT.Dispose, new Listener() {

				@Override
				public void handleEvent(final Event event) {
					SWTGraphicUtil.dispose(image);
				}
			});

			if (i != 1) {
				final Button first = new Button(c, SWT.PUSH);
				first.setText("Move to first step");
				first.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, false, i == 5 ? 3 : 1, 1));
				first.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(final Event event) {
						transitionComposite.moveToFirst();
					}
				});

				final Button back = new Button(c, SWT.PUSH);
				back.setText("Move to previous step");
				back.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
				back.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(final Event event) {
						transitionComposite.moveToPrevious();
					}
				});
			}

			if (i != 5) {

				final Button next = new Button(c, SWT.PUSH);
				next.setText("Move to next step");
				next.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, false, i == 1 ? 3 : 1, 1));
				next.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(final Event event) {
						transitionComposite.moveToNext();
					}
				});

				final Button last = new Button(c, SWT.PUSH);
				last.setText("Move to last step");
				last.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
				last.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(final Event event) {
						transitionComposite.moveToLast();
					}
				});

			}
			transitionComposite.addControl(c);

		}

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