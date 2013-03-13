/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.camunda.bpm.modeler.runtime.activiti.model.impl;

import org.camunda.bpm.modeler.runtime.activiti.model.FieldType;
import org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Field Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.impl.FieldTypeImpl#getString <em>String</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.impl.FieldTypeImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.impl.FieldTypeImpl#getExpression1 <em>Expression1</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.impl.FieldTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.impl.FieldTypeImpl#getStringValue <em>String Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FieldTypeImpl extends EObjectImpl implements FieldType {
	/**
   * The default value of the '{@link #getString() <em>String</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getString()
   * @generated
   * @ordered
   */
	protected static final String STRING_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getString() <em>String</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getString()
   * @generated
   * @ordered
   */
	protected String string = STRING_EDEFAULT;

	/**
   * The default value of the '{@link #getExpression() <em>Expression</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getExpression()
   * @generated
   * @ordered
   */
	protected static final String EXPRESSION_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getExpression() <em>Expression</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getExpression()
   * @generated
   * @ordered
   */
	protected String expression = EXPRESSION_EDEFAULT;

	/**
   * The default value of the '{@link #getExpression1() <em>Expression1</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getExpression1()
   * @generated
   * @ordered
   */
	protected static final String EXPRESSION1_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getExpression1() <em>Expression1</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getExpression1()
   * @generated
   * @ordered
   */
	protected String expression1 = EXPRESSION1_EDEFAULT;

	/**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
	protected static final String NAME_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
	protected String name = NAME_EDEFAULT;

	/**
   * The default value of the '{@link #getStringValue() <em>String Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getStringValue()
   * @generated
   * @ordered
   */
	protected static final String STRING_VALUE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getStringValue() <em>String Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getStringValue()
   * @generated
   * @ordered
   */
	protected String stringValue = STRING_VALUE_EDEFAULT;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected FieldTypeImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	protected EClass eStaticClass() {
    return ModelPackage.Literals.FIELD_TYPE;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getString() {
    return string;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setString(String newString) {
    String oldString = string;
    string = newString;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FIELD_TYPE__STRING, oldString, string));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getExpression() {
    return expression;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setExpression(String newExpression) {
    String oldExpression = expression;
    expression = newExpression;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FIELD_TYPE__EXPRESSION, oldExpression, expression));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getExpression1() {
    return expression1;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setExpression1(String newExpression1) {
    String oldExpression1 = expression1;
    expression1 = newExpression1;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FIELD_TYPE__EXPRESSION1, oldExpression1, expression1));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getName() {
    return name;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setName(String newName) {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FIELD_TYPE__NAME, oldName, name));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getStringValue() {
    return stringValue;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setStringValue(String newStringValue) {
    String oldStringValue = stringValue;
    stringValue = newStringValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FIELD_TYPE__STRING_VALUE, oldStringValue, stringValue));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case ModelPackage.FIELD_TYPE__STRING:
        return getString();
      case ModelPackage.FIELD_TYPE__EXPRESSION:
        return getExpression();
      case ModelPackage.FIELD_TYPE__EXPRESSION1:
        return getExpression1();
      case ModelPackage.FIELD_TYPE__NAME:
        return getName();
      case ModelPackage.FIELD_TYPE__STRING_VALUE:
        return getStringValue();
    }
    return super.eGet(featureID, resolve, coreType);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public void eSet(int featureID, Object newValue) {
    switch (featureID) {
      case ModelPackage.FIELD_TYPE__STRING:
        setString((String)newValue);
        return;
      case ModelPackage.FIELD_TYPE__EXPRESSION:
        setExpression((String)newValue);
        return;
      case ModelPackage.FIELD_TYPE__EXPRESSION1:
        setExpression1((String)newValue);
        return;
      case ModelPackage.FIELD_TYPE__NAME:
        setName((String)newValue);
        return;
      case ModelPackage.FIELD_TYPE__STRING_VALUE:
        setStringValue((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public void eUnset(int featureID) {
    switch (featureID) {
      case ModelPackage.FIELD_TYPE__STRING:
        setString(STRING_EDEFAULT);
        return;
      case ModelPackage.FIELD_TYPE__EXPRESSION:
        setExpression(EXPRESSION_EDEFAULT);
        return;
      case ModelPackage.FIELD_TYPE__EXPRESSION1:
        setExpression1(EXPRESSION1_EDEFAULT);
        return;
      case ModelPackage.FIELD_TYPE__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ModelPackage.FIELD_TYPE__STRING_VALUE:
        setStringValue(STRING_VALUE_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public boolean eIsSet(int featureID) {
    switch (featureID) {
      case ModelPackage.FIELD_TYPE__STRING:
        return STRING_EDEFAULT == null ? string != null : !STRING_EDEFAULT.equals(string);
      case ModelPackage.FIELD_TYPE__EXPRESSION:
        return EXPRESSION_EDEFAULT == null ? expression != null : !EXPRESSION_EDEFAULT.equals(expression);
      case ModelPackage.FIELD_TYPE__EXPRESSION1:
        return EXPRESSION1_EDEFAULT == null ? expression1 != null : !EXPRESSION1_EDEFAULT.equals(expression1);
      case ModelPackage.FIELD_TYPE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ModelPackage.FIELD_TYPE__STRING_VALUE:
        return STRING_VALUE_EDEFAULT == null ? stringValue != null : !STRING_VALUE_EDEFAULT.equals(stringValue);
    }
    return super.eIsSet(featureID);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public String toString() {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (string: ");
    result.append(string);
    result.append(", expression: ");
    result.append(expression);
    result.append(", expression1: ");
    result.append(expression1);
    result.append(", name: ");
    result.append(name);
    result.append(", stringValue: ");
    result.append(stringValue);
    result.append(')');
    return result.toString();
  }

} //FieldTypeImpl
