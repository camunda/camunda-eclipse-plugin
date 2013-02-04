package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMN2FeatureProvider;
import org.eclipse.bpmn2.modeler.ui.features.event.StartEventFeatureContainer.CreateStartEventFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddStartEventOperation extends AbstractAddOperation<CreateStartEventFeature, AddStartEventOperation> {

	public AddStartEventOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddStartEventOperation.class);
	}
	
	@Override
	protected CreateStartEventFeature createFeature(CreateContext context) {
		BPMN2FeatureProvider featureProvider = (BPMN2FeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider.getCreateFeatureForBusinessObject(StartEvent.class);
		
		return (CreateStartEventFeature) createFeature;
	}
	
	public static AddStartEventOperation addStartEvent(IDiagramTypeProvider diagramTypeProvider) {
		return new AddStartEventOperation(diagramTypeProvider);
	}
}
