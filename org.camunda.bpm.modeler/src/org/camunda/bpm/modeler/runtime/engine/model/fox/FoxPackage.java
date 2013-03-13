/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.camunda.bpm.modeler.runtime.engine.model.fox;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.camunda.bpm.modeler.runtime.engine.model.fox.FoxFactory
 * @model kind="package"
 * @generated
 */
public interface FoxPackage extends EPackage {
	/**
   * The package name.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	String eNAME = "fox";

	/**
   * The package namespace URI.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	String eNS_URI = "http://www.camunda.com/fox";

	/**
   * The package namespace name.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	String eNS_PREFIX = "fox";

	/**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	FoxPackage eINSTANCE = org.camunda.bpm.modeler.runtime.engine.model.fox.impl.FoxPackageImpl.init();

	/**
   * The meta object id for the '{@link org.camunda.bpm.modeler.runtime.engine.model.fox.impl.FailedJobRetryTimeCycleTypeImpl <em>Failed Job Retry Time Cycle Type</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.camunda.bpm.modeler.runtime.engine.model.fox.impl.FailedJobRetryTimeCycleTypeImpl
   * @see org.camunda.bpm.modeler.runtime.engine.model.fox.impl.FoxPackageImpl#getFailedJobRetryTimeCycleType()
   * @generated
   */
	int FAILED_JOB_RETRY_TIME_CYCLE_TYPE = 0;

	/**
   * The feature id for the '<em><b>Mixed</b></em>' attribute list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int FAILED_JOB_RETRY_TIME_CYCLE_TYPE__MIXED = 0;

	/**
   * The feature id for the '<em><b>Text</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int FAILED_JOB_RETRY_TIME_CYCLE_TYPE__TEXT = 1;

	/**
   * The number of structural features of the '<em>Failed Job Retry Time Cycle Type</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int FAILED_JOB_RETRY_TIME_CYCLE_TYPE_FEATURE_COUNT = 2;


	/**
   * Returns the meta object for class '{@link org.camunda.bpm.modeler.runtime.engine.model.fox.FailedJobRetryTimeCycleType <em>Failed Job Retry Time Cycle Type</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Failed Job Retry Time Cycle Type</em>'.
   * @see org.camunda.bpm.modeler.runtime.engine.model.fox.FailedJobRetryTimeCycleType
   * @generated
   */
	EClass getFailedJobRetryTimeCycleType();

	/**
   * Returns the meta object for the attribute list '{@link org.camunda.bpm.modeler.runtime.engine.model.fox.FailedJobRetryTimeCycleType#getMixed <em>Mixed</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Mixed</em>'.
   * @see org.camunda.bpm.modeler.runtime.engine.model.fox.FailedJobRetryTimeCycleType#getMixed()
   * @see #getFailedJobRetryTimeCycleType()
   * @generated
   */
	EAttribute getFailedJobRetryTimeCycleType_Mixed();

	/**
   * Returns the meta object for the attribute '{@link org.camunda.bpm.modeler.runtime.engine.model.fox.FailedJobRetryTimeCycleType#getText <em>Text</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Text</em>'.
   * @see org.camunda.bpm.modeler.runtime.engine.model.fox.FailedJobRetryTimeCycleType#getText()
   * @see #getFailedJobRetryTimeCycleType()
   * @generated
   */
	EAttribute getFailedJobRetryTimeCycleType_Text();

	/**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
	FoxFactory getFoxFactory();

	/**
   * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
   * @generated
   */
	interface Literals {
		/**
     * The meta object literal for the '{@link org.camunda.bpm.modeler.runtime.engine.model.fox.impl.FailedJobRetryTimeCycleTypeImpl <em>Failed Job Retry Time Cycle Type</em>}' class.
     * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
     * @see org.camunda.bpm.modeler.runtime.engine.model.fox.impl.FailedJobRetryTimeCycleTypeImpl
     * @see org.camunda.bpm.modeler.runtime.engine.model.fox.impl.FoxPackageImpl#getFailedJobRetryTimeCycleType()
     * @generated
     */
		EClass FAILED_JOB_RETRY_TIME_CYCLE_TYPE = eINSTANCE.getFailedJobRetryTimeCycleType();

		/**
     * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
     * @generated
     */
		EAttribute FAILED_JOB_RETRY_TIME_CYCLE_TYPE__MIXED = eINSTANCE.getFailedJobRetryTimeCycleType_Mixed();

		/**
     * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
     * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
     * @generated
     */
		EAttribute FAILED_JOB_RETRY_TIME_CYCLE_TYPE__TEXT = eINSTANCE.getFailedJobRetryTimeCycleType_Text();

	}

} //FoxPackage
