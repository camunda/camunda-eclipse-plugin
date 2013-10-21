package org.camunda.bpm.modeler.ui.property.tabs.builder;

import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.addExtension;
import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.removeExtensionByValue;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.AbstractDeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EObjectAttributeTableColumnDescriptor.EditingSupportProvider;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.SequenceFlow;
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
public class ExecutionListenerPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	protected static final EStructuralFeature EXECUTION_LISTENER_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_ExecutionListener();
	
	private static final EClass EXECUTION_LISTENER_TYPE = ModelPackage.eINSTANCE.getExecutionListenerType();

	private static final String[] EXECUTION_LISTENER_TYPE_FEATURE_NAMES = { 
		"Class",
		"Delegate Expression", 
		"Expression", 
		"Event" };
	
	private static final EStructuralFeature EXECUTION_LISTENER_EVENT_TYPE = ModelPackage.eINSTANCE.getExecutionListenerType_Event();
	
	private static final EStructuralFeature[] EXECUTION_LISTENER_TYPE_FEATURES = { 
		ModelPackage.eINSTANCE.getExecutionListenerType_Class(),
		ModelPackage.eINSTANCE.getExecutionListenerType_DelegateExpression(),
		ModelPackage.eINSTANCE.getExecutionListenerType_Expression(),
		EXECUTION_LISTENER_EVENT_TYPE };
	
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
		
		DeleteRowHandler<T> deleteHandler = new AbstractDeleteRowHandler<T>() {
			@Override
			public void rowDeleted(T element) {
				transactionalRemoveMapping(element);
			}
		};
		
		EditingSupportProvider editingSupportProvider = null;
		
		if (bo instanceof SequenceFlow) {
			editingSupportProvider = new DefaultEditingSupportProvider();
		} else {
			editingSupportProvider = new NotTransitionElementEditingSupportProvider();
		}
		
		EditableEObjectTableBuilder<T> builder = new EditableEObjectTableBuilder<T>(section, composite, typeCls);
		
		builder
			.elementFactory(elementFactory)
			.contentProvider(contentProvider)
			.editingSupportProvider(editingSupportProvider)
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
