package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.features.data.DataObjectFeatureContainer.CreateDataObjectFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddDataObjectOperation extends AbstractAddShapeOperation<CreateDataObjectFeature, AddDataObjectOperation> {

	public AddDataObjectOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddDataObjectOperation.class);
	}

	@Override
	protected CreateDataObjectFeature createFeature(CreateContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new CreateDataObjectFeature(featureProvider);	
	}
	
	public static AddDataObjectOperation addDataObject(IDiagramTypeProvider diagramTypeProvider) {
		return new AddDataObjectOperation(diagramTypeProvider);
	}
}
