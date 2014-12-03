/**
 */
package org.camunda.bpm.modeler.runtime.engine.model;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entry Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.EntryType#getKey <em>Key</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.EntryType#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.EntryType#getScript <em>Script</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.EntryType#getMap <em>Map</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.EntryType#getList <em>List</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.EntryType#getText <em>Text</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getEntryType()
 * @model extendedMetaData="name='entry_._type' kind='mixed'"
 * @generated
 */
public interface EntryType extends EObject {
	/**
	 * Returns the value of the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key</em>' attribute.
	 * @see #setKey(String)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getEntryType_Key()
	 * @model required="true" ordered="false"
	 *        extendedMetaData="kind='attribute' name='key'"
	 * @generated
	 */
	String getKey();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.EntryType#getKey <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' attribute.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(String value);

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
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getEntryType_Mixed()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='elementWildcard' name=':mixed'"
	 * @generated
	 */
	FeatureMap getMixed();

	/**
	 * Returns the value of the '<em><b>Script</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Script</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Script</em>' containment reference.
	 * @see #setScript(ScriptType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getEntryType_Script()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="name='script' kind='element' namespace='##targetNamespace'"
	 * @generated
	 */
	ScriptType getScript();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.EntryType#getScript <em>Script</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Script</em>' containment reference.
	 * @see #getScript()
	 * @generated
	 */
	void setScript(ScriptType value);

	/**
	 * Returns the value of the '<em><b>Map</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Map</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Map</em>' containment reference.
	 * @see #setMap(MapType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getEntryType_Map()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="name='map' kind='element' namespace='##targetNamespace'"
	 * @generated
	 */
	MapType getMap();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.EntryType#getMap <em>Map</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Map</em>' containment reference.
	 * @see #getMap()
	 * @generated
	 */
	void setMap(MapType value);

	/**
	 * Returns the value of the '<em><b>List</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>List</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>List</em>' containment reference.
	 * @see #setList(ListType)
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getEntryType_List()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="name='list' kind='element' namespace='##targetNamespace'"
	 * @generated
	 */
	ListType getList();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.EntryType#getList <em>List</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>List</em>' containment reference.
	 * @see #getList()
	 * @generated
	 */
	void setList(ListType value);

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
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#getEntryType_Text()
	 * @model transient="true" volatile="true" derived="true" ordered="false"
	 * @generated
	 */
	String getText();

	/**
	 * Sets the value of the '{@link org.camunda.bpm.modeler.runtime.engine.model.EntryType#getText <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
	void setText(String value);

} // EntryType
