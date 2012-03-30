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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.mihalis.opal.utils.SWTGraphicUtil;

/**
 * Instances of this class are a launcher composed of buttons. When one clicks
 * on the button, an animation is started and a selection event is fired
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 */
public class Launcher extends Composite {

	private final List<LauncherItem> items;
	private final List<SelectionListener> selectionListeners;
	private boolean needRedraw;
	private int selection = -1;

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
	public Launcher(final Composite parent, final int style) {
		super(parent, style | SWT.BORDER);
		this.items = new ArrayList<LauncherItem>();
		this.selectionListeners = new ArrayList<SelectionListener>();
		this.needRedraw = true;
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

		this.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				drawLauncher();
			}
		});

		this.addListener(SWT.KeyUp, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				OnKeyPressed(event);
			}
		});

	}

	/**
	 * Draw the launcher
	 */
	private void drawLauncher() {
		if (!this.needRedraw) {
			return;
		}

		disposePreviousContent();
		createButtons();
		pack();

		this.needRedraw = false;
	}

	/**
	 * Dispose the content before a redraw
	 */
	private void disposePreviousContent() {
		for (final Control c : this.getChildren()) {
			c.dispose();
		}
	}

	/**
	 * Create the buttons that will compose the launcher
	 */
	private void createButtons() {
		final GridLayout gridLayout = new GridLayout(this.items.size() / 2, true);
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		this.setLayout(gridLayout);
		for (final LauncherItem item : this.items) {
			final LLabel label = new LLabel(this, SWT.CENTER);
			label.setText(item.title);
			label.setImage(SWTGraphicUtil.createImage(item.image));
			label.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			final GridData gd = new GridData(GridData.FILL, GridData.FILL, true, false);
			gd.widthHint = 192;
			gd.heightHint = 220;
			label.setLayoutData(gd);
			item.label = label;

			label.addListener(SWT.KeyUp, new Listener() {

				@Override
				public void handleEvent(final Event event) {
					OnKeyPressed(event);
				}
			});

			label.addListener(SWT.MouseUp, new Listener() {

				@Override
				public void handleEvent(final Event event) {
					OnClick(event);
				}
			});

			label.addListener(SWT.MouseDoubleClick, new Listener() {

				@Override
				public void handleEvent(final Event event) {
					OnDoubleClick(event);
				}
			});

		}
	}

	/**
	 * Code executed when a key is pressed
	 * 
	 * @param event Event
	 */
	private void OnKeyPressed(final Event event) {
		switch (event.keyCode) {
		case SWT.ARROW_LEFT:
			if (this.selection == -1) {
				this.selection = 0;
				changeColor(this.selection, true);
				return;
			}

			if (this.selection % 2 == 1) {
				changeColor(this.selection, false);
				this.selection--;
				changeColor(this.selection, true);
			}
			break;
		case SWT.ARROW_UP:
			if (this.selection == -1) {
				this.selection = 0;
				changeColor(this.selection, true);
				return;
			}
			if (this.selection >= 2) {
				changeColor(this.selection, false);
				this.selection -= 2;
				changeColor(this.selection, true);
			}
			break;
		case SWT.ARROW_RIGHT:
			if (this.selection == -1) {
				this.selection = 0;
				changeColor(this.selection, true);
				return;
			}
			if (this.selection % 2 == 0) {
				changeColor(this.selection, false);
				this.selection++;
				changeColor(this.selection, true);
			}
			break;
		case SWT.ARROW_DOWN:
			if (this.selection == -1) {
				this.selection = 0;
				changeColor(this.selection, true);
				return;
			}
			if (this.selection <= this.items.size() - 2) {
				changeColor(this.selection, false);
				this.selection += 2;
				changeColor(this.selection, true);
			}
			break;
		case SWT.HOME:
			changeColor(this.selection, false);
			this.selection = 0;
			changeColor(this.selection, true);
			break;
		case SWT.END:
			changeColor(this.selection, false);
			this.selection = this.items.size() - 1;
			changeColor(this.selection, true);
			break;
		}

	}

	/**
	 * Code executed when one clicks on the button
	 * 
	 * @param event Event
	 */
	private void OnClick(final Event event) {
		for (int i = 0; i < this.items.size(); i++) {
			final LauncherItem item = this.items.get(i);
			if (item.label != null && item.label.equals(event.widget)) {
				if (this.selection != i) {
					changeColor(this.selection, false);
					this.selection = i;
					changeColor(this.selection, true);
				}
				return;
			}
		}
	}

	/**
	 * Change the background color of a given button
	 * 
	 * @param index index of the button
	 * @param isSelected if <code>true</code>, the background is the light
	 *            shadow. Otherwise, the background color is white
	 */
	private void changeColor(final int index, final boolean isSelected) {
		if (index != -1 && this.items.get(index).label != null) {
			this.items.get(index).label.setBackground(isSelected ? getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW) : getDisplay().getSystemColor(SWT.COLOR_WHITE));
		}
	}

	/**
	 * Code executed when one double-clicks on a button
	 * 
	 * @param event Event
	 */
	private void OnDoubleClick(final Event event) {
		for (int i = 0; i < this.items.size(); i++) {
			final LauncherItem item = this.items.get(i);
			if (item.label != null && item.label.equals(event.widget)) {
				if (this.selection != i) {
					changeColor(this.selection, false);
					this.selection = i;
					changeColor(this.selection, true);
				}
				startAnimation(i, event);
				return;
			}
		}
	}

	/**
	 * Start the animation for a given button
	 * 
	 * @param index index of the selected button
	 * @param event event (propagated to the selection listeners)
	 */
	private void startAnimation(final int index, final Event event) {
		final LLabel label = this.items.get(index).label;
		getDisplay().timerExec(0, new Runnable() {

			@Override
			public void run() {
				if (label.incrementAnimation()) {
					getDisplay().timerExec(20, this);
				} else {
					fireSelectionListeners(event);
				}
			}
		});

	}

	/**
	 * Fire the selection listeners
	 * 
	 * @param originalEvent mouse event
	 * @return <code>true</code> if the selection could be changed,
	 *         <code>false</code> otherwise
	 */
	private boolean fireSelectionListeners(final Event originalEvent) {
		for (final SelectionListener listener : this.selectionListeners) {
			final Event event = new Event();

			event.button = originalEvent.button;
			event.display = this.getDisplay();
			event.item = null;
			event.widget = this;
			event.data = null;
			event.time = originalEvent.time;
			event.x = originalEvent.x;
			event.y = originalEvent.y;

			final SelectionEvent selEvent = new SelectionEvent(event);
			listener.widgetSelected(selEvent);
			if (!selEvent.doit) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Add an item to the launcher
	 * 
	 * @param title text associated to this item
	 * @param image image associated to this item
	 */
	public void addItem(final String title, final String image) {
		checkWidget();
		this.items.add(new LauncherItem(title, image));
		this.needRedraw = true;
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the control is selected by the
	 * user. <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 * 
	 * @param listener the listener which should be notified
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(final SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.selectionListeners.add(listener);

	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the control is selected by the user.
	 * 
	 * @param listener the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(final SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.selectionListeners.remove(listener);
	}

	/**
	 * Return the selected button
	 * 
	 * @return the index of the selected button
	 * 
	 * @exception SWTException <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 */
	public int getSelection() {
		checkWidget();
		return this.selection;
	}
}
