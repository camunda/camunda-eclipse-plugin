package org.camunda.bpm.modeler.ui.property.tabs.dialog;

import org.camunda.bpm.modeler.runtime.engine.model.EventType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ValidatingStringComboBinding;
import org.eclipse.bpmn2.BaseElement;
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
public class TaskListenerDialog extends ListenerDialog<TaskListenerType> {
	
	protected static final String DIALOG_LABEL = "Task Listener Details";
	
	protected static final EStructuralFeature TASK_LISTENER_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_TaskListener();
	protected static final EStructuralFeature TASK_LISTENER_EVENT_FEATURE = ModelPackage.eINSTANCE.getTaskListenerType_Event();
	protected static final EStructuralFeature TASK_LISTENER_CLASS_FEATURE = ModelPackage.eINSTANCE.getTaskListenerType_Class();
	protected static final EStructuralFeature TASK_LISTENER_EXPRESSION_FEATURE = ModelPackage.eINSTANCE.getTaskListenerType_Expression();
	protected static final EStructuralFeature TASK_LISTENER_DELEGATE_EXPRESSION_FEATURE = ModelPackage.eINSTANCE.getTaskListenerType_DelegateExpression();
	protected static final EStructuralFeature TASK_LISTENER_SCRIPT_FEATURE = ModelPackage.eINSTANCE.getTaskListenerType_Script();

	public TaskListenerDialog(GFPropertySection section, Shell parentShell, BaseElement bo, TaskListenerType listener) {
		super(section, parentShell, bo, listener);
	}

	@Override
	protected void configureEventDropDown(Composite parent, CCombo dropDown, final TaskListenerType listener) {
		dropDown.add(EventType.CREATE.getName());
		dropDown.add(EventType.ASSIGNMENT.getName());
		dropDown.add(EventType.COMPLETE.getName());
		
		ValidatingStringComboBinding comboBinding = new ValidatingStringComboBinding(listener, TASK_LISTENER_EVENT_FEATURE, dropDown) {
			
			@Override
			public String getModelValue() {
				EventType event = listener.getEvent();
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
							EventType event = EventType.getByName(value);
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
		return TASK_LISTENER_FEATURE;
	}

	@Override
	protected EStructuralFeature getListenerClassFeature() {
		return TASK_LISTENER_CLASS_FEATURE;
	}

	@Override
	protected EStructuralFeature getListenerDelegateExpressionFeature() {
		return TASK_LISTENER_DELEGATE_EXPRESSION_FEATURE;
	}

	@Override
	protected EStructuralFeature getListenerExpressionFeature() {
		return TASK_LISTENER_EXPRESSION_FEATURE;
	}

	@Override
	protected EStructuralFeature getListenerScriptFeature() {
		return TASK_LISTENER_SCRIPT_FEATURE;
	}
	
}
