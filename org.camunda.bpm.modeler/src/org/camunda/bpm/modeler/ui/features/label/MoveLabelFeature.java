package org.camunda.bpm.modeler.ui.features.label;

import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.modeler.core.di.DIUtils;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class MoveLabelFeature extends DefaultMoveShapeFeature {
	
	public MoveLabelFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		return true;
	}

	@Override
	protected void internalMove(IMoveShapeContext context) {
		// perform move but do not change container
		
		if (!getUserDecision()) {
			return;
		}
		
		Shape shapeToMove = context.getShape();

		ContainerShape targetContainer = context.getTargetContainer();
		IRectangle bounds = LayoutUtil.getAbsoluteBounds(targetContainer);
		
		// context x and y are relative to new target container
		// we ignore that and have to add the containers position 
		// to get the new diagram local bounds of the label
		int x = bounds.getX() + context.getX();
		int y = bounds.getY() + context.getY();

		// always move within the original container
		if (shapeToMove.getGraphicsAlgorithm() != null) {
			Graphiti.getGaService().setLocation(shapeToMove.getGraphicsAlgorithm(), x, y,
					avoidNegativeCoordinates());
			
			GraphicsUtil.sendToFront(shapeToMove);
		}
	}
	
	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		super.postMoveShape(context);
		
		Shape labelShape = context.getShape();
		
		updateDi(labelShape);
	}

	protected void updateDi(Shape labelShape) {
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(labelShape, BPMNShape.class);
		DIUtils.updateDILabel((ContainerShape) labelShape, bpmnShape);
	}
	
	/**
	 * Return true if the given pictogram element is currently selected in the editor.
	 * 
	 * @param pictogramElement
	 * @return
	 */
	protected boolean isEditorSelection(PictogramElement pictogramElement) {
		List<PictogramElement> selection = Arrays.asList(getDiagramBehavior().getDiagramContainer().getSelectedPictogramElements());
		
		return selection.contains(pictogramElement);
	}
}