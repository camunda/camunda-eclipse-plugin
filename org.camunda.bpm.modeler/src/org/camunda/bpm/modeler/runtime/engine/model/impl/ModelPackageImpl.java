/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.camunda.bpm.modeler.runtime.engine.model.impl;

import org.camunda.bpm.modeler.runtime.engine.model.BoundaryEvent;
import org.camunda.bpm.modeler.runtime.engine.model.CallActivity;
import org.camunda.bpm.modeler.runtime.engine.model.DocumentRoot;
import org.camunda.bpm.modeler.runtime.engine.model.EventType;
import org.camunda.bpm.modeler.runtime.engine.model.EventType1;
import org.camunda.bpm.modeler.runtime.engine.model.ExecutionListenerType;
import org.camunda.bpm.modeler.runtime.engine.model.FieldType;
import org.camunda.bpm.modeler.runtime.engine.model.FormPropertyType;
import org.camunda.bpm.modeler.runtime.engine.model.HistoryType;
import org.camunda.bpm.modeler.runtime.engine.model.InType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.OutType;
import org.camunda.bpm.modeler.runtime.engine.model.TaskListenerType;
import org.camunda.bpm.modeler.runtime.engine.model.TypeType;
import org.camunda.bpm.modeler.runtime.engine.model.ValueType;
import org.camunda.bpm.modeler.runtime.engine.model.fox.FoxPackage;
import org.camunda.bpm.modeler.runtime.engine.model.fox.impl.FoxPackageImpl;
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

    // Obtain or create and register interdependencies
    FoxPackageImpl theFoxPackage = (FoxPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(FoxPackage.eNS_URI) instanceof FoxPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(FoxPackage.eNS_URI) : FoxPackage.eINSTANCE);

    // Create package meta-data objects
    theModelPackage.createPackageContents();
    theFoxPackage.createPackageContents();

    // Initialize created meta-data
    theModelPackage.initializePackageContents();
    theFoxPackage.initializePackageContents();

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

    executionListenerTypeEClass = createEClass(EXECUTION_LISTENER_TYPE);
    createEAttribute(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__GROUP);
    createEReference(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__FIELD);
    createEAttribute(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__CLASS);
    createEAttribute(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__DELEGATE_EXPRESSION);
    createEAttribute(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__EVENT);
    createEAttribute(executionListenerTypeEClass, EXECUTION_LISTENER_TYPE__EXPRESSION);

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

    outTypeEClass = createEClass(OUT_TYPE);
    createEAttribute(outTypeEClass, OUT_TYPE__SOURCE);
    createEAttribute(outTypeEClass, OUT_TYPE__SOURCE_EXPRESSION);
    createEAttribute(outTypeEClass, OUT_TYPE__TARGET);

    taskListenerTypeEClass = createEClass(TASK_LISTENER_TYPE);
    createEAttribute(taskListenerTypeEClass, TASK_LISTENER_TYPE__GROUP);
    createEReference(taskListenerTypeEClass, TASK_LISTENER_TYPE__FIELD);
    createEAttribute(taskListenerTypeEClass, TASK_LISTENER_TYPE__CLASS);
    createEAttribute(taskListenerTypeEClass, TASK_LISTENER_TYPE__DELEGATE_EXPRESSION);
    createEAttribute(taskListenerTypeEClass, TASK_LISTENER_TYPE__EVENT);
    createEAttribute(taskListenerTypeEClass, TASK_LISTENER_TYPE__EXPRESSION);

    callActivityEClass = createEClass(CALL_ACTIVITY);
    createEAttribute(callActivityEClass, CALL_ACTIVITY__CALLED_ELEMENT);

    boundaryEventEClass = createEClass(BOUNDARY_EVENT);

    valueTypeEClass = createEClass(VALUE_TYPE);
    createEAttribute(valueTypeEClass, VALUE_TYPE__ID);
    createEAttribute(valueTypeEClass, VALUE_TYPE__NAME);

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
    FoxPackage theFoxPackage = (FoxPackage)EPackage.Registry.INSTANCE.getEPackage(FoxPackage.eNS_URI);
    Bpmn2Package theBpmn2Package = (Bpmn2Package)EPackage.Registry.INSTANCE.getEPackage(Bpmn2Package.eNS_URI);
    XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

    // Add subpackages
    getESubpackages().add(theFoxPackage);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    documentRootEClass.getESuperTypes().add(theBpmn2Package.getDocumentRoot());
    callActivityEClass.getESuperTypes().add(theBpmn2Package.getCallActivity());
    boundaryEventEClass.getESuperTypes().add(theBpmn2Package.getBoundaryEvent());

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
    initEAttribute(getDocumentRoot_Priority(), ecorePackage.getEInt(), "priority", "50", 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDocumentRoot_ResultVariableName(), ecorePackage.getEString(), "resultVariableName", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_FailedJobRetryTimeCycle(), theFoxPackage.getFailedJobRetryTimeCycleType(), null, "failedJobRetryTimeCycle", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    initEClass(executionListenerTypeEClass, ExecutionListenerType.class, "ExecutionListenerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getExecutionListenerType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, ExecutionListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getExecutionListenerType_Field(), this.getFieldType(), null, "field", null, 0, -1, ExecutionListenerType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getExecutionListenerType_Class(), theXMLTypePackage.getString(), "class", null, 0, 1, ExecutionListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getExecutionListenerType_DelegateExpression(), this.getTExpression(), "delegateExpression", null, 0, 1, ExecutionListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getExecutionListenerType_Event(), this.getEventType1(), "event", null, 0, 1, ExecutionListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getExecutionListenerType_Expression(), this.getTExpression(), "expression", null, 0, 1, ExecutionListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
    initEAttribute(getInType_Target(), ecorePackage.getEString(), "target", null, 1, 1, InType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(outTypeEClass, OutType.class, "OutType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getOutType_Source(), ecorePackage.getEString(), "source", null, 0, 1, OutType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getOutType_SourceExpression(), this.getTExpression(), "sourceExpression", null, 0, 1, OutType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getOutType_Target(), ecorePackage.getEString(), "target", null, 1, 1, OutType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(taskListenerTypeEClass, TaskListenerType.class, "TaskListenerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTaskListenerType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, TaskListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTaskListenerType_Field(), this.getFieldType(), null, "field", null, 0, -1, TaskListenerType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getTaskListenerType_Class(), theXMLTypePackage.getString(), "class", null, 0, 1, TaskListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTaskListenerType_DelegateExpression(), this.getTExpression(), "delegateExpression", null, 0, 1, TaskListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTaskListenerType_Event(), this.getEventType(), "event", null, 1, 1, TaskListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTaskListenerType_Expression(), this.getTExpression(), "expression", null, 0, 1, TaskListenerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(callActivityEClass, CallActivity.class, "CallActivity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getCallActivity_CalledElement(), ecorePackage.getEString(), "calledElement", null, 0, 1, CallActivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(boundaryEventEClass, BoundaryEvent.class, "BoundaryEvent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(valueTypeEClass, ValueType.class, "ValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getValueType_Id(), theXMLTypePackage.getString(), "id", null, 0, 1, ValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getValueType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, ValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(eventTypeEEnum, EventType.class, "EventType");
    addEEnumLiteral(eventTypeEEnum, EventType.CREATE);
    addEEnumLiteral(eventTypeEEnum, EventType.ASSIGNMENT);
    addEEnumLiteral(eventTypeEEnum, EventType.COMPLETE);

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
       "namespace", "http://www.camunda.com/fox"
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
  }

} //ModelPackageImpl
