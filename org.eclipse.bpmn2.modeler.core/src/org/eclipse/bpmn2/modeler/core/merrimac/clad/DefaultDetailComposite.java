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


import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.impl.Bpmn2PackageImpl;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class DefaultDetailComposite extends AbstractDetailComposite {

	protected final String[] EMPTY_STRING_ARRAY = new String[] {};
	protected AbstractPropertiesProvider propertiesProvider = null;
	
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DefaultDetailComposite(Composite parent, int style) {
		super(parent,style);
	}
	
	public DefaultDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}
	
	public void setPropertiesProvider(AbstractPropertiesProvider provider) {
		propertiesProvider = provider;
	}
	
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (propertiesProvider==null) {
			final EObject o = object;
			return new AbstractPropertiesProvider(object) {
				public String[] getProperties() {
					List<String> list = new ArrayList<String>();
					EClass c = o.eClass();
					// add name and id attributes first (if any)
					if (c.getEStructuralFeature("name")!=null)
						list.add("name");
					if (c.getEStructuralFeature("id")!=null)
						list.add("id");
					for (EStructuralFeature attribute : o.eClass().getEStructuralFeatures()) {
						if (!list.contains(attribute.getName()))
							list.add(attribute.getName());
					}
					// add the anyAttributes
					List<EStructuralFeature> anyAttributes = ModelUtil.getAnyAttributes(o);
					for (EStructuralFeature f : anyAttributes) {
						if (f instanceof EAttribute && !list.contains(f.getName()))
							list.add(f.getName());
					}
					String a[] = new String[list.size()];
					list.toArray(a);
					return a;
				}
			};
		}
		return propertiesProvider;
	}
	
	protected Composite bindFeature(EObject be, EStructuralFeature feature, EClass eItemClass) {
		Composite composite = null;
		if (feature!=null) {
			if (isAttribute(be,feature)) {
				bindAttribute(getAttributesParent(), be,(EAttribute)feature);
			}
			else if (isList(be,feature)) {
				if (eItemClass==null)
					composite = bindList(be,feature);
				else
					composite = bindList(be,feature, eItemClass);
			}
			else if (isReference(be,feature)) {
				bindReference(getAttributesParent(), be,(EReference)feature);
			}
		}
		return composite;
	}
	
	/**
	 * "rootElements#Process.resources#HumanPerformer"
	 * @param be
	 * @param property
	 * @return
	 */
	protected Composite bindProperty(EObject be, String property) {
		Composite composite = null;
		String[] propArray = property.split("\\.");
		String prop0 = propArray[0];
		String[] featureAndClassArray = prop0.split("#");
		String featureName = featureAndClassArray[0];
		EStructuralFeature feature = getFeature(be,featureName);
		EClass eclass = null;
		if (featureAndClassArray.length>1) {
			String className = featureAndClassArray[1];
			eclass = (EClass)Bpmn2PackageImpl.eINSTANCE.getEClassifier(className);
			if (eclass==null)
				eclass = (EClass) getTargetRuntime().getModelDescriptor().getEPackage().getEClassifier(className);
			if (eclass!=null) {
				if (!isModelObjectEnabled(eclass))
					return null;
			}
		}
		
		// reconstruct the remainder of the property string (if any)
		property = "";
		for (int i=1; i<propArray.length; ++i) {
			if (!property.isEmpty())
				property += ".";
			property += propArray[i];
		}
		
		if (!property.isEmpty()) {
			// determine new object - may be a list
			if (eclass!=null) {
				Object value = be.eGet(feature);
				if (value instanceof EList) {
					for (Object o : (EList)value) {
						if (eclass.isInstance(o)) {
							propArray = property.split("[\\.#]");
							featureName = propArray[0];
							feature = getFeature((EObject)o,featureName);
							composite = bindProperty((EObject)o, property);
							if (composite instanceof AbstractListComposite) {
								((AbstractListComposite)composite).setTitle(
										ModelUtil.getLabel((EObject)o,feature)+
										" for "+
										ModelUtil.getLongDisplayName((EObject)o));
							}
						}
					}
				}
			}
			else if (feature!=null) {
				Object value = be.eGet(feature);
				if (value instanceof EList) {
					for (Object o : (EList)value) {
						if (o instanceof EObject) {
							bindProperty((EObject)o, property);
						}
					}
				}
				else if (value instanceof EObject) {
					composite = bindProperty(be, property);
				}
			}
		}
		else {
			composite = bindFeature(be, feature, eclass);
		}
		return composite;
	}
	
	@Override
	public void createBindings(EObject be) {
		AbstractPropertiesProvider provider = getPropertiesProvider(be); 
		if (provider==null) {
			createMissingPropertiesLabel(be);
			return;
		}
		
		String[] properties = provider.getProperties();
		if (properties!=null) {
			getAttributesParent();
			EStructuralFeature feature;
			for (String property : properties) {
				bindProperty(be,property);
			}
		}
		
		properties = provider.getChildren(null);
		if (properties!=null) {
			for (String a : properties) {
				bindChild(be,a);
			}
		}
		
		if (getAttributesParent().getChildren().length==0) {
			// yech! ugly hack to hide the Attributes TWISTIE section if it's empty
			if (attributesComposite!=null) {
				attributesComposite.dispose();
				attributesComposite = null;
				attributesSection.dispose();
				attributesSection = null;
			}
		}

		if (getChildren().length==0) {
			createMissingPropertiesLabel(be);
		}
		redrawPage();
	}
	
	private void createMissingPropertiesLabel(EObject be) {
		if (getDiagramEditor()!=null) {
			if (getPropertySection()!=null) {
				String tab = propertySection.tabbedPropertySheetPage.getSelectedTab().getLabel();
				createLabel(this,"No "+tab+" Properties for this "+ModelUtil.getDisplayName(be));
			}
		}
	}

	/**
	 * Provider class for the Default Properties sheet tab.
	 * This simply returns a list of properties, containment ELists and references
	 * to be rendered on the Default Properties tab. If the DefaultDetailComposite
	 * is subclassed and the client does not specify an item provider, the default
	 * behavior is to render all structural features for the business object.
	 */
	public abstract class AbstractPropertiesProvider {
		
		EObject be;
		
		public AbstractPropertiesProvider(EObject object) {
			be = object;
		}

		public abstract String[] getProperties();

		public String[] getChildren(String name) {
			return null;
		}
	}
}
