package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import static org.eclipse.bpmn2.modeler.core.utils.ExtensionUtil.getExtension;
import static org.eclipse.bpmn2.modeler.core.utils.ExtensionUtil.removeExtensionByFeature;
import static org.eclipse.bpmn2.modeler.core.utils.ExtensionUtil.updateExtension;

import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FailedJobRetryTimeCycleType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FoxFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.BooleanButtonBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.ModelTextBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class ActivityPropertiesFactory extends AbstractPropertiesFactory {

	private static final EStructuralFeature ASYNC_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_Async();
	private static final EStructuralFeature RETRY_CYCLE_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_FailedJobRetryTimeCycle();
	
	public ActivityPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		
		boolean async = (Boolean) bo.eGet(ASYNC_FEATURE);
		
		final Button checkbox = PropertyUtil.createUnboundCheckbox(section, parent, "Asynchronous");
		final Text retryText = PropertyUtil.createUnboundText(section, parent, "Retry Time Cycle");
		
		// observing the checkbox and updating the model
		
		new RetryCycleStringTextBinding(bo, RETRY_CYCLE_FEATURE, retryText).establish();
		
		new AsyncFlagButtonBinding(bo, ASYNC_FEATURE, checkbox).establish();
		
		// initial setup of GUI elements
		retryText.setEnabled(async);

		checkbox.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				boolean selected = checkbox.getSelection();
				retryText.setEnabled(selected);
			}
		});
	}
	
	private class RetryCycleStringTextBinding extends ModelTextBinding<String> {

		public RetryCycleStringTextBinding(EObject model, EStructuralFeature feature, Text control) {
			super(model, feature, control);
		}

		@Override
		protected String toString(String value) {
			if (value == null) {
				return "";
			} else {
				return value;
			}
		}

		@Override
		protected String fromString(String string) {
			if (string == null || string.trim().isEmpty()) {
				return null;
			} else {
				return string;
			}
		}

		@Override
		public String getModelValue() {
			return (String) getExtension(bo, RETRY_CYCLE_FEATURE, "text");
		}

		@Override
		public void setModelValue(String value) {
			transactionalUpdateRetryTimeCycle(value);
		}
	}
	
	private class AsyncFlagButtonBinding extends BooleanButtonBinding {

		public AsyncFlagButtonBinding(EObject model, EStructuralFeature feature, Button control) {
			super(model, feature, control);
		}
		
		@Override
		public Boolean getModelValue() {
			return (Boolean) bo.eGet(ASYNC_FEATURE);
		}

		@Override
		public void setModelValue(Boolean value) {
			transactionalToggleAsync(value);
		}
	}
	
	// invocation of transactional behavior ///////////////
	
	private void transactionalUpdateRetryTimeCycle(String newValue) {
		TransactionalEditingDomain domain = getTransactionalEditingDomain();
		domain.getCommandStack().execute(new UpdateRetryTimeCycleCommand(domain, newValue));
	}
	
	private void transactionalToggleAsync(boolean async) {
		TransactionalEditingDomain domain = getTransactionalEditingDomain();
		domain.getCommandStack().execute(new ToggleAsyncCommand(domain, async));
	}

	private TransactionalEditingDomain getTransactionalEditingDomain() {
		return TransactionUtil.getEditingDomain(bo);
	}
	
	
	// transactional behavior //////////////////////////////
	
	private void updateRetryCycle(String retryTimeCycle) {
		FailedJobRetryTimeCycleType retryCycle = FoxFactory.eINSTANCE.createFailedJobRetryTimeCycleType();
		retryCycle.setText(retryTimeCycle.trim());
	
		updateExtension(bo, RETRY_CYCLE_FEATURE, retryCycle);
	}
	
	private void removeRetryCycle() {
		removeExtensionByFeature(bo, RETRY_CYCLE_FEATURE);
	}
	
	// commands ///////////////////////////////////////////
	
	/**
	 * Command which takes care of updating the retry time cycle
	 * 
	 * @author nico.rehwaldt
	 */
	private class UpdateRetryTimeCycleCommand extends RecordingCommand {

		private String newValue;
		
		public UpdateRetryTimeCycleCommand(TransactionalEditingDomain domain, String newValue) {
			super(domain);
			
			this.newValue = newValue;
		}

		@Override
		protected void doExecute() {

			
			if (newValue == null || newValue.trim().isEmpty()) {
				removeRetryCycle();
			} else {
				updateRetryCycle(newValue);
			}
		}
	}

	/**
	 * Command which takes of toggling the async flag of the managed 
	 * business object. That may lead to the deletion of the
	 * retry time cycle, too.
	 * 
	 * @author nico.rehwaldt
	 */
	private class ToggleAsyncCommand extends RecordingCommand {
		
		private boolean async;

		public ToggleAsyncCommand(TransactionalEditingDomain domain, boolean async) {
			super(domain);
			
			this.async = async;
		}

		@Override
		protected void doExecute() {
			if (!async) {
				bo.eUnset(ASYNC_FEATURE);
				removeRetryCycle();
			} else {
				bo.eSet(ASYNC_FEATURE, async);
			}
		}
	}
}
