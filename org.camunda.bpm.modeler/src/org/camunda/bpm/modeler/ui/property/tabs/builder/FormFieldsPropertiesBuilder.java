package org.camunda.bpm.modeler.ui.property.tabs.builder;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.FormDataType;
import org.camunda.bpm.modeler.runtime.engine.model.FormFieldType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.change.filter.AnyNestedChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.AbstractDeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.EditRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.dialog.FormFieldDetailsDialog;
import org.camunda.bpm.modeler.ui.property.tabs.tables.CreateViaDialogElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.util.HelpText;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Builder for form fields
 * 
 * @author kristin.polenz
 *
 */
public class FormFieldsPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	public FormFieldsPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}
	
	private static final EStructuralFeature FORM_DATA_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_FormData();

	private static final String[] TABLE_HEADERS = { "id", "label"};
	
	private static final EStructuralFeature[] FORM_FIELDS_FEATURES = { 
		ModelPackage.eINSTANCE.getFormFieldType_Id(),
		ModelPackage.eINSTANCE.getFormFieldType_Label() };

	private TableViewer viewer;
	
	@Override
	public void create() {
		EClass formFieldCls = ModelPackage.eINSTANCE.getFormFieldType();
		createMappingTable(section, parent, "Form Fields", formFieldCls, FORM_DATA_FEATURE, FORM_FIELDS_FEATURES, TABLE_HEADERS);
	}

	protected void createMappingTable(
			final GFPropertySection section, final Composite parent, String label, final EClass typeECls, 
			final EStructuralFeature feature, EStructuralFeature[] columnFeatures, String[] columnLabels) {

		// composite for mappings table
		final Composite composite = PropertyUtil.createStandardComposite(section, parent);
		
		final ElementFactory<FormFieldType> elementFactory = new CreateViaDialogElementFactory<FormFieldType>(TransactionUtil.getEditingDomain(bo)) {

			@Override
			protected FormFieldType createType() {
				return createFormFieldType();
			}

			@Override
			protected int editInDialog(FormFieldType element) {
				return openEditDialog(element);
			}
		};
		
		ContentProvider<FormFieldType> contentProvider = new ContentProvider<FormFieldType>() {

			@Override
			public List<FormFieldType> getContents() {
				List<FormFieldType> contents = new ArrayList<FormFieldType>();

				List<FormDataType> formDataList = ExtensionUtil.getExtensions(bo, FormDataType.class);
				// formData exists only once in the extension value
				if(formDataList != null && !formDataList.isEmpty()) {
					FormDataType formData = formDataList.get(0);
					contents.addAll((List<FormFieldType>) formData.getFormField());
				}
				return contents;
			}
		};
		
		DeleteRowHandler<FormFieldType> deleteHandler = new AbstractDeleteRowHandler<FormFieldType>() {
			@Override
			public void rowDeleted(FormFieldType element) {
				transactionalRemoveMapping(element);
			}
		};
		
		EditRowHandler<FormFieldType> editRowHandler = new EditRowHandler<FormFieldType>() {

			@Override
			public void rowEdit(FormFieldType element) {
				openEditDialog((FormFieldType) element);
			}

			@Override
			public boolean canEdit(FormFieldType element) {
				return true;
			}
		};
		
		EditableEObjectTableBuilder<FormFieldType> builder = new EditableEObjectTableBuilder<FormFieldType>(section, composite, FormFieldType.class);
		
		builder
			.doubleClickEditRowHandler(editRowHandler)
			.elementFactory(elementFactory)
			.contentProvider(contentProvider)
			.columnFeatures(columnFeatures)
			.columnLabels(columnLabels)
			.deleteRowHandler(deleteHandler)
			.attachTableEditNote(false)
			.model(bo)
			.changeFilter(new ExtensionChangeFilter(bo, feature).or(new AnyNestedChangeFilter(bo, feature)));
		
		viewer = builder.build();

		Composite tableComposite = viewer.getTable().getParent();
		PropertyUtil.attachNote(tableComposite, HelpText.TABLE_HELP + " " + HelpText.SUPPORTED_VERSION_NOTE_7_1);

		// create label
		PropertyUtil.createLabel(section, composite, label, tableComposite);
	}
	
	protected void transactionalRemoveMapping(final EObject element) {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
			
			@Override
			protected void doExecute() {
				// check whether form data exists
				FormDataType formData = null;
				List<FormDataType> formDataTypes = ExtensionUtil.getExtensions(bo, FormDataType.class);
				if(formDataTypes.isEmpty()) {
					ExtensionUtil.removeExtensionByFeature(bo, FORM_DATA_FEATURE);
				} else {
					// formData exists only once
					formData = formDataTypes.get(0);
					formData.getFormField().remove(element);
					// update model
					if (formData.getFormField().isEmpty()) {
						ExtensionUtil.removeExtensionByFeature(bo, FORM_DATA_FEATURE);
						// remove empty extension element
						EStructuralFeature extensionValuesFeature = bo.eClass().getEStructuralFeature("extensionValues");
						ExtensionAttributeValue values = ExtensionUtil.getExtensionAttributeValue(bo);
						if (values != null && (values.getValue() == null || values.getValue().isEmpty())) {
							bo.eUnset(extensionValuesFeature);
						}
					} else {
						ExtensionUtil.updateExtension(bo, FORM_DATA_FEATURE, formData);
					}
				}
			}
		});
	}
	
	private FormFieldType createFormFieldType() {
		
		final FormFieldType newFormField = ModelFactory.eINSTANCE.createFormFieldType();
		
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
		 
			@Override
			protected void doExecute() {

				// check whether form data already exists
				FormDataType formData = null;
				List<FormDataType> formDataTypes = ExtensionUtil.getExtensions(bo, FormDataType.class);
				if(formDataTypes.isEmpty()) {
					formData = ModelFactory.eINSTANCE.createFormDataType();
					ExtensionUtil.addExtension(bo, FORM_DATA_FEATURE, formData);
				} else {
					formData = formDataTypes.get(0);
				}

				// create new field
				formData.getFormField().add(newFormField);
			}
		});
		
		return newFormField;
	}

	private int openEditDialog(FormFieldType formField) {
		// create dialog with ok and cancel button and warning icon
		FormFieldDetailsDialog formFieldsDialog = new FormFieldDetailsDialog(section, Display.getDefault().getActiveShell(), formField);
		// open dialog and await user selection
		return formFieldsDialog.open();
	}
}
