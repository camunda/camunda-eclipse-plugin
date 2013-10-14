package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.ui.features.data.DataObjectFeatureContainer.CreateDataObjectFeature;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddDataObjectOperation extends AbstractAddShapeOperation<CreateDataObjectFeature, AddDataObjectOperation> {

	private CreateDataObjectFeature.Option createOption = CreateDataObjectFeature.CREATE_NEW_REFERENCE;

	public AddDataObjectOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddDataObjectOperation.class);
	}

	@Override
	protected CreateDataObjectFeature createFeature(CreateContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();

		ContextUtil.set(context, CreateDataObjectFeature.CREATE_MODE, createOption);
		
		return new CreateDataObjectFeature(featureProvider);	
	}
	
	public static AddDataObjectOperation addDataObject(IDiagramTypeProvider diagramTypeProvider) {
		return new AddDataObjectOperation(diagramTypeProvider);
	}
	
	public AddDataObjectOperation withDataObject() {
		this.createOption = CreateDataObjectFeature.CREATE_NEW_REFERENCE_WITH_ATTACHED_DATA_OBJECT;
		return myself;
	}
	
	public AddDataObjectOperation referencingDataObject(DataObject dataObject) {
		this.createOption = new CreateDataObjectFeature.Option(null, dataObject);
		return myself;
	}
}
