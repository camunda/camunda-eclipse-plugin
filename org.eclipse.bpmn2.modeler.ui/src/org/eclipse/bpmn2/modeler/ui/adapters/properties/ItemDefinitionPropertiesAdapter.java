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
import org.eclipse.bpmn2.ItemKind;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.adapters.Bpmn2FeatureDescriptor;
import org.eclipse.bpmn2.modeler.ui.adapters.Bpmn2ObjectDescriptor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Bob Brodt
 *
 */
public class ItemDefinitionPropertiesAdapter extends Bpmn2ExtendedPropertiesAdapter {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public ItemDefinitionPropertiesAdapter(AdapterFactory adapterFactory, EObject object) {
		super(adapterFactory, object);

    	final EStructuralFeature ref = Bpmn2Package.eINSTANCE.getItemDefinition_StructureRef();
    	setFeatureDescriptor(ref,
			new Bpmn2FeatureDescriptor(adapterFactory,object,ref) {
				@Override
				public String getLabel(Object context) {
					EObject object = this.object;
					if (context instanceof EObject)
						object = (EObject)context;
					if (object instanceof ItemDefinition) {
						return "Data Type";
					}
					return super.getLabel(context);
				}

				@Override
				public String getText(Object context) {
					EObject object = this.object;
					if (context instanceof EObject)
						object = (EObject)context;
					if (object instanceof ItemDefinition) {
    					ItemDefinition itemDefinition = (ItemDefinition) object;
    					if (itemDefinition.getStructureRef()!=null) {
    						String type = " (Undefined";
        					if (itemDefinition.getItemKind().equals(ItemKind.PHYSICAL))
        						type = " (Physical";
        					else if (itemDefinition.getItemKind().equals(ItemKind.INFORMATION))
        						type = " (Informational";
        					if (itemDefinition.isIsCollection())
        						type += " Collection)";
        					else
        						type += ")";

    						return ModelUtil.getStructureRefValue( itemDefinition.getStructureRef() ) + type;
    					}
    					else
    						return "";
					}
					return super.getText(context);
				}
			}
    	);
    	
		setObjectDescriptor(new Bpmn2ObjectDescriptor(adapterFactory, object) {
			@Override
			public String getText(Object context) {
				return getFeatureDescriptor(ref).getText(context);
			}
		});
	}

}
