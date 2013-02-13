package org.eclipse.bpmn2.modeler.ui.features.label;

import org.eclipse.bpmn2.modeler.core.utils.LabelUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class MoveShapeLabelFeature extends MoveLabelFeature {

	public MoveShapeLabelFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		super.postMoveShape(context);

		Shape labelShape = context.getShape();
		
		LabelUtil.updateStoredShapeLabelOffset(labelShape, getDiagram());
	}
}
