/**
 */
package org.camunda.bpm.modeler.runtime.engine.model.impl;

import org.camunda.bpm.modeler.runtime.engine.model.BoundaryEvent;
import org.camunda.bpm.modeler.runtime.engine.model.CallActivity;
import org.camunda.bpm.modeler.runtime.engine.model.ConnectorType;
import org.camunda.bpm.modeler.runtime.engine.model.ConstraintType;
import org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot;
import org.camunda.bpm.modeler.runtime.engine.model.EntryType;
import org.camunda.bpm.modeler.runtime.engine.model.EventType;
import org.camunda.bpm.modeler.runtime.engine.model.EventType1;
import org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType;
import org.camunda.bpm.modeler.runtime.engine.model.Expression;
import org.camunda.bpm.modeler.runtime.engine.model.FailedJobRetryTimeCycleType;
import org.camunda.bpm.modeler.runtime.engine.model.FieldType;
import org.camunda.bpm.modeler.runtime.engine.model.FormDataContainer;
import org.camunda.bpm.modeler.runtime.engine.model.FormDataType;
import org.camunda.bpm.modeler.runtime.engine.model.FormFieldType;
import org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType;
import org.camunda.bpm.modeler.runtime.engine.model.HistoryType;
import org.camunda.bpm.modeler.runtime.engine.model.InType;
import org.camunda.bpm.modeler.runtime.engine.model.InputOutputType;
import org.camunda.bpm.modeler.runtime.engine.model.ListType;
import org.camunda.bpm.modeler.runtime.engine.model.MapType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.OutType;
import org.camunda.bpm.modeler.runtime.engine.model.ParameterType;
import org.camunda.bpm.modeler.runtime.engine.model.PropertiesType;
import org.camunda.bpm.modeler.runtime.engine.model.PropertyType;
import org.camunda.bpm.modeler.runtime.engine.model.ScriptType;
import org.camunda.bpm.modeler.runtime.engine.model.StartEvent;
import org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType;
import org.camunda.bpm.modeler.runtime.engine.model.TypeType;
import org.camunda.bpm.modeler.runtime.engine.model.UserTask;
import org.camunda.bpm.modeler.runtime.engine.model.ValidationType;
import org.camunda.bpm.modeler.runtime.engine.model.ValueType;

import org.camunda.bpm.modeler.runtime.engine.model.util.ModelValidator;

import org.eclipse.bpmn2.Bpmn2Package;

import org.eclipse.bpmn2.di.BpmnDiPackage;

import org.eclipse.dd.dc.DcPackage;

