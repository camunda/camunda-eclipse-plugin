package org.camunda.bpm.modeler.ui.property.tabs.dialog;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.runtime.engine.model.ConstraintType;
import org.camunda.bpm.modeler.runtime.engine.model.FormFieldType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.PropertyType;
import org.camunda.bpm.modeler.runtime.engine.model.ValueType;
import org.camunda.bpm.modeler.ui.change.filter.AnyNestedChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.FeatureChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.IsManyAttributeAnyChildChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ValidatingStringComboBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ValidatingStringTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.AbstractDeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.util.HelpText;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * dialog to add/edit form fields
 * 
 * @author kristin.polenz
 *
 */
public class FormFieldDetailsDialog extends ModelerDialog {

	protected static final int SHELL_WIDTH = 600;
	protected static final int SHELL_HEIGHT = 400;
	
	private static final String[] PROPERTIES_TABLE_HEADERS = { "id", "value"};

	private static final String[] VALIDATION_TABLE_HEADERS = { "name", "config"};

	private static final String[] VALUE_TABLE_HEADERS = { "id", "name"};

	private static final EStructuralFeature[] PROPERTY_FEATURES = {
		ModelPackage.eINSTANCE.getPropertyType_Id(),
		ModelPackage.eINSTANCE.getPropertyType_Value() };

	private static final EStructuralFeature[] CONSTRAINT_FEATURES = {
		ModelPackage.eINSTANCE.getConstraintType_Name(),
		ModelPackage.eINSTANCE.getConstraintType_Config() };

	private static final EStructuralFeature[] VALUE_FEATURES = {
		ModelPackage.eINSTANCE.getValueType_Id(),
		ModelPackage.eINSTANCE.getValueType_Name() };

	private static final EStructuralFeature FORM_FIELD_VALUE = ModelPackage.eINSTANCE.getFormFieldType_Value();
	private static final EStructuralFeature FORM_FIELD_TYPE = ModelPackage.eINSTANCE.getFormFieldType_Type();
	private static final EStructuralFeature FORM_FIELD_ID = ModelPackage.eINSTANCE.getFormFieldType_Id();

	private FormFieldType formFieldType;
	private GFPropertySection section;
	private Button okButton;

	private Text idText;
	private CCombo dropDown;

	private Composite valueMappingTableComposite = null;
	private Composite placeHolderComposite = null;

	private static final String[] SUPPORTED_TYPES = new String[] {
		 "string",
		 "long",
		 "boolean",
		 "date",
		 "enum"
	};

	private boolean mandatory = true;

	public FormFieldDetailsDialog(GFPropertySection section, Shell parentShell, FormFieldType formFieldType) {
	    super(parentShell, formFieldType);

		this.section = section;
	    this.formFieldType = formFieldType;
	}

