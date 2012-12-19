package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import static org.eclipse.bpmn2.modeler.core.utils.ExtensionUtil.addExtension;
import static org.eclipse.bpmn2.modeler.core.utils.ExtensionUtil.removeExtensionByValue;

import java.util.List;

import org.eclipse.bpmn2.UserTask;
import org.eclipse.bpmn2.modeler.core.utils.ExtensionUtil;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.EventType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelFactory;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.TaskListenerType;
import org.eclipse.bpmn2.modeler.ui.change.filter.ExtensionChangeFilter;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeletedRowHandler;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EObjectAttributeTableColumnDescriptor.EditingSupportProvider;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for the multi instance properties
 * 
 * @author nico.rehwaldt
 */
public class TaskListenerPropertiesBuilder extends AbstractPropertiesBuilder<UserTask> {

	protected static final EStructuralFeature TASK_LISTENER_TYPE_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_ExecutionListener();
	
	private static final EClass TASK_LISTENER_TYPE = ModelPackage.eINSTANCE.getTaskListenerType();

	private static final String[] TASK_LISTENER_TYPE_FEATURE_NAMES = { 
		"Class",
		"Delegate Expression", 
		"Expression", 
		"Event" };
	
	private static final EStructuralFeature[] TASK_LISTENER_TYPE_FEATURES = {
		ModelPackage.eINSTANCE.getTaskListenerType_Class(),
		ModelPackage.eINSTANCE.getTaskListenerType_DelegateExpression(),
		ModelPackage.eINSTANCE.getTaskListenerType_Expression(),
		ModelPackage.eINSTANCE.getTaskListenerType_Event() };

	public TaskListenerPropertiesBuilder(Composite parent, GFPropertySection section, UserTask bo) {
		super(parent, section, bo);
	}
	
	@Override
	public void create() {
		createMappingsTable(
			section, parent, 
			TaskListenerType.class, "Task Listeners", 
			TASK_LISTENER_TYPE, 
			TASK_LISTENER_TYPE_FEATURE, 
			TASK_LISTENER_TYPE_FEATURES, 
			TASK_LISTENER_TYPE_FEATURE_NAMES);
	}
	
	private <T extends EObject> void createMappingsTable(
			GFPropertySection section, Composite parent, 
			final Class<T> typeCls, String label, 
			final EClass typeECls, final EStructuralFeature feature, EStructuralFeature[] columnFeatures, String[] columnLabels) {

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
				return ExtensionUtil.getExtensions(bo, typeCls);
			}
		};
		
		DeletedRowHandler<T> deleteHandler = new DeletedRowHandler<T>() {
			@Override
			public void rowDeleted(T element) {
				transactionalRemoveMapping(element);
			}
		};
		
		final EditingSupportProvider editingSupportProvider = new DefaultEditingSupportProvider();
		
		EditableEObjectTableBuilder<T> builder = new EditableEObjectTableBuilder<T>(section, composite, typeCls);
		
		builder
			.columnFeatures(columnFeatures)
			.columnLabels(columnLabels)
			.elementFactory(elementFactory)
			.contentProvider(contentProvider)
			.editingSupportProvider(editingSupportProvider)
			.deletedRowHandler(deleteHandler)
			.model(bo)
			.changeFilter(new ExtensionChangeFilter(bo, feature));
		
		final TableViewer viewer = builder.build();
		
		// table composite ////////////
		final Composite tableComposite = viewer.getTable().getParent();

		// create label
		PropertyUtil.createLabel(section, composite, label, tableComposite);
	}
	
	protected void transactionalRemoveMapping(final EObject element) {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
			
			@Override
			protected void doExecute() {
				removeExtensionByValue(bo, element);
			}
		});
	}
	
	private EObject transactionalCreateType(EClass typeCls, final EStructuralFeature feature) {
		final EObject instance = ModelFactory.eINSTANCE.create(typeCls);
		
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
			
			@Override
			protected void doExecute() {
				addExtension(bo, feature, instance);
			}
		});
		
		return instance;
	}
}
