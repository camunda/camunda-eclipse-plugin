package org.eclipse.bpmn2.modeler.ui.views.outline;

import org.eclipse.bpmn2.BaseElement;

public class BaseElementTreeEditPart extends AbstractGraphicsTreeEditPart {

	public BaseElementTreeEditPart(DiagramTreeEditPart dep, BaseElement baseElement) {
		super(dep, baseElement);
	}

	public BaseElement getBaseElement() {
		return (BaseElement) getBpmnModel();
	}

	// ======================= overwriteable behaviour ========================

	/**
	 * Creates the EditPolicies of this EditPart. Subclasses often overwrite
	 * this method to change the behaviour of the editpart.
	 */
	@Override
	protected void createEditPolicies() {
	}
}