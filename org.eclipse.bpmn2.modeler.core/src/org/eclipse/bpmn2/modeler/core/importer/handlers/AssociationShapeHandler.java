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
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.importer.ImportException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * 
 * @author Nico Rehwaldt
 */
public class AssociationShapeHandler extends AbstractEdgeHandler<Association> {

	public AssociationShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}

	@Override
	protected PictogramElement handleEdge(Association bpmnElement, BPMNEdge edge, ContainerShape container) {

		BaseElement source = bpmnElement.getSourceRef();
		BaseElement target = bpmnElement.getTargetRef();
		
		String errorFormat = "%s reference of %s is null. Edge is not visible (%s)";
		
		if (target == null) {
			modelImport.logSilently(new ImportException(
					String.format(errorFormat,
						"Target",
						edge.getBpmnElement().eClass().getName(),
						edge.getBpmnElement().getId())
			));
			return null;
		}
		
		if (source == null) {
			modelImport.logSilently(new ImportException(
					String.format(errorFormat,
						"Source",
						edge.getBpmnElement().eClass().getName(),
						edge.getBpmnElement().getId())
			));
			return null;
		}
		
		PictogramElement sourcePictogram = getPictogramElement(source);
		PictogramElement targetPictogram = getPictogramElement(target);
		
		Connection connection = createConnectionAndSetBendpoints(edge, sourcePictogram, targetPictogram);
		return connection;
	}
}
