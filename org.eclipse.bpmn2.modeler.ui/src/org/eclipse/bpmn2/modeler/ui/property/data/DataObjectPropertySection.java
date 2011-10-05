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

package org.eclipse.bpmn2.modeler.ui.property.data;

import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.DataState;
import org.eclipse.bpmn2.modeler.core.utils.PropertyUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite.AbstractItemProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * @author Bob Brodt
 * From section 10.3.1 of the BPMN 2.0 specification:
 * "The Data Object class is an item-aware element. Data Object elements MUST be contained within Process or Sub-
 * Process elements. Data Object elements are visually displayed on a Process diagram. Data Object References are
 * a way to reuse Data Objects in the same diagram. They can specify different states of the same Data Object at
 * different points in a Process. Data Object Reference cannot specify item definitions, and Data Objects cannot
 * specify states. The names of Data Object References are derived by concatenating the name of the referenced Data
 * Data Object the state of the Data Object Reference in square brackets as follows: <Data Object Name> [ <Data
 * Object Reference State> ]."
 */
public class DataObjectPropertySection extends AbstractBpmn2PropertySection {

	private AbstractItemProvider dataObjectItemProvider;

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection#createSectionRoot()
	 */
	@Override
	protected AbstractBpmn2PropertiesComposite createSectionRoot() {
		return new DataObjectPropertiesComposite(this);
	}

	@Override
	protected EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		EObject bo = super.getBusinessObjectForPictogramElement(pe);
		if (bo instanceof DataObjectReference) {
			if ( ((DataObjectReference) bo).getDataObjectRef() == null)
				return bo;
			return ((DataObjectReference) bo).getDataObjectRef();
		} else if (bo instanceof DataObject) {
			return bo;
		}
		
		return null;
	}
	
	public class DataObjectPropertiesComposite extends DefaultPropertiesComposite {

		/**
		 * @param section
		 */
		public DataObjectPropertiesComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		@Override
		public AbstractItemProvider getItemProvider(EObject object) {
			if (object instanceof DataObject) {
				if (dataObjectItemProvider == null) {
					dataObjectItemProvider = new AbstractItemProvider(object) {
						String[] allAttributes = new String[] { "id", "name", "isCollection", "itemSubjectRef" };
						String[] attributes = new String[] { "isCollection", "itemSubjectRef" };
						
						@Override
						public String[] getAttributes() {
							EObject bo = PropertyUtil.getBusinessObjectForSelection(propertySection.getSelection());
							if (bo instanceof DataObjectReference) {
								return allAttributes;
							}
							return attributes; 
						}
	
						@Override
						public boolean alwaysShowAdvancedProperties() {
							return true;
						}
					};
				}
				return dataObjectItemProvider;
			}
			return null;
		}
		
	}
}
