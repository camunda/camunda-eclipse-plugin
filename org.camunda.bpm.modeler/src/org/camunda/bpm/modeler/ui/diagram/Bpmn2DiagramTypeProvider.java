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
 * @author Innar Made
 ******************************************************************************/
package org.camunda.bpm.modeler.ui.diagram;

import org.camunda.bpm.modeler.core.Activator;
import org.camunda.bpm.modeler.core.features.api.container.IFeatureContainer;
import org.camunda.bpm.modeler.plugin.ICustomTaskProvider;
import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;

public class Bpmn2DiagramTypeProvider extends AbstractDiagramTypeProvider {
	
	public static final String ID = Bpmn2DiagramTypeProvider.class.getName();
	
	private IToolBehaviorProvider[] toolBehaviorProviders;

	public Bpmn2DiagramTypeProvider() {
		super();

		initFeatureProvider();
	}
	
	@Override
	public boolean isAutoUpdateAtStartup() {

		// used to trigger automatic update upon startup
		// that we employ to perform a model import
		return true;
	}
	
	private void initFeatureProvider() {

		Bpmn2FeatureProvider featureProvider = new Bpmn2FeatureProvider(this);
		
		for (ICustomTaskProvider provider : Activator.getExtensions().getCustomTaskProviders()) {
			IFeatureContainer featureContainer = provider.getFeatureContainer();
			
			if (featureContainer != null) {
				featureProvider.addCustomFeatureContainer(featureContainer);
			}
		}
		
		setFeatureProvider(featureProvider);
	}

	@Override
	public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
		if (toolBehaviorProviders == null) {
			toolBehaviorProviders = new IToolBehaviorProvider[] { new Bpmn2ToolBehaviour(this) };
		}
		return toolBehaviorProviders;
	}
}
