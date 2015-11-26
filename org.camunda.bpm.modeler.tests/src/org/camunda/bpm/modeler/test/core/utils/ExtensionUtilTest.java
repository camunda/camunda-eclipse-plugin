package org.camunda.bpm.modeler.test.core.utils;

import static org.camunda.bpm.modeler.test.util.Util.findBusinessObjectById;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.FailedJobRetryTimeCycleType;
import org.camunda.bpm.modeler.runtime.engine.model.InType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.OutType;
import org.camunda.bpm.modeler.runtime.engine.model.util.ModelResourceFactoryImpl;
import org.camunda.bpm.modeler.test.feature.AbstractNonTransactionalFeatureTest;
import org.camunda.bpm.modeler.test.util.DiagramResource;
import org.camunda.bpm.modeler.test.util.Util;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.junit.Test;

/**
 * Model util test
 * 
 * @author nico.rehwaldt
 */
public class ExtensionUtilTest extends AbstractNonTransactionalFeatureTest {

	static final ModelFactory modelFactory = ModelFactory.eINSTANCE;
	static final ModelPackage modelPackage = ModelPackage.eINSTANCE;
	
	static final EStructuralFeature ASYNC_FEATURE = modelPackage.getDocumentRoot_Async();
	static final EStructuralFeature RETRY_CYCLE_FEATURE = modelPackage.getDocumentRoot_FailedJobRetryTimeCycle();
	static final EStructuralFeature CALL_ACTIVITY_IN_TYPE = modelPackage.getDocumentRoot_In();
	static final EStructuralFeature CALL_ACTIVITY_OUT_TYPE = modelPackage.getDocumentRoot_Out();
	static final EStructuralFeature IN_TYPE_BUSINESS_KEY = modelPackage.getInType_BusinessKey();
	static final EStructuralFeature IN_TYPE_VARIABLES = modelPackage.getInType_Variables();
	static final EStructuralFeature OUT_TYPE_VARIABLES = modelPackage.getOutType_Variables();
	static final EStructuralFeature OUT_TYPE_SOURCE = modelPackage.getOutType_Source();
	
	static final String BUSINESS_KEY_EXPRESSION = "#{execution.processBusinessKey}";
	static final String ALL_VARIABLES_VALUE = "all";

	@Test
	@DiagramResource
	public void testGetExtensionAttributeValues() {
		CallActivity callActivity1 = (CallActivity) Util.findBusinessObjectById(diagram, "CallActivity_1");
		
		assertThat(callActivity1).isNotNull();
		
		List<ExtensionAttributeValue> callActivity1ExtensionValues = ExtensionUtil.getExtensionAttributeValues(callActivity1);
		
		assertThat(callActivity1ExtensionValues).hasSize(1);

		Task task1 = (Task) Util.findBusinessObjectById(diagram, "Task_1");
		
		List<ExtensionAttributeValue> task1ExtensionValues = ExtensionUtil.getExtensionAttributeValues(task1);
		
		assertThat(task1ExtensionValues).hasSize(0);
	}
	
	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ExtensionUtilTest.testGetExtensionAttributeValues.bpmn")
	public void testGetExtensionAttribute() {
		final CallActivity callActivity1 = findBusinessObjectById(diagram, "CallActivity_1", CallActivity.class);
		
		Object value = ExtensionUtil.getExtension(callActivity1, RETRY_CYCLE_FEATURE, "text");
		
		assertThat(value).isEqualTo("R3/PT10S");
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ExtensionUtilTest.testGetExtensionAttributeValues.bpmn")
	public void testAddExtension() {
		final ServiceTask serviceTask1 = findBusinessObjectById(diagram, "ServiceTask_1", ServiceTask.class);
		
		final FailedJobRetryTimeCycleType retryCycle = modelFactory.createFailedJobRetryTimeCycleType();
		retryCycle.setText("R3/PT10S");

		transactionalExecute(new RecordingCommand(editingDomain) {
			
			@Override
			protected void doExecute() {
				ExtensionUtil.addExtension(serviceTask1, RETRY_CYCLE_FEATURE, retryCycle);
			}
		});
		
		Object value = ExtensionUtil.getExtension(serviceTask1, RETRY_CYCLE_FEATURE, "text");
		
		assertThat(value).isEqualTo("R3/PT10S");
	}

	@Test
	@DiagramResource("org/camunda/bpm/modeler/test/core/utils/ExtensionUtilTest.testGetExtensionAttributeValues.bpmn")
	public void testUpdateExtension() {
		final CallActivity callActivity1 = findBusinessObjectById(diagram, "CallActivity_1", CallActivity.class);
		
    Object valueBefore = ExtensionUtil.getExtension(callActivity1, RETRY_CYCLE_FEATURE, "text");
    
    assertThat(valueBefore).isEqualTo("R3/PT10S");
		
		final FailedJobRetryTimeCycleType retryCycle = modelFactory.createFailedJobRetryTimeCycleType();
		retryCycle.setText("R3/PT200000S");
		
		transactionalExecute(new RecordingCommand(editingDomain) {
			
			@Override
			protected void doExecute() {
				ExtensionUtil.updateExtension(callActivity1, RETRY_CYCLE_FEATURE, retryCycle);
			}
		});
		
		Object valueAfter = ExtensionUtil.getExtension(callActivity1, RETRY_CYCLE_FEATURE, "text");
		
		assertThat(valueAfter).isEqualTo("R3/PT200000S");
	}
	

  @Test
  @DiagramResource("org/camunda/bpm/modeler/test/core/utils/ExtensionUtilTest.testGetExtensionOutOfManyInTypes.bpmn")
  public void testGetExtensionOutOfManyInTypes() {
    final CallActivity callActivity = findBusinessObjectById(diagram, "callActivity", CallActivity.class);
    
    List<ExtensionAttributeValue> extensionAttributeValues = ExtensionUtil.getExtensionAttributeValues(callActivity);
    assertThat(extensionAttributeValues).hasSize(1);
    
    Object businessKeyValue = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_IN_TYPE, "businessKey");
    
    assertThat(businessKeyValue).isEqualTo(BUSINESS_KEY_EXPRESSION);
    
    Object variablesValue = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_IN_TYPE, "variables");
    
