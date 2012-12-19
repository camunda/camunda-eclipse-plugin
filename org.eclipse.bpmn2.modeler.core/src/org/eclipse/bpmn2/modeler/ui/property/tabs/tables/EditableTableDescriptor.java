package org.eclipse.bpmn2.modeler.ui.property.tabs.tables;

import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.RowAdded;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
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
	
	public static enum PostAddAction {
		FOCUS, 
		EDIT, 
		NONE
	}
	
	private CellEditingStrategy cellEditingStrategy = CellEditingStrategy.DOUBLE_CLICK;
	private PostAddAction postAddAction = PostAddAction.FOCUS;
	
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
	 * Sets the strategy to edit the table
	 * @param strategy
	 */
	public void setPostAddAction(PostAddAction postAddAction) {
		if (postAddAction == null) {
			throw new IllegalArgumentException("post add action may not be null");
		}
		
		this.postAddAction = postAddAction;
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
		if (elementFactory != null && addStrategy == null) {
			addStrategy = createAddStrategy(viewer, elementFactory);
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

	protected AddStrategy<T> createAddStrategy(TableViewer viewer, ElementFactory<T> elementFactory) {
		switch (postAddAction) {
		case EDIT:
			return new AddAndEditStrategy<T>(viewer, elementFactory);
		case FOCUS:
			return new AddAndFocusStrategy<T>(viewer, elementFactory);
		default: 
			return new DefaultAddStrategy<T>(viewer, elementFactory);
		}
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
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				
				System.out.println("widgetSelected " + selection);
				
				deleteEntryMenuItem.setEnabled(!selection.isEmpty());
				addEntryMenuItem.setEnabled(addStrategy != null);
			}
		});
		
		deleteEntryMenuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				if (!selection.isEmpty()) {
					Object element = selection.getFirstElement();
					if (element != null) {
						viewer.remove(element);
						viewer.setSelection(new StructuredSelection(new Object[0]), true);
						
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
	public static abstract class AddStrategy<T> {
		
		/**
		 * Perform add operation on the table and returns the newly added element
		 * 
		 */
		public abstract T performAdd();
	}
	
	private static class DefaultAddStrategy<T> extends AddStrategy<T> {

		protected TableViewer viewer;
		protected ElementFactory<T> factory;
		
		public DefaultAddStrategy(TableViewer viewer, ElementFactory<T> factory) {
			this.viewer = viewer;
			this.factory = factory;
		}

		@Override
		public T performAdd() {
			T element = factory.create();
			
			viewer.getTable().notifyListeners(Events.ROW_ADDED, new RowAdded<T>(element));
			
			return element;
		}
	}
	
	/**
	 * Implements adding of an element via focusing the dummy
	 * 
	 * @author nico.rehwaldt
	 */
	private static class AddAndEditStrategy<T> extends DefaultAddStrategy<T> {
		
		public AddAndEditStrategy(TableViewer viewer, ElementFactory<T> factory) {
			super(viewer, factory);
		}
		
		@Override
		public T performAdd() {
			final T element = super.performAdd();
			
			Display.getCurrent().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					viewer.editElement(element, 0);
				}
			});
			
			return element;
		}
	}
	
	/**
	 * Implements adding of an element via focusing the dummy
	 * 
	 * @author nico.rehwaldt
	 */
	private static class AddAndFocusStrategy<T> extends DefaultAddStrategy<T> {
		
		public AddAndFocusStrategy(TableViewer viewer, ElementFactory<T> factory) {
			super(viewer, factory);
		}
		
		@Override
		public T performAdd() {
			final T element = super.performAdd();
			
			Display.getCurrent().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					viewer.setSelection(new StructuredSelection(element), true);
				}
			});
			
			return element;
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
		protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
			
			boolean singleSelect = ((IStructuredSelection)getViewer().getSelection()).size() == 1;
			boolean isLeftMouseSelect = event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION && ((MouseEvent)event.sourceEvent).button == 1;

			return singleSelect && (isLeftMouseSelect
					|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC
					|| event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL);
		}
	}
}