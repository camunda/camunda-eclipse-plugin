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

import java.util.List;

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

	@Override
	public ITabDescriptor[] getTabDescriptors(IWorkbenchPart part,
			ISelection selection) {
		
		TargetRuntime rt = TargetRuntime.getDefaultRuntime();
		if (part instanceof BPMN2Editor) {
			rt = ((BPMN2Editor)part).getTargetRuntime();
		}
		List<Bpmn2TabDescriptor> desc = rt.getTabDescriptors();
		return desc.toArray(new ITabDescriptor[desc.size()]);
	}

}
