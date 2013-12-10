package org.camunda.bpm.modeler.ui.property.tabs.builder;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.FeatureChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.binding.BooleanButtonBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.StringTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.camunda.bpm.modeler.ui.property.tabs.util.HelpText;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public class CompensateEventDefinitionPropertiesBuilder extends AbstractPropertiesBuilder<CompensateEventDefinition> {

	private static final EStructuralFeature WAIT_FOR_COMPLETION = Bpmn2Package.eINSTANCE.getCompensateEventDefinition_WaitForCompletion();
	private static final EStructuralFeature ACTIVITY_REF = Bpmn2Package.eINSTANCE.getCompensateEventDefinition_ActivityRef();

	public CompensateEventDefinitionPropertiesBuilder(Composite parent, GFPropertySection section, CompensateEventDefinition eventDefinition) {
		super(parent, section, eventDefinition);
	}

	@Override
	public void create() {
		Button checkbox = createCheckbox();
		new BooleanButtonBinding(bo, WAIT_FOR_COMPLETION, checkbox).establish();

		Text text = PropertyUtil.createUnboundText(section, parent, "Activity Reference");
		PropertyUtil.attachNoteWithLink(section, text, HelpText.COMPENSATION_THROWING_EVENT);

		new CompensateActivityRefTextBinding(bo, ACTIVITY_REF, text).establish();
	}

	// create checkbox with help text
	private Button createCheckbox() {
		Composite composite = PropertyUtil.createStandardComposite(section, parent);

		Composite waitForCompletionComposite = PropertyUtil.createStandardComposite(section, composite);
		waitForCompletionComposite.setLayoutData(PropertyUtil.getStandardLayout());

		// add label
		PropertyUtil.createLabel(section, composite, "Wait For Completion", waitForCompletionComposite);

		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Button checkbox = factory.createButton(waitForCompletionComposite, "", SWT.CHECK);

		FormData checkboxFormData = new FormData();
		checkboxFormData.top = new FormAttachment(0);
		checkboxFormData.left = new FormAttachment(0);

		checkbox.setLayoutData(checkboxFormData);

		Text text = new Text(waitForCompletionComposite, SWT.NO_BACKGROUND | SWT.NO_FOCUS);
		text.setText(HelpText.WAIT_FOR_COMPLETION_NOTE);

		factory.adapt(text, false, false);

		FormData helpTextFormData = new FormData();
		helpTextFormData.left = new FormAttachment(checkbox, 0);
		helpTextFormData.right = new FormAttachment(100, 0);

		text.setLayoutData(helpTextFormData);

		return checkbox;
	}

	private class CompensateActivityRefTextBinding extends StringTextBinding {

		private DefaultToolTip toolTip;
		private ControlDecoration errorDecoration;

		public CompensateActivityRefTextBinding(EObject model, EStructuralFeature feature, Text control) {
			super(model, feature, control);
			
			this.errorDecoration = new ControlDecoration(control, SWT.TOP | SWT.LEFT);

			FieldDecoration errorFieldIndicator = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
			try {
				errorDecoration.setImage(errorFieldIndicator.getImage());
				errorDecoration.hide();

				this.toolTip = new DefaultToolTip(control, SWT.BALLOON | SWT.ICON_ERROR, true);
				toolTip.setHideDelay(2000);
			} catch (Exception e) {
				// FIXME
				// errorDecoration.setImage(errorFieldIndicator.getImage())
				// is causing "Graphic disposed" message sometimes
			}
		}

		@Override
		protected String fromString(String str) {
			// hide previous error when model doesn't change
			hideError();
			if (str == null || str.trim().isEmpty()) {
				return null;
			} else {
				return str;
			}
		}

		@Override
		public String getModelValue() {
			Activity activity = (Activity) model.eGet(feature);
			if (activity != null && activity.getId() != null) {
				return activity.getId();
			} else {
				return null;
			}
		}

		@Override
		public void setModelValue(final String value) {
			hideError(); // hide previous error
			
			if (value == null || value.isEmpty()) {
				setTransactionalActivityRefValue(null);
			} else {
				FlowElementsContainer container = ModelUtil.getFlowElementsContainer((BaseElement) model);
				FlowElement flowElement = findActivity(container, value);
				if (flowElement != null) {
					setTransactionalActivityRefValue((Activity) flowElement);
					// hide previous error when right element found
					hideError();
				} else {
					if (container instanceof SubProcess && ((SubProcess) container).isTriggeredByEvent()) {
						flowElement = findActivity((FlowElementsContainer) container.eContainer(), value);
						if (flowElement != null) {
							setTransactionalActivityRefValue((Activity) flowElement);
							// hide previous error when right element found
							hideError();
						} else {
							showError("No valid activity id for this process.");
						}
					}
				}
			}
		}

		private void setTransactionalActivityRefValue(final Activity activity) {
			TransactionalEditingDomain domain = getTransactionalEditingDomain();
			ModelUtil.setValue(domain, model, feature, activity);
		}

		private FlowElement findActivity(FlowElementsContainer container , String value) {
			List<FlowElement> flowElements = container.getFlowElements();
			if (flowElements != null && !flowElements.isEmpty()) {
				for (EObject eObject : flowElements) {
					FlowElement flowElement = (FlowElement) eObject;
					if (flowElement.getId().equals(value) && flowElement instanceof Activity) {
						return flowElement;
					}
				}
			}
			return null;
		}

		@Override
		protected void ensureChangeSupportAdded() {
			EAttributeChangeSupport changeSupport = new EAttributeChangeSupport(model, feature, control);
			changeSupport.setFilter(new ExtensionChangeFilter(model, feature).or(new FeatureChangeFilter(model, feature)));

			EAttributeChangeSupport.ensureAdded(changeSupport, control);
		}

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
	}
}
