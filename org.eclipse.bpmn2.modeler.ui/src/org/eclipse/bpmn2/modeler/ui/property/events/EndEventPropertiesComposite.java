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
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.property.events;

import java.util.ArrayList;

import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class EndEventPropertiesComposite extends DefaultPropertiesComposite {

	private ArrayList<String> showProperties = null;
	
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EndEventPropertiesComposite(Composite parent, int style) {
		super(parent, style);
		
		showProperties = new ArrayList<String>();
		// no properties currently
	}

	@Override
	protected boolean canBindList(EObject object, EStructuralFeature feature) {
		return false;
	}

	@Override
	protected boolean canBindListColumn(EObject object, EAttribute attribute) {
		return true;
	}

	@Override
	protected boolean canBindAttribute(EObject object, EAttribute attribute) {
		if (showProperties.contains(attribute.getName())) {
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean canBindReference(EObject object, EReference reference) {
		if (showProperties.contains(reference.getName())) {
			return true;
		}
		return false;
	}
}
