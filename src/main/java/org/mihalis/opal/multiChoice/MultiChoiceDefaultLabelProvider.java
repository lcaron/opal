package org.mihalis.opal.multiChoice;

/**
 * Default MultiChoiceLabelProvider that uses the toString() method to determine the content of a given element
 */
public class MultiChoiceDefaultLabelProvider implements MultiChoiceLabelProvider {

	/**
	 * @see org.mihalis.opal.multiChoice.MultiChoiceLabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(final Object element) {
		return element == null ? "" : element.toString();
	}

}
