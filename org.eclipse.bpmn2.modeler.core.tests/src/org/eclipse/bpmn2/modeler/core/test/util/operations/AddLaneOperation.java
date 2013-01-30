package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.modeler.ui.features.lane.CreateLaneFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddLaneOperation extends AbstractAddOperation<CreateLaneFeature, AddLaneOperation> {

	public AddLaneOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddLaneOperation.class);
	}

	@Override
	protected CreateLaneFeature createFeature(CreateContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new CreateLaneFeature(featureProvider);	
	}
	
	public static AddLaneOperation addLane(IDiagramTypeProvider diagramTypeProvider) {
		return new AddLaneOperation(diagramTypeProvider);
	}
}
