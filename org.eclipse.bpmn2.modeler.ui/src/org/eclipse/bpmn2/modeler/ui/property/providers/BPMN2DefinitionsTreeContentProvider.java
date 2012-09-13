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

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Interface;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.ui.util.ListMap;


/**
 * Provides a tree of model objects representing some expansion of the underlying graph
 * of model objects whose roots are the Variables of a Process. 
 */
public class BPMN2DefinitionsTreeContentProvider extends ModelTreeContentProvider {

	boolean isPropertyTree;
	private CompositeContentProvider fContentProvider;	
	
	
	public BPMN2DefinitionsTreeContentProvider( boolean isCondensed ) {
		super(isCondensed);
		
		fContentProvider = new CompositeContentProvider ();
		fContentProvider.add ( new BPMN2InterfaceContentProvider() );
		fContentProvider.add ( new BPMN2ProcessContentProvider() );	
		
	}

	public boolean isPropertyTree() { return isPropertyTree; }

	
	
	@Override
	public Object[] primGetElements (Object inputElement) {
		
		ITreeNode result = getTreeNode ( inputElement );
		if (result != null) {
			return new Object[] { result } ;
		}
		
		
		if (inputElement instanceof Definitions) {									
			
			return (Object[]) ListMap.Map ( 
					fContentProvider.getElements( inputElement ) ,						
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
		
		if (inputElement instanceof Interface) {
			return new BPMN2InterfaceTreeNode ((Interface) inputElement,isCondensed);
		}
		
		if (inputElement instanceof Process) {
			return new BPMN2ProcessTreeNode ((Process) inputElement,isCondensed);
		}

		return null;
		
	}
}