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
 * This class is a transition factory
 */
public class TransitionFactory {

	/**
	 * @param transition transition to get
	 * @return a transition corresponding to the <code>transition</code>
	 *         parameter
	 */
	public static Transition getTransitionFor(final TRANSITIONS transition) {
		switch (transition) {
		case DOWN_TO_UP:
			return new DownUpTransition();
		case DOWN_TO_UP_AND_APPEAR:
			return new DownUpAppearTransition();
		case LEFT_TO_RIGHT:
			return new LeftRightTransition();
		case LEFT_TO_RIGHT_AND_APPEAR:
			return new LeftRightAppearTransition();
		case NONE:
			return new NoTransition();
		case RIGHT_TO_LEFT:
			return new RightLeftTransition();
		case RIGHT_TO_LEFT_AND_APPEAR:
			return new RightLeftAppearTransition();
		case UP_TO_DOWN:
			return new UpDownTransition();
		case UP_TO_DOWN_AND_APPEAR:
			return new UpDownAppearTransition();
		}
		return null;
	}

}
