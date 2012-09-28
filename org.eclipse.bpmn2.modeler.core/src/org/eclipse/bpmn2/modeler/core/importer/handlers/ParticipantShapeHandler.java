package org.eclipse.bpmn2.modeler.core.importer.handlers;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class ParticipantShapeHandler extends AbstractShapeHandler<Participant> {

	public ParticipantShapeHandler(Bpmn2ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}
	
	@Override
	protected void setLocation(AddContext context, ContainerShape container, BPMNShape shape) {
		context.setLocation((int) shape.getBounds().getX(), (int) shape.getBounds().getY());
		FeatureSupport.setHorizontal(context, shape.isIsHorizontal());
	}
}
