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

import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.di.DIImport;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dd.dc.Point;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
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
	
	public AbstractEdgeHandler(Bpmn2ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}
	
	public final PictogramElement handleDiagramElement(T bpmnElement,
			DiagramElement diagramElement, ContainerShape container) {
		
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
		FixPointAnchor sourceAnchor = createAnchor(sourceElement);
		FixPointAnchor targetAnchor = createAnchor(targetElement);

		AddConnectionContext context = new AddConnectionContext(sourceAnchor, targetAnchor);
		context.setNewObject(bpmnEdge.getBpmnElement());

		IAddFeature addFeature = featureProvider.getAddFeature(context);
		if (addFeature != null && addFeature.canAdd(context)) {
			context.putProperty(DIImport.IMPORT_PROPERTY, true);
			Connection connection = (Connection) addFeature.add(context);

			if (connection instanceof FreeFormConnectionImpl) {
				FreeFormConnectionImpl freeForm = (FreeFormConnectionImpl) connection;

				List<Point> waypoint = bpmnEdge.getWaypoint();
				int size = waypoint.size() - 1;

				setAnchorLocation(sourceElement, sourceAnchor, waypoint.get(0));
				setAnchorLocation(targetElement, targetAnchor, waypoint.get(size));

				for (int i = 1; i < size; i++) {
					DIUtils.addBendPoint(freeForm, waypoint.get(i));
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
