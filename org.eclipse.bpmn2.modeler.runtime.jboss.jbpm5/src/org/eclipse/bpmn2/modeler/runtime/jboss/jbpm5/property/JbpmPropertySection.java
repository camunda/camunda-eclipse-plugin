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
/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.property;

import java.io.IOException;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class JbpmPropertySection extends AbstractBpmn2PropertySection implements ITabbedPropertyConstants {
	public JbpmPropertySection() {
	}

	private JbpmPropertiesComposite composite;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		composite = new JbpmPropertiesComposite(parent, SWT.None);
	}

	@Override
	protected AbstractBpmn2PropertiesComposite getComposite() {
		return composite;
	}

	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			EObject be = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			if (be instanceof Participant){
				composite.setEObject((BPMN2Editor) getDiagramEditor(), ((Participant) be).getProcessRef());	
			}else if (be instanceof BPMNDiagram){
				try {
					composite.setEObject((BPMN2Editor) getDiagramEditor(), ModelHandlerLocator.getModelHandler(be.eResource()).getInternalParticipant().getProcessRef());
				} catch (IOException e) {
					Activator.showErrorWithLogging(e);
				}
			}else 
			composite.setEObject((BPMN2Editor) getDiagramEditor(), be);
			
			super.refresh();
		}
	}
}
