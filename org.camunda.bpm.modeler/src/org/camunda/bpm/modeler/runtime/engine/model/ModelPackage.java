/**
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import org.eclipse.bpmn2.Bpmn2Package;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * This XML Schema defines and documents BPMN 2.0 extension elements and attributes introduced by activiti.
 * <!-- end-model-doc -->
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage {
  /**
	 * The package name.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  String eNAME = "model";

  /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  String eNS_URI = "http://activiti.org/bpmn";

  /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  String eNS_PREFIX = "camunda";

  /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  ModelPackage eINSTANCE = org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl.init();

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.DocumentRootImpl
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getDocumentRoot()
	 * @generated
	 */
  int DOCUMENT_ROOT = 0;

  /**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__MIXED = Bpmn2Package.DOCUMENT_ROOT__MIXED;

  /**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = Bpmn2Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP;

  /**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = Bpmn2Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION;

  /**
	 * The feature id for the '<em><b>Activity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ACTIVITY = Bpmn2Package.DOCUMENT_ROOT__ACTIVITY;

  /**
	 * The feature id for the '<em><b>Ad Hoc Sub Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__AD_HOC_SUB_PROCESS = Bpmn2Package.DOCUMENT_ROOT__AD_HOC_SUB_PROCESS;

  /**
	 * The feature id for the '<em><b>Flow Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__FLOW_ELEMENT = Bpmn2Package.DOCUMENT_ROOT__FLOW_ELEMENT;

  /**
	 * The feature id for the '<em><b>Artifact</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ARTIFACT = Bpmn2Package.DOCUMENT_ROOT__ARTIFACT;

  /**
	 * The feature id for the '<em><b>Assignment</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ASSIGNMENT = Bpmn2Package.DOCUMENT_ROOT__ASSIGNMENT;

  /**
	 * The feature id for the '<em><b>Association</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ASSOCIATION = Bpmn2Package.DOCUMENT_ROOT__ASSOCIATION;

  /**
	 * The feature id for the '<em><b>Auditing</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__AUDITING = Bpmn2Package.DOCUMENT_ROOT__AUDITING;

  /**
	 * The feature id for the '<em><b>Base Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__BASE_ELEMENT = Bpmn2Package.DOCUMENT_ROOT__BASE_ELEMENT;

  /**
	 * The feature id for the '<em><b>Base Element With Mixed Content</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__BASE_ELEMENT_WITH_MIXED_CONTENT = Bpmn2Package.DOCUMENT_ROOT__BASE_ELEMENT_WITH_MIXED_CONTENT;

  /**
	 * The feature id for the '<em><b>Boundary Event</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__BOUNDARY_EVENT = Bpmn2Package.DOCUMENT_ROOT__BOUNDARY_EVENT;

  /**
	 * The feature id for the '<em><b>Business Rule Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__BUSINESS_RULE_TASK = Bpmn2Package.DOCUMENT_ROOT__BUSINESS_RULE_TASK;

  /**
	 * The feature id for the '<em><b>Callable Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CALLABLE_ELEMENT = Bpmn2Package.DOCUMENT_ROOT__CALLABLE_ELEMENT;

  /**
	 * The feature id for the '<em><b>Call Activity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CALL_ACTIVITY = Bpmn2Package.DOCUMENT_ROOT__CALL_ACTIVITY;

  /**
	 * The feature id for the '<em><b>Call Choreography</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CALL_CHOREOGRAPHY = Bpmn2Package.DOCUMENT_ROOT__CALL_CHOREOGRAPHY;

  /**
	 * The feature id for the '<em><b>Call Conversation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CALL_CONVERSATION = Bpmn2Package.DOCUMENT_ROOT__CALL_CONVERSATION;

  /**
	 * The feature id for the '<em><b>Conversation Node</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CONVERSATION_NODE = Bpmn2Package.DOCUMENT_ROOT__CONVERSATION_NODE;

  /**
	 * The feature id for the '<em><b>Cancel Event Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CANCEL_EVENT_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__CANCEL_EVENT_DEFINITION;

  /**
	 * The feature id for the '<em><b>Event Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__EVENT_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__EVENT_DEFINITION;

  /**
	 * The feature id for the '<em><b>Root Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ROOT_ELEMENT = Bpmn2Package.DOCUMENT_ROOT__ROOT_ELEMENT;

  /**
	 * The feature id for the '<em><b>Catch Event</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CATCH_EVENT = Bpmn2Package.DOCUMENT_ROOT__CATCH_EVENT;

  /**
	 * The feature id for the '<em><b>Category</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CATEGORY = Bpmn2Package.DOCUMENT_ROOT__CATEGORY;

  /**
	 * The feature id for the '<em><b>Category Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CATEGORY_VALUE = Bpmn2Package.DOCUMENT_ROOT__CATEGORY_VALUE;

  /**
	 * The feature id for the '<em><b>Choreography</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CHOREOGRAPHY = Bpmn2Package.DOCUMENT_ROOT__CHOREOGRAPHY;

  /**
	 * The feature id for the '<em><b>Collaboration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__COLLABORATION = Bpmn2Package.DOCUMENT_ROOT__COLLABORATION;

  /**
	 * The feature id for the '<em><b>Choreography Activity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CHOREOGRAPHY_ACTIVITY = Bpmn2Package.DOCUMENT_ROOT__CHOREOGRAPHY_ACTIVITY;

  /**
	 * The feature id for the '<em><b>Choreography Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CHOREOGRAPHY_TASK = Bpmn2Package.DOCUMENT_ROOT__CHOREOGRAPHY_TASK;

  /**
	 * The feature id for the '<em><b>Compensate Event Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__COMPENSATE_EVENT_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__COMPENSATE_EVENT_DEFINITION;

  /**
	 * The feature id for the '<em><b>Complex Behavior Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__COMPLEX_BEHAVIOR_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__COMPLEX_BEHAVIOR_DEFINITION;

  /**
	 * The feature id for the '<em><b>Complex Gateway</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__COMPLEX_GATEWAY = Bpmn2Package.DOCUMENT_ROOT__COMPLEX_GATEWAY;

  /**
	 * The feature id for the '<em><b>Conditional Event Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CONDITIONAL_EVENT_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__CONDITIONAL_EVENT_DEFINITION;

  /**
	 * The feature id for the '<em><b>Conversation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CONVERSATION = Bpmn2Package.DOCUMENT_ROOT__CONVERSATION;

  /**
	 * The feature id for the '<em><b>Conversation Association</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CONVERSATION_ASSOCIATION = Bpmn2Package.DOCUMENT_ROOT__CONVERSATION_ASSOCIATION;

  /**
	 * The feature id for the '<em><b>Conversation Link</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CONVERSATION_LINK = Bpmn2Package.DOCUMENT_ROOT__CONVERSATION_LINK;

  /**
	 * The feature id for the '<em><b>Correlation Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CORRELATION_KEY = Bpmn2Package.DOCUMENT_ROOT__CORRELATION_KEY;

  /**
	 * The feature id for the '<em><b>Correlation Property</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CORRELATION_PROPERTY = Bpmn2Package.DOCUMENT_ROOT__CORRELATION_PROPERTY;

  /**
	 * The feature id for the '<em><b>Correlation Property Binding</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CORRELATION_PROPERTY_BINDING = Bpmn2Package.DOCUMENT_ROOT__CORRELATION_PROPERTY_BINDING;

  /**
	 * The feature id for the '<em><b>Correlation Property Retrieval Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CORRELATION_PROPERTY_RETRIEVAL_EXPRESSION = Bpmn2Package.DOCUMENT_ROOT__CORRELATION_PROPERTY_RETRIEVAL_EXPRESSION;

  /**
	 * The feature id for the '<em><b>Correlation Subscription</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CORRELATION_SUBSCRIPTION = Bpmn2Package.DOCUMENT_ROOT__CORRELATION_SUBSCRIPTION;

  /**
	 * The feature id for the '<em><b>Data Association</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DATA_ASSOCIATION = Bpmn2Package.DOCUMENT_ROOT__DATA_ASSOCIATION;

  /**
	 * The feature id for the '<em><b>Data Input</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DATA_INPUT = Bpmn2Package.DOCUMENT_ROOT__DATA_INPUT;

  /**
	 * The feature id for the '<em><b>Data Input Association</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DATA_INPUT_ASSOCIATION = Bpmn2Package.DOCUMENT_ROOT__DATA_INPUT_ASSOCIATION;

  /**
	 * The feature id for the '<em><b>Data Object</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DATA_OBJECT = Bpmn2Package.DOCUMENT_ROOT__DATA_OBJECT;

  /**
	 * The feature id for the '<em><b>Data Object Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DATA_OBJECT_REFERENCE = Bpmn2Package.DOCUMENT_ROOT__DATA_OBJECT_REFERENCE;

  /**
	 * The feature id for the '<em><b>Data Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DATA_OUTPUT = Bpmn2Package.DOCUMENT_ROOT__DATA_OUTPUT;

  /**
	 * The feature id for the '<em><b>Data Output Association</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DATA_OUTPUT_ASSOCIATION = Bpmn2Package.DOCUMENT_ROOT__DATA_OUTPUT_ASSOCIATION;

  /**
	 * The feature id for the '<em><b>Data State</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DATA_STATE = Bpmn2Package.DOCUMENT_ROOT__DATA_STATE;

  /**
	 * The feature id for the '<em><b>Data Store</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DATA_STORE = Bpmn2Package.DOCUMENT_ROOT__DATA_STORE;

  /**
	 * The feature id for the '<em><b>Data Store Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DATA_STORE_REFERENCE = Bpmn2Package.DOCUMENT_ROOT__DATA_STORE_REFERENCE;

  /**
	 * The feature id for the '<em><b>Definitions</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DEFINITIONS = Bpmn2Package.DOCUMENT_ROOT__DEFINITIONS;

  /**
	 * The feature id for the '<em><b>Documentation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DOCUMENTATION = Bpmn2Package.DOCUMENT_ROOT__DOCUMENTATION;

  /**
	 * The feature id for the '<em><b>End Event</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__END_EVENT = Bpmn2Package.DOCUMENT_ROOT__END_EVENT;

  /**
	 * The feature id for the '<em><b>End Point</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__END_POINT = Bpmn2Package.DOCUMENT_ROOT__END_POINT;

  /**
	 * The feature id for the '<em><b>Error</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ERROR = Bpmn2Package.DOCUMENT_ROOT__ERROR;

  /**
	 * The feature id for the '<em><b>Error Event Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ERROR_EVENT_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__ERROR_EVENT_DEFINITION;

  /**
	 * The feature id for the '<em><b>Escalation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ESCALATION = Bpmn2Package.DOCUMENT_ROOT__ESCALATION;

  /**
	 * The feature id for the '<em><b>Escalation Event Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ESCALATION_EVENT_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__ESCALATION_EVENT_DEFINITION;

  /**
	 * The feature id for the '<em><b>Event</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__EVENT = Bpmn2Package.DOCUMENT_ROOT__EVENT;

  /**
	 * The feature id for the '<em><b>Event Based Gateway</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__EVENT_BASED_GATEWAY = Bpmn2Package.DOCUMENT_ROOT__EVENT_BASED_GATEWAY;

  /**
	 * The feature id for the '<em><b>Exclusive Gateway</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__EXCLUSIVE_GATEWAY = Bpmn2Package.DOCUMENT_ROOT__EXCLUSIVE_GATEWAY;

  /**
	 * The feature id for the '<em><b>Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__EXPRESSION = Bpmn2Package.DOCUMENT_ROOT__EXPRESSION;

  /**
	 * The feature id for the '<em><b>Extension</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__EXTENSION = Bpmn2Package.DOCUMENT_ROOT__EXTENSION;

  /**
	 * The feature id for the '<em><b>Extension Elements</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__EXTENSION_ELEMENTS = Bpmn2Package.DOCUMENT_ROOT__EXTENSION_ELEMENTS;

  /**
	 * The feature id for the '<em><b>Flow Node</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__FLOW_NODE = Bpmn2Package.DOCUMENT_ROOT__FLOW_NODE;

  /**
	 * The feature id for the '<em><b>Formal Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__FORMAL_EXPRESSION = Bpmn2Package.DOCUMENT_ROOT__FORMAL_EXPRESSION;

  /**
	 * The feature id for the '<em><b>Gateway</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__GATEWAY = Bpmn2Package.DOCUMENT_ROOT__GATEWAY;

  /**
	 * The feature id for the '<em><b>Global Business Rule Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__GLOBAL_BUSINESS_RULE_TASK = Bpmn2Package.DOCUMENT_ROOT__GLOBAL_BUSINESS_RULE_TASK;

  /**
	 * The feature id for the '<em><b>Global Choreography Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__GLOBAL_CHOREOGRAPHY_TASK = Bpmn2Package.DOCUMENT_ROOT__GLOBAL_CHOREOGRAPHY_TASK;

  /**
	 * The feature id for the '<em><b>Global Conversation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__GLOBAL_CONVERSATION = Bpmn2Package.DOCUMENT_ROOT__GLOBAL_CONVERSATION;

  /**
	 * The feature id for the '<em><b>Global Manual Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__GLOBAL_MANUAL_TASK = Bpmn2Package.DOCUMENT_ROOT__GLOBAL_MANUAL_TASK;

  /**
	 * The feature id for the '<em><b>Global Script Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__GLOBAL_SCRIPT_TASK = Bpmn2Package.DOCUMENT_ROOT__GLOBAL_SCRIPT_TASK;

  /**
	 * The feature id for the '<em><b>Global Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__GLOBAL_TASK = Bpmn2Package.DOCUMENT_ROOT__GLOBAL_TASK;

  /**
	 * The feature id for the '<em><b>Global User Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__GLOBAL_USER_TASK = Bpmn2Package.DOCUMENT_ROOT__GLOBAL_USER_TASK;

  /**
	 * The feature id for the '<em><b>Group</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__GROUP = Bpmn2Package.DOCUMENT_ROOT__GROUP;

  /**
	 * The feature id for the '<em><b>Human Performer</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__HUMAN_PERFORMER = Bpmn2Package.DOCUMENT_ROOT__HUMAN_PERFORMER;

  /**
	 * The feature id for the '<em><b>Performer</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__PERFORMER = Bpmn2Package.DOCUMENT_ROOT__PERFORMER;

  /**
	 * The feature id for the '<em><b>Resource Role</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__RESOURCE_ROLE = Bpmn2Package.DOCUMENT_ROOT__RESOURCE_ROLE;

  /**
	 * The feature id for the '<em><b>Implicit Throw Event</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__IMPLICIT_THROW_EVENT = Bpmn2Package.DOCUMENT_ROOT__IMPLICIT_THROW_EVENT;

  /**
	 * The feature id for the '<em><b>Import</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__IMPORT = Bpmn2Package.DOCUMENT_ROOT__IMPORT;

  /**
	 * The feature id for the '<em><b>Inclusive Gateway</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__INCLUSIVE_GATEWAY = Bpmn2Package.DOCUMENT_ROOT__INCLUSIVE_GATEWAY;

  /**
	 * The feature id for the '<em><b>Input Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__INPUT_SET = Bpmn2Package.DOCUMENT_ROOT__INPUT_SET;

  /**
	 * The feature id for the '<em><b>Interface</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__INTERFACE = Bpmn2Package.DOCUMENT_ROOT__INTERFACE;

  /**
	 * The feature id for the '<em><b>Intermediate Catch Event</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__INTERMEDIATE_CATCH_EVENT = Bpmn2Package.DOCUMENT_ROOT__INTERMEDIATE_CATCH_EVENT;

  /**
	 * The feature id for the '<em><b>Intermediate Throw Event</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__INTERMEDIATE_THROW_EVENT = Bpmn2Package.DOCUMENT_ROOT__INTERMEDIATE_THROW_EVENT;

  /**
	 * The feature id for the '<em><b>Io Binding</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__IO_BINDING = Bpmn2Package.DOCUMENT_ROOT__IO_BINDING;

  /**
	 * The feature id for the '<em><b>Io Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__IO_SPECIFICATION = Bpmn2Package.DOCUMENT_ROOT__IO_SPECIFICATION;

  /**
	 * The feature id for the '<em><b>Item Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ITEM_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__ITEM_DEFINITION;

  /**
	 * The feature id for the '<em><b>Lane</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__LANE = Bpmn2Package.DOCUMENT_ROOT__LANE;

  /**
	 * The feature id for the '<em><b>Lane Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__LANE_SET = Bpmn2Package.DOCUMENT_ROOT__LANE_SET;

  /**
	 * The feature id for the '<em><b>Link Event Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__LINK_EVENT_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__LINK_EVENT_DEFINITION;

  /**
	 * The feature id for the '<em><b>Loop Characteristics</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__LOOP_CHARACTERISTICS = Bpmn2Package.DOCUMENT_ROOT__LOOP_CHARACTERISTICS;

  /**
	 * The feature id for the '<em><b>Manual Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__MANUAL_TASK = Bpmn2Package.DOCUMENT_ROOT__MANUAL_TASK;

  /**
	 * The feature id for the '<em><b>Message</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__MESSAGE = Bpmn2Package.DOCUMENT_ROOT__MESSAGE;

  /**
	 * The feature id for the '<em><b>Message Event Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__MESSAGE_EVENT_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__MESSAGE_EVENT_DEFINITION;

  /**
	 * The feature id for the '<em><b>Message Flow</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__MESSAGE_FLOW = Bpmn2Package.DOCUMENT_ROOT__MESSAGE_FLOW;

  /**
	 * The feature id for the '<em><b>Message Flow Association</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__MESSAGE_FLOW_ASSOCIATION = Bpmn2Package.DOCUMENT_ROOT__MESSAGE_FLOW_ASSOCIATION;

  /**
	 * The feature id for the '<em><b>Monitoring</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__MONITORING = Bpmn2Package.DOCUMENT_ROOT__MONITORING;

  /**
	 * The feature id for the '<em><b>Multi Instance Loop Characteristics</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__MULTI_INSTANCE_LOOP_CHARACTERISTICS = Bpmn2Package.DOCUMENT_ROOT__MULTI_INSTANCE_LOOP_CHARACTERISTICS;

  /**
	 * The feature id for the '<em><b>Operation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__OPERATION = Bpmn2Package.DOCUMENT_ROOT__OPERATION;

  /**
	 * The feature id for the '<em><b>Output Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__OUTPUT_SET = Bpmn2Package.DOCUMENT_ROOT__OUTPUT_SET;

  /**
	 * The feature id for the '<em><b>Parallel Gateway</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__PARALLEL_GATEWAY = Bpmn2Package.DOCUMENT_ROOT__PARALLEL_GATEWAY;

  /**
	 * The feature id for the '<em><b>Participant</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__PARTICIPANT = Bpmn2Package.DOCUMENT_ROOT__PARTICIPANT;

  /**
	 * The feature id for the '<em><b>Participant Association</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__PARTICIPANT_ASSOCIATION = Bpmn2Package.DOCUMENT_ROOT__PARTICIPANT_ASSOCIATION;

  /**
	 * The feature id for the '<em><b>Participant Multiplicity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__PARTICIPANT_MULTIPLICITY = Bpmn2Package.DOCUMENT_ROOT__PARTICIPANT_MULTIPLICITY;

  /**
	 * The feature id for the '<em><b>Partner Entity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__PARTNER_ENTITY = Bpmn2Package.DOCUMENT_ROOT__PARTNER_ENTITY;

  /**
	 * The feature id for the '<em><b>Partner Role</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__PARTNER_ROLE = Bpmn2Package.DOCUMENT_ROOT__PARTNER_ROLE;

  /**
	 * The feature id for the '<em><b>Potential Owner</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__POTENTIAL_OWNER = Bpmn2Package.DOCUMENT_ROOT__POTENTIAL_OWNER;

  /**
	 * The feature id for the '<em><b>Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__PROCESS = Bpmn2Package.DOCUMENT_ROOT__PROCESS;

  /**
	 * The feature id for the '<em><b>Property</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__PROPERTY = Bpmn2Package.DOCUMENT_ROOT__PROPERTY;

  /**
	 * The feature id for the '<em><b>Receive Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__RECEIVE_TASK = Bpmn2Package.DOCUMENT_ROOT__RECEIVE_TASK;

  /**
	 * The feature id for the '<em><b>Relationship</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__RELATIONSHIP = Bpmn2Package.DOCUMENT_ROOT__RELATIONSHIP;

  /**
	 * The feature id for the '<em><b>Rendering</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__RENDERING = Bpmn2Package.DOCUMENT_ROOT__RENDERING;

  /**
	 * The feature id for the '<em><b>Resource</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__RESOURCE = Bpmn2Package.DOCUMENT_ROOT__RESOURCE;

  /**
	 * The feature id for the '<em><b>Resource Assignment Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__RESOURCE_ASSIGNMENT_EXPRESSION = Bpmn2Package.DOCUMENT_ROOT__RESOURCE_ASSIGNMENT_EXPRESSION;

  /**
	 * The feature id for the '<em><b>Resource Parameter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__RESOURCE_PARAMETER = Bpmn2Package.DOCUMENT_ROOT__RESOURCE_PARAMETER;

  /**
	 * The feature id for the '<em><b>Resource Parameter Binding</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__RESOURCE_PARAMETER_BINDING = Bpmn2Package.DOCUMENT_ROOT__RESOURCE_PARAMETER_BINDING;

  /**
	 * The feature id for the '<em><b>Script</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__SCRIPT = Bpmn2Package.DOCUMENT_ROOT__SCRIPT;

  /**
	 * The feature id for the '<em><b>Script Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__SCRIPT_TASK = Bpmn2Package.DOCUMENT_ROOT__SCRIPT_TASK;

  /**
	 * The feature id for the '<em><b>Send Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__SEND_TASK = Bpmn2Package.DOCUMENT_ROOT__SEND_TASK;

  /**
	 * The feature id for the '<em><b>Sequence Flow</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__SEQUENCE_FLOW = Bpmn2Package.DOCUMENT_ROOT__SEQUENCE_FLOW;

  /**
	 * The feature id for the '<em><b>Service Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__SERVICE_TASK = Bpmn2Package.DOCUMENT_ROOT__SERVICE_TASK;

  /**
	 * The feature id for the '<em><b>Signal</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__SIGNAL = Bpmn2Package.DOCUMENT_ROOT__SIGNAL;

  /**
	 * The feature id for the '<em><b>Signal Event Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__SIGNAL_EVENT_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__SIGNAL_EVENT_DEFINITION;

  /**
	 * The feature id for the '<em><b>Standard Loop Characteristics</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__STANDARD_LOOP_CHARACTERISTICS = Bpmn2Package.DOCUMENT_ROOT__STANDARD_LOOP_CHARACTERISTICS;

  /**
	 * The feature id for the '<em><b>Start Event</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__START_EVENT = Bpmn2Package.DOCUMENT_ROOT__START_EVENT;

  /**
	 * The feature id for the '<em><b>Sub Choreography</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__SUB_CHOREOGRAPHY = Bpmn2Package.DOCUMENT_ROOT__SUB_CHOREOGRAPHY;

  /**
	 * The feature id for the '<em><b>Sub Conversation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__SUB_CONVERSATION = Bpmn2Package.DOCUMENT_ROOT__SUB_CONVERSATION;

  /**
	 * The feature id for the '<em><b>Sub Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__SUB_PROCESS = Bpmn2Package.DOCUMENT_ROOT__SUB_PROCESS;

  /**
	 * The feature id for the '<em><b>Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__TASK = Bpmn2Package.DOCUMENT_ROOT__TASK;

  /**
	 * The feature id for the '<em><b>Terminate Event Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__TERMINATE_EVENT_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__TERMINATE_EVENT_DEFINITION;

  /**
	 * The feature id for the '<em><b>Text</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__TEXT = Bpmn2Package.DOCUMENT_ROOT__TEXT;

  /**
	 * The feature id for the '<em><b>Text Annotation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__TEXT_ANNOTATION = Bpmn2Package.DOCUMENT_ROOT__TEXT_ANNOTATION;

  /**
	 * The feature id for the '<em><b>Throw Event</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__THROW_EVENT = Bpmn2Package.DOCUMENT_ROOT__THROW_EVENT;

  /**
	 * The feature id for the '<em><b>Timer Event Definition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__TIMER_EVENT_DEFINITION = Bpmn2Package.DOCUMENT_ROOT__TIMER_EVENT_DEFINITION;

  /**
	 * The feature id for the '<em><b>Transaction</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__TRANSACTION = Bpmn2Package.DOCUMENT_ROOT__TRANSACTION;

  /**
	 * The feature id for the '<em><b>User Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__USER_TASK = Bpmn2Package.DOCUMENT_ROOT__USER_TASK;

  /**
	 * The feature id for the '<em><b>Execution Listener</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__EXECUTION_LISTENER = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 0;

  /**
	 * The feature id for the '<em><b>Field</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__FIELD = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 1;

  /**
	 * The feature id for the '<em><b>Form Property</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__FORM_PROPERTY = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 2;

  /**
	 * The feature id for the '<em><b>In</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__IN = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 3;

  /**
	 * The feature id for the '<em><b>Out</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__OUT = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 4;

  /**
	 * The feature id for the '<em><b>Task Listener</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__TASK_LISTENER = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 5;

  /**
	 * The feature id for the '<em><b>Assignee</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ASSIGNEE = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 6;

  /**
	 * The feature id for the '<em><b>Candidate Groups</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CANDIDATE_GROUPS = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 7;

  /**
	 * The feature id for the '<em><b>Candidate Users</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CANDIDATE_USERS = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 8;

  /**
	 * The feature id for the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__CLASS = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 9;

  /**
	 * The feature id for the '<em><b>Collection</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__COLLECTION = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 10;

  /**
	 * The feature id for the '<em><b>Delegate Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DELEGATE_EXPRESSION = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 11;

  /**
	 * The feature id for the '<em><b>Due Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__DUE_DATE = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 12;

  /**
	 * The feature id for the '<em><b>Element Variable</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ELEMENT_VARIABLE = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 13;

  /**
	 * The feature id for the '<em><b>Form Handler Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__FORM_HANDLER_CLASS = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 14;

  /**
	 * The feature id for the '<em><b>Form Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__FORM_KEY = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 15;

  /**
	 * The feature id for the '<em><b>History</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__HISTORY = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 16;

  /**
	 * The feature id for the '<em><b>Initiator</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__INITIATOR = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 17;

  /**
	 * The feature id for the '<em><b>Result Variable</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__RESULT_VARIABLE = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 18;

  /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__TYPE = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 19;

  /**
	 * The feature id for the '<em><b>Async</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ASYNC = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 20;

  /**
	 * The feature id for the '<em><b>Act Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__ACT_EXPRESSION = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 21;

  /**
	 * The feature id for the '<em><b>Priority</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__PRIORITY = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 22;

  /**
	 * The feature id for the '<em><b>Result Variable Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__RESULT_VARIABLE_NAME = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 23;

  /**
	 * The feature id for the '<em><b>Failed Job Retry Time Cycle</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT__FAILED_JOB_RETRY_TIME_CYCLE = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 24;

  /**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int DOCUMENT_ROOT_FEATURE_COUNT = Bpmn2Package.DOCUMENT_ROOT_FEATURE_COUNT + 25;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.ExecutionListenerTypeImpl <em>Execution Listener Type</em>}' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ExecutionListenerTypeImpl
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getExecutionListenerType()
	 * @generated
	 */
  int EXECUTION_LISTENER_TYPE = 1;

  /**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int EXECUTION_LISTENER_TYPE__GROUP = 0;

  /**
	 * The feature id for the '<em><b>Field</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int EXECUTION_LISTENER_TYPE__FIELD = 1;

  /**
	 * The feature id for the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int EXECUTION_LISTENER_TYPE__CLASS = 2;

  /**
	 * The feature id for the '<em><b>Delegate Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int EXECUTION_LISTENER_TYPE__DELEGATE_EXPRESSION = 3;

  /**
	 * The feature id for the '<em><b>Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int EXECUTION_LISTENER_TYPE__EVENT = 4;

  /**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int EXECUTION_LISTENER_TYPE__EXPRESSION = 5;

  /**
	 * The number of structural features of the '<em>Execution Listener Type</em>' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int EXECUTION_LISTENER_TYPE_FEATURE_COUNT = 6;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.FieldTypeImpl <em>Field Type</em>}' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.FieldTypeImpl
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getFieldType()
	 * @generated
	 */
  int FIELD_TYPE = 2;

  /**
	 * The feature id for the '<em><b>String</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FIELD_TYPE__STRING = 0;

  /**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FIELD_TYPE__EXPRESSION = 1;

  /**
	 * The feature id for the '<em><b>Expression1</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FIELD_TYPE__EXPRESSION1 = 2;

  /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FIELD_TYPE__NAME = 3;

  /**
	 * The feature id for the '<em><b>String Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FIELD_TYPE__STRING_VALUE = 4;

  /**
	 * The number of structural features of the '<em>Field Type</em>' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FIELD_TYPE_FEATURE_COUNT = 5;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.FormPropertyTypeImpl <em>Form Property Type</em>}' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.FormPropertyTypeImpl
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getFormPropertyType()
	 * @generated
	 */
  int FORM_PROPERTY_TYPE = 3;

  /**
	 * The feature id for the '<em><b>Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE__VALUE = 0;

  /**
	 * The feature id for the '<em><b>Date Pattern</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE__DATE_PATTERN = 1;

  /**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE__EXPRESSION = 2;

  /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE__ID = 3;

  /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE__NAME = 4;

  /**
	 * The feature id for the '<em><b>Readable</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE__READABLE = 5;

  /**
	 * The feature id for the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE__REQUIRED = 6;

  /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE__TYPE = 7;

  /**
	 * The feature id for the '<em><b>Value1</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE__VALUE1 = 8;

  /**
	 * The feature id for the '<em><b>Variable</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE__VARIABLE = 9;

  /**
	 * The feature id for the '<em><b>Writable</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE__WRITABLE = 10;

  /**
	 * The feature id for the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE__DEFAULT = 11;

  /**
	 * The number of structural features of the '<em>Form Property Type</em>' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int FORM_PROPERTY_TYPE_FEATURE_COUNT = 12;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.InTypeImpl <em>In Type</em>}' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.InTypeImpl
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getInType()
	 * @generated
	 */
  int IN_TYPE = 4;

  /**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int IN_TYPE__SOURCE = 0;

  /**
	 * The feature id for the '<em><b>Source Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int IN_TYPE__SOURCE_EXPRESSION = 1;

  /**
	 * The feature id for the '<em><b>Target</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int IN_TYPE__TARGET = 2;

  /**
	 * The feature id for the '<em><b>Variables</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int IN_TYPE__VARIABLES = 3;

  /**
	 * The feature id for the '<em><b>Business Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int IN_TYPE__BUSINESS_KEY = 4;

  /**
	 * The number of structural features of the '<em>In Type</em>' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int IN_TYPE_FEATURE_COUNT = 5;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.OutTypeImpl <em>Out Type</em>}' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.OutTypeImpl
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getOutType()
	 * @generated
	 */
  int OUT_TYPE = 5;

  /**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int OUT_TYPE__SOURCE = 0;

  /**
	 * The feature id for the '<em><b>Source Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int OUT_TYPE__SOURCE_EXPRESSION = 1;

  /**
	 * The feature id for the '<em><b>Target</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int OUT_TYPE__TARGET = 2;

  /**
	 * The feature id for the '<em><b>Variables</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int OUT_TYPE__VARIABLES = 3;

  /**
	 * The number of structural features of the '<em>Out Type</em>' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int OUT_TYPE_FEATURE_COUNT = 4;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.TaskListenerTypeImpl <em>Task Listener Type</em>}' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.TaskListenerTypeImpl
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getTaskListenerType()
	 * @generated
	 */
  int TASK_LISTENER_TYPE = 6;

  /**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int TASK_LISTENER_TYPE__GROUP = 0;

  /**
	 * The feature id for the '<em><b>Field</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int TASK_LISTENER_TYPE__FIELD = 1;

  /**
	 * The feature id for the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int TASK_LISTENER_TYPE__CLASS = 2;

  /**
	 * The feature id for the '<em><b>Delegate Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int TASK_LISTENER_TYPE__DELEGATE_EXPRESSION = 3;

  /**
	 * The feature id for the '<em><b>Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int TASK_LISTENER_TYPE__EVENT = 4;

  /**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int TASK_LISTENER_TYPE__EXPRESSION = 5;

  /**
	 * The number of structural features of the '<em>Task Listener Type</em>' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int TASK_LISTENER_TYPE_FEATURE_COUNT = 6;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.CallActivityImpl <em>Call Activity</em>}' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.CallActivityImpl
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getCallActivity()
	 * @generated
	 */
  int CALL_ACTIVITY = 7;

  /**
	 * The feature id for the '<em><b>Extension Values</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__EXTENSION_VALUES = Bpmn2Package.CALL_ACTIVITY__EXTENSION_VALUES;

  /**
	 * The feature id for the '<em><b>Documentation</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__DOCUMENTATION = Bpmn2Package.CALL_ACTIVITY__DOCUMENTATION;

  /**
	 * The feature id for the '<em><b>Extension Definitions</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__EXTENSION_DEFINITIONS = Bpmn2Package.CALL_ACTIVITY__EXTENSION_DEFINITIONS;

  /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__ID = Bpmn2Package.CALL_ACTIVITY__ID;

  /**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__ANY_ATTRIBUTE = Bpmn2Package.CALL_ACTIVITY__ANY_ATTRIBUTE;

  /**
	 * The feature id for the '<em><b>Auditing</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__AUDITING = Bpmn2Package.CALL_ACTIVITY__AUDITING;

  /**
	 * The feature id for the '<em><b>Monitoring</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__MONITORING = Bpmn2Package.CALL_ACTIVITY__MONITORING;

  /**
	 * The feature id for the '<em><b>Category Value Ref</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__CATEGORY_VALUE_REF = Bpmn2Package.CALL_ACTIVITY__CATEGORY_VALUE_REF;

  /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__NAME = Bpmn2Package.CALL_ACTIVITY__NAME;

  /**
	 * The feature id for the '<em><b>Incoming</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__INCOMING = Bpmn2Package.CALL_ACTIVITY__INCOMING;

  /**
	 * The feature id for the '<em><b>Lanes</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__LANES = Bpmn2Package.CALL_ACTIVITY__LANES;

  /**
	 * The feature id for the '<em><b>Outgoing</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__OUTGOING = Bpmn2Package.CALL_ACTIVITY__OUTGOING;

  /**
	 * The feature id for the '<em><b>Io Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__IO_SPECIFICATION = Bpmn2Package.CALL_ACTIVITY__IO_SPECIFICATION;

  /**
	 * The feature id for the '<em><b>Boundary Event Refs</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__BOUNDARY_EVENT_REFS = Bpmn2Package.CALL_ACTIVITY__BOUNDARY_EVENT_REFS;

  /**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__PROPERTIES = Bpmn2Package.CALL_ACTIVITY__PROPERTIES;

  /**
	 * The feature id for the '<em><b>Data Input Associations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__DATA_INPUT_ASSOCIATIONS = Bpmn2Package.CALL_ACTIVITY__DATA_INPUT_ASSOCIATIONS;

  /**
	 * The feature id for the '<em><b>Data Output Associations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__DATA_OUTPUT_ASSOCIATIONS = Bpmn2Package.CALL_ACTIVITY__DATA_OUTPUT_ASSOCIATIONS;

  /**
	 * The feature id for the '<em><b>Resources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__RESOURCES = Bpmn2Package.CALL_ACTIVITY__RESOURCES;

  /**
	 * The feature id for the '<em><b>Loop Characteristics</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__LOOP_CHARACTERISTICS = Bpmn2Package.CALL_ACTIVITY__LOOP_CHARACTERISTICS;

  /**
	 * The feature id for the '<em><b>Completion Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__COMPLETION_QUANTITY = Bpmn2Package.CALL_ACTIVITY__COMPLETION_QUANTITY;

  /**
	 * The feature id for the '<em><b>Default</b></em>' reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__DEFAULT = Bpmn2Package.CALL_ACTIVITY__DEFAULT;

  /**
	 * The feature id for the '<em><b>Is For Compensation</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__IS_FOR_COMPENSATION = Bpmn2Package.CALL_ACTIVITY__IS_FOR_COMPENSATION;

  /**
	 * The feature id for the '<em><b>Start Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__START_QUANTITY = Bpmn2Package.CALL_ACTIVITY__START_QUANTITY;

  /**
	 * The feature id for the '<em><b>Called Element Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__CALLED_ELEMENT_REF = Bpmn2Package.CALL_ACTIVITY__CALLED_ELEMENT_REF;

  /**
	 * The feature id for the '<em><b>Called Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__CALLED_ELEMENT = Bpmn2Package.CALL_ACTIVITY_FEATURE_COUNT + 0;

  /**
	 * The feature id for the '<em><b>Called Element Binding</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__CALLED_ELEMENT_BINDING = Bpmn2Package.CALL_ACTIVITY_FEATURE_COUNT + 1;

  /**
	 * The feature id for the '<em><b>Called Element Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY__CALLED_ELEMENT_VERSION = Bpmn2Package.CALL_ACTIVITY_FEATURE_COUNT + 2;

  /**
	 * The number of structural features of the '<em>Call Activity</em>' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int CALL_ACTIVITY_FEATURE_COUNT = Bpmn2Package.CALL_ACTIVITY_FEATURE_COUNT + 3;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.BoundaryEventImpl <em>Boundary Event</em>}' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.BoundaryEventImpl
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getBoundaryEvent()
	 * @generated
	 */
  int BOUNDARY_EVENT = 8;

  /**
	 * The feature id for the '<em><b>Extension Values</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__EXTENSION_VALUES = Bpmn2Package.BOUNDARY_EVENT__EXTENSION_VALUES;

  /**
	 * The feature id for the '<em><b>Documentation</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__DOCUMENTATION = Bpmn2Package.BOUNDARY_EVENT__DOCUMENTATION;

  /**
	 * The feature id for the '<em><b>Extension Definitions</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__EXTENSION_DEFINITIONS = Bpmn2Package.BOUNDARY_EVENT__EXTENSION_DEFINITIONS;

  /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__ID = Bpmn2Package.BOUNDARY_EVENT__ID;

  /**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__ANY_ATTRIBUTE = Bpmn2Package.BOUNDARY_EVENT__ANY_ATTRIBUTE;

  /**
	 * The feature id for the '<em><b>Auditing</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__AUDITING = Bpmn2Package.BOUNDARY_EVENT__AUDITING;

  /**
	 * The feature id for the '<em><b>Monitoring</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__MONITORING = Bpmn2Package.BOUNDARY_EVENT__MONITORING;

  /**
	 * The feature id for the '<em><b>Category Value Ref</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__CATEGORY_VALUE_REF = Bpmn2Package.BOUNDARY_EVENT__CATEGORY_VALUE_REF;

  /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__NAME = Bpmn2Package.BOUNDARY_EVENT__NAME;

  /**
	 * The feature id for the '<em><b>Incoming</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__INCOMING = Bpmn2Package.BOUNDARY_EVENT__INCOMING;

  /**
	 * The feature id for the '<em><b>Lanes</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__LANES = Bpmn2Package.BOUNDARY_EVENT__LANES;

  /**
	 * The feature id for the '<em><b>Outgoing</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__OUTGOING = Bpmn2Package.BOUNDARY_EVENT__OUTGOING;

  /**
	 * The feature id for the '<em><b>Incoming Conversation Links</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__INCOMING_CONVERSATION_LINKS = Bpmn2Package.BOUNDARY_EVENT__INCOMING_CONVERSATION_LINKS;

  /**
	 * The feature id for the '<em><b>Outgoing Conversation Links</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__OUTGOING_CONVERSATION_LINKS = Bpmn2Package.BOUNDARY_EVENT__OUTGOING_CONVERSATION_LINKS;

  /**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__PROPERTIES = Bpmn2Package.BOUNDARY_EVENT__PROPERTIES;

  /**
	 * The feature id for the '<em><b>Data Outputs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__DATA_OUTPUTS = Bpmn2Package.BOUNDARY_EVENT__DATA_OUTPUTS;

  /**
	 * The feature id for the '<em><b>Data Output Association</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__DATA_OUTPUT_ASSOCIATION = Bpmn2Package.BOUNDARY_EVENT__DATA_OUTPUT_ASSOCIATION;

  /**
	 * The feature id for the '<em><b>Output Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__OUTPUT_SET = Bpmn2Package.BOUNDARY_EVENT__OUTPUT_SET;

  /**
	 * The feature id for the '<em><b>Event Definitions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__EVENT_DEFINITIONS = Bpmn2Package.BOUNDARY_EVENT__EVENT_DEFINITIONS;

  /**
	 * The feature id for the '<em><b>Event Definition Refs</b></em>' reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__EVENT_DEFINITION_REFS = Bpmn2Package.BOUNDARY_EVENT__EVENT_DEFINITION_REFS;

  /**
	 * The feature id for the '<em><b>Parallel Multiple</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__PARALLEL_MULTIPLE = Bpmn2Package.BOUNDARY_EVENT__PARALLEL_MULTIPLE;

  /**
	 * The feature id for the '<em><b>Attached To Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__ATTACHED_TO_REF = Bpmn2Package.BOUNDARY_EVENT__ATTACHED_TO_REF;

  /**
	 * The feature id for the '<em><b>Cancel Activity</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT__CANCEL_ACTIVITY = Bpmn2Package.BOUNDARY_EVENT__CANCEL_ACTIVITY;

  /**
	 * The number of structural features of the '<em>Boundary Event</em>' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int BOUNDARY_EVENT_FEATURE_COUNT = Bpmn2Package.BOUNDARY_EVENT_FEATURE_COUNT + 0;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.ValueTypeImpl <em>Value Type</em>}' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ValueTypeImpl
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getValueType()
	 * @generated
	 */
  int VALUE_TYPE = 9;

  /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int VALUE_TYPE__ID = 0;

  /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int VALUE_TYPE__NAME = 1;

  /**
	 * The number of structural features of the '<em>Value Type</em>' class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
  int VALUE_TYPE_FEATURE_COUNT = 2;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.EventType <em>Event Type</em>}' enum.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getEventType()
	 * @generated
	 */
  int EVENT_TYPE = 10;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.EventType1 <em>Event Type1</em>}' enum.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType1
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getEventType1()
	 * @generated
	 */
  int EVENT_TYPE1 = 11;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.HistoryType <em>History Type</em>}' enum.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.HistoryType
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getHistoryType()
	 * @generated
	 */
  int HISTORY_TYPE = 12;

  /**
	 * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.TypeType <em>Type Type</em>}' enum.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TypeType
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getTypeType()
	 * @generated
	 */
  int TYPE_TYPE = 13;

  /**
	 * The meta object id for the '<em>Class Type</em>' data type.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getClassType()
	 * @generated
	 */
  int CLASS_TYPE = 14;

  /**
	 * The meta object id for the '<em>Event Type Object</em>' data type.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getEventTypeObject()
	 * @generated
	 */
  int EVENT_TYPE_OBJECT = 15;

  /**
	 * The meta object id for the '<em>Event Type Object1</em>' data type.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType1
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getEventTypeObject1()
	 * @generated
	 */
  int EVENT_TYPE_OBJECT1 = 16;

  /**
	 * The meta object id for the '<em>Form Handler Class Type</em>' data type.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getFormHandlerClassType()
	 * @generated
	 */
  int FORM_HANDLER_CLASS_TYPE = 17;

  /**
	 * The meta object id for the '<em>History Type Object</em>' data type.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.HistoryType
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getHistoryTypeObject()
	 * @generated
	 */
  int HISTORY_TYPE_OBJECT = 18;

  /**
	 * The meta object id for the '<em>TExpression</em>' data type.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getTExpression()
	 * @generated
	 */
  int TEXPRESSION = 19;

  /**
	 * The meta object id for the '<em>Type Type Object</em>' data type.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TypeType
	 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getTypeTypeObject()
	 * @generated
	 */
  int TYPE_TYPE_OBJECT = 20;


  /**
	 * Returns the meta object for class '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot
	 * @generated
	 */
  EClass getDocumentRoot();

  /**
	 * Returns the meta object for the containment reference '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getExecutionListener <em>Execution Listener</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Execution Listener</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getExecutionListener()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EReference getDocumentRoot_ExecutionListener();

  /**
	 * Returns the meta object for the containment reference '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getField <em>Field</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Field</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getField()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EReference getDocumentRoot_Field();

  /**
	 * Returns the meta object for the containment reference '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFormProperty <em>Form Property</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Form Property</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFormProperty()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EReference getDocumentRoot_FormProperty();

  /**
	 * Returns the meta object for the containment reference '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getIn <em>In</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>In</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getIn()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EReference getDocumentRoot_In();

  /**
	 * Returns the meta object for the containment reference '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getOut <em>Out</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Out</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getOut()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EReference getDocumentRoot_Out();

  /**
	 * Returns the meta object for the containment reference '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getTaskListener <em>Task Listener</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Task Listener</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getTaskListener()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EReference getDocumentRoot_TaskListener();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getAssignee <em>Assignee</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Assignee</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getAssignee()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_Assignee();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getCandidateGroups <em>Candidate Groups</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Candidate Groups</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getCandidateGroups()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_CandidateGroups();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getCandidateUsers <em>Candidate Users</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Candidate Users</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getCandidateUsers()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_CandidateUsers();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Class</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getClass_()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_Class();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getCollection <em>Collection</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Collection</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getCollection()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_Collection();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getDelegateExpression <em>Delegate Expression</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delegate Expression</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getDelegateExpression()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_DelegateExpression();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getDueDate <em>Due Date</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Due Date</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getDueDate()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_DueDate();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getElementVariable <em>Element Variable</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Element Variable</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getElementVariable()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_ElementVariable();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFormHandlerClass <em>Form Handler Class</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Form Handler Class</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFormHandlerClass()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_FormHandlerClass();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFormKey <em>Form Key</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Form Key</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFormKey()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_FormKey();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getHistory <em>History</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>History</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getHistory()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_History();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getInitiator <em>Initiator</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Initiator</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getInitiator()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_Initiator();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getResultVariable <em>Result Variable</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Result Variable</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getResultVariable()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_ResultVariable();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getType()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_Type();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#isAsync <em>Async</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Async</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#isAsync()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_Async();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getActExpression <em>Act Expression</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Act Expression</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getActExpression()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_ActExpression();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getPriority <em>Priority</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Priority</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getPriority()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_Priority();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getResultVariableName <em>Result Variable Name</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Result Variable Name</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getResultVariableName()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EAttribute getDocumentRoot_ResultVariableName();

  /**
	 * Returns the meta object for the containment reference '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFailedJobRetryTimeCycle <em>Failed Job Retry Time Cycle</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Failed Job Retry Time Cycle</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFailedJobRetryTimeCycle()
	 * @see #getDocumentRoot()
	 * @generated
	 */
  EReference getDocumentRoot_FailedJobRetryTimeCycle();

  /**
	 * Returns the meta object for class '{@link org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType <em>Execution Listener Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Execution Listener Type</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType
	 * @generated
	 */
  EClass getExecutionListenerType();

  /**
	 * Returns the meta object for the attribute list '{@link org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType#getGroup()
	 * @see #getExecutionListenerType()
	 * @generated
	 */
  EAttribute getExecutionListenerType_Group();

  /**
	 * Returns the meta object for the containment reference list '{@link org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType#getField <em>Field</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Field</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType#getField()
	 * @see #getExecutionListenerType()
	 * @generated
	 */
  EReference getExecutionListenerType_Field();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Class</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType#getClass_()
	 * @see #getExecutionListenerType()
	 * @generated
	 */
  EAttribute getExecutionListenerType_Class();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType#getDelegateExpression <em>Delegate Expression</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delegate Expression</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType#getDelegateExpression()
	 * @see #getExecutionListenerType()
	 * @generated
	 */
  EAttribute getExecutionListenerType_DelegateExpression();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType#getEvent <em>Event</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Event</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType#getEvent()
	 * @see #getExecutionListenerType()
	 * @generated
	 */
  EAttribute getExecutionListenerType_Event();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType#getExpression()
	 * @see #getExecutionListenerType()
	 * @generated
	 */
  EAttribute getExecutionListenerType_Expression();

  /**
	 * Returns the meta object for class '{@link org.camunda.bpm.modeler.runtime.engine.model.FieldType <em>Field Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Field Type</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FieldType
	 * @generated
	 */
  EClass getFieldType();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FieldType#getString <em>String</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>String</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FieldType#getString()
	 * @see #getFieldType()
	 * @generated
	 */
  EAttribute getFieldType_String();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FieldType#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FieldType#getExpression()
	 * @see #getFieldType()
	 * @generated
	 */
  EAttribute getFieldType_Expression();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FieldType#getExpression1 <em>Expression1</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression1</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FieldType#getExpression1()
	 * @see #getFieldType()
	 * @generated
	 */
  EAttribute getFieldType_Expression1();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FieldType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FieldType#getName()
	 * @see #getFieldType()
	 * @generated
	 */
  EAttribute getFieldType_Name();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FieldType#getStringValue <em>String Value</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>String Value</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FieldType#getStringValue()
	 * @see #getFieldType()
	 * @generated
	 */
  EAttribute getFieldType_StringValue();

  /**
	 * Returns the meta object for class '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType <em>Form Property Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Form Property Type</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType
	 * @generated
	 */
  EClass getFormPropertyType();

  /**
	 * Returns the meta object for the containment reference list '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Value</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getValue()
	 * @see #getFormPropertyType()
	 * @generated
	 */
  EReference getFormPropertyType_Value();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getDatePattern <em>Date Pattern</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date Pattern</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getDatePattern()
	 * @see #getFormPropertyType()
	 * @generated
	 */
  EAttribute getFormPropertyType_DatePattern();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getExpression()
	 * @see #getFormPropertyType()
	 * @generated
	 */
  EAttribute getFormPropertyType_Expression();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getId()
	 * @see #getFormPropertyType()
	 * @generated
	 */
  EAttribute getFormPropertyType_Id();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getName()
	 * @see #getFormPropertyType()
	 * @generated
	 */
  EAttribute getFormPropertyType_Name();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getReadable <em>Readable</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Readable</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getReadable()
	 * @see #getFormPropertyType()
	 * @generated
	 */
  EAttribute getFormPropertyType_Readable();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getRequired <em>Required</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Required</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getRequired()
	 * @see #getFormPropertyType()
	 * @generated
	 */
  EAttribute getFormPropertyType_Required();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getType()
	 * @see #getFormPropertyType()
	 * @generated
	 */
  EAttribute getFormPropertyType_Type();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getValue1 <em>Value1</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value1</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getValue1()
	 * @see #getFormPropertyType()
	 * @generated
	 */
  EAttribute getFormPropertyType_Value1();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getVariable <em>Variable</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Variable</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getVariable()
	 * @see #getFormPropertyType()
	 * @generated
	 */
  EAttribute getFormPropertyType_Variable();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getWritable <em>Writable</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Writable</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getWritable()
	 * @see #getFormPropertyType()
	 * @generated
	 */
  EAttribute getFormPropertyType_Writable();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getDefault <em>Default</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getDefault()
	 * @see #getFormPropertyType()
	 * @generated
	 */
  EAttribute getFormPropertyType_Default();

  /**
	 * Returns the meta object for class '{@link org.camunda.bpm.modeler.runtime.engine.model.InType <em>In Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for class '<em>In Type</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.InType
	 * @generated
	 */
  EClass getInType();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.InType#getSource()
	 * @see #getInType()
	 * @generated
	 */
  EAttribute getInType_Source();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getSourceExpression <em>Source Expression</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source Expression</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.InType#getSourceExpression()
	 * @see #getInType()
	 * @generated
	 */
  EAttribute getInType_SourceExpression();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.InType#getTarget()
	 * @see #getInType()
	 * @generated
	 */
  EAttribute getInType_Target();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getVariables <em>Variables</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Variables</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.InType#getVariables()
	 * @see #getInType()
	 * @generated
	 */
  EAttribute getInType_Variables();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getBusinessKey <em>Business Key</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Business Key</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.InType#getBusinessKey()
	 * @see #getInType()
	 * @generated
	 */
  EAttribute getInType_BusinessKey();

  /**
	 * Returns the meta object for class '{@link org.camunda.bpm.modeler.runtime.engine.model.OutType <em>Out Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Out Type</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.OutType
	 * @generated
	 */
  EClass getOutType();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.OutType#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.OutType#getSource()
	 * @see #getOutType()
	 * @generated
	 */
  EAttribute getOutType_Source();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.OutType#getSourceExpression <em>Source Expression</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source Expression</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.OutType#getSourceExpression()
	 * @see #getOutType()
	 * @generated
	 */
  EAttribute getOutType_SourceExpression();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.OutType#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.OutType#getTarget()
	 * @see #getOutType()
	 * @generated
	 */
  EAttribute getOutType_Target();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.OutType#getVariables <em>Variables</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Variables</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.OutType#getVariables()
	 * @see #getOutType()
	 * @generated
	 */
  EAttribute getOutType_Variables();

  /**
	 * Returns the meta object for class '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType <em>Task Listener Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Task Listener Type</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType
	 * @generated
	 */
  EClass getTaskListenerType();

  /**
	 * Returns the meta object for the attribute list '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getGroup()
	 * @see #getTaskListenerType()
	 * @generated
	 */
  EAttribute getTaskListenerType_Group();

  /**
	 * Returns the meta object for the containment reference list '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getField <em>Field</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Field</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getField()
	 * @see #getTaskListenerType()
	 * @generated
	 */
  EReference getTaskListenerType_Field();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Class</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getClass_()
	 * @see #getTaskListenerType()
	 * @generated
	 */
  EAttribute getTaskListenerType_Class();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getDelegateExpression <em>Delegate Expression</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delegate Expression</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getDelegateExpression()
	 * @see #getTaskListenerType()
	 * @generated
	 */
  EAttribute getTaskListenerType_DelegateExpression();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getEvent <em>Event</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Event</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getEvent()
	 * @see #getTaskListenerType()
	 * @generated
	 */
  EAttribute getTaskListenerType_Event();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getExpression()
	 * @see #getTaskListenerType()
	 * @generated
	 */
  EAttribute getTaskListenerType_Expression();

  /**
	 * Returns the meta object for class '{@link org.camunda.bpm.modeler.runtime.engine.model.CallActivity <em>Call Activity</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Call Activity</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.CallActivity
	 * @generated
	 */
  EClass getCallActivity();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.CallActivity#getCalledElement <em>Called Element</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Called Element</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.CallActivity#getCalledElement()
	 * @see #getCallActivity()
	 * @generated
	 */
  EAttribute getCallActivity_CalledElement();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.CallActivity#getCalledElementBinding <em>Called Element Binding</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Called Element Binding</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.CallActivity#getCalledElementBinding()
	 * @see #getCallActivity()
	 * @generated
	 */
  EAttribute getCallActivity_CalledElementBinding();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.CallActivity#getCalledElementVersion <em>Called Element Version</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Called Element Version</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.CallActivity#getCalledElementVersion()
	 * @see #getCallActivity()
	 * @generated
	 */
  EAttribute getCallActivity_CalledElementVersion();

  /**
	 * Returns the meta object for class '{@link org.camunda.bpm.modeler.runtime.engine.model.BoundaryEvent <em>Boundary Event</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Boundary Event</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.BoundaryEvent
	 * @generated
	 */
  EClass getBoundaryEvent();

  /**
	 * Returns the meta object for class '{@link org.camunda.bpm.modeler.runtime.engine.model.ValueType <em>Value Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Value Type</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ValueType
	 * @generated
	 */
  EClass getValueType();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.ValueType#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ValueType#getId()
	 * @see #getValueType()
	 * @generated
	 */
  EAttribute getValueType_Id();

  /**
	 * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.ValueType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ValueType#getName()
	 * @see #getValueType()
	 * @generated
	 */
  EAttribute getValueType_Name();

  /**
	 * Returns the meta object for enum '{@link org.camunda.bpm.modeler.runtime.engine.model.EventType <em>Event Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Event Type</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType
	 * @generated
	 */
  EEnum getEventType();

  /**
	 * Returns the meta object for enum '{@link org.camunda.bpm.modeler.runtime.engine.model.EventType1 <em>Event Type1</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Event Type1</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType1
	 * @generated
	 */
  EEnum getEventType1();

  /**
	 * Returns the meta object for enum '{@link org.camunda.bpm.modeler.runtime.engine.model.HistoryType <em>History Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>History Type</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.HistoryType
	 * @generated
	 */
  EEnum getHistoryType();

  /**
	 * Returns the meta object for enum '{@link org.camunda.bpm.modeler.runtime.engine.model.TypeType <em>Type Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Type Type</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TypeType
	 * @generated
	 */
  EEnum getTypeType();

  /**
	 * Returns the meta object for data type '{@link java.lang.String <em>Class Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Class Type</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        extendedMetaData="name='class_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='([a-z]{2,3}(\\.[a-zA-Z][a-zA-Z_$0-9]*)*)\\.([A-Z][a-zA-Z_$0-9]*)'"
	 * @generated
	 */
  EDataType getClassType();

  /**
	 * Returns the meta object for data type '{@link org.camunda.bpm.modeler.runtime.engine.model.EventType <em>Event Type Object</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Event Type Object</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType
	 * @model instanceClass="org.camunda.bpm.modeler.runtime.engine.model.EventType"
	 *        extendedMetaData="name='event_._1_._type:Object' baseType='event_._1_._type'"
	 * @generated
	 */
  EDataType getEventTypeObject();

  /**
	 * Returns the meta object for data type '{@link org.camunda.bpm.modeler.runtime.engine.model.EventType1 <em>Event Type Object1</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Event Type Object1</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType1
	 * @model instanceClass="org.camunda.bpm.modeler.runtime.engine.model.EventType1"
	 *        extendedMetaData="name='event_._type:Object' baseType='event_._type'"
	 * @generated
	 */
  EDataType getEventTypeObject1();

  /**
	 * Returns the meta object for data type '{@link java.lang.String <em>Form Handler Class Type</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Form Handler Class Type</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        extendedMetaData="name='formHandlerClass_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='([a-z]{2,3}(\\.[a-zA-Z][a-zA-Z_$0-9]*)*)\\.([A-Z][a-zA-Z_$0-9]*)'"
	 * @generated
	 */
  EDataType getFormHandlerClassType();

  /**
	 * Returns the meta object for data type '{@link org.camunda.bpm.modeler.runtime.engine.model.HistoryType <em>History Type Object</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>History Type Object</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.HistoryType
	 * @model instanceClass="org.camunda.bpm.modeler.runtime.engine.model.HistoryType"
	 *        extendedMetaData="name='history_._type:Object' baseType='history_._type'"
	 * @generated
	 */
  EDataType getHistoryTypeObject();

  /**
	 * Returns the meta object for data type '{@link java.lang.String <em>TExpression</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>TExpression</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        extendedMetaData="name='tExpression' baseType='http://www.eclipse.org/emf/2003/XMLType#string'"
	 * @generated
	 */
  EDataType getTExpression();

  /**
	 * Returns the meta object for data type '{@link org.camunda.bpm.modeler.runtime.engine.model.TypeType <em>Type Type Object</em>}'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Type Type Object</em>'.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TypeType
	 * @model instanceClass="org.camunda.bpm.modeler.runtime.engine.model.TypeType"
	 *        extendedMetaData="name='type_._type:Object' baseType='type_._type'"
	 * @generated
	 */
  EDataType getTypeTypeObject();

  /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
  ModelFactory getModelFactory();

  /**
	 * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
	 * @generated
	 */
  interface Literals {
    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.DocumentRootImpl
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getDocumentRoot()
		 * @generated
		 */
    EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

    /**
		 * The meta object literal for the '<em><b>Execution Listener</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EReference DOCUMENT_ROOT__EXECUTION_LISTENER = eINSTANCE.getDocumentRoot_ExecutionListener();

    /**
		 * The meta object literal for the '<em><b>Field</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EReference DOCUMENT_ROOT__FIELD = eINSTANCE.getDocumentRoot_Field();

    /**
		 * The meta object literal for the '<em><b>Form Property</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EReference DOCUMENT_ROOT__FORM_PROPERTY = eINSTANCE.getDocumentRoot_FormProperty();

    /**
		 * The meta object literal for the '<em><b>In</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EReference DOCUMENT_ROOT__IN = eINSTANCE.getDocumentRoot_In();

    /**
		 * The meta object literal for the '<em><b>Out</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EReference DOCUMENT_ROOT__OUT = eINSTANCE.getDocumentRoot_Out();

    /**
		 * The meta object literal for the '<em><b>Task Listener</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EReference DOCUMENT_ROOT__TASK_LISTENER = eINSTANCE.getDocumentRoot_TaskListener();

    /**
		 * The meta object literal for the '<em><b>Assignee</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__ASSIGNEE = eINSTANCE.getDocumentRoot_Assignee();

    /**
		 * The meta object literal for the '<em><b>Candidate Groups</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__CANDIDATE_GROUPS = eINSTANCE.getDocumentRoot_CandidateGroups();

    /**
		 * The meta object literal for the '<em><b>Candidate Users</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__CANDIDATE_USERS = eINSTANCE.getDocumentRoot_CandidateUsers();

    /**
		 * The meta object literal for the '<em><b>Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__CLASS = eINSTANCE.getDocumentRoot_Class();

    /**
		 * The meta object literal for the '<em><b>Collection</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__COLLECTION = eINSTANCE.getDocumentRoot_Collection();

    /**
		 * The meta object literal for the '<em><b>Delegate Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__DELEGATE_EXPRESSION = eINSTANCE.getDocumentRoot_DelegateExpression();

    /**
		 * The meta object literal for the '<em><b>Due Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__DUE_DATE = eINSTANCE.getDocumentRoot_DueDate();

    /**
		 * The meta object literal for the '<em><b>Element Variable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__ELEMENT_VARIABLE = eINSTANCE.getDocumentRoot_ElementVariable();

    /**
		 * The meta object literal for the '<em><b>Form Handler Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__FORM_HANDLER_CLASS = eINSTANCE.getDocumentRoot_FormHandlerClass();

    /**
		 * The meta object literal for the '<em><b>Form Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__FORM_KEY = eINSTANCE.getDocumentRoot_FormKey();

    /**
		 * The meta object literal for the '<em><b>History</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__HISTORY = eINSTANCE.getDocumentRoot_History();

    /**
		 * The meta object literal for the '<em><b>Initiator</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__INITIATOR = eINSTANCE.getDocumentRoot_Initiator();

    /**
		 * The meta object literal for the '<em><b>Result Variable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__RESULT_VARIABLE = eINSTANCE.getDocumentRoot_ResultVariable();

    /**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__TYPE = eINSTANCE.getDocumentRoot_Type();

    /**
		 * The meta object literal for the '<em><b>Async</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__ASYNC = eINSTANCE.getDocumentRoot_Async();

    /**
		 * The meta object literal for the '<em><b>Act Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__ACT_EXPRESSION = eINSTANCE.getDocumentRoot_ActExpression();

    /**
		 * The meta object literal for the '<em><b>Priority</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__PRIORITY = eINSTANCE.getDocumentRoot_Priority();

    /**
		 * The meta object literal for the '<em><b>Result Variable Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute DOCUMENT_ROOT__RESULT_VARIABLE_NAME = eINSTANCE.getDocumentRoot_ResultVariableName();

    /**
		 * The meta object literal for the '<em><b>Failed Job Retry Time Cycle</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EReference DOCUMENT_ROOT__FAILED_JOB_RETRY_TIME_CYCLE = eINSTANCE.getDocumentRoot_FailedJobRetryTimeCycle();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.ExecutionListenerTypeImpl <em>Execution Listener Type</em>}' class.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ExecutionListenerTypeImpl
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getExecutionListenerType()
		 * @generated
		 */
    EClass EXECUTION_LISTENER_TYPE = eINSTANCE.getExecutionListenerType();

    /**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute EXECUTION_LISTENER_TYPE__GROUP = eINSTANCE.getExecutionListenerType_Group();

    /**
		 * The meta object literal for the '<em><b>Field</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EReference EXECUTION_LISTENER_TYPE__FIELD = eINSTANCE.getExecutionListenerType_Field();

    /**
		 * The meta object literal for the '<em><b>Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute EXECUTION_LISTENER_TYPE__CLASS = eINSTANCE.getExecutionListenerType_Class();

    /**
		 * The meta object literal for the '<em><b>Delegate Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute EXECUTION_LISTENER_TYPE__DELEGATE_EXPRESSION = eINSTANCE.getExecutionListenerType_DelegateExpression();

    /**
		 * The meta object literal for the '<em><b>Event</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute EXECUTION_LISTENER_TYPE__EVENT = eINSTANCE.getExecutionListenerType_Event();

    /**
		 * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute EXECUTION_LISTENER_TYPE__EXPRESSION = eINSTANCE.getExecutionListenerType_Expression();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.FieldTypeImpl <em>Field Type</em>}' class.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.FieldTypeImpl
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getFieldType()
		 * @generated
		 */
    EClass FIELD_TYPE = eINSTANCE.getFieldType();

    /**
		 * The meta object literal for the '<em><b>String</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FIELD_TYPE__STRING = eINSTANCE.getFieldType_String();

    /**
		 * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FIELD_TYPE__EXPRESSION = eINSTANCE.getFieldType_Expression();

    /**
		 * The meta object literal for the '<em><b>Expression1</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FIELD_TYPE__EXPRESSION1 = eINSTANCE.getFieldType_Expression1();

    /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FIELD_TYPE__NAME = eINSTANCE.getFieldType_Name();

    /**
		 * The meta object literal for the '<em><b>String Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FIELD_TYPE__STRING_VALUE = eINSTANCE.getFieldType_StringValue();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.FormPropertyTypeImpl <em>Form Property Type</em>}' class.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.FormPropertyTypeImpl
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getFormPropertyType()
		 * @generated
		 */
    EClass FORM_PROPERTY_TYPE = eINSTANCE.getFormPropertyType();

    /**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EReference FORM_PROPERTY_TYPE__VALUE = eINSTANCE.getFormPropertyType_Value();

    /**
		 * The meta object literal for the '<em><b>Date Pattern</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FORM_PROPERTY_TYPE__DATE_PATTERN = eINSTANCE.getFormPropertyType_DatePattern();

    /**
		 * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FORM_PROPERTY_TYPE__EXPRESSION = eINSTANCE.getFormPropertyType_Expression();

    /**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FORM_PROPERTY_TYPE__ID = eINSTANCE.getFormPropertyType_Id();

    /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FORM_PROPERTY_TYPE__NAME = eINSTANCE.getFormPropertyType_Name();

    /**
		 * The meta object literal for the '<em><b>Readable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FORM_PROPERTY_TYPE__READABLE = eINSTANCE.getFormPropertyType_Readable();

    /**
		 * The meta object literal for the '<em><b>Required</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FORM_PROPERTY_TYPE__REQUIRED = eINSTANCE.getFormPropertyType_Required();

    /**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FORM_PROPERTY_TYPE__TYPE = eINSTANCE.getFormPropertyType_Type();

    /**
		 * The meta object literal for the '<em><b>Value1</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FORM_PROPERTY_TYPE__VALUE1 = eINSTANCE.getFormPropertyType_Value1();

    /**
		 * The meta object literal for the '<em><b>Variable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FORM_PROPERTY_TYPE__VARIABLE = eINSTANCE.getFormPropertyType_Variable();

    /**
		 * The meta object literal for the '<em><b>Writable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FORM_PROPERTY_TYPE__WRITABLE = eINSTANCE.getFormPropertyType_Writable();

    /**
		 * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute FORM_PROPERTY_TYPE__DEFAULT = eINSTANCE.getFormPropertyType_Default();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.InTypeImpl <em>In Type</em>}' class.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.InTypeImpl
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getInType()
		 * @generated
		 */
    EClass IN_TYPE = eINSTANCE.getInType();

    /**
		 * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute IN_TYPE__SOURCE = eINSTANCE.getInType_Source();

    /**
		 * The meta object literal for the '<em><b>Source Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute IN_TYPE__SOURCE_EXPRESSION = eINSTANCE.getInType_SourceExpression();

    /**
		 * The meta object literal for the '<em><b>Target</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute IN_TYPE__TARGET = eINSTANCE.getInType_Target();

    /**
		 * The meta object literal for the '<em><b>Variables</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute IN_TYPE__VARIABLES = eINSTANCE.getInType_Variables();

    /**
		 * The meta object literal for the '<em><b>Business Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute IN_TYPE__BUSINESS_KEY = eINSTANCE.getInType_BusinessKey();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.OutTypeImpl <em>Out Type</em>}' class.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.OutTypeImpl
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getOutType()
		 * @generated
		 */
    EClass OUT_TYPE = eINSTANCE.getOutType();

    /**
		 * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute OUT_TYPE__SOURCE = eINSTANCE.getOutType_Source();

    /**
		 * The meta object literal for the '<em><b>Source Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute OUT_TYPE__SOURCE_EXPRESSION = eINSTANCE.getOutType_SourceExpression();

    /**
		 * The meta object literal for the '<em><b>Target</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute OUT_TYPE__TARGET = eINSTANCE.getOutType_Target();

    /**
		 * The meta object literal for the '<em><b>Variables</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute OUT_TYPE__VARIABLES = eINSTANCE.getOutType_Variables();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.TaskListenerTypeImpl <em>Task Listener Type</em>}' class.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.TaskListenerTypeImpl
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getTaskListenerType()
		 * @generated
		 */
    EClass TASK_LISTENER_TYPE = eINSTANCE.getTaskListenerType();

    /**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute TASK_LISTENER_TYPE__GROUP = eINSTANCE.getTaskListenerType_Group();

    /**
		 * The meta object literal for the '<em><b>Field</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EReference TASK_LISTENER_TYPE__FIELD = eINSTANCE.getTaskListenerType_Field();

    /**
		 * The meta object literal for the '<em><b>Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute TASK_LISTENER_TYPE__CLASS = eINSTANCE.getTaskListenerType_Class();

    /**
		 * The meta object literal for the '<em><b>Delegate Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute TASK_LISTENER_TYPE__DELEGATE_EXPRESSION = eINSTANCE.getTaskListenerType_DelegateExpression();

    /**
		 * The meta object literal for the '<em><b>Event</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute TASK_LISTENER_TYPE__EVENT = eINSTANCE.getTaskListenerType_Event();

    /**
		 * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute TASK_LISTENER_TYPE__EXPRESSION = eINSTANCE.getTaskListenerType_Expression();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.CallActivityImpl <em>Call Activity</em>}' class.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.CallActivityImpl
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getCallActivity()
		 * @generated
		 */
    EClass CALL_ACTIVITY = eINSTANCE.getCallActivity();

    /**
		 * The meta object literal for the '<em><b>Called Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute CALL_ACTIVITY__CALLED_ELEMENT = eINSTANCE.getCallActivity_CalledElement();

    /**
		 * The meta object literal for the '<em><b>Called Element Binding</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute CALL_ACTIVITY__CALLED_ELEMENT_BINDING = eINSTANCE.getCallActivity_CalledElementBinding();

    /**
		 * The meta object literal for the '<em><b>Called Element Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute CALL_ACTIVITY__CALLED_ELEMENT_VERSION = eINSTANCE.getCallActivity_CalledElementVersion();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.BoundaryEventImpl <em>Boundary Event</em>}' class.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.BoundaryEventImpl
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getBoundaryEvent()
		 * @generated
		 */
    EClass BOUNDARY_EVENT = eINSTANCE.getBoundaryEvent();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.impl.ValueTypeImpl <em>Value Type</em>}' class.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ValueTypeImpl
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getValueType()
		 * @generated
		 */
    EClass VALUE_TYPE = eINSTANCE.getValueType();

    /**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute VALUE_TYPE__ID = eINSTANCE.getValueType_Id();

    /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @generated
		 */
    EAttribute VALUE_TYPE__NAME = eINSTANCE.getValueType_Name();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.EventType <em>Event Type</em>}' enum.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getEventType()
		 * @generated
		 */
    EEnum EVENT_TYPE = eINSTANCE.getEventType();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.EventType1 <em>Event Type1</em>}' enum.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType1
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getEventType1()
		 * @generated
		 */
    EEnum EVENT_TYPE1 = eINSTANCE.getEventType1();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.HistoryType <em>History Type</em>}' enum.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.HistoryType
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getHistoryType()
		 * @generated
		 */
    EEnum HISTORY_TYPE = eINSTANCE.getHistoryType();

    /**
		 * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.TypeType <em>Type Type</em>}' enum.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.TypeType
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getTypeType()
		 * @generated
		 */
    EEnum TYPE_TYPE = eINSTANCE.getTypeType();

    /**
		 * The meta object literal for the '<em>Class Type</em>' data type.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getClassType()
		 * @generated
		 */
    EDataType CLASS_TYPE = eINSTANCE.getClassType();

    /**
		 * The meta object literal for the '<em>Event Type Object</em>' data type.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getEventTypeObject()
		 * @generated
		 */
    EDataType EVENT_TYPE_OBJECT = eINSTANCE.getEventTypeObject();

    /**
		 * The meta object literal for the '<em>Event Type Object1</em>' data type.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType1
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getEventTypeObject1()
		 * @generated
		 */
    EDataType EVENT_TYPE_OBJECT1 = eINSTANCE.getEventTypeObject1();

    /**
		 * The meta object literal for the '<em>Form Handler Class Type</em>' data type.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getFormHandlerClassType()
		 * @generated
		 */
    EDataType FORM_HANDLER_CLASS_TYPE = eINSTANCE.getFormHandlerClassType();

    /**
		 * The meta object literal for the '<em>History Type Object</em>' data type.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.HistoryType
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getHistoryTypeObject()
		 * @generated
		 */
    EDataType HISTORY_TYPE_OBJECT = eINSTANCE.getHistoryTypeObject();

    /**
		 * The meta object literal for the '<em>TExpression</em>' data type.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getTExpression()
		 * @generated
		 */
    EDataType TEXPRESSION = eINSTANCE.getTExpression();

    /**
		 * The meta object literal for the '<em>Type Type Object</em>' data type.
		 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
		 * @see org.camunda.bpm.modeler.runtime.engine.model.TypeType
		 * @see org.camunda.bpm.modeler.runtime.engine.model.impl.ModelPackageImpl#getTypeTypeObject()
		 * @generated
		 */
    EDataType TYPE_TYPE_OBJECT = eINSTANCE.getTypeTypeObject();

  }

} //ModelPackage
