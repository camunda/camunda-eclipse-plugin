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

package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.property.adapters;

import java.util.Hashtable;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Property;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.util.JbpmModelUtil;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.ItemAwareElementFeatureDescriptor;
import org.eclipse.bpmn2.modeler.ui.adapters.properties.PropertyPropertiesAdapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * @author Bob Brodt
 *
 */
public class JbpmPropertyPropertiesAdapter extends PropertyPropertiesAdapter {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public JbpmPropertyPropertiesAdapter(AdapterFactory adapterFactory, Property object) {
		super(adapterFactory, object);
    	setProperty(Bpmn2Package.eINSTANCE.getItemAwareElement_ItemSubjectRef(), UI_CAN_CREATE_NEW, Boolean.TRUE);
    	setProperty(Bpmn2Package.eINSTANCE.getItemAwareElement_ItemSubjectRef(), UI_CAN_EDIT, Boolean.FALSE);

    	EStructuralFeature feature = Bpmn2Package.eINSTANCE.getItemAwareElement_ItemSubjectRef();
    	setFeatureDescriptor(feature,
			new ItemAwareElementFeatureDescriptor<Property>(adapterFactory,object,feature) {
				@Override
				public String getLabel(Object context) {
					return "Data Type";
				}
				
				@Override
				public void setValue(Object context, final Object value) {
					final Property property = adopt(context);

					TransactionalEditingDomain domain = getEditingDomain(object);
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							property.setItemSubjectRef(JbpmModelUtil.getDataType(property, value));
						}
					});
				}

				@Override
				public String getDisplayName(Object context) {
					return super.getDisplayName(context);
				}
				
				@Override
				public Hashtable<String, Object> getChoiceOfValues(Object context) {
					final Property property = adopt(context);
					return JbpmModelUtil.collectAllDataTypes(property);
				}
				
				@Override
				public boolean isMultiLine(Object context) {
					return true;
				}
			}
    	);

    	feature = Bpmn2Package.eINSTANCE.getProperty_Name();
    	setFeatureDescriptor(feature,
			new FeatureDescriptor<Property>(adapterFactory,object,feature) {
				@Override
				public String getLabel(Object context) {
					return "Description";
				}
			}
    	);

    	feature = Bpmn2Package.eINSTANCE.getBaseElement_Id();
    	setFeatureDescriptor(feature,
			new FeatureDescriptor<Property>(adapterFactory,object,feature) {
				@Override
				public String getLabel(Object context) {
					return "Name";
				}
			}
    	);
	}

}
