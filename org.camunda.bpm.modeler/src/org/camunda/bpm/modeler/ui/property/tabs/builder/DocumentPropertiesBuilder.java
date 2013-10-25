package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for document properties from the definitions
 * 
 * @author kristin.polenz
 */
public class DocumentPropertiesBuilder extends AbstractPropertiesBuilder<Definitions> {

	private static final EStructuralFeature TARGET_NAMESPACE_FEATURE = Bpmn2Package.eINSTANCE.getDefinitions_TargetNamespace();
	
	public DocumentPropertiesBuilder(Composite parent, GFPropertySection section, Definitions bo) {
		super(parent, section, bo);
	}
	
	@Override
	public void create() {
		PropertyUtil.createText(section, parent, "Target Namespace", TARGET_NAMESPACE_FEATURE, bo);
//		final Text text = PropertyUtil.createUnboundText(section, parent, "Target Namespace");
//		new TargetNamespaceStringTextBinding(bo, TARGET_NAMESPACE_FEATURE, text).establish();
	}
	
//	/**
//	 * Binding for the target namespace element
//	 * 
//	 * @author kristin.polenz
//	 */
//	private class TargetNamespaceStringTextBinding extends StringTextBinding {
//
//		public TargetNamespaceStringTextBinding(EObject model,EStructuralFeature feature, Text control) {
//			super(model, feature, control);
//		}
//
//		@Override
//		protected String toString(String value) {
//			if (value == null) {
//				return "";
//			} else {
//				return value;
//			}
//		}
//
//		@Override
//		protected String fromString(String str) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public String getModelValue() {
//			return (String) model.eContainer().eGet(feature);
//		}
//	}
}
