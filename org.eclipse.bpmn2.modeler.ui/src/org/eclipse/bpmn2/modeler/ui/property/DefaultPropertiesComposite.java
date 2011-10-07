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


import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.impl.Bpmn2PackageImpl;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class DefaultPropertiesComposite extends AbstractBpmn2PropertiesComposite {

	protected final String[] EMPTY_STRING_ARRAY = new String[] {};
	private AbstractPropertiesProvider propertiesProvider = null;
	
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DefaultPropertiesComposite(Composite parent, int style) {
		super(parent,style);
	}
	
	public DefaultPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}
	
	public void setPropertiesProvider(AbstractPropertiesProvider provider) {
		propertiesProvider = provider;
	}
	
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (propertiesProvider==null) {
			propertiesProvider = new AbstractPropertiesProvider(object) {
				public String[] getProperties() {
					List<String> list = new ArrayList<String>();
					for (EStructuralFeature attribute : be.eClass().getEStructuralFeatures()) {
						list.add(attribute.getName());
					}
					String a[] = new String[list.size()];
					list.toArray(a);
					return a;
				}
			};
		}
		return propertiesProvider;
	}
	
	@Override
	public void createBindings(EObject be) {
		if (getPropertiesProvider(be)==null) {
			String tab = propertySection.tabbedPropertySheetPage.getSelectedTab().getLabel();
			createLabel(this,"No "+tab+" Properties for this "+ModelUtil.getObjectDisplayName(be));
			return;
		}
		
		if (getPropertiesProvider(be).getProperties()!=null) {
			getAttributesParent();
			EStructuralFeature feature;
			for (String a : getPropertiesProvider(be).getProperties()) {
				EClass eItemClass = null;
				if (a.contains(".")) {
					String[] names = a.split("\\.");
					a = names[0];
					eItemClass = (EClass)Bpmn2PackageImpl.eINSTANCE.getEClassifier(names[1]);
				}
				feature = getAttributeFeature(be,a);
				if (feature!=null) {
					bindAttribute(getAttributesParent(), be,(EAttribute)feature);
					continue;
				}
				
				if (getDiagramEditor().getPreferences().getShowAdvancedPropertiesTab() == false ||
						propertySection instanceof AdvancedPropertySection ||
						getPropertiesProvider(be).alwaysShowAdvancedProperties())
				{
					feature = getListFeature(be,a);
					if (feature!=null) {
						bindList(be,feature, eItemClass);
						continue;
					}
					feature = getReferenceFeature(be,a);
					if (feature!=null) {
						bindReference(getAttributesParent(), be,(EReference)feature);
						continue;
					}
				}
			}
		}
		
		if (getPropertiesProvider(be).getChildren(null)!=null) {
			for (String a : getPropertiesProvider(be).getChildren(null)) {
				bindChild(be,a);
			}
		}
	}

	/**
	 * Provider class for the Default Properties sheet tab.
	 * This simply returns a list of properties, containment ELists and references
	 * to be rendered on the Default Properties tab. If the DefaultPropertiesComposite
	 * is subclassed and the client does not specify an item provider, the default
	 * behavior is to render all structural features for the business object.
	 */
	public abstract class AbstractPropertiesProvider {
		
		EObject be;
		
		public AbstractPropertiesProvider(EObject object) {
			be = object;
		}
		
		public boolean alwaysShowAdvancedProperties() {
			return false;
		}

		public abstract String[] getProperties();

		public String[] getChildren(String name) {
			return null;
		}
	}
}
