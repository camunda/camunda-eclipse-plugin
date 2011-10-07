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

package org.eclipse.bpmn2.modeler.ui.property;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpmn2.modeler.core.runtime.Bpmn2SectionDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;

/**
 * This class maintains the registry of PropertySheetPage Composite widgets for each
 * BPMN2 metamodel type. This ensures that the same Property Section layout is used
 * for each model object regardless of where the Composite is embedded. This happens,
 * for example, in the Advanced Property Section which displays a customized details
 * section depending on the currently selected object type. 
 * 
 * @author Bob Brodt
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PropertiesCompositeRegistry {

	protected final static Map<Class,Class> registry = new HashMap<Class,Class>();
	
	public static void register(Class eClass, Class composite) {
		registry.put(eClass, composite);
		
		// make sure the constructors are declared
		try {
			Constructor ctor = null;
			Class ec = composite.getEnclosingClass();
			if (ec!=null) {
				ctor = composite.getConstructor(ec,AbstractBpmn2PropertySection.class);
				ctor = composite.getConstructor(ec,Composite.class,int.class);
			}
			else {
				ctor = composite.getConstructor(AbstractBpmn2PropertySection.class);
				ctor = composite.getConstructor(Composite.class,int.class);
			}
		} catch (Exception e) {
			Activator.logError(e);
		}
	}
	
	public static Class unregister(EClass eClass) {
		if (registry.containsKey(eClass))
			return registry.remove(eClass);
		return null;
	}
	
	public static Class findCompositeClass(Class eClass) {
		while (eClass!=null) {
			if (registry.containsKey(eClass)) {
				return registry.get(eClass);
			}
			for (Class iface : eClass.getInterfaces()) {
				if (registry.containsKey(iface)) {
					return registry.get(iface);
				}
			}
			// if this is an interface, it won't have a super class,
			// so check all implemented interfaces
			if (eClass.isInterface()) {
				for (Class iface : eClass.getInterfaces()) {
					Class composite = findCompositeClass(iface);
					if (composite!=null)
						return composite;
				}
			}
			eClass = eClass.getSuperclass();
		}
		return null;
	}
		
	public static AbstractBpmn2PropertiesComposite createComposite(Class eClass, AbstractBpmn2PropertySection section) {
		AbstractBpmn2PropertiesComposite composite = null;
		System.out.println("createComposit");
		Class clazz = findCompositeClass(eClass);
		if (clazz!=null) {
			try {
				Constructor ctor = null;
				// allow the composite to be declared in an enclosing class
				Class ec = clazz.getEnclosingClass();
				if (ec!=null) {
					ctor = clazz.getConstructor(ec,AbstractBpmn2PropertySection.class);
					composite = (AbstractBpmn2PropertiesComposite) ctor.newInstance(null,section);
				}
				else {
					ctor = clazz.getConstructor(AbstractBpmn2PropertySection.class);
					composite = (AbstractBpmn2PropertiesComposite) ctor.newInstance(section);
				}
			} catch (Exception e) {
				logError(eClass,e);
				composite = new DefaultPropertiesComposite(section);
			}
			
		}
		return composite;
	}
	
	public static AbstractBpmn2PropertiesComposite createComposite(Class eClass, Composite parent, int style) {
		AbstractBpmn2PropertiesComposite composite = null;
		Class clazz = findCompositeClass(eClass);
		if (clazz!=null) {
			try {
				Constructor ctor = null;
				// allow the composite to be declared in an enclosing class
				Class ec = clazz.getEnclosingClass();
				if (ec!=null) {
					ctor = clazz.getConstructor(ec,Composite.class,int.class);
					composite = (AbstractBpmn2PropertiesComposite) ctor.newInstance(null,parent,style);
				}
				else {
					ctor = clazz.getConstructor(Composite.class,int.class);
					composite = (AbstractBpmn2PropertiesComposite) ctor.newInstance(parent,style);
				}
			} catch (Exception e) {
				logError(eClass,e);
				composite = new DefaultPropertiesComposite(parent,style);
			}
			
		}
		return composite;
	}
	
	private static void logError(Class eClass, Exception e) {
		Activator.logError(e);
		MessageDialog.openError(Display.getDefault().getActiveShell(), "Internal Error",
				"No property sheet has been registered for the object type:\n\n"+
				eClass+"\n\nCause: "+
				e+"\n\n"+
				"Using the default property sheet instead.");
	}

}
