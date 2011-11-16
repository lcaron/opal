/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron@gmail.com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.transitionComposite;

import org.eclipse.swt.widgets.Control;

/**
 * This transition is simple, because there is no effect :)
 * 
 */
class NoTransition implements Transition {

	/**
	 * @see org.mihalis.opal.transitionComposite.Transition#performTransition(org.eclipse.swt.widgets.Control,
	 *      org.eclipse.swt.widgets.Control)
	 */
	@Override
	public void performTransition(final Control first, final Control second) {
		first.setVisible(false);
		second.setVisible(true);
	}

}
