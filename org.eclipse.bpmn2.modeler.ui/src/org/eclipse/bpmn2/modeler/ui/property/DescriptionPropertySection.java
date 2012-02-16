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

import org.eclipse.bpmn2.modeler.core.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateConnectionFeature;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.Bpmn2EditorPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNFeatureProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;

/**
 * @author Bob Brodt
 * 
 */
public class DescriptionPropertySection extends AbstractBpmn2PropertySection implements ITabbedPropertyConstants {

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection#createSectionRoot()
	 */
	@Override
	protected AbstractBpmn2PropertiesComposite createSectionRoot() {
		return new DescriptionPropertyComposite(this);		
	}
	
	@Override
	public boolean appliesTo(IWorkbenchPart part, ISelection selection) {
		// always show this tab
		return true;
	}

	public class DescriptionPropertyComposite extends AbstractBpmn2PropertiesComposite {

		/**
		 * @param section
		 */
		public DescriptionPropertyComposite(AbstractBpmn2PropertySection section) {
			super(section);
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

			bindDescription(be);
			
			// temporarily enable these for this tab only!
//			boolean idEnabled = modelEnablement.isEnabled(be.eClass().getName(), "id");
			boolean nameEnabled = modelEnablement.isEnabled(be.eClass().getName(), "name");
//			boolean documentationEnabled = modelEnablement.isEnabled(be.eClass().getName(), "documentation");
			
//			modelEnablement.setEnabled(be.eClass().getName(), "id", true);
			modelEnablement.setEnabled(be.eClass().getName(), "name", true);
//			modelEnablement.setEnabled(be.eClass().getName(), "documentation", true);

			bindAttribute(be,"id");
			bindAttribute(be,"name");
			bindList(be, "documentation");

//			if (!idEnabled)
//				modelEnablement.setEnabled(be.eClass().getName(), "id", false);
			if (!nameEnabled)
				modelEnablement.setEnabled(be.eClass().getName(), "name", false);
//			if (!documentationEnabled)
//				modelEnablement.setEnabled(be.eClass().getName(), "documentation", false);
		}
		
		protected void bindDescription(EObject be) {
			String description = getDescription(be);

			if (description != null) {
				createDescription(this, description);
			}
		}
		
		public String getDescription(EObject object) {
			String description = null;

			ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, Bpmn2EditorPropertiesAdapter.class);
			if (adapter!=null) {
				description = (String) adapter.getProperty(Bpmn2EditorPropertiesAdapter.LONG_DESCRIPTION);
			}
			return description;
		}
	}
}