import org.eclipse.dd.di.DiPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelPackageImpl extends EPackageImpl implements ModelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass documentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass startEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass executionListenerTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fieldTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass formPropertyTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass inTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass outTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass taskListenerTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass callActivityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass boundaryEventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass valueTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass formDataTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass formFieldTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass propertiesTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass propertyTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass validationTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constraintTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass userTaskEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass formDataContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass expressionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass connectorTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass inputOutputTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parameterTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scriptTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mapTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entryTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass listTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass failedJobRetryTimeCycleTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum eventTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum eventType1EEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum historyTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum typeTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType classTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eventTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eventTypeObject1EDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType formHandlerClassTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType historyTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType tExpressionEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType typeTypeObjectEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.camunda.bpm.modeler.runtime.engine.model.ModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ModelPackageImpl() {
		super(eNS_URI, ModelFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link ModelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ModelPackage init() {
		if (isInited) return (ModelPackage)EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI);

		// Obtain or create and register package
		ModelPackageImpl theModelPackage = (ModelPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ModelPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		Bpmn2Package.eINSTANCE.eClass();
		BpmnDiPackage.eINSTANCE.eClass();
		DiPackage.eINSTANCE.eClass();
		DcPackage.eINSTANCE.eClass();
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theModelPackage.createPackageContents();

		// Initialize created meta-data
		theModelPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put
			(theModelPackage, 
			 new EValidator.Descriptor() {
				 public EValidator getEValidator() {
					 return ModelValidator.INSTANCE;
				 }
			 });

		// Mark meta-data to indicate it can't be changed
		theModelPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, theModelPackage);
		return theModelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDocumentRoot() {
		return documentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_ExecutionListener() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Field() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_FormProperty() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_In() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Out() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_TaskListener() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Assignee() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_CandidateGroups() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_CandidateUsers() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Class() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Collection() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_DelegateExpression() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_DueDate() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_ElementVariable() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_FormHandlerClass() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_FormKey() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_History() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Initiator() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_ResultVariable() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Type() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Async() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_ActExpression() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Priority() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_ResultVariableName() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_FailedJobRetryTimeCycle() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(24);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_FormData() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(25);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_FollowUpDate() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(26);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Properties() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(27);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Exclusive() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(28);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Connector() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(29);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_InputOutput() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(30);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_AsyncAfter() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(31);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_AsyncBefore() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(32);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Resource1() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(33);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStartEvent() {
		return startEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExecutionListenerType() {
		return executionListenerTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExecutionListenerType_Group() {
		return (EAttribute)executionListenerTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExecutionListenerType_Field() {
		return (EReference)executionListenerTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExecutionListenerType_Class() {
		return (EAttribute)executionListenerTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExecutionListenerType_DelegateExpression() {
		return (EAttribute)executionListenerTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExecutionListenerType_Event() {
		return (EAttribute)executionListenerTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExecutionListenerType_Expression() {
		return (EAttribute)executionListenerTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExecutionListenerType_Script() {
		return (EReference)executionListenerTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFieldType() {
		return fieldTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFieldType_String() {
		return (EAttribute)fieldTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFieldType_Expression() {
		return (EAttribute)fieldTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFieldType_Expression1() {
		return (EAttribute)fieldTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFieldType_Name() {
		return (EAttribute)fieldTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFieldType_StringValue() {
		return (EAttribute)fieldTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFormPropertyType() {
		return formPropertyTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFormPropertyType_Value() {
		return (EReference)formPropertyTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormPropertyType_DatePattern() {
		return (EAttribute)formPropertyTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormPropertyType_Expression() {
		return (EAttribute)formPropertyTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormPropertyType_Id() {
		return (EAttribute)formPropertyTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormPropertyType_Name() {
		return (EAttribute)formPropertyTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormPropertyType_Readable() {
		return (EAttribute)formPropertyTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormPropertyType_Required() {
		return (EAttribute)formPropertyTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormPropertyType_Type() {
		return (EAttribute)formPropertyTypeEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormPropertyType_Value1() {
		return (EAttribute)formPropertyTypeEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormPropertyType_Variable() {
		return (EAttribute)formPropertyTypeEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormPropertyType_Writable() {
		return (EAttribute)formPropertyTypeEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormPropertyType_Default() {
		return (EAttribute)formPropertyTypeEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInType() {
		return inTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInType_Source() {
		return (EAttribute)inTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInType_SourceExpression() {
		return (EAttribute)inTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInType_Target() {
		return (EAttribute)inTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInType_Variables() {
		return (EAttribute)inTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInType_BusinessKey() {
		return (EAttribute)inTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOutType() {
		return outTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOutType_Source() {
		return (EAttribute)outTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOutType_SourceExpression() {
		return (EAttribute)outTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOutType_Target() {
		return (EAttribute)outTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOutType_Variables() {
		return (EAttribute)outTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTaskListenerType() {
		return taskListenerTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskListenerType_Group() {
		return (EAttribute)taskListenerTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTaskListenerType_Field() {
		return (EReference)taskListenerTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskListenerType_Class() {
		return (EAttribute)taskListenerTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskListenerType_DelegateExpression() {
		return (EAttribute)taskListenerTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskListenerType_Event() {
		return (EAttribute)taskListenerTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaskListenerType_Expression() {
		return (EAttribute)taskListenerTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTaskListenerType_Script() {
		return (EReference)taskListenerTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCallActivity() {
		return callActivityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCallActivity_CalledElement() {
		return (EAttribute)callActivityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCallActivity_CalledElementBinding() {
		return (EAttribute)callActivityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCallActivity_CalledElementVersion() {
		return (EAttribute)callActivityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBoundaryEvent() {
		return boundaryEventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getValueType() {
		return valueTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueType_Id() {
		return (EAttribute)valueTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueType_Name() {
		return (EAttribute)valueTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFormDataType() {
		return formDataTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFormDataType_FormField() {
		return (EReference)formDataTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFormFieldType() {
		return formFieldTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormFieldType_Id() {
		return (EAttribute)formFieldTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormFieldType_Label() {
		return (EAttribute)formFieldTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormFieldType_Type() {
		return (EAttribute)formFieldTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFormFieldType_DefaultValue() {
		return (EAttribute)formFieldTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFormFieldType_Properties() {
		return (EReference)formFieldTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFormFieldType_Validation() {
		return (EReference)formFieldTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFormFieldType_Value() {
		return (EReference)formFieldTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPropertiesType() {
		return propertiesTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPropertiesType_Property() {
		return (EReference)propertiesTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPropertyType() {
		return propertyTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPropertyType_Id() {
		return (EAttribute)propertyTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPropertyType_Value() {
		return (EAttribute)propertyTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPropertyType_Name() {
		return (EAttribute)propertyTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getValidationType() {
		return validationTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getValidationType_Constraint() {
		return (EReference)validationTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConstraintType() {
		return constraintTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConstraintType_Name() {
		return (EAttribute)constraintTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConstraintType_Config() {
		return (EAttribute)constraintTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUserTask() {
		return userTaskEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFormDataContainer() {
		return formDataContainerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExpression() {
		return expressionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpression_Body() {
		return (EAttribute)expressionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpression_Mixed() {
		return (EAttribute)expressionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConnectorType() {
		return connectorTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConnectorType_ConnectorId() {
		return (EAttribute)connectorTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConnectorType_InputOutput() {
		return (EReference)connectorTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInputOutputType() {
		return inputOutputTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInputOutputType_InputParameters() {
		return (EReference)inputOutputTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInputOutputType_OutputParameters() {
		return (EReference)inputOutputTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParameterType() {
		return parameterTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterType_Name() {
		return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterType_Mixed() {
		return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterType_Script() {
		return (EReference)parameterTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterType_Map() {
		return (EReference)parameterTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterType_List() {
		return (EReference)parameterTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterType_Text() {
		return (EAttribute)parameterTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScriptType() {
		return scriptTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScriptType_ScriptFormat() {
		return (EAttribute)scriptTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScriptType_Resource() {
		return (EAttribute)scriptTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScriptType_Mixed() {
		return (EAttribute)scriptTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScriptType_Text() {
		return (EAttribute)scriptTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMapType() {
		return mapTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMapType_Entries() {
		return (EReference)mapTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntryType() {
		return entryTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntryType_Key() {
		return (EAttribute)entryTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntryType_Mixed() {
		return (EAttribute)entryTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntryType_Script() {
		return (EReference)entryTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntryType_Map() {
		return (EReference)entryTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntryType_List() {
		return (EReference)entryTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntryType_Text() {
		return (EAttribute)entryTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getListType() {
		return listTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getListType_Group() {
		return (EAttribute)listTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getListType_Scripts() {
		return (EReference)listTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getListType_Maps() {
		return (EReference)listTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getListType_Lists() {
		return (EReference)listTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getListType_Values() {
		return (EAttribute)listTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFailedJobRetryTimeCycleType() {
		return failedJobRetryTimeCycleTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFailedJobRetryTimeCycleType_Mixed() {
		return (EAttribute)failedJobRetryTimeCycleTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFailedJobRetryTimeCycleType_Text() {
		return (EAttribute)failedJobRetryTimeCycleTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getEventType() {
		return eventTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getEventType1() {
		return eventType1EEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getHistoryType() {
		return historyTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getTypeType() {
		return typeTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getClassType() {
		return classTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getEventTypeObject() {
		return eventTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getEventTypeObject1() {
		return eventTypeObject1EDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getFormHandlerClassType() {
		return formHandlerClassTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getHistoryTypeObject() {
		return historyTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getTExpression() {
		return tExpressionEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getTypeTypeObject() {
		return typeTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelFactory getModelFactory() {
		return (ModelFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEReference(documentRootEClass, DOCUMENT_ROOT__EXECUTION_LISTENER);
		createEReference(documentRootEClass, DOCUMENT_ROOT__FIELD);
		createEReference(documentRootEClass, DOCUMENT_ROOT__FORM_PROPERTY);
		createEReference(documentRootEClass, DOCUMENT_ROOT__IN);
		createEReference(documentRootEClass, DOCUMENT_ROOT__OUT);
		createEReference(documentRootEClass, DOCUMENT_ROOT__TASK_LISTENER);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__ASSIGNEE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__CANDIDATE_GROUPS);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__CANDIDATE_USERS);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__CLASS);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__COLLECTION);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__DELEGATE_EXPRESSION);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__DUE_DATE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__ELEMENT_VARIABLE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__FORM_HANDLER_CLASS);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__FORM_KEY);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__HISTORY);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__INITIATOR);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__RESULT_VARIABLE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__TYPE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__ASYNC);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__ACT_EXPRESSION);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__PRIORITY);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__RESULT_VARIABLE_NAME);
		createEReference(documentRootEClass, DOCUMENT_ROOT__FAILED_JOB_RETRY_TIME_CYCLE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__FORM_DATA);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__FOLLOW_UP_DATE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTIES);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__EXCLUSIVE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__CONNECTOR);
		createEReference(documentRootEClass, DOCUMENT_ROOT__INPUT_OUTPUT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__ASYNC_AFTER);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__ASYNC_BEFORE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__RESOURCE1);

		startEventEClass = createEClass(START_EVENT);

		executionListenerTypeEClass = createEClass(EXECUTION_LISTENER_TYPE);
		createEAttribute(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__GROUP);
		createEReference(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__FIELD);
		createEAttribute(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__CLASS);
		createEAttribute(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__DELEGATE_EXPRESSION);
		createEAttribute(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__EVENT);
		createEAttribute(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__EXPRESSION);
		createEReference(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__SCRIPT);

		fieldTypeEClass = createEClass(FIELD_TYPE);
		createEAttribute(fieldTypeEClass, FIELD_TYPE__STRING);
		createEAttribute(fieldTypeEClass, FIELD_TYPE__EXPRESSION);
		createEAttribute(fieldTypeEClass, FIELD_TYPE__EXPRESSION1);
		createEAttribute(fieldTypeEClass, FIELD_TYPE__NAME);
		createEAttribute(fieldTypeEClass, FIELD_TYPE__STRING_VALUE);

		formPropertyTypeEClass = createEClass(FORM_PROPERTY_TYPE);
		createEReference(formPropertyTypeEClass, FORM_PROPERTY_TYPE__VALUE);
		createEAttribute(formPropertyTypeEClass, FORM_PROPERTY_TYPE__DATE_PATTERN);
		createEAttribute(formPropertyTypeEClass, FORM_PROPERTY_TYPE__EXPRESSION);
		createEAttribute(formPropertyTypeEClass, FORM_PROPERTY_TYPE__ID);
		createEAttribute(formPropertyTypeEClass, FORM_PROPERTY_TYPE__NAME);
		createEAttribute(formPropertyTypeEClass, FORM_PROPERTY_TYPE__READABLE);
		createEAttribute(formPropertyTypeEClass, FORM_PROPERTY_TYPE__REQUIRED);
		createEAttribute(formPropertyTypeEClass, FORM_PROPERTY_TYPE__TYPE);
		createEAttribute(formPropertyTypeEClass, FORM_PROPERTY_TYPE__VALUE1);
		createEAttribute(formPropertyTypeEClass, FORM_PROPERTY_TYPE__VARIABLE);
		createEAttribute(formPropertyTypeEClass, FORM_PROPERTY_TYPE__WRITABLE);
		createEAttribute(formPropertyTypeEClass, FORM_PROPERTY_TYPE__DEFAULT);

		inTypeEClass = createEClass(IN_TYPE);
		createEAttribute(inTypeEClass, IN_TYPE__SOURCE);
		createEAttribute(inTypeEClass, IN_TYPE__SOURCE_EXPRESSION);
		createEAttribute(inTypeEClass, IN_TYPE__TARGET);
		createEAttribute(inTypeEClass, IN_TYPE__VARIABLES);
		createEAttribute(inTypeEClass, IN_TYPE__BUSINESS_KEY);

		outTypeEClass = createEClass(OUT_TYPE);
		createEAttribute(outTypeEClass, OUT_TYPE__SOURCE);
		createEAttribute(outTypeEClass, OUT_TYPE__SOURCE_EXPRESSION);
		createEAttribute(outTypeEClass, OUT_TYPE__TARGET);
		createEAttribute(outTypeEClass, OUT_TYPE__VARIABLES);

		taskListenerTypeEClass = createEClass(TASK_LISTENER_TYPE);
		createEAttribute(taskListenerTypeEClass, TASK_LISTENER_TYPE__GROUP);
		createEReference(taskListenerTypeEClass, TASK_LISTENER_TYPE__FIELD);
		createEAttribute(taskListenerTypeEClass, TASK_LISTENER_TYPE__CLASS);
		createEAttribute(taskListenerTypeEClass, TASK_LISTENER_TYPE__DELEGATE_EXPRESSION);
		createEAttribute(taskListenerTypeEClass, TASK_LISTENER_TYPE__EVENT);
		createEAttribute(taskListenerTypeEClass, TASK_LISTENER_TYPE__EXPRESSION);
		createEReference(taskListenerTypeEClass, TASK_LISTENER_TYPE__SCRIPT);

		callActivityEClass = createEClass(CALL_ACTIVITY);
		createEAttribute(callActivityEClass, CALL_ACTIVITY__CALLED_ELEMENT);
		createEAttribute(callActivityEClass, CALL_ACTIVITY__CALLED_ELEMENT_BINDING);
		createEAttribute(callActivityEClass, CALL_ACTIVITY__CALLED_ELEMENT_VERSION);

		boundaryEventEClass = createEClass(BOUNDARY_EVENT);

		valueTypeEClass = createEClass(VALUE_TYPE);
		createEAttribute(valueTypeEClass, VALUE_TYPE__ID);
		createEAttribute(valueTypeEClass, VALUE_TYPE__NAME);

		formDataTypeEClass = createEClass(FORM_DATA_TYPE);
		createEReference(formDataTypeEClass, FORM_DATA_TYPE__FORM_FIELD);

		formFieldTypeEClass = createEClass(FORM_FIELD_TYPE);
		createEAttribute(formFieldTypeEClass, FORM_FIELD_TYPE__ID);
		createEAttribute(formFieldTypeEClass, FORM_FIELD_TYPE__LABEL);
		createEAttribute(formFieldTypeEClass, FORM_FIELD_TYPE__TYPE);
		createEAttribute(formFieldTypeEClass, FORM_FIELD_TYPE__DEFAULT_VALUE);
		createEReference(formFieldTypeEClass, FORM_FIELD_TYPE__PROPERTIES);
		createEReference(formFieldTypeEClass, FORM_FIELD_TYPE__VALIDATION);
		createEReference(formFieldTypeEClass, FORM_FIELD_TYPE__VALUE);

		propertiesTypeEClass = createEClass(PROPERTIES_TYPE);
		createEReference(propertiesTypeEClass, PROPERTIES_TYPE__PROPERTY);

		propertyTypeEClass = createEClass(PROPERTY_TYPE);
		createEAttribute(propertyTypeEClass, PROPERTY_TYPE__ID);
		createEAttribute(propertyTypeEClass, PROPERTY_TYPE__VALUE);
		createEAttribute(propertyTypeEClass, PROPERTY_TYPE__NAME);

		validationTypeEClass = createEClass(VALIDATION_TYPE);
		createEReference(validationTypeEClass, VALIDATION_TYPE__CONSTRAINT);

		constraintTypeEClass = createEClass(CONSTRAINT_TYPE);
		createEAttribute(constraintTypeEClass, CONSTRAINT_TYPE__NAME);
		createEAttribute(constraintTypeEClass, CONSTRAINT_TYPE__CONFIG);

		userTaskEClass = createEClass(USER_TASK);

		formDataContainerEClass = createEClass(FORM_DATA_CONTAINER);

		expressionEClass = createEClass(EXPRESSION);
		createEAttribute(expressionEClass, EXPRESSION__BODY);
		createEAttribute(expressionEClass, EXPRESSION__MIXED);

		connectorTypeEClass = createEClass(CONNECTOR_TYPE);
		createEAttribute(connectorTypeEClass, CONNECTOR_TYPE__CONNECTOR_ID);
		createEReference(connectorTypeEClass, CONNECTOR_TYPE__INPUT_OUTPUT);

		inputOutputTypeEClass = createEClass(INPUT_OUTPUT_TYPE);
		createEReference(inputOutputTypeEClass, INPUT_OUTPUT_TYPE__INPUT_PARAMETERS);
		createEReference(inputOutputTypeEClass, INPUT_OUTPUT_TYPE__OUTPUT_PARAMETERS);

		parameterTypeEClass = createEClass(PARAMETER_TYPE);
		createEAttribute(parameterTypeEClass, PARAMETER_TYPE__NAME);
		createEAttribute(parameterTypeEClass, PARAMETER_TYPE__MIXED);
		createEReference(parameterTypeEClass, PARAMETER_TYPE__SCRIPT);
		createEReference(parameterTypeEClass, PARAMETER_TYPE__MAP);
		createEReference(parameterTypeEClass, PARAMETER_TYPE__LIST);
		createEAttribute(parameterTypeEClass, PARAMETER_TYPE__TEXT);

		scriptTypeEClass = createEClass(SCRIPT_TYPE);
		createEAttribute(scriptTypeEClass, SCRIPT_TYPE__SCRIPT_FORMAT);
		createEAttribute(scriptTypeEClass, SCRIPT_TYPE__RESOURCE);
		createEAttribute(scriptTypeEClass, SCRIPT_TYPE__MIXED);
		createEAttribute(scriptTypeEClass, SCRIPT_TYPE__TEXT);

		mapTypeEClass = createEClass(MAP_TYPE);
		createEReference(mapTypeEClass, MAP_TYPE__ENTRIES);

		entryTypeEClass = createEClass(ENTRY_TYPE);
		createEAttribute(entryTypeEClass, ENTRY_TYPE__KEY);
		createEAttribute(entryTypeEClass, ENTRY_TYPE__MIXED);
		createEReference(entryTypeEClass, ENTRY_TYPE__SCRIPT);
		createEReference(entryTypeEClass, ENTRY_TYPE__MAP);
		createEReference(entryTypeEClass, ENTRY_TYPE__LIST);
		createEAttribute(entryTypeEClass, ENTRY_TYPE__TEXT);

		listTypeEClass = createEClass(LIST_TYPE);
		createEAttribute(listTypeEClass, LIST_TYPE__GROUP);
		createEReference(listTypeEClass, LIST_TYPE__SCRIPTS);
		createEReference(listTypeEClass, LIST_TYPE__MAPS);
		createEReference(listTypeEClass, LIST_TYPE__LISTS);
		createEAttribute(listTypeEClass, LIST_TYPE__VALUES);

		failedJobRetryTimeCycleTypeEClass = createEClass(FAILED_JOB_RETRY_TIME_CYCLE_TYPE);
		createEAttribute(failedJobRetryTimeCycleTypeEClass, FAILED_JOB_RETRY_TIME_CYCLE_TYPE__MIXED);
		createEAttribute(failedJobRetryTimeCycleTypeEClass, FAILED_JOB_RETRY_TIME_CYCLE_TYPE__TEXT);

		// Create enums
		eventTypeEEnum = createEEnum(EVENT_TYPE);
		eventType1EEnum = createEEnum(EVENT_TYPE1);
		historyTypeEEnum = createEEnum(HISTORY_TYPE);
		typeTypeEEnum = createEEnum(TYPE_TYPE);

		// Create data types
		classTypeEDataType = createEDataType(CLASS_TYPE);
		eventTypeObjectEDataType = createEDataType(EVENT_TYPE_OBJECT);
		eventTypeObject1EDataType = createEDataType(EVENT_TYPE_OBJECT1);
		formHandlerClassTypeEDataType = createEDataType(FORM_HANDLER_CLASS_TYPE);
		historyTypeObjectEDataType = createEDataType(HISTORY_TYPE_OBJECT);
		tExpressionEDataType = createEDataType(TEXPRESSION);
		typeTypeObjectEDataType = createEDataType(TYPE_TYPE_OBJECT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		Bpmn2Package theBpmn2Package = (Bpmn2Package)EPackage.Registry.INSTANCE.getEPackage(Bpmn2Package.eNS_URI);
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		documentRootEClass.getESuperTypes().add(theBpmn2Package.getDocumentRoot());
		startEventEClass.getESuperTypes().add(theBpmn2Package.getStartEvent());
		startEventEClass.getESuperTypes().add(this.getFormDataContainer());
		callActivityEClass.getESuperTypes().add(theBpmn2Package.getCallActivity());
		boundaryEventEClass.getESuperTypes().add(theBpmn2Package.getBoundaryEvent());
		userTaskEClass.getESuperTypes().add(theBpmn2Package.getUserTask());
		userTaskEClass.getESuperTypes().add(this.getFormDataContainer());
		expressionEClass.getESuperTypes().add(theBpmn2Package.getExpression());

		// Initialize classes and features; add operations and parameters
		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDocumentRoot_ExecutionListener(), this.getExecutionListenerType(), null, "executionListener", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Field(), this.getFieldType(), null, "field", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_FormProperty(), this.getFormPropertyType(), null, "formProperty", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_In(), this.getInType(), null, "in", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Out(), this.getOutType(), null, "out", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_TaskListener(), this.getTaskListenerType(), null, "taskListener", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Assignee(), ecorePackage.getEString(), "assignee", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_CandidateGroups(), ecorePackage.getEString(), "candidateGroups", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_CandidateUsers(), ecorePackage.getEString(), "candidateUsers", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Class(), this.getClassType(), "class", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Collection(), theXMLTypePackage.getString(), "collection", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_DelegateExpression(), theXMLTypePackage.getAnySimpleType(), "delegateExpression", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_DueDate(), this.getTExpression(), "dueDate", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_ElementVariable(), theXMLTypePackage.getString(), "elementVariable", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_FormHandlerClass(), this.getFormHandlerClassType(), "formHandlerClass", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_FormKey(), theXMLTypePackage.getString(), "formKey", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_History(), this.getHistoryType(), "history", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Initiator(), theXMLTypePackage.getString(), "initiator", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_ResultVariable(), theXMLTypePackage.getString(), "resultVariable", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Type(), this.getTypeType(), "type", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Async(), theXMLTypePackage.getBoolean(), "async", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_ActExpression(), ecorePackage.getEString(), "actExpression", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Priority(), ecorePackage.getEString(), "priority", "", 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_ResultVariableName(), ecorePackage.getEString(), "resultVariableName", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_FailedJobRetryTimeCycle(), this.getFailedJobRetryTimeCycleType(), null, "failedJobRetryTimeCycle", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_FormData(), this.getFormDataType(), null, "formData", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_FollowUpDate(), this.getTExpression(), "followUpDate", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Properties(), this.getPropertiesType(), null, "properties", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Exclusive(), theXMLTypePackage.getBoolean(), "exclusive", "true", 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Connector(), this.getConnectorType(), null, "connector", null, 0, 1, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_InputOutput(), this.getInputOutputType(), null, "inputOutput", null, 0, 1, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_AsyncAfter(), theXMLTypePackage.getBoolean(), "asyncAfter", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_AsyncBefore(), theXMLTypePackage.getBoolean(), "asyncBefore", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Resource1(), ecorePackage.getEString(), "resource1", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(startEventEClass, StartEvent.class, "StartEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(executionListenerTypeEClass, ExecutionListenerType.class, "ExecutionListenerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExecutionListenerType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, ExecutionListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionListenerType_Field(), this.getFieldType(), null, "field", null, 0, -1, ExecutionListenerType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionListenerType_Class(), theXMLTypePackage.getString(), "class", null, 0, 1, ExecutionListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionListenerType_DelegateExpression(), this.getTExpression(), "delegateExpression", null, 0, 1, ExecutionListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionListenerType_Event(), this.getEventType1(), "event", null, 0, 1, ExecutionListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExecutionListenerType_Expression(), this.getTExpression(), "expression", null, 0, 1, ExecutionListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExecutionListenerType_Script(), this.getScriptType(), null, "script", null, 0, 1, ExecutionListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fieldTypeEClass, FieldType.class, "FieldType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFieldType_String(), theXMLTypePackage.getString(), "string", null, 0, 1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFieldType_Expression(), this.getTExpression(), "expression", null, 0, 1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFieldType_Expression1(), this.getTExpression(), "expression1", null, 0, 1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFieldType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFieldType_StringValue(), theXMLTypePackage.getString(), "stringValue", null, 0, 1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(formPropertyTypeEClass, FormPropertyType.class, "FormPropertyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFormPropertyType_Value(), this.getValueType(), null, "value", null, 0, -1, FormPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormPropertyType_DatePattern(), theXMLTypePackage.getString(), "datePattern", null, 0, 1, FormPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormPropertyType_Expression(), theXMLTypePackage.getString(), "expression", null, 0, 1, FormPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormPropertyType_Id(), theXMLTypePackage.getString(), "id", null, 1, 1, FormPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormPropertyType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, FormPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormPropertyType_Readable(), theXMLTypePackage.getString(), "readable", null, 0, 1, FormPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormPropertyType_Required(), theXMLTypePackage.getString(), "required", null, 0, 1, FormPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormPropertyType_Type(), theXMLTypePackage.getString(), "type", null, 0, 1, FormPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormPropertyType_Value1(), this.getTExpression(), "value1", null, 0, 1, FormPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormPropertyType_Variable(), theXMLTypePackage.getString(), "variable", null, 0, 1, FormPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormPropertyType_Writable(), theXMLTypePackage.getString(), "writable", null, 0, 1, FormPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormPropertyType_Default(), theXMLTypePackage.getString(), "default", null, 0, 1, FormPropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(inTypeEClass, InType.class, "InType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInType_Source(), ecorePackage.getEString(), "source", null, 0, 1, InType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInType_SourceExpression(), this.getTExpression(), "sourceExpression", null, 0, 1, InType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInType_Target(), ecorePackage.getEString(), "target", null, 0, 1, InType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInType_Variables(), ecorePackage.getEString(), "variables", null, 0, 1, InType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInType_BusinessKey(), this.getTExpression(), "businessKey", null, 0, 1, InType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(outTypeEClass, OutType.class, "OutType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOutType_Source(), ecorePackage.getEString(), "source", null, 0, 1, OutType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOutType_SourceExpression(), this.getTExpression(), "sourceExpression", null, 0, 1, OutType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOutType_Target(), ecorePackage.getEString(), "target", null, 1, 1, OutType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOutType_Variables(), ecorePackage.getEString(), "variables", null, 0, 1, OutType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(taskListenerTypeEClass, TaskListenerType.class, "TaskListenerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTaskListenerType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, TaskListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTaskListenerType_Field(), this.getFieldType(), null, "field", null, 0, -1, TaskListenerType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskListenerType_Class(), theXMLTypePackage.getString(), "class", null, 0, 1, TaskListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskListenerType_DelegateExpression(), this.getTExpression(), "delegateExpression", null, 0, 1, TaskListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskListenerType_Event(), this.getEventType(), "event", null, 1, 1, TaskListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaskListenerType_Expression(), this.getTExpression(), "expression", null, 0, 1, TaskListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTaskListenerType_Script(), this.getScriptType(), null, "script", null, 0, 1, TaskListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(callActivityEClass, CallActivity.class, "CallActivity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCallActivity_CalledElement(), ecorePackage.getEString(), "calledElement", null, 0, 1, CallActivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCallActivity_CalledElementBinding(), ecorePackage.getEString(), "calledElementBinding", "latest", 0, 1, CallActivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCallActivity_CalledElementVersion(), theXMLTypePackage.getIntObject(), "calledElementVersion", null, 0, 1, CallActivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(boundaryEventEClass, BoundaryEvent.class, "BoundaryEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(valueTypeEClass, ValueType.class, "ValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getValueType_Id(), theXMLTypePackage.getString(), "id", null, 0, 1, ValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, ValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(formDataTypeEClass, FormDataType.class, "FormDataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFormDataType_FormField(), this.getFormFieldType(), null, "formField", null, 0, -1, FormDataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(formFieldTypeEClass, FormFieldType.class, "FormFieldType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFormFieldType_Id(), ecorePackage.getEString(), "id", null, 0, 1, FormFieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormFieldType_Label(), ecorePackage.getEString(), "label", null, 0, 1, FormFieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormFieldType_Type(), ecorePackage.getEString(), "type", null, 0, 1, FormFieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFormFieldType_DefaultValue(), ecorePackage.getEString(), "defaultValue", null, 0, 1, FormFieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFormFieldType_Properties(), this.getPropertiesType(), null, "properties", null, 0, 1, FormFieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFormFieldType_Validation(), this.getValidationType(), null, "validation", null, 0, 1, FormFieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFormFieldType_Value(), this.getValueType(), null, "value", null, 0, -1, FormFieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(propertiesTypeEClass, PropertiesType.class, "PropertiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPropertiesType_Property(), this.getPropertyType(), null, "property", null, 0, -1, PropertiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(propertyTypeEClass, PropertyType.class, "PropertyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPropertyType_Id(), ecorePackage.getEString(), "id", null, 0, 1, PropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPropertyType_Value(), ecorePackage.getEString(), "value", null, 0, 1, PropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPropertyType_Name(), ecorePackage.getEString(), "name", null, 0, 1, PropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(validationTypeEClass, ValidationType.class, "ValidationType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getValidationType_Constraint(), this.getConstraintType(), null, "constraint", null, 0, -1, ValidationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constraintTypeEClass, ConstraintType.class, "ConstraintType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConstraintType_Name(), ecorePackage.getEString(), "name", null, 0, 1, ConstraintType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConstraintType_Config(), ecorePackage.getEString(), "config", null, 0, 1, ConstraintType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(userTaskEClass, UserTask.class, "UserTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(formDataContainerEClass, FormDataContainer.class, "FormDataContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(expressionEClass, Expression.class, "Expression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExpression_Body(), ecorePackage.getEString(), "body", null, 1, 1, Expression.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
		initEAttribute(getExpression_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, Expression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(connectorTypeEClass, ConnectorType.class, "ConnectorType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConnectorType_ConnectorId(), ecorePackage.getEString(), "connectorId", null, 0, 1, ConnectorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConnectorType_InputOutput(), this.getInputOutputType(), null, "inputOutput", null, 0, 1, ConnectorType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(inputOutputTypeEClass, InputOutputType.class, "InputOutputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInputOutputType_InputParameters(), this.getParameterType(), null, "inputParameters", null, 0, -1, InputOutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInputOutputType_OutputParameters(), this.getParameterType(), null, "outputParameters", null, 0, -1, InputOutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parameterTypeEClass, ParameterType.class, "ParameterType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getParameterType_Name(), ecorePackage.getEString(), "name", null, 0, 1, ParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterType_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, ParameterType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getParameterType_Script(), this.getScriptType(), null, "script", null, 0, 1, ParameterType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getParameterType_Map(), this.getMapType(), null, "map", null, 0, 1, ParameterType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getParameterType_List(), this.getListType(), null, "list", null, 0, 1, ParameterType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterType_Text(), ecorePackage.getEString(), "text", null, 0, 1, ParameterType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

		initEClass(scriptTypeEClass, ScriptType.class, "ScriptType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getScriptType_ScriptFormat(), ecorePackage.getEString(), "scriptFormat", null, 1, 1, ScriptType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getScriptType_Resource(), ecorePackage.getEString(), "resource", null, 0, 1, ScriptType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScriptType_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, ScriptType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScriptType_Text(), ecorePackage.getEString(), "text", null, 0, 1, ScriptType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

		initEClass(mapTypeEClass, MapType.class, "MapType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMapType_Entries(), this.getEntryType(), null, "entries", null, 0, -1, MapType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(entryTypeEClass, EntryType.class, "EntryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEntryType_Key(), ecorePackage.getEString(), "key", null, 1, 1, EntryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getEntryType_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, EntryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntryType_Script(), this.getScriptType(), null, "script", null, 0, 1, EntryType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getEntryType_Map(), this.getMapType(), null, "map", null, 0, 1, EntryType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getEntryType_List(), this.getListType(), null, "list", null, 0, 1, EntryType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntryType_Text(), ecorePackage.getEString(), "text", null, 0, 1, EntryType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

		initEClass(listTypeEClass, ListType.class, "ListType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getListType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, ListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getListType_Scripts(), this.getScriptType(), null, "scripts", null, 0, -1, ListType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getListType_Maps(), this.getMapType(), null, "maps", null, 0, -1, ListType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getListType_Lists(), this.getListType(), null, "lists", null, 0, -1, ListType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getListType_Values(), ecorePackage.getEString(), "values", null, 0, -1, ListType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(failedJobRetryTimeCycleTypeEClass, FailedJobRetryTimeCycleType.class, "FailedJobRetryTimeCycleType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFailedJobRetryTimeCycleType_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, FailedJobRetryTimeCycleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFailedJobRetryTimeCycleType_Text(), ecorePackage.getEString(), "text", null, 1, 1, FailedJobRetryTimeCycleType.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(eventTypeEEnum, EventType.class, "EventType");
		addEEnumLiteral(eventTypeEEnum, EventType.CREATE);
		addEEnumLiteral(eventTypeEEnum, EventType.ASSIGNMENT);
		addEEnumLiteral(eventTypeEEnum, EventType.COMPLETE);
		addEEnumLiteral(eventTypeEEnum, EventType.DELETE);

		initEEnum(eventType1EEnum, EventType1.class, "EventType1");
		addEEnumLiteral(eventType1EEnum, EventType1.START);
		addEEnumLiteral(eventType1EEnum, EventType1.END);
		addEEnumLiteral(eventType1EEnum, EventType1.TAKE);

		initEEnum(historyTypeEEnum, HistoryType.class, "HistoryType");
		addEEnumLiteral(historyTypeEEnum, HistoryType.NONE);
		addEEnumLiteral(historyTypeEEnum, HistoryType.ACTIVITY);
		addEEnumLiteral(historyTypeEEnum, HistoryType.AUDIT);
		addEEnumLiteral(historyTypeEEnum, HistoryType.FULL);

		initEEnum(typeTypeEEnum, TypeType.class, "TypeType");
		addEEnumLiteral(typeTypeEEnum, TypeType.MAIL);

		// Initialize data types
		initEDataType(classTypeEDataType, String.class, "ClassType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(eventTypeObjectEDataType, EventType.class, "EventTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(eventTypeObject1EDataType, EventType1.class, "EventTypeObject1", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(formHandlerClassTypeEDataType, String.class, "FormHandlerClassType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(historyTypeObjectEDataType, HistoryType.class, "HistoryTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(tExpressionEDataType, String.class, "TExpression", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(typeTypeObjectEDataType, TypeType.class, "TypeTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";			
		addAnnotation
		  (documentRootEClass, 
		   source, 
		   new String[] {
			 "name", "",
			 "kind", "mixed",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_ExecutionListener(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "executionListener",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Field(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "field",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_FormProperty(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "formProperty",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_In(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "in",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Out(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "out",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_TaskListener(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "taskListener",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Assignee(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "assignee",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_CandidateGroups(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "candidateGroups",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_CandidateUsers(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "candidateUsers",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Class(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "class",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Collection(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "collection",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_DelegateExpression(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "delegateExpression",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_DueDate(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "dueDate",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_ElementVariable(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "elementVariable",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_FormHandlerClass(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "formHandlerClass",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_FormKey(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "formKey",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_History(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "history",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Initiator(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "initiator",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_ResultVariable(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "resultVariable",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Async(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "async",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_ActExpression(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "expression",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Priority(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "priority",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_ResultVariableName(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "resultVariableName",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_FailedJobRetryTimeCycle(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "failedJobRetryTimeCycle",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_FormData(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "formData",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_FollowUpDate(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "followUpDate",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Properties(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "properties",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Exclusive(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "exclusive",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Connector(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "connector",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_InputOutput(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "inputOutput",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_AsyncAfter(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "asyncAfter",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_AsyncBefore(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "asyncBefore",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Resource1(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "resource",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (classTypeEDataType, 
		   source, 
		   new String[] {
			 "name", "class_._type",
			 "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
			 "pattern", "([a-z]{2,3}(\\.[a-zA-Z][a-zA-Z_$0-9]*)*)\\.([A-Z][a-zA-Z_$0-9]*)"
		   });		
		addAnnotation
		  (eventTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "event_._1_._type"
		   });		
		addAnnotation
		  (eventType1EEnum, 
		   source, 
		   new String[] {
			 "name", "event_._type"
		   });		
		addAnnotation
		  (eventTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "event_._1_._type:Object",
			 "baseType", "event_._1_._type"
		   });		
		addAnnotation
		  (eventTypeObject1EDataType, 
		   source, 
		   new String[] {
			 "name", "event_._type:Object",
			 "baseType", "event_._type"
		   });		
		addAnnotation
		  (executionListenerTypeEClass, 
		   source, 
		   new String[] {
			 "name", "executionListener_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getExecutionListenerType_Group(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "group:0"
		   });			
		addAnnotation
		  (getExecutionListenerType_Field(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "field",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });			
		addAnnotation
		  (getExecutionListenerType_Class(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "class"
		   });			
		addAnnotation
		  (getExecutionListenerType_DelegateExpression(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "delegateExpression"
		   });			
		addAnnotation
		  (getExecutionListenerType_Event(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "event"
		   });			
		addAnnotation
		  (getExecutionListenerType_Expression(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "expression"
		   });		
		addAnnotation
		  (getExecutionListenerType_Script(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "script",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (fieldTypeEClass, 
		   source, 
		   new String[] {
			 "name", "field_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getFieldType_String(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "string",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getFieldType_Expression(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "expression",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getFieldType_Expression1(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "expression"
		   });		
		addAnnotation
		  (getFieldType_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (getFieldType_StringValue(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "stringValue"
		   });		
		addAnnotation
		  (formHandlerClassTypeEDataType, 
		   source, 
		   new String[] {
			 "name", "formHandlerClass_._type",
			 "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
			 "pattern", "([a-z]{2,3}(\\.[a-zA-Z][a-zA-Z_$0-9]*)*)\\.([A-Z][a-zA-Z_$0-9]*)"
		   });		
		addAnnotation
		  (formPropertyTypeEClass, 
		   source, 
		   new String[] {
			 "name", "formProperty_._type",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getFormPropertyType_Value(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "value",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getFormPropertyType_DatePattern(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "datePattern"
		   });			
		addAnnotation
		  (getFormPropertyType_Expression(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "expression"
		   });			
		addAnnotation
		  (getFormPropertyType_Id(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "id"
		   });			
		addAnnotation
		  (getFormPropertyType_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });			
		addAnnotation
		  (getFormPropertyType_Readable(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "readable"
		   });			
		addAnnotation
		  (getFormPropertyType_Required(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "required"
		   });			
		addAnnotation
		  (getFormPropertyType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type"
		   });			
		addAnnotation
		  (getFormPropertyType_Value1(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "value"
		   });			
		addAnnotation
		  (getFormPropertyType_Variable(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "variable"
		   });			
		addAnnotation
		  (getFormPropertyType_Writable(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "writable"
		   });		
		addAnnotation
		  (historyTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "history_._type"
		   });		
		addAnnotation
		  (historyTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "history_._type:Object",
			 "baseType", "history_._type"
		   });		
		addAnnotation
		  (inTypeEClass, 
		   source, 
		   new String[] {
			 "name", "in_._type",
			 "kind", "empty"
		   });		
		addAnnotation
		  (getInType_Source(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "source"
		   });		
		addAnnotation
		  (getInType_SourceExpression(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "sourceExpression"
		   });		
		addAnnotation
		  (getInType_Target(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "target"
		   });		
		addAnnotation
		  (getInType_Variables(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "variables"
		   });		
		addAnnotation
		  (getInType_BusinessKey(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "businessKey"
		   });		
		addAnnotation
		  (outTypeEClass, 
		   source, 
		   new String[] {
			 "name", "out_._type",
			 "kind", "empty"
		   });		
		addAnnotation
		  (getOutType_Source(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "source"
		   });		
		addAnnotation
		  (getOutType_SourceExpression(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "sourceExpression"
		   });		
		addAnnotation
		  (getOutType_Target(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "target"
		   });		
		addAnnotation
		  (getOutType_Variables(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "variables"
		   });		
		addAnnotation
		  (taskListenerTypeEClass, 
		   source, 
		   new String[] {
			 "name", "taskListener_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getTaskListenerType_Group(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "group:0"
		   });			
		addAnnotation
		  (getTaskListenerType_Field(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "field",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });			
		addAnnotation
		  (getTaskListenerType_Class(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "class"
		   });			
		addAnnotation
		  (getTaskListenerType_DelegateExpression(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "delegateExpression"
		   });			
		addAnnotation
		  (getTaskListenerType_Event(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "event"
		   });			
		addAnnotation
		  (getTaskListenerType_Expression(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "expression"
		   });		
		addAnnotation
		  (getTaskListenerType_Script(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "script",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (tExpressionEDataType, 
		   source, 
		   new String[] {
			 "name", "tExpression",
			 "baseType", "http://www.eclipse.org/emf/2003/XMLType#string"
		   });		
		addAnnotation
		  (typeTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "type_._type"
		   });		
		addAnnotation
		  (typeTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "type_._type:Object",
			 "baseType", "type_._type"
		   });		
		addAnnotation
		  (callActivityEClass, 
		   source, 
		   new String[] {
			 "name", "tCallActivity",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getCallActivity_CalledElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "calledElement"
		   });		
		addAnnotation
		  (getCallActivity_CalledElementBinding(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "calledElementBinding",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getCallActivity_CalledElementVersion(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "calledElementVersion",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (valueTypeEClass, 
		   source, 
		   new String[] {
			 "name", "value_._type",
			 "kind", "empty"
		   });				
		addAnnotation
		  (getValueType_Id(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "id"
		   });			
		addAnnotation
		  (getValueType_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (formDataTypeEClass, 
		   source, 
		   new String[] {
			 "name", "formData_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getFormDataType_FormField(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "formField",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (formFieldTypeEClass, 
		   source, 
		   new String[] {
			 "name", "formField_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getFormFieldType_Id(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "id"
		   });		
		addAnnotation
		  (getFormFieldType_Label(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "label", "label"
		   });		
		addAnnotation
		  (getFormFieldType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type"
		   });		
		addAnnotation
		  (getFormFieldType_DefaultValue(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "defaultValue"
		   });		
		addAnnotation
		  (getFormFieldType_Properties(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "properties",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getFormFieldType_Validation(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "validation",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getFormFieldType_Value(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "value",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (propertiesTypeEClass, 
		   source, 
		   new String[] {
			 "name", "properties_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getPropertiesType_Property(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "property",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (propertyTypeEClass, 
		   source, 
		   new String[] {
			 "name", "property_._type",
			 "kind", "empty"
		   });		
		addAnnotation
		  (getPropertyType_Id(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "id"
		   });		
		addAnnotation
		  (getPropertyType_Value(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "value"
		   });		
		addAnnotation
		  (getPropertyType_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (validationTypeEClass, 
		   source, 
		   new String[] {
			 "name", "validation_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getValidationType_Constraint(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "constraint",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (constraintTypeEClass, 
		   source, 
		   new String[] {
			 "name", "constraint_._type",
			 "kind", "empty"
		   });		
		addAnnotation
		  (getConstraintType_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (getConstraintType_Config(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "config"
		   });		
		addAnnotation
		  (expressionEClass, 
		   source, 
		   new String[] {
			 "name", "tExpression",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getExpression_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (connectorTypeEClass, 
		   source, 
		   new String[] {
			 "name", "connector_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getConnectorType_ConnectorId(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "connectorId",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getConnectorType_InputOutput(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "inputOutput",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (inputOutputTypeEClass, 
		   source, 
		   new String[] {
			 "name", "inputOutput_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getInputOutputType_InputParameters(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "inputParameter",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getInputOutputType_OutputParameters(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "outputParameter",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (parameterTypeEClass, 
		   source, 
		   new String[] {
			 "name", "parameter_._type",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getParameterType_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (getParameterType_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getParameterType_Script(), 
		   source, 
		   new String[] {
			 "name", "script",
			 "kind", "element",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getParameterType_Map(), 
		   source, 
		   new String[] {
			 "name", "map",
			 "kind", "element",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getParameterType_List(), 
		   source, 
		   new String[] {
			 "name", "list",
			 "kind", "element",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (scriptTypeEClass, 
		   source, 
		   new String[] {
			 "name", "script_._type",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getScriptType_ScriptFormat(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "scriptFormat"
		   });		
		addAnnotation
		  (getScriptType_Resource(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "resource"
		   });		
		addAnnotation
		  (getScriptType_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (mapTypeEClass, 
		   source, 
		   new String[] {
			 "name", "map_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getMapType_Entries(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "entry",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (entryTypeEClass, 
		   source, 
		   new String[] {
			 "name", "entry_._type",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getEntryType_Key(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "key"
		   });		
		addAnnotation
		  (getEntryType_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getEntryType_Script(), 
		   source, 
		   new String[] {
			 "name", "script",
			 "kind", "element",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEntryType_Map(), 
		   source, 
		   new String[] {
			 "name", "map",
			 "kind", "element",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEntryType_List(), 
		   source, 
		   new String[] {
			 "name", "list",
			 "kind", "element",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (listTypeEClass, 
		   source, 
		   new String[] {
			 "name", "list_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getListType_Group(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "value:group"
		   });		
		addAnnotation
		  (getListType_Scripts(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "script",
			 "namespace", "##targetNamespace",
			 "group", "#value:group"
		   });		
		addAnnotation
		  (getListType_Maps(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "map",
			 "namespace", "##targetNamespace",
			 "group", "#value:group"
		   });		
		addAnnotation
		  (getListType_Lists(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "list",
			 "namespace", "##targetNamespace",
			 "group", "#value:group"
		   });		
		addAnnotation
		  (getListType_Values(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "value",
			 "namespace", "##targetNamespace",
			 "group", "#value:group"
		   });		
		addAnnotation
		  (failedJobRetryTimeCycleTypeEClass, 
		   source, 
		   new String[] {
			 "name", "tFailedJobRetryCycleType",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getFailedJobRetryTimeCycleType_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });
	}

} //ModelPackageImpl
