/**
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Script Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.ScriptType#getScriptFormat <em>Script Format</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.ScriptType#getResource <em>Resource</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.ScriptType#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.ScriptType#getText <em>Text</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getScriptType()
 * @model extendedMetaData="name='script_._type' kind='mixed'"
 * @generated
 */
public interface ScriptType extends EObject {
	/**
	 * Returns the value of the '<em><b>Script Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Script Format</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Script Format</em>' attribute.
	 * @see #setScriptFormat(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getScriptType_ScriptFormat()
	 * @model required="true" ordered="false"
	 *        extendedMetaData="kind='attribute' name='scriptFormat'"
	 * @generated
	 */
	String getScriptFormat();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.ScriptType#getScriptFormat <em>Script Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Script Format</em>' attribute.
	 * @see #getScriptFormat()
	 * @generated
	 */
	void setScriptFormat(String value);

	/**
	 * Returns the value of the '<em><b>Resource</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource</em>' attribute.
	 * @see #setResource(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getScriptType_Resource()
	 * @model extendedMetaData="kind='attribute' name='resource'"
	 * @generated
	 */
	String getResource();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.ScriptType#getResource <em>Resource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource</em>' attribute.
	 * @see #getResource()
	 * @generated
	 */
	void setResource(String value);

	/**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mixed</em>' attribute list.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getScriptType_Mixed()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='elementWildcard' name=':mixed'"
	 * @generated
	 */
	FeatureMap getMixed();

	/**
	 * Returns the value of the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Text</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Text</em>' attribute.
	 * @see #setText(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getScriptType_Text()
	 * @model transient="true" volatile="true" derived="true" ordered="false"
	 * @generated
	 */
	String getText();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.ScriptType#getText <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
	void setText(String value);

} // ScriptType
