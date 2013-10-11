/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import org.camunda.bpm.modeler.runtime.engine.model.fox.FailedJobRetryTimeCycleType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getExecutionListener <em>Execution Listener</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getField <em>Field</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFormProperty <em>Form Property</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getIn <em>In</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getOut <em>Out</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getTaskListener <em>Task Listener</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getAssignee <em>Assignee</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getCandidateGroups <em>Candidate Groups</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getCandidateUsers <em>Candidate Users</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getClass_ <em>Class</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getCollection <em>Collection</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getDelegateExpression <em>Delegate Expression</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getDueDate <em>Due Date</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getElementVariable <em>Element Variable</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFormHandlerClass <em>Form Handler Class</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFormKey <em>Form Key</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getHistory <em>History</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getInitiator <em>Initiator</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getResultVariable <em>Result Variable</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getType <em>Type</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#isAsync <em>Async</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getActExpression <em>Act Expression</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getPriority <em>Priority</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getResultVariableName <em>Result Variable Name</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFailedJobRetryTimeCycle <em>Failed Job Retry Time Cycle</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed' namespace='##targetNamespace'"
 * @generated
 */
public interface DocumentRoot extends org.eclipse.bpmn2.DocumentRoot {
  /**
	 * Returns the value of the '<em><b>Execution Listener</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Extension element for any activities and sequenceflow, used to execute custom Java logic or an expression upon the occurrence of a certain event.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execution Listener</em>' containment reference.
	 * @see #setExecutionListener(ExecutionListenerType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_ExecutionListener()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='executionListener' namespace='##targetNamespace'"
	 * @generated
	 */
  ExecutionListenerType getExecutionListener();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getExecutionListener <em>Execution Listener</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Execution Listener</em>' containment reference.
	 * @see #getExecutionListener()
	 * @generated
	 */
  void setExecutionListener(ExecutionListenerType value);

  /**
	 * Returns the value of the '<em><b>Field</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Extension Element for Service Tasks to inject values into the fields of delegate classes.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Field</em>' containment reference.
	 * @see #setField(FieldType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_Field()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='field' namespace='##targetNamespace'"
	 * @generated
	 */
  FieldType getField();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getField <em>Field</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Field</em>' containment reference.
	 * @see #getField()
	 * @generated
	 */
  void setField(FieldType value);

  /**
	 * Returns the value of the '<em><b>Form Property</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Subelement of the extensionsElement of activities that support forms. Allows to specifies properties (!= process variables) for a form. See documentation chapter on form properties.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Form Property</em>' containment reference.
	 * @see #setFormProperty(FormPropertyType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_FormProperty()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='formProperty' namespace='##targetNamespace'"
	 * @generated
	 */
  FormPropertyType getFormProperty();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFormProperty <em>Form Property</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Form Property</em>' containment reference.
	 * @see #getFormProperty()
	 * @generated
	 */
  void setFormProperty(FormPropertyType value);

  /**
	 * Returns the value of the '<em><b>In</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Element to specify Data Input in activiti Shortcuts (compare to DataInputAssociation in BPMN)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>In</em>' containment reference.
	 * @see #setIn(InType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_In()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='in' namespace='##targetNamespace'"
	 * @generated
	 */
  InType getIn();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getIn <em>In</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>In</em>' containment reference.
	 * @see #getIn()
	 * @generated
	 */
  void setIn(InType value);

  /**
	 * Returns the value of the '<em><b>Out</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Element to specify Data Output in activiti Shortcuts (compare to DataOutputAssociation in BPMN)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Out</em>' containment reference.
	 * @see #setOut(OutType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_Out()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='out' namespace='##targetNamespace'"
	 * @generated
	 */
  OutType getOut();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getOut <em>Out</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Out</em>' containment reference.
	 * @see #getOut()
	 * @generated
	 */
  void setOut(OutType value);

  /**
	 * Returns the value of the '<em><b>Task Listener</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Extension element for User Tasks used to execute custom Java logic or an expression upon the occurrence of a certain event.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Task Listener</em>' containment reference.
	 * @see #setTaskListener(TaskListenerType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_TaskListener()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='taskListener' namespace='##targetNamespace'"
	 * @generated
	 */
  TaskListenerType getTaskListener();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getTaskListener <em>Task Listener</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Task Listener</em>' containment reference.
	 * @see #getTaskListener()
	 * @generated
	 */
  void setTaskListener(TaskListenerType value);

