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
import java.util.Stack;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BpmnDiPackage;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateConnectionFeature;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.core.preferences.ToolEnablementPreferences;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNFeatureProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.forms.widgets.Section;

public class DefaultPropertiesComposite extends AbstractBpmn2PropertiesComposite {

	protected final String[] EMPTY_STRING_ARRAY = new String[] {};
	private AbstractItemProvider itemProvider = null;
	
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
	
	public void setItemProvider(AbstractItemProvider provider) {
		itemProvider = provider;
	}
	
	public AbstractItemProvider getItemProvider(EObject object) {
		if (itemProvider==null) {
			itemProvider = new AbstractItemProvider(object) {
				public String[] getAttributes() {
					List<String> list = new ArrayList<String>();
					for (EStructuralFeature attribute : be.eClass().getEAllStructuralFeatures()) {
						if (attribute instanceof EAttribute) {
							list.add(attribute.getName());
						}
					}
					String a[] = new String[list.size()];
					list.toArray(a);
					return a;
				}

				@Override
				public String[] getLists() {
					List<String> list = new ArrayList<String>();
					for (EStructuralFeature feature : be.eClass().getEAllStructuralFeatures()) {
						if (be.eGet(feature) instanceof EList) {
							list.add(feature.getName());
						}
					}
					String a[] = new String[list.size()];
					list.toArray(a);
					return a;
				}

				@Override
				public String[] getReferences() {
					List<String> list = new ArrayList<String>();;
					for (EReference reference : be.eClass().getEAllReferences()) {
						if (!(be.eGet(reference) instanceof EList)) {
							list.add(reference.getName());
						}
					}
					String a[] = new String[list.size()];
					list.toArray(a);
					return a;
				}
			};
		}
		return itemProvider;
	}
	
	@Override
	public void createBindings(EObject be) {
		if (getItemProvider(be)==null) {
			String tab = propertySection.tabbedPropertySheetPage.getSelectedTab().getLabel();
			createLabel(this,"No "+tab+" Properties for this "+ModelUtil.getObjectDisplayName(be));
			return;
		}
		
		if (getItemProvider(be).getAttributes()!=null) {
			getAttributesParent();
			EStructuralFeature feature;
			for (String a : getItemProvider(be).getAttributes()) {
				feature = getAttributeFeature(be,a);
				if (feature!=null) {
					bindAttribute(getAttributesParent(), be,(EAttribute)feature);
					continue;
				}
				feature = getListFeature(be,a);
				if (feature!=null) {
					bindList(be,feature);
					continue;
				}
				feature = getReferenceFeature(be,a);
				if (feature!=null) {
					bindReference(getAttributesParent(), be,(EReference)feature);
					getReferencesParent();
					referencesSection.setVisible(false);
					continue;
				}
			}
		}
		
		if (getDiagramEditor().getPreferences().getShowAdvancedPropertiesTab() == false ||
				propertySection instanceof AdvancedPropertySection ||
				getItemProvider(be).alwaysShowAdvancedProperties())
		{
			if (getItemProvider(be).getLists()!=null) {
				for (String a : getItemProvider(be).getLists()) {
					bindList(be, a);
				}
			}
			
			if (getItemProvider(be).getReferences()!=null) {
				this.getReferencesParent();
				for (String a : getItemProvider(be).getReferences()) {
					bindReference(be, a);
				}
			}
		}
		
		if (getItemProvider(be).getChildren(null)!=null) {
			for (String a : getItemProvider(be).getChildren(null)) {
				bindChild(be,a);
			}
		}
	}

	public class ChildObjectStack {
		private Stack<EObject> objects;
		private Stack<Composite> attributesComposites;
		private Stack<Section> attributesSections;
		private Stack<Composite> referencesComposites;
		private Stack<Section> referencesSections;
		
		public void push(EObject object) {
			objects.push(object);
			
			attributesComposites.push(attributesComposite);
			attributesComposite = null;
			attributesSections.push(attributesSection);
			attributesSections = null;

			referencesComposites.push(referencesComposite);
			referencesComposite = null;
			referencesSections.push(referencesSection);
			referencesSections = null;
		}
		
		public EObject pop() {
			if (objects.size()>0) {
				attributesComposite = attributesComposites.pop();
				referencesSection = referencesSections.pop();
				
				return objects.pop();
			}
			return null;
		}
		
		protected Composite getAttributesParent() {
			if (objects.size()>0) {
				return DefaultPropertiesComposite.this;
			}
			return attributesComposites.peek();
		}
		
		protected Composite getReferencesParent() {
			if (objects.size()>0) {
				return DefaultPropertiesComposite.this;
			}
			return referencesComposites.peek();
		}
	}

	/**
	 * Provider class for the Default Properties sheet tab.
	 * This simply returns a list of attributes, containment ELists and references
	 * to be rendered on the Default Properties tab. If the DefaultPropertiesComposite
	 * is subclassed and the client does not specify an item provider, the default
	 * behavior is to render all structural features for the business object.
	 */
	public abstract class AbstractItemProvider {
		
		EObject be;
		
		public AbstractItemProvider(EObject object) {
			be = object;
		}
		
		public boolean alwaysShowAdvancedProperties() {
			return false;
		}

		public abstract String[] getAttributes();
		public String[] getLists() {
			return null;
		}
		
		public String[] getReferences() {
			return null;
		}

		public String[] getChildren(String name) {
			return null;
		}
	}
}
