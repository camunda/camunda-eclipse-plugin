/**
 */
package org.camunda.bpm.modeler.runtime.engine.model.impl;

import java.math.BigInteger;
import org.camunda.bpm.modeler.runtime.engine.model.CallActivity;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Call Activity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.CallActivityImpl#getCalledElement <em>Called Element</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.CallActivityImpl#getCalledElementBinding <em>Called Element Binding</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.CallActivityImpl#getCalledElementVersion <em>Called Element Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CallActivityImpl extends org.eclipse.bpmn2.impl.CallActivityImpl implements CallActivity {
  /**
	 * The default value of the '{@link #getCalledElement() <em>Called Element</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getCalledElement()
	 * @generated
	 * @ordered
	 */
  protected static final String CALLED_ELEMENT_EDEFAULT = null;

  /**
	 * The cached value of the '{@link #getCalledElement() <em>Called Element</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getCalledElement()
	 * @generated
	 * @ordered
	 */
  protected String calledElement = CALLED_ELEMENT_EDEFAULT;

  /**
	 * The default value of the '{@link #getCalledElementBinding() <em>Called Element Binding</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getCalledElementBinding()
	 * @generated
	 * @ordered
	 */
  protected static final String CALLED_ELEMENT_BINDING_EDEFAULT = "latest";

  /**
	 * The cached value of the '{@link #getCalledElementBinding() <em>Called Element Binding</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getCalledElementBinding()
	 * @generated
	 * @ordered
	 */
  protected String calledElementBinding = CALLED_ELEMENT_BINDING_EDEFAULT;

  /**
	 * The default value of the '{@link #getCalledElementVersion() <em>Called Element Version</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getCalledElementVersion()
	 * @generated
	 * @ordered
	 */
  protected static final Integer CALLED_ELEMENT_VERSION_EDEFAULT = null;

  /**
	 * The cached value of the '{@link #getCalledElementVersion() <em>Called Element Version</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getCalledElementVersion()
	 * @generated
	 * @ordered
	 */
  protected Integer calledElementVersion = CALLED_ELEMENT_VERSION_EDEFAULT;

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  protected CallActivityImpl() {
		super();
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  @Override
  protected EClass eStaticClass() {
		return ModelPackage.Literals.CALL_ACTIVITY;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public String getCalledElement() {
		return calledElement;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public void setCalledElement(String newCalledElement) {
		String oldCalledElement = calledElement;
		calledElement = newCalledElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT, oldCalledElement, calledElement));
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public String getCalledElementBinding() {
		return calledElementBinding;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public void setCalledElementBinding(String newCalledElementBinding) {
		String oldCalledElementBinding = calledElementBinding;
		calledElementBinding = newCalledElementBinding;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT_BINDING, oldCalledElementBinding, calledElementBinding));
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public Integer getCalledElementVersion() {
		return calledElementVersion;
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  public void setCalledElementVersion(Integer newCalledElementVersion) {
		Integer oldCalledElementVersion = calledElementVersion;
		calledElementVersion = newCalledElementVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT_VERSION, oldCalledElementVersion, calledElementVersion));
	}

  /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT:
				return getCalledElement();
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT_BINDING:
				return getCalledElementBinding();
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT_VERSION:
				return getCalledElementVersion();
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT_REF:
				return getCalledElementRef();
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
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT:
				setCalledElement((String)newValue);
				return;
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT_BINDING:
				setCalledElementBinding((String)newValue);
				return;
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT_VERSION:
				setCalledElementVersion((Integer)newValue);
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
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT:
				setCalledElement(CALLED_ELEMENT_EDEFAULT);
				return;
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT_BINDING:
				setCalledElementBinding(CALLED_ELEMENT_BINDING_EDEFAULT);
				return;
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT_VERSION:
				setCalledElementVersion(CALLED_ELEMENT_VERSION_EDEFAULT);
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
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT:
				return CALLED_ELEMENT_EDEFAULT == null ? calledElement != null : !CALLED_ELEMENT_EDEFAULT.equals(calledElement);
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT_BINDING:
				return CALLED_ELEMENT_BINDING_EDEFAULT == null ? calledElementBinding != null : !CALLED_ELEMENT_BINDING_EDEFAULT.equals(calledElementBinding);
			case ModelPackage.CALL_ACTIVITY__CALLED_ELEMENT_VERSION:
				return CALLED_ELEMENT_VERSION_EDEFAULT == null ? calledElementVersion != null : !CALLED_ELEMENT_VERSION_EDEFAULT.equals(calledElementVersion);
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
		result.append(" (calledElement: ");
		result.append(calledElement);
		result.append(", calledElementBinding: ");
		result.append(calledElementBinding);
		result.append(", calledElementVersion: ");
		result.append(calledElementVersion);
		result.append(')');
		return result.toString();
	}

} //CallActivityImpl
