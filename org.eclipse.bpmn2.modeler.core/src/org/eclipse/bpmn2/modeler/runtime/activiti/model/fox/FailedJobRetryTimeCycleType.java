/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.bpmn2.modeler.runtime.activiti.model.fox;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Failed Job Retry Time Cycle Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FailedJobRetryTimeCycleType#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FailedJobRetryTimeCycleType#getText <em>Text</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FoxPackage#getFailedJobRetryTimeCycleType()
 * @model extendedMetaData="name='tFailedJobRetryCycleType' kind='mixed'"
 * @generated
 */
public interface FailedJobRetryTimeCycleType extends EObject {
	/**
   * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
   * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Mixed</em>' attribute list.
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FoxPackage#getFailedJobRetryTimeCycleType_Mixed()
   * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
   *        extendedMetaData="kind='elementWildcard' name=':mixed'"
   * @generated
   */
	FeatureMap getMixed();

	/**
   * Returns the value of the '<em><b>Text</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Text</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Text</em>' attribute.
   * @see #setText(String)
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FoxPackage#getFailedJobRetryTimeCycleType_Text()
   * @model required="true" volatile="true" derived="true" ordered="false"
   * @generated
   */
	String getText();

	/**
   * Sets the value of the '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FailedJobRetryTimeCycleType#getText <em>Text</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Text</em>' attribute.
   * @see #getText()
   * @generated
   */
	void setText(String value);

} // FailedJobRetryTimeCycleType
