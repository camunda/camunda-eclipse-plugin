/**
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Task Listener Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getGroup <em>Group</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getField <em>Field</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getClass_ <em>Class</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getDelegateExpression <em>Delegate Expression</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getEvent <em>Event</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getExpression <em>Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getTaskListenerType()
 * @model extendedMetaData="name='taskListener_._type' kind='elementOnly'"
 * @generated
 */
public interface TaskListenerType extends EObject {
  /**
	 * Returns the value of the '<em><b>Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Group</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute list.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getTaskListenerType_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:0'"
	 * @generated
	 */
  FeatureMap getGroup();

  /**
	 * Returns the value of the '<em><b>Field</b></em>' containment reference list.
	 * The list contents are of type {@link org.camunda.bpm.modeler.runtime.engine.model.FieldType}.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Extension Element for Service Tasks to inject values into the fields of delegate classes.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Field</em>' containment reference list.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getTaskListenerType_Field()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='field' namespace='##targetNamespace' group='#group:0'"
	 * @generated
	 */
  EList<FieldType> getField();

  /**
	 * Returns the value of the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * An implementation of the org.activiti.activiti.impl.pvm.delegate.TaskListener interface that will be called when the task event occurs.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Class</em>' attribute.
	 * @see #setClass(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getTaskListenerType_Class()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='class'"
	 * @generated
	 */
  String getClass_();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getClass_ <em>Class</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class</em>' attribute.
	 * @see #getClass_()
	 * @generated
	 */
  void setClass(String value);

  /**
	 * Returns the value of the '<em><b>Delegate Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Expression that must resolve to an object implementing a compatible interface for a taskListener. Evaluation and delegation to the resulting object is done when the task event occurs.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Delegate Expression</em>' attribute.
	 * @see #setDelegateExpression(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getTaskListenerType_DelegateExpression()
	 * @model dataType="org.camunda.bpm.modeler.runtime.engine.model.TExpression"
	 *        extendedMetaData="kind='attribute' name='delegateExpression'"
	 * @generated
	 */
  String getDelegateExpression();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getDelegateExpression <em>Delegate Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Delegate Expression</em>' attribute.
	 * @see #getDelegateExpression()
	 * @generated
	 */
  void setDelegateExpression(String value);

  /**
	 * Returns the value of the '<em><b>Event</b></em>' attribute.
	 * The literals are from the enumeration {@link org.camunda.bpm.modeler.runtime.engine.model.EventType}.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The event on which the delegation class or expression will be executed.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Event</em>' attribute.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType
	 * @see #isSetEvent()
	 * @see #unsetEvent()
	 * @see #setEvent(EventType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getTaskListenerType_Event()
	 * @model unsettable="true" required="true"
	 *        extendedMetaData="kind='attribute' name='event'"
	 * @generated
	 */
  EventType getEvent();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getEvent <em>Event</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Event</em>' attribute.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.EventType
	 * @see #isSetEvent()
	 * @see #unsetEvent()
	 * @see #getEvent()
	 * @generated
	 */
  void setEvent(EventType value);

  /**
	 * Unsets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getEvent <em>Event</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #isSetEvent()
	 * @see #getEvent()
	 * @see #setEvent(EventType)
	 * @generated
	 */
  void unsetEvent();

  /**
	 * Returns whether the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getEvent <em>Event</em>}' attribute is set.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Event</em>' attribute is set.
	 * @see #unsetEvent()
	 * @see #getEvent()
	 * @see #setEvent(EventType)
	 * @generated
	 */
  boolean isSetEvent();

  /**
	 * Returns the value of the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Expression that will be evaluated when the task event occurs.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Expression</em>' attribute.
	 * @see #setExpression(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getTaskListenerType_Expression()
	 * @model dataType="org.camunda.bpm.modeler.runtime.engine.model.TExpression"
	 *        extendedMetaData="kind='attribute' name='expression'"
	 * @generated
	 */
  String getExpression();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType#getExpression <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression</em>' attribute.
	 * @see #getExpression()
	 * @generated
	 */
  void setExpression(String value);

} // TaskListenerType
