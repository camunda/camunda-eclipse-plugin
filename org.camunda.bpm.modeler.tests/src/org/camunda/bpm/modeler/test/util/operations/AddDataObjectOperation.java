package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.ui.features.data.DataObjectReferenceFeatureContainer.CreateDataObjectReferenceFeature;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddDataObjectOperation extends AbstractAddShapeOperation<CreateDataObjectReferenceFeature, AddDataObjectOperation> {

	private CreateDataObjectReferenceFeature.Option createOption = CreateDataObjectReferenceFeature.CREATE_NEW_REFERENCE_WITH_ATTACHED_DATA_OBJECT;

	public AddDataObjectOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddDataObjectOperation.class);
	}

	@Override
	protected CreateDataObjectReferenceFeature createFeature(CreateContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();

		ContextUtil.set(context, CreateDataObjectReferenceFeature.CREATE_MODE, createOption);
		
		return new CreateDataObjectReferenceFeature(featureProvider);	
	}
	
	public static AddDataObjectOperation addDataObjectReference(IDiagramTypeProvider diagramTypeProvider) {
		return new AddDataObjectOperation(diagramTypeProvider);
	}
	
	public AddDataObjectOperation referencingDataObject(DataObject dataObject) {
		this.createOption = new CreateDataObjectReferenceFeature.Option(null, dataObject);
		return myself;
	}
}
