package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.modeler.ui.features.data.DataStoreReferenceFeatureContainer.CreateDataStoreReferenceFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddDataStoreOperation extends AbstractAddOperation<CreateDataStoreReferenceFeature, AddDataStoreOperation> {

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
