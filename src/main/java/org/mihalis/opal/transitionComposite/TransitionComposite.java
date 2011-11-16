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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Instances of this class provide a multi-part composite. You can perform
 * transitions when you move from one part to another
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * </p>
 */
public class TransitionComposite extends Composite {

	private final List<Control> controls;
	private int selection = 0;
	private TRANSITIONS transition = TRANSITIONS.NONE;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent a composite control which will be the parent of the new
	 *            instance (cannot be null)
	 * @param style the style of control to construct
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 * 
	 */
	public TransitionComposite(final Composite parent, final int style) {
		super(parent, style);
		this.controls = new ArrayList<Control>();

		addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(final Event e) {
				TransitionComposite.this.controls.get(TransitionComposite.this.selection).setBounds(getClientArea());
			}
		});
	}

	/**
	 * Add a control to this composite
	 * 
	 * @param control control to add
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void addControl(final Control control) {
		checkWidget();
		this.controls.add(control);
		if (this.controls.size() == 1) {
			control.setVisible(true);
		} else {
			control.setVisible(false);
		}
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
	 */
	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		int width = 0, height = 0;
		for (final Control control : this.controls) {
			final Point point = control.computeSize(wHint, hHint, changed);
			width = Math.max(width, point.x);
			height = Math.max(height, point.y);
		}
		return new Point(Math.max(width, wHint), Math.max(height, hHint));
	}

	/**
	 * @return the current transition
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public TRANSITIONS getTransition() {
		checkWidget();
		return this.transition;
	}

	/**
	 * Move selection to the first control
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void moveToFirst() {
		checkWidget();
		changeSelectionTo(0);
	}

	/**
	 * Move selection to the last control
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void moveToLast() {
		checkWidget();
		changeSelectionTo(this.controls.size() - 1);
	}

	/**
	 * Move selection to the next control
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void moveToNext() {
		checkWidget();
		final int index = this.selection + 1;
		if (index == this.controls.size()) {
			return;
		}
		changeSelectionTo(index);
	}

	/**
	 * Move selection to the previous control
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void moveToPrevious() {
		checkWidget();
		final int index = this.selection - 1;
		if (index < 0) {
			return;
		}
		changeSelectionTo(index);
	}

	/**
	 * Move selection to a given index
	 * 
	 * @param index index of the new selection
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(final int index) {
		checkWidget();
		if (index < 0 || index > this.controls.size() - 1) {
			return;
		}
		changeSelectionTo(index);
	}

	/**
	 * Change current selection
	 * 
	 * @param index index of the new selection
	 */
	private void changeSelectionTo(final int index) {

		final Transition t = TransitionFactory.getTransitionFor(this.transition);
		t.performTransition(this.controls.get(this.selection), this.controls.get(index));

		this.selection = index;
		this.controls.get(this.selection).setBounds(getClientArea());
	}

	/**
	 * Move selection to a given control
	 * 
	 * @param control control newly selected
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(final Control control) {
		checkWidget();
		for (int i = 0; i < this.controls.size(); i++) {
			if (this.controls.get(i) != null && this.controls.get(i).equals(control)) {
				changeSelectionTo(i);
				return;
			}
		}
	}

	/**
	 * Move selection to the first control
	 * 
	 * @param transition new transition
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */

	public void setTransition(final TRANSITIONS transition) {
		checkWidget();
		this.transition = transition;
	}

}
