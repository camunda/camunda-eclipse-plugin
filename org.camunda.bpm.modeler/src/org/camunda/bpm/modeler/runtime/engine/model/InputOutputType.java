/**
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Output Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.InputOutputType#getInputParameters <em>Input Parameters</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.InputOutputType#getOutputParameters <em>Output Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getInputOutputType()
 * @model extendedMetaData="name='inputOutput_._type' kind='elementOnly'"
 * @generated
 */
public interface InputOutputType extends EObject {
	/**
	 * Returns the value of the '<em><b>Input Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link org.camunda.bpm.modeler.runtime.engine.model.ParameterType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Parameters</em>' containment reference list.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getInputOutputType_InputParameters()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='inputParameter' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<ParameterType> getInputParameters();

	/**
	 * Returns the value of the '<em><b>Output Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link org.camunda.bpm.modeler.runtime.engine.model.ParameterType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output Parameters</em>' containment reference list.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getInputOutputType_OutputParameters()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='outputParameter' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<ParameterType> getOutputParameters();

} // InputOutputType
