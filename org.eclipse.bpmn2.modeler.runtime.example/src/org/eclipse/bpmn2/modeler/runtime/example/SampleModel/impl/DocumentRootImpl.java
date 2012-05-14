/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.bpmn2.modeler.runtime.example.SampleModel.impl;

import org.eclipse.bpmn2.modeler.runtime.example.SampleModel.DocumentRoot;
import org.eclipse.bpmn2.modeler.runtime.example.SampleModel.SampleModelPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.example.SampleModel.impl.DocumentRootImpl#getSampleCustomTaskId <em>Sample Custom Task Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends org.eclipse.bpmn2.impl.DocumentRootImpl implements DocumentRoot {
	/**
	 * The default value of the '{@link #getSampleCustomTaskId() <em>Sample Custom Task Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSampleCustomTaskId()
	 * @generated
	 * @ordered
	 */
	protected static final String SAMPLE_CUSTOM_TASK_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSampleCustomTaskId() <em>Sample Custom Task Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSampleCustomTaskId()
	 * @generated
	 * @ordered
	 */
	protected String sampleCustomTaskId = SAMPLE_CUSTOM_TASK_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DocumentRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SampleModelPackage.Literals.DOCUMENT_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSampleCustomTaskId() {
		return sampleCustomTaskId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSampleCustomTaskId(String newSampleCustomTaskId) {
		String oldSampleCustomTaskId = sampleCustomTaskId;
		sampleCustomTaskId = newSampleCustomTaskId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SampleModelPackage.DOCUMENT_ROOT__SAMPLE_CUSTOM_TASK_ID, oldSampleCustomTaskId, sampleCustomTaskId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SampleModelPackage.DOCUMENT_ROOT__SAMPLE_CUSTOM_TASK_ID:
				return getSampleCustomTaskId();
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
			case SampleModelPackage.DOCUMENT_ROOT__SAMPLE_CUSTOM_TASK_ID:
				setSampleCustomTaskId((String)newValue);
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
			case SampleModelPackage.DOCUMENT_ROOT__SAMPLE_CUSTOM_TASK_ID:
				setSampleCustomTaskId(SAMPLE_CUSTOM_TASK_ID_EDEFAULT);
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
			case SampleModelPackage.DOCUMENT_ROOT__SAMPLE_CUSTOM_TASK_ID:
				return SAMPLE_CUSTOM_TASK_ID_EDEFAULT == null ? sampleCustomTaskId != null : !SAMPLE_CUSTOM_TASK_ID_EDEFAULT.equals(sampleCustomTaskId);
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
		result.append(" (sampleCustomTaskId: ");
		result.append(sampleCustomTaskId);
		result.append(')');
		return result.toString();
	}

} //DocumentRootImpl
