/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.impl;

import org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FailedJobRetryTimeCycleType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FoxFactory;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FoxPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FoxFactoryImpl extends EFactoryImpl implements FoxFactory {
	/**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public static FoxFactory init() {
    try {
      FoxFactory theFoxFactory = (FoxFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.camunda.com/fox"); 
      if (theFoxFactory != null) {
        return theFoxFactory;
      }
    }
    catch (Exception exception) {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new FoxFactoryImpl();
  }

	/**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public FoxFactoryImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public EObject create(EClass eClass) {
    switch (eClass.getClassifierID()) {
      case FoxPackage.FAILED_JOB_RETRY_TIME_CYCLE_TYPE: return createFailedJobRetryTimeCycleType();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public FailedJobRetryTimeCycleType createFailedJobRetryTimeCycleType() {
    FailedJobRetryTimeCycleTypeImpl failedJobRetryTimeCycleType = new FailedJobRetryTimeCycleTypeImpl();
    return failedJobRetryTimeCycleType;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public FoxPackage getFoxPackage() {
    return (FoxPackage)getEPackage();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
	@Deprecated
	public static FoxPackage getPackage() {
    return FoxPackage.eINSTANCE;
  }

} //FoxFactoryImpl
