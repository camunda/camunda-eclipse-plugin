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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.modeler.core.property.SectionDescriptor;
import org.eclipse.bpmn2.modeler.core.property.TabDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.EventTabSection;
import org.eclipse.bpmn2.modeler.ui.property.tabs.GeneralTabSection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ISectionDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider;

public class PropertyTabDescriptorProvider implements ITabDescriptorProvider {

	@Override
	@SuppressWarnings("unchecked")
	public ITabDescriptor[] getTabDescriptors(IWorkbenchPart part, ISelection selection) {
		List<ITabDescriptor> tabs = new ArrayList<ITabDescriptor>();
		
		EObject businessObject = getSelectedBusinessObject(selection);
		if (businessObject != null) {
			tabs.add(createGeneralTabDescriptor());
			
			if (businessObject instanceof Event) {
				tabs.add(createEventTabDescriptor());
			}
		}
		
		return tabs.toArray(new ITabDescriptor[]{});
	}

	private ITabDescriptor createEventTabDescriptor() {
		TabDescriptor tabDescriptor = new TabDescriptor("eventTab", "Event", "Event");
		ISectionDescriptor sectionDescriptor = new SectionDescriptor("eventTab.section", new EventTabSection());
		tabDescriptor.setSectionDescriptors(Arrays.asList(new ISectionDescriptor[] { sectionDescriptor }));
		
		return tabDescriptor;
	}

	private TabDescriptor createGeneralTabDescriptor() {
		TabDescriptor tabDescriptor = new TabDescriptor("generalTab", "General", "General");
		ISectionDescriptor sectionDescriptor = new SectionDescriptor("generalTab.section", new GeneralTabSection());
		
		tabDescriptor.setSectionDescriptors(Arrays.asList(new ISectionDescriptor[] { sectionDescriptor }));
		return tabDescriptor;
	}
}
