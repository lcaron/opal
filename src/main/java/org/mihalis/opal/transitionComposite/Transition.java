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
 * This interface describes a transition
 */
interface Transition {

	/**
	 * Performs a transition between 2 controls of a TransitionComposite
	 * 
	 * @param first first control
	 * @param second second control
	 */
	void performTransition(Control first, Control second);

}
