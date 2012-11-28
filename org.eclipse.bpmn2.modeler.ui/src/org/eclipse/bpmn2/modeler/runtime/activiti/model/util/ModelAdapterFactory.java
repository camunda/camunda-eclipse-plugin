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
package org.eclipse.bpmn2.modeler.runtime.activiti.model.util;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.CallActivity;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.*;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.DocumentRoot;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ExecutionListenerType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.FieldType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.FormPropertyType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.InType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.OutType;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.TaskListenerType;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage
 * @generated
 */
public class ModelAdapterFactory extends AdapterFactoryImpl {
	/**
   * The cached model package.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected static ModelPackage modelPackage;

	/**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public ModelAdapterFactory() {
    if (modelPackage == null) {
      modelPackage = ModelPackage.eINSTANCE;
    }
  }

	/**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
	@Override
	public boolean isFactoryForType(Object object) {
    if (object == modelPackage) {
      return true;
    }
    if (object instanceof EObject) {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

	/**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected ModelSwitch<Adapter> modelSwitch =
		new ModelSwitch<Adapter>() {
      @Override
      public Adapter caseDocumentRoot(DocumentRoot object) {
        return createDocumentRootAdapter();
      }
      @Override
      public Adapter caseExecutionListenerType(ExecutionListenerType object) {
        return createExecutionListenerTypeAdapter();
      }
      @Override
      public Adapter caseFieldType(FieldType object) {
        return createFieldTypeAdapter();
      }
      @Override
      public Adapter caseFormPropertyType(FormPropertyType object) {
        return createFormPropertyTypeAdapter();
      }
      @Override
      public Adapter caseInType(InType object) {
        return createInTypeAdapter();
      }
      @Override
      public Adapter caseOutType(OutType object) {
        return createOutTypeAdapter();
      }
      @Override
      public Adapter caseTaskListenerType(TaskListenerType object) {
        return createTaskListenerTypeAdapter();
      }
      @Override
      public Adapter caseCallActivity(CallActivity object) {
        return createCallActivityAdapter();
      }
      @Override
      public Adapter caseBoundaryEvent(BoundaryEvent object) {
        return createBoundaryEventAdapter();
      }
      @Override
      public Adapter caseValueType(ValueType object) {
        return createValueTypeAdapter();
      }
      @Override
      public Adapter caseBpmn2_DocumentRoot(org.eclipse.bpmn2.DocumentRoot object) {
        return createBpmn2_DocumentRootAdapter();
      }
      @Override
      public Adapter caseBaseElement(BaseElement object) {
        return createBaseElementAdapter();
      }
      @Override
      public Adapter caseFlowElement(FlowElement object) {
        return createFlowElementAdapter();
      }
      @Override
      public Adapter caseFlowNode(FlowNode object) {
        return createFlowNodeAdapter();
      }
      @Override
      public Adapter caseActivity(Activity object) {
        return createActivityAdapter();
      }
      @Override
      public Adapter caseBpmn2_CallActivity(org.eclipse.bpmn2.CallActivity object) {
        return createBpmn2_CallActivityAdapter();
      }
      @Override
      public Adapter caseInteractionNode(InteractionNode object) {
        return createInteractionNodeAdapter();
      }
      @Override
      public Adapter caseEvent(Event object) {
        return createEventAdapter();
      }
      @Override
      public Adapter caseCatchEvent(CatchEvent object) {
        return createCatchEventAdapter();
      }
      @Override
      public Adapter caseBpmn2_BoundaryEvent(org.eclipse.bpmn2.BoundaryEvent object) {
        return createBpmn2_BoundaryEventAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object) {
        return createEObjectAdapter();
      }
    };

	/**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
	@Override
	public Adapter createAdapter(Notifier target) {
    return modelSwitch.doSwitch((EObject)target);
  }


	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.DocumentRoot <em>Document Root</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.DocumentRoot
   * @generated
   */
	public Adapter createDocumentRootAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.ExecutionListenerType <em>Execution Listener Type</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.ExecutionListenerType
   * @generated
   */
	public Adapter createExecutionListenerTypeAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.FieldType <em>Field Type</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.FieldType
   * @generated
   */
	public Adapter createFieldTypeAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.FormPropertyType <em>Form Property Type</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.FormPropertyType
   * @generated
   */
	public Adapter createFormPropertyTypeAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.InType <em>In Type</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.InType
   * @generated
   */
	public Adapter createInTypeAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.OutType <em>Out Type</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.OutType
   * @generated
   */
	public Adapter createOutTypeAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.TaskListenerType <em>Task Listener Type</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.TaskListenerType
   * @generated
   */
	public Adapter createTaskListenerTypeAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.DocumentRoot <em>Document Root</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.DocumentRoot
   * @generated
   */
	public Adapter createBpmn2_DocumentRootAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.BaseElement <em>Base Element</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.BaseElement
   * @generated
   */
	public Adapter createBaseElementAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.FlowElement <em>Flow Element</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.FlowElement
   * @generated
   */
	public Adapter createFlowElementAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.FlowNode <em>Flow Node</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.FlowNode
   * @generated
   */
	public Adapter createFlowNodeAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.Activity <em>Activity</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.Activity
   * @generated
   */
	public Adapter createActivityAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.CallActivity <em>Call Activity</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.CallActivity
   * @generated
   */
	public Adapter createBpmn2_CallActivityAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.InteractionNode <em>Interaction Node</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.InteractionNode
   * @generated
   */
	public Adapter createInteractionNodeAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.Event <em>Event</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.Event
   * @generated
   */
	public Adapter createEventAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.CatchEvent <em>Catch Event</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.CatchEvent
   * @generated
   */
	public Adapter createCatchEventAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.BoundaryEvent <em>Boundary Event</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.BoundaryEvent
   * @generated
   */
	public Adapter createBpmn2_BoundaryEventAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.CallActivity <em>Call Activity</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.CallActivity
   * @generated
   */
	public Adapter createCallActivityAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.BoundaryEvent <em>Boundary Event</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.BoundaryEvent
   * @generated
   */
	public Adapter createBoundaryEventAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.modeler.runtime.activiti.model.ValueType <em>Value Type</em>}'.
   * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.bpmn2.modeler.runtime.activiti.model.ValueType
   * @generated
   */
	public Adapter createValueTypeAdapter() {
    return null;
  }

	/**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
	public Adapter createEObjectAdapter() {
    return null;
  }

} //ModelAdapterFactory
