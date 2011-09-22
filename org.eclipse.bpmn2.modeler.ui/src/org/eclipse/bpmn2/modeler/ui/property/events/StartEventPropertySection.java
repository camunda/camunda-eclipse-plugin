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
package org.eclipse.bpmn2.modeler.ui.property.events;

import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class StartEventPropertySection extends AbstractBpmn2PropertySection implements ITabbedPropertyConstants {

	private StartEventPropertiesComposite composite;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		composite = new StartEventPropertiesComposite(parent, SWT.BORDER);
	}

	@Override
	protected AbstractBpmn2PropertiesComposite getComposite() {
		return composite;
	}

	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			EObject be = (EObject) Graphiti.getLinkService()
					.getBusinessObjectForLinkedPictogramElement(pe);
			if (be instanceof StartEvent) {
				StartEvent se = BusinessObjectUtil.
						getFirstElementOfType(pe, StartEvent.class,true);
				composite.setEObject((BPMN2Editor) getDiagramEditor(),
						se);
			}
			
			super.refresh();
		}
	}
}
