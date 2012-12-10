package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.modeler.ui.features.participant.CreateParticipantFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class CreateParticipantOperation extends Operation<ICreateContext, CreateParticipantFeature> {

	private ContainerShape targetContainer;
	private int x;
	private int y;
	private int width;
	private int height;

	public CreateParticipantOperation(int x, int y, int width, int height, ContainerShape targetContainer, IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		
		this.targetContainer = targetContainer;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		createContext();
		createFeature();
	}

	@Override
	protected void createContext() {
		CreateContext tmpContext = new CreateContext();
		
		tmpContext.setTargetContainer(targetContainer);
		tmpContext.setX(x);
		tmpContext.setY(y);
		tmpContext.setHeight(height);
		tmpContext.setWidth(width);
		
		context = tmpContext;
	}

	@Override
	protected void createFeature() {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		feature = new CreateParticipantFeature(featureProvider);	
	}
	
	public static CreateParticipantOperation createParticipant (int x, int y, int width, int height, ContainerShape targetContainer, IDiagramTypeProvider diagramTypeProvider) {
		return new CreateParticipantOperation(x, y, width, height, targetContainer, diagramTypeProvider);
	}
	
}
