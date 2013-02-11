package org.eclipse.bpmn2.modeler.ui.features.label;

import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ConnectionLabelUtil;
import org.eclipse.bpmn2.modeler.ui.features.context.IRepositionContext;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class RepositionConnectionLabelFeature extends AbstractRepositionFeature {

	public RepositionConnectionLabelFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected Point getAdjustedPosition(Shape labelShape, IRepositionContext context) {
		Connection labeledConnection = (Connection) context.getReferenceElement();
		
		Point adjustedLabelPosition = ConnectionLabelUtil.getAdjustedLabelPosition(labelShape, labeledConnection);
		
		// if adjustment is null then no adjustment can be made
		if (adjustedLabelPosition == null) {
			// in that case reset the label position to the default position
			return ConnectionLabelUtil.getDefaultLabelPosition(labeledConnection);
		} else {
			return adjustedLabelPosition;
		}
	}

	@Override
	protected void postAdjustPosition(Shape labelShape, IRepositionContext context) {
		Connection labeledConnection = (Connection) context.getReferenceElement();
		ConnectionLabelUtil.updateLabelRefAfterMove(labeledConnection);
	}
	
	@Override
	protected void updateDi(Shape labelShape) {
		BPMNEdge bpmnShape = BusinessObjectUtil.getFirstElementOfType(labelShape, BPMNEdge.class);
		DIUtils.updateDILabel((ContainerShape) labelShape, bpmnShape);
	}
}
