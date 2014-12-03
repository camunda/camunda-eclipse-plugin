/**
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connector Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.ConnectorType#getConnectorId <em>Connector Id</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.ConnectorType#getInputOutput <em>Input Output</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getConnectorType()
 * @model extendedMetaData="name='connector_._type' kind='elementOnly'"
 * @generated
 */
public interface ConnectorType extends EObject {
	/**
	 * Returns the value of the '<em><b>Connector Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connector Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connector Id</em>' attribute.
	 * @see #setConnectorId(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getConnectorType_ConnectorId()
	 * @model extendedMetaData="kind='element' name='connectorId' namespace='##targetNamespace'"
	 * @generated
	 */
	String getConnectorId();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.ConnectorType#getConnectorId <em>Connector Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connector Id</em>' attribute.
	 * @see #getConnectorId()
	 * @generated
	 */
	void setConnectorId(String value);

	/**
	 * Returns the value of the '<em><b>Input Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Output</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Output</em>' containment reference.
	 * @see #setInputOutput(InputOutputType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getConnectorType_InputOutput()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='inputOutput' namespace='##targetNamespace'"
	 * @generated
	 */
	InputOutputType getInputOutput();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.ConnectorType#getInputOutput <em>Input Output</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input Output</em>' containment reference.
	 * @see #getInputOutput()
	 * @generated
	 */
	void setInputOutput(InputOutputType value);

} // ConnectorType
