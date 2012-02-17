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

import java.util.Hashtable;

import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.connectors.SequenceFlowPropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.editors.ComboObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class JbpmSequenceFlowPropertiesComposite extends SequenceFlowPropertiesComposite {

	/**
	 * @param section
	 */
	public JbpmSequenceFlowPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public JbpmSequenceFlowPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	public void createBindings(EObject be) {
		bindAttribute(this, be, "priority");
		super.createBindings(be);
	}
	
	protected void bindAttribute(Composite parent, EObject object, EAttribute attribute, String label) {
		if ("language".equals(attribute.getName())) {
			ObjectEditor editor = new ComboObjectEditor(this,be,be.eClass().getEStructuralFeature("language")) {
				
				@Override
				protected Hashtable getChoiceOfValues(EObject object, EStructuralFeature feature) {
					Hashtable<String, Object> values = new Hashtable<String, Object>();
					values.put("Java", "http://www.java.com/java");
					values.put("MVEL", "http://www.mvel.org/2.0");
					values.put("Rule", "http://www.jboss.org/drools/rule");
					return values;
				}
			};
			editor.createControl(getAttributesParent(),"Script Language");
		}
		else
			super.bindAttribute(parent, object, attribute, label);
	}
}
