package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import static org.eclipse.bpmn2.modeler.core.utils.ExtensionUtil.getExtension;
import static org.eclipse.bpmn2.modeler.core.utils.ExtensionUtil.removeExtensionByFeature;
import static org.eclipse.bpmn2.modeler.core.utils.ExtensionUtil.updateExtension;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FailedJobRetryTimeCycleType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FoxFactory;
import org.eclipse.bpmn2.modeler.ui.change.filter.ExtensionChangeFilter;
import org.eclipse.bpmn2.modeler.ui.change.filter.FeatureChangeFilter;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.ModelTextBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.HelpText;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Factory for retry time cycle enabled elements
 * 
 * @author nico.rehwaldt
 */
public class RetryEnabledPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	private static final EStructuralFeature RETRY_CYCLE_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_FailedJobRetryTimeCycle();
	
	public RetryEnabledPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		createRetryCycleText();
	}
	
	protected Text createRetryCycleText() {

		final Text retryText = PropertyUtil.createUnboundText(section, parent, "Retry Time Cycle");

		PropertyUtil.attachNote(retryText, HelpText.TIME_CYCLE);
		
		// observing the checkbox and updating the model
		
		new RetryCycleStringTextBinding(bo, RETRY_CYCLE_FEATURE, retryText).establish();
		
		return retryText;
	}
	
	/**
	 * Binding for the retry time cycle element
	 * 
	 * @author nico.rehwaldt
	 */
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
		protected String fromString(String value) {
			if (value == null || value.trim().isEmpty()) {
				return null;
			} else {
				return value;
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
		
		@Override
		protected void ensureChangeSupportAdded() {
			EAttributeChangeSupport changeSupport = new EAttributeChangeSupport(model, feature, control);
			changeSupport.setFilter(new ExtensionChangeFilter(model, feature).or(new FeatureChangeFilter(model, feature)));
			
			EAttributeChangeSupport.ensureAdded(changeSupport, control);
		}
	}
	
	// invocation of transactional behavior ///////////////
	
	protected void transactionalUpdateRetryTimeCycle(String newValue) {
		TransactionalEditingDomain domain = getTransactionalEditingDomain();
		domain.getCommandStack().execute(new UpdateRetryTimeCycleCommand(domain, newValue));
	}

	protected TransactionalEditingDomain getTransactionalEditingDomain() {
		return TransactionUtil.getEditingDomain(bo);
	}
	
	// transactional behavior //////////////////////////////
	
	protected void updateRetryCycle(String retryTimeCycle) {
		FailedJobRetryTimeCycleType retryCycle = FoxFactory.eINSTANCE.createFailedJobRetryTimeCycleType();
		retryCycle.setText(retryTimeCycle.trim());
	
		updateExtension(bo, RETRY_CYCLE_FEATURE, retryCycle);
	}
	
	protected void removeRetryCycle() {
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
}
