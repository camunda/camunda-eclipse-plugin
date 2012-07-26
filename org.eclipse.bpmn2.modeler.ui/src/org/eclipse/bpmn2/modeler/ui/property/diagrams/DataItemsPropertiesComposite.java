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

package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.Property;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2TableComposite;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Bob Brodt
 *
 */
public class DataItemsPropertiesComposite extends DefaultPropertiesComposite {

	public DataItemsPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public DataItemsPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (propertiesProvider==null) {
			propertiesProvider = new AbstractPropertiesProvider(object) {
				String[] properties = new String[] {
						"rootElements.ItemDefinition",
						"rootElements.DataStore",
						"rootElements.Message",
						"rootElements.Error",
						"rootElements.Escalation",
						"rootElements.Signal",
						"rootElements.CorrelationProperty"
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
	public void createBindings(EObject be) {
		if (be instanceof Definitions) {
			Definitions definitions = (Definitions)be;
			for (RootElement re : definitions.getRootElements()) {
				if (re instanceof Process) {
					Process process = (Process)re;
					EStructuralFeature feature = process.eClass().getEStructuralFeature("properties");
					if (modelEnablement.isEnabled(process.eClass(), feature)) {
						PropertiesTable propertiesTable = new PropertiesTable(this); 
						propertiesTable.bindList(process, feature);
					}
					AbstractBpmn2TableComposite table = bindList(process, "resources");
					if (table!=null)
						table.setTitle("Resources for "+PropertyUtil.getDisplayName(process));
				}
			}
		}
		super.createBindings(be);
	}
	
	@Override
	protected AbstractBpmn2TableComposite bindList(EObject object, EStructuralFeature feature, EClass listItemClass) {
		if (listItemClass!=null && listItemClass.getName().equals("ItemDefinition")) {
			if (modelEnablement.isEnabled(object.eClass(), feature) || modelEnablement.isEnabled(listItemClass)) {
				AbstractBpmn2TableComposite table = super.bindList(object, feature, listItemClass);
				if (table!=null)
					table.setTitle("Data Types");
				return table;
			}
			return null;
		}
		else
			return super.bindList(object, feature, listItemClass);
	}

	public class PropertiesTable extends AbstractBpmn2TableComposite {

		public PropertiesTable(AbstractBpmn2PropertySection section) {
			super(section);
		}
		
		public PropertiesTable(Composite parent) {
			this(parent, DEFAULT_STYLE);
		}
		
		public PropertiesTable(Composite parent, int style) {
			super(parent,style);
		}
		
		@Override
		public void bindList(EObject theobject, EStructuralFeature thefeature) {
			// TODO Auto-generated method stub
			super.bindList(theobject, thefeature);
			setTitle("Variables for "+PropertyUtil.getDisplayName(theobject));
		}

		@Override
		protected EObject addListItem(EObject object, EStructuralFeature feature) {
			Process process = (Process)object;
			// generate a unique parameter name
			String base = "processVar";
			int suffix = 1;
			String name = base + suffix;
			for (;;) {
				boolean found = false;
				for (Property p : process.getProperties()) {
					if (name.equals(p.getName())) {
						found = true;
						break;
					}
				}
				if (!found)
					break;
				name = base + ++suffix;
			}
			Property prop  = (Property)super.addListItem(object, feature);
			prop.setName(name);
			return prop;
		}
	}

}
