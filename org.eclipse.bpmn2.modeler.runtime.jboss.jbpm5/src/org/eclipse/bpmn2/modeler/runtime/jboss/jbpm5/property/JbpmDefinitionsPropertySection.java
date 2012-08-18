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

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Message;
import org.eclipse.bpmn2.Property;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.ListCompositeColumnProvider;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.TableColumn;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite.AbstractPropertiesProvider;
import org.eclipse.bpmn2.modeler.ui.property.data.ItemAwareElementDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.MessageDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.data.MessageListComposite;
import org.eclipse.bpmn2.modeler.ui.property.diagrams.DefinitionsPropertySection;
import org.eclipse.bpmn2.modeler.ui.property.tasks.TaskDetailComposite;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class JbpmDefinitionsPropertySection extends DefinitionsPropertySection {
	static {
		PropertiesCompositeFactory.register(Message.class, JbpmMessageDetailComposite.class);
		PropertiesCompositeFactory.register(Message.class, JbpmMessageListComposite.class);
	}

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new JbpmDefinitionsPropertyComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new JbpmDefinitionsPropertyComposite(parent,style);
	}
	public class JbpmMessageDetailComposite extends DefaultDetailComposite {

		private AbstractPropertiesProvider propertiesProvider;

		public JbpmMessageDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		/**
		 * @param section
		 */
		public JbpmMessageDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		@Override
		public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
			if (propertiesProvider==null) {
				propertiesProvider = new AbstractPropertiesProvider(object) {
					String[] properties = new String[] {
							"id",
							"itemRef"
					};
					
					@Override
					public String[] getProperties() {
						return properties; 
					}
				};
			}
			return propertiesProvider;
		}
		
		@Override
		protected void bindAttribute(Composite parent, EObject object, EAttribute attribute, String label) {
			if ("id".equals(attribute.getName()))
				label = "Name";
			super.bindAttribute(parent, object, attribute, label);
		}
		
		@Override
		protected boolean isModelObjectEnabled(String className, String featureName) {
			return true;
		}
	}
	
	public class JbpmMessageListComposite extends DefaultListComposite {

		public JbpmMessageListComposite(AbstractBpmn2PropertySection section, int style) {
			super(section, style);
			// TODO Auto-generated constructor stub
		}

		public JbpmMessageListComposite(AbstractBpmn2PropertySection section) {
			super(section,
					AbstractListComposite.SHOW_DETAILS |
					AbstractListComposite.ADD_BUTTON |
					AbstractListComposite.MOVE_BUTTONS |
					AbstractListComposite.DELETE_BUTTON);
		}

		public JbpmMessageListComposite(Composite parent, int style) {
			super(parent,
					AbstractListComposite.SHOW_DETAILS |
					AbstractListComposite.ADD_BUTTON |
					AbstractListComposite.MOVE_BUTTONS |
					AbstractListComposite.DELETE_BUTTON);
		}
		
		@Override
		public ListCompositeColumnProvider getColumnProvider(EObject object, EStructuralFeature feature) {
			if (columnProvider==null) {
				columnProvider = new ListCompositeColumnProvider(this, false);
				TableColumn tc = new TableColumn(object,Bpmn2Package.eINSTANCE.getBaseElement_Id());
				tc.setHeaderText("Name"); 
				columnProvider.add(tc);
				columnProvider.add(new TableColumn(object,Bpmn2Package.eINSTANCE.getMessage_ItemRef()));
			}
			return columnProvider;
		}

		@Override
		protected boolean isModelObjectEnabled(String className, String featureName) {
			return true;
		}
	}

}