    assertThat(variablesValue).isEqualTo("all");
    
    List<InType> extensions = ExtensionUtil.getExtensions(callActivity, InType.class);
    assertThat(extensions).hasSize(5);
  }
  
  @Test
  @DiagramResource("org/camunda/bpm/modeler/test/core/utils/ExtensionUtilTest.testGetExtensionOutOfManyOutTypes.bpmn")
  public void testGetExtensionOutOfManyOutTypes() {
    final CallActivity callActivity = findBusinessObjectById(diagram, "callActivity", CallActivity.class);
    
    List<ExtensionAttributeValue> extensionAttributeValues = ExtensionUtil.getExtensionAttributeValues(callActivity);
    assertThat(extensionAttributeValues).hasSize(1);
    
    Object variablesValue = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, "variables");
    
    assertThat(variablesValue).isEqualTo("all");
    
    List<OutType> extensions = ExtensionUtil.getExtensions(callActivity, OutType.class);
    assertThat(extensions).hasSize(4);
  }
  
  @Test
  @DiagramResource("org/camunda/bpm/modeler/test/core/utils/ExtensionUtilTest.testAddAndRemoveExtensions.bpmn")
  public void testAddAndRemoveExtensionsToInType() {
    final CallActivity callActivity = findBusinessObjectById(diagram, "callActivity", CallActivity.class);
    
    List<ExtensionAttributeValue> extensionAttributeValues = ExtensionUtil.getExtensionAttributeValues(callActivity);
    assertThat(extensionAttributeValues).hasSize(0);
    
    // add <camunda:in source="test" target="test"/>
    final InType sourceInType = modelFactory.createInType();
    sourceInType.setSource("test");
    sourceInType.setTarget("test");
    
    addCallActivityExtension(callActivity, CALL_ACTIVITY_IN_TYPE, sourceInType);
    
    Object value = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_IN_TYPE, "source");
    assertThat(value).isEqualTo("test");
    
    // add <camunda:in source="test2" target="test2"/>
    final InType source2InType = modelFactory.createInType();
    source2InType.setSource("test2");
    source2InType.setTarget("test2");
    
    addCallActivityExtension(callActivity, CALL_ACTIVITY_IN_TYPE, source2InType);

    // add <camunda:in businessKey="#{execution.processBusinessKey}"/>
    final InType businessKeyInType = modelFactory.createInType();
    businessKeyInType.setBusinessKey(BUSINESS_KEY_EXPRESSION);
    
    addCallActivityExtension(callActivity, CALL_ACTIVITY_IN_TYPE, businessKeyInType);
    
    value = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_IN_TYPE, "businessKey");
    assertThat(value).isEqualTo(BUSINESS_KEY_EXPRESSION);
    
    // add <camunda:in variables="all"/>
    final InType variablesInType = modelFactory.createInType();
    variablesInType.setVariables(ALL_VARIABLES_VALUE);
    
    addCallActivityExtension(callActivity, CALL_ACTIVITY_IN_TYPE, variablesInType);
    
    value = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_IN_TYPE, "variables");
    assertThat(value).isEqualTo(ALL_VARIABLES_VALUE);
    
    List<InType> extensions = ExtensionUtil.getExtensions(callActivity, InType.class);
    assertThat(extensions).hasSize(4);
    
    // remove inType businessKey extension
    transactionalExecute(new RecordingCommand(editingDomain) {
        
      @Override
      protected void doExecute() {
        ExtensionUtil.removeExtensionByValue(callActivity, CALL_ACTIVITY_IN_TYPE, IN_TYPE_BUSINESS_KEY, BUSINESS_KEY_EXPRESSION);
      }
    });

    extensions = ExtensionUtil.getExtensions(callActivity, InType.class);
    assertThat(extensions).hasSize(3);
    
    value = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_IN_TYPE, "businessKey");
    assertThat(value).isNull();

    extensionAttributeValues = ExtensionUtil.getExtensionAttributeValues(callActivity);
    assertThat(extensionAttributeValues).hasSize(1);

  }
  
  @Test
  @DiagramResource("org/camunda/bpm/modeler/test/core/utils/ExtensionUtilTest.testAddAndRemoveExtensions.bpmn")
  public void testAddAndRemoveExtensionsToOutType() {
    final CallActivity callActivity = findBusinessObjectById(diagram, "callActivity", CallActivity.class);
    
    List<ExtensionAttributeValue> extensionAttributeValues = ExtensionUtil.getExtensionAttributeValues(callActivity);
    assertThat(extensionAttributeValues).hasSize(0);
    
    // add <camunda:out source="test" target="test"/>
    final OutType sourceOutType = modelFactory.createOutType();
    sourceOutType.setSource("test");
    sourceOutType.setTarget("test");
    
    addCallActivityExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, sourceOutType);
    
    Object value = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, "source");
    assertThat(value).isEqualTo("test");
    
    // add <camunda:out source="test2" target="test2"/>
    final OutType source2OutType = modelFactory.createOutType();
    source2OutType.setSource("test2");
    source2OutType.setTarget("test2");
    
    addCallActivityExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, source2OutType);

    // add <camunda:out variables="all"/>
    final OutType variablesOutType = modelFactory.createOutType();
    variablesOutType.setVariables(ALL_VARIABLES_VALUE);
    
    addCallActivityExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, variablesOutType);
    
    value = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, "variables");
    assertThat(value).isEqualTo(ALL_VARIABLES_VALUE);
    
    List<OutType> extensions = ExtensionUtil.getExtensions(callActivity, OutType.class);
    assertThat(extensions).hasSize(3);
    
    // remove outType source="test2" extension
    transactionalExecute(new RecordingCommand(editingDomain) {
        
      @Override
      protected void doExecute() {
        ExtensionUtil.removeExtensionByValue(callActivity, CALL_ACTIVITY_OUT_TYPE, OUT_TYPE_SOURCE, "test2");
      }
    });

    extensions = ExtensionUtil.getExtensions(callActivity, OutType.class);
    assertThat(extensions).hasSize(2);
    
    value = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, "source");
    assertThat(value).isEqualTo("test");

    extensionAttributeValues = ExtensionUtil.getExtensionAttributeValues(callActivity);
    assertThat(extensionAttributeValues).hasSize(1);

  }
  
  @Test
  @DiagramResource("org/camunda/bpm/modeler/test/core/utils/ExtensionUtilTest.testAddAndRemoveExtensions.bpmn")
  public void testAddAndRemoveExtensions() {
    final CallActivity callActivity = findBusinessObjectById(diagram, "callActivity", CallActivity.class);
    
    List<ExtensionAttributeValue> extensionAttributeValues = ExtensionUtil.getExtensionAttributeValues(callActivity);
    assertThat(extensionAttributeValues).hasSize(0);
    
    // add <camunda:in source="test" target="test"/>
    final InType sourceInType = modelFactory.createInType();
    sourceInType.setSource("test");
    sourceInType.setTarget("test");
    
    addCallActivityExtension(callActivity, CALL_ACTIVITY_IN_TYPE, sourceInType);
    
    Object value = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_IN_TYPE, "source");
    assertThat(value).isEqualTo("test");
    
    // add <camunda:out source="test" target="test"/>
    final OutType sourceOutType = modelFactory.createOutType();
    sourceOutType.setSource("test");
    sourceOutType.setTarget("test");
    
    addCallActivityExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, sourceOutType);
    
    value = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, "source");
    assertThat(value).isEqualTo("test");
    
    // add <camunda:in variables="all"/>
    final InType variablesInType = modelFactory.createInType();
    variablesInType.setVariables(ALL_VARIABLES_VALUE);
    
    addCallActivityExtension(callActivity, CALL_ACTIVITY_IN_TYPE, variablesInType);
    
    value = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_IN_TYPE, "variables");
    assertThat(value).isEqualTo(ALL_VARIABLES_VALUE);
    
    // add <camunda:out variables="all"/>
    final OutType variablesOutType = modelFactory.createOutType();
    variablesOutType.setVariables(ALL_VARIABLES_VALUE);
    
    addCallActivityExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, variablesOutType);
    
    value = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, "variables");
    assertThat(value).isEqualTo(ALL_VARIABLES_VALUE);
    
    // check number of extensions
    List<InType> inExtensions = ExtensionUtil.getExtensions(callActivity, InType.class);
    assertThat(inExtensions).hasSize(2);
    
    List<OutType> outExtensions = ExtensionUtil.getExtensions(callActivity, OutType.class);
    assertThat(outExtensions).hasSize(2);
    
    // remove outType variables extension
    transactionalExecute(new RecordingCommand(editingDomain) {
        
      @Override
      protected void doExecute() {
        ExtensionUtil.removeExtensionByValue(callActivity, CALL_ACTIVITY_OUT_TYPE, OUT_TYPE_VARIABLES, ALL_VARIABLES_VALUE);
      }
    });

    inExtensions = ExtensionUtil.getExtensions(callActivity, InType.class);
    assertThat(inExtensions).hasSize(2);
    
    outExtensions = ExtensionUtil.getExtensions(callActivity, OutType.class);
    assertThat(outExtensions).hasSize(1);
    
    extensionAttributeValues = ExtensionUtil.getExtensionAttributeValues(callActivity);
    assertThat(extensionAttributeValues).hasSize(1);

  }
  
  @Test
  @DiagramResource("org/camunda/bpm/modeler/test/core/utils/ExtensionUtilTest.testAddAndRemoveExtensions.bpmn")
  public void testRemoveAllExtensions() {
    final CallActivity callActivity = findBusinessObjectById(diagram, "callActivity", CallActivity.class);
    
    List<ExtensionAttributeValue> extensionAttributeValues = ExtensionUtil.getExtensionAttributeValues(callActivity);
    assertThat(extensionAttributeValues).hasSize(0);
    
    // add <camunda:out variables="all"/>
    final OutType variablesOutType = modelFactory.createOutType();
    variablesOutType.setVariables(ALL_VARIABLES_VALUE);
    
    addCallActivityExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, variablesOutType);
    
    Object value = ExtensionUtil.getExtension(callActivity, CALL_ACTIVITY_OUT_TYPE, "variables");
    assertThat(value).isEqualTo(ALL_VARIABLES_VALUE);
    
    List<OutType> extensions = ExtensionUtil.getExtensions(callActivity, OutType.class);
    assertThat(extensions).hasSize(1);
    
    // remove outType variables extension
    transactionalExecute(new RecordingCommand(editingDomain) {
        
      @Override
      protected void doExecute() {
        ExtensionUtil.removeExtensionByValue(callActivity, CALL_ACTIVITY_OUT_TYPE, OUT_TYPE_VARIABLES, "all");
      }
    });

    extensions = ExtensionUtil.getExtensions(callActivity, OutType.class);
    assertThat(extensions).isNullOrEmpty();
    
    extensionAttributeValues = ExtensionUtil.getExtensionAttributeValues(callActivity);
    assertThat(extensionAttributeValues).hasSize(0);

  }
  
	/**
	 * We want to use the model extensions
	 */
	protected Bpmn2ResourceImpl createBpmn2Resource(URI uri) {
		return (Bpmn2ResourceImpl) new ModelResourceFactoryImpl().createResource(uri);
	}
	
	private void addCallActivityExtension(final EObject model, final EStructuralFeature feature, final EObject value) {
    transactionalExecute(new RecordingCommand(editingDomain) {
      
      @Override
      protected void doExecute() {
        ExtensionUtil.addExtension(model, feature, value);
      }
    });
	}
}
