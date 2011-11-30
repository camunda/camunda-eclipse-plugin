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
package org.eclipse.bpmn2.modeler.core.utils;

import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class PropertyUtil {

	public static String deCamelCase(String string) {
		return string.replaceAll("([A-Z][a-z])", " $0").substring(1);
	}

	public static void disposeChildWidgets(Composite parent) {
		Control[] kids = parent.getChildren();
		for (Control k : kids) {
			if (k instanceof Composite) {
				disposeChildWidgets((Composite)k);
			}
			k.dispose();
		}
	}

	public static void layoutAllParents(Composite child) {
		Composite parent = child;
		while (parent!=null && parent.getParent() instanceof Composite) {
			parent = parent.getParent(); 
			parent.layout();
		}
	}
	
	public static EditPart getEditPartForSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection &&
				((IStructuredSelection) selection).isEmpty()==false) {
		
			Object firstElement = ((IStructuredSelection) selection).getFirstElement();
			EditPart editPart = null;
			if (firstElement instanceof EditPart) {
				editPart = (EditPart) firstElement;
			} else if (firstElement instanceof IAdaptable) {
				editPart = (EditPart) ((IAdaptable) firstElement).getAdapter(EditPart.class);
			}
			return editPart;
		}
		return null;
	}
	
	public static PictogramElement getPictogramElementForSelection(ISelection selection) {
		EditPart editPart = getEditPartForSelection(selection);
		if (editPart != null && editPart.getModel() instanceof PictogramElement) {
			return (PictogramElement) editPart.getModel();
		}
		return null;
	}
	
	public static EObject getBusinessObjectForSelection(ISelection selection) {
		PictogramElement pe = getPictogramElementForSelection(selection);
		if (pe!=null)
			return BusinessObjectUtil.getFirstElementOfType(pe, EObject.class);
		return null;
	}

	public static EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		if (pe!=null) {
			Object be = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			if (be instanceof EObject)
				return (EObject) be;
		}
		return null;
	}

	// Debugging utilities for widget trees.
	public static void check(Control control) {
		String name = control.getClass().getSimpleName();
		if (control.isDisposed()) {
			System.err.println(name+" disposed!");
			return;
		}
		if (control instanceof Composite) {
			((Composite)control).layout(true);
		}
		control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point sz = control.getSize();
		if (sz.x==0 || sz.y==0)
			System.err.println(name+" zero size!");
	}

	public static void dump(Composite parent, String comment) {
		System.out.println(comment);
		int i = 1;
		System.out.println("parent="+parent.hashCode());
		check(parent);

		Composite p = parent.getParent();
		while (p!=null) {
			check(p);
			p = p.getParent();
			++i;
		}
		dump(parent,0);
	}
	
	public static void dump(Composite parent, int indent) {
		Control[] kids = parent.getChildren();
		for (Control k : kids) {
			for (int i=0; i<indent; ++i)
				System.out.print("|");
			System.out.print(" "+k);
			check(k);
			
			if (k instanceof Label) {
				System.out.print(((Label)k).getText());
			}
			System.out.println("");
			if (k instanceof Composite) {
				dump((Composite)k, indent+1);
			}
		}
	}
}
