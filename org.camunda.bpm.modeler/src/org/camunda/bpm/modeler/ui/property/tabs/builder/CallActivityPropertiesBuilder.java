package org.camunda.bpm.modeler.ui.property.tabs.builder;

import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.addExtension;
import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.getExtensions;
import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.removeExtensionByValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.camunda.bpm.modeler.runtime.engine.model.InType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.OutType;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.FeatureChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.binding.IntegerTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ModelAttributeComboBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ModelExtensionButtonBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.AbstractDeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.util.Events;
import org.camunda.bpm.modeler.ui.property.tabs.util.HelpText;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.camunda.bpm.modeler.ui.util.Browser;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.validation.model.ConstraintStatus;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public class CallActivityPropertiesBuilder extends
		AbstractPropertiesBuilder<CallActivity> {

	private static final String CALLED_ELEMENT = "Called Element";
	private static final String VARIABLES_ALL = "all";
	private static final String BUSINESS_KEY_EXPRESSION = "#{execution.processBusinessKey}";

	private static final String[] FEATURE_NAMES = { "source",
			"sourceExpression", "target" };

	private static final EStructuralFeature[] IN_FEATURES = {
			ModelPackage.eINSTANCE.getInType_Source(),
			ModelPackage.eINSTANCE.getInType_SourceExpression(),
			ModelPackage.eINSTANCE.getInType_Target() };

	private static final EStructuralFeature[] OUT_FEATURES = {
			ModelPackage.eINSTANCE.getOutType_Source(),
			ModelPackage.eINSTANCE.getOutType_SourceExpression(),
			ModelPackage.eINSTANCE.getOutType_Target() };

	private static final EStructuralFeature IN_TYPE_ALL_VARIABLES = ModelPackage.eINSTANCE.getInType_Variables();
	private static final EStructuralFeature IN_TYPE_BUSINESS_KEY = ModelPackage.eINSTANCE.getInType_BusinessKey();
	private static final EStructuralFeature OUT_TYPE_ALL_VARIABLES = ModelPackage.eINSTANCE.getOutType_Variables();

	private static final EStructuralFeature CALLED_ELEMENT_BINDING = ModelPackage.eINSTANCE.getCallActivity_CalledElementBinding();
	private static final EStructuralFeature CALLED_ELEMENT_VERSION = ModelPackage.eINSTANCE.getCallActivity_CalledElementVersion();

	public CallActivityPropertiesBuilder(Composite parent,
			GFPropertySection section, CallActivity bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {

		PropertyUtil.createText(section, parent, CALLED_ELEMENT, ModelPackage.eINSTANCE.getCallActivity_CalledElement(), bo);

		final CCombo dropDown = PropertyUtil.createDropDown(section, parent, "Element Binding");
		dropDown.add("latest");
		dropDown.add("deployment");
		dropDown.add("version");

		// create called element version text element with binding
		final Text elementVersionText = PropertyUtil.createUnboundText(section, parent, "Element Version");
		PropertyUtil.attachNote(elementVersionText, HelpText.CALL_ACTIVITY_CALLED_ELEMENT_VERSION);
		final CalledElementVersionTextBinding calledElementVersionTextBinding = new CalledElementVersionTextBinding(bo, CALLED_ELEMENT_VERSION, elementVersionText);
		// validate unique id
		calledElementVersionTextBinding.addErrorCode(100);
		calledElementVersionTextBinding.addErrorCode(101);

		calledElementVersionTextBinding.setMandatory(true);
		calledElementVersionTextBinding.establish();

		// initial setup of GUI element
		elementVersionText.setEnabled("version".equals(bo.eGet(CALLED_ELEMENT_BINDING)));

		new CalledElementBindingComboBinding(bo, CALLED_ELEMENT_BINDING, dropDown).establish();

		dropDown.addListener(Events.MODEL_CHANGED, new Listener() {
			@Override
			public void handleEvent(Event event) {
				String dropDownText = dropDown.getText();
				elementVersionText.setEnabled("version".equals(dropDownText));
				if (elementVersionText.isEnabled() &&
					(elementVersionText.getText() == null ||
					elementVersionText.getText().isEmpty())) {
					calledElementVersionTextBinding.showError("This value is mandatory. Empty value will not be saved.");
				} else {
					calledElementVersionTextBinding.hideError();
				}
			}
		});

		EClass inTypeCls = ModelPackage.eINSTANCE.getInType();
		EReference inTypeFeature = ModelPackage.eINSTANCE.getDocumentRoot_In();

		EClass outTypeCls = ModelPackage.eINSTANCE.getOutType();
		EReference outTypeFeature = ModelPackage.eINSTANCE.getDocumentRoot_Out();

		createCheckbox(section, parent, inTypeCls, inTypeFeature, IN_TYPE_BUSINESS_KEY, "Business Key", HelpText.CALL_ACTIVITY_BUSINESS_KEY, BUSINESS_KEY_EXPRESSION);

		createMappingsTable(section, parent, InType.class, "Input Mapping", inTypeCls, inTypeFeature, IN_FEATURES, FEATURE_NAMES);
		createCheckbox(section, parent, inTypeCls, inTypeFeature, IN_TYPE_ALL_VARIABLES, "All Variables", HelpText.CALL_ACTIVITY_ALL_VARIABLES_IN, VARIABLES_ALL);

		createMappingsTable(section, parent, OutType.class, "Output Mapping", outTypeCls, outTypeFeature, OUT_FEATURES, FEATURE_NAMES);
		createCheckbox(section, parent, outTypeCls, outTypeFeature, OUT_TYPE_ALL_VARIABLES, "All Variables", HelpText.CALL_ACTIVITY_ALL_VARIABLES_OUT, VARIABLES_ALL);
	}

	private <T extends EObject> void createMappingsTable(
			GFPropertySection section, Composite parent,
			final Class<T> typeCls, String label, final EClass typeECls,
			final EStructuralFeature feature,
			EStructuralFeature[] columnFeatures, String[] columnLabels) {

		// composite for mappings table
		Composite composite = PropertyUtil.createStandardComposite(section, parent);

		ElementFactory<T> elementFactory = new ElementFactory<T>() {
			@Override
			public T create() {
				T instance = (T) transactionalCreateType(typeECls, feature);
				return instance;
			}
		};

		ContentProvider<T> contentProvider = new ContentProvider<T>() {

			@Override
			public List<T> getContents() {
				List<T> extensions = getExtensions(bo, typeCls);
				
				Iterator<T> iterator = extensions.iterator();
				while (iterator.hasNext()) {
					T elem = iterator.next();
					
					if (elem instanceof InType) {
						if (elem.eIsSet(IN_TYPE_ALL_VARIABLES) || elem.eIsSet(IN_TYPE_BUSINESS_KEY)) {
							iterator.remove();
							continue;
						}
					}
					
					if (elem instanceof OutType) {
						if (elem.eIsSet(OUT_TYPE_ALL_VARIABLES)) {
							iterator.remove();
							continue;
						}
					}
				}
				
				return extensions;
			}
		};

		DeleteRowHandler<T> deleteHandler = new AbstractDeleteRowHandler<T>() {
			@Override
			public void rowDeleted(T element) {
				transactionalRemoveMapping(element);
			}
		};

		EditableEObjectTableBuilder<T> builder = new EditableEObjectTableBuilder<T>(section, composite, typeCls);

		builder.elementFactory(elementFactory)
			   .contentProvider(contentProvider)
			   .columnFeatures(columnFeatures)
			   .columnLabels(columnLabels)
			   .deleteRowHandler(deleteHandler)
			   .model(bo)
			   .changeFilter(new ExtensionChangeFilter(bo, feature));

		final TableViewer viewer = builder.build();

		// table composite ////////////
		final Composite tableComposite = viewer.getTable().getParent();

		// create label
		PropertyUtil.createLabel(section, composite, label, tableComposite);
	}

	private <T> void createCheckbox(GFPropertySection section,
			Composite parent, final EClass typeECls,
			final EStructuralFeature featureParent, EStructuralFeature feature,
			String label, String helpText, String featureValue) {

		final Button checkbox = createCheckboxWithHelpText(section, parent, label, helpText);

		new ModelExtensionButtonBinding(bo, typeECls, featureParent, feature, checkbox, featureValue).establish();
	}

	protected void transactionalRemoveMapping(final EObject element) {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(
				new RecordingCommand(editingDomain) {

					@Override
					protected void doExecute() {
						removeExtensionByValue(bo, element);
					}
				});
	}

	private EObject transactionalCreateType(EClass typeCls,
			final EStructuralFeature feature) {
		final EObject instance = ModelFactory.eINSTANCE.create(typeCls);

		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(
				new RecordingCommand(editingDomain) {

					@Override
					protected void doExecute() {
						addExtension(bo, feature, instance);
					}
				});

		return instance;
	}

	private Button createCheckboxWithHelpText(GFPropertySection section,
			Composite parent, String label, String helpText) {
		Composite composite = PropertyUtil.createStandardComposite(section, parent);

		Composite checkboxComposite = PropertyUtil.createStandardComposite(section, composite);
		checkboxComposite.setLayoutData(PropertyUtil.getStandardLayout());

		// add label
		PropertyUtil.createLabel(section, composite, label, checkboxComposite);

		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Button checkbox = factory.createButton(checkboxComposite, "", SWT.CHECK);

		FormData checkboxFormData = new FormData();
		checkboxFormData.top = new FormAttachment(0);
		checkboxFormData.left = new FormAttachment(0);

		checkbox.setLayoutData(checkboxFormData);

		Link link = new Link(checkboxComposite, SWT.NO_BACKGROUND | SWT.NO_FOCUS);
		link.setText(helpText);

		factory.adapt(link, false, false);

		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Browser.open(e.text);
			}
		});

		FormData helpTextFormData = new FormData();
		helpTextFormData.left = new FormAttachment(checkbox, 0);
		helpTextFormData.right = new FormAttachment(100, 0);

		link.setLayoutData(helpTextFormData);
		return checkbox;
	}

	private static class CalledElementBindingComboBinding extends
			ModelAttributeComboBinding<String> {

		public CalledElementBindingComboBinding(EObject model,
				EStructuralFeature feature, CCombo control) {
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
			}
			return value;
		}

		@Override
		public String getModelValue() {
			return (String) model.eGet(feature);
		}

		@Override
		public void setModelValue(final String value) {
			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
			editingDomain.getCommandStack().execute(
					new RecordingCommand(editingDomain) {

						@Override
						protected void doExecute() {
							if (!value.equals("version")) {
								model.eUnset(CALLED_ELEMENT_VERSION);
							}
							model.eSet(feature, value);
						}
					});
		}
	}

	private class CalledElementVersionTextBinding extends IntegerTextBinding {

		List<Integer> checkedErrorCodes = new ArrayList<Integer>();
		private boolean mandatory = false;

		private DefaultToolTip toolTip;
		private ControlDecoration errorDecoration;
		private boolean updateViewValue = true;

		public CalledElementVersionTextBinding(EObject model, EStructuralFeature feature, Text control) {
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

		public void addErrorCode(Integer errorCode) {
			checkedErrorCodes.add(errorCode);
		}

		public void setMandatory(boolean mandatory) {
			this.mandatory = mandatory;
		}

		@Override
		public Integer getModelValue() {
			Integer value = (Integer) model.eGet(feature);
			String calledElementBindingValue = (String) model.eGet(CALLED_ELEMENT_BINDING);
			if (calledElementBindingValue.equals("version") && mandatory && (value == null || value.equals(0))) {
				showError("This value is mandatory. Empty value will not be saved.");
			}
			return value;
		}

		@Override
		public void setModelValue(final Integer value) {
			hideError(); // hide previous error

			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);

			if (mandatory && (value == null || value.equals(0))) {
				showError("This value is mandatory. Empty value will not be saved.");
				return;
			}

			editingDomain.getCommandStack().execute(
					new RecordingCommand(editingDomain) {
						@Override
						protected void doExecute() {
							if (value == null || value.equals(0)) {
								bo.eUnset(CALLED_ELEMENT_VERSION);
							} else {
								bo.eSet(CALLED_ELEMENT_VERSION, value);
							}
						}
					});

			ModelValidationService modelValidationService = ModelValidationService.getInstance();
			IStatus validation = modelValidationService.newValidator(EvaluationMode.BATCH).validate(model);

			Stack<IStatus> statusStack = new Stack<IStatus>();
			statusStack.add(validation);

			// a IStatus might have children, we are using a stack to really get
			// all validation status
			// by just pushing the children on the stack
			while (!statusStack.isEmpty()) {
				IStatus status = statusStack.pop();

				if (status instanceof ConstraintStatus) {
					ConstraintStatus constraintStatus = ((ConstraintStatus) status);
					if (constraintStatus.getTarget().equals(model) && checkedErrorCodes.contains(constraintStatus.getCode())) {
						showError(constraintStatus.getMessage());

						updateViewValue = false;

						// revert the model value
						editingDomain.getCommandStack().undo();
					}
				}

				statusStack.addAll(Arrays.asList(status.getChildren()));
			}

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

		@Override
		public void setViewValue(Integer value) {
			if (!updateViewValue) {
				updateViewValue = true;
			} else {
				super.setViewValue(value);
			}
		}
	}

}
