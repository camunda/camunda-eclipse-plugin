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
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.IConstants;
import org.eclipse.swt.graphics.Image;

/**
 * Tree node to represent a Process model object.
 */
public class BPMN2ProcessTreeNode extends TreeNode {

	public BPMN2ProcessTreeNode(Process process, boolean isCondensed) {
		super(process, isCondensed);
	}
	
	@Override
	public Object[] getChildren() {
		Process process = (Process)modelObject;
		if (process.getSupportedInterfaceRefs().isEmpty())
			return EMPTY_ARRAY;
		List<BPMN2InterfaceTreeNode> v = new ArrayList<BPMN2InterfaceTreeNode>();
		for (Interface it : process.getSupportedInterfaceRefs()) {
			v.add(new BPMN2InterfaceTreeNode(it, isCondensed));
		}
		return v.toArray();
	}

	@Override
	public boolean hasChildren() {
		Process process = (Process)modelObject;
		return !process.getSupportedInterfaceRefs().isEmpty();
	}

	@Override
	public Image getImage() {
		return Activator.getDefault().getImage(IConstants.ICON_BPMN2_PROCESS_16);
	}

	@Override
	public String getLabel() {
		Process process = (Process)modelObject;
		String label = process.getName();
		if (label==null || label.isEmpty())
			label = "Unnamed Process";
		return label;
	}
}
