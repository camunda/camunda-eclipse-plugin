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
package org.eclipse.bpmn2.modeler.ui.property;

import java.io.IOException;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.impl.BPMNDiagramImpl;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.runtime.IBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class AdvancedPropertySection extends GFPropertySection implements ITabbedPropertyConstants, IBpmn2PropertySection {

	private ImprovedAdvancedPropertiesComposite composite;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		parent.setLayout(new FillLayout());

		composite = new ImprovedAdvancedPropertiesComposite(parent, SWT.BORDER);
		composite.setSheetPage(aTabbedPropertySheetPage);
	}

	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			EObject be = BusinessObjectUtil.getFirstElementOfType(pe, BaseElement.class,true);
			if (be==null) {
				// maybe it's the Diagram (editor canvas)?
				be = BusinessObjectUtil.getFirstElementOfType(pe, BPMNDiagram.class);
			}
			if (be instanceof BPMNDiagramImpl) {
				try {
					composite.setEObject((BPMN2Editor) getDiagramEditor(),
							ModelHandlerLocator.getModelHandler(be.eResource()).getDefinitions());
				} catch (IOException e) {
					Activator.showErrorWithLogging(e);
				}
			} else {
				composite.setEObject((BPMN2Editor) getDiagramEditor(), be);
			}

		}
	}

	@Override
	public boolean shouldUseExtraSpace() {
		return false;
	}

	@Override
	public boolean appliesTo(IWorkbenchPart part, ISelection selection) {
		BPMN2Editor editor = (BPMN2Editor)part;
		return editor.getPreferences().getShowAdvancedPropertiesTab();
	}
}
