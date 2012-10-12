/******************************************************************************* 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * camunda services GmbH - initial API and implementation 
 *
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.core.importer.handlers;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class ParticipantShapeHandler extends AbstractShapeHandler<Participant> {

	public ParticipantShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}
	
	@Override
	protected void setLocation(AddContext context, ContainerShape container, BPMNShape shape) {
		
		Bounds bounds = shape.getBounds();
		int x = (int) bounds.getX();
		int y = (int) bounds.getY();
		context.setLocation(x, y);
		
		FeatureSupport.setHorizontal(context, shape.isIsHorizontal());
		
	}
	
	
}
