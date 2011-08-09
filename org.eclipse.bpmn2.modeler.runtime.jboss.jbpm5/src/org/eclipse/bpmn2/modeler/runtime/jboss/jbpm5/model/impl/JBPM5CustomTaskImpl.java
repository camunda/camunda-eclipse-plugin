/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl;

import java.util.Collection;

import org.eclipse.bpmn2.impl.TaskImpl;

import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Parameter;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>JBPM5 Custom Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.JBPM5CustomTaskImpl#getTaskName <em>Task Name</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.JBPM5CustomTaskImpl#getDisplayName <em>Display Name</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.JBPM5CustomTaskImpl#getIcon <em>Icon</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.JBPM5CustomTaskImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.JBPM5CustomTaskImpl#getResults <em>Results</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JBPM5CustomTaskImpl extends TaskImpl implements JBPM5CustomTask {
	/**
	 * The default value of the '{@link #getTaskName() <em>Task Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaskName()
	 * @generated
	 * @ordered
	 */
	protected static final String TASK_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTaskName() <em>Task Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaskName()
	 * @generated
	 * @ordered
	 */
	protected String taskName = TASK_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDisplayName() <em>Display Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplayName()
	 * @generated
	 * @ordered
	 */
	protected static final String DISPLAY_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDisplayName() <em>Display Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplayName()
	 * @generated
	 * @ordered
	 */
	protected String displayName = DISPLAY_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getIcon() <em>Icon</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIcon()
	 * @generated
	 * @ordered
	 */
	protected static final String ICON_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIcon() <em>Icon</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIcon()
	 * @generated
	 * @ordered
	 */
	protected String icon = ICON_EDEFAULT;

	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<Parameter> parameters;

	/**
	 * The cached value of the '{@link #getResults() <em>Results</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResults()
	 * @generated
	 * @ordered
	 */
	protected EList<Parameter> results;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JBPM5CustomTaskImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.JBPM5_CUSTOM_TASK;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTaskName(String newTaskName) {
		String oldTaskName = taskName;
		taskName = newTaskName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.JBPM5_CUSTOM_TASK__TASK_NAME, oldTaskName, taskName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDisplayName(String newDisplayName) {
		String oldDisplayName = displayName;
		displayName = newDisplayName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.JBPM5_CUSTOM_TASK__DISPLAY_NAME, oldDisplayName, displayName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIcon(String newIcon) {
		String oldIcon = icon;
		icon = newIcon;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.JBPM5_CUSTOM_TASK__ICON, oldIcon, icon));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Parameter> getParameters() {
		if (parameters == null) {
			parameters = new EObjectContainmentEList<Parameter>(Parameter.class, this, ModelPackage.JBPM5_CUSTOM_TASK__PARAMETERS);
		}
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Parameter> getResults() {
		if (results == null) {
			results = new EObjectContainmentEList<Parameter>(Parameter.class, this, ModelPackage.JBPM5_CUSTOM_TASK__RESULTS);
		}
		return results;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.JBPM5_CUSTOM_TASK__PARAMETERS:
				return ((InternalEList<?>)getParameters()).basicRemove(otherEnd, msgs);
			case ModelPackage.JBPM5_CUSTOM_TASK__RESULTS:
				return ((InternalEList<?>)getResults()).basicRemove(otherEnd, msgs);
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
			case ModelPackage.JBPM5_CUSTOM_TASK__TASK_NAME:
				return getTaskName();
			case ModelPackage.JBPM5_CUSTOM_TASK__DISPLAY_NAME:
				return getDisplayName();
			case ModelPackage.JBPM5_CUSTOM_TASK__ICON:
				return getIcon();
			case ModelPackage.JBPM5_CUSTOM_TASK__PARAMETERS:
				return getParameters();
			case ModelPackage.JBPM5_CUSTOM_TASK__RESULTS:
				return getResults();
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
			case ModelPackage.JBPM5_CUSTOM_TASK__TASK_NAME:
				setTaskName((String)newValue);
				return;
			case ModelPackage.JBPM5_CUSTOM_TASK__DISPLAY_NAME:
				setDisplayName((String)newValue);
				return;
			case ModelPackage.JBPM5_CUSTOM_TASK__ICON:
				setIcon((String)newValue);
				return;
			case ModelPackage.JBPM5_CUSTOM_TASK__PARAMETERS:
				getParameters().clear();
				getParameters().addAll((Collection<? extends Parameter>)newValue);
				return;
			case ModelPackage.JBPM5_CUSTOM_TASK__RESULTS:
				getResults().clear();
				getResults().addAll((Collection<? extends Parameter>)newValue);
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
			case ModelPackage.JBPM5_CUSTOM_TASK__TASK_NAME:
				setTaskName(TASK_NAME_EDEFAULT);
				return;
			case ModelPackage.JBPM5_CUSTOM_TASK__DISPLAY_NAME:
				setDisplayName(DISPLAY_NAME_EDEFAULT);
				return;
			case ModelPackage.JBPM5_CUSTOM_TASK__ICON:
				setIcon(ICON_EDEFAULT);
				return;
			case ModelPackage.JBPM5_CUSTOM_TASK__PARAMETERS:
				getParameters().clear();
				return;
			case ModelPackage.JBPM5_CUSTOM_TASK__RESULTS:
				getResults().clear();
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
			case ModelPackage.JBPM5_CUSTOM_TASK__TASK_NAME:
				return TASK_NAME_EDEFAULT == null ? taskName != null : !TASK_NAME_EDEFAULT.equals(taskName);
			case ModelPackage.JBPM5_CUSTOM_TASK__DISPLAY_NAME:
				return DISPLAY_NAME_EDEFAULT == null ? displayName != null : !DISPLAY_NAME_EDEFAULT.equals(displayName);
			case ModelPackage.JBPM5_CUSTOM_TASK__ICON:
				return ICON_EDEFAULT == null ? icon != null : !ICON_EDEFAULT.equals(icon);
			case ModelPackage.JBPM5_CUSTOM_TASK__PARAMETERS:
				return parameters != null && !parameters.isEmpty();
			case ModelPackage.JBPM5_CUSTOM_TASK__RESULTS:
				return results != null && !results.isEmpty();
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
		result.append(" (taskName: ");
		result.append(taskName);
		result.append(", displayName: ");
		result.append(displayName);
		result.append(", icon: ");
		result.append(icon);
		result.append(')');
		return result.toString();
	}

} //JBPM5CustomTaskImpl
