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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.swt.widgets.Composite;

public class ExtensionValueTableComposite extends AbstractBpmn2TableComposite {

	EStructuralFeature extensionValueFeature;

	/**
	 * @param parent
	 * @param style
	 */
	public ExtensionValueTableComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	public void bindList(EObject object, EStructuralFeature feature) {
		extensionValueFeature = feature;
		listItemClass = (EClass)feature.getEType();
		EStructuralFeature evf = object.eClass().getEStructuralFeature("extensionValues");
		super.bindList(object,evf);
	}
	
	@SuppressWarnings("unchecked")
	protected void addExtensionValue(EObject value) {
		EStructuralFeature evf = object.eClass().getEStructuralFeature("extensionValues");
		EList<EObject> list = (EList<EObject>)object.eGet(evf);

		ExtensionAttributeValue newItem = FACTORY.createExtensionAttributeValue();
		FeatureMap map = newItem.getValue();
		map.add(extensionValueFeature, value);
		list.add(newItem);
	}
	
	@Override
	public TableContentProvider getContentProvider(EObject object, EStructuralFeature feature, EList<EObject>list) {
		if (contentProvider==null) {
			contentProvider = new TableContentProvider(object, feature, list) {

				@Override
				public Object[] getElements(Object inputElement) {
					List<EObject> elements = new ArrayList<EObject>();
					for (EObject o : list) {
						ExtensionAttributeValue eav = (ExtensionAttributeValue)o;
						FeatureMap fm = eav.getValue();
						for (Entry e : fm) {
							EStructuralFeature sf = e.getEStructuralFeature();
							if (sf == extensionValueFeature)
								elements.add((EObject) e.getValue());
						}
					}
					return elements.toArray(new EObject[elements.size()]);
				}

			};
		}
		return contentProvider;
	}
}