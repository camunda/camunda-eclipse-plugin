/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Task#getTaskName <em>Task Name</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Task#getDisplayName <em>Display Name</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Task#getIcon <em>Icon</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Task#getParameters <em>Parameters</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Task#getResults <em>Results</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage#getTask()
 * @model
 * @generated
 */
public interface Task extends org.eclipse.bpmn2.Task {
	/**
	 * Returns the value of the '<em><b>Task Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Task Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Task Name</em>' attribute.
	 * @see #setTaskName(String)
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage#getTask_TaskName()
	 * @model
	 * @generated
	 */
	String getTaskName();

	/**
	 * Sets the value of the '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Task#getTaskName <em>Task Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Task Name</em>' attribute.
	 * @see #getTaskName()
	 * @generated
	 */
	void setTaskName(String value);

	/**
	 * Returns the value of the '<em><b>Display Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Display Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Display Name</em>' attribute.
	 * @see #setDisplayName(String)
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage#getTask_DisplayName()
	 * @model
	 * @generated
	 */
	String getDisplayName();

	/**
	 * Sets the value of the '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Task#getDisplayName <em>Display Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Display Name</em>' attribute.
	 * @see #getDisplayName()
	 * @generated
	 */
	void setDisplayName(String value);

	/**
	 * Returns the value of the '<em><b>Icon</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Icon</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Icon</em>' attribute.
	 * @see #setIcon(String)
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage#getTask_Icon()
	 * @model
	 * @generated
	 */
	String getIcon();

	/**
	 * Sets the value of the '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Task#getIcon <em>Icon</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Icon</em>' attribute.
	 * @see #getIcon()
	 * @generated
	 */
	void setIcon(String value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Parameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage#getTask_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<Parameter> getParameters();

	/**
	 * Returns the value of the '<em><b>Results</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Parameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Results</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Results</em>' containment reference list.
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelPackage#getTask_Results()
	 * @model containment="true"
	 * @generated
	 */
	EList<Parameter> getResults();

} // Task
