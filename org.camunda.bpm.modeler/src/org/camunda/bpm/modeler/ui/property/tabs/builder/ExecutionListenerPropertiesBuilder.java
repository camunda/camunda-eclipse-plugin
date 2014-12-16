package org.camunda.bpm.modeler.ui.property.tabs.builder;

import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.addExtension;
import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.removeExtensionByValue;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.ScriptType;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.IsManyAttributeAnyChildChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.AbstractDeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.EditRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.dialog.ExecutionListenerDialog;
import org.camunda.bpm.modeler.ui.property.tabs.tables.CreateViaDialogElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EObjectAttributeTableColumnDescriptor;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.tables.TypedColumnLabelProvider;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
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
public class ExecutionListenerPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	protected static final EStructuralFeature EXECUTION_LISTENER_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_ExecutionListener();
	
	private static final EClass EXECUTION_LISTENER_TYPE = ModelPackage.eINSTANCE.getExecutionListenerType();

	private static final String[] EXECUTION_LISTENER_TYPE_FEATURE_NAMES = { 
		"Class",
		"Delegate Expression", 
		"Expression",
		"Script",
		"Event"
	};
	
	private static final EStructuralFeature EXECUTION_LISTENER_CLASS_FEATURE = ModelPackage.eINSTANCE.getExecutionListenerType_Class();
	private static final EStructuralFeature EXECUTION_LISTENER_DELEGATE_EXPRESSION_FEATURE = ModelPackage.eINSTANCE.getExecutionListenerType_DelegateExpression();
	private static final EStructuralFeature EXECUTION_LISTENER_EXPRESSION_FEATURE = ModelPackage.eINSTANCE.getExecutionListenerType_Expression();
	private static final EStructuralFeature EXECUTION_LISTENER_SCRIPT_FEATURE = ModelPackage.eINSTANCE.getExecutionListenerType_Script();
	private static final EStructuralFeature EXECUTION_LISTENER_EVENT_FEATURE = ModelPackage.eINSTANCE.getExecutionListenerType_Event();
	
	private static final EStructuralFeature[] EXECUTION_LISTENER_TYPE_FEATURES = { 
		EXECUTION_LISTENER_CLASS_FEATURE,
		EXECUTION_LISTENER_DELEGATE_EXPRESSION_FEATURE,
		EXECUTION_LISTENER_EXPRESSION_FEATURE,
		EXECUTION_LISTENER_SCRIPT_FEATURE,
		EXECUTION_LISTENER_EVENT_FEATURE
	};
	
	public ExecutionListenerPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}
	
	@Override
	public void create() {
		createMappingsTable(
			section, parent, 
			ExecutionListenerType.class, "Execution Listeners", 
			EXECUTION_LISTENER_TYPE, 
			EXECUTION_LISTENER_FEATURE, 
			EXECUTION_LISTENER_TYPE_FEATURES, 
			EXECUTION_LISTENER_TYPE_FEATURE_NAMES);
	}
	
	private <T extends EObject> void createMappingsTable(
			GFPropertySection section, Composite parent, 
			final Class<T> typeCls, String label, 
			final EClass typeECls, final EStructuralFeature feature, EStructuralFeature[] columnFeatures, String[] columnLabels) {

		// composite for mappings table
		Composite composite = PropertyUtil.createStandardComposite(section, parent);
		
		final ElementFactory<ExecutionListenerType> elementFactory = new CreateViaDialogElementFactory<ExecutionListenerType>(TransactionUtil.getEditingDomain(bo)) {

			@Override
			protected ExecutionListenerType createType() {
				ExecutionListenerType instance = (ExecutionListenerType) transactionalCreateType(typeECls, feature);
				return instance;
			}

			@Override
			protected int editInDialog(ExecutionListenerType element) {
				return openEditDialog(element);
			}
		};
		
		ContentProvider<ExecutionListenerType> contentProvider = new ContentProvider<ExecutionListenerType>() {

			@Override
			@SuppressWarnings("unchecked")
			public List<ExecutionListenerType> getContents() {
				return (List<ExecutionListenerType>) ExtensionUtil.getExtensions(bo, typeCls);
			}
		};
		
		DeleteRowHandler<ExecutionListenerType> deleteHandler = new AbstractDeleteRowHandler<ExecutionListenerType>() {
			@Override
			public void rowDeleted(ExecutionListenerType element) {
				transactionalRemoveMapping(element);
			}
		};
		
		EditRowHandler<ExecutionListenerType> editRowHandler = new EditRowHandler<ExecutionListenerType>() {

			@Override
			public void rowEdit(ExecutionListenerType element) {
				openEditDialog(element);
			}

			@Override
			public boolean canEdit(ExecutionListenerType element) {
				return true;
			}
		};
		
		EditableEObjectTableBuilder<ExecutionListenerType> builder = new EditableEObjectTableBuilder<ExecutionListenerType>(section, composite, ExecutionListenerType.class) {
			
			@Override
			protected EObjectAttributeTableColumnDescriptor<ExecutionListenerType> createAttributeTableColumnDescriptor(final EStructuralFeature columnFeature, String columnLabel, int weight) {
				EObjectAttributeTableColumnDescriptor<ExecutionListenerType> columnDescriptor = null;
				
				if (!EXECUTION_LISTENER_SCRIPT_FEATURE.equals(columnFeature)) {
					columnDescriptor = super.createAttributeTableColumnDescriptor(columnFeature, columnLabel, weight);
					
				} else {
					columnDescriptor = new EObjectAttributeTableColumnDescriptor<ExecutionListenerType>(columnFeature, columnLabel, 30) {
						
						@Override
						public ColumnLabelProvider getColumnLabelProvider() {
							ColumnLabelProvider labelProvider = new TypedColumnLabelProvider<ExecutionListenerType>() {
								
								@Override
								public String getText(ExecutionListenerType element) {
									boolean clss = element.eIsSet(EXECUTION_LISTENER_CLASS_FEATURE);
									boolean delegateExpression = element.eIsSet(EXECUTION_LISTENER_DELEGATE_EXPRESSION_FEATURE);
									boolean expression = element.eIsSet(EXECUTION_LISTENER_EXPRESSION_FEATURE);
									
									if (!(clss || delegateExpression || expression)) {
										
										if (element.eIsSet(EXECUTION_LISTENER_SCRIPT_FEATURE)) {
											ScriptType script = (ScriptType) element.eGet(EXECUTION_LISTENER_SCRIPT_FEATURE);
											
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
			.elementFactory(elementFactory)
			.contentProvider(contentProvider)
			.columnFeatures(columnFeatures)
			.columnLabels(columnLabels)
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
	
	protected int openEditDialog(ExecutionListenerType listener) {
		// create dialog with ok and cancel button
		ExecutionListenerDialog listenerDialog = new ExecutionListenerDialog(section, Display.getDefault().getActiveShell(), bo, listener);
		
		// open dialog and await user selection
		return listenerDialog.open();
	}
}
