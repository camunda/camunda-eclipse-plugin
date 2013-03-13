package org.camunda.bpm.modeler.ui.property.tabs.builder;

import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.addExtension;
import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.getExtensions;
import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.removeExtensionByValue;

import java.util.List;

import org.camunda.bpm.modeler.runtime.engine.model.InType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.OutType;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeletedRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

public class CallActivityPropertiesBuilder extends AbstractPropertiesBuilder<CallActivity> {

	private static final String CALLED_ELEMENT = "Called Element";

	private static final String[] FEATURE_NAMES = { "source", "sourceExpression", "target" };

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
		
		PropertyUtil.createText(section, parent, CALLED_ELEMENT,
				ModelPackage.eINSTANCE.getCallActivity_CalledElement(), bo);

		EClass inTypeCls = ModelPackage.eINSTANCE.getInType();
		EReference inTypeFeature = ModelPackage.eINSTANCE.getDocumentRoot_In();

		EClass outTypeCls = ModelPackage.eINSTANCE.getOutType();
		EReference outTypeFeature = ModelPackage.eINSTANCE.getDocumentRoot_Out();

		createMappingsTable(section, parent, InType.class, "Input Mapping", inTypeCls, inTypeFeature, IN_FEATURES, FEATURE_NAMES);
		createMappingsTable(section, parent, OutType.class, "Output Mapping", outTypeCls, outTypeFeature, OUT_FEATURES, FEATURE_NAMES);
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
				return getExtensions(bo, typeCls);
			}
		};
		
		DeletedRowHandler<T> deleteHandler = new DeletedRowHandler<T>() {
			@Override
			public void rowDeleted(T element) {
				transactionalRemoveMapping(element);
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
