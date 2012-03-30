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

/**
 * In this transition, the second control is behind, the first control is
 * sliding from down to up
 */
public class DownUpAppearTransition extends VerticalTransition {

	/**
	 * @see org.mihalis.opal.transitionComposite.VerticalTransition#getCoeff()
	 */
	@Override
	protected int getCoeff() {
		return -1;
	}

	/**
	 * @see org.mihalis.opal.transitionComposite.VerticalTransition#secondIsBehind()
	 */
	@Override
	protected boolean secondIsBehind() {
		return false;
	}
}
