package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.features.participant.CreateParticipantFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddPoolOperation extends AbstractAddShapeOperation<CreateParticipantFeature, AddPoolOperation> {

	public AddPoolOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddPoolOperation.class);
	}

	@Override
	protected CreateParticipantFeature createFeature(CreateContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new CreateParticipantFeature(featureProvider);	
	}
	
	public static AddPoolOperation addPool(IDiagramTypeProvider diagramTypeProvider) {
		return new AddPoolOperation(diagramTypeProvider);
	}
}
