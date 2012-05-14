/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.bpmn2.modeler.runtime.example.SampleModel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.bpmn2.modeler.runtime.example.SampleModel.DocumentRoot#getSampleCustomTaskId <em>Sample Custom Task Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.bpmn2.modeler.runtime.example.SampleModel.SampleModelPackage#getDocumentRoot()
 * @model extendedMetaData="kind='mixed' name='' namespace='##targetNamespace'"
 * @generated
 */
public interface DocumentRoot extends org.eclipse.bpmn2.DocumentRoot {
	/**
	 * Returns the value of the '<em><b>Sample Custom Task Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sample Custom Task Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sample Custom Task Id</em>' attribute.
	 * @see #setSampleCustomTaskId(String)
	 * @see org.eclipse.bpmn2.modeler.runtime.example.SampleModel.SampleModelPackage#getDocumentRoot_SampleCustomTaskId()
	 * @model extendedMetaData="kind='attribute' name='sampleCustomTaskId' namespace='##targetNamespace'"
	 * @generated
	 */
	String getSampleCustomTaskId();

	/**
	 * Sets the value of the '{@link org.eclipse.bpmn2.modeler.runtime.example.SampleModel.DocumentRoot#getSampleCustomTaskId <em>Sample Custom Task Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sample Custom Task Id</em>' attribute.
	 * @see #getSampleCustomTaskId()
	 * @generated
	 */
	void setSampleCustomTaskId(String value);

} // DocumentRoot
