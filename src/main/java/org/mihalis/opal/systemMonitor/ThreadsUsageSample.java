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
 * Instances of this class represent a sample that contains the Thread Usage of
 * the running application
 */
public class ThreadsUsageSample implements Sample {
	private static final String PEAK_THREAD_COUNT = "PeakThreadCount";
	private static final String THREAD_COUNT = "ThreadCount";
	private static final String OBJECT_NAME_ATTRIBUTE = "java.lang:type=Threading";
	private final MBeanServerConnection mBeanServerConnection = ManagementFactory.getPlatformMBeanServer();
	private ObjectName objectName;

	/**
	 * Constructor
	 */
	ThreadsUsageSample() {
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
			return ((Integer) this.mBeanServerConnection.getAttribute(this.objectName, THREAD_COUNT)).intValue();
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
			return ((Integer) this.mBeanServerConnection.getAttribute(this.objectName, PEAK_THREAD_COUNT)).intValue();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
