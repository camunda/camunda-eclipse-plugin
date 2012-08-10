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
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.property.data;

import org.eclipse.bpmn2.ConditionalEventDefinition;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

public class ConditionalEventDefinitionDetailComposite extends ExpressionDetailComposite {

	public ConditionalEventDefinitionDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public ConditionalEventDefinitionDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public void setBusinessObject(EObject object) {
		if (object instanceof ConditionalEventDefinition) {
			ConditionalEventDefinition ced = (ConditionalEventDefinition)object;
			object = ced.getCondition();
		}
		super.setBusinessObject(object);
	}
	
}
