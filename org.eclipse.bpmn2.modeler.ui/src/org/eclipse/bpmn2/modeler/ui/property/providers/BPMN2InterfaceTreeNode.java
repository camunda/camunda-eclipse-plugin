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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Interface;
import org.eclipse.bpmn2.Operation;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.IConstants;
import org.eclipse.swt.graphics.Image;

/**
 * Tree node to represent an Interface model object.
 */
public class BPMN2InterfaceTreeNode extends TreeNode {

	public BPMN2InterfaceTreeNode(Interface it, boolean isCondensed) {
		super(it, isCondensed);
	}

	/* ITreeNode */

	@Override
	public Object[] getChildren() {		
		Interface intf = (Interface)modelObject;
		List<BPMN2OperationTreeNode> list = new ArrayList<BPMN2OperationTreeNode>();
		for (Operation op : intf.getOperations()) {
			list.add(new BPMN2OperationTreeNode(op,isCondensed));
		}
		return list.toArray();
	}

	@Override
	public boolean hasChildren() {
		Interface intf = (Interface) modelObject;
		return !intf.getOperations().isEmpty();
	}

	@Override
	public Image getImage() {
		return Activator.getDefault().getImage(IConstants.ICON_BPMN2_INTERFACE_16);
	}

	@Override
	public String getLabel() {
		Interface intf = (Interface)modelObject;
		String label = intf.getName();
		if (label==null || label.isEmpty())
			label = "Unnamed Interface";
		return label;
	}
}
