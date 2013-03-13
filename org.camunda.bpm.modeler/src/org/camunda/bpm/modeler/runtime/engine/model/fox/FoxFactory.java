/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.camunda.bpm.modeler.runtime.engine.model.fox;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.camunda.bpm.modeler.runtime.engine.model.fox.FoxPackage
 * @generated
 */
public interface FoxFactory extends EFactory {
	/**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	FoxFactory eINSTANCE = org.camunda.bpm.modeler.runtime.engine.model.fox.impl.FoxFactoryImpl.init();

	/**
   * Returns a new object of class '<em>Failed Job Retry Time Cycle Type</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return a new object of class '<em>Failed Job Retry Time Cycle Type</em>'.
   * @generated
   */
	FailedJobRetryTimeCycleType createFailedJobRetryTimeCycleType();

	/**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
	FoxPackage getFoxPackage();

} //FoxFactory
