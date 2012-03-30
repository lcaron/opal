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
import javax.management.openmbean.CompositeDataSupport;

/**
 * Instances of this class represent a sample that contains the Thread Usage of
 * the running application
 */
public class HeapMemorySample implements Sample {
	private static final String COMMITTED = "committed";
	private static final String USED = "used";
	private static final String HEAP_MEMORY_USAGE = "HeapMemoryUsage";
	private static final String OBJECT_NAME_ATTRIBUTE = "java.lang:type=Memory";
	private final MBeanServerConnection mBeanServerConnection = ManagementFactory.getPlatformMBeanServer();
	private final ObjectName objectName;

	/**
	 * Constructor
	 */
	HeapMemorySample() {
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
			return ((Long) ((CompositeDataSupport) this.mBeanServerConnection.getAttribute(this.objectName, HEAP_MEMORY_USAGE)).get(USED)).longValue() / 1024.0D / 1024.0D;
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
			return ((Long) ((CompositeDataSupport) this.mBeanServerConnection.getAttribute(this.objectName, HEAP_MEMORY_USAGE)).get(COMMITTED)).longValue() / 1024.0D / 1024.0D;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
