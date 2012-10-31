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

import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.importer.ImportException;
import org.eclipse.bpmn2.modeler.core.importer.InvalidContentException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class MessageFlowShapeHandler extends AbstractEdgeHandler<MessageFlow> {

	public MessageFlowShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}

	@Override
	protected PictogramElement handleEdge(MessageFlow bpmnElement, BPMNEdge edge, ContainerShape container) {
		
		InteractionNode source;
		InteractionNode target;
		
		try {
			source = bpmnElement.getSourceRef();
			
		} catch (ClassCastException e) {
			modelImport.log(new InvalidContentException("Invalid source referenced, not displaying message flow", bpmnElement));
			return null;
		}

		try {
			target = bpmnElement.getTargetRef();
		} catch (ClassCastException e) {
			modelImport.log(new InvalidContentException("Invalid target referenced, not displaying message flow", bpmnElement));
			return null;
		}
		
		PictogramElement sourcePictogram = getPictogramElement(source);
		PictogramElement targetPictogram = getPictogramElement(target);
		
		if (source != null && target!= null && sourcePictogram != null && targetPictogram != null) {
			Connection connection = createConnectionAndSetBendpoints(edge, sourcePictogram, targetPictogram);
			return connection;
		}else {
			modelImport.log(new ImportException("Source or target invalid", edge));
			return null;
		}

	}
}
