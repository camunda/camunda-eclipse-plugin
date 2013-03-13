package org.camunda.bpm.modeler.test.util.operations;

import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class AddEventDefinitionOperation extends Operation<AddContext, IAddFeature> {

	public AddEventDefinitionOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		
		this.context = createContext();
	}
	
	public AddEventDefinitionOperation addDefinition(EventDefinition definition) {
		context.setNewObject(definition);
		return this;
	}
	
	public AddEventDefinitionOperation toEvent(Shape shape) {
		context.setTargetContainer((ContainerShape) shape);
		return this;
	}
	
	@Override
	protected AddContext createContext() {
		AddContext context = new AddContext();
		return context;
	}

	@Override
	protected IAddFeature createFeature(AddContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return featureProvider.getAddFeature(context);
	}
	
	public static AddEventDefinitionOperation addEventDefinition(IDiagramTypeProvider diagramTypeProvider) {
		return new AddEventDefinitionOperation(diagramTypeProvider);
	}
}
