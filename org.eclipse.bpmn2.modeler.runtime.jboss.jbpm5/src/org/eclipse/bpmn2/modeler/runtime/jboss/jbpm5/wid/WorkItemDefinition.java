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
 * WorkItemDefinition simply captures the data we need from the *.wid/*.conf file
 * @author bfitzpat
 *
 */
public interface WorkItemDefinition {

	public String getName();
	public void setName ( String name );
	public String getDisplayName();
	public String getCustomEditor();
	public void setCustomEditor ( String editor );
	public void setDispalyName ( String displayName );
	public String getIcon();
	public void setIcon ( String iconPath );
	public HashMap<String, String> getParameters();
	
}
