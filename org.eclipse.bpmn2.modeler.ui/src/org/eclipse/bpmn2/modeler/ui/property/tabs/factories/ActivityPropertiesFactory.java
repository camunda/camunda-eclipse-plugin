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
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class ActivityPropertiesFactory extends PropertiesFactory {
	
	public ActivityPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
	}

	@Override
	protected void createControls() {
		Composite asyncComposite = PropertyUtil.createStandardComposite(section, parent);
		
		final EStructuralFeature asyncFeature = ModelPackage.eINSTANCE.getDocumentRoot_Async();
		boolean async = (Boolean) bo.eGet(asyncFeature);
		final Button checkbox = PropertyUtil.createSimpleCheckbox(section, asyncComposite, async);
		
		PropertyUtil.createLabel(section, asyncComposite, "Asynchronous", checkbox);
		
		Composite retryComposite = PropertyUtil.createStandardComposite(section, parent);
		
		String retryTimeCycleValue = getRetryTimeCycleValue(bo);
		final Text retryText = PropertyUtil.createSimpleText(section, retryComposite, retryTimeCycleValue);
		
		if (!async) {
			retryText.setEnabled(false);
		}
		
		PropertyUtil.createLabel(section, retryComposite, "Retry Time Cycle", retryText);
		
		checkbox.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				boolean selection = checkbox.getSelection();
				if (selection) {
					retryText.setEnabled(true);
				} else {
					deleteRetryTimeCycleType(bo);
					clearExtensionElements(bo);
					retryText.setText("");
					retryText.setEnabled(false);
				}
				PropertyUtil.setValue(bo, asyncFeature, selection);
				
			}
		});
		
		retryText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent arg0) {
				saveRetryTimeCycleValue(bo, retryText.getText());
			}
		});
		
	}
	
	private void clearExtensionElements(final EObject bo) {
		TransactionalEditingDomain domain = BPMN2Editor.getActiveEditor().getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {

			@Override
			protected void doExecute() {
				List<ExtensionAttributeValue> extensionElements = ModelUtil.getExtensionAttributeValues(bo);
				if (extensionElements != null && !extensionElements.isEmpty()) {
					if (extensionElements.get(0).getValue().isEmpty()) {
						extensionElements.clear();
					}
				}
			}
			
		});
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
	
	private void saveRetryTimeCycleValue(final EObject bo, final String retryTimeCycleValue) {
		TransactionalEditingDomain domain = BPMN2Editor.getActiveEditor().getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {

			@Override
			protected void doExecute() {
				List<ExtensionAttributeValue> extensionElements = ModelUtil.getExtensionAttributeValues(bo);
				if (retryTimeCycleValue == null || retryTimeCycleValue.trim().isEmpty()) {
					deleteRetryTimeCycleType(bo);
					clearExtensionElements(bo);
					return;
				}
				if (extensionElements != null) {
					if (extensionElements.size() == 0) {
						extensionElements.add(Bpmn2Factory.eINSTANCE.createExtensionAttributeValue());
					} else {
						deleteRetryTimeCycleType(bo);
					}
					FailedJobRetryTimeCycleType failed = FoxFactory.eINSTANCE.createFailedJobRetryTimeCycleType();
					failed.setText(retryTimeCycleValue.trim());
					extensionElements.get(0).getValue().add(ModelPackage.eINSTANCE.getDocumentRoot_FailedJobRetryTimeCycle(), failed);
				}
			}
			
		});
	}
	
	private void deleteRetryTimeCycleType(final EObject bo) {
		TransactionalEditingDomain domain = BPMN2Editor.getActiveEditor().getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			
			@Override
			protected void doExecute() {
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
		});
	}	


}
