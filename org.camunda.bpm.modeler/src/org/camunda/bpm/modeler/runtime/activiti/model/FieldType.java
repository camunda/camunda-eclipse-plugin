/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.camunda.bpm.modeler.runtime.activiti.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Field Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.FieldType#getString <em>String</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.FieldType#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.FieldType#getExpression1 <em>Expression1</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.FieldType#getName <em>Name</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.FieldType#getStringValue <em>String Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage#getFieldType()
 * @model extendedMetaData="name='field_._type' kind='elementOnly'"
 * @generated
 */
public interface FieldType extends EObject {
	/**
   * Returns the value of the '<em><b>String</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>String</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>String</em>' attribute.
   * @see #setString(String)
   * @see org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage#getFieldType_String()
   * @model dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='element' name='string' namespace='##targetNamespace'"
   * @generated
   */
	String getString();

	/**
   * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.activiti.model.FieldType#getString <em>String</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>String</em>' attribute.
   * @see #getString()
   * @generated
   */
	void setString(String value);

	/**
   * Returns the value of the '<em><b>Expression</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Expression</em>' attribute.
   * @see #setExpression(String)
   * @see org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage#getFieldType_Expression()
   * @model dataType="org.eclipse.bpmn2.modeler.runtime.activiti.model.TExpression"
   *        extendedMetaData="kind='element' name='expression' namespace='##targetNamespace'"
   * @generated
   */
	String getExpression();

	/**
   * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.activiti.model.FieldType#getExpression <em>Expression</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Expression</em>' attribute.
   * @see #getExpression()
   * @generated
   */
	void setExpression(String value);

	/**
   * Returns the value of the '<em><b>Expression1</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression1</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Expression1</em>' attribute.
   * @see #setExpression1(String)
   * @see org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage#getFieldType_Expression1()
   * @model dataType="org.eclipse.bpmn2.modeler.runtime.activiti.model.TExpression"
   *        extendedMetaData="kind='attribute' name='expression'"
   * @generated
   */
	String getExpression1();

	/**
   * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.activiti.model.FieldType#getExpression1 <em>Expression1</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Expression1</em>' attribute.
   * @see #getExpression1()
   * @generated
   */
	void setExpression1(String value);

	/**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage#getFieldType_Name()
   * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   *        extendedMetaData="kind='attribute' name='name'"
   * @generated
   */
	String getName();

	/**
   * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.activiti.model.FieldType#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
	void setName(String value);

	/**
   * Returns the value of the '<em><b>String Value</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>String Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>String Value</em>' attribute.
   * @see #setStringValue(String)
   * @see org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage#getFieldType_StringValue()
   * @model dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='stringValue'"
   * @generated
   */
	String getStringValue();

	/**
   * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.activiti.model.FieldType#getStringValue <em>String Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>String Value</em>' attribute.
   * @see #getStringValue()
   * @generated
   */
	void setStringValue(String value);

} // FieldType