  /**
	 * Returns the value of the '<em><b>Assignee</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * User Task attribute to set the human performer of a user task. Also supports expressions that evaluate to a String.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Assignee</em>' attribute.
	 * @see #setAssignee(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_Assignee()
	 * @model extendedMetaData="kind='attribute' name='assignee' namespace='##targetNamespace'"
	 * @generated
	 */
  String getAssignee();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getAssignee <em>Assignee</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assignee</em>' attribute.
	 * @see #getAssignee()
	 * @generated
	 */
  void setAssignee(String value);

  /**
	 * Returns the value of the '<em><b>Candidate Groups</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * User Task attribute to set the potential owners of a user task. The provided group(s) will be candidate for performing the user task. In case of multiple group ids, a comma-separated list must be provided. Also supports expressions that evaluate to a String or Collection<String>.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Candidate Groups</em>' attribute.
	 * @see #setCandidateGroups(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_CandidateGroups()
	 * @model extendedMetaData="kind='attribute' name='candidateGroups' namespace='##targetNamespace'"
	 * @generated
	 */
  String getCandidateGroups();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getCandidateGroups <em>Candidate Groups</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Candidate Groups</em>' attribute.
	 * @see #getCandidateGroups()
	 * @generated
	 */
  void setCandidateGroups(String value);

  /**
	 * Returns the value of the '<em><b>Candidate Users</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * User Task attribute to set the potential owners of a user task. The provided user(s) will be candidate for performing the user task. In case of multiple user ids, a comma-separated list must be provided. Also supports expressions that evaluate to a String or Collection<String>.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Candidate Users</em>' attribute.
	 * @see #setCandidateUsers(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_CandidateUsers()
	 * @model extendedMetaData="kind='attribute' name='candidateUsers' namespace='##targetNamespace'"
	 * @generated
	 */
  String getCandidateUsers();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getCandidateUsers <em>Candidate Users</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Candidate Users</em>' attribute.
	 * @see #getCandidateUsers()
	 * @generated
	 */
  void setCandidateUsers(String value);

  /**
	 * Returns the value of the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Service Task attribute for specifying a fully qualified Java classname. The Java class must implement either org.activiti.activiti.delegate.JavaDelegate or org.activiti.activiti.impl.pvm.delegate.ActivityBehavior
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Class</em>' attribute.
	 * @see #setClass(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_Class()
	 * @model dataType="org.camunda.bpm.modeler.runtime.engine.model.ClassType"
	 *        extendedMetaData="kind='attribute' name='class' namespace='##targetNamespace'"
	 * @generated
	 */
  String getClass_();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getClass_ <em>Class</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class</em>' attribute.
	 * @see #getClass_()
	 * @generated
	 */
  void setClass(String value);

  /**
	 * Returns the value of the '<em><b>Collection</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * To be used on the multiInstanceLoopCharacteristics element, referencing a collection. For each element in the collection, an instance will be created. Can be an expression or reference to a process variable.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Collection</em>' attribute.
	 * @see #setCollection(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_Collection()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='collection' namespace='##targetNamespace'"
	 * @generated
	 */
  String getCollection();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getCollection <em>Collection</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Collection</em>' attribute.
	 * @see #getCollection()
	 * @generated
	 */
  void setCollection(String value);

  /**
	 * Returns the value of the '<em><b>Delegate Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Allows to specify an expression on a service task, taskListener or executionListener that at runtime must resolve to an object that implements the corresponsing interface (JavaDelegate, ActivityBehavior, TaskListener, ExecutionListener, etc.)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Delegate Expression</em>' attribute.
	 * @see #setDelegateExpression(Object)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_DelegateExpression()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
	 *        extendedMetaData="kind='attribute' name='delegateExpression' namespace='##targetNamespace'"
	 * @generated
	 */
  Object getDelegateExpression();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getDelegateExpression <em>Delegate Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Delegate Expression</em>' attribute.
	 * @see #getDelegateExpression()
	 * @generated
	 */
  void setDelegateExpression(Object value);

