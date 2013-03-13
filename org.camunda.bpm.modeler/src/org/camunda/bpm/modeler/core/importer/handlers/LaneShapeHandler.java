/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.camunda.bpm.modeler.core.importer.handlers;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class LaneShapeHandler extends AbstractShapeHandler<Lane> {

	public LaneShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}
	
	@Override
	protected void setLocation(AddContext context, ContainerShape container, BPMNShape shape) {
		super.setLocation(context, container, shape);
		
		FeatureSupport.setHorizontal(context, shape.isIsHorizontal());
	}
}
