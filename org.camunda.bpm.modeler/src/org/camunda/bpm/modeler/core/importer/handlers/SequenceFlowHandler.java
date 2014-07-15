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

import org.camunda.bpm.modeler.core.importer.ImportException;
import org.camunda.bpm.modeler.core.importer.InvalidContentException;
import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.runtime.engine.model.Expression;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class SequenceFlowHandler extends AbstractEdgeHandler<SequenceFlow> {

	public SequenceFlowHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}

	@Override
	protected PictogramElement handleEdge(SequenceFlow bpmnElement, BPMNEdge edge, ContainerShape container) {

		// replace conditionExpression field which is from type Expression through conditionExpression as FormalExpression when 
		// no xsi:type="tFormelExpression" is set
		if (bpmnElement.getConditionExpression() != null) {
			if (!(bpmnElement.getConditionExpression() instanceof FormalExpression)) {
				String conditionExpressionValue = ((Expression)bpmnElement.getConditionExpression()).getBody();
				bpmnElement.eUnset(Bpmn2Package.eINSTANCE.getSequenceFlow_ConditionExpression());
				FormalExpression formalExpression = Bpmn2Factory.eINSTANCE.createFormalExpression();
				formalExpression.setBody(conditionExpressionValue);
				bpmnElement.eSet(Bpmn2Package.eINSTANCE.getSequenceFlow_ConditionExpression(), formalExpression);
			}
		}

		FlowNode source = bpmnElement.getSourceRef();
		if (source == null) {
			InvalidContentException exception = new InvalidContentException("Could not resolve source", bpmnElement);
			modelImport.log(exception);
		}

		FlowNode target = bpmnElement.getTargetRef();
		if (target == null) {
			InvalidContentException exception = new InvalidContentException("Could not resolve target", bpmnElement);
			modelImport.log(exception);
		}
		
		PictogramElement sourcePictogram = getPictogramElement(source);
		PictogramElement targetPictogram = getPictogramElement(target);
		
		if (source != null && target != null && sourcePictogram != null && targetPictogram != null) {
			Connection connection = createConnectionAndSetBendpoints(edge, sourcePictogram, targetPictogram);
			return connection;	
		} else {
			modelImport.log(new ImportException("Source or target invalid", edge));
			return null;
		}
		
	}
}
