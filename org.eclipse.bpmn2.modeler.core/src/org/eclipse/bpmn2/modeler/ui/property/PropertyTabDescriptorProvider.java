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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.runtime.Bpmn2SectionDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.Bpmn2TabDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.GeneralTabSection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ISectionDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider;

public class PropertyTabDescriptorProvider implements ITabDescriptorProvider {

	public PropertyTabDescriptorProvider() {
		super();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ITabDescriptor[] getTabDescriptors(IWorkbenchPart part, ISelection selection) {
		ISectionDescriptor sectionDescriptor = new Bpmn2SectionDescriptor("id.section", new GeneralTabSection());
		Bpmn2TabDescriptor generalTabDescriptor = new Bpmn2TabDescriptor("id", "General", "General");
		
		generalTabDescriptor.setSectionDescriptors(Arrays.asList(new ISectionDescriptor[] { sectionDescriptor }));

		List<ITabDescriptor> tabs = new ArrayList<ITabDescriptor>();
		tabs.add(generalTabDescriptor);
		
		return tabs.toArray(new ITabDescriptor[]{});
	}

}
