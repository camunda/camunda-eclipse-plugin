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
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class FlowNodeShapeHandler extends AbstractShapeHandler<FlowNode> {

	public FlowNodeShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}
	
  protected Diagram getDiagram(Shape shape) {
    if (shape instanceof Diagram) {
      return (Diagram) shape;
    }
    ContainerShape parent = shape.getContainer();
    if (parent != null) {
      return getDiagram(parent);
    }
    return null;
  }
	
	

}
