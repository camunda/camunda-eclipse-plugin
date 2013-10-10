/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import java.math.BigInteger;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Call Activity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.CallActivity#getCalledElement <em>Called Element</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.CallActivity#getCalledElementBinding <em>Called Element Binding</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.CallActivity#getCalledElementVersion <em>Called Element Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getCallActivity()
 * @model extendedMetaData="name='tCallActivity' kind='elementOnly'"
 * @generated
 */
public interface CallActivity extends org.eclipse.bpmn2.CallActivity {
  /**
	 * Returns the value of the '<em><b>Called Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Called Element</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Called Element</em>' attribute.
	 * @see #setCalledElement(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getCallActivity_CalledElement()
	 * @model extendedMetaData="kind='attribute' name='calledElement'"
	 * @generated
	 */
  String getCalledElement();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.CallActivity#getCalledElement <em>Called Element</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Called Element</em>' attribute.
	 * @see #getCalledElement()
	 * @generated
	 */
  void setCalledElement(String value);

  /**
	 * Returns the value of the '<em><b>Called Element Binding</b></em>' attribute.
	 * The default value is <code>"latest"</code>.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Called Element Binding</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Called Element Binding</em>' attribute.
	 * @see #setCalledElementBinding(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getCallActivity_CalledElementBinding()
	 * @model default="latest"
	 *        extendedMetaData="kind='attribute' name='calledElementBinding'"
	 * @generated
	 */
  String getCalledElementBinding();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.CallActivity#getCalledElementBinding <em>Called Element Binding</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Called Element Binding</em>' attribute.
	 * @see #getCalledElementBinding()
	 * @generated
	 */
  void setCalledElementBinding(String value);

  /**
	 * Returns the value of the '<em><b>Called Element Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Called Element Version</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Called Element Version</em>' attribute.
	 * @see #setCalledElementVersion(Integer)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getCallActivity_CalledElementVersion()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.IntObject"
	 *        extendedMetaData="kind='attribute' name='calledElementVersion'"
	 * @generated
	 */
  Integer getCalledElementVersion();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.CallActivity#getCalledElementVersion <em>Called Element Version</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Called Element Version</em>' attribute.
	 * @see #getCalledElementVersion()
	 * @generated
	 */
  void setCalledElementVersion(Integer value);

} // CallActivity
