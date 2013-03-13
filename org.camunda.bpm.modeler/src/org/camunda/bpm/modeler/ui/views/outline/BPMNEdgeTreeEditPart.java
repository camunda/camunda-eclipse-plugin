package org.camunda.bpm.modeler.ui.views.outline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.di.BPMNEdge;

public class BPMNEdgeTreeEditPart extends AbstractGraphicsTreeEditPart {

	public BPMNEdgeTreeEditPart(DiagramTreeEditPart dep, BPMNEdge bpmnEdge) {
		super(dep, bpmnEdge);
	}

	public BPMNEdge getBPMNEdge() {
		return (BPMNEdge) getBpmnModel();
	}

	// ======================= overwriteable behaviour ========================

	/**
	 * Creates the EditPolicies of this EditPart. Subclasses often overwrite
	 * this method to change the behaviour of the editpart.
	 */
	@Override
	protected void createEditPolicies() {
	}

	@Override
	protected List<Object> getModelChildren() {
		List<Object> retList = new ArrayList<Object>();
		BPMNEdge bpmnEdge = getBPMNEdge();
		// TODO
		return retList;
	}
	
	@Override
	protected String getText() {
		BPMNEdge bpmnEdge = getBPMNEdge();
		return super.getText(bpmnEdge.getBpmnElement());
	}
}