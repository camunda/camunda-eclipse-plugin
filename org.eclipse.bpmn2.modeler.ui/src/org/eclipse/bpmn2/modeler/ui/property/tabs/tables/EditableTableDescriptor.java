package org.eclipse.bpmn2.modeler.ui.property.tabs.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public abstract class EditableTableDescriptor<T> extends TableDescriptor<T> {
	
	private T dummy;

	public void setAddDummy(T dummy) {
		this.dummy = dummy;
	}
	
	public T getAddDummy() {
		return dummy;
	}
	
	public void configure(final Table table) {
		super.configure(table);

		Menu menu = new Menu(table.getShell(), SWT.POP_UP);
		final MenuItem deleteEntryMenuItem = new MenuItem(menu, SWT.PUSH);
		
		deleteEntryMenuItem.setText("Delete Entry");
		
		table.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				deleteEntryMenuItem.setEnabled(item != null);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		deleteEntryMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				table.remove(table.getSelectionIndices());
			}
		});
		
		final MenuItem addEntryMenuItem = new MenuItem(menu, SWT.PUSH);
		
		addEntryMenuItem.setText("Add new Entry");
		
		addEntryMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Would add entry now!");
			}
		});
		
		table.setMenu(menu);
	}
}