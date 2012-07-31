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

package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.property;


import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.GlobalType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelFactory;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite;
import org.eclipse.bpmn2.modeler.ui.property.ExtensionValueListComposite;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.DataItemsDetailComposite;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class JbpmDataItemsDetailComposite extends DataItemsDetailComposite {

	/**
	 * @param section
	 */
	public JbpmDataItemsDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public JbpmDataItemsDetailComposite(Composite parent, int style) {
		super(parent, style);
	}
	

	@Override
	public void createBindings(EObject be) {
		if (be instanceof Definitions) {
			Definitions definitions = (Definitions)be;
			for (RootElement re : definitions.getRootElements()) {
				if (re instanceof Process) {
					Process process = (Process)re;
					ExtensionValueListComposite globalsTable = new ExtensionValueListComposite(
							this, AbstractListComposite.DEFAULT_STYLE)
					{
						
						@Override
						protected EObject addListItem(EObject object, EStructuralFeature feature) {
							// generate a unique global variable name
							String base = "globalVar";
							int suffix = 1;
							String name = base + suffix;
							for (;;) {
								boolean found = false;
								for (Object g : ModelUtil.getAllExtensionAttributeValues(object, GlobalType.class)) {
									if (name.equals(((GlobalType)g).getIdentifier()) ) {
										found = true;
										break;
									}
								}
								if (!found)
									break;
								name = base + ++suffix;
							}
							
							GlobalType newGlobal = (GlobalType)ModelFactory.eINSTANCE.create(listItemClass);
							newGlobal.setIdentifier(name);
							addExtensionValue(newGlobal);
							return newGlobal;
						}
					};
					globalsTable.bindList(process, ModelPackage.eINSTANCE.getDocumentRoot_Global());
					globalsTable.setTitle("Globals for "+PropertyUtil.getLongDisplayName(process));

//					bindList(process, "properties");
//					bindList(process, "resources");
				}
			}
		}
		super.createBindings(be);
	}
}
