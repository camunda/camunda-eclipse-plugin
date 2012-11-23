/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.util;

import java.util.Map;

import org.eclipse.bpmn2.modeler.runtime.activiti.model.fox.FoxPackage;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class FoxXMLProcessor extends XMLProcessor {

	/**
   * Public constructor to instantiate the helper.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public FoxXMLProcessor() {
    super((EPackage.Registry.INSTANCE));
    FoxPackage.eINSTANCE.eClass();
  }
	
	/**
   * Register for "*" and "xml" file extensions the FoxResourceFactoryImpl factory.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	protected Map<String, Resource.Factory> getRegistrations() {
    if (registrations == null) {
      super.getRegistrations();
      registrations.put(XML_EXTENSION, new FoxResourceFactoryImpl());
      registrations.put(STAR_EXTENSION, new FoxResourceFactoryImpl());
    }
    return registrations;
  }

} //FoxXMLProcessor
