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

package org.eclipse.bpmn2.modeler.ui.adapters.properties;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class ItemDefinitionPropertiesAdapter extends ExtendedPropertiesAdapter<ItemDefinition> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public ItemDefinitionPropertiesAdapter(AdapterFactory adapterFactory, ItemDefinition object) {
		super(adapterFactory, object);

    	final EStructuralFeature ref = Bpmn2Package.eINSTANCE.getItemDefinition_StructureRef();
    	setFeatureDescriptor(ref,
			new FeatureDescriptor<ItemDefinition>(adapterFactory,object,ref) {
				@Override
				public String getLabel(Object context) {
					return "Data Type";
				}

				@Override
				public String getDisplayName(Object context) {
					final ItemDefinition itemDefinition = adopt(context);
					if (itemDefinition.getStructureRef()!=null) {
						return ModelUtil.getStringWrapperValue(itemDefinition.getStructureRef());
					}
					return itemDefinition.getId() + " type is undefined";
				}
				
	    		@Override
				public EObject createObject(Object context, EClass eClass) {
					final ItemDefinition itemDefinition = adopt(context);
					EObject structureRef = ModelUtil.createStringWrapper("");
					InsertionAdapter.add(itemDefinition, ref, structureRef);
					return structureRef;
	    		}

	    		@Override
	    		public Object getValue(Object context) {
					final ItemDefinition itemDefinition = adopt(context);
					return itemDefinition.getStructureRef();
	    		}

	    		@Override
	    		public void setValue(Object context, Object value) {
	    			if (value instanceof String) {
						value = ModelUtil.createStringWrapper((String)value);
	    			}
	    			else if (!ModelUtil.isStringWrapper(value)) {
	    				return;
	    			}
	    			super.setValue(object, value);
	    		}
			}
    	);
    	
		setObjectDescriptor(new ObjectDescriptor<ItemDefinition>(adapterFactory, object) {
			@Override
			public String getDisplayName(Object context) {
				return getFeatureDescriptor(ref).getDisplayName(context);
			}
			
			@Override
			public String getLabel(Object context) {
				return getFeatureDescriptor(ref).getLabel(context);
			}
		});
	}

}
