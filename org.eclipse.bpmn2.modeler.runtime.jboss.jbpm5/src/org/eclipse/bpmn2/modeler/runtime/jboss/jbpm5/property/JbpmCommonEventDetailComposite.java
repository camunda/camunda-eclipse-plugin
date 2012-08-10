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

package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.property;

import java.util.List;

import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.events.CommonEventDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.events.EventDefinitionsListComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class JbpmCommonEventDetailComposite extends CommonEventDetailComposite {

	/**
	 * @param parent
	 * @param style
	 */
	public JbpmCommonEventDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public JbpmCommonEventDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public void cleanBindings() {
		super.cleanBindings();
		eventsTable = null;
	}

	@Override
	public void createBindings(EObject be) {
		super.createBindings(be);
		eventsTable = new EventDefinitionsListComposite(this, (Event)be) {

			@Override
			protected EObject addListItem(EObject object, EStructuralFeature feature) {
				List<EventDefinition> eventDefinitions = null;
				if (event instanceof ThrowEvent)
					eventDefinitions = ((ThrowEvent)event).getEventDefinitions();
				else if  (event instanceof CatchEvent)
					eventDefinitions = ((CatchEvent)event).getEventDefinitions();
					
				if (eventDefinitions.size()>0) {
					MessageDialog.openError(getShell(), "Not Supported",
						"Can not add more than one Event Definition"
					);
					return null;
				}
				return super.addListItem(object, feature);
			}
		};
		eventsTable.bindList(be, getFeature(be, "eventDefinitions"));
		eventsTable.setTitle("Event Definitions");
	}
}
