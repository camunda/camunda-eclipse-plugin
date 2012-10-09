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

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.di.DIImport;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.context.impl.AreaContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 * 
 */
public abstract class AbstractShapeHandler<T extends BaseElement> extends AbstractDiagramElementHandler<T> {
	
	public AbstractShapeHandler(Bpmn2ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}

	public final PictogramElement handleDiagramElement(T bpmnElement, DiagramElement diagramElement, ContainerShape container) {
		if (diagramElement instanceof BPMNShape) {
			return handleShape(bpmnElement, (BPMNShape) diagramElement, container);
		} else {
			throw new IllegalArgumentException("Handling instances of BPMNShape only");
		}
	}
	
	/**
	 * Find a Graphiti feature for given shape and generate necessary diagram elements.
	 * 
	 * @param bpmnElement the element to find and create the diagram element for
	 * @param shape the shape of the diagram element, use in {@link AbstractShapeHandler#createLink(BaseElement, BPMNShape, PictogramElement)}
	 * @param container the container element to add visual element to
	 * 
	 * @return the generated picture
	 */
	public PictogramElement handleShape(T bpmnElement, BPMNShape shape, ContainerShape container) {

		AddContext context = createAddContext(bpmnElement);		
		IAddFeature addFeature = createAddFeature(context);
		
		if (addFeature != null) {
			
			setSize(context, shape, bpmnElement, container);
			
			setLocation(context, container, shape);
			
			addToTargetContainer(context, container);
			
			PictogramElement pictogramElement = createPictogramElement(bpmnElement, shape, context, addFeature);
			
			return pictogramElement;
			
		} else  {
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Element not supported: "
					+ bpmnElement.eClass().getName()));
			
			return null;
			
		}
			
	}

	protected PictogramElement createPictogramElement(T bpmnElement, BPMNShape shape, AddContext context, IAddFeature addFeature) {
		
		if (addFeature.canAdd(context)) {
			PictogramElement newElement = createPictogramElement(context, addFeature);
			createLink(bpmnElement, shape, newElement);
			return newElement;
			
		} else { 
			Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Add feature cannot add context: "+ addFeature));
			return null;
		}
		
	}


	protected void setLocation(AddContext context, ContainerShape container, BPMNShape shape) {
		
		Bounds bounds = shape.getBounds();
		
		int x = (int) bounds.getX();
		int y = (int) bounds.getY();
		
		ILocation loc = Graphiti.getPeLayoutService().getLocationRelativeToDiagram(container);
		x -= loc.getX();
		y -= loc.getY();

		context.setLocation(x, y);
	}

	protected void addToTargetContainer(AddContext context, ContainerShape container) {
		context.setTargetContainer(container);
	}

	protected void setSize(AddContext context, BPMNShape shape, T bpmnElement, ContainerShape targetContainer) {

		Bounds bounds = shape.getBounds();
		
		int width = (int) bounds.getWidth();
		int height = (int) bounds.getHeight();
		
		context.setSize(width, height);		
		
	}

	protected IAddFeature createAddFeature(AddContext context) {
		return featureProvider.getAddFeature(context);
	}

	protected AddContext createAddContext(BaseElement bpmnElement) {
		AddContext context = new AddContext(new AreaContext(), bpmnElement);

		// MUST be set to make the layout mechanisms work
		context.putProperty(DIImport.IMPORT_PROPERTY, true);
		
		return context;
	}
	
	protected PictogramElement createPictogramElement(AddContext context, IAddFeature addFeature) {
		return addFeature.add(context);
	}
}
