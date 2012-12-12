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

package org.eclipse.bpmn2.modeler.ui.property.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.IConstants;
import org.eclipse.swt.graphics.Image;

/**
 * @author Bob Brodt
 *
 */
public class JavaTypeTreeNode extends TreeNode {

	/**
	 * @param modelObject
	 * @param isCondensed
	 */
	public JavaTypeTreeNode(Object modelObject, boolean isCondensed) {
		super(modelObject, isCondensed);
	}

	@Override
	public String getLabel() {
		Class c = (Class)modelObject;
		return c.getSimpleName() + " - " + c.getPackage().getName();
	}

	@Override
	public Image getImage() {
		Class c = (Class)modelObject;
		if (c.isInterface())
			return Activator.getDefault().getImage(IConstants.ICON_JAVA_INTERFACE_16);
		return Activator.getDefault().getImage(IConstants.ICON_JAVA_CLASS_16);
	}

	@Override
	public boolean hasChildren() {
		return getChildren().length>0;
	}

	@Override
	public Object[] getChildren() {
		List<JavaMemberTreeNode> kids = new ArrayList<JavaMemberTreeNode>();
		Class c = (Class)modelObject;
		for (Field f : c.getDeclaredFields()) {
			if ((Modifier.PUBLIC & f.getModifiers()) != 0)
				kids.add(new JavaMemberTreeNode(f,isCondensed));
		}
		for (Method m : c.getDeclaredMethods()) {
			if ((Modifier.PUBLIC & m.getModifiers()) != 0)
				kids.add(new JavaMemberTreeNode(m,isCondensed));
		}
		return kids.toArray(new Object[kids.size()]);
	}
}
