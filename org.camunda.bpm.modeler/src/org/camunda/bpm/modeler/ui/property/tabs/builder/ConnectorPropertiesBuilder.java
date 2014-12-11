package org.camunda.bpm.modeler.ui.property.tabs.builder;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.ConnectorType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.property.tabs.binding.StringTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author Roman Smirnov
 */
public class ConnectorPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	public ConnectorPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		createConnectorIdText();
		createParamterTables();
	}
	
	protected void createConnectorIdText() {
		Text connectorIdText = PropertyUtil.createUnboundText(section, parent, "Id");
		
		EStructuralFeature feature = ModelPackage.eINSTANCE.getConnectorType_ConnectorId();
		StringTextBinding connectorIdTextbinding = new StringTextBinding(bo, feature, connectorIdText) {
			
			@Override
			public String getModelValue() {
				ConnectorType connector = getConnector(model);
				if (connector != null) {
					return (String) connector.eGet(feature);
				}
				return null;
			}
			
			@Override
			public void setModelValue(final String value) {
				if (getConnector(model) != null || value != null && !value.isEmpty()) {
					
					TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						
						@Override
						protected void doExecute() {
							ConnectorType connector = getConnector(model);
							if (connector == null) {
								connector = ModelFactory.eINSTANCE.createConnectorType();
								ExtensionUtil.addExtension(model, ModelPackage.eINSTANCE.getDocumentRoot_Connector(), connector);
							}
							
							connector.eSet(feature, value);
							
							if (!connector.eIsSet(feature) && !connector.eIsSet(ModelPackage.eINSTANCE.getConnectorType_InputOutput())) {
								ExtensionUtil.removeExtensionByFeature(model, ModelPackage.eINSTANCE.getDocumentRoot_Connector());
								
								// remove empty extension element
								EStructuralFeature extensionValuesFeature = model.eClass().getEStructuralFeature("extensionValues");
								ExtensionAttributeValue values = ExtensionUtil.getExtensionAttributeValue(model);
								if (values != null && (values.getValue() == null || values.getValue().isEmpty())) {
									bo.eUnset(extensionValuesFeature);
								}
							}
						}
					});
				}
			}
		};
		
		connectorIdTextbinding.establish();
	}
	
	protected void createParamterTables() {
		new InputOutputPropertiesBuilder(parent, section, bo, true).create();
	}

	protected ConnectorType getConnector(EObject model) {
		List<ConnectorType> extensions = ExtensionUtil.getExtensions(model, ConnectorType.class);
		if (extensions != null && !extensions.isEmpty()) {
			return extensions.get(0);
		}
		return null;
	}
	
}
