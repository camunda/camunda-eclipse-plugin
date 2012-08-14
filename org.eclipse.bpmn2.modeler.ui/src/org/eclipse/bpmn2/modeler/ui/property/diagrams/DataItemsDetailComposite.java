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

package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class DataItemsDetailComposite extends DefaultDetailComposite {

	public DataItemsDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public DataItemsDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (propertiesProvider==null) {
			propertiesProvider = new AbstractPropertiesProvider(object) {
				String[] properties = new String[] {
						"rootElements#Process.properties",
						"rootElements#Process.resources",
						"rootElements#Process.correlationSubscriptions",
						"rootElements#Process.collaborations",
						"rootElements#Process.artifacts",
				};
				
				@Override
				public String[] getProperties() {
					return properties; 
				}
			};
		}
		return propertiesProvider;
	}
	
	@Override
	protected AbstractListComposite bindList(EObject object, EStructuralFeature feature, EClass listItemClass) {
		if (listItemClass!=null && listItemClass.getName().equals("ItemDefinition")) {
			if (modelEnablement.isEnabled(object.eClass(), feature) || modelEnablement.isEnabled(listItemClass)) {
				AbstractListComposite table = super.bindList(object, feature, listItemClass);
				return table;
			}
			return null;
		}
		else
			return super.bindList(object, feature, listItemClass);
	}

}
