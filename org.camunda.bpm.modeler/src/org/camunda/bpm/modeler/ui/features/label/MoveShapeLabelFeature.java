package org.camunda.bpm.modeler.ui.features.label;

import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class MoveShapeLabelFeature extends MoveLabelFeature {

	public MoveShapeLabelFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		super.postMoveShape(context);

		Shape labelShape = context.getShape();
		PictogramElement labeledShape = LabelUtil.getNonLabelPictogramElement(labelShape, getDiagram());
		
		// update only if the labeled pictogram element 
		// is not selected and thus moved, too
		if (!isEditorSelection(labeledShape)) {
			LabelUtil.updateStoredShapeLabelOffset(labelShape, getDiagram());
		}
	}
}
