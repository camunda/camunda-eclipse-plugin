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

import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.ui.property.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertySection;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextObjectEditor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.swt.SWT;

/**
 * This is an empty tab section which simply exists to hide the "Basic" tab
 * defined the editor UI plugin.
 * 
 * @author Bob Brodt
 *
 */
public class JbpmDiagramPropertySection extends DefaultPropertySection {

	public JbpmDiagramPropertySection() {
		super();
	}

	@Override
	protected EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		EObject be = super.getBusinessObjectForPictogramElement(pe);
		if (be instanceof BPMNDiagram)
			return be;
		return null;
	}

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		
		return new DefaultDetailComposite(this) {
			
			TextObjectEditor documentationEditor;

			@Override
			public void cleanBindings() {
				super.cleanBindings();
				documentationEditor = null;
			}

			@Override
			public void createBindings(EObject be) {

				bindAttribute(be,"id");
				bindAttribute(be,"name");

				EAttribute documentation = (EAttribute) be.eClass().getEStructuralFeature("documentation");
				documentationEditor = new TextObjectEditor(this,be,documentation);
				documentationEditor.createControl(getAttributesParent(),"Documentation",SWT.MULTI);
			}
		};
	}
}
