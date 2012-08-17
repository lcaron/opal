package org.mihalis.opal.obutton;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

public interface ButtonRenderer {

	void dispose();

	void drawButtonWhenMouseHover(GC gc);

	void drawButtonWhenDisabled(GC gc);

	void drawButtonWhenSelected(GC gc);

	void drawButton(GC gc);

	Point computeSize(int wHint, int hHint, boolean changed);

	void drawButtonWhenClicked(GC gc);

}
