/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.camunda.bpm.modeler.runtime.activiti.model.util;

import java.util.Map;

import org.camunda.bpm.modeler.runtime.activiti.model.BoundaryEvent;
import org.camunda.bpm.modeler.runtime.activiti.model.CallActivity;
import org.camunda.bpm.modeler.runtime.activiti.model.DocumentRoot;
import org.camunda.bpm.modeler.runtime.activiti.model.EventType;
import org.camunda.bpm.modeler.runtime.activiti.model.EventType1;
import org.camunda.bpm.modeler.runtime.activiti.model.ExecutionListenerType;
import org.camunda.bpm.modeler.runtime.activiti.model.FieldType;
import org.camunda.bpm.modeler.runtime.activiti.model.FormPropertyType;
import org.camunda.bpm.modeler.runtime.activiti.model.HistoryType;
import org.camunda.bpm.modeler.runtime.activiti.model.InType;
import org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.activiti.model.OutType;
import org.camunda.bpm.modeler.runtime.activiti.model.TaskListenerType;
import org.camunda.bpm.modeler.runtime.activiti.model.TypeType;
import org.camunda.bpm.modeler.runtime.activiti.model.ValueType;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.camunda.bpm.modeler.runtime.activiti.model.ModelPackage
 * @generated
 */
public class ModelValidator extends EObjectValidator {
	/**
   * The cached model package
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public static final ModelValidator INSTANCE = new ModelValidator();

	/**
   * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.emf.common.util.Diagnostic#getSource()
   * @see org.eclipse.emf.common.util.Diagnostic#getCode()
   * @generated
   */
	public static final String DIAGNOSTIC_SOURCE = "org.eclipse.bpmn2.modeler.runtime.activiti.model";

	/**
   * A constant with a fixed name that can be used as the base value for additional hand written constants.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
   * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
   * The cached base package validator.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected XMLTypeValidator xmlTypeValidator;

	/**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public ModelValidator() {
    super();
    xmlTypeValidator = XMLTypeValidator.INSTANCE;
  }

	/**
   * Returns the package of this validator switch.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	protected EPackage getEPackage() {
    return ModelPackage.eINSTANCE;
  }

	/**
   * Calls <code>validateXXX</code> for the corresponding classifier of the model.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
    switch (classifierID) {
      case ModelPackage.DOCUMENT_ROOT:
        return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
      case ModelPackage.EXECUTION_LISTENER_TYPE:
        return validateExecutionListenerType((ExecutionListenerType)value, diagnostics, context);
      case ModelPackage.FIELD_TYPE:
        return validateFieldType((FieldType)value, diagnostics, context);
      case ModelPackage.FORM_PROPERTY_TYPE:
        return validateFormPropertyType((FormPropertyType)value, diagnostics, context);
      case ModelPackage.IN_TYPE:
        return validateInType((InType)value, diagnostics, context);
      case ModelPackage.OUT_TYPE:
        return validateOutType((OutType)value, diagnostics, context);
      case ModelPackage.TASK_LISTENER_TYPE:
        return validateTaskListenerType((TaskListenerType)value, diagnostics, context);
      case ModelPackage.CALL_ACTIVITY:
        return validateCallActivity((CallActivity)value, diagnostics, context);
      case ModelPackage.BOUNDARY_EVENT:
        return validateBoundaryEvent((BoundaryEvent)value, diagnostics, context);
      case ModelPackage.VALUE_TYPE:
        return validateValueType((ValueType)value, diagnostics, context);
      case ModelPackage.EVENT_TYPE:
        return validateEventType((EventType)value, diagnostics, context);
      case ModelPackage.EVENT_TYPE1:
        return validateEventType1((EventType1)value, diagnostics, context);
      case ModelPackage.HISTORY_TYPE:
        return validateHistoryType((HistoryType)value, diagnostics, context);
      case ModelPackage.TYPE_TYPE:
        return validateTypeType((TypeType)value, diagnostics, context);
      case ModelPackage.CLASS_TYPE:
        return validateClassType((String)value, diagnostics, context);
      case ModelPackage.EVENT_TYPE_OBJECT:
        return validateEventTypeObject((EventType)value, diagnostics, context);
      case ModelPackage.EVENT_TYPE_OBJECT1:
        return validateEventTypeObject1((EventType1)value, diagnostics, context);
      case ModelPackage.FORM_HANDLER_CLASS_TYPE:
        return validateFormHandlerClassType((String)value, diagnostics, context);
      case ModelPackage.HISTORY_TYPE_OBJECT:
        return validateHistoryTypeObject((HistoryType)value, diagnostics, context);
      case ModelPackage.TEXPRESSION:
        return validateTExpression((String)value, diagnostics, context);
      case ModelPackage.TYPE_TYPE_OBJECT:
        return validateTypeTypeObject((TypeType)value, diagnostics, context);
      default:
        return true;
    }
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateDocumentRoot(DocumentRoot documentRoot, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return validate_EveryDefaultConstraint(documentRoot, diagnostics, context);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateExecutionListenerType(ExecutionListenerType executionListenerType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return validate_EveryDefaultConstraint(executionListenerType, diagnostics, context);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateFieldType(FieldType fieldType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return validate_EveryDefaultConstraint(fieldType, diagnostics, context);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateFormPropertyType(FormPropertyType formPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return validate_EveryDefaultConstraint(formPropertyType, diagnostics, context);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateInType(InType inType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return validate_EveryDefaultConstraint(inType, diagnostics, context);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateOutType(OutType outType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return validate_EveryDefaultConstraint(outType, diagnostics, context);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateTaskListenerType(TaskListenerType taskListenerType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return validate_EveryDefaultConstraint(taskListenerType, diagnostics, context);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateCallActivity(CallActivity callActivity, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return validate_EveryDefaultConstraint(callActivity, diagnostics, context);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateBoundaryEvent(BoundaryEvent boundaryEvent, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return validate_EveryDefaultConstraint(boundaryEvent, diagnostics, context);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateValueType(ValueType valueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return validate_EveryDefaultConstraint(valueType, diagnostics, context);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateEventType(EventType eventType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return true;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateEventType1(EventType1 eventType1, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return true;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateHistoryType(HistoryType historyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return true;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateTypeType(TypeType typeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return true;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateClassType(String classType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    boolean result = validateClassType_Pattern(classType, diagnostics, context);
    return result;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @see #validateClassType_Pattern
   */
	public static final  PatternMatcher [][] CLASS_TYPE__PATTERN__VALUES =
		new PatternMatcher [][] {
      new PatternMatcher [] {
        XMLTypeUtil.createPatternMatcher("([a-z]{2,3}(\\.[a-zA-Z][a-zA-Z_$0-9]*)*)\\.([A-Z][a-zA-Z_$0-9]*)")
      }
    };

