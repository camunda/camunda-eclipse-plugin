package org.camunda.bpm.modeler.test.importer.scenarios;

import static org.fest.assertions.api.Assertions.assertThat;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.emf.ecore.EAttribute;
import org.junit.Test;

/**
 * 
 * @author Daniel Meyer
 * 
 */
public class NamespaceTest extends AbstractImportBpmnModelTest {
  
  @Test
  @DiagramResource
  public void testCamundaNsPrefixReadUpdateAttrClass() {
    ModelImport importer = createModelImport();
    importer.execute();

    ServiceTask serviceTask = (ServiceTask) Util.findBusinessObjectById(diagram, "Task_1");

    ModelPackage modelPackage = ModelFactory.eINSTANCE.getModelPackage();
    EAttribute classAttribute = modelPackage.getDocumentRoot_Class();

    assertThat(serviceTask.eGet(classAttribute)).isEqualTo("asdf");

    // no set it to fdsa
    serviceTask.eSet(classAttribute, "fdsa");
    assertThat(serviceTask.eGet(classAttribute)).isEqualTo("fdsa");
  }

  @Test
  @DiagramResource
  public void testActivitiNsPrefixReadUpdateAttrClass() {
    ModelImport importer = createModelImport();
    importer.execute();

    ServiceTask serviceTask = (ServiceTask) Util.findBusinessObjectById(diagram, "Task_1");

    ModelPackage modelPackage = ModelFactory.eINSTANCE.getModelPackage();
    EAttribute classAttribute = modelPackage.getDocumentRoot_Class();

    assertThat(serviceTask.eGet(classAttribute)).isEqualTo("asdf");

    // no set it to fdsa
    serviceTask.eSet(classAttribute, "fdsa");
    assertThat(serviceTask.eGet(classAttribute)).isEqualTo("fdsa");
  }

}
