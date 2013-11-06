/**
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Form Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormDataType#getFormField <em>Form Field</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormDataType()
 * @model extendedMetaData="name='formData_._type' kind='elementOnly'"
 * @generated
 */
public interface FormDataType extends EObject {
	/**
	 * Returns the value of the '<em><b>Form Field</b></em>' containment reference list.
	 * The list contents are of type {@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Form Field</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Form Field</em>' containment reference list.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormDataType_FormField()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='formField' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<FormFieldType> getFormField();

} // FormDataType
