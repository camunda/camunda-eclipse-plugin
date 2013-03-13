/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.camunda.bpm.modeler.runtime.activiti.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage
 * @generated
 */
public interface ModelFactory extends EFactory {
	/**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	ModelFactory eINSTANCE = org.camunda.bpm.modeler.runtime.activiti.model.impl.ModelFactoryImpl.init();

	/**
   * Returns a new object of class '<em>Document Root</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Document Root</em>'.
   * @generated
   */
	DocumentRoot createDocumentRoot();

	/**
   * Returns a new object of class '<em>Execution Listener Type</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Execution Listener Type</em>'.
   * @generated
   */
	ExecutionListenerType createExecutionListenerType();

	/**
   * Returns a new object of class '<em>Field Type</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Field Type</em>'.
   * @generated
   */
	FieldType createFieldType();

	/**
   * Returns a new object of class '<em>Form Property Type</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Form Property Type</em>'.
   * @generated
   */
	FormPropertyType createFormPropertyType();

	/**
   * Returns a new object of class '<em>In Type</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>In Type</em>'.
   * @generated
   */
	InType createInType();

	/**
   * Returns a new object of class '<em>Out Type</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Out Type</em>'.
   * @generated
   */
	OutType createOutType();

	/**
   * Returns a new object of class '<em>Task Listener Type</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Task Listener Type</em>'.
   * @generated
   */
	TaskListenerType createTaskListenerType();

	/**
   * Returns a new object of class '<em>Call Activity</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Call Activity</em>'.
   * @generated
   */
	CallActivity createCallActivity();

	/**
   * Returns a new object of class '<em>Boundary Event</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Boundary Event</em>'.
   * @generated
   */
	BoundaryEvent createBoundaryEvent();

	/**
   * Returns a new object of class '<em>Value Type</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Value Type</em>'.
   * @generated
   */
	ValueType createValueType();

	/**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
	ModelPackage getModelPackage();

} //ModelFactory
