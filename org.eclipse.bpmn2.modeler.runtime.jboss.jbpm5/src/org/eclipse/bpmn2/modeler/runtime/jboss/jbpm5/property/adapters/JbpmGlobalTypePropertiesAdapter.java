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
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.DataType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.DataTypeFactory;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.DataTypeRegistry;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.impl.type.EnumDataType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.drools.process.core.datatype.impl.type.UndefinedDataType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.GlobalType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ImportType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.util.JbpmModelUtil;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * @author Bob Brodt
 *
 */
public class JbpmGlobalTypePropertiesAdapter extends ExtendedPropertiesAdapter {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public JbpmGlobalTypePropertiesAdapter(AdapterFactory adapterFactory, EObject object) {
		super(adapterFactory, object);
    	setProperty(ModelPackage.GLOBAL_TYPE__TYPE, ExtendedPropertiesAdapter.UI_CAN_CREATE_NEW, Boolean.TRUE);

    	EStructuralFeature feature = ModelPackage.eINSTANCE.getGlobalType_Identifier();
    	setFeatureDescriptor(feature,
			new FeatureDescriptor(adapterFactory,object,feature) {
				@Override
				public String getLabel(Object context) {
					return "Name";
				}
    		});	

    	feature = ModelPackage.eINSTANCE.getGlobalType_Type();
    	setFeatureDescriptor(feature,
			new FeatureDescriptor(adapterFactory,object,feature) {
				@Override
				public String getLabel(Object context) {
					return "Data Type";
				}
				
				@Override
				public void setValue(Object context, final Object value) {
					final GlobalType global = context instanceof GlobalType ?
							(GlobalType)context :
							(GlobalType)this.object;

					TransactionalEditingDomain domain = getEditingDomain(object);
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							global.setType(JbpmModelUtil.getDataType(value));
						}
					});
				}
				
				@Override
				public Hashtable<String, Object> getChoiceOfValues(Object context) {
					EObject object = context instanceof EObject ? (EObject)context : this.object;
					return JbpmModelUtil.collectAllDataTypes(object);
				}
				
				@Override
				public boolean isMultiLine(Object context) {
					return true;
				}
			}
    	);
    	

	}

}
