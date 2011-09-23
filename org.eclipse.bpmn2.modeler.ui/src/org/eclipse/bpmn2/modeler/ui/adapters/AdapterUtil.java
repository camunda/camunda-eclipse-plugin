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

package org.eclipse.bpmn2.modeler.ui.adapters;

import org.eclipse.bpel.wsil.model.inspection.InspectionPackage;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.modeler.core.adapters.AdapterRegistry;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.xsd.XSDPackage;

/**
 * @author Bob Brodt
 *
 */
public class AdapterUtil {
	
	static {
//		AdapterRegistry.INSTANCE.registerAdapterFactory(
//				Bpmn2Package.eINSTANCE, Bpmn2UIAdapterFactory.getInstance());
		
		AdapterRegistry.INSTANCE.registerAdapterFactory(
			    WSDLPackage.eINSTANCE, Bpmn2WSDLAdapterFactory.getInstance());
		
		AdapterRegistry.INSTANCE.registerAdapterFactory(
			    XSDPackage.eINSTANCE, Bpmn2XSDAdapterFactory.getInstance());
	
//		AdapterRegistry.INSTANCE.registerAdapterFactory(
//			    UiextensionmodelPackage.eINSTANCE, BPELUIExtensionAdapterFactory.getInstance());
		
		AdapterRegistry.INSTANCE.registerAdapterFactory(
			    InspectionPackage.eINSTANCE, Bpmn2WSILAdapterFactory.getInstance() );
		
	}

	/**
	 * @param <T>
	 * @param target
	 * @param clazz
	 * @return the adapted interface or object
	 */
	
	@SuppressWarnings("unchecked")
	
	public static <T extends Object> T adapt ( Object target,  Class<T> clazz) {	
		return AdapterRegistry.INSTANCE.adapt(target, clazz);
	}
	
	
	
	/**
	 * This method tries the registered adapter factories one by one, returning
	 * the first non-null result it gets.  If none of the factories can adapt
	 * the result, it returns null.
	 * @param target target object 
	 * @param type type of the adapter to find
	 * @return the adapter for the target.
	 */
	
	public static Object adapt (Object target, Object type) {
		return AdapterRegistry.INSTANCE.adapt(target, type);
	}

	/**
	 * Create an adapter for the given target of the given type. 
	 * In addition, pass a context object to the adapter(s) of the target. 
	 * 
	 * The idea is that some adapters can be stateful and depend not only 
	 * on the objects that they wrap, but also on some other context that is needed
	 * to completely and correctly implement the interface for which the adaptor is
	 * needed.
	 * 
	 * Adapters that are stateless, should ignore any notifications sent to them.
	 *  
	 * @param target the target object
	 * @param type the type it wants to adapt to
	 * @param context the context object
	 * 
	 * @return the adapter
	 */
	public static Object adapt (Object target, Object type, Object context) {		
		return AdapterRegistry.INSTANCE.adapt(target, type,context);
	}
}
