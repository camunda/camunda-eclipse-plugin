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

import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.importer.ModelImport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * 
 * @author Nico Rehwaldt
 * @author Daniel Meyer
 */
public class EventShapeHandler extends FlowNodeShapeHandler {

	public EventShapeHandler(ModelImport bpmn2ModelImport) {
		super(bpmn2ModelImport);
	}
	
	@Override
	public PictogramElement handleShape(FlowNode bpmnElement, BPMNShape shape, ContainerShape container) {
	  Event event = (Event) bpmnElement;
	  List<EventDefinition> eventDefinitions = null;
	  
	  if (event instanceof CatchEvent) {
	    eventDefinitions = ((CatchEvent) event).getEventDefinitions();
	  }else if (event instanceof ThrowEvent){
	    eventDefinitions = ((ThrowEvent) event).getEventDefinitions();
	  }
	  else {
	    throw new RuntimeException("Impossible to handle event");
	  }
	  ContainerShape eventContainer = (ContainerShape) super.handleShape(bpmnElement, shape, container);
	  
	  if (eventContainer!= null && eventDefinitions.size() > 0) {
	    AddContext addDefinitionContext = createAddContext(eventDefinitions.get(0));
	    addDefinitionContext.setTargetContainer(eventContainer);
	    featureProvider.getAddFeature(addDefinitionContext).execute(addDefinitionContext);
	  }
	  
	  return eventContainer;
	}
	
  @Override
  protected void setSize(AddContext context, BPMNShape shape, FlowNode bpmnElement, ContainerShape targetContainer) {
    super.setSize(context, shape, bpmnElement, targetContainer);
    GraphicsUtil.setEventSize(context.getWidth(), context.getHeight(), getDiagram(targetContainer));
  }

}
