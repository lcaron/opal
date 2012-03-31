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

import java.util.Random;

import org.mihalis.opal.systemMonitor.Sample;

/**
 * A random sample
 */
public class RandomSample implements Sample {

	@Override
	public double getValue() {
		return new Random().nextInt(100);
	}

	@Override
	public double getMaxValue() {
		return 99d;
	}

}
