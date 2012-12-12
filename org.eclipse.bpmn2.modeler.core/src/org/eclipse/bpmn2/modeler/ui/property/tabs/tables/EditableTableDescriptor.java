package org.eclipse.bpmn2.modeler.ui.property.tabs.tables;

import java.util.List;

import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.RowAdded;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;

public class EditableTableDescriptor<T> extends TableDescriptor<T> {
	
	public static enum CellEditingStrategy {
		SINGLE_CLICK, 
		DOUBLE_CLICK, 
		NO_EDIT
	}
	
	private CellEditingStrategy cellEditingStrategy = CellEditingStrategy.DOUBLE_CLICK;
	
	private AddStrategy addStrategy;
	
	private ElementFactory<T> elementFactory;
	
	public void setElementFactory(ElementFactory<T> elementFactory) {
		this.elementFactory = elementFactory;
	}

	/**
	 * Sets the strategy to edit the table
	 * @param strategy
	 */
	public void setCellEditingStrategy(CellEditingStrategy strategy) {
		if (strategy == null) {
			throw new IllegalArgumentException("strategy may not be null");
		}
		
		this.cellEditingStrategy = strategy;
	}
	
	/**
	 * Returns the strategy used to edit the table
	 * @return
	 */
	public CellEditingStrategy getCellEditingStrategy() {
		return cellEditingStrategy;
	}
	
	@Override
	public void configureViewer(TableViewer viewer) {
		super.configureViewer(viewer);
		
		addEditingCapabilities(viewer);
		addAddCapability(viewer);
		addMenu(viewer);
	}
	
	/**
	 * Adds the add capability to the viewer
	 * @param viewer
	 */
	protected void addAddCapability(final TableViewer viewer) {
		if (elementFactory != null) {
			// adding is done by creating and editing a new object
			addStrategy = new AddAndEditStrategy<T>(viewer, elementFactory);
		}
		
		// CARRIAGE RETURN to add a new element
		
		viewer.getTable().addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (!viewer.isCellEditorActive()) {
					if (e.character == SWT.CR) {
					    Table table = viewer.getTable();
			            int next = table.getSelectionIndex() + 1;

			            if (next < table.getItemCount()) {
			            	viewer.setSelection(new StructuredSelection(viewer.getElementAt(next)),true);
			            } else {
				         	addStrategy.performAdd();
						}
					}
				}
			}
		});
	}

	/**
	 * Adds a menu to the viewer
	 * @param viewer
	 */
	protected void addMenu(final TableViewer viewer) {
		final Table table = viewer.getTable();

		final Menu menu = new Menu(table.getShell(), SWT.POP_UP);
		
		final MenuItem deleteEntryMenuItem = new MenuItem(menu, SWT.PUSH);		
		deleteEntryMenuItem.setText("Remove");
		deleteEntryMenuItem.setEnabled(false);
		
		final MenuItem addEntryMenuItem = new MenuItem(menu, SWT.PUSH);
		addEntryMenuItem.setText("Add");
		addEntryMenuItem.setEnabled(addStrategy != null);
		
		// activation of menu items
		table.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = viewer.getSelection();
				
				deleteEntryMenuItem.setEnabled(!selection.isEmpty());
				addEntryMenuItem.setEnabled(addStrategy != null);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		deleteEntryMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				if (!selection.isEmpty()) {
					Object element = selection.getFirstElement();
					if (element != null) {
						viewer.remove(element);
						
						table.notifyListeners(Events.ROW_DELETED, new Events.RowDeleted<T>((T) element));
					}
				}
			}
		});
		
		addEntryMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (addStrategy != null) {
					addStrategy.performAdd();
				}
			}
		});
		
		table.setMenu(menu);
	}

	/**
	 * Adds editing capabilities to the viewer
	 * @param viewer
	 */
	protected void addEditingCapabilities(TableViewer viewer) {

		ColumnViewerEditorActivationStrategy activationStrategy;
		
		switch (cellEditingStrategy) {
		case NO_EDIT:
			activationStrategy = new NoActivationStrategy(viewer);
			break;
		case DOUBLE_CLICK: 
			activationStrategy = new DoubleClickActivationStrategy(viewer);
			break;
		default: 
			activationStrategy = new ColumnViewerEditorActivationStrategy(viewer);
			break;
		}
		
		TableViewerEditor.create(viewer, activationStrategy, ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR);
	}
	
	/**
	 * Factory for the elements managed in a editable table
	 * 
	 * @author nico.rehwaldt
	 *
	 * @param <T>
	 */
	public static abstract class ElementFactory<T> {
		
		/**
		 * Create a new instance of the object
		 * @return
		 */
		public abstract T create();
	}
	
	/**
	 * Strategy to perform an add on the table
	 * 
	 * @author nico.rehwaldt
	 */
	public static abstract class AddStrategy {
		
		/**
		 * Perform add operation on the table
		 */
		public abstract void performAdd();
	}
	
	/**
	 * Implements adding of an element via focusing the dummy
	 * 
	 * @author nico.rehwaldt
	 */
	private static class AddAndEditStrategy<T> extends AddStrategy {

		private TableViewer viewer;
		private ElementFactory<T> factory;
		
		public AddAndEditStrategy(TableViewer viewer, ElementFactory<T> factory) {
			this.viewer = viewer;
			this.factory = factory;
		}
		
		@Override
		public void performAdd() {
			T element = factory.create();
			viewer.add(element);

			viewer.getTable().notifyListeners(Events.ROW_ADDED, new RowAdded<T>(element));

			viewer.editElement(element, 0);
		}
	}
	
	/**
	 * Strategy which never activates the cell editing capabilities
	 * 
	 * @author nico.rehwaldt
	 */
	private static class NoActivationStrategy extends ColumnViewerEditorActivationStrategy {

		public NoActivationStrategy(ColumnViewer viewer) {
			super(viewer);
		}
		
		@Override
		protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
			return false;
		}
	}
	
	/**
	 * Strategy which activates the cell editing via double click
	 * 
	 * @author nico.rehwaldt
	 */
	private static class DoubleClickActivationStrategy extends ColumnViewerEditorActivationStrategy {
		
		public DoubleClickActivationStrategy(ColumnViewer viewer) {
			super(viewer);
		}

		/**
		 * @param event
		 *            the event triggering the action
		 * @return <code>true</code> if this event should open the editor
		 */
		protected boolean isEditorActivationEvent(
				ColumnViewerEditorActivationEvent event) {
			boolean singleSelect = ((IStructuredSelection)getViewer().getSelection()).size() == 1;
			boolean isLeftMouseSelect = event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION && ((MouseEvent)event.sourceEvent).button == 1;

			return singleSelect && (isLeftMouseSelect
					|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC
					|| event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL);
		}
	}
}