package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.diagram.BpmnFeatureProvider;
import org.camunda.bpm.modeler.ui.features.artifact.CreateTextAnnotationFeature;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddTextAnnotationOperation extends AbstractAddShapeOperation<CreateTextAnnotationFeature, AddTextAnnotationOperation> {

	public AddTextAnnotationOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddTextAnnotationOperation.class);
	}

	@Override
	protected CreateTextAnnotationFeature createFeature(CreateContext context) {
		BpmnFeatureProvider featureProvider = (BpmnFeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider.getCreateFeatureForBusinessObject(Bpmn2Package.eINSTANCE.getTextAnnotation());
		
		return (CreateTextAnnotationFeature) createFeature;
	}
	
	public static AddTextAnnotationOperation addTextAnnotation(IDiagramTypeProvider diagramTypeProvider) {
		return new AddTextAnnotationOperation(diagramTypeProvider);
	}

}