  /**
	 * Returns the value of the '<em><b>Due Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * User Task attribute to set the task due date. The expression should resolve to a value of typejava.util.Date.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Due Date</em>' attribute.
	 * @see #setDueDate(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_DueDate()
	 * @model dataType="org.camunda.bpm.modeler.runtime.engine.model.TExpression"
	 *        extendedMetaData="kind='attribute' name='dueDate' namespace='##targetNamespace'"
	 * @generated
	 */
  String getDueDate();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getDueDate <em>Due Date</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Due Date</em>' attribute.
	 * @see #getDueDate()
	 * @generated
	 */
  void setDueDate(String value);

  /**
	 * Returns the value of the '<em><b>Element Variable</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * To be used on the multiInstanceLoopCharacteristics element, used in conjunction with the activiti:collection attribute. Denotes the name of the process variable that will be set on each created instance, containing an element of the specified collection.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Element Variable</em>' attribute.
	 * @see #setElementVariable(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_ElementVariable()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='elementVariable' namespace='##targetNamespace'"
	 * @generated
	 */
  String getElementVariable();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getElementVariable <em>Element Variable</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element Variable</em>' attribute.
	 * @see #getElementVariable()
	 * @generated
	 */
  void setElementVariable(String value);

  /**
	 * Returns the value of the '<em><b>Form Handler Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Attribute on a startEvent or userTask. Allows to specify a custom class that will be called during the parsing of the form information. Thus way, it is possible to use custom forms and form handling. This class must implement the org.activiti.activiti.inpl.form.FormHamdler/StartFormHandler/taskFormHandler interface (specific interface depending on the activity).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Form Handler Class</em>' attribute.
	 * @see #setFormHandlerClass(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_FormHandlerClass()
	 * @model dataType="org.camunda.bpm.modeler.runtime.engine.model.FormHandlerClassType"
	 *        extendedMetaData="kind='attribute' name='formHandlerClass' namespace='##targetNamespace'"
	 * @generated
	 */
  String getFormHandlerClass();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFormHandlerClass <em>Form Handler Class</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Form Handler Class</em>' attribute.
	 * @see #getFormHandlerClass()
	 * @generated
	 */
  void setFormHandlerClass(String value);

  /**
	 * Returns the value of the '<em><b>Form Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Attribute used on a startEvent or a userTask. The value can be anything. The default form support in activiti assumes that this is a reference to a form html file inside the deployment of the process definition. But this key can also be something completely different, in case of external form resolving.
	 *       
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Form Key</em>' attribute.
	 * @see #setFormKey(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_FormKey()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='formKey' namespace='##targetNamespace'"
	 * @generated
	 */
  String getFormKey();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFormKey <em>Form Key</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Form Key</em>' attribute.
	 * @see #getFormKey()
	 * @generated
	 */
  void setFormKey(String value);

  /**
	 * Returns the value of the '<em><b>History</b></em>' attribute.
	 * The literals are from the enumeration {@link org.camunda.bpm.modeler.runtime.engine.model.HistoryType}.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Attribute on the process element. Allows to set the history level for this specific process definition differently from the history level set in the process activiti configuration.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>History</em>' attribute.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.HistoryType
	 * @see #isSetHistory()
	 * @see #unsetHistory()
	 * @see #setHistory(HistoryType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_History()
	 * @model unsettable="true"
	 *        extendedMetaData="kind='attribute' name='history' namespace='##targetNamespace'"
	 * @generated
	 */
  HistoryType getHistory();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getHistory <em>History</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>History</em>' attribute.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.HistoryType
	 * @see #isSetHistory()
	 * @see #unsetHistory()
	 * @see #getHistory()
	 * @generated
	 */
  void setHistory(HistoryType value);

  /**
	 * Unsets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getHistory <em>History</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #isSetHistory()
	 * @see #getHistory()
	 * @see #setHistory(HistoryType)
	 * @generated
	 */
  void unsetHistory();

  /**
	 * Returns whether the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getHistory <em>History</em>}' attribute is set.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return whether the value of the '<em>History</em>' attribute is set.
	 * @see #unsetHistory()
	 * @see #getHistory()
	 * @see #setHistory(HistoryType)
	 * @generated
	 */
  boolean isSetHistory();

  /**
	 * Returns the value of the '<em><b>Initiator</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Attribute on a start event. Denotes a process variable in which the process initiator set in the identityService.setAuthenticatedUserId(userId) is captured.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Initiator</em>' attribute.
	 * @see #setInitiator(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_Initiator()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='initiator' namespace='##targetNamespace'"
	 * @generated
	 */
  String getInitiator();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getInitiator <em>Initiator</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initiator</em>' attribute.
	 * @see #getInitiator()
	 * @generated
	 */
  void setInitiator(String value);

