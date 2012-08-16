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

import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.Property;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.ListCompositeColumnProvider;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.TableColumn;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ComboObjectEditor;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.GlobalType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ImportType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelFactory;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.util.JbpmModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.data.ItemAwareElementDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.DataItemsPropertySection;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.ItemDefinitionListComposite;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.PropertyListComposite;
import org.eclipse.bpmn2.modeler.ui.property.dialogs.SchemaImportDialog;
import org.eclipse.bpmn2.modeler.ui.property.tasks.TaskDetailComposite;
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
		// register the DataStoreDetailComposite for rendering DataStore objects
		PropertiesCompositeFactory.register(GlobalType.class, GlobalTypeDetailComposite.class);
		PropertiesCompositeFactory.register(ItemDefinition.class, JbpmItemDefinitionDetailComposite.class);
		PropertiesCompositeFactory.register(Property.class, JbpmPropertyDetailComposite.class);
		PropertiesCompositeFactory.register(Property.class, JbpmPropertyListComposite.class);
	}

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new JbpmDataItemsDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new JbpmDataItemsDetailComposite(parent,style);
	}

	public class GlobalTypeDetailComposite extends DefaultDetailComposite {

		public GlobalTypeDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		public GlobalTypeDetailComposite(AbstractBpmn2PropertySection section) {
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
						String name = JbpmModelUtil.showImportDialog(object);
						return JbpmModelUtil.addImport(name, object);
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
		}

		public JbpmPropertyListComposite(Composite parent, int style) {
			super(parent, style);
		}

		public JbpmPropertyListComposite(Composite parent) {
			super(parent);
		}

		protected boolean isModelObjectEnabled(String className, String featureName) {
			if (featureName!=null && "id".equals(featureName))
					return true;
			return super.isModelObjectEnabled(className,featureName);
		}

		public ListCompositeColumnProvider getColumnProvider(EObject object, EStructuralFeature feature) {
			if (columnProvider==null) {
				columnProvider = new ListCompositeColumnProvider(this,true);
				columnProvider.add(object, PACKAGE.getProperty(), PACKAGE.getBaseElement_Id());
				columnProvider.add(object, PACKAGE.getProperty(), PACKAGE.getItemAwareElement_ItemSubjectRef());
			}
			return columnProvider;
		}
		
		@Override
		protected EObject addListItem(EObject object, EStructuralFeature feature) {
			Property prop  = (Property)super.addListItem(object, feature);
			String label = ModelUtil.getLongDisplayName(prop.eContainer());
			prop.setId( prop.getName() );
			prop.setName(null);
			return prop;
		}
	}
	
	public class JbpmPropertyDetailComposite extends ItemAwareElementDetailComposite {

		public JbpmPropertyDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public JbpmPropertyDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		protected boolean isModelObjectEnabled(String className, String featureName) {
			if (featureName!=null && "id".equals(featureName))
					return true;
			return super.isModelObjectEnabled(className,featureName);
		}
	}
}
