package org.camunda.bpm.modeler.test.feature.add;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.point;
import static org.camunda.bpm.modeler.test.util.operations.AddCallActivityOperation.addCallActivity;
import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.test.feature.AbstractFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * 
 * @author Daniel Meyer
 *
 */
public class AddCallActivityFeatureTest extends AbstractFeatureTest {
  
  @Test
  @DiagramResource("org/camunda/bpm/modeler/test/feature/add/AddFlowElementFeatureTestBase.testBox.bpmn")
  public void testShapeNotExpanded() throws Exception {
    
    // given
    Shape subProcessShape = Util.findShapeByBusinessObjectId(diagram, "SubProcess_1");
    
    Point addPosition = point(10, 10);
    
    // when
    addCallActivity(getDiagramTypeProvider())      
      .atLocation(addPosition)
      .toContainer((ContainerShape) subProcessShape)      
      .execute();
    
    // then
    BPMNShape callActivity = (BPMNShape) Util.findBpmnShapeByBusinessObjectId(diagram, "CallActivity_1");
    
    assertThat(callActivity.isIsExpanded())
      .isFalse();
    
  }


}
