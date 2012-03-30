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
 * Instances of this class are vertical transitions (down>up, up>down...)
 */
abstract class VerticalTransition implements Transition {

	/**
	 * @see org.mihalis.opal.transitionComposite.Transition#performTransition(org.eclipse.swt.widgets.Control,
	 *      org.eclipse.swt.widgets.Control)
	 */
	@Override
	public void performTransition(final Control first, final Control second) {
		if (first.isDisposed() || second.isDisposed()) {
			return;
		}

		final int[] currentPosition = new int[1];
		currentPosition[0] = 0;
		final int maxValue = first.getParent().getClientArea().height;

		first.setBounds(0, 0, first.getBounds().width, first.getBounds().height);
		second.setBounds(0, -1 * second.getBounds().width, second.getBounds().width, second.getBounds().height);

		first.moveAbove(second);
		second.setVisible(true);

		if (first.isDisposed() || first.getDisplay().isDisposed()) {
			return;
		}

		first.getDisplay().timerExec(0, new Runnable() {

			@Override
			public void run() {

				if (first.isDisposed() || second.isDisposed()) {
					return;
				}

				currentPosition[0] = currentPosition[0] + 10;
				if (currentPosition[0] > maxValue) {
					first.setVisible(false);
					second.setBounds(0, 0, first.getBounds().width, first.getBounds().height);

					return;
				}

				first.setBounds(0, getCoeff() * currentPosition[0], first.getBounds().width, first.getBounds().height);
				if (secondIsBehind()) {
					second.setBounds(0, -1 * getCoeff() * (maxValue - currentPosition[0]), second.getBounds().width, second.getBounds().height);
				}
				if (!first.isDisposed() && !first.getDisplay().isDisposed()) {
					first.getDisplay().timerExec(15, this);
				}
			}

		});

	}

	/**
	 * @return the multiplicator coefficient
	 */
	protected abstract int getCoeff();

	/**
	 * @return <code>true</code> if the second composite is behind the first
	 *         one, <code>false</code> otherwise
	 */
	protected abstract boolean secondIsBehind();
}
