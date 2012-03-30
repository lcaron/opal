/*******************************************************************************
 * Copyright (c) 2011 Luis Carlos Moreira da Costa.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luis Carlos Moreira da Costa (tcljava at gmail dot com) - initial API and implementation
 *******************************************************************************/
package org.mihalis.opal.dynamictablecolumns;

import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * 
 * DynamicColumnData
 * 
 * <p>Sets the dynamic properties of a column.</p>
 * <p>The objects of this class must be registered and
 * handled by an instance of <code>DynamicColumnManager</code></p>
 * @see org.mihalis.opal.dynamictablecolumns.DynamicTableColumnLayout
 */
public class DynamicColumnData {
	
	private DynamicLength preferredLength = null;
	private DynamicLength minLength = null;
	private boolean visible = true;
	
	private TableColumn tableColumn = null;
	private TreeColumn  treeColumn = null;

	/**
	 * <p>Create a dynamic column definition in order to specify
	 *  the measures required for a <code>TableColumn</code>.</p>
	 * 
	 * <p><b>Definition of length</b>
	 * <br>The lengths are specified using a <code>String</code>, containing
	 *  the number, followed by an identifier of measurement.
	 * <br>Ex.: "355px", "100%"</p>
	 * 
	 * @param tableColumn     TableColumn The column (a table) that should be controlled length.
	 * @param preferredLength String      That defines or <i>desired length</i> as column.
	 * @param minLength       String      That defines or <i>minimum length</i> as column.
	 * 
	 * @see org.eclipse.swt.widgets.TableColumn
	 */
	public DynamicColumnData(final TableColumn tableColumn, final String preferredLength, final String minLength) {
		this(tableColumn, DynamicLengthFormat.parse(preferredLength), DynamicLengthFormat.parse(minLength));
	}
	
	/**
	 * <p>Create a dynamic column definition in order to specify
     * steps required for a <code>TableColumn</code>.</p>
	 * <p><i>!Builder alternative avoids setting minimum length,
     * using default 0px !</i></p>
	 * 
	 * <p><b>Definition of length</b>
	 * <br>The lengths are specified using a <code>String</code>, containing
     * quantity, followed by an identifier of measurement.
	 * <br>Ex.: "355px", "100%"</p>
	 * 
	 * @param tableColumn     TableColumn The column (a table) that should be controlled length.
	 * @param preferredLength String      That defines or <i>desired length</i> as column.
	 * 
	 * @see org.eclipse.swt.widgets.TableColumn
	 */
	public DynamicColumnData(final TableColumn tableColumn, final String preferredLength) {
		this(tableColumn, DynamicLengthFormat.parse(preferredLength), new DynamicLength(0d, DynamicLengthMeasure.PIXEL));
	}
	
	/**
	 * <p>Create a dynamic column definition in order to specify
     * steps required for a <code>TreeColumn</code>.</p>
	 * 
	 * <p><b>Definition of length</b>
	 * <br>The lengths are specified using a <code>String</code>, containing
     * quantity, followed by an identifier of measurement.
	 * <br>Ex.: "355px", "100%"</p>
	 * 
	 * @param treeColumn      TreeColumn Column (a tree) you want to control the length.
	 * @param preferredLength String     That defines or <i>desired length</i> as column.
	 * @param minLength       String     That defines or <i>minimum length</i> as column.
	 * 
	 * @see org.eclipse.swt.widgets.TreeColumn
	 */
	public DynamicColumnData(final TreeColumn treeColumn, final String preferredLength, final String minLength) {
		this(treeColumn, DynamicLengthFormat.parse(preferredLength), DynamicLengthFormat.parse(minLength));
	}
	
	/**
	 * <p>Create a dynamic column definition in order to specify
     * steps required for a <code>TreeColumn</code>.</p>
	 * <p><i>!Builder alternative avoids setting minimum length,
	 * using as standard 0px !</i></p>
	 * 
	 * <p><b>Definition of length</b>
	 * <br>The lengths are specified using a <code>String</code>, containing
     * quantity, followed by an identifier of measurement.
	 * <br>Ex.: "355px", "100%"</p>
	 * 
	 * @param treeColumn      TreeColumn Column (a tree) you want to control the length.
	 * @param preferredLength String     That defines or <i>desired length</i> as column.
	 * 
	 * @see org.eclipse.swt.widgets.TreeColumn
	 */
	public DynamicColumnData(final TreeColumn treeColumn, final String preferredLength) {
		this(treeColumn, DynamicLengthFormat.parse(preferredLength), new DynamicLength(0d, DynamicLengthMeasure.PIXEL));
	}
	
	/**
	 * Create a dynamic column definition in order to specify
     * steps required for a <code>TableColumn</code>.
	 * 
	 * @param tableColumn     TableColumn   The column (a table) that should be controlled length.
	 * @param preferredLength DynamicLength The <i>desired length</i> as column.
	 * @param minLength       DynamicLength The <i>minimum length</i> as column.
	 * 
	 * @see org.eclipse.swt.widgets.TableColumn
	 */
	DynamicColumnData(final TableColumn tableColumn, final DynamicLength preferredLength, final DynamicLength minLength) {
		this.tableColumn = tableColumn;
		this.preferredLength = preferredLength;
		this.minLength = minLength;
	}
	
	/**
	 * Create a dynamic column definition in order to specify
	 * measures required for a <code>TreeColumn</code>.
	 * 
	 * @param treeColumn       TreColumn     Column (a tree) you want to control the length.
	 * @param preferredLength  DynamicLength The <i>desired length</i> as column.
	 * @param minLength        DynamicLength The <i>minimum length</i> as column.
	 * 
	 * @see org.eclipse.swt.widgets.TreeColumn
	 */
	DynamicColumnData(final TreeColumn treeColumn, final DynamicLength preferredLength, final DynamicLength minLength) {
		this.treeColumn = treeColumn;
		this.preferredLength = preferredLength;
		this.minLength = minLength;
	}
	
	/**
	 * Get preferred length
	 */
	public DynamicLength getPreferredLength() {
		return preferredLength;
	}

	/**
	 * Set preferred length
	 * @param preferredLength DynamicLength
	 */
	public void setPreferredLength(final DynamicLength preferredLength) {
		this.preferredLength = preferredLength;
	}

	/**
	 * Get minimal length
	 * @return DynamicLength
	 */
	public DynamicLength getMinLength() {
		return minLength;
	}

	/**
	 * Set minimal length
	 * @param minLength DynamicLength
	 */
	public void setMinLength(final DynamicLength minLength) {
		this.minLength = minLength;
	}

	/**
	 * Get table column
	 * @return TableColumn
	 */
	public TableColumn getTableColumn() {
		return tableColumn;
	}

	/**
	 * Set table column
	 * @param tableColumn TableColumn
	 */
	public void setTableColumn(final TableColumn tableColumn) {
		this.tableColumn = tableColumn;
	}

	/**
	 * Get tree column
	 * @return TreeColumn
	 */
	public TreeColumn getTreeColumn() {
		return treeColumn;
	}

	/**
	 * Set tree column
	 * @param treeColumn TreeColumn
	 */
	public void setTreeColumn(final TreeColumn treeColumn) {
		this.treeColumn = treeColumn;
	}

	/**
	 * Is visible
	 * @return boolean
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Set visible
	 * @param visible boolean
	 */
	public void setVisible(final boolean visible) {
		this.visible = visible;
	}
	
}