package org.camunda.bpm.modeler.ui.property.tabs.builder;

import java.util.List;

import org.camunda.bpm.modeler.core.ModelHandler;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.change.filter.FeatureChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.NestedFeatureChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Process;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

public class ProcessPropertiesBuilder extends AbstractPropertiesBuilder<Process> {

	private static final EStructuralFeature IS_EXECUTABLE = Bpmn2Package.eINSTANCE.getProcess_IsExecutable();

	private static final EStructuralFeature FLOW_ELEMENTS_FEATURE = Bpmn2Package.eINSTANCE.getFlowElementsContainer_FlowElements();

	private static final String[] DATA_OBJECT_TABLE_HEADERS = { "name" };

	private static final EStructuralFeature[] DATA_OBJECT_FEATURES = {
		Bpmn2Package.eINSTANCE.getFlowElement_Name() };

	public ProcessPropertiesBuilder(Composite parent, GFPropertySection section, Process bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		Button isExecutableCheckbox = PropertyUtil.createCheckbox(section, parent, "Is Executable", IS_EXECUTABLE, bo);

		createDataObjectMappingsTable();
	}

	public void createDataObjectMappingsTable() {
		EClass dataObjectECls = Bpmn2Package.eINSTANCE.getDataObject();
		createMappingsTable(section, parent, DataObject.class, "Dataobjects", dataObjectECls, FLOW_ELEMENTS_FEATURE, DATA_OBJECT_FEATURES, DATA_OBJECT_TABLE_HEADERS);
	}

	protected <T extends EObject> void createMappingsTable(
			GFPropertySection section, final Composite parent,
			final Class<T> typeCls, String label, final EClass typeECls,
			final EStructuralFeature feature, EStructuralFeature[] columnFeatures, String[] columnLabels) {

		// composite for mappings table
		final Composite composite = PropertyUtil.createStandardComposite(section, parent);

		ElementFactory<T> elementFactory = new ElementFactory<T>() {

			@Override
			public T create() {
				T instance = (T) transactionalCreateType(typeECls, feature);
				return instance;
			}
		};

		final ContentProvider<T> contentProvider = new ContentProvider<T>() {

			@Override
			public List<T> getContents() {
				return ModelUtil.getAllFlowElements(bo, typeCls);
			}
		};

		DeleteRowHandler<T> deleteHandler = new DeleteRowHandler<T>() {

			@Override
			public void rowDeleted(T element) {
				transactionalRemoveMapping(element, feature);
			}

			@Override
			public boolean canDelete(T element) {
				// create dialog with ok and cancel button and warning icon
				MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_WARNING | SWT.OK| SWT.CANCEL);
				dialog.setMessage("Do you really want to delete this data object? If you delete it, the reference of the data object reference element is also deleted!");

				// open dialog and await user selection
				int returnCode = dialog.open();
				if (returnCode == SWT.OK) {
					return true;
				}

				return false;
			}
		};

		EditableEObjectTableBuilder<T> builder = new EditableEObjectTableBuilder<T>(section, composite, typeCls);

		builder
			.elementFactory(elementFactory)
			.contentProvider(contentProvider)
			.columnFeatures(columnFeatures)
			.columnLabels(columnLabels)
			.deleteRowHandler(deleteHandler)
			.model(bo)
			.changeFilter(
				new NestedFeatureChangeFilter(bo, feature)
					.or(new FeatureChangeFilter(bo, feature)));

		final TableViewer viewer = builder.build();

		// table composite ////////////
		final Composite tableComposite = viewer.getTable().getParent();

		// create label
		PropertyUtil.createLabel(section, composite, label, tableComposite);

	}

	protected void transactionalRemoveMapping(final EObject element, final EStructuralFeature feature) {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				EList<EObject> list = (EList<EObject>) bo.eGet(feature);

				// Delete references to deleted element
				removeDangelingObjectRefs(element);

				list.remove(element);
			}
		});
	}

	private EObject transactionalCreateType(EClass typeCls, final EStructuralFeature feature) {
		final FlowElement instance = (FlowElement) Bpmn2Factory.eINSTANCE.create(typeCls);

		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {

			@Override
			protected void doExecute() {
				EList<EObject> list = (EList<EObject>) bo.eGet(feature);
				list.add(instance);

				ModelUtil.setID(instance);
			}
		});

		return instance;
	}

	private void removeDangelingObjectRefs(EObject element) {
		List<DataObjectReference> dataObjectReferences = ModelHandler.getAll(bo.eResource(), DataObjectReference.class);
		for (DataObjectReference dataObjectReference : dataObjectReferences) {
			if (element.equals(dataObjectReference.getDataObjectRef())) {
				dataObjectReference.setDataObjectRef(null);
			}
		}
	}
}
