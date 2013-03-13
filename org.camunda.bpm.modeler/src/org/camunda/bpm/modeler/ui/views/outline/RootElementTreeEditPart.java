package org.camunda.bpm.modeler.ui.views.outline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.RootElement;

public class RootElementTreeEditPart extends AbstractGraphicsTreeEditPart {
	
	public RootElementTreeEditPart(DiagramTreeEditPart dep, RootElement graphicsAlgorithm) {
		super(dep, graphicsAlgorithm);
	}

	public RootElement getRootElement() {
		return (RootElement) getBpmnModel();
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
		RootElement elem = getRootElement();
		if (elem != null && elem.eResource() != null) {
			if (elem instanceof FlowElementsContainer) {
				FlowElementsContainer container = (FlowElementsContainer)elem;
				retList.addAll(container.getFlowElements());
			}
			if (elem instanceof Collaboration) {
				Collaboration collaboration = (Collaboration)elem;
				retList.addAll(collaboration.getParticipants());
				retList.addAll(collaboration.getConversations());
			}
		}
		return retList;
	}
}