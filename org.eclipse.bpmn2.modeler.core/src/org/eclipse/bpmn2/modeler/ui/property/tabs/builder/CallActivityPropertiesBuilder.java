package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import static org.eclipse.bpmn2.modeler.core.utils.ExtensionUtil.addExtension;
import static org.eclipse.bpmn2.modeler.core.utils.ExtensionUtil.getExtensions;
import static org.eclipse.bpmn2.modeler.core.utils.ExtensionUtil.removeExtensionByValue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.InType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelFactory;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.OutType;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change.EObjectChangeSupport;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.change.EObjectChangeSupport.ModelChangedEvent;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EObjectAttributeTableColumnDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EditableTableDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.TableColumnDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.Events.RowDeleted;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.NotificationFilter;
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

public class CallActivityPropertiesBuilder extends AbstractPropertiesBuilder<CallActivity> {

	private static final String CALLED_ELEMENT = "Called Element";

	private static final String[] TABLE_HEADERS = { "source", "sourceExpression", "target" };

	private static final EStructuralFeature[] IN_FEATURES = { 
		ModelPackage.eINSTANCE.getInType_Source(),
		ModelPackage.eINSTANCE.getInType_SourceExpression(), 
		ModelPackage.eINSTANCE.getInType_Target() };

	private static final EStructuralFeature[] OUT_FEATURES = { 
		ModelPackage.eINSTANCE.getOutType_Source(),
		ModelPackage.eINSTANCE.getOutType_SourceExpression(), 
		ModelPackage.eINSTANCE.getOutType_Target() };
	
	public CallActivityPropertiesBuilder(Composite parent, GFPropertySection section, CallActivity bo) {
		super(parent, section, bo);
	}
	
	@Override
	public void create() {
		
		// FIXME: Add broken callActiviti_CalledElement control
		
		PropertyUtil.createText(section, parent, CALLED_ELEMENT,
				ModelPackage.eINSTANCE.getCallActivity_CalledElement(), bo);

		EClass inTypeCls = ModelPackage.eINSTANCE.getInType();
		EReference inTypeFeature = ModelPackage.eINSTANCE.getDocumentRoot_In();

		EClass outTypeCls = ModelPackage.eINSTANCE.getOutType();
		EReference outTypeFeature = ModelPackage.eINSTANCE.getDocumentRoot_Out();

		createMappingsTable(section, parent, InType.class, "Input Mapping", inTypeCls, inTypeFeature, IN_FEATURES);
		createMappingsTable(section, parent, OutType.class, "Output Mapping", outTypeCls, outTypeFeature, OUT_FEATURES);
	}

	private <T extends EObject> void createMappingsTable(
			GFPropertySection section, Composite parent, 
			final Class<T> cls, String label, 
			final EClass typeCls, final EStructuralFeature feature, EStructuralFeature[] typeFeatures) {
		
		EditableTableDescriptor<T> tableDescriptor = new EditableTableDescriptor<T>();
		
		tableDescriptor.setElementFactory(new ElementFactory<T>() {
			
			@Override
			public T create() {
				T instance = (T) transactionalCreateType(typeCls, feature);
				return instance;
			}
		});
		
		List<TableColumnDescriptor> columns = new ArrayList<TableColumnDescriptor>();

		for (int i = 0; i < TABLE_HEADERS.length; i++) {
			String title = TABLE_HEADERS[i];
			EStructuralFeature typeFeature = typeFeatures[i];

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
				transactionalRemoveMapping(removedElement);
			}
		});
		
		EObjectChangeSupport changeSupport = new EObjectChangeSupport(bo, table);
		changeSupport.setFilter(new ExtensionAttributeChangeFilter(bo, typeCls));
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
		List<T> inTypes = getExtensions(bo, typeCls);
		
		viewer.setInput(inTypes);
		viewer.refresh();
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
	
	/**
	 * Checks for changes in the target objects extension elements
	 * 
	 * @author nico.rehwaldt
	 */
	private class ExtensionAttributeChangeFilter extends NotificationFilter.Custom {

		private EObject object;
		private EClass attributeClass;

		public ExtensionAttributeChangeFilter(EObject object, EClass attributeClass) {
			this.object = object;
			this.attributeClass = attributeClass;
		}
		
		@Override
		public boolean matches(Notification notification) {
			
			Object notifier = notification.getNotifier();
			if (notifier instanceof EObject) {
				EStructuralFeature feature = (EStructuralFeature) notification.getFeature();
				
				// perform a simple member check for the notifier
				// to figure out if we are dealing with relevant data
				return isMemberOfObject((EObject) notifier);
			} else {
				return false;
			}
		}
		
		private boolean isMemberOfObject(EObject notifier) {
			EObject container = notifier.eContainer();
			if (container == null) {
				return false;
			} else {
				// the case in which we currently handle a ExtensionAttributeValue element
				// object <- extensionAttributeValue
				if (object.equals(container)) {
					return true;
				} else {
					container = container.eContainer();
					if (container == null) {
						return false;
					} else {
						// the case in which we currently handle a extension attribute
						// object <- extensionAttributeValue <- attribute
						return object.equals(container);
					}
				}
			}
		}
	}
}
