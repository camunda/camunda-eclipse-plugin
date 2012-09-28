package org.eclipse.bpmn2.modeler.core.importer.handlers;

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class LaneShapeHandler extends AbstractShapeHandler<Lane> {

	public LaneShapeHandler(Bpmn2ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}
	
	@Override
	protected void setLocation(AddContext context, ContainerShape container, BPMNShape shape) {
		super.setLocation(context, container, shape);
		
		FeatureSupport.setHorizontal(context, shape.isIsHorizontal());
	}
}