  /**
	 * Returns the value of the '<em><b>Result Variable</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Attribute on Service and Script Task corresponding with a process variable name. The result of executing the service task logic or the script will be stored in this process variable.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Result Variable</em>' attribute.
	 * @see #setResultVariable(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_ResultVariable()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='resultVariable' namespace='##targetNamespace'"
	 * @generated
	 */
  String getResultVariable();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getResultVariable <em>Result Variable</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result Variable</em>' attribute.
	 * @see #getResultVariable()
	 * @generated
	 */
  void setResultVariable(String value);

  /**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.camunda.bpm.modeler.runtime.engine.model.TypeType}.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Service Task attribute specifying a built-in service task implementation.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TypeType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(TypeType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_Type()
	 * @model unsettable="true"
	 *        extendedMetaData="kind='attribute' name='type' namespace='##targetNamespace'"
	 * @generated
	 */
  TypeType getType();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TypeType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
  void setType(TypeType value);

  /**
	 * Unsets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(TypeType)
	 * @generated
	 */
  void unsetType();

  /**
	 * Returns whether the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getType <em>Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(TypeType)
	 * @generated
	 */
  boolean isSetType();

  /**
	 * Returns the value of the '<em><b>Async</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Async</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Async</em>' attribute.
	 * @see #setAsync(boolean)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_Async()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='async' namespace='##targetNamespace'"
	 * @generated
	 */
  boolean isAsync();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#isAsync <em>Async</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Async</em>' attribute.
	 * @see #isAsync()
	 * @generated
	 */
  void setAsync(boolean value);

  /**
	 * Returns the value of the '<em><b>Act Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Act Expression</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Act Expression</em>' attribute.
	 * @see #setActExpression(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_ActExpression()
	 * @model extendedMetaData="kind='attribute' name='expression' namespace='##targetNamespace'"
	 * @generated
	 */
  String getActExpression();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getActExpression <em>Act Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Act Expression</em>' attribute.
	 * @see #getActExpression()
	 * @generated
	 */
  void setActExpression(String value);

  /**
	 * Returns the value of the '<em><b>Priority</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * User Task attribute to set the priority. The expression should resolve to a value of type int.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Priority</em>' attribute.
	 * @see #setPriority(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_Priority()
	 * @model default=""
	 *        extendedMetaData="kind='attribute' name='priority' namespace='##targetNamespace'"
	 * @generated
	 */
	String getPriority();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getPriority <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Priority</em>' attribute.
	 * @see #getPriority()
	 * @generated
	 */
	void setPriority(String value);

  /**
	 * Returns the value of the '<em><b>Result Variable Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Attribute on Service and Script Task corresponding with a process variable name. The result of executing the service task logic or the script will be stored in this process variable.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Result Variable Name</em>' attribute.
	 * @see #setResultVariableName(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_ResultVariableName()
	 * @model extendedMetaData="kind='attribute' name='resultVariableName' namespace='##targetNamespace'"
	 * @generated
	 */
  String getResultVariableName();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getResultVariableName <em>Result Variable Name</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result Variable Name</em>' attribute.
	 * @see #getResultVariableName()
	 * @generated
	 */
  void setResultVariableName(String value);

  /**
	 * Returns the value of the '<em><b>Failed Job Retry Time Cycle</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Failed Job Retry Time Cycle</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Failed Job Retry Time Cycle</em>' containment reference.
	 * @see #setFailedJobRetryTimeCycle(FailedJobRetryTimeCycleType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getDocumentRoot_FailedJobRetryTimeCycle()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='failedJobRetryTimeCycle' namespace='http://www.camunda.com/fox'"
	 * @generated
	 */
  FailedJobRetryTimeCycleType getFailedJobRetryTimeCycle();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot#getFailedJobRetryTimeCycle <em>Failed Job Retry Time Cycle</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Failed Job Retry Time Cycle</em>' containment reference.
	 * @see #getFailedJobRetryTimeCycle()
	 * @generated
	 */
  void setFailedJobRetryTimeCycle(FailedJobRetryTimeCycleType value);

} // DocumentRoot
