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

import org.eclipse.bpmn2.Error;
import org.eclipse.bpmn2.Operation;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.IConstants;
import org.eclipse.swt.graphics.Image;

/**
 * Tree node to represent a message-type model object.
 */
public class BPMN2OperationTreeNode extends TreeNode {
	
	public BPMN2OperationTreeNode(Operation operation, boolean isCondensed) {
		super(operation, isCondensed);		
	}

	@Override
	public Object[] getChildren() {
		Operation operation = (Operation)modelObject;

		List<TreeNode> list = new ArrayList<TreeNode>();
		if (operation.getInMessageRef()!=null)
			list.add(new BPMN2MessageTreeNode(operation.getInMessageRef(), isCondensed, true));
		if (operation.getOutMessageRef()!=null)
			list.add(new BPMN2MessageTreeNode(operation.getOutMessageRef(), isCondensed, false));
		if (!operation.getErrorRefs().isEmpty()) {
			for (Error e : operation.getErrorRefs()) {
				list.add(new BPMN2ErrorTreeNode(e, isCondensed));
			}
		}
		return list.toArray();
	}

	@Override
	public boolean hasChildren() {
		Operation operation = (Operation)modelObject;
		return operation.getInMessageRef()!=null
				|| operation.getOutMessageRef()!=null
				|| !operation.getErrorRefs().isEmpty();
	}

	@Override
	public Image getImage() {
		return Activator.getDefault().getImage(IConstants.ICON_BPMN2_OPERATION_16);
	}

	@Override
	public String getLabel() {
		Operation op = (Operation)modelObject;
		String label = op.getName();
		if (label==null || label.isEmpty())
			label = "Unnamed Operation";
		return label;
	}
}
