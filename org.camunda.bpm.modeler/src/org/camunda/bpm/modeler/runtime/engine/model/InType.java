/**
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>In Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getSource <em>Source</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getSourceExpression <em>Source Expression</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getTarget <em>Target</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getVariables <em>Variables</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getBusinessKey <em>Business Key</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getInType()
 * @model extendedMetaData="name='in_._type' kind='empty'"
 * @generated
 */
public interface InType extends EObject {
  /**
	 * Returns the value of the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Source</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' attribute.
	 * @see #setSource(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getInType_Source()
	 * @model extendedMetaData="kind='attribute' name='source'"
	 * @generated
	 */
  String getSource();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getSource <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' attribute.
	 * @see #getSource()
	 * @generated
	 */
  void setSource(String value);

  /**
	 * Returns the value of the '<em><b>Source Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Source Expression</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Expression</em>' attribute.
	 * @see #setSourceExpression(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getInType_SourceExpression()
	 * @model dataType="org.camunda.bpm.modeler.runtime.engine.model.TExpression"
	 *        extendedMetaData="kind='attribute' name='sourceExpression'"
	 * @generated
	 */
  String getSourceExpression();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getSourceExpression <em>Source Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source Expression</em>' attribute.
	 * @see #getSourceExpression()
	 * @generated
	 */
  void setSourceExpression(String value);

  /**
	 * Returns the value of the '<em><b>Target</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Target</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' attribute.
	 * @see #setTarget(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getInType_Target()
	 * @model extendedMetaData="kind='attribute' name='target'"
	 * @generated
	 */
  String getTarget();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getTarget <em>Target</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' attribute.
	 * @see #getTarget()
	 * @generated
	 */
  void setTarget(String value);

  /**
	 * Returns the value of the '<em><b>Variables</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Variables</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Variables</em>' attribute.
	 * @see #setVariables(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getInType_Variables()
	 * @model extendedMetaData="kind='attribute' name='variables'"
	 * @generated
	 */
  String getVariables();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getVariables <em>Variables</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Variables</em>' attribute.
	 * @see #getVariables()
	 * @generated
	 */
  void setVariables(String value);

  /**
	 * Returns the value of the '<em><b>Business Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Business Key</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Business Key</em>' attribute.
	 * @see #setBusinessKey(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getInType_BusinessKey()
	 * @model dataType="org.camunda.bpm.modeler.runtime.engine.model.TExpression"
	 *        extendedMetaData="kind='attribute' name='businessKey'"
	 * @generated
	 */
  String getBusinessKey();

  /**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.InType#getBusinessKey <em>Business Key</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Business Key</em>' attribute.
	 * @see #getBusinessKey()
	 * @generated
	 */
  void setBusinessKey(String value);

} // InType
