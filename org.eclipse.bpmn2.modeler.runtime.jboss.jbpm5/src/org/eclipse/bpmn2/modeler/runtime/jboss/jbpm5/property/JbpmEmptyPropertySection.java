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

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.TaskDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.tasks.TaskPropertySection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This is an empty tab section which simply exists to hide the "Basic" tab
 * defined the editor UI plugin.
 * 
 * @author Bob Brodt
 *
 */
public class JbpmEmptyPropertySection extends TaskPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return null;
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return null;
	}

	@Override
	public boolean appliesTo(IWorkbenchPart part, ISelection selection) {
		return false;
	}
	
	@Override
	public boolean doReplaceTab(String id, IWorkbenchPart part, ISelection selection) {
		return true;
	}
}