	// create dialog area with controls
	@Override
	protected Control createDialogArea(final Composite parent) {
		idText = PropertyUtil.createUnboundText(section, parent, "Id");
		IdValidatingStringTextBinding idTextbinding = new IdValidatingStringTextBinding(formFieldType, FORM_FIELD_ID, idText);
		idTextbinding.addErrorCode(100);
		idTextbinding.addErrorCode(101);

		idTextbinding.setMandatory(mandatory);
		idTextbinding.establish();

		PropertyUtil.createText(section, parent, "Label", ModelPackage.eINSTANCE.getFormFieldType_Label(), formFieldType);
		PropertyUtil.createText(section, parent, "Default Value", ModelPackage.eINSTANCE.getFormFieldType_DefaultValue(), formFieldType);

		// create editable drop down
		dropDown = PropertyUtil.createDropDown(section, parent, "Type", SWT.BORDER);
		for (int i=0; i<SUPPORTED_TYPES.length; i++) {
			dropDown.add(SUPPORTED_TYPES[i]);
		}

		ValidatingStringComboBinding comboBinding = new ValidatingStringComboBinding(formFieldType, FORM_FIELD_TYPE, dropDown);
		comboBinding.addErrorCode(100);
		comboBinding.addErrorCode(101);

		comboBinding.setMandatory(mandatory);
		comboBinding.establish();

		dropDown.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				rebuildValueMappingTable(parent);
				okButton.setEnabled(checkOkButtonActivation());
			}
		});

		idText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				okButton.setEnabled(checkOkButtonActivation());
			}
		});

		// create place holder composite for value mapping table which is empty
		placeHolderComposite = PropertyUtil.createGridLayoutedComposite(section, parent);
		// create mapping table for form field value
		rebuildValueMappingTable(parent);

		// create mapping table for form field validation
		createMappingTable(section, parent, ModelPackage.eINSTANCE.getFormFieldType_Validation(), ConstraintType.class, "Validation", ModelPackage.eINSTANCE.getConstraintType(), 
			ModelPackage.eINSTANCE.getValidationType_Constraint(), CONSTRAINT_FEATURES, VALIDATION_TABLE_HEADERS);

		// create mapping table for form field properties
		createMappingTable(section, parent, ModelPackage.eINSTANCE.getFormFieldType_Properties(), PropertyType.class, "Properties", ModelPackage.eINSTANCE.getPropertyType(), 
	    		ModelPackage.eINSTANCE.getPropertiesType_Property(), PROPERTY_FEATURES, PROPERTIES_TABLE_HEADERS);

	    return parent;
	}

	private void rebuildValueMappingTable(Composite parent) {

		if (valueMappingTableComposite != null && !valueMappingTableComposite.isDisposed()) {
			valueMappingTableComposite.dispose();
		}

		valueMappingTableComposite = PropertyUtil.createGridLayoutedComposite(section, placeHolderComposite);

		String formFieldTypeValue  = (String) formFieldType.eGet(FORM_FIELD_TYPE);
		if ("enum".equals(formFieldTypeValue)) {
			createMappingTable(section, valueMappingTableComposite, null, ValueType.class, "Value", ModelPackage.eINSTANCE.getValueType(), FORM_FIELD_VALUE, VALUE_FEATURES, VALUE_TABLE_HEADERS);
		} 

		relayout(parent);
	}

	/**
	 * dialog OK button is only activated when mandatory fields are set
	 *
	 * @return
	 */
	private boolean checkOkButtonActivation() {
		if (formFieldType.eGet(FORM_FIELD_ID) != null && formFieldType.eGet(FORM_FIELD_TYPE) != null
		    && idText.getText() != null && !idText.getText().isEmpty()
		    && dropDown.getText() != null && !dropDown.getText().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	protected <T extends EObject> void createMappingTable(final GFPropertySection section, final Composite parent, final EStructuralFeature containerFeature, 
		final Class<T> typeCls, String label, final EClass typeECls, final EStructuralFeature feature, EStructuralFeature[] columnFeatures, String[] columnLabels) {

		// composite for mappings table
		final Composite composite = PropertyUtil.createStandardComposite(section, parent);

		final ElementFactory<T> elementFactory = new ElementFactory<T>() {

			@Override
			public T create() {
				return (T) transactionalCreateType(typeECls, feature, containerFeature);
			}
		};

		ContentProvider<T> contentProvider = new ContentProvider<T>() {

			@Override
			public List<T> getContents() {
				if (containerFeature == null) {
					return (List<T>) formFieldType.eGet(feature);
				}

				EObject containerInstance = (EObject) formFieldType.eGet(containerFeature);
				if (containerInstance != null) {
					return (List<T>) containerInstance.eGet(feature);
				}
				return new ArrayList<T>();
			}
		};

		DeleteRowHandler<T> deleteHandler = new AbstractDeleteRowHandler<T>() {
			@Override
			public void rowDeleted(T element) {
				transactionalRemoveMapping(element, feature, containerFeature);
			}
		};

		EditableEObjectTableBuilder<T> builder = new EditableEObjectTableBuilder<T>(section, composite, typeCls);

		builder
			.elementFactory(elementFactory)
			.contentProvider(contentProvider)
			.columnFeatures(columnFeatures)
			.columnLabels(columnLabels)
			.deleteRowHandler(deleteHandler)
			.model(formFieldType);

		if (containerFeature == null) {
			builder.changeFilter(
				new FeatureChangeFilter(formFieldType, feature).or(new IsManyAttributeAnyChildChangeFilter(formFieldType, feature)));
		} else {
			builder.changeFilter(
				new FeatureChangeFilter(formFieldType, containerFeature).or(new AnyNestedChangeFilter(formFieldType, containerFeature)));
		}

		final TableViewer viewer = builder.build();

		// table composite ////////////
		final Composite tableComposite = viewer.getTable().getParent();

		// create label
		PropertyUtil.createLabel(section, composite, label, tableComposite);
	}

	protected void transactionalRemoveMapping(final EObject element, final EStructuralFeature feature, final EStructuralFeature containerFeature) {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(formFieldType);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				if (containerFeature == null) {
					EList<EObject> list = (EList<EObject>) formFieldType.eGet(feature);
					list.remove(element);
				} else {
					EObject containerInstance = (EObject) formFieldType.eGet(containerFeature);
					if (containerInstance != null) {
						EList<EObject> list = (EList<EObject>) containerInstance.eGet(feature);
						list.remove(element);
						
						if (list.isEmpty()) {
							formFieldType.eUnset(containerFeature);
						}
					}
				}
			}
		});
	}

	private EObject transactionalCreateType(EClass typeECls, final EStructuralFeature feature, final EStructuralFeature containerFeature) {

		final EObject instance = ModelFactory.eINSTANCE.create(typeECls);

		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(formFieldType);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				if (containerFeature == null) {
					EList<EObject> list = (EList<EObject>) formFieldType.eGet(feature);
					list.add(instance);
				} else {
					EObject containerInstance = (EObject) formFieldType.eGet(containerFeature);
					if (containerInstance == null) {
						containerInstance = ModelFactory.eINSTANCE.create(feature.getEContainingClass());
						formFieldType.eSet(containerFeature, containerInstance);
					}
					EList<EObject> list = (EList<EObject>) containerInstance.eGet(feature);
					list.add(instance);
				}
			}
		});

		return instance;
	}

	/**
	 * Relayout the properties after changes in the
	 * controls.
	 * 
	 */
	private void relayout(Composite parent) {
		computeSize();

		parent.layout();
		parent.redraw();
	}

	/**
	 * Compute shell size after adding/removing
	 * controls.
	 * 
	 */
	private void computeSize() {
		Point size = getShell().computeSize( SWT.DEFAULT, SWT.DEFAULT );
		getShell().setSize(SHELL_WIDTH, size.y);
	}

	private class IdValidatingStringTextBinding extends ValidatingStringTextBinding {

		public IdValidatingStringTextBinding(EObject model, EStructuralFeature feature, Text control) {
			super(model, feature, control);
		}

		@Override
		public String getModelValue() {
			String value = (String) super.getModelValue();
			if (mandatory && (value == null || isEmptyValue(value))) {
				showError(HelpText.MANDATORY_VALUE);
				return null;
			}
			return value;
		}
	}

	// overriding some dialog methods for customizing

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Details");
		newShell.setSize(SHELL_WIDTH, SHELL_HEIGHT);

		Point shellSize = newShell.getSize();
		Rectangle screen = newShell.getMonitor().getBounds();

		int x = ((screen.width - shellSize.x) / 2) + screen.x;
		int y = ((screen.height - shellSize.y) / 2) + screen.y - 100;

		if (y <= 0) {
			y = 50 + screen.y;
		}

		newShell.setLocation(x, y);
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.TITLE | SWT.APPLICATION_MODAL);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		// create CANCEL
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		// disable button when mandatory fields are not set
		okButton.setEnabled(checkOkButtonActivation());

		// compute shell size at the end of the dialog creation
		computeSize();
	}

}