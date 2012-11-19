package org.eclipse.bpmn2.modeler.core.layout.util;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.draw2d.geometry.Vector;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class LayoutUtil {
	
	/**
	 * evolutionary developed value to switch between the AboveBeneath and LeftRight Strategy for gateways 
	 */
	public static final double MAGIC_VALUE = 0.81;
	
	public enum Sector {
		TOP_LEFT,
		LEFT,
		BOTTOM_LEFT,
		BOTTOM,
		BOTTOM_RIGHT,
		RIGHT,
		TOP_RIGHT,
		TOP,
		UNDEFINED;
		
	}
	
	private static Sector fromBooleans(boolean left, boolean right, boolean above, boolean beneath) {
		if (left && above) {
			return Sector.TOP_LEFT;
		} else if (left && beneath) {
			return Sector.BOTTOM_LEFT;
		} else if (left) {
			return Sector.LEFT;
		} else if (right && above) {
			return Sector.TOP_RIGHT;
		} else if (right && beneath) {
			return Sector.BOTTOM_RIGHT;
		} else if (right) {
			return Sector.RIGHT;
		} else if(above) {
			return Sector.TOP;
		} else if(beneath) {
			return Sector.BOTTOM;
		} else {
			return Sector.UNDEFINED;
		}
	}
	
	public static Sector getEndShapeSector(FreeFormConnection connection) {
		Shape startShape = (Shape) connection.getStart().getParent();
		Shape endShape = (Shape) connection.getEnd().getParent();
		
		boolean left = LayoutUtil.isLeftToStartShape(startShape, endShape); 
		boolean right = LayoutUtil.isRightToStartShape(startShape, endShape);
		boolean above = LayoutUtil.isAboveStartShape(startShape, endShape);
		boolean beneath = LayoutUtil.isBeneathStartShape(startShape, endShape);
		
		return fromBooleans(left, right, above, beneath);
	}
	
	public static BaseElement getSourceBaseElement(FreeFormConnection connection) {
		return (BaseElement) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(connection.getStart().getParent());	
	}
	
	
	public static boolean anchorEqual(Anchor some, Anchor that) {
		
		// this case happens, when you reconnect the connection manually with a shape
		// so in that case it is not equal to the assigned object 'that'
		if (some instanceof ChopboxAnchor) {
			return false;
		}

		if (!(some instanceof FixPointAnchor)) {
			throw new IllegalArgumentException("Can only compare "+FixPointAnchor.class.getName());
		}
		
		FixPointAnchor someFixAnchor = (FixPointAnchor) some;
		FixPointAnchor thatFixAnchor = (FixPointAnchor) that;
		
		return someFixAnchor.getLocation().getX() == thatFixAnchor.getLocation().getX() && someFixAnchor.getLocation().getY() == thatFixAnchor.getLocation().getY();
	}
	
	public static Anchor getLeftAnchor(Shape shape) {
		return shape.getAnchors().get(4);
	}
	
	public static Anchor getRightAnchor(Shape shape) {
		return shape.getAnchors().get(2);
	}
	
	public static Anchor getTopAnchor(Shape shape) {
		return shape.getAnchors().get(1);
	}
	
	public static Anchor getBottomAnchor(Shape shape) {
		return shape.getAnchors().get(3);
	}
	
	
	public static boolean isAboveStartShape (Shape startShape, Shape endShape) {
		ILocation startShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(startShape);
		ILocation endShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(endShape);
		
		Point endShapeBoundary = Graphiti.getGaService().createPoint(endShapeLocation.getX(), endShapeLocation.getY() + endShape.getGraphicsAlgorithm().getHeight());
		Point startShapeLocationPoint = Graphiti.getGaService().createPoint(startShapeLocation.getX() ,startShapeLocation.getY());
		
		return getHorizontalLayoutTreshold(startShapeLocationPoint, endShapeBoundary) > 0 ? false : true;
	}
	
	
	public static boolean isBeneathStartShape (Shape startShape, Shape endShape) {
		ILocation startShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(startShape);
		ILocation endShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(endShape);
		
		Point startShapeBoundary = Graphiti.getGaService().createPoint(startShapeLocation.getX(), startShapeLocation.getY() + startShape.getGraphicsAlgorithm().getHeight());
		Point endShapeLocationPoint = Graphiti.getGaService().createPoint(endShapeLocation.getX() ,endShapeLocation.getY());
		
		return getHorizontalLayoutTreshold(startShapeBoundary, endShapeLocationPoint) >= 0 ? true : false;
	}
	
	public static boolean isLeftToStartShape (Shape startShape, Shape endShape) {
		ILocation startShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(startShape);
		ILocation endShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(endShape);
		
		Point endShapeBoundary = Graphiti.getGaService().createPoint(endShapeLocation.getX() + endShape.getGraphicsAlgorithm().getWidth(), endShapeLocation.getY());
		Point startShapeLocationPoint = Graphiti.getGaService().createPoint(startShapeLocation.getX() ,startShapeLocation.getY());
		
		return getVerticalLayoutTreshold(startShapeLocationPoint, endShapeBoundary) > 0 ? false : true;
	}
	
	
	public static boolean isRightToStartShape (Shape startShape, Shape endShape) {
		ILocation startShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(startShape);
		ILocation endShapeLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(endShape);
		
		Point startShapeBoundary = Graphiti.getGaService().createPoint(startShapeLocation.getX() + startShape.getGraphicsAlgorithm().getWidth(), startShapeLocation.getY());
		Point endShapeLocationPoint = Graphiti.getGaService().createPoint(endShapeLocation.getX() ,endShapeLocation.getY());
		
		return getVerticalLayoutTreshold(startShapeBoundary, endShapeLocationPoint) >= 0 ? true : false;
	}

	/**
	 * 
	 * @param connection
	 * @return negative value if end shape is left to start shape and
	 * positive if end shape is right to start shape.
	 */
	public static double getLayoutTreshold(FreeFormConnection connection) {
		Point startShapeCenter = getCenter((Shape) connection.getStart().getParent());
		Point endShapeCenter = getCenter((Shape) connection.getEnd().getParent());
		return getVerticalLayoutTreshold(startShapeCenter, endShapeCenter);
	}
	
	public static double getLayoutTreshold(Shape startShape, Shape endShape) {
		Point startShapeCenter = getCenter(startShape);
		Point endShapeCenter = getCenter(endShape);
		return getVerticalLayoutTreshold(startShapeCenter, endShapeCenter);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @return 1.0 if the points are on the same vertical line, 0.0 if the points are on the same horizontal line,
	 * sign is negative if end point is above the start point 
	 */
	public static double getHorizontalLayoutTreshold(Point start, Point end) {
		Vector shapeVector = new Vector(end.getX() - start.getX(), end.getY() - start.getY());
		Vector unitYVector = new Vector (0, 1);
		
		double product = Math.floor(shapeVector.getDivided(shapeVector.getLength()).getDotProduct(unitYVector) * 1000) / 1000;
		return product;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return 1.0 if the points are on the same horizonal line, 0.0 if the points are on the same vertical line,
	 * sign is negative if end point is left to start point 
	 */
	public static double getVerticalLayoutTreshold(Point start, Point end) {
		Vector shapeVector = new Vector(end.getX() - start.getX(), end.getY() - start.getY());
		Vector unitXVector = new Vector (1,0);
		
		double product = Math.floor(shapeVector.getDivided(shapeVector.getLength()).getDotProduct(unitXVector) * 1000) / 1000;
		return product;
	}
	
	public static Point getCenter(Shape shape) {
		
		if (shape == null) {
			throw new NullPointerException("Argument is null");
		}
		
		ILocation shapeLocation = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(shape);
		GraphicsAlgorithm shapeGa = shape.getGraphicsAlgorithm();
		Point shapeCenter = Graphiti.getGaService().createPoint(shapeLocation.getX() + shapeGa.getWidth() / 2, shapeLocation.getY() + shapeGa.getHeight() / 2 );
		
		return shapeCenter;
	}
	
	public static void addVerticalCenteredBendpoints(FreeFormConnection connection) {
		ILocation startAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getStart());
		ILocation endAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getEnd());
		
		int midX = ((endAnchorLocation.getX() - startAnchorLocation.getX()) / 2) + startAnchorLocation.getX();
		
		Point firstPoint = Graphiti.getCreateService().createPoint(midX, startAnchorLocation.getY());
		Point secondPoint = Graphiti.getCreateService().createPoint(midX, endAnchorLocation.getY());
		
		connection.getBendpoints().add(firstPoint);
		connection.getBendpoints().add(secondPoint);
	}
	
	public static void addHorizontalCenteredBendpoints(FreeFormConnection connection) {
		ILocation startAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getStart());
		ILocation endAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getEnd());
		
		int midY = ((endAnchorLocation.getY() - startAnchorLocation.getY()) / 2) + startAnchorLocation.getY();
		
		Point firstPoint = Graphiti.getCreateService().createPoint(startAnchorLocation.getX(), midY);
		Point secondPoint = Graphiti.getCreateService().createPoint(endAnchorLocation.getX(), midY);
		
		connection.getBendpoints().add(firstPoint);
		connection.getBendpoints().add(secondPoint);
	}
	
	public static void addRectangularBendpoint(FreeFormConnection connection) {
		ILocation startAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getStart());
		ILocation endAnchorLocation = Graphiti.getLayoutService().getLocationRelativeToDiagram(connection.getEnd());

		Point point = Graphiti.getCreateService().createPoint(startAnchorLocation.getX(), endAnchorLocation.getY());
		
		connection.getBendpoints().add(point);
	}
	
}
