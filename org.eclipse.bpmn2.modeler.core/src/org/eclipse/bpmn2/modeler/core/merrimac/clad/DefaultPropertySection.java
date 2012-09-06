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
package org.eclipse.bpmn2.modeler.core.merrimac.clad;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite.AbstractPropertiesProvider;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;

public class DefaultPropertySection extends AbstractBpmn2PropertySection {

	protected AbstractPropertiesProvider propertiesProvider = null;
	protected Class appliesToClass = null;

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection#createSectionRoot()
	 */
	@Override
	protected AbstractDetailComposite createSectionRoot() {
		DefaultDetailComposite composite = new DefaultDetailComposite(this);
		composite.setPropertiesProvider(propertiesProvider);
		return composite;
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		DefaultDetailComposite composite = new DefaultDetailComposite(parent, style);
		composite.setPropertiesProvider(propertiesProvider);
		return composite;
	}

	public void setProperties(String[] properties) {
		propertiesProvider = new AbstractPropertiesProvider(null) {
			String[] properties = null;
			@Override
			public String[] getProperties() {
				// TODO Auto-generated method stub
				return properties;
			}
			
			public void setProperties(String[] properties) {
				this.properties = properties;
			}
		};
		propertiesProvider.setProperties(properties);
	}
	
	@Override
	public boolean appliesTo(IWorkbenchPart part, ISelection selection) {
//		BPMN2Editor editor = (BPMN2Editor)part;
//		EObject object = getBusinessObjectForSelection();
//		DefaultDetailComposite composite = new DefaultDetailComposite();
//		AbstractPropertiesProvider provider = composite.getPropertiesProvider(object);
//		ModelEnablementDescriptor modelEnablement = editor.getTargetRuntime().getModelEnablements(object);
//		String className = object.eClass().getName();
//		if (modelEnablement.isEnabled(className)) {
//			String[] properties = provider.getProperties();
//			for (String featureName : properties) {
//				if (modelEnablement.isEnabled(className, featureName))
//					return true;
//			}
//		}
//		return false;
		if (appliesToClass==null)
			return super.appliesTo(part, selection);
		
		PictogramElement pe = BusinessObjectUtil.getPictogramElementForSelection(selection);
		// this is a special hack to allow selection of connection decorator labels:
		// the connection decorator does not have a business object linked to it,
		// but its parent (the connection) does.
		if (pe.getLink()==null && pe.eContainer() instanceof PictogramElement)
			pe = (PictogramElement)pe.eContainer();

		// check all linked BusinessObjects for a match
		if (pe.getLink()!=null) {
			for (EObject eObj : pe.getLink().getBusinessObjects()){
				if (appliesToClass.isInstance(eObj)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void setAppliesTo(Class appliesToClass) {
		this.appliesToClass = appliesToClass;
	}
}