	/**
   * Validates the Pattern constraint of '<em>Class Type</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateClassType_Pattern(String classType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return validatePattern(ModelPackage.Literals.CLASS_TYPE, classType, CLASS_TYPE__PATTERN__VALUES, diagnostics, context);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateEventTypeObject(EventType eventTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return true;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateEventTypeObject1(EventType1 eventTypeObject1, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return true;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateFormHandlerClassType(String formHandlerClassType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    boolean result = validateFormHandlerClassType_Pattern(formHandlerClassType, diagnostics, context);
    return result;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @see #validateFormHandlerClassType_Pattern
   */
	public static final  PatternMatcher [][] FORM_HANDLER_CLASS_TYPE__PATTERN__VALUES =
		new PatternMatcher [][] {
      new PatternMatcher [] {
        XMLTypeUtil.createPatternMatcher("([a-z]{2,3}(\\.[a-zA-Z][a-zA-Z_$0-9]*)*)\\.([A-Z][a-zA-Z_$0-9]*)")
      }
    };

	/**
   * Validates the Pattern constraint of '<em>Form Handler Class Type</em>'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateFormHandlerClassType_Pattern(String formHandlerClassType, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return validatePattern(ModelPackage.Literals.FORM_HANDLER_CLASS_TYPE, formHandlerClassType, FORM_HANDLER_CLASS_TYPE__PATTERN__VALUES, diagnostics, context);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateHistoryTypeObject(HistoryType historyTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return true;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateTExpression(String tExpression, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return true;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean validateTypeTypeObject(TypeType typeTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
    return true;
  }

	/**
   * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public ResourceLocator getResourceLocator() {
    // TODO
    // Specialize this to return a resource locator for messages specific to this validator.
    // Ensure that you remove @generated or mark it @generated NOT
    return super.getResourceLocator();
  }

} //ModelValidator
