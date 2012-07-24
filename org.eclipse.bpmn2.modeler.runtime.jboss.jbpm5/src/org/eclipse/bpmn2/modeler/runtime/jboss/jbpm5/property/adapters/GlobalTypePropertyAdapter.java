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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.SequenceFlow;
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
import org.eclipse.bpmn2.modeler.ui.adapters.properties.FormalExpressionPropertiesAdapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * @author Bob Brodt
 *
 */
public class GlobalTypePropertyAdapter extends FormalExpressionPropertiesAdapter {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public GlobalTypePropertyAdapter(AdapterFactory adapterFactory, EObject object) {
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
					final EObject object = context instanceof EObject ? (EObject)context : this.object;
					final GlobalType type = (GlobalType)object;

					TransactionalEditingDomain domain = getEditingDomain(object);
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							String stringValue = "";
							if (value instanceof String) {
								stringValue = (String)value;
							}
							else if (value instanceof DataType) {
								stringValue = ((DataType)value).getStringType();
							}
							else if (value instanceof ImportType) {
								stringValue = ((ImportType)value).getName();
							}
							type.setType(stringValue);
						}
					});
				}
				
				@Override
				public Hashtable<String, Object> getChoiceOfValues(Object context) {
					Hashtable<String,Object> choices = new Hashtable<String,Object>();
					DataTypeRegistry.getFactory("dummy");
					for (Entry<String, DataTypeFactory> e : DataTypeRegistry.instance.entrySet()) {
						DataType dt = e.getValue().createDataType();
						if (dt instanceof EnumDataType || dt instanceof UndefinedDataType)
							continue;
						choices.put(dt.getStringType(),dt);
					}
					// add all imported data types
					EObject parent = object.eContainer();
					while (parent!=null && !(parent instanceof org.eclipse.bpmn2.Process))
						parent = parent.eContainer();
					List<ImportType> imports = ModelUtil.getAllExtensionAttributeValues(parent, ImportType.class);
					for (ImportType i : imports) {
						choices.put(i.getName(), i);
					}
					return choices;
				}
				
				@Override
				public boolean isMultiLine(Object context) {
					// formal expression body is always a multiline text field
					return true;
				}
			}
    	);
    	

	}

}
