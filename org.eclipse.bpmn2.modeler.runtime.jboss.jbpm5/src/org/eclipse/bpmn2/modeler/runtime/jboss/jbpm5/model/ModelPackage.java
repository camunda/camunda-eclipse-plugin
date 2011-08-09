/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model;

import org.eclipse.bpmn2.Bpmn2Package;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data description</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.drools.com";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "jbpm5";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelPackage eINSTANCE = org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.ModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.JBPM5CustomTaskImpl <em>JBPM5 Custom Task</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.JBPM5CustomTaskImpl
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.ModelPackageImpl#getJBPM5CustomTask()
	 * @generated
	 */
	int JBPM5_CUSTOM_TASK = 0;

	/**
	 * The feature id for the '<em><b>Extension Values</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__EXTENSION_VALUES = Bpmn2Package.TASK__EXTENSION_VALUES;

	/**
	 * The feature id for the '<em><b>Documentation</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__DOCUMENTATION = Bpmn2Package.TASK__DOCUMENTATION;

	/**
	 * The feature id for the '<em><b>Extension Definitions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__EXTENSION_DEFINITIONS = Bpmn2Package.TASK__EXTENSION_DEFINITIONS;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__ID = Bpmn2Package.TASK__ID;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__ANY_ATTRIBUTE = Bpmn2Package.TASK__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Auditing</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__AUDITING = Bpmn2Package.TASK__AUDITING;

	/**
	 * The feature id for the '<em><b>Monitoring</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__MONITORING = Bpmn2Package.TASK__MONITORING;

	/**
	 * The feature id for the '<em><b>Category Value Ref</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__CATEGORY_VALUE_REF = Bpmn2Package.TASK__CATEGORY_VALUE_REF;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__NAME = Bpmn2Package.TASK__NAME;

	/**
	 * The feature id for the '<em><b>Incoming</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__INCOMING = Bpmn2Package.TASK__INCOMING;

	/**
	 * The feature id for the '<em><b>Lanes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__LANES = Bpmn2Package.TASK__LANES;

	/**
	 * The feature id for the '<em><b>Outgoing</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__OUTGOING = Bpmn2Package.TASK__OUTGOING;

	/**
	 * The feature id for the '<em><b>Io Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__IO_SPECIFICATION = Bpmn2Package.TASK__IO_SPECIFICATION;

	/**
	 * The feature id for the '<em><b>Boundary Event Refs</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__BOUNDARY_EVENT_REFS = Bpmn2Package.TASK__BOUNDARY_EVENT_REFS;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__PROPERTIES = Bpmn2Package.TASK__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Data Input Associations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__DATA_INPUT_ASSOCIATIONS = Bpmn2Package.TASK__DATA_INPUT_ASSOCIATIONS;

	/**
	 * The feature id for the '<em><b>Data Output Associations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__DATA_OUTPUT_ASSOCIATIONS = Bpmn2Package.TASK__DATA_OUTPUT_ASSOCIATIONS;

	/**
	 * The feature id for the '<em><b>Resources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__RESOURCES = Bpmn2Package.TASK__RESOURCES;

	/**
	 * The feature id for the '<em><b>Loop Characteristics</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__LOOP_CHARACTERISTICS = Bpmn2Package.TASK__LOOP_CHARACTERISTICS;

	/**
	 * The feature id for the '<em><b>Completion Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__COMPLETION_QUANTITY = Bpmn2Package.TASK__COMPLETION_QUANTITY;

	/**
	 * The feature id for the '<em><b>Default</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__DEFAULT = Bpmn2Package.TASK__DEFAULT;

	/**
	 * The feature id for the '<em><b>Is For Compensation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__IS_FOR_COMPENSATION = Bpmn2Package.TASK__IS_FOR_COMPENSATION;

	/**
	 * The feature id for the '<em><b>Start Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__START_QUANTITY = Bpmn2Package.TASK__START_QUANTITY;

	/**
	 * The feature id for the '<em><b>Incoming Conversation Links</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__INCOMING_CONVERSATION_LINKS = Bpmn2Package.TASK__INCOMING_CONVERSATION_LINKS;

	/**
	 * The feature id for the '<em><b>Outgoing Conversation Links</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__OUTGOING_CONVERSATION_LINKS = Bpmn2Package.TASK__OUTGOING_CONVERSATION_LINKS;

	/**
	 * The feature id for the '<em><b>Task Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__TASK_NAME = Bpmn2Package.TASK_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Display Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__DISPLAY_NAME = Bpmn2Package.TASK_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Icon</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__ICON = Bpmn2Package.TASK_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__PARAMETERS = Bpmn2Package.TASK_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Results</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK__RESULTS = Bpmn2Package.TASK_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>JBPM5 Custom Task</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JBPM5_CUSTOM_TASK_FEATURE_COUNT = Bpmn2Package.TASK_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.ParameterImpl <em>Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.ParameterImpl
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.ModelPackageImpl#getParameter()
	 * @generated
	 */
	int PARAMETER = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__NAME = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__TYPE = 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__VALUE = 2;

	/**
	 * The number of structural features of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_FEATURE_COUNT = 3;

	/**
	 * Returns the meta object for class '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask <em>JBPM5 Custom Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>JBPM5 Custom Task</em>'.
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask
	 * @generated
	 */
	EClass getJBPM5CustomTask();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask#getTaskName <em>Task Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Task Name</em>'.
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask#getTaskName()
	 * @see #getJBPM5CustomTask()
	 * @generated
	 */
	EAttribute getJBPM5CustomTask_TaskName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask#getDisplayName <em>Display Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Display Name</em>'.
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask#getDisplayName()
	 * @see #getJBPM5CustomTask()
	 * @generated
	 */
	EAttribute getJBPM5CustomTask_DisplayName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask#getIcon <em>Icon</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Icon</em>'.
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask#getIcon()
	 * @see #getJBPM5CustomTask()
	 * @generated
	 */
	EAttribute getJBPM5CustomTask_Icon();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask#getParameters()
	 * @see #getJBPM5CustomTask()
	 * @generated
	 */
	EReference getJBPM5CustomTask_Parameters();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask#getResults <em>Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Results</em>'.
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.JBPM5CustomTask#getResults()
	 * @see #getJBPM5CustomTask()
	 * @generated
	 */
	EReference getJBPM5CustomTask_Results();

	/**
	 * Returns the meta object for class '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter</em>'.
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Parameter
	 * @generated
	 */
	EClass getParameter();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Parameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Parameter#getName()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Parameter#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Parameter#getType()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Parameter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.Parameter#getValue()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Value();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data description</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.JBPM5CustomTaskImpl <em>JBPM5 Custom Task</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.JBPM5CustomTaskImpl
		 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.ModelPackageImpl#getJBPM5CustomTask()
		 * @generated
		 */
		EClass JBPM5_CUSTOM_TASK = eINSTANCE.getJBPM5CustomTask();

		/**
		 * The meta object literal for the '<em><b>Task Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JBPM5_CUSTOM_TASK__TASK_NAME = eINSTANCE.getJBPM5CustomTask_TaskName();

		/**
		 * The meta object literal for the '<em><b>Display Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JBPM5_CUSTOM_TASK__DISPLAY_NAME = eINSTANCE.getJBPM5CustomTask_DisplayName();

		/**
		 * The meta object literal for the '<em><b>Icon</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JBPM5_CUSTOM_TASK__ICON = eINSTANCE.getJBPM5CustomTask_Icon();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JBPM5_CUSTOM_TASK__PARAMETERS = eINSTANCE.getJBPM5CustomTask_Parameters();

		/**
		 * The meta object literal for the '<em><b>Results</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JBPM5_CUSTOM_TASK__RESULTS = eINSTANCE.getJBPM5CustomTask_Results();

		/**
		 * The meta object literal for the '{@link org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.ParameterImpl <em>Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.ParameterImpl
		 * @see org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.model.impl.ModelPackageImpl#getParameter()
		 * @generated
		 */
		EClass PARAMETER = eINSTANCE.getParameter();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__NAME = eINSTANCE.getParameter_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__TYPE = eINSTANCE.getParameter_Type();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__VALUE = eINSTANCE.getParameter_Value();

	}

} //ModelPackage
