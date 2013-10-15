package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.ui.property.tabs.binding.BooleanButtonBinding;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author kristin.polenz
 *
 */
public class IsForCompensationPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	private static final EStructuralFeature IS_FOR_COMPENSATION_FEATURE = Bpmn2Package.eINSTANCE.getActivity_IsForCompensation();
	
	public IsForCompensationPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		
		final Button checkbox = PropertyUtil.createUnboundCheckbox(section, parent, "For Compensation");
		
		new IsForCompensationFlagButtonBinding(bo, IS_FOR_COMPENSATION_FEATURE, checkbox).establish();
	}

	private class IsForCompensationFlagButtonBinding extends BooleanButtonBinding {

		public IsForCompensationFlagButtonBinding(EObject model, EStructuralFeature feature, Button control) {
			super(model, feature, control);
		}
		
		@Override
		public Boolean getModelValue() {
			return (Boolean) bo.eGet(IS_FOR_COMPENSATION_FEATURE);
		}

		@Override
		public void setModelValue(final Boolean value) {
			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
			editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
			
				@Override
				protected void doExecute() {
					if (!value) {
						bo.eUnset(IS_FOR_COMPENSATION_FEATURE);
					} else {
						bo.eSet(IS_FOR_COMPENSATION_FEATURE, value);
					}
				}
			});
		}
	}
}
