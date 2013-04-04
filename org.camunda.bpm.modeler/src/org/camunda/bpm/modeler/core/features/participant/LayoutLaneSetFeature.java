package org.camunda.bpm.modeler.core.features.participant;

import org.camunda.bpm.modeler.core.features.LayoutBpmnShapeFeature;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

/**
 * Layout feature for pools and lanes
 * 
 * @author nico.rehwaldt
 */
public class LayoutLaneSetFeature extends LayoutBpmnShapeFeature {

	public LayoutLaneSetFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void layoutShape(ContainerShape containerShape) {
		super.layoutShape(containerShape);
		
		updateLabel(containerShape);
		updateMarkers(containerShape);
	}

	/**
	 * Update markers during layouting
	 * 
	 * @param containerShape
	 */
	protected void updateMarkers(ContainerShape containerShape) {
		
	}

	/**
	 * Update label during layouting
	 * 
	 * @param containerShape
	 */
	protected void updateLabel(ContainerShape containerShape) {
		
		GraphicsAlgorithm containerGa = containerShape.getGraphicsAlgorithm();
		
		int containerHeight = containerGa.getHeight();
		
		for (Shape shape : containerShape.getChildren()) {
			GraphicsAlgorithm shapeGraphics = shape.getGraphicsAlgorithm();
			
			if (shapeGraphics instanceof Polyline) {
				Polyline line = (Polyline) shapeGraphics;
				Point p0 = line.getPoints().get(0);
				Point p1 = line.getPoints().get(1);
				
				p0.setX(GraphicsUtil.PARTICIPANT_LABEL_OFFSET); 
				p0.setY(0);
				
				p1.setX(GraphicsUtil.PARTICIPANT_LABEL_OFFSET);
				p1.setY(containerHeight);
			} else
			if (shapeGraphics instanceof Text) {
				Text text = (Text) shapeGraphics;
				text.setAngle(-90);
				
				Graphiti.getGaService().setLocationAndSize(shapeGraphics, 5, 0, 15, containerHeight);
			}
		}
	}
}