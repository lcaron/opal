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
package org.mihalis.opal.systemMonitor;

import org.eclipse.swt.graphics.RGB;
import org.mihalis.opal.utils.ResourceManager;

/**
 * This class is a factory that returns the built-o, samples
 */
public class SampleFactory {

	/**
	 * SampleFactory instance
	 */
	private static SampleFactory instance;

	/**
	 * Constructor
	 */
	private SampleFactory() {
	}

	/**
	 * @return the instance of the factory
	 */
	public static SampleFactory getInstance() {
		if (instance == null) {
			instance = new SampleFactory();
		}
		return instance;
	}

	/**
	 * Give a built-in sample
	 * 
	 * @param identifier Identifier
	 * @return the sample that corresponds to the identifier
	 */
	public SampleWrapper getSample(final SampleIdentifier identifier) {
		switch (identifier) {
			case CPU_USAGE: {
				final SampleWrapper sr = new SampleWrapper(new CPUUsageSample());
				sr.setColor(new RGB(128, 25, 0));
				sr.setCaption(ResourceManager.CPU_USAGE + ":");
				sr.setFormatPattern("%{percentValue}.0f%%");
				return sr;
			}
			case HEAP_MEMORY: {
				final SampleWrapper sr = new SampleWrapper(new HeapMemorySample());
				sr.setColor(new RGB(111, 83, 0));
				sr.setCaption(ResourceManager.HEAP_MEMORY + ":");
				sr.setFormatPattern("%{value},.2fMB / %{maxValue},.2fMB");
				return sr;
			}
			case PHYSICAL_MEMORY: {
				final SampleWrapper sr = new SampleWrapper(new PhysicalMemorySample());
				sr.setColor(new RGB(15, 75, 0));
				sr.setCaption(ResourceManager.PHYSICAL_MEMORY + ":");
				sr.setFormatPattern("%{value},.0fMB / %{maxValue},.0fMB");
				return sr;
			}
			default: {
				final SampleWrapper sr = new SampleWrapper(new ThreadsUsageSample());
				sr.setColor(new RGB(0, 77, 88));
				sr.setCaption(ResourceManager.THREADS + ":");
				sr.setFormatPattern("%{value},.0f / %{maxValue},.0f (Peak)");
				return sr;
			}
		}
	}

}
