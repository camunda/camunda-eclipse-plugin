/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.bpmn2.modeler.runtime.activiti.model.impl;

import java.util.Collection;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.FormPropertyType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;

import org.eclipse.bpmn2.modeler.runtime.activiti.model.ValueType;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Form Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.impl.FormPropertyTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.impl.FormPropertyTypeImpl#getDatePattern <em>Date Pattern</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.impl.FormPropertyTypeImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.impl.FormPropertyTypeImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.impl.FormPropertyTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.impl.FormPropertyTypeImpl#getReadable <em>Readable</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.impl.FormPropertyTypeImpl#getRequired <em>Required</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.impl.FormPropertyTypeImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.impl.FormPropertyTypeImpl#getValue1 <em>Value1</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.impl.FormPropertyTypeImpl#getVariable <em>Variable</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.impl.FormPropertyTypeImpl#getWritable <em>Writable</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.impl.FormPropertyTypeImpl#getDefault <em>Default</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FormPropertyTypeImpl extends EObjectImpl implements FormPropertyType {
	/**
   * The cached value of the '{@link #getValue() <em>Value</em>}' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
	protected EList<ValueType> value;

	/**
   * The default value of the '{@link #getDatePattern() <em>Date Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getDatePattern()
   * @generated
   * @ordered
   */
	protected static final String DATE_PATTERN_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getDatePattern() <em>Date Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getDatePattern()
   * @generated
   * @ordered
   */
	protected String datePattern = DATE_PATTERN_EDEFAULT;

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
   * The default value of the '{@link #getId() <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getId()
   * @generated
   * @ordered
   */
	protected static final String ID_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getId()
   * @generated
   * @ordered
   */
	protected String id = ID_EDEFAULT;

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
   * The default value of the '{@link #getReadable() <em>Readable</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getReadable()
   * @generated
   * @ordered
   */
	protected static final String READABLE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getReadable() <em>Readable</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getReadable()
   * @generated
   * @ordered
   */
	protected String readable = READABLE_EDEFAULT;

	/**
   * The default value of the '{@link #getRequired() <em>Required</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getRequired()
   * @generated
   * @ordered
   */
	protected static final String REQUIRED_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getRequired() <em>Required</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getRequired()
   * @generated
   * @ordered
   */
	protected String required = REQUIRED_EDEFAULT;

	/**
   * The default value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
	protected static final String TYPE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
	protected String type = TYPE_EDEFAULT;

	/**
   * The default value of the '{@link #getValue1() <em>Value1</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getValue1()
   * @generated
   * @ordered
   */
	protected static final String VALUE1_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getValue1() <em>Value1</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getValue1()
   * @generated
   * @ordered
   */
	protected String value1 = VALUE1_EDEFAULT;

	/**
   * The default value of the '{@link #getVariable() <em>Variable</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getVariable()
   * @generated
   * @ordered
   */
	protected static final String VARIABLE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getVariable() <em>Variable</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getVariable()
   * @generated
   * @ordered
   */
	protected String variable = VARIABLE_EDEFAULT;

	/**
   * The default value of the '{@link #getWritable() <em>Writable</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getWritable()
   * @generated
   * @ordered
   */
	protected static final String WRITABLE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getWritable() <em>Writable</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getWritable()
   * @generated
   * @ordered
   */
	protected String writable = WRITABLE_EDEFAULT;

	/**
   * The default value of the '{@link #getDefault() <em>Default</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefault()
   * @generated
   * @ordered
   */
  protected static final String DEFAULT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDefault() <em>Default</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefault()
   * @generated
   * @ordered
   */
  protected String default_ = DEFAULT_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected FormPropertyTypeImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	protected EClass eStaticClass() {
    return ModelPackage.Literals.FORM_PROPERTY_TYPE;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EList<ValueType> getValue() {
    if (value == null) {
      value = new EObjectContainmentEList<ValueType>(ValueType.class, this, ModelPackage.FORM_PROPERTY_TYPE__VALUE);
    }
    return value;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getDatePattern() {
    return datePattern;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setDatePattern(String newDatePattern) {
    String oldDatePattern = datePattern;
    datePattern = newDatePattern;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FORM_PROPERTY_TYPE__DATE_PATTERN, oldDatePattern, datePattern));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FORM_PROPERTY_TYPE__EXPRESSION, oldExpression, expression));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getId() {
    return id;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setId(String newId) {
    String oldId = id;
    id = newId;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FORM_PROPERTY_TYPE__ID, oldId, id));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FORM_PROPERTY_TYPE__NAME, oldName, name));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getReadable() {
    return readable;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setReadable(String newReadable) {
    String oldReadable = readable;
    readable = newReadable;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FORM_PROPERTY_TYPE__READABLE, oldReadable, readable));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getRequired() {
    return required;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setRequired(String newRequired) {
    String oldRequired = required;
    required = newRequired;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FORM_PROPERTY_TYPE__REQUIRED, oldRequired, required));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getType() {
    return type;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setType(String newType) {
    String oldType = type;
    type = newType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FORM_PROPERTY_TYPE__TYPE, oldType, type));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getValue1() {
    return value1;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setValue1(String newValue1) {
    String oldValue1 = value1;
    value1 = newValue1;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FORM_PROPERTY_TYPE__VALUE1, oldValue1, value1));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getVariable() {
    return variable;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setVariable(String newVariable) {
    String oldVariable = variable;
    variable = newVariable;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FORM_PROPERTY_TYPE__VARIABLE, oldVariable, variable));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getWritable() {
    return writable;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setWritable(String newWritable) {
    String oldWritable = writable;
    writable = newWritable;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FORM_PROPERTY_TYPE__WRITABLE, oldWritable, writable));
  }

	/**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDefault() {
    return default_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDefault(String newDefault) {
    String oldDefault = default_;
    default_ = newDefault;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FORM_PROPERTY_TYPE__DEFAULT, oldDefault, default_));
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case ModelPackage.FORM_PROPERTY_TYPE__VALUE:
        return ((InternalEList<?>)getValue()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case ModelPackage.FORM_PROPERTY_TYPE__VALUE:
        return getValue();
      case ModelPackage.FORM_PROPERTY_TYPE__DATE_PATTERN:
        return getDatePattern();
      case ModelPackage.FORM_PROPERTY_TYPE__EXPRESSION:
        return getExpression();
      case ModelPackage.FORM_PROPERTY_TYPE__ID:
        return getId();
      case ModelPackage.FORM_PROPERTY_TYPE__NAME:
        return getName();
      case ModelPackage.FORM_PROPERTY_TYPE__READABLE:
        return getReadable();
      case ModelPackage.FORM_PROPERTY_TYPE__REQUIRED:
        return getRequired();
      case ModelPackage.FORM_PROPERTY_TYPE__TYPE:
        return getType();
      case ModelPackage.FORM_PROPERTY_TYPE__VALUE1:
        return getValue1();
      case ModelPackage.FORM_PROPERTY_TYPE__VARIABLE:
        return getVariable();
      case ModelPackage.FORM_PROPERTY_TYPE__WRITABLE:
        return getWritable();
      case ModelPackage.FORM_PROPERTY_TYPE__DEFAULT:
        return getDefault();
    }
    return super.eGet(featureID, resolve, coreType);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
    switch (featureID) {
      case ModelPackage.FORM_PROPERTY_TYPE__VALUE:
        getValue().clear();
        getValue().addAll((Collection<? extends ValueType>)newValue);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__DATE_PATTERN:
        setDatePattern((String)newValue);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__EXPRESSION:
        setExpression((String)newValue);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__ID:
        setId((String)newValue);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__NAME:
        setName((String)newValue);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__READABLE:
        setReadable((String)newValue);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__REQUIRED:
        setRequired((String)newValue);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__TYPE:
        setType((String)newValue);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__VALUE1:
        setValue1((String)newValue);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__VARIABLE:
        setVariable((String)newValue);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__WRITABLE:
        setWritable((String)newValue);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__DEFAULT:
        setDefault((String)newValue);
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
      case ModelPackage.FORM_PROPERTY_TYPE__VALUE:
        getValue().clear();
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__DATE_PATTERN:
        setDatePattern(DATE_PATTERN_EDEFAULT);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__EXPRESSION:
        setExpression(EXPRESSION_EDEFAULT);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__ID:
        setId(ID_EDEFAULT);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__READABLE:
        setReadable(READABLE_EDEFAULT);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__REQUIRED:
        setRequired(REQUIRED_EDEFAULT);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__TYPE:
        setType(TYPE_EDEFAULT);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__VALUE1:
        setValue1(VALUE1_EDEFAULT);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__VARIABLE:
        setVariable(VARIABLE_EDEFAULT);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__WRITABLE:
        setWritable(WRITABLE_EDEFAULT);
        return;
      case ModelPackage.FORM_PROPERTY_TYPE__DEFAULT:
        setDefault(DEFAULT_EDEFAULT);
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
      case ModelPackage.FORM_PROPERTY_TYPE__VALUE:
        return value != null && !value.isEmpty();
      case ModelPackage.FORM_PROPERTY_TYPE__DATE_PATTERN:
        return DATE_PATTERN_EDEFAULT == null ? datePattern != null : !DATE_PATTERN_EDEFAULT.equals(datePattern);
      case ModelPackage.FORM_PROPERTY_TYPE__EXPRESSION:
        return EXPRESSION_EDEFAULT == null ? expression != null : !EXPRESSION_EDEFAULT.equals(expression);
      case ModelPackage.FORM_PROPERTY_TYPE__ID:
        return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
      case ModelPackage.FORM_PROPERTY_TYPE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ModelPackage.FORM_PROPERTY_TYPE__READABLE:
        return READABLE_EDEFAULT == null ? readable != null : !READABLE_EDEFAULT.equals(readable);
      case ModelPackage.FORM_PROPERTY_TYPE__REQUIRED:
        return REQUIRED_EDEFAULT == null ? required != null : !REQUIRED_EDEFAULT.equals(required);
      case ModelPackage.FORM_PROPERTY_TYPE__TYPE:
        return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
      case ModelPackage.FORM_PROPERTY_TYPE__VALUE1:
        return VALUE1_EDEFAULT == null ? value1 != null : !VALUE1_EDEFAULT.equals(value1);
      case ModelPackage.FORM_PROPERTY_TYPE__VARIABLE:
        return VARIABLE_EDEFAULT == null ? variable != null : !VARIABLE_EDEFAULT.equals(variable);
      case ModelPackage.FORM_PROPERTY_TYPE__WRITABLE:
        return WRITABLE_EDEFAULT == null ? writable != null : !WRITABLE_EDEFAULT.equals(writable);
      case ModelPackage.FORM_PROPERTY_TYPE__DEFAULT:
        return DEFAULT_EDEFAULT == null ? default_ != null : !DEFAULT_EDEFAULT.equals(default_);
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
    result.append(" (datePattern: ");
    result.append(datePattern);
    result.append(", expression: ");
    result.append(expression);
    result.append(", id: ");
    result.append(id);
    result.append(", name: ");
    result.append(name);
    result.append(", readable: ");
    result.append(readable);
    result.append(", required: ");
    result.append(required);
    result.append(", type: ");
    result.append(type);
    result.append(", value1: ");
    result.append(value1);
    result.append(", variable: ");
    result.append(variable);
    result.append(", writable: ");
    result.append(writable);
    result.append(", default: ");
    result.append(default_);
    result.append(')');
    return result.toString();
  }

} //FormPropertyTypeImpl
