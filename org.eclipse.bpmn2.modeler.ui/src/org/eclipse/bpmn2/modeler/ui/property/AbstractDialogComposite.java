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
package org.eclipse.bpmn2.modeler.ui.property;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractDialogComposite extends Composite {
	
	protected Class eclass;
	
	public AbstractDialogComposite(Composite parent, int style) {
		super(parent,style);
		this.eclass = eclass;
	}
	
	public abstract EClass getBusinessObjectClass();
	public abstract void setBusinessObject(EObject newObject);
	public abstract Composite getControl();
	public abstract void aboutToOpen();
}
