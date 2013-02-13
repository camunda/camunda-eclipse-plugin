package org.eclipse.bpmn2.modeler.ui.property.tabs.binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.validation.model.ConstraintStatus;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

public abstract class ValidatingTextBinding<V> extends
		ModelAttributeTextBinding<V> {

	List<Integer> checkedErrorCodes = new ArrayList<Integer>();
	private boolean mandatory = false; 
	
	private DefaultToolTip toolTip;
	private ControlDecoration errorDecoration;
	private boolean updateViewValue = true;
	
	public ValidatingTextBinding(EObject model, EStructuralFeature feature,
			Text control) {
		super(model, feature, control);
		this.errorDecoration = new ControlDecoration(control, SWT.TOP
				| SWT.LEFT);

		FieldDecoration errorFieldIndicator = FieldDecorationRegistry
				.getDefault().getFieldDecoration(
						FieldDecorationRegistry.DEC_ERROR);

		errorDecoration.setImage(errorFieldIndicator.getImage());
		errorDecoration.hide();

		this.toolTip = new DefaultToolTip(control,
				SWT.BALLOON | SWT.ICON_ERROR, true);
		toolTip.setHideDelay(2000);
	}

	public void addErrorCode(Integer errorCode) {
		checkedErrorCodes.add(errorCode);
	}
	
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	@Override
	public void setModelValue(final V value) {
		hideError(); // hide previous error
		
		final TransactionalEditingDomain domain = getTransactionalEditingDomain();
		
		if (mandatory && (value == null || isEmptyValue(value))) {
			showError("This value is mandatory. Empty value will not be saved.");
			return;
		}
		// wrap the underlying command stuff, so we know exactly what we gonna undo
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			
			@Override
			protected void doExecute() {
				ModelUtil.setValue(domain, model, feature, value);
			}
			
		});
		
		ModelValidationService modelValidationService = ModelValidationService
				.getInstance();
		IStatus validation = modelValidationService.newValidator(
				EvaluationMode.BATCH).validate(model);
		
		Stack<IStatus> statusStack = new Stack<IStatus>();
		statusStack.add(validation);
		
		// a IStatus might have children, we are using a stack to really get all validation status
		// by just pushing the children on the stack
		while (!statusStack.isEmpty()) {
			IStatus status = statusStack.pop();
			
			if (status instanceof ConstraintStatus) {
				ConstraintStatus constraintStatus = ((ConstraintStatus) status);
				if (constraintStatus.getTarget().equals(model)
						&& checkedErrorCodes.contains(constraintStatus
								.getCode())) {
					showError(constraintStatus.getMessage());
					
					updateViewValue = false;

					// revert the model value
					domain.getCommandStack().undo();
				}
			}
			
			statusStack.addAll(Arrays.asList(status.getChildren()));
		}
		
	}
	
	protected abstract boolean isEmptyValue(V value);
	
	protected void showError(String message) {
		toolTip.setText(message);
		errorDecoration.setDescriptionText(message);
		
		Point location = new Point(0, control.getBounds().height);
		toolTip.show(location);
		errorDecoration.show();
	}
	
	protected void hideError() {
		errorDecoration.hide();
		toolTip.hide();
	}

	@Override
	public void setViewValue(V value) {
		if (!updateViewValue) {
			updateViewValue = true;
		}else {
			super.setViewValue(value);
		}
	}
	
}
