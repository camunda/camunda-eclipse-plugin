package org.eclipse.bpmn2.modeler.ui.property.tabs.binding;

import static org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.MODEL_CHANGED;

import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

public class ModelViewChangeSupport implements ResourceSetListener {
	
	private EObject model;
	private EStructuralFeature feature;
	private Control control;

	private DisposeListener unregisterListener = new UnregisterResourceListenerListener();
	
	private boolean registered = false;
	
	public ModelViewChangeSupport(EObject model, EStructuralFeature feature, Control control) {
		
		this.model = model;
		this.feature = feature;
		
		this.control = control;
	}
	
	// event registration ///////////////////////////////////////
	
	public boolean isRegistered() {
		return registered;
	}

	public void unregister() {
		unregisterEditingDomainListener();
		unregisterControlDisposeListener();
		
		registered = false;
	}
	
	public void register() {
		registerEditingDomainListener();
		registerControlDisposeListener();
		
		registered = true;
	}
	
	protected void registerControlDisposeListener() {
		control.addDisposeListener(unregisterListener);
	}

	protected void registerEditingDomainListener() {
		TransactionalEditingDomain transactionalEditingDomain = getTransactionalEditingDomain();
		transactionalEditingDomain.addResourceSetListener(this);
	}
	
	protected void unregisterControlDisposeListener() {
		if (!control.isDisposed()) {
			control.removeDisposeListener(unregisterListener);
		}
	}

	protected void unregisterEditingDomainListener() {
		TransactionalEditingDomain transactionalEditingDomain = getTransactionalEditingDomain();
		transactionalEditingDomain.removeResourceSetListener(this);
	}
	

	// ResourceSetListener API implementation /////////////////
	
	protected TransactionalEditingDomain getTransactionalEditingDomain() {
		return TransactionUtil.getEditingDomain(model);
	}

	@Override
	public NotificationFilter getFilter() {
		return new InstanceSpecificFilter();
	}

	@Override
	public boolean isAggregatePrecommitListener() {
		return false;
	}

	@Override
	public boolean isPostcommitOnly() {
		return false;
	}

	@Override
	public boolean isPrecommitOnly() {
		return false;
	}

	@Override
	public void resourceSetChanged(final ResourceSetChangeEvent event) {
		fireModelChanged(event);
	}

	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event) throws RollbackException {
		return null;
	}
	
	protected void fireModelChanged(ResourceSetChangeEvent event) {
		if (!control.isDisposed()) {
			control.notifyListeners(MODEL_CHANGED, new ModelChangedEvent(model, feature, control, event.getSource()));
		}
	}
	
	// utility classes ///////////////////////////////////
	
	/**
	 * Filters only changes for the managed business object and feature
	 * 
	 * @author nico.rehwaldt
	 */
	private class InstanceSpecificFilter extends NotificationFilter.Custom {

		@Override
		public boolean matches(Notification notification) {

			Object notificationFeature = notification.getFeature();
			Object notifier = notification.getNotifier();
			
			if (model.equals(notifier) || feature.equals(notificationFeature)) {
				return true;
			}
			
			if (notification.getEventType() == Notification.REMOVE) {
				Object removedValue = notification.getOldValue();
				
				if (removedValue instanceof FeatureMap.Entry) {
					FeatureMap.Entry removed = (FeatureMap.Entry) removedValue;
					EStructuralFeature removedFeature = removed.getEStructuralFeature();
					
					if (feature.equals(removedFeature)) {
						return true;
					}
				}
			}

			return false;
		}
	};
	
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
	 * @param feature
	 * @param control
	 */
	public static void ensureAdded(EObject model, EStructuralFeature feature, Control control) {
		if (control.getData("ModelViewChangeSupport_KEY") != null) {
			return;
		}
		
		ModelViewChangeSupport modelViewChangeSupport = new ModelViewChangeSupport(model, feature, control);
		modelViewChangeSupport.register();
		
		control.setData("ModelViewChangeSupport_KEY", modelViewChangeSupport);
	}
}
