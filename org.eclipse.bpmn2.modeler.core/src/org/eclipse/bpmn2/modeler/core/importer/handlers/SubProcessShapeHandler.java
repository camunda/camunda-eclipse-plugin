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

import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil.Size;
import org.eclipse.graphiti.features.context.impl.AddContext;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 * 
 */
public class SubProcessShapeHandler extends AbstractShapeHandler<SubProcess> {

	public SubProcessShapeHandler(Bpmn2ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}

	protected void setLocation(AddContext context, BPMNShape shape) {
		// TODO Auto-generated method stub

	}

	protected void setTargetContainer(AddContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setSize(AddContext context, BPMNShape shape, SubProcess bpmnElement) {
		
		if (!shape.isIsExpanded()) {
			
			Size activitySize = GraphicsUtil.getActivitySize(null);
			int width = activitySize.getWidth();
			int height = activitySize.getHeight();
			
			context.setSize(width, height);
			
		} else {
			super.setSize(context, shape, bpmnElement);
		}
		
	}

}
