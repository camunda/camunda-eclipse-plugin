package org.camunda.bpm.modeler.test.importer.scenarios;

import static org.camunda.bpm.modeler.test.util.TestHelper.transactionalExecute;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.modeler.core.importer.ModelImport;
import org.camunda.bpm.modeler.core.model.Bpmn2ModelerResourceImpl.Bpmn2ModelerXmlHelper;
import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.FailedJobRetryTimeCycleType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.util.ModelResourceFactoryImpl;
import org.camunda.bpm.modeler.runtime.engine.model.util.ModelResourceImpl;
import org.camunda.bpm.modeler.test.importer.AbstractImportBpmnModelTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.TestHelper;
import org.camunda.bpm.modeler.test.util.Util;
import org.camunda.bpm.modeler.test.util.TestHelper.ModelResources;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.bpmn2.util.OnlyContainmentTypeInfo;
import org.eclipse.bpmn2.util.XmlExtendedMetadata;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.ElementHandlerImpl;
import org.eclipse.jface.dialogs.IDialogConstants;
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

  @Test
  public void testDeprecatedActivitiNs() {
    String path = "org/camunda/bpm/modeler/test/importer/scenarios/NamespaceTest.testDeprecatedActivitiNs.bpmn";
	ModelResources resources = TestHelper.createModel(path, new MyModelResourceFactory(new MyBpmn2XmlHelper(null, true)));
	final Resource resource = resources.getResource();

	DocumentRoot documentRoot = (DocumentRoot) resource.getContents().get(0);
	Map<String, String> prefixMap = documentRoot.getXMLNSPrefixMap();
	assertThat(prefixMap.get("camunda")).isEqualTo("http://camunda.org/schema/1.0/bpmn");
	assertThat(prefixMap.get("fox")).isEqualTo("http://camunda.org/schema/1.0/bpmn");

	final ServiceTask serviceTask = (ServiceTask) resource.getEObject("Task_1");

	// class attribute
	ModelPackage modelPackage = ModelFactory.eINSTANCE.getModelPackage();
	final EAttribute classAttribute = modelPackage.getDocumentRoot_Class();

	assertThat(serviceTask.eGet(classAttribute)).isEqualTo("asdf");

	transactionalExecute(resources, new Runnable() {
      @Override
	  public void run() {
	    serviceTask.eSet(classAttribute, "fdsa");
	  }
	});

	assertThat(serviceTask.eGet(classAttribute)).isEqualTo("fdsa");

	// failed job retry time cycle element
	final List<FailedJobRetryTimeCycleType> timeCycles = ExtensionUtil.getExtensions(serviceTask, FailedJobRetryTimeCycleType.class);
	assertThat(timeCycles).isNotNull();
	assertThat(timeCycles).hasSize(1);
	assertThat(timeCycles.get(0).getText()).isEqualTo("R3/PT10S");

	transactionalExecute(resources, new Runnable() {
      @Override
      public void run() {
        timeCycles.get(0).setText("qwer");
      }
    });

	assertThat(timeCycles.get(0).getText()).isEqualTo("qwer");
  }

  @Test
  public void testDeprecatedActivitiNsDoNotUpdate() {
    String path = "org/camunda/bpm/modeler/test/importer/scenarios/NamespaceTest.testDeprecatedActivitiNs.bpmn";
	ModelResources resources = TestHelper.createModel(path, new MyModelResourceFactory(new MyBpmn2XmlHelper(null, false)));
	final Resource resource = resources.getResource();

	DocumentRoot documentRoot = (DocumentRoot) resource.getContents().get(0);
	Map<String, String> prefixMap = documentRoot.getXMLNSPrefixMap();
	assertThat(prefixMap.get("camunda")).isEqualTo("http://activiti.org/bpmn");
	assertThat(prefixMap.get("fox")).isEqualTo("http://www.camunda.com/fox");

	final ServiceTask serviceTask = (ServiceTask) resource.getEObject("Task_1");

	// class attribute
	ModelPackage modelPackage = ModelFactory.eINSTANCE.getModelPackage();
	final EAttribute classAttribute = modelPackage.getDocumentRoot_Class();

	assertThat(serviceTask.eGet(classAttribute)).isNull();

	transactionalExecute(resources, new Runnable() {
      @Override
	  public void run() {
	    serviceTask.eSet(classAttribute, "fdsa");
	  }
	});

	assertThat(serviceTask.eGet(classAttribute)).isEqualTo("fdsa");

	// failed job retry time cycle element
	List<FailedJobRetryTimeCycleType> timeCycles = ExtensionUtil.getExtensions(serviceTask, FailedJobRetryTimeCycleType.class);
	assertThat(timeCycles).isEmpty();

	transactionalExecute(resources, new Runnable() {
      @Override
      public void run() {
        FailedJobRetryTimeCycleType retryCycle = ModelFactory.eINSTANCE.createFailedJobRetryTimeCycleType();
        retryCycle.setText("qwer");
        ExtensionUtil.addExtension(serviceTask, ModelPackage.eINSTANCE.getDocumentRoot_FailedJobRetryTimeCycle(), retryCycle);
      }
    });

    timeCycles = ExtensionUtil.getExtensions(serviceTask, FailedJobRetryTimeCycleType.class);
	assertThat(timeCycles).isNotEmpty();
  }

  protected static class MyBpmn2XmlHelper extends Bpmn2ModelerXmlHelper {

	public MyBpmn2XmlHelper(Bpmn2ResourceImpl resource, boolean update) {
	  super(resource);
      this.alreadyOpenedDialog = true;
	  if (update) {
	    this.dialogResult = IDialogConstants.OK_ID;
	  }
	  else {
	    this.dialogResult = IDialogConstants.CANCEL_ID;
	  }
	}
  }

  protected static class MyModelResource extends ModelResourceImpl {

	public MyModelResource(URI uri, Bpmn2ModelerXmlHelper xmlHelper) {
	  super(uri);
	  this.xmlHelper = xmlHelper;
	}

  }

  protected static class MyModelResourceFactory extends ModelResourceFactoryImpl {

	protected Bpmn2ModelerXmlHelper xmlHelper;

	public MyModelResourceFactory(Bpmn2ModelerXmlHelper xmlHelper) {
	  this.xmlHelper = xmlHelper;
	}

	public Resource createResource(URI uri) {
      ModelResourceImpl result = new MyModelResource(uri, xmlHelper);
      ExtendedMetaData extendedMetadata = new XmlExtendedMetadata(){
        @Override
        protected boolean isFeatureNamespaceMatchingLax() {
          return true;
        }

        @Override
        public EStructuralFeature getAttribute(EClass eClass, String namespace, String name) {
		  List<EStructuralFeature> classAttributes = getAttributes(eClass);
		  for (Iterator<EStructuralFeature> iterator = classAttributes.iterator(); iterator.hasNext();) {
		    EStructuralFeature eStructuralFeature = iterator.next();
			if (name.equals(getName(eStructuralFeature))) {
			  return eStructuralFeature;
			}
		  }
		  return super.getAttribute(eClass, namespace, name);
		}
      };

      result.getDefaultSaveOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetadata);
      result.getDefaultLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetadata);

      result.getDefaultSaveOptions().put(XMLResource.OPTION_SAVE_TYPE_INFORMATION, new OnlyContainmentTypeInfo());

      result.getDefaultLoadOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE,
                Boolean.TRUE);
      result.getDefaultSaveOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE,
                Boolean.TRUE);

      result.getDefaultSaveOptions().put(XMLResource.OPTION_ELEMENT_HANDLER,
                new ElementHandlerImpl(true));

      result.getDefaultSaveOptions().put(XMLResource.OPTION_ENCODING, "UTF-8");
      result.getDefaultSaveOptions().put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);

      // save xsi:schemaLocation in Definitions parameter
      result.getDefaultSaveOptions().put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);

      return result;
	}
  }

}
