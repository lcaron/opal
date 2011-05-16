package org.mihalis.opal.utils;

import org.eclipse.swt.graphics.Resource;

public class SWTGraphicUtil {
	private static SWTGraphicUtil instance;

	private SWTGraphicUtil() {

	}

	/**
	 * @return the instance
	 */
	public static SWTGraphicUtil getInstance() {
		if (instance == null) {
			instance = new SWTGraphicUtil();
		}

		return instance;
	}

	public void dispose(final Resource r) {
		if (r != null && !r.isDisposed()) {
			r.dispose();
		}
	}

}
