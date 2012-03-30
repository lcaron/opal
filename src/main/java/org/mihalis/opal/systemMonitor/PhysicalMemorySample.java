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

import java.lang.management.ManagementFactory;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

/**
 * Instances of this class represent a sample that contains the OS memory usage
 */
public class PhysicalMemorySample implements Sample {
	private static final String TOTAL_PHYSICAL_MEMORY_SIZE = "TotalPhysicalMemorySize";
	private static final String FREE_PHYSICAL_MEMORY_SIZE = "FreePhysicalMemorySize";
	private static final String OBJECT_NAME_ATTRIBUTE = "java.lang:type=OperatingSystem";
	private final MBeanServerConnection mBeanServerConnection = ManagementFactory.getPlatformMBeanServer();
	private ObjectName objectName;

	/**
	 * Constructor
	 */
	PhysicalMemorySample() {
		try {
			this.objectName = new ObjectName(OBJECT_NAME_ATTRIBUTE);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see org.mihalis.opal.systemMonitor.Sample#getValue()
	 */
	@Override
	public double getValue() {
		try {
			final double memSize = ((Long) this.mBeanServerConnection.getAttribute(this.objectName, FREE_PHYSICAL_MEMORY_SIZE)).longValue() / 1024.0d / 1024.0d;
			return getMaxValue() - memSize;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see org.mihalis.opal.systemMonitor.Sample#getMaxValue()
	 */
	@Override
	public double getMaxValue() {
		try {
			return ((Long) this.mBeanServerConnection.getAttribute(this.objectName, TOTAL_PHYSICAL_MEMORY_SIZE)).longValue() / 1024.0d / 1024.0d;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
