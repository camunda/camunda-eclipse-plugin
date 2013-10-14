/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Form Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getValue <em>Value</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getDatePattern <em>Date Pattern</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getId <em>Id</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getName <em>Name</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getReadable <em>Readable</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getRequired <em>Required</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getType <em>Type</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getValue1 <em>Value1</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getVariable <em>Variable</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getWritable <em>Writable</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getDefault <em>Default</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType()
 * @model extendedMetaData="name='formProperty_._type' kind='empty'"
 * @generated
 */
public interface FormPropertyType extends EObject {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference list.
	 * The list contents are of type {@link org.camunda.bpm.modeler.runtime.engine.model.ValueType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Defines a (potential) value for the form property. Especially usedful when using 'enum' as type.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Value</em>' containment reference list.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType_Value()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='value' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<ValueType> getValue();

	/**
	 * Returns the value of the '<em><b>Date Pattern</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Can be used when type is 'date' and defines how a date should be provided
	 *  in the form. Any date pattern that is compatible
	 * 	 with java.text.SimpleDataFormat is valid.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Date Pattern</em>' attribute.
	 * @see #setDatePattern(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType_DatePattern()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='datePattern'"
	 * @generated
	 */
	String getDatePattern();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getDatePattern <em>Date Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date Pattern</em>' attribute.
	 * @see #getDatePattern()
	 * @generated
	 */
	void setDatePattern(String value);

	/**
	 * Returns the value of the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies an expression that maps the property, eg. ${street.address}
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Expression</em>' attribute.
	 * @see #setExpression(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType_Expression()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='expression'"
	 * @generated
	 */
	String getExpression();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getExpression <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression</em>' attribute.
	 * @see #getExpression()
	 * @generated
	 */
	void setExpression(String value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The key used to submit the property through the API.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType_Id()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='id'"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getId <em>Id</em>}' attribute.
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
	 * The display label of the property.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Readable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies if the property can be read and displayed in the form.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Readable</em>' attribute.
	 * @see #setReadable(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType_Readable()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='readable'"
	 * @generated
	 */
	String getReadable();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getReadable <em>Readable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Readable</em>' attribute.
	 * @see #getReadable()
	 * @generated
	 */
	void setReadable(String value);

	/**
	 * Returns the value of the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies if the property is a required field.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Required</em>' attribute.
	 * @see #setRequired(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType_Required()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='required'"
	 * @generated
	 */
	String getRequired();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getRequired <em>Required</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Required</em>' attribute.
	 * @see #getRequired()
	 * @generated
	 */
	void setRequired(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The type of the property (see documentation for supported types). Default is 'string'.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType_Type()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='type'"
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Value1</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A literal or an expression that evaluates at runtime to the value for
	 *  the form property.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Value1</em>' attribute.
	 * @see #setValue1(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType_Value1()
	 * @model dataType="org.camunda.bpm.modeler.runtime.engine.model.TExpression"
	 *        extendedMetaData="kind='attribute' name='value'"
	 * @generated
	 */
	String getValue1();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getValue1 <em>Value1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value1</em>' attribute.
	 * @see #getValue1()
	 * @generated
	 */
	void setValue1(String value);

	/**
	 * Returns the value of the '<em><b>Variable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies the process variable on which the variable is mapped.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Variable</em>' attribute.
	 * @see #setVariable(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType_Variable()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='variable'"
	 * @generated
	 */
	String getVariable();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getVariable <em>Variable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Variable</em>' attribute.
	 * @see #getVariable()
	 * @generated
	 */
	void setVariable(String value);

	/**
	 * Returns the value of the '<em><b>Writable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies if the property is expected when the form is submitted.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Writable</em>' attribute.
	 * @see #setWritable(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType_Writable()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='writable'"
	 * @generated
	 */
	String getWritable();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getWritable <em>Writable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Writable</em>' attribute.
	 * @see #getWritable()
	 * @generated
	 */
	void setWritable(String value);

  /**
	 * Returns the value of the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Default</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Default</em>' attribute.
	 * @see #setDefault(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getFormPropertyType_Default()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
  String getDefault();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType#getDefault <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default</em>' attribute.
	 * @see #getDefault()
	 * @generated
	 */
  void setDefault(String value);

} // FormPropertyType
