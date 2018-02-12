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
package org.mihalis.opal.propertyTable;

import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.mihalis.opal.propertyTable.editor.PTCheckboxEditor;
import org.mihalis.opal.propertyTable.editor.PTColorEditor;
import org.mihalis.opal.propertyTable.editor.PTComboEditor;
import org.mihalis.opal.propertyTable.editor.PTDateEditor;
import org.mihalis.opal.propertyTable.editor.PTDimensionEditor;
import org.mihalis.opal.propertyTable.editor.PTDirectoryEditor;
import org.mihalis.opal.propertyTable.editor.PTFileEditor;
import org.mihalis.opal.propertyTable.editor.PTFloatEditor;
import org.mihalis.opal.propertyTable.editor.PTFontEditor;
import org.mihalis.opal.propertyTable.editor.PTInsetsEditor;
import org.mihalis.opal.propertyTable.editor.PTIntegerEditor;
import org.mihalis.opal.propertyTable.editor.PTPasswordEditor;
import org.mihalis.opal.propertyTable.editor.PTRectangleEditor;
import org.mihalis.opal.propertyTable.editor.PTSpinnerEditor;
import org.mihalis.opal.propertyTable.editor.PTURLEditor;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * This snippet demonstrates the PropertyTable widget
 *
 */
public class PropertyTableProblem {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		Locale.setDefault(Locale.ENGLISH);

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("PropertyTable snippet");
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		final PropertyTable table = new PropertyTable(shell, SWT.NONE);

		table.addProperty(addTestProp("1 - Category One", "prop1", "Property for One", "Test Val 1"));
		table.addProperty(addTestProp("1 - Category One", "prop2", "For One Property", "Test Val 2"));
		table.addProperty(addTestProp("2 - Category Two", "prop3", "Property for Two", "Testing"));
		table.addProperty(addTestProp("2 - Category Two", "prop4", "Property also for Two", "blah blah"));
		table.addProperty(addTestProp("2 - Category Two", "prop5", "Property next", "ROIPREOIT"));
		table.addProperty(addTestProp("3 - Category Three", "prop6", "Property Three", "POIPOIP"));
		table.addProperty(addTestProp("3 - Category Three", "prop7", "Property also Three", "NONSENSE"));
		table.addProperty(addTestProp("4 - Category Four", "prop8", "Property Four", "BLAH BLAH BLAH"));
		table.addProperty(addTestProp("5 - Category Five", "prop9", "Property Five", "WHAT IS HAPPENING"));
		table.addProperty(addTestProp("5 - Category Five", "prop10", "Property also Five", "TEST TEST"));
		table.addProperty(addTestProp("5 - Category Five", "prop11", "Property as well Five", "HELLO"));
		table.addProperty(addTestProp("6 - Category Six", "prop12", "Property Six", "HOW ARE YOU"));
		table.addProperty(addTestProp("6 - Category Six", "prop13", "Property Six 2", "ALSKDJLKJAD"));
		table.addProperty(addTestProp("7 - Category Seven", "prop14", "Property Seven", "ZXNCUOU UOU UUOUO"));
		table.addProperty(addTestProp("7 - Category Seven", "prop15", "Property Seven too", "ODD RESULTS"));
		table.addProperty(addTestProp("8 - Category Eight", "prop16", "Property Eight1", "KDLKAJDLKJL"));
		table.addProperty(addTestProp("8 - Category Eight", "prop17", "Property Eigh2t", "LKLKJLWEWEW"));
		table.addProperty(addTestProp("8 - Category Eight", "prop18", "Property Eight3", "RRRRRRRRRRRR"));
		table.addProperty(addTestProp("9 - Category Nine", "prop19", "Property Nine", "YYYYYYYYYYYYY"));
		table.addProperty(addTestProp("9 - Category Nine", "prop20", "Property Nine", "AAAAAAAAAA"));
		table.addProperty(addTestProp("10 - Category Ten", "prop22", "Property Ten", "EEEEEEEEEEEE"));
		table.addProperty(addTestProp("10 - Category Ten", "prop21", "Property Ten - last", "THIS SHOULD BE LAST"));

		final List<PTProperty> propertyList = table.getPropertiesAsList();

		for (final PTProperty nextP : propertyList) {
			System.out.println(nextP.getCategory() + " : " + nextP.getValue());
		}

