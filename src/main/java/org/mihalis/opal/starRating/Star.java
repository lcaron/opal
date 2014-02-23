/*******************************************************************************
 * Copyright (c) 2012 Laurent CARON.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron@gmail.com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.starRating;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * Instances of this class represent a star displayed by the StarRating component
 */
class Star {
	private static final String SMALL_STAR_MARKED_FOCUS = "mark-focus16.png";
	private static final String SMALL_STAR_MARKED = "mark16.png";
	private static final String SMALL_STAR_FOCUS = "focus16.png";
	private static final String SMALL_STAR = "16.png";
	private static final String BIG_STAR_MARKED_FOCUS = "mark-focus32.png";
	private static final String BIG_STAR_MARKED = "mark32.png";
	private static final String BIG_STAR_FOCUS = "focus32.png";
	private static final String BIG_STAR = "32.png";
	boolean hover;
	boolean marked;
	Rectangle bounds;
	Image defaultImage;
	Image hoverImage;
	Image selectedImage;
	Image selectedHoverImage;
	private StarRating parent;

	void dispose() {
		this.defaultImage.dispose();
		this.hoverImage.dispose();
		this.selectedImage.dispose();
		this.selectedHoverImage.dispose();
	}

	void draw(final GC gc, final int x, final int y) {
		Image image;
		if (!this.parent.isEnabled()) {
			image = this.defaultImage;
		} else {
			if (this.marked) {
				if (this.hover) {
					image = this.selectedHoverImage;
				} else {
					image = this.selectedImage;
				}
			} else {
				if (this.hover) {
					image = this.hoverImage;
				} else {
					image = this.defaultImage;
				}
			}
		}

		gc.drawImage(image, x, y);
		this.bounds = new Rectangle(x, y, image.getBounds().width, image.getBounds().height);
	}

	static Star initBig(final StarRating parent) {
		final Star star = new Star();
		star.parent = parent;
		star.defaultImage = new Image(Display.getCurrent(), star.getClass().getClassLoader().getResourceAsStream("images/stars/" + BIG_STAR));
		star.hoverImage = new Image(Display.getCurrent(), star.getClass().getClassLoader().getResourceAsStream("images/stars/" + BIG_STAR_FOCUS));
		star.selectedImage = new Image(Display.getCurrent(), star.getClass().getClassLoader().getResourceAsStream("images/stars/" + BIG_STAR_MARKED));
		star.selectedHoverImage = new Image(Display.getCurrent(), star.getClass().getClassLoader().getResourceAsStream("images/stars/" + BIG_STAR_MARKED_FOCUS));
		return star;
	}

	static Star initSmall(final StarRating parent) {
		final Star star = new Star();
		star.parent = parent;
		star.defaultImage = new Image(Display.getCurrent(), star.getClass().getClassLoader().getResourceAsStream("images/stars/" + SMALL_STAR));
		star.hoverImage = new Image(Display.getCurrent(), star.getClass().getClassLoader().getResourceAsStream("images/stars/" + SMALL_STAR_FOCUS));
		star.selectedImage = new Image(Display.getCurrent(), star.getClass().getClassLoader().getResourceAsStream("images/stars/" + SMALL_STAR_MARKED));
		star.selectedHoverImage = new Image(Display.getCurrent(), star.getClass().getClassLoader().getResourceAsStream("images/stars/" + SMALL_STAR_MARKED_FOCUS));
		return star;
	}
}
