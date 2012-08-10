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

package org.eclipse.bpmn2.modeler.ui.property.tasks;

import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.GlobalBusinessRuleTask;
import org.eclipse.bpmn2.GlobalManualTask;
import org.eclipse.bpmn2.GlobalScriptTask;
import org.eclipse.bpmn2.GlobalTask;
import org.eclipse.bpmn2.GlobalUserTask;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.PropertiesCompositeFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;

/**
 * @author Bob Brodt
 *
 */
public class CallActivityPropertySection extends AbstractBpmn2PropertySection implements ITabbedPropertyConstants {
	static {
		PropertiesCompositeFactory.register(CallActivity.class, CallActivityDetailComposite.class);
		PropertiesCompositeFactory.register(GlobalTask.class, GlobalTaskDetailComposite.class);
		PropertiesCompositeFactory.register(GlobalBusinessRuleTask.class, GlobalTaskDetailComposite.class);
		PropertiesCompositeFactory.register(GlobalManualTask.class, GlobalTaskDetailComposite.class);
		PropertiesCompositeFactory.register(GlobalScriptTask.class, GlobalTaskDetailComposite.class);
		PropertiesCompositeFactory.register(GlobalUserTask.class, GlobalTaskDetailComposite.class);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection#createSectionRoot()
	 */
	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new CallActivityDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new CallActivityDetailComposite(parent,style);
	}

	@Override
	protected EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		EObject be = super.getBusinessObjectForPictogramElement(pe);
		if (be instanceof CallActivity)
			return be;
		return null;
	}
	
	public class GlobalTaskDetailComposite extends DefaultDetailComposite {

		public GlobalTaskDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		public GlobalTaskDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		@Override
		public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
			if (propertiesProvider==null) {
				propertiesProvider = new AbstractPropertiesProvider(object) {
					String[] properties = new String[] {
							"id", // BaseElement
							"name", // CallableElement
							"ioBinding", // CallableElement
							"ioSpecification",// CallableElement
							"supportedInterfaceRefs",// CallableElement
							"resources", // GlobalTask
							"implementation", // GlobalBusinessRuleTask & GlobalUserTask
							"script", // GlobalScriptTask
							"scriptLanguage", // GlobalScriptTask
							"renderings", // GlobalUserTask
					};
					
					@Override
					public String[] getProperties() {
						return properties; 
					}
				};
			}
			return propertiesProvider;
		}

	}
}
