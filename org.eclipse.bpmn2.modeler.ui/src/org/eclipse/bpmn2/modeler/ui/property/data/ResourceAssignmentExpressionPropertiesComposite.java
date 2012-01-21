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

package org.eclipse.bpmn2.modeler.ui.property.data;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ResourceAssignmentExpression;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class ResourceAssignmentExpressionPropertiesComposite extends ExpressionPropertiesComposite {

	public ResourceAssignmentExpressionPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public ResourceAssignmentExpressionPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void setEObject(BPMN2Editor bpmn2Editor, EObject object) {
		object = ((ResourceAssignmentExpression)object).getExpression();
		if (object==null)
			object = getDiagramEditor().getModelHandler().create(PACKAGE.getExpression());
		super.setEObject(bpmn2Editor, object);
	}

}
