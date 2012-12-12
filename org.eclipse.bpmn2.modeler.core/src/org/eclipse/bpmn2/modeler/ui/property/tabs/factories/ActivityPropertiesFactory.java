package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FailedJobRetryTimeCycleType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FoxFactory;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.BooleanButtonBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.ModelButtonBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.ModelTextBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class ActivityPropertiesFactory extends AbstractPropertiesFactory {

	private static final EStructuralFeature ASYNC_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_Async();
	private static final EStructuralFeature RETRY_CYCLE_TIME_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_FailedJobRetryTimeCycle();
	
	public ActivityPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		
		boolean async = (Boolean) bo.eGet(ASYNC_FEATURE);
		
		final Button checkbox = PropertyUtil.createUnboundCheckbox(section, parent, "Asynchronous");
		final Text retryText = PropertyUtil.createUnboundText(section, parent, "Retry Time Cycle");
		
		// observing the checkbox and updating the model
		
		new RetryCycleStringTextBinding(bo, RETRY_CYCLE_TIME_FEATURE, retryText).establish();
		
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
			return getRetryTimeCycleValue(bo);
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
	
	private String getRetryTimeCycleValue(EObject bo) {
		List<ExtensionAttributeValue> extensionElements = ModelUtil.getExtensionAttributeValues(bo);
		if (extensionElements != null && extensionElements.size() == 1) {
			Object o = extensionElements.get(0).getValue().get(ModelPackage.eINSTANCE.getDocumentRoot_FailedJobRetryTimeCycle(), true);
			if (o instanceof FeatureMapUtil.FeatureEList<?>) {
				FeatureMapUtil.FeatureEList<?> featureList = (FeatureMapUtil.FeatureEList<?>) o;
				if (featureList.size() == 1) {
					FailedJobRetryTimeCycleType failedJobRetryTimeCycleValue = (FailedJobRetryTimeCycleType) featureList.get(0);
					return failedJobRetryTimeCycleValue.getText();
				}
			}
			Iterator<FeatureMap.Entry> iterator = extensionElements.get(0).getValue().iterator();
			while (iterator.hasNext()) {
				FeatureMap.Entry entry = iterator.next();
				if (entry.getEStructuralFeature().getName().equals(ModelPackage.eINSTANCE.getDocumentRoot_FailedJobRetryTimeCycle().getName())) {
					Object value = entry.getValue();
					if (value instanceof AnyType) {
						AnyType failedJobRetryTimeCycleAnyType = (AnyType) value;
						FeatureMap.Entry featureMapEntry = failedJobRetryTimeCycleAnyType.getMixed().get(0);
						if (featureMapEntry.getEStructuralFeature().getName().equals("text")) {
							return (String) featureMapEntry.getValue();
						}
					}
				}
			}
		}
		return "";
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
	
	private void updateRetryTimeCycle(String retryTimeCycle) { 
		List<ExtensionAttributeValue> extensionElements = ModelUtil.getExtensionAttributeValues(bo);
		
		if (extensionElements != null) {
			if (extensionElements.isEmpty()) {
				extensionElements.add(Bpmn2Factory.eINSTANCE.createExtensionAttributeValue());
			} else {
				deleteRetryTimeCycle();
			}
			
			FailedJobRetryTimeCycleType failed = FoxFactory.eINSTANCE.createFailedJobRetryTimeCycleType();
			failed.setText(retryTimeCycle.trim());
			extensionElements.get(0).getValue().add(ModelPackage.eINSTANCE.getDocumentRoot_FailedJobRetryTimeCycle(), failed);
		}
	}
	
	private void deleteRetryTimeCycle() {
		List<ExtensionAttributeValue> extensionElements = ModelUtil.getExtensionAttributeValues(bo);
		if (extensionElements != null && extensionElements.size() == 1) {
			Iterator<Entry> iterator = extensionElements.get(0).getValue().iterator();
			while (iterator.hasNext()) {
				EStructuralFeature feature = iterator.next().getEStructuralFeature();
				if (feature.getName().equals(ModelPackage.eINSTANCE.getDocumentRoot_FailedJobRetryTimeCycle().getName())) {
					iterator.remove();
					break;
				}
			}
		}
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
				deleteRetryTimeCycle();
			} else {
				updateRetryTimeCycle(newValue);
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
				deleteRetryTimeCycle();
			} else {
				bo.eSet(ASYNC_FEATURE, async);
			}
		}
	}
}
