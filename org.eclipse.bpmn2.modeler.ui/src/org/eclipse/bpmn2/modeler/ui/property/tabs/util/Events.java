package org.eclipse.bpmn2.modeler.ui.property.tabs.util;

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.widgets.Event;

/**
 * Collection of user defined events
 * 
 * @author nico.rehwaldt
 */
public class Events {

	// something above SWT.OpenDocument
	public static final int RADIO_SELECTION_CHANGED = 20001;
	
	public static final int MODEL_CHANGED = 20002;

	public static final int CELL_VALUE_CHANGED = 20003;
	
	public static final int ROW_DELETED = 20004;

	public static final int ROW_ADDED = 20005;
	
	/**
	 * Radio selection changed event
	 * 
	 * @author nico.rehwaldt
	 *
	 * @param <T>
	 */
	public static class RadioSelectionChanged<T> extends Event {
		
		private static final long serialVersionUID = 1L;
		
		private T newSelection;
		private T oldSelection;

		public RadioSelectionChanged(Object source, T newSelection, T oldSelection) {
			super();
			
			this.newSelection = newSelection;
			this.oldSelection = oldSelection;
		}
		
		public T getNewSelection() {
			return newSelection;
		}
		
		public T getOldSelection() {
			return oldSelection;
		}
	}
	
	/**
	 * Cell value changed event due to user input
	 * 
	 * @author nico.rehwaldt
	 */
	public static class CellValueChanged<T> extends Event {

		private ViewerCell cell;
		
		private Object newValue;
		private Object oldValue;

		private T target;

		public CellValueChanged(ViewerCell cell, T target, Object newValue, Object oldValue) {
			this.cell = cell;
			this.target = target;
			
			this.newValue = newValue;
			this.oldValue = oldValue;
		}
		
		public ViewerCell getCell() {
			return cell;
		}
		
		public Object getNewValue() {
			return newValue;
		}
		
		public Object getOldValue() {
			return oldValue;
		}
		
		/**
		 * Returns the target of the change operation
		 * @return
		 */
		public T getTarget() {
			return target;
		}
		
	}
	
	/**
	 * Event indicating the removal of a row
	 * 
	 * @author nico.rehwaldt
	 *
	 * @param <T>
	 */
	public static class RowDeleted<T> extends Event {
		
		private T removedElement;

		public RowDeleted(T removedElement) {
			this.removedElement = removedElement;
		}
		
		public T getRemovedElement() {
			return removedElement;
		}
	}
	
	/**
	 * Event indicating the addition of a row
	 * 
	 * @author nico.rehwaldt
	 *
	 * @param <T>
	 */
	public static class RowAdded<T> extends Event {
		
		private T addedElement;

		public RowAdded(T addedElement) {
			this.addedElement = addedElement;
		}
		
		public T getAddedElement() {
			return addedElement;
		}
	}
}
