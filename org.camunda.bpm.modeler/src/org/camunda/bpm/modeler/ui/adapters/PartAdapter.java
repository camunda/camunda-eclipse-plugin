/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.camunda.bpm.modeler.ui.adapters;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.IConstants;
import org.camunda.bpm.modeler.core.adapters.AbstractAdapter;
import org.camunda.bpm.modeler.ui.Messages;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Part;

public class PartAdapter extends AbstractAdapter implements ILabeledElement {

	/* ILabeledElement */
	
	public Image getSmallImage(Object object) {
		return Activator.getDefault().getImage(IConstants.ICON_PART_16);
	}
	
	public Image getLargeImage(Object object) {
		return Activator.getDefault().getImage(IConstants.ICON_PART_32);
	}
	
	public String getTypeLabel(Object object) {
		return Messages.PartAdapter_Part_1; 
	}
	public String getLabel(Object object) {
		Part part = (Part)object;
		String name = part.getName();
		if (name != null)  return name;
		return getTypeLabel(object);
	}
}
