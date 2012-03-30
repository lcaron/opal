/*******************************************************************************
 * Copyright (c) 2011 Luis Carlos Moreira da Costa.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luis Carlos Moreira da Costa (tcljava at gmail dot com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.dynamictablecolumns;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

/**
 * 
 * DynamicColumnsSnippet.
 * 
 */
public final class DynamicTableColumnsSnippet {

	private static int idCount = 1;

	private static String[] firstNameSet = { "Luis Carlos", "Laurent", "Getulio", "Nicholas" };
	private static String[] lastNameSet = { "Moreira da Costa", "Caron", "Moreira da Costa", "Rocha da Costa" };
	private static String[] birthDateSet = { "1967", "1974", "1939", "2001" };

	private static Shell shell;

	private static DynamicTable tblDyn;
	private static DynamicTableColumn tblcId;
	private static DynamicTableColumn tblcFirstName;
	private static DynamicTableColumn tblcLastName;
	private static DynamicTableColumn tblcAge;

	private static Composite pnlButtons;
	private static Button btnAdd;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Display display = new Display();
		shell = new Shell(display);
		shell.setText("DynamicColumns SWT Usage Snippet");
		final GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		shell.setLayout(layout);

		createContents();

		// Initial Content
		createPerson(0);
		createPerson(1);
		createPerson(2);
		createPerson(3);
		createPerson(3);
		createPerson(3);

		shell.open();
		shell.pack();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * Create contents
	 */
	private static void createContents() {
		// Create a Dynamic Table
		tblDyn = new DynamicTable(shell, SWT.BORDER | SWT.FULL_SELECTION);
		tblDyn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tblDyn.setHeaderVisible(true);
		tblDyn.setLinesVisible(true);
		{
			tblcId = new DynamicTableColumn(tblDyn, SWT.NONE);
			tblcId.setText("Id");
			tblcId.setWidth("25px");

			tblcFirstName = new DynamicTableColumn(tblDyn, SWT.NONE);
			tblcFirstName.setText("First Name");
			tblcFirstName.setWidth("50%", "100px");

			tblcLastName = new DynamicTableColumn(tblDyn, SWT.NONE);
			tblcLastName.setText("Last Name");
			tblcLastName.setWidth("50%", "100px");

			tblcAge = new DynamicTableColumn(tblDyn, SWT.NONE);
			tblcAge.setText("Age");
			tblcAge.setWidth("60px");
		}

		pnlButtons = new Composite(shell, SWT.NONE);
		pnlButtons.setLayout(new GridLayout(1, false));
		pnlButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		{
			btnAdd = new Button(pnlButtons, SWT.NONE);
			btnAdd.setText("Add");
			btnAdd.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					final TableItem tbli = new TableItem(tblDyn, SWT.NONE);
					tbli.setText(0, Integer.toString(idCount++));
					tbli.setText(1, firstNameSet[new Random().nextInt(4)]);
					tbli.setText(2, lastNameSet[new Random().nextInt(4)]);
					tbli.setText(3, birthDateSet[new Random().nextInt(4)]);
					tblDyn.layout();
				}
			});
		}
	}

	/**
	 * Create person
	 * 
	 * @param i int
	 */
	private static void createPerson(final int i) {
		final TableItem tbli = new TableItem(tblDyn, SWT.NONE);
		tbli.setText(0, Integer.toString(idCount++));
		tbli.setText(1, firstNameSet[i]);
		tbli.setText(2, lastNameSet[i]);
		tbli.setText(3, birthDateSet[i]);
	}
}