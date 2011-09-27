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

package org.eclipse.bpmn2.modeler.ui.property;

import java.util.List;

import org.eclipse.bpmn2.UserTask;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateConnectionFeature;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNFeatureProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author Bob Brodt
 * 
 */
public class DescriptionPropertySection extends AbstractBpmn2PropertySection implements ITabbedPropertyConstants {

	DescriptionPropertyComposite composite;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		composite = new DescriptionPropertyComposite(parent, SWT.None);
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
			composite.setEObject((BPMN2Editor) getDiagramEditor(), be);
			
			super.refresh();
		}
	}

	public class DescriptionPropertyComposite extends AbstractBpmn2PropertiesComposite {

		/**
		 * @param parent
		 * @param style
		 */
		public DescriptionPropertyComposite(Composite parent, int style) {
			super(parent, style);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite
		 * #createBindings(org.eclipse.emf.ecore.EObject)
		 */
		@Override
		public void createBindings(EObject be) {
			String description = null;

			BPMN2Editor editor = BPMN2Editor.getEditor(be);
			BPMNFeatureProvider fp = (BPMNFeatureProvider) editor.getDiagramTypeProvider().getFeatureProvider();
			IFeature cf = fp.getCreateFeatureForBusinessObject(be);
			if (cf instanceof AbstractBpmn2CreateConnectionFeature) {
				AbstractBpmn2CreateConnectionFeature acf = (AbstractBpmn2CreateConnectionFeature) cf;
				description = acf.getCreateDescription();
			} else if (cf instanceof AbstractBpmn2CreateFeature) {
				AbstractBpmn2CreateFeature acf = (AbstractBpmn2CreateFeature) cf;
				description = acf.getDescription();
			}
			if (description != null) {
				createDescription(this, description);
			}
			
			bindAttribute(be,"id");
			bindAttribute(be,"name");

			bindList(be, "documentation");
			bindList(be, "extensionDefinitions");
			bindList(be, "extensionValues");
		}
	}
}
