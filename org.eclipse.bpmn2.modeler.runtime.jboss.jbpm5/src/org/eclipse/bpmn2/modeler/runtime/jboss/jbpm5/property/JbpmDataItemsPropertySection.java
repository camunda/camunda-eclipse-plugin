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

import org.eclipse.bpmn2.Property;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.GlobalType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ImportType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelFactory;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.util.JbpmModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.DataItemsPropertySection;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.PropertyListComposite;
import org.eclipse.bpmn2.modeler.ui.property.dialogs.SchemaImportDialog;
import org.eclipse.bpmn2.modeler.ui.property.editors.ComboObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
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
		PropertiesCompositeFactory.register(Property.class, JbpmPropertyListComposite.class);
	}

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new JbpmDataItemsPropertiesComposite(this);
	}

	public class GlobalTypePropertiesComposite extends DefaultDetailComposite {

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
							final ImportType value = newImport;
							TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
							domain.getCommandStack().execute(new RecordingCommand(domain) {
								@Override
								protected void doExecute() {
									JbpmModelUtil.addImport(object);
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
	
	public class JbpmPropertyListComposite extends PropertyListComposite {

		public JbpmPropertyListComposite(AbstractBpmn2PropertySection section) {
			super(section);
			// TODO Auto-generated constructor stub
		}

		public JbpmPropertyListComposite(Composite parent, int style) {
			super(parent, style);
			// TODO Auto-generated constructor stub
		}

		public JbpmPropertyListComposite(Composite parent) {
			super(parent);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected EObject addListItem(EObject object, EStructuralFeature feature) {
			Property prop  = (Property)super.addListItem(object, feature);
			String label = PropertyUtil.getLongDisplayName(prop.eContainer());
			prop.setId( prop.getName() );
			prop.setName(label + " Variable");
			return prop;
		}
	}
}
