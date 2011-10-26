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
package org.mihalis.opal.launcher;

/**
 * Instances of this class are POJO to store information handled by the Launcher
 * widget I could have used a inner class but I prefer this solution :)
 */
class LauncherItem {
	String title;
	String image;
	LLabel label;

	/**
	 * Constructor
	 * 
	 * @param title text associated to the item
	 * @param image image associated to the item
	 */
	LauncherItem(final String title, final String image) {
		this.title = title;
		this.image = image;
	}
}
