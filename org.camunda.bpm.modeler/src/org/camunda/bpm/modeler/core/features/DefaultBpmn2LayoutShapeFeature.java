package org.camunda.bpm.modeler.core.features;

import static org.camunda.bpm.modeler.core.utils.ContextUtil.copyProperties;
import static org.camunda.bpm.modeler.core.utils.ContextUtil.is;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.layout.util.Layouter;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Group;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.impl.LayoutContext;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

/**
 * Default layouting implementation for all shapes (non-connections).
 * 
 * @author nico.rehwaldt
 * @author smirnov
 */
public class DefaultBpmn2LayoutShapeFeature extends AbstractBpmn2LayoutElementFeature<ContainerShape> {

	public DefaultBpmn2LayoutShapeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		return true;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		
		super.layout(context);
		
		ContainerShape shape = (ContainerShape) context.getPictogramElement();
		
		layoutShape(shape);
		
		// layout children
		if (isBpmnContainer(shape)) {
			layoutChildren(shape, context);
		}
		
		postLayoutShapeAndChildren(shape, context);
		
		if (isRepairConnections(context)) {
			repairConnections(context);
			// repairing connections is a good trigger
			// to bring all Group shapes back to front 
			repairGroupZOrder(context);
		}
		
		return true;
	}

	/**
	 * Execute a post layout operation
	 * 
	 * @param shape
	 * @param context
	 */
	protected void postLayoutShapeAndChildren(ContainerShape shape, final ILayoutContext context) {
		
	}

	/**
	 * Layout the shape itself (i.e. icons and labels placed on it)
	 * 
	 * May be overridden by subclasses to perform actual work.
	 * 
	 * @param shape
	 */
	protected void layoutShape(ContainerShape shape) {
		
	}

	/**
	 * Return true if we want reconnect 
	 * 
	 * @param context
	 * @return
	 */
	protected boolean isRepairConnections(ILayoutContext context) {
		return is(context, PropertyNames.LAYOUT_REPAIR_CONNECTIONS);
	}

	/**
	 * Return true if the shape represents a bpmn container
	 * 
	 * @param pictogramElement
	 * @return
	 */
	protected boolean isBpmnContainer(PictogramElement pictogramElement) {
		BaseElement baseElement = getLinkedBaseElement(pictogramElement);
		
		return 
				baseElement instanceof Participant ||
				baseElement instanceof Lane ||
				baseElement instanceof SubProcess;
	}
	
	/**
	 * Prepare a layout context for layouting the given element
	 * 
	 * @param context
	 * @param shapeToLayout
	 * 
	 * @return
	 */
	protected ILayoutContext prepareLayoutContext(ILayoutContext context, Shape shapeToLayout) {
		LayoutContext subLayoutContext = new LayoutContext(shapeToLayout);
		
		copyProperties(context, subLayoutContext, PropertyNames.LAYOUT_PROPERTIES);
		
		return subLayoutContext;
	}
	
	/**
	 * Layout direct children of the shape
	 *
	 * @param shape
	 * @param context
	 */
	protected void layoutChildren(ContainerShape shape, ILayoutContext context) {
		
		// copy children to prevent concurrent modification
		// in case containments or positioning changes
		// during layout
		List<Shape> children = new ArrayList<Shape>(shape.getChildren());
		for (Shape child: children) {
			
			BPMNShape bpmnShape = getLinkedBPMNShape(child);
			BaseElement baseElement = getLinkedBaseElement(child);
			
			if (bpmnShape != null && !(baseElement instanceof BoundaryEvent)) {
				layoutChild(context, child);
			}
		}
	}

	/**
	 * Layout the child element
	 * 
	 * @param context
	 * @param child
	 */
	protected void layoutChild(ILayoutContext context, Shape child) {
		ILayoutContext childLayoutContext = prepareLayoutContext(context, child);
		
		getFeatureProvider().layoutIfPossible(childLayoutContext);
	}
	
	/**
	 * 
	 * @param context
	 */
	protected void repairConnections(ILayoutContext context) {
		ContainerShape shape = getLayoutedElement(context);
		
		Set<Connection> connections = LayoutUtil.getConnections(shape);
		
		Set<Connection> sharedConnections = LayoutUtil.getSharedConnections(shape, getEditorSelection());
		
		// we do not repair shared connections between multiple
		// selected elements in order to not screw them up
		// depending on the order in which layout features on
		// the shapes are executed
		connections.removeAll(sharedConnections);
		
		for (Connection connection : connections) {
			layoutConnection(connection, context);
		}		
	}
	
	/**
	 * Repairs the Z-order of all Group shapes and brings them back to front.
	 * 
	 * @param context the layout context
	 */
	protected void repairGroupZOrder(ILayoutContext context) {
		IPeService peService = Graphiti.getPeService();
		List<Shape> diagramChildren = // beware of concurrent modification 
				new ArrayList<Shape>(getDiagram().getChildren());
		for (Shape s: diagramChildren) {
			if (s instanceof ContainerShape && 
					BusinessObjectUtil.getFirstBaseElement(s) instanceof Group) {
				peService.sendToFront(s); 
			}
		}
	}

	/**
	 * Layout connection during layouting of the shape
	 * @param context 
	 * @param connection
	 */
	protected void layoutConnection(Connection connection, ILayoutContext context) {
		Layouter.layoutConnectionAfterShapeMove(connection, getFeatureProvider());
	}
}
