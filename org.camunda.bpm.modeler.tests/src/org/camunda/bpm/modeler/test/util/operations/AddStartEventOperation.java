package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.diagram.Bpmn2FeatureProvider;
import org.camunda.bpm.modeler.ui.features.event.StartEventFeatureContainer.CreateStartEventFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddStartEventOperation extends AbstractAddShapeOperation<CreateStartEventFeature, AddStartEventOperation> {

	public AddStartEventOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddStartEventOperation.class);
	}
	
	@Override
	protected CreateStartEventFeature createFeature(CreateContext context) {
		Bpmn2FeatureProvider featureProvider = (Bpmn2FeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider.getCreateFeatureForBusinessObject(Bpmn2Package.eINSTANCE.getStartEvent());
		
		return (CreateStartEventFeature) createFeature;
	}
	
	public static AddStartEventOperation addStartEvent(IDiagramTypeProvider diagramTypeProvider) {
		return new AddStartEventOperation(diagramTypeProvider);
	}
}
