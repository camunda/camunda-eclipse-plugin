package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.features.data.DataStoreReferenceFeatureContainer.CreateDataStoreReferenceFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddDataStoreOperation extends AbstractAddShapeOperation<CreateDataStoreReferenceFeature, AddDataStoreOperation> {

	public AddDataStoreOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddDataStoreOperation.class);
	}

	@Override
	protected CreateDataStoreReferenceFeature createFeature(CreateContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new CreateDataStoreReferenceFeature(featureProvider);	
	}
	
	public static AddDataStoreOperation addDataStore(IDiagramTypeProvider diagramTypeProvider) {
		return new AddDataStoreOperation(diagramTypeProvider);
	}
}
