/**
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Form Field Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getId <em>Id</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getLabel <em>Label</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getType <em>Type</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getValidation <em>Validation</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormFieldType()
 * @model extendedMetaData="name='formField_._type' kind='elementOnly'"
 * @generated
 */
public interface FormFieldType extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormFieldType_Id()
	 * @model extendedMetaData="kind='attribute' name='id'"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see #setLabel(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormFieldType_Label()
	 * @model extendedMetaData="kind='attribute' label='label'"
	 * @generated
	 */
	String getLabel();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormFieldType_Type()
	 * @model extendedMetaData="kind='attribute' name='type'"
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Value</em>' attribute.
	 * @see #setDefaultValue(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormFieldType_DefaultValue()
	 * @model extendedMetaData="kind='attribute' name='defaultValue'"
	 * @generated
	 */
	String getDefaultValue();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getDefaultValue <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Value</em>' attribute.
	 * @see #getDefaultValue()
	 * @generated
	 */
	void setDefaultValue(String value);

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' containment reference.
	 * @see #setProperties(PropertiesType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormFieldType_Properties()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='properties' namespace='##targetNamespace'"
	 * @generated
	 */
	PropertiesType getProperties();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getProperties <em>Properties</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Properties</em>' containment reference.
	 * @see #getProperties()
	 * @generated
	 */
	void setProperties(PropertiesType value);

	/**
	 * Returns the value of the '<em><b>Validation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Validation</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Validation</em>' containment reference.
	 * @see #setValidation(ValidationType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormFieldType_Validation()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='validation' namespace='##targetNamespace'"
	 * @generated
	 */
	ValidationType getValidation();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType#getValidation <em>Validation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Validation</em>' containment reference.
	 * @see #getValidation()
	 * @generated
	 */
	void setValidation(ValidationType value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference list.
	 * The list contents are of type {@link org.camunda.bpm.modeler.runtime.engine.model.ValueType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' containment reference list.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormFieldType_Value()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='value' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<ValueType> getValue();

} // FormFieldType
