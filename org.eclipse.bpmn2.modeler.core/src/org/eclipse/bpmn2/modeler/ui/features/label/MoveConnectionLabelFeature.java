package org.eclipse.bpmn2.modeler.ui.features.label;

import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ConnectionLabelUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.LabelUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Special move feature for connection labels
 * 
 * @author nico.rehwaldt
 */
public class MoveConnectionLabelFeature extends MoveLabelFeature {

	public MoveConnectionLabelFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		super.postMoveShape(context);
		
		Shape labelShape = context.getShape();
		Connection labeledConnection = (Connection) LabelUtil.getNonLabelPictogramElement(labelShape, getDiagram());
		
		ConnectionLabelUtil.updateLabelRefAfterMove(labeledConnection);
	}
	
	@Override
	protected void updateDi(Shape labelShape) {
		BPMNEdge bpmnEdge = BusinessObjectUtil.getFirstElementOfType(labelShape, BPMNEdge.class);
		DIUtils.updateDILabel((ContainerShape) labelShape, bpmnEdge);
	}
}
