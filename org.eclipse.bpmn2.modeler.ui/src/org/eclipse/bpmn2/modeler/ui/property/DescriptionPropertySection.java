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
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
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
	protected AbstractDetailComposite createSectionRoot() {
		return new DescriptionPropertyComposite(this);		
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		 return new DescriptionPropertyComposite(parent, style);
	}

	@Override
	public boolean appliesTo(IWorkbenchPart part, ISelection selection) {
		// always show this tab
		return getBusinessObjectForSelection(selection) != null;
	}

	public class DescriptionPropertyComposite extends AbstractDetailComposite {

		/**
		 * @param section
		 */
		public DescriptionPropertyComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}
		
		public DescriptionPropertyComposite(Composite parent, int style) {
			super(parent,style);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2DetailComposite
		 * #createBindings(org.eclipse.emf.ecore.EObject)
		 */
		@Override
		public void createBindings(EObject be) {

			bindDescription(be);
			bindAttribute(be,"id");
			bindAttribute(be,"name");
			bindList(be, "documentation");
			redrawPage();
		}

		protected boolean isModelObjectEnabled(String className, String featureName) {
			if (featureName!=null && "name".equals(featureName))
					return true;
			return super.isModelObjectEnabled(className,featureName);
		}
		
		protected void bindDescription(EObject be) {
			if (Bpmn2Preferences.getInstance().getShowDescriptions()) {
				String description = getDescription(be);
	
				if (description != null) {
					createDescription(this, description);
				}
			}
		}
		
		public String getDescription(EObject object) {
			String description = null;

			ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(object, ExtendedPropertiesAdapter.class);
			if (adapter!=null) {
				description = (String) adapter.getProperty(ExtendedPropertiesAdapter.LONG_DESCRIPTION);
			}
			return description;
		}
	}
}
