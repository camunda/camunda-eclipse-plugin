package org.camunda.bpm.modeler.core.importer.handlers;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;


/**
 * @author roman.smirnov
 */
public class GatewayShapeHandler extends FlowNodeShapeHandler {

  public GatewayShapeHandler(ModelImport bpmn2ModelImport) {
    super(bpmn2ModelImport);
  }
  
  @Override
  protected void setSize(AddContext context, BPMNShape shape, FlowNode bpmnElement, ContainerShape targetContainer) {
    super.setSize(context, shape, bpmnElement, targetContainer);
    GraphicsUtil.setGatewaySize(context.getWidth(), context.getHeight(), getDiagram(targetContainer));
  }

}
