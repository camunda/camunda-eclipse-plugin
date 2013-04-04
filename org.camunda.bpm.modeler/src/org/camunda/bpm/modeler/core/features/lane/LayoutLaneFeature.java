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
 * @author Ivar Meikas
 ******************************************************************************/
package org.camunda.bpm.modeler.core.features.lane;

import org.camunda.bpm.modeler.core.features.participant.LayoutLaneSetFeature;
import org.eclipse.graphiti.features.IFeatureProvider;

/**
 * Layout feature for lanes
 * 
 * @author nico.rehwaldt
 */
public class LayoutLaneFeature extends LayoutLaneSetFeature {

	public LayoutLaneFeature(IFeatureProvider fp) {
		super(fp);
	}
}
