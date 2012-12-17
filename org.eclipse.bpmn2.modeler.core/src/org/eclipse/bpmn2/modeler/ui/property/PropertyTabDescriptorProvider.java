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

import static org.eclipse.bpmn2.modeler.core.utils.SelectionUtil.getSelectedBusinessObject;
import static org.eclipse.bpmn2.modeler.core.utils.SelectionUtil.getSelectedModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.modeler.core.property.AbstractTabSection;
import org.eclipse.bpmn2.modeler.core.property.SectionDescriptor;
import org.eclipse.bpmn2.modeler.core.property.TabDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.DefinitionsTabSection;
import org.eclipse.bpmn2.modeler.ui.property.tabs.EventTabSection;
import org.eclipse.bpmn2.modeler.ui.property.tabs.GeneralTabSection;
import org.eclipse.bpmn2.modeler.ui.property.tabs.MultiInstanceTabSection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ISectionDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider;

public class PropertyTabDescriptorProvider implements ITabDescriptorProvider {

	@Override
	public ITabDescriptor[] getTabDescriptors(IWorkbenchPart part, ISelection selection) {
		List<ITabDescriptor> tabs = new ArrayList<ITabDescriptor>();
		
		Object model = getSelectedModel(selection);
		EObject businessObject = getSelectedBusinessObject(model);
		
		if (businessObject != null) {
			tabs.add(createGeneralTabDescriptor());
			
			if (businessObject instanceof Event) {
				tabs.add(createEventTabDescriptor());
			}
			
			// add definitions tab for changes
			if (model instanceof Diagram) {
				tabs.add(createDefinitionsTabDescriptor());
			}
			
			if (businessObject instanceof Activity) {
				tabs.add(createMultiInstanceTabDescriptor());
			}
		}
		
		return tabs.toArray(new ITabDescriptor[]{});
	}
	
	private ITabDescriptor createTabDescriptor(String id, String name, AbstractTabSection tabSection) {

		TabDescriptor tabDescriptor = new TabDescriptor(id, name, name);
		ISectionDescriptor sectionDescriptor = new SectionDescriptor(id + ".section", tabSection);
		tabDescriptor.setSectionDescriptors(Arrays.asList(new ISectionDescriptor[] { sectionDescriptor }));
		
		return tabDescriptor;
	}

	private ITabDescriptor createMultiInstanceTabDescriptor() {
		return createTabDescriptor("multiInstanceTab", "Multi Instance", new MultiInstanceTabSection());
	}
	
	private ITabDescriptor createEventTabDescriptor() {
		return createTabDescriptor("eventTab", "Event", new EventTabSection());
	}

	private ITabDescriptor createGeneralTabDescriptor() {
		return createTabDescriptor("generalTab", "General", new GeneralTabSection());
	}

	private ITabDescriptor createDefinitionsTabDescriptor() {
		return createTabDescriptor("definitionsTab", "Definitions", new DefinitionsTabSection());
	}

}
