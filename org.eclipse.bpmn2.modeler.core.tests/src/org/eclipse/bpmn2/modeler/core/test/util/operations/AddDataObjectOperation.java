package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.modeler.ui.features.data.DataObjectFeatureContainer.CreateDataObjectFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddDataObjectOperation extends AbstractAddOperation<CreateDataObjectFeature, AddDataObjectOperation> {

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
