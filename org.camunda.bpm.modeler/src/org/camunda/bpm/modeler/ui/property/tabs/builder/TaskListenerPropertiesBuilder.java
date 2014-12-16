package org.camunda.bpm.modeler.ui.property.tabs.builder;

import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.addExtension;
import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.removeExtensionByValue;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.ScriptType;
import org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.IsManyAttributeAnyChildChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.AbstractDeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.EditRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.dialog.TaskListenerDialog;
import org.camunda.bpm.modeler.ui.property.tabs.tables.CreateViaDialogElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EObjectAttributeTableColumnDescriptor;
import org.camunda.bpm.modeler.ui.property.tabs.tables.TypedColumnLabelProvider;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Builder for the multi instance properties
 * 
 * @author nico.rehwaldt
 */
public class TaskListenerPropertiesBuilder extends AbstractPropertiesBuilder<UserTask> {

	protected static final EStructuralFeature TASK_LISTENER_TYPE_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_TaskListener();
	
	private static final EClass TASK_LISTENER_TYPE = ModelPackage.eINSTANCE.getTaskListenerType();

	private static final String[] TASK_LISTENER_TYPE_FEATURE_NAMES = { 
		"Class",
		"Delegate Expression", 
		"Expression",
		"Script",
		"Event" };
	
	protected static final EStructuralFeature TASK_LISTENER_CLASS_FEATURE = ModelPackage.eINSTANCE.getTaskListenerType_Class();
	protected static final EStructuralFeature TASK_LISTENER_DELEGATE_EXPRESSION_FEATURE = ModelPackage.eINSTANCE.getTaskListenerType_DelegateExpression();
	protected static final EStructuralFeature TASK_LISTENER_EXPRESSION_FEATURE = ModelPackage.eINSTANCE.getTaskListenerType_Expression();
	protected static final EStructuralFeature TASK_LISTENER_SCRIPT_FEATURE = ModelPackage.eINSTANCE.getTaskListenerType_Script();
	protected static final EStructuralFeature TASK_LISTENER_EVENT_FEATURE = ModelPackage.eINSTANCE.getTaskListenerType_Event();
	
	private static final EStructuralFeature[] TASK_LISTENER_TYPE_FEATURES = {
		TASK_LISTENER_CLASS_FEATURE,
		TASK_LISTENER_DELEGATE_EXPRESSION_FEATURE,
		TASK_LISTENER_EXPRESSION_FEATURE,
		TASK_LISTENER_SCRIPT_FEATURE,
		TASK_LISTENER_EVENT_FEATURE
	};

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
		
		ElementFactory<TaskListenerType> elementFactory = new CreateViaDialogElementFactory<TaskListenerType>(TransactionUtil.getEditingDomain(bo)) {
			
			@Override
			protected TaskListenerType createType() {
				TaskListenerType instance = (TaskListenerType) transactionalCreateType(typeECls, feature);
				return instance;
			}

			@Override
			protected int editInDialog(TaskListenerType element) {
				return openEditDialog(element);
			}
		};

		ContentProvider<TaskListenerType> contentProvider = new ContentProvider<TaskListenerType>() {

			@Override
			@SuppressWarnings("unchecked")
			public List<TaskListenerType> getContents() {
				return (List<TaskListenerType>) ExtensionUtil.getExtensions(bo, typeCls);
			}
		};
		
		DeleteRowHandler<TaskListenerType> deleteHandler = new AbstractDeleteRowHandler<TaskListenerType>() {
			@Override
			public void rowDeleted(TaskListenerType element) {
				transactionalRemoveMapping(element);
			}
		};
		
		EditRowHandler<TaskListenerType> editRowHandler = new EditRowHandler<TaskListenerType>() {

			@Override
			public void rowEdit(TaskListenerType element) {
				openEditDialog(element);
			}

			@Override
			public boolean canEdit(TaskListenerType element) {
				return true;
			}
		};
		
		EditableEObjectTableBuilder<TaskListenerType> builder = new EditableEObjectTableBuilder<TaskListenerType>(section, composite, TaskListenerType.class) {
			
			@Override
			protected EObjectAttributeTableColumnDescriptor<TaskListenerType> createAttributeTableColumnDescriptor(final EStructuralFeature columnFeature, String columnLabel, int weight) {
				EObjectAttributeTableColumnDescriptor<TaskListenerType> columnDescriptor = null;
				
				if (!TASK_LISTENER_SCRIPT_FEATURE.equals(columnFeature)) {
					columnDescriptor = super.createAttributeTableColumnDescriptor(columnFeature, columnLabel, weight);
					
				} else {
					columnDescriptor = new EObjectAttributeTableColumnDescriptor<TaskListenerType>(columnFeature, columnLabel, 30) {
						
						@Override
						public ColumnLabelProvider getColumnLabelProvider() {
							ColumnLabelProvider labelProvider = new TypedColumnLabelProvider<TaskListenerType>() {
								
								@Override
								public String getText(TaskListenerType element) {
									boolean clss = element.eIsSet(TASK_LISTENER_CLASS_FEATURE);
									boolean delegateExpression = element.eIsSet(TASK_LISTENER_DELEGATE_EXPRESSION_FEATURE);
									boolean expression = element.eIsSet(TASK_LISTENER_EXPRESSION_FEATURE);
									
									if (!(clss || delegateExpression || expression)) {
										
										if (element.eIsSet(TASK_LISTENER_SCRIPT_FEATURE)) {
											ScriptType script = (ScriptType) element.eGet(TASK_LISTENER_SCRIPT_FEATURE);
											
											String result = "";
											
											String scriptFormat = script.getScriptFormat();
											if (scriptFormat != null) {
												result = "format: " + scriptFormat;
											}
											
											String scriptResource = script.getResource();
											if (scriptResource != null) {
												if (!result.isEmpty()) {
													result += "; ";
												}
												result += "resource: " + scriptResource;
											}

											FeatureMap mixed = script.getMixed();
											if (mixed != null && !mixed.isEmpty()) {
												String text = script.getText(); 
												if (!result.isEmpty()) {
													result += "; ";
												}
												result += "script: " + text;
											}
											
											return result;
										}
									}
									return "";
								}
							};
							
							return labelProvider;
						}
					};
				}
				
				return columnDescriptor;
			}
		};
		
		builder
			.doubleClickEditRowHandler(editRowHandler)
			.columnFeatures(columnFeatures)
			.columnLabels(columnLabels)
			.elementFactory(elementFactory)
			.contentProvider(contentProvider)
			.deleteRowHandler(deleteHandler)
			.model(bo)
			.changeFilter(new ExtensionChangeFilter(bo, feature).or(new IsManyAttributeAnyChildChangeFilter(bo, feature) {
				
				@Override
				public boolean matches(Notification notification) {
					Object notifierObj = notification.getNotifier();
					
					if (notifierObj != null && notifierObj instanceof ScriptType) {
						ScriptType script = (ScriptType) notifierObj;
						
						List<?> allExtensions = ExtensionUtil.getExtensions(object, feature.getEType().getInstanceClass());
						
						if (allExtensions != null && !allExtensions.isEmpty()) {
							return allExtensions.contains(script.eContainer());
						}
					}
					
					return false;
				}
				
			}));
		
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
	
	protected int openEditDialog(TaskListenerType listener) {
		// create dialog with ok and cancel button
		TaskListenerDialog listenerDialog = new TaskListenerDialog(section, Display.getDefault().getActiveShell(), bo, listener);
		
		// open dialog and await user selection
		return listenerDialog.open();
	}
}
