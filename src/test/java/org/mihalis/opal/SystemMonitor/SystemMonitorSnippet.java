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
package org.mihalis.opal.SystemMonitor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mihalis.opal.systemMonitor.SampleIdentifier;
import org.mihalis.opal.systemMonitor.SystemMonitor;

/**
 * A simple snippet for the SystemMonitor Widget
 */
public class SystemMonitorSnippet {
	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(3, false));

		final SystemMonitor allWithoutCaption = new SystemMonitor(shell, SWT.NONE);
		allWithoutCaption.displayAll();
		allWithoutCaption.setCaptionVisible(false);
		allWithoutCaption.setLayoutData(createLayoutData());

		final SystemMonitor cpu = new SystemMonitor(shell, SWT.NONE, SampleIdentifier.CPU_USAGE);
		cpu.setLayoutData(createLayoutData());

		final SystemMonitor heap = new SystemMonitor(shell, SWT.NONE, SampleIdentifier.HEAP_MEMORY);
		heap.setLayoutData(createLayoutData());

		final SystemMonitor physical = new SystemMonitor(shell, SWT.NONE, SampleIdentifier.PHYSICAL_MEMORY);
		physical.setLayoutData(createLayoutData());

		final SystemMonitor threads = new SystemMonitor(shell, SWT.NONE, SampleIdentifier.THREADS);
		threads.setLayoutData(createLayoutData());

		final SystemMonitor custom = new SystemMonitor(shell, SWT.NONE);
		custom.addSample("custom", new RandomSample());
		custom.setCaption("custom", "Random value:");
		custom.setColor("custom", new RGB(255, 255, 216));
		custom.setFormatPattern("custom", "%{value},.0f / %{maxValue},.0f / %{percentValue}.0f%%");
		custom.setLayoutData(createLayoutData());

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * @return a layout data
	 */
	private static GridData createLayoutData() {
		final GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
		gd.widthHint = 500;
		gd.heightHint = 400;
		return gd;
	}
}
