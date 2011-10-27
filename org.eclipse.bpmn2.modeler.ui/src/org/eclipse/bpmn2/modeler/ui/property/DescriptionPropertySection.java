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

import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateConnectionFeature;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNFeatureProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.graphiti.features.IFeature;
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
			String description = null;

			BPMN2Editor editor = (BPMN2Editor)getDiagramEditor();
			boolean showAdvancedProperties = editor.getPreferences().getShowAdvancedPropertiesTab();

			BPMNFeatureProvider fp = (BPMNFeatureProvider) editor.getDiagramTypeProvider().getFeatureProvider();
			IFeature cf = fp.getCreateFeatureForBusinessObject(be);
			if (cf instanceof AbstractBpmn2CreateConnectionFeature) {
				AbstractBpmn2CreateConnectionFeature acf = (AbstractBpmn2CreateConnectionFeature) cf;
				description = acf.getCreateDescription();
			} else if (cf instanceof AbstractBpmn2CreateFeature) {
				AbstractBpmn2CreateFeature acf = (AbstractBpmn2CreateFeature) cf;
				description = acf.getDescription();
			}
			if (description == null) {
				description = "No description";
			}
			if (description != null) {
				createDescription(this, description);
			}
			
			bindAttribute(be,"id");
			bindAttribute(be,"name");

			if (showAdvancedProperties) {
				bindReference(be, "extensionDefinitions");
				bindList(be, "extensionValues");
				bindList(be, "documentation");
			}
		}
	}
}
