/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	   Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 *                                               - fix for bug 178280
 *     IBM Corporation - API refactoring and general maintenance
 *******************************************************************************/

package org.eclipse.jface.layout.fix;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;

/**
 * A nearly exact copy of {@link TableColumnLayout}. 
 * With the small difference that it fixes a bug which makes composites
 * layouted using it explode when nested in 
 * {@link FormLayout} layouted composites.
 * 
 * Actual fix is done in {@link AbstractColumnLayout2}.
 * 
 * @author nico.rehwaldt
 */
public class TableColumnLayout2 extends AbstractColumnLayout2 {

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.5
	 */
	protected int getColumnCount(Scrollable tableTree) {
		return ((Table) tableTree).getColumnCount();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.5
	 */
	protected void setColumnWidths(Scrollable tableTree, int[] widths) {
		TableColumn[] columns = ((Table) tableTree).getColumns();
		for (int i = 0; i < widths.length; i++) {
			columns[i].setWidth(widths[i]);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.5
	 */
	protected ColumnLayoutData getLayoutData(Scrollable tableTree,
			int columnIndex) {
		TableColumn column = ((Table) tableTree).getColumn(columnIndex);
		return (ColumnLayoutData) column.getData(LAYOUT_DATA);
	}

	Composite getComposite(Widget column) {
		return ((TableColumn) column).getParent().getParent();
	}

	/**
	 * @since 3.5
	 */
	protected void updateColumnData(Widget column) {
		TableColumn tColumn = (TableColumn) column;
		Table t = tColumn.getParent();

		if (!IS_GTK || t.getColumn(t.getColumnCount() - 1) != tColumn) {
			tColumn.setData(LAYOUT_DATA,
					new ColumnPixelData(tColumn.getWidth()));
			layout(t.getParent(), true);
		}
	}
}
