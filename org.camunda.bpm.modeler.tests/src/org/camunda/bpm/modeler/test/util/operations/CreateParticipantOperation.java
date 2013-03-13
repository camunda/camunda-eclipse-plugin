package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.ui.features.participant.CreateParticipantFeature;
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
		
		this.context = createContext();
	}

	@Override
	protected ICreateContext createContext() {
		CreateContext ctx = new CreateContext();
		
		ctx.setTargetContainer(targetContainer);
		ctx.setX(x);
		ctx.setY(y);
		ctx.setHeight(height);
		ctx.setWidth(width);
		
		return ctx;
	}

	@Override
	protected CreateParticipantFeature createFeature(ICreateContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return new CreateParticipantFeature(featureProvider);	
	}
	
	public static CreateParticipantOperation createParticipant (int x, int y, int width, int height, ContainerShape targetContainer, IDiagramTypeProvider diagramTypeProvider) {
		return new CreateParticipantOperation(x, y, width, height, targetContainer, diagramTypeProvider);
	}
	
}
