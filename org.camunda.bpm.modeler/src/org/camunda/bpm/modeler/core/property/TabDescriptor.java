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
package org.camunda.bpm.modeler.core.property;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.tabbed.AbstractTabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ISectionDescriptor;

public class TabDescriptor extends AbstractTabDescriptor {

	protected String id;
	protected String category;
	protected String label;
	protected Image image = null;
	
	public TabDescriptor(String id, String category, String label, List<ISectionDescriptor> sectionDescriptors) {
		this(id , category, label);
		
		setSectionDescriptors(sectionDescriptors);
	}
	
	public TabDescriptor(String id, String category, String label) {
		this.id = id;
		
		if (category==null || category.isEmpty() ) {
			category = "BPMN2";
		}
		
		this.category = category;
		this.label = label;
	}
	
	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
	@Override
	public Image getImage() {
		if (image == null)
			return super.getImage();
		
		return image;
	}
}