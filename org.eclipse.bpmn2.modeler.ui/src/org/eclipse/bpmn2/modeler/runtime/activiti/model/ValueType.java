/**
 */
package org.eclipse.bpmn2.modeler.runtime.activiti.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Defines a (potential) value for the form property. Especially useful when using 'enum' as type.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.ValueType#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.ValueType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage#getValueType()
 * @model extendedMetaData="name='value_._type' kind='empty'"
 * @generated
 */
public interface ValueType extends EObject {
	/**
   * Returns the value of the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Defines the internal value for the form property value.
   * <!-- end-model-doc -->
   * @return the value of the '<em>Id</em>' attribute.
   * @see #setId(String)
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage#getValueType_Id()
   * @model dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='id'"
   * @generated
   */
	String getId();

	/**
   * Sets the value of the '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.ValueType#getId <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Id</em>' attribute.
   * @see #getId()
   * @generated
   */
	void setId(String value);

	/**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Defines the display label for the form property value.
   * <!-- end-model-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage#getValueType_Name()
   * @model dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='name'"
   * @generated
   */
	String getName();

	/**
   * Sets the value of the '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.ValueType#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
	void setName(String value);

} // ValueType
