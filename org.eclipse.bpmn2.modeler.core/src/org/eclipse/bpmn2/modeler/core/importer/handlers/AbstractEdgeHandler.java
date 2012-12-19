/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.importer.handlers;

import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.location;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.point;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.rectangle;
import static org.eclipse.bpmn2.modeler.core.layout.util.ConversionUtil.*;

import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.layout.util.LayoutUtil;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dd.dc.Point;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.mm.pictograms.impl.FreeFormConnectionImpl;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

/**
 * Superclass for all connecting elements (sequence flow, association, message
 * flow, conversation link, data association)
 * 
 * @author Nico Rehwaldt
 */
public abstract class AbstractEdgeHandler<T extends BaseElement> extends AbstractDiagramElementHandler<T> {

	private IPeService peService = Graphiti.getPeService();
	private IGaService gaService = Graphiti.getGaService();
	
	public AbstractEdgeHandler(ModelImport modelImport) {
		super(modelImport);
	}
	
	public final PictogramElement handleDiagramElement(T bpmnElement, DiagramElement diagramElement, ContainerShape container) {
		
		if (diagramElement instanceof BPMNEdge) {
			return handleEdge(bpmnElement, (BPMNEdge) diagramElement, container);
		} else {
			throw new IllegalArgumentException("Handling instances of BPMNEdge only");
		}
	}
	
	protected abstract PictogramElement handleEdge(T bpmnElement, BPMNEdge diagramElement, ContainerShape container);

	protected void addSourceAndTargetToEdge(BPMNEdge bpmnEdge, FlowNode source, FlowNode target) {
		
		if (source != null) {
			DiagramElement sourceElement = getDiagramElement(source);
			if (sourceElement == null) {
				throw new RuntimeException("No BPMN DI element for " + source);
			}
			
			bpmnEdge.setSourceElement(sourceElement);
		}
		
		if (target != null) {
			DiagramElement targetElement = getDiagramElement(target);
			if (targetElement == null) {
				throw new RuntimeException("No BPMN DI element for " + target);
			}
			
			bpmnEdge.setTargetElement(targetElement);
		}
	}

