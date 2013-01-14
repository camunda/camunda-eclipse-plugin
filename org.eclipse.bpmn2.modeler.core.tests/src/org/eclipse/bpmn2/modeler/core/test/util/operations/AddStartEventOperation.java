package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.modeler.ui.features.event.StartEventFeatureContainer.CreateStartEventFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class AddStartEventOperation<T extends Event> extends Operation<CreateContext, CreateStartEventFeature> {

	public AddStartEventOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		
		this.context = createContext();
	}

	public AddStartEventOperation<T> atLocation(int x, int y) {
		context.setLocation(x, y);
		return this;
	}
	
	public AddStartEventOperation<T> sized(int width, int height) {
		context.setSize(width, height);
		return this;
	}
	
	public AddStartEventOperation<T> inContainer(ContainerShape containerShape) {
		context.setTargetContainer(containerShape);
		
		return this;
	}
	
	@Override
	protected CreateContext createContext() {
		CreateContext context = new CreateContext();
		context.setX(0);
		context.setY(0);
		
		context.setWidth(100);
		context.setHeight(100);
		
		return context;
	}

	@Override
	protected CreateStartEventFeature createFeature(CreateContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new CreateStartEventFeature(featureProvider);	
	}
}
