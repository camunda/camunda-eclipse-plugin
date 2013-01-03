package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.modeler.ui.features.participant.MoveParticipantFeature;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

/**
 * 
 * @author Daniel Meyer
 * @author Nico Rehwaldt
 */
public class MoveParticipantOperation extends MoveShapeOperation<MoveParticipantFeature, MoveParticipantOperation> {

	public MoveParticipantOperation(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		super(shape, diagramTypeProvider, MoveParticipantOperation.class);
	}
	
	@Override
	protected void createFeature() {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		EObject element = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(shape);
		
		assertInstanceOf(Participant.class, element);
	
		feature = new MoveParticipantFeature(featureProvider);
	}
	
	public static MoveParticipantOperation move(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		return new MoveParticipantOperation(shape, diagramTypeProvider).toContainer(shape.getContainer());
	}
}
