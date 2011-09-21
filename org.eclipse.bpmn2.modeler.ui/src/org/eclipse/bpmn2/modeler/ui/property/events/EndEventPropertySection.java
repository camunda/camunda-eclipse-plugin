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

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class EndEventPropertySection extends GFPropertySection implements ITabbedPropertyConstants {

	private EndEventPropertiesComposite composite;
	private TabbedPropertySheetPage aTabbedPropertySheetPage;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		this.aTabbedPropertySheetPage = aTabbedPropertySheetPage;
		super.createControls(parent, aTabbedPropertySheetPage);
		parent.setLayout(new FillLayout());

		composite = new EndEventPropertiesComposite(parent, SWT.BORDER);
	}

	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			EObject be = (EObject) Graphiti.getLinkService()
					.getBusinessObjectForLinkedPictogramElement(pe);
			if (be instanceof EndEvent) {
				EndEvent se = BusinessObjectUtil.
						getFirstElementOfType(pe, EndEvent.class,true);
				composite.setEObject((BPMN2Editor) getDiagramEditor(),
						se);
			}
			
			aTabbedPropertySheetPage.resizeScrolledComposite();
		}
	}

	@Override
	public boolean shouldUseExtraSpace() {
		return true;
	}
}
