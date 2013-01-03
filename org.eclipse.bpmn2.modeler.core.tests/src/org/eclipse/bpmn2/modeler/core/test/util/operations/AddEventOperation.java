package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.modeler.core.features.event.AddEventFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class AddEventOperation<T extends Event> extends Operation<CreateContext, AddEventFeature<T>> {

	public AddEventOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		
		createContext();
		createFeature();
	}

	public AddEventOperation<T> atLocation(int x, int y) {
		
		context.setLocation(x, x);
		
		return this;
	}
	
	public AddEventOperation<T> sized(int width, int height) {
		
		context.setSize(width, height);
		
		return this;
	}
	
	public AddEventOperation<T> inContainer(ContainerShape containerShape) {
		context.setTargetContainer(containerShape);
		
		return this;
	}
	
	@Override
	protected void createContext() {
		context = null; // new AddContext(new AreaContext());
		
		context.setX(0);
		context.setY(0);
		
		context.setWidth(-1);
		context.setHeight(-1);
	}

	@Override
	protected void createFeature() {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		feature = new AddEventFeature<T>(featureProvider);	
	}
}
