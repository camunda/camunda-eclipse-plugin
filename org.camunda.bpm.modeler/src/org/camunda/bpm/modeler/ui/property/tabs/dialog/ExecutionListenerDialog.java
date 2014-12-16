package org.camunda.bpm.modeler.ui.property.tabs.dialog;

import org.camunda.bpm.modeler.runtime.engine.model.EventType1;
import org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ValidatingStringComboBinding;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Roman Smirnov
 */
public class ExecutionListenerDialog extends ListenerDialog<ExecutionListenerType> {
	
	protected static final String DIALOG_LABEL = "Execution Listener Details";
	
	protected static final EStructuralFeature EXECUTION_LISTENER_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_ExecutionListener();
	protected static final EStructuralFeature EXECUTION_LISTENER_EVENT_FEATURE = ModelPackage.eINSTANCE.getExecutionListenerType_Event();
	protected static final EStructuralFeature EXECUTION_LISTENER_CLASS_FEATURE = ModelPackage.eINSTANCE.getExecutionListenerType_Class();
	protected static final EStructuralFeature EXECUTION_LISTENER_EXPRESSION_FEATURE = ModelPackage.eINSTANCE.getExecutionListenerType_Expression();
	protected static final EStructuralFeature EXECUTION_LISTENER_DELEGATE_EXPRESSION_FEATURE = ModelPackage.eINSTANCE.getExecutionListenerType_DelegateExpression();
	protected static final EStructuralFeature EXECUTION_LISTENER_SCRIPT_FEATURE = ModelPackage.eINSTANCE.getExecutionListenerType_Script();

	public ExecutionListenerDialog(GFPropertySection section, Shell parentShell, BaseElement bo, ExecutionListenerType listener) {
		super(section, parentShell, bo, listener);
	}

	@Override
	protected void configureEventDropDown(Composite parent, CCombo dropDown, final ExecutionListenerType listener) {
		dropDown.add(EventType1.START.getName());
		dropDown.add(EventType1.END.getName());
		
		if (bo instanceof SequenceFlow) {
			dropDown.add(EventType1.TAKE.getName());	
		}
		
		ValidatingStringComboBinding comboBinding = new ValidatingStringComboBinding(listener, EXECUTION_LISTENER_EVENT_FEATURE, dropDown) {
			
			@Override
			public String getModelValue() {
				EventType1 event = listener.getEvent();
				if (event != null) {
					return event.getName();
				}
				return EMPTY_STRING;
			}
			
			@Override
			public void setModelValue(final String value) {

				TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
					@Override
					protected void doExecute() {
						
						if (value != null && !value.isEmpty()) {
							EventType1 event = EventType1.getByName(value);
							model.eSet(feature, event);
						} else {
							model.eUnset(feature);
						}
					}
				});
			}
			
		};
		
		comboBinding.establish();
	}

	@Override
	protected String getDialogLabel() {
		return DIALOG_LABEL;
	}

	@Override
	protected EStructuralFeature getListenerFeature() {
		return EXECUTION_LISTENER_FEATURE;
	}

	@Override
	protected EStructuralFeature getListenerClassFeature() {
		return EXECUTION_LISTENER_CLASS_FEATURE;
	}

	@Override
	protected EStructuralFeature getListenerDelegateExpressionFeature() {
		return EXECUTION_LISTENER_EXPRESSION_FEATURE;
	}

	@Override
	protected EStructuralFeature getListenerExpressionFeature() {
		return EXECUTION_LISTENER_DELEGATE_EXPRESSION_FEATURE;
	}

	@Override
	protected EStructuralFeature getListenerScriptFeature() {
		return EXECUTION_LISTENER_SCRIPT_FEATURE;
	}
	
}
