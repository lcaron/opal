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
package org.mihalis.opal.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

/**
 * Instances of this class are used to convert HTML content of a styled text
 * into style ranges
 */
public class HTMLStyledTextParser extends ParserCallback {

	private final List<StyleRange> listOfStyles;
	private final StyledText styledText;
	private int position;
	private final StringBuilder outputString;

	private StyleRange currentStyleRange;
	private TagType currentTagType;
	private int currentPosition;

	private enum TagType {
		B, U, I
	};

	/**
	 * Constructor
	 * 
	 * @param styledText styled text to analyze
	 */
	HTMLStyledTextParser(final StyledText styledText) {
		super();
		this.styledText = styledText;
		this.position = -1;
		this.listOfStyles = new ArrayList<StyleRange>();
		this.currentStyleRange = null;
		this.currentTagType = null;
		this.currentPosition = 0;
		this.outputString = new StringBuilder();
	}

	/**
	 * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleStartTag(javax.swing.text.html.HTML.Tag,
	 *      javax.swing.text.MutableAttributeSet, int)
	 */
	@Override
	public void handleStartTag(final Tag t, final MutableAttributeSet a, final int pos) {
		if (t == Tag.B) {
			this.currentStyleRange = new StyleRange();
			this.currentTagType = TagType.B;
			this.currentPosition = this.position;
		}
		if (t == Tag.I) {
			this.currentStyleRange = new StyleRange();
			this.currentTagType = TagType.I;
			this.currentPosition = this.position;
		}
		if (t == Tag.U) {
			this.currentStyleRange = new StyleRange();
			this.currentTagType = TagType.U;
			this.currentPosition = this.position;
		}

	}

	/**
	 * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleEndTag(javax.swing.text.html.HTML.Tag,
	 *      int)
	 */
	@Override
	public void handleEndTag(final Tag t, final int pos) {

		if (t != Tag.B && t != Tag.I && t != Tag.U) {
			return;
		}

		int style;
		boolean underline;
		if (t == Tag.B) {
			if (TagType.B != this.currentTagType) {
				throw new RuntimeException("Error parsing [" + this.styledText.getText() + "] : bad syntax");
			}
			style = SWT.BOLD;
			underline = false;
		} else if (t == Tag.I) {
			if (TagType.I != this.currentTagType) {
				throw new RuntimeException("Error parsing [" + this.styledText.getText() + "] : bad syntax");
			}
			style = SWT.ITALIC;
			underline = false;
		} else if (t == Tag.U) {
			if (TagType.U != this.currentTagType) {
				throw new RuntimeException("Error parsing [" + this.styledText.getText() + "] : bad syntax");
			}
			style = SWT.NORMAL;
			underline = true;
		} else {
			style = SWT.NORMAL;
			underline = false;
		}

		this.currentStyleRange.start = this.currentPosition;
		this.currentStyleRange.length = this.position - this.currentPosition + 1;
		this.currentStyleRange.fontStyle = style;
		this.currentStyleRange.underline = underline;
		this.listOfStyles.add(this.currentStyleRange);

		this.currentStyleRange = null;
		this.currentTagType = null;
	}

	/**
	 * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleError(java.lang.String,
	 *      int)
	 */
	@Override
	public void handleError(final String errorMsg, final int pos) {
		throw new RuntimeException("Parsing error: " + errorMsg + " at " + pos);
	}

	/**
	 * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleText(char[],
	 *      int)
	 */
	@Override
	public void handleText(final char[] data, final int pos) {
		this.outputString.append(data);
		this.position += data.length;
	}

	/**
	 * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleSimpleTag(javax.swing.text.html.HTML.Tag,
	 *      javax.swing.text.MutableAttributeSet, int)
	 */
	@Override
	public void handleSimpleTag(final Tag t, final MutableAttributeSet a, final int pos) {
		if (t == Tag.BR) {
			this.outputString.append("\n");
			this.position += t.toString().length();

		}
	}

	/**
	 * Parse the content, build the list of style ranges and apply them to the
	 * styled text widget
	 * 
	 * @throws IOException
	 */
	public void parse() throws IOException {
		final HTMLEditorKit.Parser parser = new ParserDelegator();
		parser.parse(new StringReader(this.styledText.getText()), this, true);
		this.styledText.setText(this.outputString.toString());
		this.styledText.setStyleRanges(this.listOfStyles.toArray(new StyleRange[this.listOfStyles.size()]));

	}

}
