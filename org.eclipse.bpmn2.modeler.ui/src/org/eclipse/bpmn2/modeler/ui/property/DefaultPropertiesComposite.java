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

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BpmnDiPackage;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateConnectionFeature;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.core.preferences.ToolEnablementPreferences;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNFeatureProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
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

	private AbstractItemProvider itemProvider = null;
	
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DefaultPropertiesComposite(Composite parent, int style) {
		super(parent, style);
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
						list.add(reference.getName());
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
		
		for (String a : getItemProvider(be).getAttributes()) {
			bindAttribute(be,a);
		}
		
		for (String a : getItemProvider(be).getLists()) {
			bindList(be, a);
		}

		for (String a : getItemProvider(be).getReferences()) {
			bindReference(be, a);
		}
		
		itemProviderAdapter.dispose();
	}
	
	public abstract class AbstractItemProvider {
		
		EObject be;
		
		public AbstractItemProvider(EObject object) {
			be = object;
		}
		
		public abstract String[] getAttributes();
		public abstract String[] getLists();
		public abstract String[] getReferences();
		
	}
}
