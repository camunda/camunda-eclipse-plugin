/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl;

import java.math.BigInteger;

import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.DocumentRoot;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.GlobalType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ImportType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.OnEntryScriptType;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.OnExitScriptType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getGlobal <em>Global</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getImportType <em>Import Type</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getOnEntryScript <em>On Entry Script</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getOnExitScript <em>On Exit Script</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getPackageName <em>Package Name</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getPriority <em>Priority</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getRuleFlowGroup <em>Rule Flow Group</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getTaskName <em>Task Name</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#isWaitForCompletion <em>Wait For Completion</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.DocumentRootImpl#isIndependent <em>Independent</em>}</li>
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
	 * The default value of the '{@link #isWaitForCompletion() <em>Wait For Completion</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWaitForCompletion()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WAIT_FOR_COMPLETION_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWaitForCompletion() <em>Wait For Completion</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWaitForCompletion()
	 * @generated
	 * @ordered
	 */
	protected boolean waitForCompletion = WAIT_FOR_COMPLETION_EDEFAULT;

	/**
	 * The default value of the '{@link #isIndependent() <em>Independent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIndependent()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INDEPENDENT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIndependent() <em>Independent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIndependent()
	 * @generated
	 * @ordered
	 */
	protected boolean independent = INDEPENDENT_EDEFAULT;

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
	public GlobalType getGlobal() {
		return (GlobalType)getMixed().get(ModelPackage.Literals.DOCUMENT_ROOT__GLOBAL, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGlobal(GlobalType newGlobal, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(ModelPackage.Literals.DOCUMENT_ROOT__GLOBAL, newGlobal, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGlobal(GlobalType newGlobal) {
		((FeatureMap.Internal)getMixed()).set(ModelPackage.Literals.DOCUMENT_ROOT__GLOBAL, newGlobal);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImportType getImportType() {
		return (ImportType)getMixed().get(ModelPackage.Literals.DOCUMENT_ROOT__IMPORT_TYPE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetImportType(ImportType newImportType, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(ModelPackage.Literals.DOCUMENT_ROOT__IMPORT_TYPE, newImportType, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setImportType(ImportType newImportType) {
		((FeatureMap.Internal)getMixed()).set(ModelPackage.Literals.DOCUMENT_ROOT__IMPORT_TYPE, newImportType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OnEntryScriptType getOnEntryScript() {
		return (OnEntryScriptType)getMixed().get(ModelPackage.Literals.DOCUMENT_ROOT__ON_ENTRY_SCRIPT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOnEntryScript(OnEntryScriptType newOnEntryScript, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(ModelPackage.Literals.DOCUMENT_ROOT__ON_ENTRY_SCRIPT, newOnEntryScript, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOnEntryScript(OnEntryScriptType newOnEntryScript) {
		((FeatureMap.Internal)getMixed()).set(ModelPackage.Literals.DOCUMENT_ROOT__ON_ENTRY_SCRIPT, newOnEntryScript);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OnExitScriptType getOnExitScript() {
		return (OnExitScriptType)getMixed().get(ModelPackage.Literals.DOCUMENT_ROOT__ON_EXIT_SCRIPT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOnExitScript(OnExitScriptType newOnExitScript, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(ModelPackage.Literals.DOCUMENT_ROOT__ON_EXIT_SCRIPT, newOnExitScript, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOnExitScript(OnExitScriptType newOnExitScript) {
		((FeatureMap.Internal)getMixed()).set(ModelPackage.Literals.DOCUMENT_ROOT__ON_EXIT_SCRIPT, newOnExitScript);
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
	public boolean isWaitForCompletion() {
		return waitForCompletion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWaitForCompletion(boolean newWaitForCompletion) {
		boolean oldWaitForCompletion = waitForCompletion;
		waitForCompletion = newWaitForCompletion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DOCUMENT_ROOT__WAIT_FOR_COMPLETION, oldWaitForCompletion, waitForCompletion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIndependent() {
		return independent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndependent(boolean newIndependent) {
		boolean oldIndependent = independent;
		independent = newIndependent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DOCUMENT_ROOT__INDEPENDENT, oldIndependent, independent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.DOCUMENT_ROOT__GLOBAL:
				return basicSetGlobal(null, msgs);
			case ModelPackage.DOCUMENT_ROOT__IMPORT_TYPE:
				return basicSetImportType(null, msgs);
			case ModelPackage.DOCUMENT_ROOT__ON_ENTRY_SCRIPT:
				return basicSetOnEntryScript(null, msgs);
			case ModelPackage.DOCUMENT_ROOT__ON_EXIT_SCRIPT:
				return basicSetOnExitScript(null, msgs);
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
			case ModelPackage.DOCUMENT_ROOT__GLOBAL:
				return getGlobal();
			case ModelPackage.DOCUMENT_ROOT__IMPORT_TYPE:
				return getImportType();
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
			case ModelPackage.DOCUMENT_ROOT__WAIT_FOR_COMPLETION:
				return isWaitForCompletion();
			case ModelPackage.DOCUMENT_ROOT__INDEPENDENT:
				return isIndependent();
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
			case ModelPackage.DOCUMENT_ROOT__GLOBAL:
				setGlobal((GlobalType)newValue);
				return;
			case ModelPackage.DOCUMENT_ROOT__IMPORT_TYPE:
				setImportType((ImportType)newValue);
				return;
			case ModelPackage.DOCUMENT_ROOT__ON_ENTRY_SCRIPT:
				setOnEntryScript((OnEntryScriptType)newValue);
				return;
			case ModelPackage.DOCUMENT_ROOT__ON_EXIT_SCRIPT:
				setOnExitScript((OnExitScriptType)newValue);
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
			case ModelPackage.DOCUMENT_ROOT__WAIT_FOR_COMPLETION:
				setWaitForCompletion((Boolean)newValue);
				return;
			case ModelPackage.DOCUMENT_ROOT__INDEPENDENT:
				setIndependent((Boolean)newValue);
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
			case ModelPackage.DOCUMENT_ROOT__GLOBAL:
				setGlobal((GlobalType)null);
				return;
			case ModelPackage.DOCUMENT_ROOT__IMPORT_TYPE:
				setImportType((ImportType)null);
				return;
			case ModelPackage.DOCUMENT_ROOT__ON_ENTRY_SCRIPT:
				setOnEntryScript((OnEntryScriptType)null);
				return;
			case ModelPackage.DOCUMENT_ROOT__ON_EXIT_SCRIPT:
				setOnExitScript((OnExitScriptType)null);
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
			case ModelPackage.DOCUMENT_ROOT__WAIT_FOR_COMPLETION:
				setWaitForCompletion(WAIT_FOR_COMPLETION_EDEFAULT);
				return;
			case ModelPackage.DOCUMENT_ROOT__INDEPENDENT:
				setIndependent(INDEPENDENT_EDEFAULT);
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
			case ModelPackage.DOCUMENT_ROOT__GLOBAL:
				return getGlobal() != null;
			case ModelPackage.DOCUMENT_ROOT__IMPORT_TYPE:
				return getImportType() != null;
			case ModelPackage.DOCUMENT_ROOT__ON_ENTRY_SCRIPT:
				return getOnEntryScript() != null;
			case ModelPackage.DOCUMENT_ROOT__ON_EXIT_SCRIPT:
				return getOnExitScript() != null;
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
			case ModelPackage.DOCUMENT_ROOT__WAIT_FOR_COMPLETION:
				return waitForCompletion != WAIT_FOR_COMPLETION_EDEFAULT;
			case ModelPackage.DOCUMENT_ROOT__INDEPENDENT:
				return independent != INDEPENDENT_EDEFAULT;
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
		result.append(", waitForCompletion: ");
		result.append(waitForCompletion);
		result.append(", independent: ");
		result.append(independent);
		result.append(')');
		return result.toString();
	}

} //DocumentRootImpl
