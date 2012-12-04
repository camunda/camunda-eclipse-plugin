/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.ui.property.tabs.swt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 * @author Nico Rehwaldt
 */
public class Radio {

	public static class RadioGroup<T> {

		private List<Listener> listeners = new ArrayList<Listener>();
		private Map<T, Button> memberMap = new HashMap<T, Button>();
		
		private Model<T> model;
		
		public RadioGroup() {
			this.model = new Model<T>();
		}
		
		public void addListener(int eventType, Listener listener) {
			if (eventType != SELECTION_CHANGED) {
				throw new IllegalArgumentException("Only supports Radio.SELECTION_CHANGED events");
			}
			
			listeners.add(listener);
		}

		public void removeListener(Listener listener) {
			listeners.remove(listener);
		}
		
		/**
		 * Selects the specified new value and returns the old value
		 * 
		 * @param value
		 * @return
		 */
		public T select(T value) {
			Button button = null;

			T oldValue = model.getSelection();
			model.setSelection(value);
			
			if (value != null) {
				button = memberMap.get(value);
				if (button == null) {
					throw new IllegalArgumentException("No control for value " + value);
				}
				button.setSelection(true);
			}
			
			deselectAllBut(button);
			
			return oldValue;
		}
		
		/**
		 * Adds a radio button to the group
		 * @param radio
		 */
		public void add(final Button radio, final T value) {
			memberMap.put(value, radio);
			
			radio.addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event event) {
					boolean selection = radio.getSelection();
					
					if (selection) {
						T oldValue = select(value);
						
						if (oldValue == null || !oldValue.equals(value)) {
							notifyListeners(Radio.SELECTION_CHANGED, new SelectionChangedEvent<T>(RadioGroup.this, value, oldValue));
						}
					}
				}
			});
		}
		
		protected void deselectAllBut(Button radio) {
			for (Button b: memberMap.values()) {
				if (radio == null || !b.equals(radio)) {
					b.setSelection(false);
					
					// required in order to propagate the selection change to the buttons
					b.notifyListeners(SWT.Selection, new Event());
				}
			}
		}

		protected void notifyListeners(int eventType, SelectionChangedEvent<T> event) {
			if (eventType != SELECTION_CHANGED) {
				return;
			}
			
			for (Listener l : listeners) {
				try {
					l.handleEvent(event);
				} catch (Exception e) {
					// need to do that in order to handle all listeners
					Activator.logError(e);
				}
			}
		}
	}

	// something above SWT.OpenDocument
	public static int SELECTION_CHANGED = 20001;
	
	/**
	 * Model for a radio group
	 * 
	 * @author nico.rehwaldt
	 *
	 * @param <T>
	 */
	static class Model<T> {

		protected T selection;
		
		public Model() { }
		
		public void setSelection(T selection) {
			this.selection = selection;
		}
		
		public T getSelection() {
			return selection;
		}
	}
	
	/**
	 * Radio selection event
	 * 
	 * @author nico.rehwaldt
	 *
	 * @param <T>
	 */
	public static class SelectionChangedEvent<T> extends Event {
		
		private static final long serialVersionUID = 1L;
		
		private T newSelection;
		private T oldSelection;

		public SelectionChangedEvent(Object source, T newSelection, T oldSelection) {
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
	 * Radio selection event listener adapter super class
	 * 
	 * @author nico.rehwaldt
	 *
	 * @param <T>
	 */
	public static abstract class RadioSelectionAdapter<T> implements Listener {
		
		@Override
		public final void handleEvent(Event event) {
			if (event instanceof SelectionChangedEvent) {
				try {
					SelectionChangedEvent<T> evt = (SelectionChangedEvent<T>) event;
					radioSelectionChanged(evt);
				} catch (Exception e) {
					// ignore
				}
			}
		}
		
		/**
		 * The radio selection changed
		 * 
		 * @param event
		 */
		public abstract void radioSelectionChanged(SelectionChangedEvent<T> event);
	}
}
