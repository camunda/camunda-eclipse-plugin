package org.eclipse.bpmn2.modeler.ui.property.tabs.binding.util;

import static org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.MODEL_CHANGED;

import org.eclipse.bpmn2.modeler.core.change.AbstractEObjectChangeSupport;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

public class EObjectChangeSupport extends AbstractEObjectChangeSupport {
	
	protected Control control;

	private DisposeListener unregisterListener = new UnregisterResourceListenerListener();

	public EObjectChangeSupport(EObject object, Control control) {
		super(object);
		
		this.control = control;
		this.filter = NotificationFilter.createNotifierFilter(object);
	}
	
	// event registration ///////////////////////////////////////
	
	public void unregister() {
		unregisterControlDisposeListener();
		super.unregister();
	}
	
	public void register() {
		registerControlDisposeListener();
		super.register();
	}
	
	protected void registerControlDisposeListener() {
		control.addDisposeListener(unregisterListener);
	}
	
	protected void unregisterControlDisposeListener() {
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
		String CHANGE_SUPPORT_KEY = EObjectChangeSupport.class.getName() + "_" + model.eClass().getClassifierID() + "_" + model.hashCode();
		
		if (control.getData(CHANGE_SUPPORT_KEY) != null) {
			return;
		}
		
		EObjectChangeSupport modelViewChangeSupport = new EObjectChangeSupport(model, control);
		modelViewChangeSupport.register();
		
		control.setData(CHANGE_SUPPORT_KEY, modelViewChangeSupport);
	}
}
