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
import org.eclipse.bpmn2.Interface;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.FeatureDescriptor;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class InterfacePropertiesAdapter extends ExtendedPropertiesAdapter<Interface> {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public InterfacePropertiesAdapter(AdapterFactory adapterFactory, Interface object) {
		super(adapterFactory, object);
		
    	final EStructuralFeature ref = Bpmn2Package.eINSTANCE.getInterface_ImplementationRef();
    	setFeatureDescriptor(ref,
			new FeatureDescriptor<Interface>(adapterFactory,object,ref) {
				@Override
				public String getDisplayName(Object context) {
					final Interface iface = adopt(context);
							
					if (iface.getImplementationRef()!=null) {
						String text = ModelUtil.getStringWrapperValue( iface.getImplementationRef() ); // + type;
						if (text==null)
							return PropertyUtil.getDisplayName(iface.getImplementationRef());
					}
					return "";
				}
				
	    		@Override
				public EObject createObject(Object context, EClass eClass) {
					final Interface iface = adopt(context);

					EObject impl = ModelUtil.createStringWrapper("");
					InsertionAdapter.add(iface, ref, impl);
					return impl;
	    		}

	    		@Override
	    		public Object getValue(Object context) {
					final Interface iface = adopt(context);
					return iface.getImplementationRef();
	    		}

	    		@Override
	    		public void setValue(Object context, Object value) {
					if (value instanceof String) {
						value = ModelUtil.createStringWrapper((String)value);
	    			}
	    			else if (!ModelUtil.isStringWrapper(value)) {
	    				return;
	    			}
	    			super.setValue(object,value);
	    		}
    		}
    	);
    	
		setObjectDescriptor(new ObjectDescriptor<Interface>(adapterFactory, object) {
			@Override
			public String getDisplayName(Object context) {
				return getFeatureDescriptor(ref).getDisplayName(context);
			}

			@Override
			public boolean equals(Object obj) {
				if (obj instanceof Interface) {
					Interface iface = (Interface)obj;
					if (ModelUtil.compare(iface.getName(),(object).getName())) {
						if (ModelUtil.compare(iface.getImplementationRef(),(object).getImplementationRef()))
							return true;
					}
				}
				return false;
			}
		});

	}

}
