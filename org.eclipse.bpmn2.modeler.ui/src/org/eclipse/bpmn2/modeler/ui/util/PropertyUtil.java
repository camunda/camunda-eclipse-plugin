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
package org.eclipse.bpmn2.modeler.ui.util;

import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Point;
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

	/**
	 * Ugly hack to force layout of the entire widget tree of the property sheet page.
	 * @param parent
	 */
	public static void recursivelayout(Composite parent) {
		Control[] kids = parent.getChildren();
		for (Control k : kids) {
			if (k.isDisposed())
				Activator.logError(new SWTException("Widget is disposed."));
			if (k instanceof Composite) {
				recursivelayout((Composite)k);
				((Composite)k).layout(true);
			}
		}
		parent.layout(true);
	}


	public static String getObjectDisplayName(EObject obj) {
		String objName = null;
		if (obj instanceof BPMNDiagram) {
			Bpmn2DiagramType type = ModelUtil.getDiagramType((BPMNDiagram)obj); 
			if (type == Bpmn2DiagramType.CHOREOGRAPHY) {
				objName = "Choreography Diagram";
			}
			else if (type == Bpmn2DiagramType.COLLABORATION) {
				objName = "Collaboration Diagram";
			}
			else if (type == Bpmn2DiagramType.PROCESS) {
				objName = "Process Diagram";
			}
		}
		if (objName==null){
			objName = ModelUtil.toDisplayName( obj.eClass().getName() );
		}
		return objName;
	}

	public static String getDisplayName(EObject obj) {
		String objName = getObjectDisplayName(obj);
		EStructuralFeature feature = obj.eClass().getEStructuralFeature("name");
		if (feature!=null) {
			String name = (String)obj.eGet(feature);
			if (name==null || name.isEmpty())
				name = "Unnamed " + objName;
			else
				name = objName + " \"" + name + "\"";
			return name;
		}
		feature = obj.eClass().getEStructuralFeature("id");
		if (feature!=null) {
			if (obj.eGet(feature)!=null)
				objName = (String)obj.eGet(feature);
		}
		return objName;
	}
	
	public static String getDisplayName(EObject obj, EAttribute attr) {
		if (attr!=null) {
			ItemProviderAdapter itemProviderAdapter = (ItemProviderAdapter) new Bpmn2ItemProviderAdapterFactory()
					.adapt(obj, ItemProviderAdapter.class);
			if (itemProviderAdapter!=null) {
				IItemPropertyDescriptor propertyDescriptor = itemProviderAdapter.getPropertyDescriptor(obj,attr);
				itemProviderAdapter.dispose();
				if (propertyDescriptor!=null)
					return propertyDescriptor.getDisplayName(attr);
			}
			
			// There are no property descriptors available for this EObject -
			// this is probably because the "edit" plugin was not generated for
			// the EMF model, or is not available.
			// Use the class name to synthesize a display name
			obj = attr;
		}
		
		String className = obj.eClass().getName();
		className = className.replaceAll("Impl$", "");
		return ModelUtil.toDisplayName(className);
	}
}
