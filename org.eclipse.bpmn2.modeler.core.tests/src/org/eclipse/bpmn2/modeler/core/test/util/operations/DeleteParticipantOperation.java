package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.modeler.ui.features.participant.DeleteParticipantFeature;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class DeleteParticipantOperation extends Operation<IDeleteContext, DeleteParticipantFeature> {

	private PictogramElement pictogramElement;

	public DeleteParticipantOperation(PictogramElement pictogramElement, IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		
		this.pictogramElement = pictogramElement;
		
		createContext();
		createFeature();
	}

	@Override
	protected void createContext() {
		DeleteContext tmpContext = new DeleteContext(pictogramElement);
		context = tmpContext;
	}

	@Override
	protected void createFeature() {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		feature = new DeleteParticipantFeature(featureProvider);	
	}
	
	public static DeleteParticipantOperation deleteParticipant (PictogramElement pictogramElement, IDiagramTypeProvider diagramTypeProvider) {
		return new DeleteParticipantOperation(pictogramElement, diagramTypeProvider);
	}
	
}
