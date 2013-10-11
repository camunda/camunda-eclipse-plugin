package org.camunda.bpm.modeler.ui.property.tabs.binding;

import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.addExtension;
import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.getExtension;
import static org.camunda.bpm.modeler.core.utils.ExtensionUtil.removeExtensionByValue;

import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.widgets.Button;

/**
 * Establishes a extension element model to button binding
 * e.g. 
 * call activity extension elements in and out 
 * <bpmn2:callActivity id="CallActivity_1" name="Call Activity 1">
 *  <bpmn2:extensionElements>
 *   <camunda:in variables="all"/>
 *   <camunda:in businessKey="#{execution.processBusinessKey}"/>
 *   <camunda:out variables="all"/>
 *  </bpmn2:extensionElements>
 * </bpmn2:callActivity> 
 * 
 * button binding code for extension element <camunda:in variables="all"/>
 * new ModelExtensionButtonBinding(callActivity, InType.class, ModelPackage.eINSTANCE.getDocumentRoot_In(),
 *                                 ModelPackage.eINSTANCE.getInType_Variables(), checkbox, "all")
 * .establish();
 * @author kristin.polenz
 */
public class ModelExtensionButtonBinding extends ModelButtonBinding<Boolean> {
  
  private EClass typeECls;
  private EStructuralFeature featureParent;
  private String featureValue;


	public ModelExtensionButtonBinding(EObject model, EClass typeECls, EStructuralFeature featureParent, EStructuralFeature feature, Button control, String featureValue) {
		super(model, feature, control);
		
		this.typeECls = typeECls;
		this.featureParent = featureParent;
		this.featureValue = featureValue;
	}
	
  @Override
  public void setViewValue(Boolean value) {
    control.setSelection(value);
  }
  
  @Override
  public Boolean getViewValue() throws IllegalArgumentException {
    return control.getSelection();
  }

  @Override
  public Boolean getModelValue() {
    return featureValue.equals(getExtension(model, featureParent, feature.getName()));
  }

  @Override
  public void setModelValue(final Boolean selected) {
    
    TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
    editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
      
      @Override
      protected void doExecute() {
        Object extension = getExtension(model, featureParent, feature.getName());
        if (extension == null) {
          final EObject eObject = ModelFactory.eINSTANCE.create(typeECls);
          eObject.eSet(feature, selected ? featureValue : "");
          addExtension(model, featureParent, eObject);
        } else {
          removeExtensionByValue(model, featureParent, feature, extension);
        }
      }
    });
  }
}
