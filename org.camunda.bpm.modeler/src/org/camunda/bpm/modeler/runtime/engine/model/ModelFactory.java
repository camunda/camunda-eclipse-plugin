/**
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage
 * @generated
 */
public interface ModelFactory extends EFactory {
  /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  ModelFactory eINSTANCE = org.camunda.bpm.modeler.runtime.engine.model.impl.ModelFactoryImpl.init();

  /**
	 * Returns a new object of class '<em>Document Root</em>'.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return a new object of class '<em>Document Root</em>'.
	 * @generated
	 */
  DocumentRoot createDocumentRoot();

  /**
	 * Returns a new object of class '<em>Start Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Start Event</em>'.
	 * @generated
	 */
	StartEvent createStartEvent();

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
	 * Returns a new object of class '<em>Form Data Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Form Data Type</em>'.
	 * @generated
	 */
	FormDataType createFormDataType();

		/**
	 * Returns a new object of class '<em>Form Field Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Form Field Type</em>'.
	 * @generated
	 */
	FormFieldType createFormFieldType();

		/**
	 * Returns a new object of class '<em>Properties Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Properties Type</em>'.
	 * @generated
	 */
	PropertiesType createPropertiesType();

		/**
	 * Returns a new object of class '<em>Property Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Property Type</em>'.
	 * @generated
	 */
	PropertyType createPropertyType();

		/**
	 * Returns a new object of class '<em>Validation Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Validation Type</em>'.
	 * @generated
	 */
	ValidationType createValidationType();

		/**
	 * Returns a new object of class '<em>Constraint Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Constraint Type</em>'.
	 * @generated
	 */
	ConstraintType createConstraintType();

		/**
	 * Returns a new object of class '<em>User Task</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>User Task</em>'.
	 * @generated
	 */
	UserTask createUserTask();

		/**
	 * Returns a new object of class '<em>Form Data Container</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Form Data Container</em>'.
	 * @generated
	 */
	FormDataContainer createFormDataContainer();

		/**
	 * Returns a new object of class '<em>Expression</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Expression</em>'.
	 * @generated
	 */
	Expression createExpression();

		/**
	 * Returns a new object of class '<em>Connector Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Connector Type</em>'.
	 * @generated
	 */
	ConnectorType createConnectorType();

		/**
	 * Returns a new object of class '<em>Input Output Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Input Output Type</em>'.
	 * @generated
	 */
	InputOutputType createInputOutputType();

		/**
	 * Returns a new object of class '<em>Parameter Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parameter Type</em>'.
	 * @generated
	 */
	ParameterType createParameterType();

		/**
	 * Returns a new object of class '<em>Script Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Script Type</em>'.
	 * @generated
	 */
	ScriptType createScriptType();

		/**
	 * Returns a new object of class '<em>Map Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Map Type</em>'.
	 * @generated
	 */
	MapType createMapType();

		/**
	 * Returns a new object of class '<em>Entry Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entry Type</em>'.
	 * @generated
	 */
	EntryType createEntryType();

		/**
	 * Returns a new object of class '<em>List Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>List Type</em>'.
	 * @generated
	 */
	ListType createListType();

		/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
  ModelPackage getModelPackage();

} //ModelFactory
