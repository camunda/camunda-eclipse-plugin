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

import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class TaskShapeHandler extends FlowNodeShapeHandler {

	public TaskShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}
	
	@Override
	protected void setSize(AddContext context, BPMNShape shape, FlowNode bpmnElement, ContainerShape targetContainer) {
		
		super.setSize(context, shape, bpmnElement, targetContainer);
		
		// remember the bounds of a created task, in order to draw new tasks in the same size
		if(targetContainer instanceof Diagram) {
			Diagram diagram = (Diagram) targetContainer;
			
			int width = context.getWidth();
			int height = context.getHeight();
			
			GraphicsUtil.setActivitySize(width, height, diagram);
		}
		
	}

}
