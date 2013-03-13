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
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil.Size;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class SubProcessShapeHandler extends AbstractShapeHandler<SubProcess> {

	public SubProcessShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}

	@Override
	protected void setSize(AddContext context, BPMNShape shape, SubProcess bpmnElement, ContainerShape container) {
		
		if (!shape.isIsExpanded()) {
			
			Size activitySize = GraphicsUtil.getActivitySize(null);
			int width = activitySize.getWidth();
			int height = activitySize.getHeight();
			
			context.setSize(width, height);
			
		} else {
			super.setSize(context, shape, bpmnElement, container);
		}
		
	}

}
