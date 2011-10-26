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
package org.mihalis.opal.imageSelector;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * A simple snipper for the Image Selector widget
 * 
 */
public class ImageSelectorSnippet {
	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		// Create the list of images
		final List<ISItem> items = new LinkedList<ISItem>();
		items.add(new ISItem("Black Eyed Peas", "org/mihalis/opal/imageSelector/images/Black Eyed Peas.jpg"));
		items.add(new ISItem("Coldplay", "org/mihalis/opal/imageSelector/images/Coldplay.jpg"));
		items.add(new ISItem("Foo Fighters", "org/mihalis/opal/imageSelector/images/Foo Fighters.jpg"));
		items.add(new ISItem("Gorillaz", "org/mihalis/opal/imageSelector/images/Gorillaz.jpg"));
		items.add(new ISItem("Green Day", "org/mihalis/opal/imageSelector/images/Green Day.jpg"));
		items.add(new ISItem("Moby", "org/mihalis/opal/imageSelector/images/Moby.jpg"));
		items.add(new ISItem("Norah Jones", "org/mihalis/opal/imageSelector/images/Norah Jones.jpg"));
		items.add(new ISItem("Shivaree", "org/mihalis/opal/imageSelector/images/Shivaree.jpg"));
		items.add(new ISItem("Sin City", "org/mihalis/opal/imageSelector/images/Sin City.jpg"));

		final ImageSelector imageSelector = new ImageSelector(shell, SWT.NONE);
		imageSelector.setItems(items);

		// Open the shell
		shell.setSize(640, 360);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
