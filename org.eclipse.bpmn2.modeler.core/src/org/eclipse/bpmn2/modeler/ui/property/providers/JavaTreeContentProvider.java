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

import java.lang.reflect.Member;
import java.util.List;

import org.eclipse.bpmn2.modeler.ui.util.ListMap;


/**
 * Provides a tree of model objects representing some expansion of the underlying graph
 * of model objects whose roots are the Variables of a Process. 
 */
public class JavaTreeContentProvider extends ModelTreeContentProvider {

	boolean isPropertyTree;
	private CompositeContentProvider fContentProvider;	
	
	
	public JavaTreeContentProvider( boolean isCondensed ) {
		super(isCondensed);
		
		fContentProvider = new CompositeContentProvider ();
		fContentProvider.add ( new JavaTypeContentProvider() );
		fContentProvider.add ( new JavaMemberContentProvider() );	
		
	}

	public boolean isPropertyTree() { return isPropertyTree; }

	
	
	@Override
	public Object[] primGetElements (Object inputElement) {
		
		ITreeNode result = getTreeNode ( inputElement );
		if (result != null) {
			return new Object[] { result } ;
		}
		
		if (inputElement instanceof List) {									
			List list = (List)inputElement;
			return (Object[]) ListMap.Map ( 
					fContentProvider.getElements( inputElement ), //list.toArray(),						
					new ListMap.Visitor () {		
						public Object visit (Object obj) {
							Object r = getTreeNode ( obj );
							return (r == null ? ListMap.IGNORE : r );
						}					
					},
					EMPTY_ARRAY );							
		}
		
		return EMPTY_ARRAY;
	}
	
	
	ITreeNode getTreeNode ( Object inputElement ) {
		
		if (inputElement instanceof Class) {
			return new JavaTypeTreeNode(inputElement,isCondensed);
		}
		else if (inputElement instanceof Member) {
			return new JavaMemberTreeNode(inputElement,isCondensed);
		}

		return null;
	}
}