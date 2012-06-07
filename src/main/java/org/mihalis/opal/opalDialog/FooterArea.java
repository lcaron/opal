/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - Initial implementation and API
 *     Eugene Ryzhikov - Author of the Oxbow Project (http://code.google.com/p/oxbow/) - Inspiration
 *******************************************************************************/
package org.mihalis.opal.opalDialog;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
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
import org.mihalis.opal.utils.ResourceManager;

/**
 * Instances of this class are message areas
 */
public class FooterArea extends DialogArea {
	private Image icon;
	private String footerText;

	private List<String> buttonLabels;
	private int defaultButtonIndex;

	private int timer;
	private int timerIndexButton;

	int selectedButtonIndex;

	private String collapsedLabelText;
	private String expandedLabelText;
	private boolean expanded;
	private String detailText;
	private boolean details;
	private Button disabledButton;

	private String checkBoxLabel;
	private boolean checkBoxValue;

	private Label expandedPanel;
	private Composite composite;

	/**
	 * Constructor
	 * 
	 * @param parent dialog that is composed of this footer area
	 */
	public FooterArea(final Dialog parent) {
		super(parent);
		this.selectedButtonIndex = -1;
		this.expandedLabelText = ResourceManager.getLabel(ResourceManager.FEWER_DETAILS);
		this.collapsedLabelText = ResourceManager.getLabel(ResourceManager.MORE_DETAILS);
		this.timer = -1;
		this.timerIndexButton = -1;
	}

	/**
	 * Add a check box
	 * 
	 * @param label label to display
	 * @param selection default value of the check box
	 * @return this footer area
	 */
	public FooterArea addCheckBox(final String label, final boolean selection) {
		this.checkBoxLabel = label;
		this.checkBoxValue = selection;
		this.setInitialised(true);
		return this;
	}

	/**
	 * @see org.mihalis.opal.OpalDialog.DialogArea#render()
	 */
	@Override
	void render() {
		if (!this.isInitialised()) {
			return;
		}

		this.createSeparator();

		this.composite = new Composite(this.parent.shell, SWT.NONE);
		this.composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		this.composite.setBackground(this.getGreyColor());

		int numberOfColumns = this.buttonLabels == null ? 0 : this.buttonLabels.size();
		if (this.details) {
			numberOfColumns += 2;
		}

		final GridLayout gridLayout = new GridLayout(numberOfColumns, false);
		gridLayout.marginHeight = gridLayout.marginWidth = 10;
		this.composite.setLayout(gridLayout);

		if (this.details) {
			this.createDetails(numberOfColumns);
		}

		if (this.buttonLabels != null) {
			this.createButtons();
		}

		if (this.details && this.parent.getMessageArea().getException() == null && this.expanded) {
			this.createExpandedPanel(numberOfColumns);
		}

		if (this.checkBoxLabel != null) {
			this.createCheckBox(numberOfColumns);
		}

		if (this.footerText != null) {
			this.createFooter();

		}

	}

	/**
	 * Create the buttons
	 */
	private void createButtons() {
		Button defaultButton = null;
		for (int i = 0; i < this.buttonLabels.size(); i++) {
			final Button button = new Button(this.composite, SWT.PUSH);
			button.setText(this.buttonLabels.get(i));

			final GridData gd = new GridData(GridData.END, GridData.CENTER, i == 0, false);
			gd.minimumWidth = 70;
			gd.widthHint = 70;
			button.setLayoutData(gd);

			if (i == this.defaultButtonIndex) {
				defaultButton = button;
			}

			final Integer integer = new Integer(i);
			button.addSelectionListener(new SelectionAdapter() {

				/**
				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
				 */
				@Override
				public void widgetSelected(final SelectionEvent e) {
					FooterArea.this.parent.shell.dispose();
					FooterArea.this.selectedButtonIndex = integer.intValue();
				}

			});

			if (i == this.timerIndexButton && this.timer != -1) {
				this.disabledButton = button;
				button.setData(button.getText());
				button.setText(button.getText() + " (" + this.timer + ")");
				button.setEnabled(false);
			}

		}

		if (this.timerIndexButton != -1 && this.timer != -1) {
			Display.getCurrent().timerExec(1000, new Runnable() {

				@Override
				public void run() {
					FooterArea.this.timer--;
					if (FooterArea.this.disabledButton.isDisposed()) {
						return;
					}

					if (FooterArea.this.timer == 0) {
						FooterArea.this.disabledButton.setText((String) FooterArea.this.disabledButton.getData());
						FooterArea.this.disabledButton.setEnabled(true);
					} else {
						FooterArea.this.disabledButton.setText(FooterArea.this.disabledButton.getData() + " (" + FooterArea.this.timer + ")");
						Display.getCurrent().timerExec(1000, this);
					}

				}
			});
		}

		this.parent.shell.setDefaultButton(defaultButton);
	}

	/**
	 * Create the details section
	 * 
	 * @param numberOfColumns
	 */
	private void createDetails(final int numberOfColumns) {
		final Label icon = new Label(this.composite, SWT.NONE);
		icon.setBackground(this.getGreyColor());
		icon.setImage(this.isExpanded() ? this.getFewerDetailsImage() : this.getMoreDetailsImage());
		icon.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));

		final Label label = new Label(this.composite, SWT.NONE);
		label.setBackground(this.getGreyColor());
		label.setText(this.isExpanded() ? this.expandedLabelText : this.collapsedLabelText);
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));

		final int numberOfColumnsParam = numberOfColumns;

		final Listener listener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (FooterArea.this.parent.getMessageArea().getException() != null) {
					if (label.getText().equals(FooterArea.this.expandedLabelText)) {
						label.setText(FooterArea.this.collapsedLabelText);
						icon.setImage(FooterArea.this.getMoreDetailsImage());
						FooterArea.this.parent.getMessageArea().hideException();
					} else {
						label.setText(FooterArea.this.expandedLabelText);
						icon.setImage(FooterArea.this.getFewerDetailsImage());
						FooterArea.this.parent.getMessageArea().showException();
					}

				} else {
					if (label.getText().equals(FooterArea.this.expandedLabelText)) {
						label.setText(FooterArea.this.collapsedLabelText);
						icon.setImage(FooterArea.this.getMoreDetailsImage());
						FooterArea.this.expandedPanel.dispose();
						FooterArea.this.parent.pack();
					} else {
						label.setText(FooterArea.this.expandedLabelText);
						icon.setImage(FooterArea.this.getFewerDetailsImage());
						FooterArea.this.createExpandedPanel(numberOfColumnsParam);
						FooterArea.this.parent.pack();
					}
				}
			}
		};

		label.addListener(SWT.MouseUp, listener);
		icon.addListener(SWT.MouseUp, listener);
	}

	/**
	 * Create a check box
	 * 
	 * @param numberOfColumns
	 */
	private void createCheckBox(final int numberOfColumns) {
		final Button button = new Button(this.composite, SWT.CHECK);
		button.setText(this.checkBoxLabel);
		button.setSelection(this.checkBoxValue);
		button.setBackground(this.getGreyColor());
		button.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false, numberOfColumns, 1));
		button.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				FooterArea.this.checkBoxValue = button.getSelection();
			}

		});
	}

	/**
	 * Create footer section
	 */
	private void createFooter() {
		this.createSeparator();

		final Composite informationComposite = new Composite(this.parent.shell, SWT.NONE);
		informationComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
		informationComposite.setBackground(this.getGreyColor());

		informationComposite.setLayout(new GridLayout(this.icon == null ? 1 : 2, false));

		if (this.icon != null) {
			final Label labelIcon = new Label(informationComposite, SWT.NONE);
			labelIcon.setBackground(this.getGreyColor());
			labelIcon.setImage(this.icon);
			labelIcon.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));
		}
		final Label labelText = new Label(informationComposite, SWT.NONE);
		labelText.setBackground(this.getGreyColor());
		labelText.setText(this.footerText);
		labelText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
	}

	/**
	 * Create the expanded panel
	 * 
	 * @param numberOfColumns
	 */
	private void createExpandedPanel(final int numberOfColumns) {
		this.expandedPanel = new Label(this.composite, SWT.BORDER);
		this.expandedPanel.setText(this.detailText);
		this.expandedPanel.setBackground(this.getGreyColor());
		final GridData gd = new GridData(GridData.FILL, GridData.FILL, false, false, numberOfColumns, 1);
		gd.minimumHeight = gd.heightHint = 150;
		this.expandedPanel.setLayoutData(gd);
	}

	/**
	 * Create a separator
	 */
	private void createSeparator() {
		final Composite c = new Composite(this.parent.shell, SWT.NONE);
		c.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		c.setBackground(this.getGreyColor());

		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		c.setLayout(gridLayout);

		final Label separator = new Label(c, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

	}

	// ------------------------------------------- Getters & Setters

	/**
	 * @return the icon
	 */
	public Image getIcon() {
		return this.icon;
	}

	/**
	 * @param icon the icon to set
	 * @return this footer area
	 */
	public FooterArea setIcon(final Image icon) {
		this.icon = icon;
		this.setInitialised(true);
		return this;
	}

	/**
	 * @return the text
	 */
	public String getFooterText() {
		return this.footerText;
	}

	/**
	 * @param text the text to set
	 * @return this footer area
	 */
	public FooterArea setFooterText(final String text) {
		this.footerText = text;
		this.setInitialised(true);
		return this;
	}

	/**
	 * @return the button labels
	 */
	public List<String> getButtonLabels() {
		return this.buttonLabels;
	}

	/**
	 * @param buttonLabels the button labels to set
	 * @return this footer area
	 */
	public FooterArea setButtonLabels(final List<String> buttonLabels) {
		this.buttonLabels = buttonLabels;
		this.setInitialised(true);
		return this;
	}

	/**
	 * @param buttonLabels the button labels to set
	 * @return this footer area
	 */
	public FooterArea setButtonLabels(final String... buttonLabels) {
		this.buttonLabels = Arrays.asList(buttonLabels);
		this.setInitialised(true);
		return this;
	}

	/**
	 * @return the default button index
	 */
	public int getDefaultButtonIndex() {
		return this.defaultButtonIndex;
	}

	/**
	 * @param defaultButtonIndex the default button index to set
	 * @return this footer area
	 */
	public FooterArea setDefaultButtonIndex(final int defaultButtonIndex) {
		this.defaultButtonIndex = defaultButtonIndex;
		this.setInitialised(true);
		return this;
	}

	/**
	 * @return the timer value
	 */
	public int getTimer() {
		return this.timer;
	}

	/**
	 * @param timer the timer value to set
	 * @return this footer area
	 */
	public FooterArea setTimer(final int timer) {
		this.timer = timer;
		this.setInitialised(true);
		return this;
	}

	/**
	 * @return the timer index button
	 */
	public int getTimerIndexButton() {
		return this.timerIndexButton;
	}

	/**
	 * @param timerIndexButton the timer index button to set
	 * @return this footer area
	 */
	public FooterArea setTimerIndexButton(final int timerIndexButton) {
		this.timerIndexButton = timerIndexButton;
		this.setInitialised(true);
		return this;
	}

	/**
	 * @return the selected button
	 */
	int getSelectedButton() {
		return this.selectedButtonIndex;
	}

	/**
	 * @return the collapsed label text
	 */
	public String getCollapsedLabelText() {
		return this.collapsedLabelText;
	}

	/**
	 * @param collapsedLabelText the collapsed label text to set
	 * @return this footer area
	 */
	public FooterArea setCollapsedLabelText(final String collapsedLabelText) {
		this.details = true;
		this.collapsedLabelText = collapsedLabelText;
		this.setInitialised(true);
		return this;
	}

	/**
	 * @return the expanded label text
	 */
	public String getExpandedLabelText() {
		return this.expandedLabelText;
	}

	/**
	 * @param expandedLabelText the expanded label text to set
	 * @return this footer area
	 */
	public FooterArea setExpandedLabelText(final String expandedLabelText) {
		this.details = true;
		this.expandedLabelText = expandedLabelText;
		this.setInitialised(true);
		return this;
	}

	/**
	 * @return the expanded flag
	 */
	public boolean isExpanded() {
		return this.expanded;
	}

	/**
	 * @param expanded the expanded flag to set
	 * @return this footer area
	 */
	public FooterArea setExpanded(final boolean expanded) {
		this.details = true;
		this.expanded = expanded;
		this.setInitialised(true);
		return this;
	}

	/**
	 * @return the detail text
	 */
	public String getDetailText() {
		return this.detailText;
	}

	/**
	 * @param detailText the detail text to set
	 * @return this footer area
	 */
	public FooterArea setDetailText(final String detailText) {
		this.details = true;
		this.detailText = detailText;
		this.setInitialised(true);
		return this;
	}

	/**
	 * @return the check box vqlue
	 */
	public boolean getCheckBoxValue() {
		return this.checkBoxValue;
	}

}
