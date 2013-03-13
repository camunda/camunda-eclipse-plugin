/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.camunda.bpm.modeler.runtime.activiti.model.impl;

import java.util.Collection;

import org.camunda.bpm.modeler.runtime.activiti.model.EventType;
import org.camunda.bpm.modeler.runtime.activiti.model.FieldType;
import org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.activiti.model.TaskListenerType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Task Listener Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.impl.TaskListenerTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.impl.TaskListenerTypeImpl#getField <em>Field</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.impl.TaskListenerTypeImpl#getClass_ <em>Class</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.impl.TaskListenerTypeImpl#getDelegateExpression <em>Delegate Expression</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.impl.TaskListenerTypeImpl#getEvent <em>Event</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.activiti.model.impl.TaskListenerTypeImpl#getExpression <em>Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TaskListenerTypeImpl extends EObjectImpl implements TaskListenerType {
	/**
   * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getGroup()
   * @generated
   * @ordered
   */
	protected FeatureMap group;

	/**
   * The default value of the '{@link #getClass_() <em>Class</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getClass_()
   * @generated
   * @ordered
   */
	protected static final String CLASS_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getClass_() <em>Class</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getClass_()
   * @generated
   * @ordered
   */
	protected String class_ = CLASS_EDEFAULT;

	/**
   * The default value of the '{@link #getDelegateExpression() <em>Delegate Expression</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getDelegateExpression()
   * @generated
   * @ordered
   */
	protected static final String DELEGATE_EXPRESSION_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getDelegateExpression() <em>Delegate Expression</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getDelegateExpression()
   * @generated
   * @ordered
   */
	protected String delegateExpression = DELEGATE_EXPRESSION_EDEFAULT;

	/**
   * The default value of the '{@link #getEvent() <em>Event</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getEvent()
   * @generated
   * @ordered
   */
	protected static final EventType EVENT_EDEFAULT = EventType.CREATE;

	/**
   * The cached value of the '{@link #getEvent() <em>Event</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getEvent()
   * @generated
   * @ordered
   */
	protected EventType event = EVENT_EDEFAULT;

	/**
   * This is true if the Event attribute has been set.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	protected boolean eventESet;

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
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected TaskListenerTypeImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	protected EClass eStaticClass() {
    return ModelPackage.Literals.TASK_LISTENER_TYPE;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public FeatureMap getGroup() {
    if (group == null) {
      group = new BasicFeatureMap(this, ModelPackage.TASK_LISTENER_TYPE__GROUP);
    }
    return group;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EList<FieldType> getField() {
    return getGroup().list(ModelPackage.Literals.TASK_LISTENER_TYPE__FIELD);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getClass_() {
    return class_;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setClass(String newClass) {
    String oldClass = class_;
    class_ = newClass;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TASK_LISTENER_TYPE__CLASS, oldClass, class_));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getDelegateExpression() {
    return delegateExpression;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setDelegateExpression(String newDelegateExpression) {
    String oldDelegateExpression = delegateExpression;
    delegateExpression = newDelegateExpression;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TASK_LISTENER_TYPE__DELEGATE_EXPRESSION, oldDelegateExpression, delegateExpression));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EventType getEvent() {
    return event;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setEvent(EventType newEvent) {
    EventType oldEvent = event;
    event = newEvent == null ? EVENT_EDEFAULT : newEvent;
    boolean oldEventESet = eventESet;
    eventESet = true;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TASK_LISTENER_TYPE__EVENT, oldEvent, event, !oldEventESet));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void unsetEvent() {
    EventType oldEvent = event;
    boolean oldEventESet = eventESet;
    event = EVENT_EDEFAULT;
    eventESet = false;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.TASK_LISTENER_TYPE__EVENT, oldEvent, EVENT_EDEFAULT, oldEventESet));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean isSetEvent() {
    return eventESet;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TASK_LISTENER_TYPE__EXPRESSION, oldExpression, expression));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case ModelPackage.TASK_LISTENER_TYPE__GROUP:
        return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
      case ModelPackage.TASK_LISTENER_TYPE__FIELD:
        return ((InternalEList<?>)getField()).basicRemove(otherEnd, msgs);
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
      case ModelPackage.TASK_LISTENER_TYPE__GROUP:
        if (coreType) return getGroup();
        return ((FeatureMap.Internal)getGroup()).getWrapper();
      case ModelPackage.TASK_LISTENER_TYPE__FIELD:
        return getField();
      case ModelPackage.TASK_LISTENER_TYPE__CLASS:
        return getClass_();
      case ModelPackage.TASK_LISTENER_TYPE__DELEGATE_EXPRESSION:
        return getDelegateExpression();
      case ModelPackage.TASK_LISTENER_TYPE__EVENT:
        return getEvent();
      case ModelPackage.TASK_LISTENER_TYPE__EXPRESSION:
        return getExpression();
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
      case ModelPackage.TASK_LISTENER_TYPE__GROUP:
        ((FeatureMap.Internal)getGroup()).set(newValue);
        return;
      case ModelPackage.TASK_LISTENER_TYPE__FIELD:
        getField().clear();
        getField().addAll((Collection<? extends FieldType>)newValue);
        return;
      case ModelPackage.TASK_LISTENER_TYPE__CLASS:
        setClass((String)newValue);
        return;
      case ModelPackage.TASK_LISTENER_TYPE__DELEGATE_EXPRESSION:
        setDelegateExpression((String)newValue);
        return;
      case ModelPackage.TASK_LISTENER_TYPE__EVENT:
        setEvent((EventType)newValue);
        return;
      case ModelPackage.TASK_LISTENER_TYPE__EXPRESSION:
        setExpression((String)newValue);
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
      case ModelPackage.TASK_LISTENER_TYPE__GROUP:
        getGroup().clear();
        return;
      case ModelPackage.TASK_LISTENER_TYPE__FIELD:
        getField().clear();
        return;
      case ModelPackage.TASK_LISTENER_TYPE__CLASS:
        setClass(CLASS_EDEFAULT);
        return;
      case ModelPackage.TASK_LISTENER_TYPE__DELEGATE_EXPRESSION:
        setDelegateExpression(DELEGATE_EXPRESSION_EDEFAULT);
        return;
      case ModelPackage.TASK_LISTENER_TYPE__EVENT:
        unsetEvent();
        return;
      case ModelPackage.TASK_LISTENER_TYPE__EXPRESSION:
        setExpression(EXPRESSION_EDEFAULT);
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
      case ModelPackage.TASK_LISTENER_TYPE__GROUP:
        return group != null && !group.isEmpty();
      case ModelPackage.TASK_LISTENER_TYPE__FIELD:
        return !getField().isEmpty();
      case ModelPackage.TASK_LISTENER_TYPE__CLASS:
        return CLASS_EDEFAULT == null ? class_ != null : !CLASS_EDEFAULT.equals(class_);
      case ModelPackage.TASK_LISTENER_TYPE__DELEGATE_EXPRESSION:
        return DELEGATE_EXPRESSION_EDEFAULT == null ? delegateExpression != null : !DELEGATE_EXPRESSION_EDEFAULT.equals(delegateExpression);
      case ModelPackage.TASK_LISTENER_TYPE__EVENT:
        return isSetEvent();
      case ModelPackage.TASK_LISTENER_TYPE__EXPRESSION:
        return EXPRESSION_EDEFAULT == null ? expression != null : !EXPRESSION_EDEFAULT.equals(expression);
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
    result.append(" (group: ");
    result.append(group);
    result.append(", class: ");
    result.append(class_);
    result.append(", delegateExpression: ");
    result.append(delegateExpression);
    result.append(", event: ");
    if (eventESet) result.append(event); else result.append("<unset>");
    result.append(", expression: ");
    result.append(expression);
    result.append(')');
    return result.toString();
  }

} //TaskListenerTypeImpl
