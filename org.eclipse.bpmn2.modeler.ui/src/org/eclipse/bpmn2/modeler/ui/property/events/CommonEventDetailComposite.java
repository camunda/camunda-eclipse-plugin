/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/


package org.eclipse.bpmn2.modeler.ui.property.events;


import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.modeler.ui.property.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

public class CommonEventDetailComposite extends AbstractDetailComposite {

	protected AbstractListComposite inputTable;
	protected AbstractListComposite outputTable;
	protected EventDefinitionsListComposite eventsTable;

	public CommonEventDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public CommonEventDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2DetailComposite
	 * #createBindings(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void createBindings(EObject be) {
		
		// Attributes
		if (be instanceof StartEvent) {
			bindAttribute(be,"isInterrupting");
		}
		if (be instanceof CatchEvent) {
			bindAttribute(be,"parallelMultiple");
		}
		if (be instanceof BoundaryEvent) {
			bindAttribute(be,"cancelActivity");
		}
		
		// Lists
		if (be instanceof Event) {
			bindList(be,"properties");
		}
		if (be instanceof CatchEvent || be instanceof ThrowEvent) {
			eventsTable = new EventDefinitionsListComposite(this, (Event)be);
			eventsTable.bindList(be, getFeature(be, "eventDefinitions"));
			eventsTable.setTitle("Event Definitions");

			if (be instanceof ThrowEvent) {
				ThrowEvent throwEvent = (ThrowEvent)be;
				inputTable = new DataInputsListComposite(this, throwEvent);
				inputTable.bindList(be, getFeature(throwEvent, "dataInputs"));
				inputTable.setTitle("Input Parameters");
			}
			else if (be instanceof CatchEvent) {
				CatchEvent catchEvent = (CatchEvent)be;
				outputTable = new DataOutputsListComposite(this, catchEvent);
				outputTable.bindList(catchEvent, getFeature(catchEvent, "dataOutputs"));
				outputTable.setTitle("Output Parameters");
			}
		}
	}
}