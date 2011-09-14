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
import java.util.List;

import org.eclipse.bpmn2.modeler.core.runtime.Bpmn2SectionDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.Bpmn2TabDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider;

public class PropertyTabDescriptorProvider implements ITabDescriptorProvider {

	public PropertyTabDescriptorProvider() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public ITabDescriptor[] getTabDescriptors(IWorkbenchPart part,
			ISelection selection) {
		
		TargetRuntime rt = TargetRuntime.getDefaultRuntime();
		if (part instanceof BPMN2Editor) {
			rt = ((BPMN2Editor)part).getTargetRuntime();
		}
		List<Bpmn2TabDescriptor> desc = rt.getTabDescriptors();
		ArrayList<Bpmn2TabDescriptor> replaced = new ArrayList<Bpmn2TabDescriptor>();
		
		for (Bpmn2TabDescriptor d : desc) {
			String replacedId = d.getReplaceTab(); 
			if (replacedId!=null) {
				boolean empty = true;
				for (Bpmn2SectionDescriptor s : (List<Bpmn2SectionDescriptor>) d.getSectionDescriptors()) {
					if (s.appliesTo(part, selection)) {
						empty = false;
						break;
					}
				}
				if (!empty) {
					// replace the tab whose ID is specified as "replaceTab" in this tab.
					Bpmn2TabDescriptor replacedTab = rt.getTabDescriptor(replacedId);
					if (replacedTab!=null)
						replaced.add(replacedTab);
				}
			}
		}
		if (replaced.size()>0)
			desc.removeAll(replaced);
		
		return desc.toArray(new ITabDescriptor[desc.size()]);
	}

}
