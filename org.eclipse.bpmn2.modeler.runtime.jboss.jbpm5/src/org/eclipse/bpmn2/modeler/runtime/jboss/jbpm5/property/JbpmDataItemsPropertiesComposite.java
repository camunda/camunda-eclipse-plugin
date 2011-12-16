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
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.GlobalType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelFactory;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2TableComposite;
import org.eclipse.bpmn2.modeler.ui.property.ExtensionValueTableComposite;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.DataItemsPropertiesComposite;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class JbpmDataItemsPropertiesComposite extends DataItemsPropertiesComposite {

	/**
	 * @param section
	 */
	public JbpmDataItemsPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public JbpmDataItemsPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}
	

	@Override
	public void createBindings(EObject be) {
		if (be instanceof Definitions) {
			Definitions definitions = (Definitions)be;
			for (RootElement re : definitions.getRootElements()) {
				if (re instanceof Process) {
					Process process = (Process)re;
					ExtensionValueTableComposite globalsTable = new ExtensionValueTableComposite(
							this, AbstractBpmn2TableComposite.DEFAULT_STYLE)
					{
						
						@Override
						protected EObject addListItem(EObject object, EStructuralFeature feature) {
							InputDialog dialog = new InputDialog(getShell(), "Add Global",
									"Enter new global variable name","", new IInputValidator() {

										@Override
										public String isValid(String newText) {
											if (newText==null || newText.isEmpty() || newText.contains(" "))
												return "Please enter a valid name";
											return null;
										}
										
									});
							if (dialog.open()!=Window.OK){
								return null;
							}
							String name = dialog.getValue();
							
							GlobalType newGlobal = (GlobalType)ModelFactory.eINSTANCE.create(listItemClass);
							newGlobal.setIdentifier(name);
							addExtensionValue(newGlobal);
							return newGlobal;
						}
					};
					globalsTable.bindList(process, ModelPackage.eINSTANCE.getDocumentRoot_Global());
					globalsTable.setTitle("Globals");
				}
			}
		}
		super.createBindings(be);
	}
}
