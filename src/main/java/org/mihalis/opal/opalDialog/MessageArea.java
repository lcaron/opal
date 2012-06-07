/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Laurent CARON (laurent.caron at gmail dot com) - Initial implementation and API Eugene Ryzhikov - Author of the Oxbow Project (http://code.google.com/p/oxbow/) - Inspiration
 *******************************************************************************/
package org.mihalis.opal.opalDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.mihalis.opal.utils.ReadOnlyStyledText;
import org.mihalis.opal.utils.SWTGraphicUtil;
import org.mihalis.opal.utils.StringUtil;

/**
 * Instances of this class are message areas
 */
public class MessageArea extends DialogArea {
	// Main composite
	private Composite composite;

	// Informations for a simple dialog box
	private String title;
	private Image icon;
	private String text;

	// Informations for a radio choice dialog box
	private int radioChoice;
	private int radioDefaultSelection;
	private String[] radioValues;

	// Informations for a exception viewer dialog box
	private Throwable exception;
	private Text textException;

	// Informations for an input dialog box
	private String textBoxValue;

	// Informations for a choice dialog box
	private int choice;
	private int choiceDefaultSelection;
	private ChoiceItem[] choiceValues;

	// Informations for a progress bar displayed in a dialog box
	private ProgressBar progressBar;
	private int progressBarMinimumValue;
	private int progressBarMaximumValue;
	private int progressBarValue;

	private boolean verticalScrollbar = false;
	private int height = -1;

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            dialog that is composed of this message area
	 */
	public MessageArea(final Dialog parent) {
		super(parent);
		this.radioChoice = -1;
		this.choice = -1;
		this.progressBarValue = -1;
	}

	/**
	 * Add a choice
	 * 
	 * @param defaultSelection
	 *            default selection
	 * @param items
	 *            a list of the choice item
	 * @return the current message area
	 */
	public MessageArea addChoice(final int defaultSelection, final ChoiceItem... items) {
		setInitialised(true);
		this.choiceDefaultSelection = defaultSelection;
		this.choiceValues = items;
		return this;
	}

	/**
	 * Add a choice composed of radio buttons
	 * 
	 * @param defaultSelection
	 *            default selection
	 * @param values
	 *            values
	 * @return the current message area
	 */
	public MessageArea addRadioButtons(final int defaultSelection, final String... values) {
		setInitialised(true);
		this.radioDefaultSelection = defaultSelection;
		this.radioValues = values;
		return this;
	}

	/**
	 * Add a text box for input
	 * 
	 * @param value
	 *            defaut value of the textbox
	 * @return the current message area
	 */
	public MessageArea addTextBox(final String value) {
		setInitialised(true);
		this.textBoxValue = value;
		return this;
	}

	/**
	 * Add a progress bar
	 * 
	 * @param mininum
	 *            minimum value
	 * @param maximum
	 *            maximum value
	 * @param value
	 *            default value
	 * @return the current message area
	 */
	public MessageArea addProgressBar(final int mininum, final int maximum, final int value) {
		setInitialised(true);
		this.progressBarMinimumValue = mininum;
		this.progressBarMaximumValue = maximum;
		this.progressBarValue = value;
		return this;
	}

	/**
	 * @see org.mihalis.opal.OpalDialog.DialogArea#render()
	 */
	@Override
	public void render() {
		if (!isInitialised()) {
			return;
		}

		this.composite = new Composite(this.parent.shell, SWT.NONE);
		this.composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		this.composite.setBackground(this.composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		final boolean hasIcon = this.icon != null;
		final boolean hasTitle = !StringUtil.isEmpty(this.title);
		final boolean hasText = !StringUtil.isEmpty(this.text);
		final boolean hasRadio = this.radioValues != null;
		final boolean hasException = this.exception != null;
		final boolean hasTextbox = this.textBoxValue != null;
		final boolean hasChoice = this.choiceValues != null;
		final boolean hasProgressBar = this.progressBarValue != -1;

		final int numberOfColumns = hasIcon ? 2 : 1;
		int numberOfRows = hasTitle && hasText ? 2 : 1;

		if (hasRadio) {
			numberOfRows += this.radioValues.length;
		}

		if (hasChoice) {
			numberOfRows += this.choiceValues.length;
		}

		if (hasException || hasTextbox) {
			numberOfRows++;
		}

		if (hasProgressBar) {
			numberOfRows++;
		}

		final GridLayout gridLayout = new GridLayout(numberOfColumns, false);
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		gridLayout.marginRight = 10;
		gridLayout.marginLeft = 10;
		gridLayout.marginTop = 10;
		gridLayout.marginBottom = 10;
		this.composite.setLayout(gridLayout);

		if (hasIcon) {
			createIcon(numberOfRows);
		}

		if (hasTitle) {
			createTitle(hasIcon);
		}

		if (hasText) {
			createText(hasIcon, hasTitle);
		}

		if (hasRadio) {
			createRadioButtons();
		}

		if (hasException) {
			createTextException();
		}

		if (hasTextbox) {
			createTextBox();
		}

		if (hasChoice) {
			createChoice();
		}

		if (hasProgressBar) {
			createProgressBar();
		}

	}

	/**
	 * Create the icon
	 * 
	 * @param numberOfRows
	 *            number of rows displayed
	 */
	private void createIcon(final int numberOfRows) {
		final Label label = new Label(this.composite, SWT.NONE);
		label.setImage(this.icon);
		label.setBackground(this.composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		label.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false, 1, numberOfRows));
	}

	/**
	 * Create the title
	 * 
	 * @param hasIcon
	 *            if <code>true</code> an icon is displayed
	 */
	private void createTitle(final boolean hasIcon) {
		final Label label = new Label(this.composite, SWT.NONE);
		label.setText(this.title);
		label.setFont(getBiggerFont());
		label.setForeground(getTitleColor());
		label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridData gd = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false, 1, 1);

		if (hasIcon) {
			gd.horizontalIndent = 8;
		} else {
			gd.horizontalIndent = 10;
			gd.verticalIndent = 10;
		}

		label.setLayoutData(gd);
	}

	/**
	 * Create the text
	 * 
	 * @param hasIcon
	 *            if <code>true</code> an icon is displayed
	 * @param hasTitle
	 *            if <code>true</code> a title is displayed
	 */
	private void createText(final boolean hasIcon, final boolean hasTitle) {

		final StyledText label = new ReadOnlyStyledText(this.composite, SWT.NONE | (this.verticalScrollbar ? SWT.V_SCROLL : SWT.NONE));
		label.setText(this.text);
		SWTGraphicUtil.applyHTMLFormating(label);
		label.setEditable(false);
		label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 1);
		if (this.height != -1) {
			gd.heightHint = this.height;
		}

		if (hasIcon) {
			gd.horizontalIndent = 8;
		} else {
			gd.horizontalIndent = 20;
			if (hasTitle) {
				gd.verticalIndent = 8;
			} else {
				gd.verticalIndent = 20;
			}
		}

		label.setLayoutData(gd);
	}

	/**
	 * Create radio buttons
	 */
	private void createRadioButtons() {
		for (int i = 0; i < this.radioValues.length; i++) {
			final Button button = new Button(this.composite, SWT.RADIO);
			button.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			button.setText(this.radioValues[i]);

			final Integer index = new Integer(i);
			button.addSelectionListener(new SelectionAdapter() {

				/**
				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
				 */
				@Override
				public void widgetSelected(final SelectionEvent e) {
					if (button.getSelection()) {
						MessageArea.this.radioChoice = index.intValue();
					}
				}

			});

			button.setSelection(i == this.radioDefaultSelection);
			final GridData gd = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false, 1, 1);
			gd.horizontalIndent = 10;
			button.setLayoutData(gd);
		}
	}

	/**
	 * Create the text that displays an exception
	 */
	private void createTextException() {
		this.textException = new Text(this.composite, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		this.textException.setText(StringUtil.stackStraceAsString(this.exception));
		this.textException.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 1);
		gd.minimumHeight = 300;
		this.textException.setLayoutData(gd);
	}

	/**
	 * Create a text box
	 */
	private void createTextBox() {
		final Text textbox = new Text(this.composite, SWT.BORDER);
		textbox.setText(this.textBoxValue);
		textbox.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 1);
		textbox.setLayoutData(gd);
		textbox.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				MessageArea.this.textBoxValue = textbox.getText();
			}
		});

		textbox.addListener(SWT.KeyUp, new Listener() {

			@Override
			public void handleEvent(Event e) {
				if (e.keyCode == SWT.CR) {
					parent.shell.dispose();
					parent.getFooterArea().selectedButtonIndex = 0;
				}

			}
		});

		textbox.getShell().addListener(SWT.Activate, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				textbox.forceFocus();
				textbox.setSelection(textbox.getText().length());
				textbox.getShell().removeListener(SWT.Activate, this);
			}
		});

	}

	/**
	 * Create a choice selection
	 */
	private void createChoice() {
		for (int i = 0; i < this.choiceValues.length; i++) {
			final ChoiceWidget choice = new ChoiceWidget(this.composite, SWT.RADIO);
			choice.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			choice.setChoiceItem(this.choiceValues[i]);

			final Integer index = new Integer(i);
			choice.addSelectionListener(new SelectionAdapter() {

				/**
				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
				 */
				@Override
				public void widgetSelected(final SelectionEvent e) {
					MessageArea.this.choice = index.intValue();
					MessageArea.this.parent.shell.dispose();
				}

			});

			choice.setSelection(i == this.choiceDefaultSelection);
			final GridData gd = new GridData(GridData.FILL, GridData.FILL, false, false, 1, 1);
			choice.setLayoutData(gd);
		}
	}

	/**
	 * Create a progress bar
	 */
	private void createProgressBar() {
		this.progressBar = new ProgressBar(this.composite, SWT.SMOOTH | SWT.HORIZONTAL);
		this.progressBar.setMinimum(this.progressBarMinimumValue);
		this.progressBar.setMaximum(this.progressBarMaximumValue);
		this.progressBar.setSelection(this.progressBarValue);
		this.progressBar.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final GridData gd = new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1);
		this.progressBar.setLayoutData(gd);
	}

	/**
	 * Hide the exception panel
	 */
	void hideException() {
		this.textException.dispose();
		this.parent.pack();

	}

	/**
	 * Show the exception panel
	 */
	void showException() {
		createTextException();
		this.parent.pack();
	}

	// ------------------------------------------- Getters & Setters

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title
	 *            the title to set
	 * @return the current message area
	 */
	public MessageArea setTitle(final String title) {
		this.title = title;
		setInitialised(true);
		return this;
	}

	/**
	 * @return the icon
	 */
	public Image getIcon() {
		return this.icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public MessageArea setIcon(final Image icon) {
		this.icon = icon;
		setInitialised(true);
		return this;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public MessageArea setText(final String text) {
		this.text = text;
		setInitialised(true);
		return this;
	}

	/**
	 * @return the radio choice
	 */
	public int getRadioChoice() {
		return this.radioChoice;
	}

	/**
	 * @return the exception
	 */
	public Throwable getException() {
		return this.exception;
	}

	/**
	 * @param exception
	 *            the exception to set
	 * @return
	 */
	public MessageArea setException(final Throwable exception) {
		this.exception = exception;
		setInitialised(true);
		return this;
	}

	/**
	 * @return the choice
	 */
	public int getChoice() {
		return this.choice;
	}

	/**
	 * @return the value stored in the text box
	 */
	public String getTextBoxValue() {
		return this.textBoxValue;
	}

	/**
	 * @return the progress bar minimum value
	 */
	public int getProgressBarMinimumValue() {
		return this.progressBarMinimumValue;
	}

	/**
	 * @param progressBarMinimumValue
	 *            the progress bar minimum value to set
	 */
	public void setProgressBarMinimumValue(final int progressBarMinimumValue) {
		this.progressBarMinimumValue = progressBarMinimumValue;
		if (this.progressBar != null && !this.progressBar.isDisposed()) {
			this.progressBar.setMinimum(progressBarMinimumValue);
		}
	}

	/**
	 * @return the progress bar maximum value
	 */
	public int getProgressBarMaximumValue() {
		return this.progressBarMaximumValue;
	}

	/**
	 * @param progressBarMaximumValue
	 *            the progress bar minimum value to set
	 */
	public void setProgressBarMaximumValue(final int progressBarMaximumValue) {
		this.progressBarMaximumValue = progressBarMaximumValue;
		if (this.progressBar != null && !this.progressBar.isDisposed()) {
			this.progressBar.setMaximum(progressBarMaximumValue);
		}
	}

	/**
	 * @return the progress bar value
	 */
	public int getProgressBarValue() {
		return this.progressBarValue;
	}

	/**
	 * @param progressBarValue
	 *            the progress bar value to set
	 */
	public void setProgressBarValue(final int progressBarValue) {
		this.progressBarValue = progressBarValue;
		if (this.progressBar != null && !this.progressBar.isDisposed()) {
			this.progressBar.setSelection(progressBarValue);
		}
	}

	/**
	 * @return the verticalScrollbar
	 */
	public boolean isVerticalScrollbar() {
		return this.verticalScrollbar;
	}

	/**
	 * @param verticalScrollbar
	 *            the verticalScrollbar to set
	 */
	public void setVerticalScrollbar(final boolean verticalScrollbar) {
		this.verticalScrollbar = verticalScrollbar;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(final int height) {
		this.height = height;
	}

}
