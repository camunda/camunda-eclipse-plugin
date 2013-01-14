package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class DeleteParticipantOperation extends Operation<IDeleteContext, IDeleteFeature> {

	private PictogramElement pictogramElement;

	public DeleteParticipantOperation(PictogramElement pictogramElement, IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		
		this.pictogramElement = pictogramElement;
		
		this.context = createContext();
	}

	@Override
	protected IDeleteContext createContext() {
		return new DeleteContext(pictogramElement);
	}

	@Override
	protected IDeleteFeature createFeature(IDeleteContext context) {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		return featureProvider.getDeleteFeature(context);
	}
	
	public static DeleteParticipantOperation deleteParticipant (PictogramElement pictogramElement, IDiagramTypeProvider diagramTypeProvider) {
		return new DeleteParticipantOperation(pictogramElement, diagramTypeProvider);
	}
}
