package org.camunda.bpm.modeler.ui.property.tabs.binding;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.widgets.Button;

public abstract class ModelRadioBinding extends ModelButtonBinding<Boolean> {

	private EStructuralFeature[] radioFeatures;

	public ModelRadioBinding(EObject model, EStructuralFeature feature, EStructuralFeature[] typeFeatures, Button control) {
		super(model, feature, control);
		
		this.radioFeatures = typeFeatures;
	}

	@Override
	public void setViewValue(Boolean value) {
		if (!control.isDisposed()) {
			control.setSelection(value);
		}
	}

	@Override
	public Boolean getViewValue() {
		return control.getSelection();
	}

	@Override
	public Boolean getModelValue() {
		return model.eIsSet(feature);
	}

	@Override
	public void setModelValue(Boolean value) {
		transactionalHandleFeatureActivation(value ? feature : null);
	}

	@Override
	protected void updateViewState(Boolean modelValue) {
		control.setSelection(modelValue);
	}
	
	// model radio binding ////////////////////////////////

	protected void transactionalHandleFeatureActivation(EStructuralFeature feature) {
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(model);
		domain.getCommandStack().execute(new ActivateFeatureCommand(domain, model, radioFeatures, feature));
	}

	protected abstract void activateFeature(EStructuralFeature feature);
	
	private class ActivateFeatureCommand extends RecordingCommand {
		
		private EObject object;
		
		private EStructuralFeature[] allFeatures;
		private EStructuralFeature activeFeature;
		
		public ActivateFeatureCommand(TransactionalEditingDomain domain, EObject object, EStructuralFeature[] allFeatures, EStructuralFeature activeFeature) {
			super(domain);
			
			this.object = object;
			
			this.allFeatures = allFeatures;
			this.activeFeature = activeFeature;
		}
		
		@Override
		protected void doExecute() {
			for (EStructuralFeature radioFeature: allFeatures) {
				if (!radioFeature.equals(activeFeature)) {
					object.eUnset(radioFeature);
				}
			}
			
			if (!getModelValue()) {
				activateFeature(activeFeature);
			}else {
				object.eUnset(activeFeature);
			}
		}
	}
}