		final Button btn = new Button(shell, SWT.PUSH);
		btn.setText("Unsort");
		btn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				table.unsort();
			}

		});

		shell.setSize(800, 600);
		shell.open();
		SWTGraphicUtil.centerShell(shell);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private static PTProperty addTestProp(String cat, String strkey, String name, String myval) {
		final PTProperty retPTP = new PTProperty(strkey, name, "this is where the help goes", myval);
		retPTP.setCategory(cat);
		retPTP.setEnabled(true);
		return retPTP;
	}

	/**
	 * Build a property table
	 *
	 * @param tabFolder tabFolder that holds the property table
	 * @param showButton if <code>true</code>, show buttons
	 * @param showAsCategory if <code>true</code>, show property as categories.
	 *            If <code>false</code>, show property as a flat list
	 * @param showDescription if <code>true</code>, show description
	 * @return a property table
	 */
	private static PropertyTable buildPropertyTable(final TabFolder tabFolder, final boolean showButton, final boolean showAsCategory, final boolean showDescription) {
		final PropertyTable table = new PropertyTable(tabFolder, SWT.NONE);

		if (showButton) {
			table.showButtons();
		} else {
			table.hideButtons();
		}

		if (showAsCategory) {
			table.viewAsCategories();
		} else {
			table.viewAsFlatList();
		}

		if (showDescription) {
			table.showDescription();
		} else {
			table.hideDescription();
		}
		table.addProperty(new PTProperty("id", "Identifier", "Description for identifier", "My id")).setCategory("General");
		table.addProperty(new PTProperty("text", "Description", "Description for the description field", "blahblah...")).setCategory("General");
		table.addProperty(new PTProperty("url", "URL:", "This is a nice <b>URL</b>", "http://www.google.com").setCategory("General")).setEditor(new PTURLEditor());
		table.addProperty(new PTProperty("password", "Password", "Enter your <i>password</i> and keep it secret...", "password")).setCategory("General").setEditor(new PTPasswordEditor());

		table.addProperty(new PTProperty("int", "An integer", "Type any integer", "123")).setCategory("Number").setEditor(new PTIntegerEditor());
		table.addProperty(new PTProperty("float", "A float", "Type any float", "123.45")).setCategory("Number").setEditor(new PTFloatEditor());
		table.addProperty(new PTProperty("spinner", "Another integer", "Use a spinner to enter an integer")).setCategory("Number").setEditor(new PTSpinnerEditor(0, 100));

		table.addProperty(new PTProperty("directory", "Directory", "Select a directory")).setCategory("Directory/File").setEditor(new PTDirectoryEditor());
		table.addProperty(new PTProperty("file", "File", "Select a file")).setCategory("Directory/File").setEditor(new PTFileEditor());

		table.addProperty(new PTProperty("comboReadOnly", "Combo (read-only)", "A simple combo with seasons")).setCategory("Combo").setEditor(new PTComboEditor(true, new Object[] { "Spring", "Summer", "Autumn", "Winter" }));
		table.addProperty(new PTProperty("combo", "Combo", "A combo that is not read-only")).setCategory("Combo").setEditor(new PTComboEditor("Value 1", "Value 2", "Value 3"));

		table.addProperty(new PTProperty("cb", "Checkbox", "A checkbox")).setCategory("Checkbox").setEditor(new PTCheckboxEditor()).setCategory("Checkbox");
		table.addProperty(new PTProperty("cb2", "Checkbox (disabled)", "A disabled checkbox...")).setEditor(new PTCheckboxEditor()).setCategory("Checkbox").setEnabled(false);

		table.addProperty(new PTProperty("color", "Color", "Pick it !")).setCategory("Misc").setEditor(new PTColorEditor());
		table.addProperty(new PTProperty("font", "Font", "Pick again my friend")).setEditor(new PTFontEditor()).setCategory("Misc");
		table.addProperty(new PTProperty("dimension", "Dimension", "A dimension is composed of a width and a height")).setCategory("Misc").setEditor(new PTDimensionEditor());
		table.addProperty(new PTProperty("rectangle", "Rectangle", "A rectangle is composed of a position (x,y) and a dimension(width,height)")).setCategory("Misc").setEditor(new PTRectangleEditor());
		table.addProperty(new PTProperty("inset", "Inset", "An inset is composed of the following fields:top,left,bottom,right)")).setCategory("Misc").setEditor(new PTInsetsEditor());
		table.addProperty(new PTProperty("date", "Date", "Well, is there something more to say ?")).setCategory("Misc").setEditor(new PTDateEditor());

		return table;
	}

}
