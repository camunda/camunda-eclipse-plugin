package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import org.eclipse.bpmn2.ItemDefinition;
import org.eclipse.bpmn2.ItemKind;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultDetailComposite.AbstractPropertiesProvider;
import org.eclipse.bpmn2.modeler.ui.property.editors.ComboObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.ObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.SchemaObjectEditor;
import org.eclipse.bpmn2.modeler.ui.property.editors.TextObjectEditor;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class ItemDefinitionDetailComposite extends DefaultDetailComposite {

	public ItemDefinitionDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param section
	 */
	public ItemDefinitionDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	@Override
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (propertiesProvider==null) {
			propertiesProvider = new AbstractPropertiesProvider(object) {
				String[] properties = new String[] {
						"itemKind",
						"isCollection",
						"structureRef",
						// this thing is transient so it won't be serialized; no point in allowing user to set it
						// "import"
				};
				
				@Override
				public String[] getProperties() {
					return properties; 
				}
			};
		}
		return propertiesProvider;
	}
	
	protected void bindAttribute(Composite parent, EObject object, EAttribute attribute, String label) {

		if ("itemKind".equals(attribute.getName())) {
			if (modelEnablement.isEnabled(object.eClass(), attribute)) {

				if (parent==null)
					parent = getAttributesParent();
				
				if (label==null)
					label = PropertyUtil.getLabel(object, attribute);
				
				ObjectEditor editor = new ComboObjectEditor(this,object,attribute) {
					protected boolean updateObject(final Object result) {
						super.updateObject(result);
						Display.getCurrent().syncExec( new Runnable() {
							@Override
							public void run() {
								setBusinessObject(getBusinessObject());
							}
						});
						return true;
					}
				};
				
				editor.createControl(parent,label);
			}
		}
		else
			super.bindAttribute(parent, object, attribute, label);
	}
	
	@Override
	protected void bindReference(Composite parent, EObject object, EReference reference) {
		if ("structureRef".equals(reference.getName()) &&
				modelEnablement.isEnabled(object.eClass(), reference)) {
			
			if (parent==null)
				parent = getAttributesParent();
			
			final ItemDefinition def = (ItemDefinition)object;
			String displayName = PropertyUtil.getLabel(object, reference);
			
			if (def.getItemKind().equals(ItemKind.INFORMATION)) {
				SchemaObjectEditor editor = new SchemaObjectEditor(this,object,reference);
				editor.createControl(parent,displayName);
			}
			else {
				ObjectEditor editor = new TextObjectEditor(this,object,reference) {
					@Override
					protected boolean updateObject(Object result) {
						return super.updateObject(ModelUtil.createStringWrapper((String)result));
					}
				};
				editor.createControl(parent,displayName);
			}
		}
		else
			super.bindReference(parent, object, reference);
	}
}