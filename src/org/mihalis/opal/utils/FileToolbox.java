package org.mihalis.opal.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FileToolbox {

	/**
	 * Loads a file in a stream
	 * 
	 * @param fileName file name
	 * @return a stream that points to this file
	 */
	public static InputStream getInputStream(final String fileName) {
		if (fileName.startsWith("jar:")) {
			URL url;
			try {
				url = new URL(fileName);
				return url.openStream();
			} catch (final MalformedURLException e) {
				throw new RuntimeException(e);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}

		} else {
			try {
				return new FileInputStream(fileName);
			} catch (final FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
