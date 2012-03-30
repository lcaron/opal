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
 * Instances of this class represent a sample that contains the CPU usage
 */
public class CPUUsageSample implements Sample {

	private static final String PROCESS_CPU_TIME = "ProcessCpuTime";
	private static final String OBJECT_NAME_ATTRIBUTE = "java.lang:type=OperatingSystem";
	private final MBeanServerConnection mBeanServerConnection = ManagementFactory.getPlatformMBeanServer();
	private ObjectName objectName;
	private long time;
	private long processTime;

	/**
	 * Constructor
	 */
	public CPUUsageSample() {
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
			float f = ((Long) this.mBeanServerConnection.getAttribute(this.objectName, PROCESS_CPU_TIME)).longValue() - this.processTime;
			f /= System.nanoTime() - this.time;
			this.time = System.nanoTime();
			this.processTime = ((Long) this.mBeanServerConnection.getAttribute(this.objectName, PROCESS_CPU_TIME)).longValue();
			return f;
		} catch (final Exception localException) {
			throw new RuntimeException(localException);
		}
	}

	/**
	 * @see org.mihalis.opal.systemMonitor.Sample#getMaxValue()
	 */
	@Override
	public double getMaxValue() {
		return 1.0d;
	}

}
