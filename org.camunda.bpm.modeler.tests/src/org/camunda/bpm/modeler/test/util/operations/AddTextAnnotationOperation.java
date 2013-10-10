package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.diagram.BPMN2FeatureProvider;
import org.camunda.bpm.modeler.ui.features.artifact.CreateTextAnnotationFeature;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddTextAnnotationOperation extends AbstractAddShapeOperation<CreateTextAnnotationFeature, AddTextAnnotationOperation> {

	public AddTextAnnotationOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddTextAnnotationOperation.class);
	}

	@Override
	protected CreateTextAnnotationFeature createFeature(CreateContext context) {
		BPMN2FeatureProvider featureProvider = (BPMN2FeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider.getCreateFeatureForBusinessObject(TextAnnotation.class);
		
		return (CreateTextAnnotationFeature) createFeature;
	}
	
	public static AddTextAnnotationOperation addTextAnnotation(IDiagramTypeProvider diagramTypeProvider) {
		return new AddTextAnnotationOperation(diagramTypeProvider);
	}

}
