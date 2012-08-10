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


package org.eclipse.bpmn2.modeler.ui.property.tasks;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

public class ChoreographyTaskDetailComposite extends DefaultDetailComposite {

	private AbstractPropertiesProvider itemProvider;

	/**
	 * @param section
	 */
	public ChoreographyTaskDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}
	
	public ChoreographyTaskDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (itemProvider == null) {
			itemProvider = new AbstractPropertiesProvider(object) {
				String[] properties = new String[] {
						"loopType",
						"initiatingParticipantRef",
						"participantRefs",
						"correlationKeys",
						"messageFlowRef"
				};
				
				@Override
				public String[] getProperties() {
					return properties; 
				}
			};
		}
		return itemProvider;
	}
}