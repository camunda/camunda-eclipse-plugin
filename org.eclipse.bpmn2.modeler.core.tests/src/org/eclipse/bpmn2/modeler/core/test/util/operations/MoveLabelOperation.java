package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * 
 * @author Nico Rehwaldt
 *
 */
public class MoveLabelOperation extends MoveShapeOperation<DefaultMoveShapeFeature, MoveLabelOperation> {

	public MoveLabelOperation(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		super(shape, diagramTypeProvider, MoveLabelOperation.class);
	}
	
	@Override
	protected void createFeature() {
		IFeatureProvider featureProvider = diagramTypeProvider.getFeatureProvider();
		feature = new DefaultMoveShapeFeature(featureProvider);
	}
	
	public MoveLabelOperation move(Shape shape, IDiagramTypeProvider diagramTypeProvider) {
		return new MoveLabelOperation(shape, diagramTypeProvider).toContainer(shape.getContainer());
	}
}
