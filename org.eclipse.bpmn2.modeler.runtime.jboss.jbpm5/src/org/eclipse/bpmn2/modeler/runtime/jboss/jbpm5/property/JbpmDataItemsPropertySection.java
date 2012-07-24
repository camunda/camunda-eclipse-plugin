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

import org.eclipse.bpmn2.DataStore;
import org.eclipse.bpmn2.modeler.core.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.GlobalType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ImportType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelFactory;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.DataItemsPropertySection;
import org.eclipse.bpmn2.modeler.ui.property.dialogs.SchemaImportDialog;
import org.eclipse.bpmn2.modeler.ui.property.editors.ComboObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class JbpmDataItemsPropertySection extends DataItemsPropertySection {

	static {
		// register the DataStorePropertiesComposite for rendering DataStore objects
		PropertiesCompositeFactory.register(GlobalType.class, GlobalTypePropertiesComposite.class);
	}

	@Override
	protected AbstractBpmn2PropertiesComposite createSectionRoot() {
		return new JbpmDataItemsPropertiesComposite(this);
	}

	public class GlobalTypePropertiesComposite extends DefaultPropertiesComposite {

		public GlobalTypePropertiesComposite(Composite parent, int style) {
			super(parent, style);
		}

		public GlobalTypePropertiesComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}
		
		@Override
		protected void bindAttribute(Composite parent, EObject object, EAttribute attribute, String label) {
			if ("type".equals(attribute.getName())) {
				ObjectEditor editor = new ComboObjectEditor(this,object,attribute) {
					
					@Override
					protected boolean canCreateNew() {
						return true;
					}
					
					protected EObject createObject() {
						String name = null;
						ImportType newImport = null;
						SchemaImportDialog dialog = new SchemaImportDialog(getShell(), SchemaImportDialog.ALLOW_JAVA);
						if (dialog.open() == Window.OK) {
							Object result[] = dialog.getResult();
							if (result.length == 1 && result[0] instanceof Class) {
								name = ((Class)result[0]).getName();
							}
						}

						if (name!=null && !name.isEmpty()) {
							newImport = (ImportType)ModelFactory.eINSTANCE.createImportType();
							newImport.setName(name);
							final EStructuralFeature importFeature = ModelPackage.eINSTANCE.getDocumentRoot_ImportType();
							final EObject value = newImport;
							TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
							domain.getCommandStack().execute(new RecordingCommand(domain) {
								@Override
								protected void doExecute() {
									EObject parent = object.eContainer();
									while (parent!=null && !(parent instanceof org.eclipse.bpmn2.Process))
										parent = parent.eContainer();
									ModelUtil.addExtensionAttributeValue(parent, importFeature, value);
								}
							});
						}
						return newImport;
					}
				};
				editor.createControl(parent,label);
			}
			else
				super.bindAttribute(parent, object, attribute, label);
		}
		
	}
}
