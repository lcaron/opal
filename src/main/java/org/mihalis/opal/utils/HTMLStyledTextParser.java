/*******************************************************************************
 * Copyright (c) 2012 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - Initial implementation and API
 *******************************************************************************/
package org.mihalis.opal.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

/**
 * Instances of this class are used to convert HTML content of a styled text
 * into style ranges
 */
public class HTMLStyledTextParser {

	private final StyledText styledText;

	/**
	 * Constructor
	 * 
	 * @param styledText styled text to analyze
	 */
	HTMLStyledTextParser(final StyledText styledText) {
		this.styledText = styledText;
	}

	/**
	 * Parse the content, build the list of style ranges and apply them to the
	 * styled text widget
	 * 
	 * @throws IOException
	 */
	public void parse() throws IOException {
		if (this.styledText == null || "".equals(this.styledText.getText().trim())) {
			return;
		}
		final String text = this.styledText.getText().trim();
		int currentPosition = 0;
		final StringBuilder output = new StringBuilder();
		final List<StyleRange> listOfStyles = new ArrayList<StyleRange>();
		final LinkedList<StyleRange> stack = new LinkedList<StyleRange>();
		final int max = text.length();
		for (int i = 0; i < max; i++) {
			String currentTag;
			String currentClosingTag;
			String currentBRTag;
			if (i <= max - 4) {
				currentTag = text.substring(i, i + 3).toLowerCase();
			} else {
				currentTag = null;
			}

			if (i <= max - 4) {
				currentClosingTag = text.substring(i, i + 4).toLowerCase();
			} else {
				currentClosingTag = null;
			}

			if (i <= max - 5) {
				currentBRTag = text.substring(i, i + 5).toLowerCase();
			} else {
				currentBRTag = null;
			}

			if (currentTag != null) {
				if ("<b>".equalsIgnoreCase(currentTag)) {
					final StyleRange currentStyleRange = new StyleRange();
					currentStyleRange.start = currentPosition;
					currentStyleRange.length = 0;
					currentStyleRange.fontStyle = SWT.BOLD;
					stack.push(currentStyleRange);
					i += 2;
					continue;
				}
				if ("<i>".equalsIgnoreCase(currentTag)) {
					final StyleRange currentStyleRange = new StyleRange();
					currentStyleRange.start = currentPosition;
					currentStyleRange.length = 0;
					currentStyleRange.fontStyle = SWT.ITALIC;
					stack.push(currentStyleRange);
					i += 2;
					continue;
				}
				if ("<u>".equalsIgnoreCase(currentTag)) {
					final StyleRange currentStyleRange = new StyleRange();
					currentStyleRange.start = currentPosition;
					currentStyleRange.length = 0;
					currentStyleRange.fontStyle = SWT.NONE;
					currentStyleRange.underline = true;
					stack.push(currentStyleRange);
					i += 2;
					continue;
				}
			}

			if (currentClosingTag != null) {
				if ("</b>".equalsIgnoreCase(currentClosingTag)) {
					final StyleRange currentStyleRange = stack.pop();
					if ((currentStyleRange.fontStyle & SWT.BOLD) == 0) {
						final StringBuilder sb = new StringBuilder();
						sb.append("Error at position #").append(currentPosition).append(" - closing </b> tag found but ");
						if ((currentStyleRange.fontStyle & SWT.ITALIC) != 0) {
							sb.append("</i>");
						} else {
							sb.append("</u>");
						}
						sb.append(" tag expected !");
						throw new RuntimeException(sb.toString());
					}
					currentStyleRange.length = currentPosition - currentStyleRange.start;
					listOfStyles.add(currentStyleRange);
					i += 3;
					continue;
				}

				if ("</i>".equalsIgnoreCase(currentClosingTag)) {
					final StyleRange currentStyleRange = stack.pop();
					if ((currentStyleRange.fontStyle & SWT.ITALIC) == 0) {
						final StringBuilder sb = new StringBuilder();
						sb.append("Error at position #").append(currentPosition).append(" - closing </i> tag found but ");
						if ((currentStyleRange.fontStyle & SWT.BOLD) != 0) {
							sb.append("</b>");
						} else {
							sb.append("</u>");
						}
						sb.append(" tag expected !");
						throw new RuntimeException(sb.toString());
					}
					currentStyleRange.length = currentPosition - currentStyleRange.start;
					listOfStyles.add(currentStyleRange);
					i += 3;
					continue;
				}

				if ("</u>".equalsIgnoreCase(currentClosingTag)) {
					final StyleRange currentStyleRange = stack.pop();
					if (!currentStyleRange.underline) {
						final StringBuilder sb = new StringBuilder();
						sb.append("Error at position #").append(currentPosition).append(" - closing </u> tag found but ");
						if ((currentStyleRange.fontStyle & SWT.ITALIC) != 0) {
							sb.append("</i>");
						} else {
							sb.append("</b>");
						}
						sb.append(" tag expected !");
						throw new RuntimeException(sb.toString());
					}
					currentStyleRange.length = currentPosition - currentStyleRange.start;
					listOfStyles.add(currentStyleRange);
					i += 3;
					continue;
				}
			}

			if (currentBRTag != null && "<br/>".equalsIgnoreCase(currentBRTag)) {
				output.append("\n");
				i += 4;
				continue;
			}
			currentPosition++;
			output.append(text.substring(i, i + 1));
		}
		this.styledText.setText(output.toString());
		this.styledText.setStyleRanges(listOfStyles.toArray(new StyleRange[listOfStyles.size()]));
	}
}
