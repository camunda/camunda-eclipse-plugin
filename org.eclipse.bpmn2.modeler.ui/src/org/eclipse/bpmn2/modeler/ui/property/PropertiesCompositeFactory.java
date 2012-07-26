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
import java.util.Hashtable;

import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

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
public class PropertiesCompositeFactory {

	protected final static Hashtable<TargetRuntime, Hashtable<Class,Class>> detailRegistry = new Hashtable<TargetRuntime,Hashtable<Class,Class>>();
	protected final static Hashtable<TargetRuntime, Hashtable<Class,Class>> listRegistry = new Hashtable<TargetRuntime,Hashtable<Class,Class>>();

	public enum CompositeType {
		DETAIL,
		LIST
	};
	
	public static void register(Class eClass, Class composite) {
		TargetRuntime rt = TargetRuntime.getCurrentRuntime();
		Hashtable<Class,Class> map = AbstractBpmn2TableComposite.class.isAssignableFrom(composite) ?
				listRegistry.get(rt) :
				detailRegistry.get(rt);
		if (map==null) {
			map = new Hashtable<Class,Class>();
			if (AbstractBpmn2TableComposite.class.isAssignableFrom(composite))
				listRegistry.put(rt,map);
			else
				detailRegistry.put(rt,map);
		}
		map.put(eClass, composite);
		
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
	
//	public static Class unregister(TargetRuntime rt, EClass eClass) {
//		if (detailRegistry.containsKey(rt)) {
//			Hashtable<Class,Class> map = detailRegistry.get(rt);
//			return map.remove(eClass);
//		}
//		return null;
//	}
	
	////////////////////////////////////////////////////////////////////////////////
	// Detail Composite methods
	////////////////////////////////////////////////////////////////////////////////
	public static Class findDetailCompositeClass(Class eClass) {
		TargetRuntime rt = TargetRuntime.getCurrentRuntime();
		Class composite = findCompositeClass(detailRegistry.get(rt),eClass);
		if (composite==null && rt!=TargetRuntime.getDefaultRuntime()) {
			// fall back to default target runtime
			rt = TargetRuntime.getDefaultRuntime();
			composite = findCompositeClass(detailRegistry.get(rt),eClass);
		}
		return composite;
	}

	public static AbstractBpmn2PropertiesComposite createDetailComposite(Class eClass, AbstractBpmn2PropertySection section) {
		return createDetailComposite(eClass,section,true);
	}
	
	public static AbstractBpmn2PropertiesComposite createDetailComposite(Class eClass, AbstractBpmn2PropertySection section, boolean useDefault) {
		Class clazz = findDetailCompositeClass(eClass);
		return (AbstractBpmn2PropertiesComposite)createComposite(clazz, eClass, section, useDefault, CompositeType.DETAIL);
	}
	
	public static AbstractBpmn2PropertiesComposite createDetailComposite(Class eClass, Composite parent, int style) {
		return createDetailComposite(eClass,parent,style,true);
	}
	
	public static AbstractBpmn2PropertiesComposite createDetailComposite(Class eClass, Composite parent, int style, boolean useDefault) {
		Class clazz = findDetailCompositeClass(eClass);
		return (AbstractBpmn2PropertiesComposite)createComposite(clazz, eClass, parent, style, useDefault, CompositeType.DETAIL);
	}

	////////////////////////////////////////////////////////////////////////////////
	// List Composite methods
	////////////////////////////////////////////////////////////////////////////////
	public static Class findListCompositeClass(Class eClass) {
		TargetRuntime rt = TargetRuntime.getCurrentRuntime();
		Class composite = findCompositeClass(listRegistry.get(rt),eClass);
		if (composite==null && rt!=TargetRuntime.getDefaultRuntime()) {
			// fall back to default target runtime
			rt = TargetRuntime.getDefaultRuntime();
			composite = findCompositeClass(listRegistry.get(rt),eClass);
		}
		return composite;
	}

	public static AbstractBpmn2TableComposite createListComposite(Class eClass, AbstractBpmn2PropertySection section) {
		return createListComposite(eClass,section,true);
	}
	
	public static AbstractBpmn2TableComposite createListComposite(Class eClass, AbstractBpmn2PropertySection section, boolean useDefault) {
		Class clazz = findListCompositeClass(eClass);
		return (AbstractBpmn2TableComposite)createComposite(clazz, eClass, section, useDefault, CompositeType.LIST);
	}
	
	public static AbstractBpmn2TableComposite createListComposite(Class eClass, Composite parent, int style) {
		return createListComposite(eClass,parent,style,true);
	}
	
	public static AbstractBpmn2TableComposite createListComposite(Class eClass, Composite parent, int style, boolean useDefault) {
		Class clazz = findListCompositeClass(eClass);
		return (AbstractBpmn2TableComposite)createComposite(clazz, eClass, parent, style, useDefault, CompositeType.LIST);
	}
	
	////////////////////////////////////////////////////////////////////////////////
	// Common
	////////////////////////////////////////////////////////////////////////////////
	private static Class findCompositeClass(Hashtable<Class,Class> map, Class eClass) {
		if (map!=null) {
			while (eClass!=null && eClass!=EObjectImpl.class) {
				if (map.containsKey(eClass)) {
					return map.get(eClass);
				}
				for (Class iface : eClass.getInterfaces()) {
					if (map.containsKey(iface)) {
						return map.get(iface);
					}
				}
				// if this is an interface, it won't have a super class,
				// so check all implemented interfaces
				if (eClass.isInterface()) {
					for (Class iface : eClass.getInterfaces()) {
						Class composite = findCompositeClass(map,iface);
						if (composite!=null)
							return composite;
					}
				}
				eClass = eClass.getSuperclass();
			}
		}
		return null;
	}

	private static Composite createComposite(Class clazz, Class eClass, AbstractBpmn2PropertySection section, boolean useDefault, CompositeType type) {
		Composite composite = null;
		if (clazz!=null) {
			try {
				Constructor ctor = null;
				// allow the composite to be declared in an enclosing class
				Class ec = clazz.getEnclosingClass();
				if (ec!=null) {
					ctor = clazz.getConstructor(ec,AbstractBpmn2PropertySection.class);
					composite = (Composite) ctor.newInstance(null,section);
				}
				else {
					ctor = clazz.getConstructor(AbstractBpmn2PropertySection.class);
					composite = (Composite) ctor.newInstance(section);
				}
			} catch (Exception e) {
				if (useDefault)
					logError(eClass,e);
			}
			
		}
		
		if (composite==null && useDefault)
			composite = createDefaultComposite(type, section);

		if (!useDefault && isDefaultComposite(composite))
			composite = null;
		
		return composite;
	}
	
	private static Composite createComposite(Class clazz, Class eClass, Composite parent, int style, boolean useDefault, CompositeType type) {
		Composite composite = null;
		if (clazz!=null) {
			try {
				Constructor ctor = null;
				// allow the composite to be declared in an enclosing class
				Class ec = clazz.getEnclosingClass();
				if (ec!=null) {
					ctor = clazz.getConstructor(ec,Composite.class,int.class);
					composite = (Composite) ctor.newInstance(null,parent,style);
				}
				else {
					ctor = clazz.getConstructor(Composite.class,int.class);
					composite = (Composite) ctor.newInstance(parent,style);
				}
			} catch (Exception e) {
				if (useDefault)
					logError(eClass,e);
			}
			
		}
		
		if (composite==null && useDefault)
			composite = createDefaultComposite(type, parent, style);

		if (!useDefault && isDefaultComposite(composite))
			composite = null;
		
		// set a default layout data
		if (composite!=null) {
			if (parent.getLayout() instanceof GridLayout) {
				GridLayout layout = (GridLayout)parent.getLayout(); 
				composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, layout.numColumns, 1));
			}
		}
		
		return composite;
	}
	
	private static Composite createDefaultComposite(CompositeType type, AbstractBpmn2PropertySection section) {
		if (type==CompositeType.DETAIL)
			return new DefaultPropertiesComposite(section);
		return new AbstractBpmn2TableComposite(section);
	}

	private static Composite createDefaultComposite(CompositeType type, Composite parent, int style) {
		if (type==CompositeType.DETAIL)
			return new DefaultPropertiesComposite(parent,style);
		return new AbstractBpmn2TableComposite(parent,style);
	}
	
	private static boolean isDefaultComposite(Composite composite) {
		return composite.getClass() == DefaultPropertiesComposite.class
				|| composite.getClass() == AbstractBpmn2TableComposite.class;
	}
	
	private static void logError(Class eClass, Exception e) {
		Activator.logError(e);
		MessageDialog.openError(Display.getDefault().getActiveShell(), "Internal Error",
				"The property sheet for the object type:\n\n"+
				eClass+"\n\nhas not been defined or is not visible."+
				"\n\nCause: "+
				e+"\n\n"+
				"Using the default property sheet instead.");
	}

}
