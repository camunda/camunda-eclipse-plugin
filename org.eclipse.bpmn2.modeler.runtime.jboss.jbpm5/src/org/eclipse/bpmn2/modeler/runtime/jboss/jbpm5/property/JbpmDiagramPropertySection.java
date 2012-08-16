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

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultPropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.tasks.TaskDetailComposite;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

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
		return new JbpmDiagramDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new JbpmDiagramDetailComposite(parent,style);
	}

	public class JbpmDiagramDetailComposite extends DefaultDetailComposite {
		
		public JbpmDiagramDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public JbpmDiagramDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		protected boolean isModelObjectEnabled(String className, String featureName) {
			if (featureName!=null && "id".equals(featureName))
					return true;
			return super.isModelObjectEnabled(className,featureName);
		}

		TextObjectEditor documentationEditor;

		@Override
		public void cleanBindings() {
			super.cleanBindings();
			documentationEditor = null;
		}

		@Override
		public void createBindings(EObject be) {

			BPMNDiagram bpmnDiagram = (BPMNDiagram)be;
			bindAttribute(bpmnDiagram.getPlane().getBpmnElement(),"id");
			bindAttribute(be,"name");

			EAttribute documentation = (EAttribute) be.eClass().getEStructuralFeature("documentation");
			documentationEditor = new TextObjectEditor(this,be,documentation);
			documentationEditor.createControl(getAttributesParent(),"Documentation",SWT.MULTI);
		}
	};
}
