package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.Signal;
import org.eclipse.bpmn2.modeler.core.change.filter.ExtensionChangeFilter;
import org.eclipse.bpmn2.modeler.core.change.filter.FeatureChangeFilter;
import org.eclipse.bpmn2.modeler.core.change.filter.NestedFeatureChangeFilter;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change.EObjectChangeSupport;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change.EObjectChangeSupport.ModelChangedEvent;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EObjectAttributeTableColumnDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EditableTableDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.TableColumnDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.RowDeleted;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;

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
		

		createMappingsTable(section, parent, Error.class, "Errors", errorCls, ROOT_ELEMENTS_FEATURE, ERROR_TABLE_HEADERS, ERROR_FEATURES);		
		createMappingsTable(section, parent, Signal.class, "Signals", signalCls, ROOT_ELEMENTS_FEATURE, SIGNAL_TABLE_HEADERS, SIGNAL_FEATURES);
		createMappingsTable(section, parent, Message.class, "Messages", messageCls, ROOT_ELEMENTS_FEATURE, MESSAGE_TABLE_HEADERS, MESSAGE_FEATURES);
	}

	private <T extends EObject> void createMappingsTable(
			GFPropertySection section, Composite parent, 
			final Class<T> cls, String label, 
			final EClass typeCls, final EStructuralFeature feature, String[] featureLabels, EStructuralFeature[] features) {
		
		EditableTableDescriptor<T> tableDescriptor = new EditableTableDescriptor<T>();
		
		tableDescriptor.setElementFactory(new ElementFactory<T>() {
			
			@Override
			public T create() {
				T instance = (T) transactionalCreateType(typeCls, feature);
				return instance;
			}
		});
		
		List<TableColumnDescriptor> columns = new ArrayList<TableColumnDescriptor>();

		for (int i = 0; i < featureLabels.length; i++) {
			String title = featureLabels[i];
			EStructuralFeature typeFeature = features[i];

			EObjectAttributeTableColumnDescriptor<EObject> descriptor = 
					new EObjectAttributeTableColumnDescriptor<EObject>(typeFeature, title, 30);

			columns.add(descriptor);
		}
		
		tableDescriptor.setColumns(columns);
		
		
		// create table composites ////////////
		
		Composite composite = PropertyUtil.createStandardComposite(section, parent);
		
		Composite tableComposite = new Composite(composite, SWT.NONE);
		FormData tableCompositeFormData = PropertyUtil.getStandardLayout();
		tableCompositeFormData.height = 100;
		
		tableComposite.setLayoutData(tableCompositeFormData);

		// create label
		PropertyUtil.createLabel(section, composite, label, tableComposite);
		
		// instantiate table and viewer ////////////
		
		final TableViewer viewer = tableDescriptor.createTableViewer(tableComposite);
		
		final Table table = viewer.getTable();

		
		// add listeners /////////////
		
		table.addListener(Events.ROW_DELETED, new Listener() {

			@Override
			public void handleEvent(Event e) {
				Events.RowDeleted<T> event = (RowDeleted<T>) e;
				
				T removedElement = event.getRemovedElement();
				transactionalRemoveMapping(removedElement, feature);
			}
		});
		
		EObjectChangeSupport changeSupport = new EObjectChangeSupport(bo, table);
		changeSupport.setFilter(new NestedFeatureChangeFilter(bo, feature).or(new FeatureChangeFilter(bo, feature)));
		changeSupport.register();
		
		table.addListener(Events.MODEL_CHANGED, new Listener() {

			@Override
			public void handleEvent(Event e) {
				ModelChangedEvent event = (ModelChangedEvent) e;
				updateViewerContents(viewer, cls);
			}
		});
		
		// finally update viewer contents
		updateViewerContents(viewer, cls);
	}
	
	protected <T extends EObject> void updateViewerContents(TableViewer viewer, Class<T> typeCls) {
		List<T> elements = ModelUtil.getAllRootElements(bo, typeCls);
		
		viewer.setInput(elements);
		viewer.refresh();
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
