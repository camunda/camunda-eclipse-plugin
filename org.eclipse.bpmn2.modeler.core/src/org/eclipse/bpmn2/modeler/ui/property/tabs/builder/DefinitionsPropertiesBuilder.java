package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.Signal;
import org.eclipse.bpmn2.modeler.core.change.filter.ExtensionChangeFilter;
import org.eclipse.bpmn2.modeler.core.change.filter.NestedFeatureChangeFilter;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeletedRowHandler;
import org.eclipse.bpmn2.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

public class DefinitionsPropertiesBuilder extends AbstractPropertiesBuilder<Definitions> {

	public DefinitionsPropertiesBuilder(Composite parent, GFPropertySection section, Definitions bo) {
		super(parent, section, bo);
	}

	private static final EStructuralFeature ROOT_ELEMENTS_FEATURE = Bpmn2Package.eINSTANCE.getDefinitions_RootElements();
	
	private static final String[] ERROR_TABLE_HEADERS = { "error code", "name" };

	private static final EStructuralFeature[] ERROR_FEATURES = { 
		Bpmn2Package.eINSTANCE.getError_ErrorCode(),
		Bpmn2Package.eINSTANCE.getError_Name() };
	
	private static final String[] SIGNAL_TABLE_HEADERS = { "name" };
	
	private static final EStructuralFeature[] SIGNAL_FEATURES = { 
		Bpmn2Package.eINSTANCE.getSignal_Name() };

	private static final String[] MESSAGE_TABLE_HEADERS = { "name" };
	
	private static final EStructuralFeature[] MESSAGE_FEATURES = { 
		Bpmn2Package.eINSTANCE.getMessage_Name() };
	
	@Override
	public void create() {
		EClass errorCls = Bpmn2Package.eINSTANCE.getError();
		EClass signalCls = Bpmn2Package.eINSTANCE.getSignal();
		EClass messageCls = Bpmn2Package.eINSTANCE.getMessage();
		
		createMappingsTable(section, parent, Error.class, "Errors", errorCls, ROOT_ELEMENTS_FEATURE, ERROR_FEATURES, ERROR_TABLE_HEADERS);
		createMappingsTable(section, parent, Message.class, "Messages", messageCls, ROOT_ELEMENTS_FEATURE, MESSAGE_FEATURES, MESSAGE_TABLE_HEADERS);
		createMappingsTable(section, parent, Signal.class, "Signals", signalCls, ROOT_ELEMENTS_FEATURE, SIGNAL_FEATURES, SIGNAL_TABLE_HEADERS);
	}

	private <T extends EObject> void createMappingsTable(
			GFPropertySection section, Composite parent, 
			final Class<T> typeCls, String label, final EClass typeECls, 
			final EStructuralFeature feature, EStructuralFeature[] columnFeatures, String[] columnLabels) {

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
				return ModelUtil.getAllRootElements(bo, typeCls);
			}
		};
		
		DeletedRowHandler<T> deleteHandler = new DeletedRowHandler<T>() {
			@Override
			public void rowDeleted(T element) {
				transactionalRemoveMapping(element, feature);
			}
		};
		
		EditableEObjectTableBuilder<T> builder = new EditableEObjectTableBuilder<T>(section, composite, typeCls);
		
		builder
			.elementFactory(elementFactory)
			.contentProvider(contentProvider)
			.columnFeatures(columnFeatures)
			.columnLabels(columnLabels)
			.deletedRowHandler(deleteHandler)
			.model(bo)
			.changeFilter(new NestedFeatureChangeFilter(bo, feature));
		
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
				list.remove(element);
			}
		});
	}
	
	private EObject transactionalCreateType(EClass typeCls, final EStructuralFeature feature) {
		final RootElement instance = (RootElement) Bpmn2Factory.eINSTANCE.create(typeCls);
		
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(bo);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
			
			@Override
			protected void doExecute() {
				ModelUtil.setID(instance);
				
				EList<EObject> list = (EList<EObject>) bo.eGet(feature);
				list.add(instance);
			}
		});
		
		return instance;
	}
}
