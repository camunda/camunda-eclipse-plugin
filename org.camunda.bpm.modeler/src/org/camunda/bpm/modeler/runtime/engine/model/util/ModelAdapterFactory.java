/**
 */
package org.camunda.bpm.modeler.runtime.engine.model.util;

import org.camunda.bpm.modeler.runtime.engine.model.*;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.InteractionNode;

import org.eclipse.bpmn2.Task;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage
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
			public Adapter caseStartEvent(StartEvent object) {
				return createStartEventAdapter();
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
			public Adapter caseFormDataType(FormDataType object) {
				return createFormDataTypeAdapter();
			}
			@Override
			public Adapter caseFormFieldType(FormFieldType object) {
				return createFormFieldTypeAdapter();
			}
			@Override
			public Adapter casePropertiesType(PropertiesType object) {
				return createPropertiesTypeAdapter();
			}
			@Override
			public Adapter casePropertyType(PropertyType object) {
				return createPropertyTypeAdapter();
			}
			@Override
			public Adapter caseValidationType(ValidationType object) {
				return createValidationTypeAdapter();
			}
			@Override
			public Adapter caseConstraintType(ConstraintType object) {
				return createConstraintTypeAdapter();
			}
			@Override
			public Adapter caseUserTask(UserTask object) {
				return createUserTaskAdapter();
			}
			@Override
			public Adapter caseFormDataContainer(FormDataContainer object) {
				return createFormDataContainerAdapter();
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
			public Adapter caseBpmn2_StartEvent(org.eclipse.bpmn2.StartEvent object) {
				return createBpmn2_StartEventAdapter();
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
			public Adapter caseBpmn2_BoundaryEvent(org.eclipse.bpmn2.BoundaryEvent object) {
				return createBpmn2_BoundaryEventAdapter();
			}
			@Override
			public Adapter caseTask(Task object) {
				return createTaskAdapter();
			}
			@Override
			public Adapter caseBpmn2_UserTask(org.eclipse.bpmn2.UserTask object) {
				return createBpmn2_UserTaskAdapter();
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
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot
	 * @generated
	 */
  public Adapter createDocumentRootAdapter() {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.StartEvent <em>Start Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.StartEvent
	 * @generated
	 */
	public Adapter createStartEventAdapter() {
		return null;
	}

		/**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType <em>Execution Listener Type</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType
	 * @generated
	 */
  public Adapter createExecutionListenerTypeAdapter() {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.FieldType <em>Field Type</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FieldType
	 * @generated
	 */
  public Adapter createFieldTypeAdapter() {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType <em>Form Property Type</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType
	 * @generated
	 */
  public Adapter createFormPropertyTypeAdapter() {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.InType <em>In Type</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.InType
	 * @generated
	 */
  public Adapter createInTypeAdapter() {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.OutType <em>Out Type</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.OutType
	 * @generated
	 */
  public Adapter createOutTypeAdapter() {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType <em>Task Listener Type</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType
	 * @generated
	 */
  public Adapter createTaskListenerTypeAdapter() {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.CallActivity <em>Call Activity</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.CallActivity
	 * @generated
	 */
  public Adapter createCallActivityAdapter() {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.BoundaryEvent <em>Boundary Event</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.BoundaryEvent
	 * @generated
	 */
  public Adapter createBoundaryEventAdapter() {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.ValueType <em>Value Type</em>}'.
	 * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ValueType
	 * @generated
	 */
  public Adapter createValueTypeAdapter() {
		return null;
	}

  /**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.FormDataType <em>Form Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormDataType
	 * @generated
	 */
	public Adapter createFormDataTypeAdapter() {
		return null;
	}

		/**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.FormFieldType <em>Form Field Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormFieldType
	 * @generated
	 */
	public Adapter createFormFieldTypeAdapter() {
		return null;
	}

		/**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.PropertiesType <em>Properties Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.PropertiesType
	 * @generated
	 */
	public Adapter createPropertiesTypeAdapter() {
		return null;
	}

		/**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.PropertyType <em>Property Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.PropertyType
	 * @generated
	 */
	public Adapter createPropertyTypeAdapter() {
		return null;
	}

		/**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.ValidationType <em>Validation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ValidationType
	 * @generated
	 */
	public Adapter createValidationTypeAdapter() {
		return null;
	}

		/**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.ConstraintType <em>Constraint Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ConstraintType
	 * @generated
	 */
	public Adapter createConstraintTypeAdapter() {
		return null;
	}

		/**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.UserTask <em>User Task</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.UserTask
	 * @generated
	 */
	public Adapter createUserTaskAdapter() {
		return null;
	}

		/**
	 * Creates a new adapter for an object of class '{@link org.camunda.bpm.modeler.runtime.engine.model.FormDataContainer <em>Form Data Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.camunda.bpm.modeler.runtime.engine.model.FormDataContainer
	 * @generated
	 */
	public Adapter createFormDataContainerAdapter() {
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
	 * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.StartEvent <em>Start Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.bpmn2.StartEvent
	 * @generated
	 */
	public Adapter createBpmn2_StartEventAdapter() {
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
	 * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.Task <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.bpmn2.Task
	 * @generated
	 */
	public Adapter createTaskAdapter() {
		return null;
	}

		/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.bpmn2.UserTask <em>User Task</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.bpmn2.UserTask
	 * @generated
	 */
	public Adapter createBpmn2_UserTaskAdapter() {
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
