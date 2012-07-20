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
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.IConstants;
import org.eclipse.swt.graphics.Image;

/**
 * @author Bob Brodt
 *
 */
public class JavaMemberTreeNode extends TreeNode {

	/**
	 * @param modelObject
	 * @param isCondensed
	 */
	public JavaMemberTreeNode(Object modelObject, boolean isCondensed) {
		super(modelObject, isCondensed);
	}

	@Override
	public String getLabel() {
		Member member = (Member)modelObject;
		String label = "";
		String name = member.getName();
//		int mod = member.getModifiers();
//		if ((mod & Modifier.PUBLIC)!=0)
//			label += "public ";
//		if ((mod & Modifier.PROTECTED)!=0)
//			label += "protected ";
//		if ((mod & Modifier.PRIVATE)!=0)
//			label += "private ";
//		if ((mod & Modifier.STATIC)!=0)
//			label += "static ";
		
		if (member instanceof Field) {
			Field f = (Field)member;
			Type t = f.getGenericType();
			if (t instanceof Class)
				label += ((Class)t).getSimpleName() + " ";
			else
				label += t.toString() + " ";
		}
		if (member instanceof Method) {
			Method m = (Method)member;
			label += m.getReturnType().getSimpleName() + " ";
		}
		label += name;
		if (member instanceof Method) {
			Method m = (Method)member;
			if (m.getParameterTypes().length>0) {
				label += "(";
				Class[] p = m.getParameterTypes();
				for (int i=0; i<p.length; ++i) {
					label += p[i].getSimpleName();
					if (i+1<p.length)
						label += ",";
				}
				label += ")";
			}
			else
				label += "()";
		}

		return label;
	}

	@Override
	public Image getImage() {
		if (modelObject instanceof Method)
			return Activator.getDefault().getImage(IConstants.ICON_JAVA_PUBLIC_METHOD_16);
		if (modelObject instanceof Field)
			return Activator.getDefault().getImage(IConstants.ICON_JAVA_PUBLIC_FIELD_16);
		return null;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Object[] getChildren() {
		return null;
	}
}
