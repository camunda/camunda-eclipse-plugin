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

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.importer.Bpmn2ModelImport;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * 
 * @author Nico Rehwaldt
 */
public class AssociationShapeHandler extends AbstractEdgeHandler<Association> {

	public AssociationShapeHandler(Bpmn2ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}

	@Override
	protected PictogramElement handleEdge(Association bpmnElement, BPMNEdge edge, ContainerShape container) {

		BaseElement source = bpmnElement.getSourceRef();
		BaseElement target = bpmnElement.getTargetRef();
		
		PictogramElement sourcePictogram = getPictogramElement(source);
		PictogramElement targetPictogram = getPictogramElement(target);
		
		Connection connection = createConnectionAndSetBendpoints(edge, sourcePictogram, targetPictogram);
		return connection;
	}
}
