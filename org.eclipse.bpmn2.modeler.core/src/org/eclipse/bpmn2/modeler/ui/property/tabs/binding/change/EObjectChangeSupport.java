package org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change;

import static org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.MODEL_CHANGED;

import org.eclipse.bpmn2.modeler.ui.change.AbstractEObjectChangeSupport;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

/**
 * Notifies a control when the EObject has changed.
 * 
 * @author nico.rehwaldt
 */
public class EObjectChangeSupport extends AbstractEObjectChangeSupport {
	
	protected Control control;

	private DisposeListener unregisterListener = new UnregisterResourceListenerListener();

	public EObjectChangeSupport(EObject object, Control control) {
		super(object);
		
		this.control = control;
		this.filter = NotificationFilter.createNotifierFilter(object);
	}
	
	// event registration ///////////////////////////////////////
	
	/**
	 * Returns a hash which uniquely identifies the change support 
	 * with its bound control and object.
	 * 
	 * @return
	 */
	protected String getHash() {
		return 
			getClass().getName() + "_" + 
			object.eClass().getClassifierID() + "_" + 
			object.hashCode() + "_" + 
			control.getClass().getName() + "_" + 
			control.hashCode();
	}
	
	public final void unregister() {
		unregisterControlDisposeListener();
		super.unregister();
	}
	
	public final void register() {
		registerControlDisposeListener();
		super.register();
	}
	
	private void registerControlDisposeListener() {
		control.addDisposeListener(unregisterListener);
	}
	
	private void unregisterControlDisposeListener() {
		if (!control.isDisposed()) {
			control.removeDisposeListener(unregisterListener);
		}
	}
	
	// ResourceSetListener API implementation /////////////////

	@Override
	public final void resourceSetChanged(final ResourceSetChangeEvent event) {
		fireModelChanged(event);
	}
	
	protected void fireModelChanged(ResourceSetChangeEvent event) {
		if (!control.isDisposed()) {
			control.notifyListeners(MODEL_CHANGED, new ModelChangedEvent(object, null, control, event.getSource()));
		}
	}
	
	/**
	 * Listener which unregisters the EObject change listener when the 
	 * control it is attached to is disposed
	 * 
	 * @author nico.rehwaldt
	 */
	private class UnregisterResourceListenerListener implements DisposeListener {

		@Override
		public void widgetDisposed(DisposeEvent e) {
			unregisterEditingDomainListener();
		}
	}
	
	// event implementation /////////////////////////////
	
	/**
	 * Event sent to the client when the model changed
	 * 
	 * @author nico.rehwaldt
	 */
	public static class ModelChangedEvent extends Event {

		private Object source;
		private EObject model;
		private EStructuralFeature feature;
		
		private Control control;

		public ModelChangedEvent(EObject model, EStructuralFeature feature, Control control, Object source) {
			super();
			
			this.model = model;
			this.feature = feature;
			
			this.source = source;
			
			this.control = control;
		}
		
		public EObject getModel() {
			return model;
		}
		
		public Control getControl() {
			return control;
		}
		
		public EStructuralFeature getFeature() {
			return feature;
		}
		
		public Object getSource() {
			return source;
		}
	}
	
	// static utility methods /////////////////////////
	
	/**
	 * Adds change support to the given model and makes sure it is only added once.
	 * 
	 * @param model
	 * @param control
	 */
	public static void ensureAdded(EObject model, Control control) {
		
		EObjectChangeSupport changeSupport = new EObjectChangeSupport(model, control);
		String CHANGE_SUPPORT_KEY = changeSupport.getHash();
		
		if (control.getData(CHANGE_SUPPORT_KEY) != null) {
			return;
		}
		
		changeSupport.register();
		
		control.setData(CHANGE_SUPPORT_KEY, changeSupport);
	}
}
