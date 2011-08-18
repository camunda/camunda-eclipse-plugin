/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl;

import java.math.BigInteger;

import java.util.Collection;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.DocumentRoot;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.GlobalType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ImportType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.OnEntryScriptType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.OnExitScriptType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getGlobals <em>Globals</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getImports <em>Imports</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getOnEntryScript <em>On Entry Script</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getOnExitScript <em>On Exit Script</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getPackageName <em>Package Name</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getPriority <em>Priority</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getRuleFlowGroup <em>Rule Flow Group</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getTaskName <em>Task Name</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends org.eclipse.bpmn2.impl.DocumentRootImpl implements DocumentRoot {
	/**
	 * The default value of the '{@link #getPackageName() <em>Package Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackageName()
	 * @generated
	 * @ordered
	 */
	protected static final String PACKAGE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPackageName() <em>Package Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackageName()
	 * @generated
	 * @ordered
	 */
	protected String packageName = PACKAGE_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPriority() <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriority()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger PRIORITY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPriority() <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriority()
	 * @generated
	 * @ordered
	 */
	protected BigInteger priority = PRIORITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getRuleFlowGroup() <em>Rule Flow Group</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRuleFlowGroup()
	 * @generated
	 * @ordered
	 */
	protected static final String RULE_FLOW_GROUP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRuleFlowGroup() <em>Rule Flow Group</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRuleFlowGroup()
	 * @generated
	 * @ordered
	 */
	protected String ruleFlowGroup = RULE_FLOW_GROUP_EDEFAULT;

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
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected BigInteger version = VERSION_EDEFAULT;

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
		return ModelPackage.Literals.DOCUMENT_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<GlobalType> getGlobals() {
		return getMixed().list(ModelPackage.Literals.DOCUMENT_ROOT__GLOBALS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ImportType> getImports() {
		return getMixed().list(ModelPackage.Literals.DOCUMENT_ROOT__IMPORTS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OnEntryScriptType> getOnEntryScript() {
		return getMixed().list(ModelPackage.Literals.DOCUMENT_ROOT__ON_ENTRY_SCRIPT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OnExitScriptType> getOnExitScript() {
		return getMixed().list(ModelPackage.Literals.DOCUMENT_ROOT__ON_EXIT_SCRIPT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPackageName(String newPackageName) {
		String oldPackageName = packageName;
		packageName = newPackageName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DOCUMENT_ROOT__PACKAGE_NAME, oldPackageName, packageName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getPriority() {
		return priority;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPriority(BigInteger newPriority) {
		BigInteger oldPriority = priority;
		priority = newPriority;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DOCUMENT_ROOT__PRIORITY, oldPriority, priority));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRuleFlowGroup() {
		return ruleFlowGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRuleFlowGroup(String newRuleFlowGroup) {
		String oldRuleFlowGroup = ruleFlowGroup;
		ruleFlowGroup = newRuleFlowGroup;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DOCUMENT_ROOT__RULE_FLOW_GROUP, oldRuleFlowGroup, ruleFlowGroup));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DOCUMENT_ROOT__TASK_NAME, oldTaskName, taskName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(BigInteger newVersion) {
		BigInteger oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DOCUMENT_ROOT__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.DOCUMENT_ROOT__GLOBALS:
				return ((InternalEList<?>)getGlobals()).basicRemove(otherEnd, msgs);
			case ModelPackage.DOCUMENT_ROOT__IMPORTS:
				return ((InternalEList<?>)getImports()).basicRemove(otherEnd, msgs);
			case ModelPackage.DOCUMENT_ROOT__ON_ENTRY_SCRIPT:
				return ((InternalEList<?>)getOnEntryScript()).basicRemove(otherEnd, msgs);
			case ModelPackage.DOCUMENT_ROOT__ON_EXIT_SCRIPT:
				return ((InternalEList<?>)getOnExitScript()).basicRemove(otherEnd, msgs);
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
			case ModelPackage.DOCUMENT_ROOT__GLOBALS:
				return getGlobals();
			case ModelPackage.DOCUMENT_ROOT__IMPORTS:
				return getImports();
			case ModelPackage.DOCUMENT_ROOT__ON_ENTRY_SCRIPT:
				return getOnEntryScript();
			case ModelPackage.DOCUMENT_ROOT__ON_EXIT_SCRIPT:
				return getOnExitScript();
			case ModelPackage.DOCUMENT_ROOT__PACKAGE_NAME:
				return getPackageName();
			case ModelPackage.DOCUMENT_ROOT__PRIORITY:
				return getPriority();
			case ModelPackage.DOCUMENT_ROOT__RULE_FLOW_GROUP:
				return getRuleFlowGroup();
			case ModelPackage.DOCUMENT_ROOT__TASK_NAME:
				return getTaskName();
			case ModelPackage.DOCUMENT_ROOT__VERSION:
				return getVersion();
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
			case ModelPackage.DOCUMENT_ROOT__GLOBALS:
				getGlobals().clear();
				getGlobals().addAll((Collection<? extends GlobalType>)newValue);
				return;
			case ModelPackage.DOCUMENT_ROOT__IMPORTS:
				getImports().clear();
				getImports().addAll((Collection<? extends ImportType>)newValue);
				return;
			case ModelPackage.DOCUMENT_ROOT__ON_ENTRY_SCRIPT:
				getOnEntryScript().clear();
				getOnEntryScript().addAll((Collection<? extends OnEntryScriptType>)newValue);
				return;
			case ModelPackage.DOCUMENT_ROOT__ON_EXIT_SCRIPT:
				getOnExitScript().clear();
				getOnExitScript().addAll((Collection<? extends OnExitScriptType>)newValue);
				return;
			case ModelPackage.DOCUMENT_ROOT__PACKAGE_NAME:
				setPackageName((String)newValue);
				return;
			case ModelPackage.DOCUMENT_ROOT__PRIORITY:
				setPriority((BigInteger)newValue);
				return;
			case ModelPackage.DOCUMENT_ROOT__RULE_FLOW_GROUP:
				setRuleFlowGroup((String)newValue);
				return;
			case ModelPackage.DOCUMENT_ROOT__TASK_NAME:
				setTaskName((String)newValue);
				return;
			case ModelPackage.DOCUMENT_ROOT__VERSION:
				setVersion((BigInteger)newValue);
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
			case ModelPackage.DOCUMENT_ROOT__GLOBALS:
				getGlobals().clear();
				return;
			case ModelPackage.DOCUMENT_ROOT__IMPORTS:
				getImports().clear();
				return;
			case ModelPackage.DOCUMENT_ROOT__ON_ENTRY_SCRIPT:
				getOnEntryScript().clear();
				return;
			case ModelPackage.DOCUMENT_ROOT__ON_EXIT_SCRIPT:
				getOnExitScript().clear();
				return;
			case ModelPackage.DOCUMENT_ROOT__PACKAGE_NAME:
				setPackageName(PACKAGE_NAME_EDEFAULT);
				return;
			case ModelPackage.DOCUMENT_ROOT__PRIORITY:
				setPriority(PRIORITY_EDEFAULT);
				return;
			case ModelPackage.DOCUMENT_ROOT__RULE_FLOW_GROUP:
				setRuleFlowGroup(RULE_FLOW_GROUP_EDEFAULT);
				return;
			case ModelPackage.DOCUMENT_ROOT__TASK_NAME:
				setTaskName(TASK_NAME_EDEFAULT);
				return;
			case ModelPackage.DOCUMENT_ROOT__VERSION:
				setVersion(VERSION_EDEFAULT);
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
			case ModelPackage.DOCUMENT_ROOT__GLOBALS:
				return !getGlobals().isEmpty();
			case ModelPackage.DOCUMENT_ROOT__IMPORTS:
				return !getImports().isEmpty();
			case ModelPackage.DOCUMENT_ROOT__ON_ENTRY_SCRIPT:
				return !getOnEntryScript().isEmpty();
			case ModelPackage.DOCUMENT_ROOT__ON_EXIT_SCRIPT:
				return !getOnExitScript().isEmpty();
			case ModelPackage.DOCUMENT_ROOT__PACKAGE_NAME:
				return PACKAGE_NAME_EDEFAULT == null ? packageName != null : !PACKAGE_NAME_EDEFAULT.equals(packageName);
			case ModelPackage.DOCUMENT_ROOT__PRIORITY:
				return PRIORITY_EDEFAULT == null ? priority != null : !PRIORITY_EDEFAULT.equals(priority);
			case ModelPackage.DOCUMENT_ROOT__RULE_FLOW_GROUP:
				return RULE_FLOW_GROUP_EDEFAULT == null ? ruleFlowGroup != null : !RULE_FLOW_GROUP_EDEFAULT.equals(ruleFlowGroup);
			case ModelPackage.DOCUMENT_ROOT__TASK_NAME:
				return TASK_NAME_EDEFAULT == null ? taskName != null : !TASK_NAME_EDEFAULT.equals(taskName);
			case ModelPackage.DOCUMENT_ROOT__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
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
		result.append(" (packageName: ");
		result.append(packageName);
		result.append(", priority: ");
		result.append(priority);
		result.append(", ruleFlowGroup: ");
		result.append(ruleFlowGroup);
		result.append(", taskName: ");
		result.append(taskName);
		result.append(", version: ");
		result.append(version);
		result.append(')');
		return result.toString();
	}

} //DocumentRootImpl
