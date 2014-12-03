/**
 */
package org.camunda.bpm.modeler.runtime.engine.model.impl;

import org.camunda.bpm.modeler.runtime.engine.model.ConnectorType;
import org.camunda.bpm.modeler.runtime.engine.model.InputOutputType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Connector Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.ConnectorTypeImpl#getConnectorId <em>Connector Id</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.ConnectorTypeImpl#getInputOutput <em>Input Output</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConnectorTypeImpl extends EObjectImpl implements ConnectorType {
	/**
	 * The default value of the '{@link #getConnectorId() <em>Connector Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnectorId()
	 * @generated
	 * @ordered
	 */
	protected static final String CONNECTOR_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getConnectorId() <em>Connector Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnectorId()
	 * @generated
	 * @ordered
	 */
	protected String connectorId = CONNECTOR_ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInputOutput() <em>Input Output</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputOutput()
	 * @generated
	 * @ordered
	 */
	protected InputOutputType inputOutput;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConnectorTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.CONNECTOR_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getConnectorId() {
		return connectorId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConnectorId(String newConnectorId) {
		String oldConnectorId = connectorId;
		connectorId = newConnectorId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CONNECTOR_TYPE__CONNECTOR_ID, oldConnectorId, connectorId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InputOutputType getInputOutput() {
		return inputOutput;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInputOutput(InputOutputType newInputOutput, NotificationChain msgs) {
		InputOutputType oldInputOutput = inputOutput;
		inputOutput = newInputOutput;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.CONNECTOR_TYPE__INPUT_OUTPUT, oldInputOutput, newInputOutput);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInputOutput(InputOutputType newInputOutput) {
		if (newInputOutput != inputOutput) {
			NotificationChain msgs = null;
			if (inputOutput != null)
				msgs = ((InternalEObject)inputOutput).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.CONNECTOR_TYPE__INPUT_OUTPUT, null, msgs);
			if (newInputOutput != null)
				msgs = ((InternalEObject)newInputOutput).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.CONNECTOR_TYPE__INPUT_OUTPUT, null, msgs);
			msgs = basicSetInputOutput(newInputOutput, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CONNECTOR_TYPE__INPUT_OUTPUT, newInputOutput, newInputOutput));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.CONNECTOR_TYPE__INPUT_OUTPUT:
				return basicSetInputOutput(null, msgs);
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
			case ModelPackage.CONNECTOR_TYPE__CONNECTOR_ID:
				return getConnectorId();
			case ModelPackage.CONNECTOR_TYPE__INPUT_OUTPUT:
				return getInputOutput();
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
			case ModelPackage.CONNECTOR_TYPE__CONNECTOR_ID:
				setConnectorId((String)newValue);
				return;
			case ModelPackage.CONNECTOR_TYPE__INPUT_OUTPUT:
				setInputOutput((InputOutputType)newValue);
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
			case ModelPackage.CONNECTOR_TYPE__CONNECTOR_ID:
				setConnectorId(CONNECTOR_ID_EDEFAULT);
				return;
			case ModelPackage.CONNECTOR_TYPE__INPUT_OUTPUT:
				setInputOutput((InputOutputType)null);
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
			case ModelPackage.CONNECTOR_TYPE__CONNECTOR_ID:
				return CONNECTOR_ID_EDEFAULT == null ? connectorId != null : !CONNECTOR_ID_EDEFAULT.equals(connectorId);
			case ModelPackage.CONNECTOR_TYPE__INPUT_OUTPUT:
				return inputOutput != null;
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
		result.append(" (connectorId: ");
		result.append(connectorId);
		result.append(')');
		return result.toString();
	}

} //ConnectorTypeImpl
