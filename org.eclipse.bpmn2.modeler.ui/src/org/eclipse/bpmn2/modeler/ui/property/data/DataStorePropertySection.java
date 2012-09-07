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

import org.eclipse.bpmn2.DataStore;
import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.DataState;
import org.eclipse.bpmn2.DataStore;
import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite.AbstractPropertiesProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class DataStorePropertySection extends AbstractBpmn2PropertySection {
	
	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection#createSectionRoot()
	 */
	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new DataStoreDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new DataStoreDetailComposite(parent,style);
	}

	@Override
	protected EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		EObject bo = super.getBusinessObjectForPictogramElement(pe);
		if (bo instanceof DataStoreReference) {
			return ((DataStoreReference) bo).getDataStoreRef();
		} else if (bo instanceof DataStore) {
			return bo;
		}
		
		return null;
	}
	
	public class DataStoreDetailComposite extends DefaultDetailComposite {

		private AbstractPropertiesProvider dataStoreReferencePropertiesProvider;
		private AbstractPropertiesProvider dataStatePropertiesProvider;

		public DataStoreDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		/**
		 * @param section
		 */
		public DataStoreDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		@Override
		public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
			if (object instanceof DataState) {
				if (dataStatePropertiesProvider == null) {
					dataStatePropertiesProvider = new AbstractPropertiesProvider(object) {
						String[] properties = new String[] { "id", "name" };
						
						@Override
						public String[] getProperties() {
							return properties; 
						}
					};
				}
				return dataStatePropertiesProvider;
			}
			else if (object instanceof DataStore) {
				if (propertiesProvider == null) {
					propertiesProvider = new AbstractPropertiesProvider(object) {
						String[] properties = new String[] { "id", "name", "capacity", "isUnlimited", "itemSubjectRef", "dataState" };
						
						@Override
						public String[] getProperties() {
							return properties;
						}
					};
				}
				return propertiesProvider;
			}
			else if (object instanceof DataStoreReference) {
				if (dataStoreReferencePropertiesProvider == null) {
					dataStoreReferencePropertiesProvider = new AbstractPropertiesProvider(object) {
						String[] properties = new String[] { "id", "name", "dataStoreRef" , "dataState" };
	
						@Override
						public String[] getProperties() {
							return properties; 
						}
					};
			
				}
				return dataStoreReferencePropertiesProvider;
			}
			return null;
		}
	}
}
