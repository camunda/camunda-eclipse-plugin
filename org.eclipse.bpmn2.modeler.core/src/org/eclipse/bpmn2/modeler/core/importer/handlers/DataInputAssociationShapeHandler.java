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

import java.util.List;

import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.importer.InvalidContentException;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.importer.UnmappedElementException;
import org.eclipse.bpmn2.modeler.core.importer.UnsupportedFeatureException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * 
 * @author Nico Rehwaldt
 */
public class DataInputAssociationShapeHandler extends DataAssociationShapeHandler<DataInputAssociation> {

	public DataInputAssociationShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}
	
	@Override
	protected PictogramElement handleEdge(DataInputAssociation bpmnElement, BPMNEdge edge, ContainerShape container) {

		// Data Association allows connections for multiple starting points, we don't support it yet
		List<ItemAwareElement> sourceRefs = bpmnElement.getSourceRef();
		
		if (sourceRefs == null || sourceRefs.isEmpty()) {
			modelImport.logAndThrow(new InvalidContentException("No source references specified", bpmnElement));
		}
		
		if (sourceRefs.size() > 1) {
			modelImport.log(new UnsupportedFeatureException("Multiple source references", bpmnElement));
		}
		
		ItemAwareElement sourceRef = sourceRefs.get(0);
		
		// we do not employ targetRef as it opens a whole host of new issues
		// refer to spec 
		// * Figure 10.50 - ItemAware class diagram
		// * Figure 10.57 - InputOutputSpecification class diagram
		
		// instead, we log an error if someone wants to use it
		ItemAwareElement targetRef = bpmnElement.getTargetRef();

		if (targetRef != null) {
			modelImport.log(new UnsupportedFeatureException("Target reference not supported", bpmnElement));
		}
		
		PictogramElement sourcePictogram = resolvePictogramElement((EObject) sourceRef);

		// we use the bpmn elements container (task, event...) as the target
		PictogramElement targetPictogram = getPictogramElement(bpmnElement.eContainer());
		
		if (sourcePictogram == null) {
			modelImport.logAndThrow(new UnmappedElementException("Could not resolve", sourceRef));
		}
		
		Connection connection = createConnectionAndSetBendpoints(edge, sourcePictogram, targetPictogram);
		return connection;
	}
}
