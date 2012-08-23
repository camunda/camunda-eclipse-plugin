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
package org.eclipse.bpmn2.modeler.ui.property.providers;

import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.IConstants;
import org.eclipse.swt.graphics.Image;


/**
 * Tree node to represent a Part within the messageType of a Variable.
 */
public class BPMN2ErrorTreeNode extends TreeNode {

	public BPMN2ErrorTreeNode(Error msg, boolean isCondensed) {
		super(msg, isCondensed);
	}

	/* ITreeNode */

	@Override
	public String getLabelSuffix() {
		Error error = (Error)modelObject;
		ItemDefinition itemDef = error.getStructureRef();
		if (itemDef==null || itemDef.getStructureRef()==null)
			return null;
		return ModelUtil.getStringWrapperValue(itemDef.getStructureRef());
	}

	@Override
	public Object[] getChildren() {
		return EMPTY_ARRAY;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Image getImage() {
		return Activator.getDefault().getImage(IConstants.ICON_BPMN2_ERROR_16);
	}

	@Override
	public String getLabel() {
		Error error = (Error)modelObject;
		String label = error.getName();
		if (label==null || label.isEmpty())
			label = "Unnamed Error";
		return label;
	}
}