	protected Connection createConnectionAndSetBendpoints(BPMNEdge bpmnEdge, PictogramElement sourceElement, PictogramElement targetElement) {
		List<Point> waypoints = bpmnEdge.getWaypoint();
		
		Anchor sourceAnchor = createSourceAnchor(sourceElement, waypoints);
		Anchor targetAnchor = createTargetAnchor(targetElement, waypoints);

		AddConnectionContext context = new AddConnectionContext(sourceAnchor, targetAnchor);
		context.setNewObject(bpmnEdge.getBpmnElement());

		IAddFeature addFeature = featureProvider.getAddFeature(context);
		if (addFeature != null && addFeature.canAdd(context)) {
			
			context.putProperty(DIUtils.IMPORT_PROPERTY, true);
			Connection connection = (Connection) addFeature.add(context);

			if (connection instanceof FreeFormConnectionImpl) {
				FreeFormConnectionImpl freeForm = (FreeFormConnectionImpl) connection;

				int last = waypoints.size() - 1;

				for (int i = 1; i < last; i++) {
					Point waypoint = waypoints.get(i);
					DIUtils.addBendPoint(freeForm, waypoint);
				}
			}
			
			createLink((T) bpmnEdge.getBpmnElement(), bpmnEdge, connection);
			return connection;
		} else {
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Unsupported feature "
					+ ((EObject) context.getNewObject()).eClass().getName()));
		}
		return null;
	}

	private Anchor createTargetAnchor(PictogramElement element, List<Point> waypoints) {
		int last = waypoints.size() - 1;
		
		verifyCorrectInstance(element, AnchorContainer.class);
		
		return createAnchorOnContainer((AnchorContainer) element, waypoints.get(last), waypoints.get(last - 1));
	}

	private Anchor createSourceAnchor(PictogramElement element, List<Point> waypoints) {
		verifyCorrectInstance(element, AnchorContainer.class);
		
		return createAnchorOnContainer((AnchorContainer) element, waypoints.get(0), waypoints.get(1));
	}

	private Anchor createAnchorOnContainer(AnchorContainer container, Point containerDockingPoint, Point referencePoint) {
		
		verifyCorrectInstance(container, Shape.class);
		
		Shape shape = (Shape) container;
		
		// ADONIS COMPATIBILITY (!!)
		
		// check if point centers anchor container; 
		// if so assume the usage of a chopbox anchor
		
		ILocation containerCenter = LayoutUtil.getShapeLocationMidpoint(shape);
		if (positionsMatch(containerCenter, containerDockingPoint)) {
			return LayoutUtil.getCenterAnchor(container);
		}
		
		// end ADONIS COMPATIBILITY (!!)
		
		
		// check if any of the default anchors dock
		Anchor dockingAnchor = getDefaultAnchorAtPoint(containerDockingPoint, container);
		if (dockingAnchor != null) {
			return dockingAnchor;
		}
		
		// check if chopbox anchor matches
		dockingAnchor = getReferencedChopboxAnchor(container, containerDockingPoint, referencePoint);
		if (dockingAnchor != null) {
			return dockingAnchor;
		}
	
		// create new fix point anchor for the element
		FixPointAnchor fixPointAnchor = peService.createFixPointAnchor(container);
		fixPointAnchor.setReferencedGraphicsAlgorithm(shape.getGraphicsAlgorithm());
		Rectangle rect = gaService.createInvisibleRectangle(fixPointAnchor);
		gaService.setSize(rect, 1, 1);
		
		// set anchor location now
		setAnchorLocation(shape, fixPointAnchor, containerDockingPoint);
			
		return fixPointAnchor;
	}

	private Anchor getReferencedChopboxAnchor(AnchorContainer container, Point elementDockingPoint, Point referencePoint) {
		
		Shape shape = (Shape) container;

		IRectangle containerRect = LayoutUtil.getAbsoluteRectangle(shape);
		
		ILocation referencePointLocation = location(referencePoint);
		
		ILocation intersectionPoint = LayoutUtil.getChopboxIntersectionPoint(containerRect, referencePointLocation);
		
		if (positionsMatch(intersectionPoint, elementDockingPoint)) {
			return LayoutUtil.getCenterAnchor(container);
		} else {
			return null;
		}
	}

	private Anchor getDefaultAnchorAtPoint(Point elementDockingPoint, AnchorContainer container) {
		List<Anchor> containerDefaultAnchors = LayoutUtil.getDefaultAnchors(container);
		for (Anchor anchor: containerDefaultAnchors) {
			ILocation location = Graphiti.getLayoutService().getLocationRelativeToDiagram(anchor);
			
			if (positionsMatch(location, elementDockingPoint)) {
				return anchor;
			}
		}
		
		return null;
	}

	private boolean positionsMatch(ILocation location, Point point) {
		return GraphicsUtil.isPointNear(point(point), location, 1);
	}
	
	private void verifyCorrectInstance(Object o, Class<?> cls) {
		if (cls.isInstance(o)) {
			// ok
		} else {
			throw new IllegalArgumentException(String.format("Can handle instances of %s only", cls.getName()));
		}
	}
	
	private FixPointAnchor createAnchor(PictogramElement elem) {
		FixPointAnchor sa;
		
		if (elem instanceof FreeFormConnection) {
			Shape connectionPointShape = AnchorUtil.createConnectionPoint(featureProvider,
					(FreeFormConnection)elem,
					Graphiti.getPeLayoutService().getConnectionMidpoint((FreeFormConnection)elem, 0.5));
			sa = AnchorUtil.getConnectionPointAnchor(connectionPointShape);
		}
		else
		{
			sa = peService.createFixPointAnchor((AnchorContainer) elem);
			sa.setReferencedGraphicsAlgorithm(elem.getGraphicsAlgorithm());
			Rectangle rect = gaService.createInvisibleRectangle(sa);
			gaService.setSize(rect, 1, 1);
		}
		return sa;
	}

	private void setAnchorLocation(PictogramElement elem, FixPointAnchor anchor, Point point) {
		org.eclipse.graphiti.mm.algorithms.styles.Point p = gaService.createPoint((int) point.getX(),
				(int) point.getY());

		ILocation loc;
		if (elem instanceof Connection) {
			loc = peService.getConnectionMidpoint((Connection) elem, 0.5);
		} else {
			loc = peService.getLocationRelativeToDiagram((Shape) elem);
		}
		
		int x = p.getX() - loc.getX();
		int y = p.getY() - loc.getY();

		p.setX(x);
		p.setY(y);

		anchor.setLocation(p);
	}
}
