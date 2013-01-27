package org.mihalis.opal.busyindicator;

/**
 * @author kiran
 *
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Support for showing a custom Busy Cursor during a long running process.
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#busyindicator">BusyIndicator snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class OBusyIndicator {

	static int nextBusyId = 1;
	static final String BUSYID_NAME = "Opal BusyIndicator"; //$NON-NLS-1$

/**
 * Runs the given <code>Runnable</code> while providing
 * busy feedback using this busy indicator.
 * 
 * @param display the display on which the busy feedback should be
 *        displayed.  If the display is null, the Display for the current
 *        thread will be used.  If there is no Display for the current thread,
 *        the runnable code will be executed and no busy feedback will be displayed.
 * @param runnable the runnable for which busy feedback is to be shown.
 *        Must not be null.
 * 
 * @param cursor to display busy feedback.
 *        
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the runnable is null</li>
 * </ul>
 */

public static void showWhile(Display display, Runnable runnable, Cursor busyCursor) {
	if (runnable == null)
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (display == null) {
		display = Display.getCurrent();
		if (display == null) {
			runnable.run();
			return;
		}
	}
	
	Integer busyId = new Integer(nextBusyId);
	nextBusyId++;
	Cursor cursor = (busyCursor==null||busyCursor.isDisposed())?display.getSystemCursor(SWT.CURSOR_WAIT):busyCursor;
	Shell[] shells = display.getShells();
	for (int i = 0; i < shells.length; i++) {
		Integer id = (Integer)shells[i].getData(BUSYID_NAME);
		if (id == null) {
			shells[i].setCursor(cursor);
			shells[i].setData(BUSYID_NAME, busyId);
		}
	}
		
	try {
		runnable.run();
	} finally {
		shells = display.getShells();
		for (int i = 0; i < shells.length; i++) {
			Integer id = (Integer)shells[i].getData(BUSYID_NAME);
			if (id == busyId) {
				shells[i].setCursor(null);
				shells[i].setData(BUSYID_NAME, null);
			}
		}
	}
}
}
