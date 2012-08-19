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


package org.eclipse.bpmn2.modeler.ui.property.events;


import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite.AbstractPropertiesProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class CommonEventDetailComposite extends DefaultDetailComposite {

	protected AbstractListComposite inputTable;
	protected AbstractListComposite outputTable;
	protected EventDefinitionsListComposite eventsTable;

	public CommonEventDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public CommonEventDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public void cleanBindings() {
		super.cleanBindings();
		inputTable = null;
		outputTable = null;
		eventsTable = null;
	}

	@Override
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (propertiesProvider==null) {
			propertiesProvider = new AbstractPropertiesProvider(object) {
				String[] properties = new String[] {
						"isInterrupting",
						"parallelMultiple",
						"cancelActivity",
						"eventDefinitions",
						"dataInputs",
						"dataOutputs",
						"properties"
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
	protected AbstractListComposite bindList(EObject object, EStructuralFeature feature, EClass listItemClass) {
		if (object instanceof CatchEvent || object instanceof ThrowEvent) {
			if ("eventDefinitions".equals(feature.getName())) {
				eventsTable = new EventDefinitionsListComposite(this, (Event)object);
				eventsTable.bindList(object, feature);
				eventsTable.setTitle("Event Definitions");
				return eventsTable;
			}
			if ("dataInputs".equals(feature.getName())) {
				if (object instanceof ThrowEvent) {
					ThrowEvent throwEvent = (ThrowEvent)object;
					inputTable = new DataInputsListComposite(this, throwEvent);
					inputTable.bindList(object, feature);
					inputTable.setTitle("Input Parameters");
					return inputTable;
				}
			}
			if ("dataOutputs".equals(feature.getName())) {
				if (object instanceof CatchEvent) {
					CatchEvent catchEvent = (CatchEvent)object;
					outputTable = new DataOutputsListComposite(this, catchEvent);
					outputTable.bindList(catchEvent, feature);
					outputTable.setTitle("Output Parameters");
					return outputTable;
				}
			}
			return null;
		}
		return super.bindList(object, feature, listItemClass);
	}
	
}