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
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.DescriptionPropertySection;
import org.eclipse.bpmn2.modeler.ui.property.tasks.TaskDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This is an empty tab section which simply exists to hide the "Basic" tab
 * defined the editor UI plugin.
 * 
 * @author Bob Brodt
 *
 */
public class JbpmDescriptionPropertySection extends DescriptionPropertySection {
	
	@Override
	public boolean appliesTo(IWorkbenchPart part, ISelection selection) {
		return getBusinessObjectForSelection(selection) != null;
	}

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new JbpmDescriptionPropertyComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new JbpmDescriptionPropertyComposite(parent,style);
	}
	
	public JbpmDescriptionPropertySection() {
		super();
		// TODO Auto-generated constructor stub
	}

	public class JbpmDescriptionPropertyComposite extends DescriptionPropertyComposite {
		
		public JbpmDescriptionPropertyComposite(
				AbstractBpmn2PropertySection section) {
			super(section);
		}

		public JbpmDescriptionPropertyComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(EObject be) {
			// for BPMNDiagram objects, pick out the Process and render the Process attributes
			Process process = null;
			if (be instanceof Participant) {
				process = ((Participant) be).getProcessRef();
			} else if (be instanceof BPMNDiagram) {
				BaseElement bpmnElement = ((BPMNDiagram)be).getPlane().getBpmnElement();
				if (bpmnElement instanceof Process)
					process = (Process)bpmnElement;
			}
			
			if (process==null) {
				// display the default Description tab
				super.createBindings(be);
			}
			else {
				// create our own for Process
				bindDescription(be);
				
				bindAttribute(process, "id");
				bindAttribute(process, "name");
				bindAttribute(process, "anyAttribute");
				bindAttribute(process, "processType");
				bindAttribute(process, "isExecutable");
				bindAttribute(process, "isClosed");
//				bindList(process, "properties"); // this has moved to JbpmDataItemsDetailComposite
//				bindList(process, "laneSets"); // don't need this
			}
		}

	}
}
