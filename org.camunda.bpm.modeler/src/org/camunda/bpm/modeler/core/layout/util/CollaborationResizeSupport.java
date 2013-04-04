package org.camunda.bpm.modeler.core.layout.util;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.camunda.bpm.modeler.core.layout.util.RectangleUtil.translate;

import java.util.List;

import org.camunda.bpm.modeler.core.features.DefaultResizeBPMNShapeFeature;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.BBox;
import org.camunda.bpm.modeler.core.layout.util.LayoutUtil.Sector;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Resize support for collaborations.
 * 
 * @author nico.rehwaldt
 */
public class CollaborationResizeSupport {
	
	/**
	 * Return the bounding box for the given container shape
	 * 
	 * @param containerShape
	 * @param context
	 * @return
	 */
	public static IRectangle getRelativeResizeBBox(ContainerShape containerShape, List<Shape> laneShapes, Sector sector) {

		// construct a bounding box by the contents of visible lanes
		// and taking a minimal size of each lane into account
		
		BBox resizeBBox = new BBox(null, DefaultResizeBPMNShapeFeature.PADDING, DefaultResizeBPMNShapeFeature.PADDING);
		
		if (laneShapes.isEmpty()) {
			
			// ensure minimum size for the pool
			// as we have on lane shapes in it
			addLaneBounds(resizeBBox, containerShape, containerShape, sector);
		} else {
			
			// add all lane shapes
			// to ensure the minimum x bounds are met
			if (sector.isLeft() || sector.isRight()) {
				
				for (Shape laneShape: laneShapes) {
					addLaneBounds(resizeBBox, laneShape, containerShape, sector);
				}
			}
			
			// add top / bottom lane shape or pool bounding box
			if (sector.isTop()) {
				addLaneBounds(resizeBBox, laneShapes.get(0), containerShape, sector);
			}
			
			if (sector.isBottom()) {
				addLaneBounds(resizeBBox, laneShapes.get(laneShapes.size() - 1), containerShape, sector);
			}
		}
		
		return resizeBBox.getBounds();
	}

	/**
	 * Add the bounds of a lane inside a participant or lane container to the given resize box.
	 * 
	 * @param resizeBBox
	 * @param child
	 * @param container
	 * 
	 * @param sector
	 */
	private static void addLaneBounds(BBox resizeBBox, Shape child, Shape container, Sector sector) {

		IRectangle minimumChildBounds = getMinimumLaneBounds(container, sector);
		IRectangle childBounds = getContainerRelativeChildrenBBox(child, container, minimumChildBounds);
		
		if (childBounds != null) {
			resizeBBox.addBounds(childBounds);
		}
	}

	/**
	 * Return the minimum bounds of a lane shape for the given resize direction.
	 * 
	 * @param laneShape
	 * @param resizeDirection
	 * 
	 * @return
	 */
	public static IRectangle getMinimumLaneBounds(Shape laneShape, Sector resizeDirection) {
		GraphicsAlgorithm graphicsAlgorithm = laneShape.getGraphicsAlgorithm();
		
		int x = 0;
		int y = 0;
		int width = 300;
		int height = 50;
		
		if (resizeDirection.isTop()) {
			y = graphicsAlgorithm.getHeight() - height;
		}
		
		if (resizeDirection.isBottom()) {
			y = height;
		}
		
		if (resizeDirection.isLeft()) {
			x = graphicsAlgorithm.getWidth() - width;
		}
		
		if (resizeDirection.isRight()) {
			x = width;
		}
		
		return rectangle(x, y, 1, 1);
	}

	/**
	 * Return the container (e.g. lane or participant) relative position bounding box of a child lanes children.
	 * 
	 * Will take minimum bounds if specified.
	 * 
	 * @param container
	 * @param participantShape
	 * @param minimumBounds
	 * 
	 * @return
	 */
	public static IRectangle getContainerRelativeChildrenBBox(Shape container, Shape participantShape, IRectangle minimumBounds) {
		IRectangle bbox = LayoutUtil.getChildrenBBox((ContainerShape) container, minimumBounds, 0, 0);
		
		int labelOffset = GraphicsUtil.PARTICIPANT_LABEL_OFFSET;
		
		// space for participant / lane labels
		int offsetX = labelOffset;
		
		// vertical offset of lane
		int offsetY = 0;
		
		while (!container.equals(participantShape)) {
			offsetY += container.getGraphicsAlgorithm().getY();
			offsetX += labelOffset;
			container = container.getContainer();	
		}
		
		// transforming the bbox into participant relative coordinates
		return translate(bbox, rectangle(-1 * labelOffset, offsetY, offsetX, 0));
	}
}
