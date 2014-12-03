/**
 */
package org.camunda.bpm.modeler.runtime.engine.model.impl;

import org.camunda.bpm.modeler.runtime.engine.model.ListType;
import org.camunda.bpm.modeler.runtime.engine.model.MapType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.ParameterType;
import org.camunda.bpm.modeler.runtime.engine.model.ScriptType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.ParameterTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.ParameterTypeImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.ParameterTypeImpl#getScript <em>Script</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.ParameterTypeImpl#getMap <em>Map</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.ParameterTypeImpl#getList <em>List</em>}</li>
 *   <li>{@link org.camunda.bpm.modeler.runtime.engine.model.impl.ParameterTypeImpl#getText <em>Text</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParameterTypeImpl extends EObjectImpl implements ParameterType {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMixed()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap mixed;

	/**
	 * The default value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected static final String TEXT_EDEFAULT = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParameterTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.PARAMETER_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.PARAMETER_TYPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, ModelPackage.PARAMETER_TYPE__MIXED);
		}
		return mixed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScriptType getScript() {
		return (ScriptType)getMixed().get(ModelPackage.Literals.PARAMETER_TYPE__SCRIPT, true);
	}

/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetScript(ScriptType newScript, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(ModelPackage.Literals.PARAMETER_TYPE__SCRIPT, newScript, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScript(ScriptType newScript) {
		((FeatureMap.Internal)getMixed()).set(ModelPackage.Literals.PARAMETER_TYPE__SCRIPT, newScript);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MapType getMap() {
		return (MapType)getMixed().get(ModelPackage.Literals.PARAMETER_TYPE__MAP, true);
	}

/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMap(MapType newMap, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(ModelPackage.Literals.PARAMETER_TYPE__MAP, newMap, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMap(MapType newMap) {
		((FeatureMap.Internal)getMixed()).set(ModelPackage.Literals.PARAMETER_TYPE__MAP, newMap);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ListType getList() {
		return (ListType)getMixed().get(ModelPackage.Literals.PARAMETER_TYPE__LIST, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetList(ListType newList, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(ModelPackage.Literals.PARAMETER_TYPE__LIST, newList, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setList(ListType newList) {
		((FeatureMap.Internal)getMixed()).set(ModelPackage.Literals.PARAMETER_TYPE__LIST, newList);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getText() {
		if (mixed != null) {
            StringBuilder result = new StringBuilder();
            for (FeatureMap.Entry cur : mixed) {
                switch (cur.getEStructuralFeature().getFeatureID()) {
                case XMLTypePackage.XML_TYPE_DOCUMENT_ROOT__CDATA:
                case XMLTypePackage.XML_TYPE_DOCUMENT_ROOT__TEXT:
                    result.append(cur.getValue());
                    break;

                default:
                    break;
                }
            }
            return result.toString();
        }

        return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setText(String newText) {
		getMixed().clear();
        getMixed().add(XMLTypePackage.eINSTANCE.getXMLTypeDocumentRoot_Text(), newText);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.PARAMETER_TYPE__MIXED:
				return ((InternalEList<?>)getMixed()).basicRemove(otherEnd, msgs);
			case ModelPackage.PARAMETER_TYPE__SCRIPT:
				return basicSetScript(null, msgs);
			case ModelPackage.PARAMETER_TYPE__MAP:
				return basicSetMap(null, msgs);
			case ModelPackage.PARAMETER_TYPE__LIST:
				return basicSetList(null, msgs);
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
			case ModelPackage.PARAMETER_TYPE__NAME:
				return getName();
			case ModelPackage.PARAMETER_TYPE__MIXED:
				if (coreType) return getMixed();
				return ((FeatureMap.Internal)getMixed()).getWrapper();
			case ModelPackage.PARAMETER_TYPE__SCRIPT:
				return getScript();
			case ModelPackage.PARAMETER_TYPE__MAP:
				return getMap();
			case ModelPackage.PARAMETER_TYPE__LIST:
				return getList();
			case ModelPackage.PARAMETER_TYPE__TEXT:
				return getText();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModelPackage.PARAMETER_TYPE__NAME:
				setName((String)newValue);
				return;
			case ModelPackage.PARAMETER_TYPE__MIXED:
				((FeatureMap.Internal)getMixed()).set(newValue);
				return;
			case ModelPackage.PARAMETER_TYPE__SCRIPT:
				setScript((ScriptType)newValue);
				return;
			case ModelPackage.PARAMETER_TYPE__MAP:
				setMap((MapType)newValue);
				return;
			case ModelPackage.PARAMETER_TYPE__LIST:
				setList((ListType)newValue);
				return;
			case ModelPackage.PARAMETER_TYPE__TEXT:
				setText((String)newValue);
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
			case ModelPackage.PARAMETER_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModelPackage.PARAMETER_TYPE__MIXED:
				getMixed().clear();
				return;
			case ModelPackage.PARAMETER_TYPE__SCRIPT:
				setScript((ScriptType)null);
				return;
			case ModelPackage.PARAMETER_TYPE__MAP:
				setMap((MapType)null);
				return;
			case ModelPackage.PARAMETER_TYPE__LIST:
				setList((ListType)null);
				return;
			case ModelPackage.PARAMETER_TYPE__TEXT:
				setText(TEXT_EDEFAULT);
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
			case ModelPackage.PARAMETER_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModelPackage.PARAMETER_TYPE__MIXED:
				return mixed != null && !mixed.isEmpty();
			case ModelPackage.PARAMETER_TYPE__SCRIPT:
				return getScript() != null;
			case ModelPackage.PARAMETER_TYPE__MAP:
				return getMap() != null;
			case ModelPackage.PARAMETER_TYPE__LIST:
				return getList() != null;
			case ModelPackage.PARAMETER_TYPE__TEXT:
				return TEXT_EDEFAULT == null ? getText() != null : !TEXT_EDEFAULT.equals(getText());
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
		result.append(" (name: ");
		result.append(name);
		result.append(", mixed: ");
		result.append(mixed);
		result.append(')');
		return result.toString();
	}

} //ParameterTypeImpl
