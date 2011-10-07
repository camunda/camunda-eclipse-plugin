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
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

public class CommonEventPropertiesComposite extends AbstractBpmn2PropertiesComposite {

	public CommonEventPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public CommonEventPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite
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
			bindAttribute(be,"isCancelActivity");
		}
		
		// Lists
		if (be instanceof Event) {
			bindList(be,"properties");
		}
		if (be instanceof CatchEvent || be instanceof ThrowEvent) {
			bindList(be,"eventDefinitions");
			if (be instanceof ThrowEvent)
				bindList(be,"dataInputs");
			if (be instanceof CatchEvent)
				bindList(be,"dataOutputs");
		}
		
		// References
		if (be instanceof CatchEvent || be instanceof BoundaryEvent) {
			bindReference(be,"attachedToRef");
		}
	}
}