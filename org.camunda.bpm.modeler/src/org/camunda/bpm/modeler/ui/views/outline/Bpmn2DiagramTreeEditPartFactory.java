package org.camunda.bpm.modeler.ui.views.outline;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.graphiti.mm.pictograms.Diagram;

/**
 * A concrete implementation of the interface IEditPartFactory for Trees, which
 * works on a pictogram model.
 */
public class Bpmn2DiagramTreeEditPartFactory implements EditPartFactory {

	private int id;
	
	/**
	 * Creates a new PictogramsEditPartFactory.
	 */
	public Bpmn2DiagramTreeEditPartFactory(int id) {
		super();
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 * java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart ret = null;
		if (model instanceof Diagram) {
			ret = new DiagramTreeEditPart(id, (Diagram) model);
		} else if (context instanceof AbstractGraphicsTreeEditPart ){
			EditPart root = context;
			while (root.getParent()!=null)
				root = root.getParent();
			DiagramTreeEditPart dep = (DiagramTreeEditPart)root.getChildren().get(0);
			
			if (model instanceof RootElement) {
				ret = new RootElementTreeEditPart(dep, (RootElement) model);
			} else if (model instanceof FlowElement) {
				ret = new FlowElementTreeEditPart(dep, (FlowElement) model);
			} else if (model instanceof BaseElement) {
				ret = new BaseElementTreeEditPart(dep, (BaseElement) model);
			} else if (model instanceof BPMNDiagram) {
				ret = new Bpmn2DiagramTreeEditPart(dep, (BPMNDiagram) model);
			} else if (model instanceof BPMNShape) {
				ret = new Bpmn2ShapeTreeEditPart(dep, (BPMNShape) model);
			} else if (model instanceof BPMNEdge) {
				ret = new Bpmn2EdgeTreeEditPart(dep, (BPMNEdge) model);
			}
		}
		return ret;
	}
}