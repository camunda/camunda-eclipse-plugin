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

import org.eclipse.bpmn2.Expression;
import org.eclipse.bpmn2.ResourceAssignmentExpression;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class ResourceAssignmentExpressionDetailComposite extends ExpressionDetailComposite {

	private AbstractDetailComposite exprDetails;

	public ResourceAssignmentExpressionDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public ResourceAssignmentExpressionDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void cleanBindings() {
		super.cleanBindings();
		exprDetails = null;
	}

	@Override
	public void createBindings(final EObject be) {
		
		if (be instanceof ResourceAssignmentExpression) {
			
			final ResourceAssignmentExpression rae = (ResourceAssignmentExpression) be;
			
			// an Assignment is not really valid without both a From and To
			Expression expr = rae.getExpression();
			if (expr==null) {
				expr = FACTORY.createFormalExpression();
				InsertionAdapter.add(rae, PACKAGE.getResourceAssignmentExpression_Expression(), expr);
			}
			
			if (exprDetails==null) {
				exprDetails = PropertiesCompositeFactory.createDetailComposite(
						Expression.class, this, SWT.NONE);
			}
			exprDetails.setBusinessObject(expr);
			exprDetails.setTitle("Resource Assignment Expression");
		}
	}
}
