package org.eclipse.bpmn2.modeler.ui.property.artifact;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.modeler.core.adapters.AdapterUtil;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultDetailComposite.AbstractPropertiesProvider;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
/**
 * 
 * @author hien quoc dang
 *
 */
public class TextAnnotationDetailComposite extends DefaultDetailComposite {
	
	public TextAnnotationDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	public TextAnnotationDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}
	
	@Override
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (propertiesProvider==null) {
			propertiesProvider = new AbstractPropertiesProvider(object) {
				String[] properties = new String[] {
						"textFormat",
						"text",
				};
				
				@Override
				public String[] getProperties() {
					return properties; 
				}
			};
		}
		return propertiesProvider;
	}

	@Override
	public void createBindings(EObject be) {
		ExtendedPropertiesAdapter adapter = (ExtendedPropertiesAdapter) AdapterUtil.adapt(be, ExtendedPropertiesAdapter.class);
		adapter.getFeatureDescriptor(Bpmn2Package.eINSTANCE.getTextAnnotation_Text()).setMultiLine(true);
		super.createBindings(be);
	}
}
