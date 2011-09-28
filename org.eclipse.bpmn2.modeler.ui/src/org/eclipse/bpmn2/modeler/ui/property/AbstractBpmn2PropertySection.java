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

import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public abstract class AbstractBpmn2PropertySection extends GFPropertySection {
	
	protected AbstractBpmn2PropertiesComposite composite;
	protected TabbedPropertySheetPage tabbedPropertySheetPage;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		parent.setLayout(new FillLayout());
		this.tabbedPropertySheetPage = aTabbedPropertySheetPage;
	}

	/**
	 * The subclass must provide the parent Composite it created in createControls()
	 * so that we can pass the PropertySheetPage down to the Composite. This is
	 * useful for allowing the Composite to resize itself based on the parent's size.
	 * 
	 * @return
	 */
	protected abstract AbstractBpmn2PropertiesComposite getComposite();
	
	@Override
	public void refresh() {
		Composite composite = (Composite)tabbedPropertySheetPage.getControl();
		composite.layout(true);
		tabbedPropertySheetPage.resizeScrolledComposite();
	}

	@Override
	public boolean shouldUseExtraSpace() {
		return true;
	}

}
