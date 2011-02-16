package org.mihalis.opal.ItemSelector;

import org.eclipse.swt.graphics.Image;
import org.mihalis.opal.OpalItem;

public class DLItem extends OpalItem {
	public DLItem(final String text) {
		setText(text);
	}

	public DLItem(final String text, final Image image) {
		setText(text);
		setImage(image);
	}

}
