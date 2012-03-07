/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.wid;

import java.util.HashMap;

/**
 * @author bfitzpat
 *
 */
public class WorkItemDefinitionImpl implements WorkItemDefinition {
	
	@Override
	public String toString() {
		return "WorkItemDefinitionImpl [widName=" + widName //$NON-NLS-1$
				+ ", widDisplayName=" + widDisplayName  //$NON-NLS-1$
				+ ", widIcon=" + widIcon //$NON-NLS-1$
				+ ", widCustomEditor=" + widCustomEditor //$NON-NLS-1$
				+ ", widEclipseCustomEditor=" + widEclipseCustomEditor //$NON-NLS-1$
				+ ", widParameters=" + widParameters //$NON-NLS-1$
				+ ", widResults=" + widResults //$NON-NLS-1$
				+ "]"; //$NON-NLS-1$
	}

	private String widName;
	private String widDisplayName;
	private String widIcon;
	private String widCustomEditor;
	private String widEclipseCustomEditor;
	private HashMap<String, String> widParameters;
	private HashMap<String, String> widResults;

	@Override
	public String getName() {
		return this.widName;
	}

	@Override
	public String getDisplayName() {
		return this.widDisplayName;
	}

	@Override
	public String getIcon() {
		return this.widIcon;
	}

	@Override
	public HashMap<String, String> getParameters() {
		if (this.widParameters == null) 
			this.widParameters = new HashMap<String, String>();
		return this.widParameters;
	}

	@Override
	public void setName(String name) {
		this.widName = name;
	}

	@Override
	public void setDispalyName(String displayName) {
		this.widDisplayName = displayName;
	}

	@Override
	public void setIcon(String iconPath) {
		this.widIcon = iconPath;
	}

	@Override
	public String getCustomEditor() {
		return this.widCustomEditor;
	}

	@Override
	public void setCustomEditor(String editor) {
		this.widCustomEditor = editor;
	}

	@Override
	public String getEclipseCustomEditor() {
		return this.widEclipseCustomEditor;
	}

	@Override
	public void setEclipseCustomEditor(String editor) {
		this.widEclipseCustomEditor = editor;
	}

	@Override
	public HashMap<String, String> getResults() {
		if (this.widResults == null) 
			this.widResults = new HashMap<String, String>();
		return this.widResults;
	}

}
