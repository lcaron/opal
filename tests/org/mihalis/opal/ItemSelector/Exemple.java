package org.mihalis.opal.ItemSelector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Exemple {

	public static void main(final String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Composite comp = new Composite(shell, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));

		final Table table = new Table(comp, SWT.BORDER | SWT.V_SCROLL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));

		construitColonnes(table, 0);
		construitColonnes(table, 1);

		final ControlAdapter ca = new ControlAdapter() {
			@Override
			public void controlResized(final ControlEvent e) {
				final Rectangle area = comp.getClientArea();
				final Point size = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				final ScrollBar vBar = table.getVerticalBar();
				int width = area.width - 10;
				if (size.y > area.height + table.getHeaderHeight()) {
					// Subtract the scrollbar width from the total column width
					// if a vertical scrollbar will be required
					final Point vBarSize = vBar.getSize();
					width -= vBarSize.x;
				}
				final Point oldSize = table.getSize();
				if (oldSize.x > area.width) {
					// table is getting smaller so make the columns
					// smaller first and then resize the table to
					// match the client area width
					computeColumnSize(width);
					table.setSize(area.width, area.height);
				} else {
					// table is getting bigger so make the table
					// bigger first and then make the columns wider
					// to match the client area width
					table.setSize(area.width, area.height);
					computeColumnSize(width);
				}
			}

			private void computeColumnSize(final int width) {
				int totalSize = 20;
				for (int i = 0, n = table.getColumnCount(); i < n; i++) {
					if (i == n - 1) {
						table.getColumn(i).setWidth(width - totalSize);
					} else {
						table.getColumn(i).setWidth(width / n);
					}
					totalSize += table.getColumn(i).getWidth();
				}
			}
		};
		comp.addControlListener(ca);

		final Button add = new Button(comp, SWT.PUSH);
		add.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, true, false));
		add.setText("Ajouter une colonne");
		add.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				table.getParent().setRedraw(false);
				construitColonnes(table, table.getColumnCount());
				ca.controlResized(null);
				table.getParent().layout();
				table.getParent().setRedraw(true);
			}
		});

		final Button remove = new Button(comp, SWT.PUSH);
		remove.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));
		remove.setText("Supprimer une colonne");
		remove.addSelectionListener(new SelectionAdapter() {

			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				if (table.getColumnCount() == 1) {
					return;
				}
				table.getParent().setRedraw(false);
				table.getColumn(table.getColumnCount() - 1).dispose();
				ca.controlResized(null);
				table.getParent().layout();
				table.getParent().setRedraw(true);
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void construitColonnes(final Table table, final int j) {
		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Column " + j);

		for (int i = 0; i < 10; i++) {
			TableItem item;
			if (j == 0) {
				item = new TableItem(table, SWT.NONE);
			} else {
				item = table.getItem(i);
			}

			item.setText(j, "item " + j + i);
		}
	}
}
