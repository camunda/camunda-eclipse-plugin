package org.camunda.bpm.modeler.ui.property.tabs.binding;

import java.util.List;

import org.camunda.bpm.modeler.core.utils.ExtensionUtil;
import org.camunda.bpm.modeler.runtime.engine.model.FieldType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.change.filter.ExtensionChangeFilter;
import org.camunda.bpm.modeler.ui.change.filter.FeatureChangeFilter;
import org.camunda.bpm.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.widgets.Text;

public class FieldInjectionTextBinding extends ModelTextBinding<String> {

	protected EStructuralFeature FIELD_ATTR = ModelPackage.eINSTANCE.getDocumentRoot_Field();

	/**
	 * The binding to be used when adding new form fields
	 * 
	 * @author nico.rehwaldt
	 */
	public static enum Binding {
		INLINE_STRING(ModelPackage.eINSTANCE.getFieldType_StringValue()),
		STRING(ModelPackage.eINSTANCE.getFieldType_String()),
		EXPRESSION(ModelPackage.eINSTANCE.getFieldType_Expression()),
		INLINE_EXPRESSION(ModelPackage.eINSTANCE.getFieldType_Expression1());
		
		private EStructuralFeature feature;
		
		private Binding(EStructuralFeature feature) {
			this.feature = feature;
		}
		
		public EStructuralFeature getFeature() {
			return feature;
		}
	}
	
	private String fieldName;

	private EStructuralFeature fieldBinding;
	
	public FieldInjectionTextBinding(EObject model, String fieldName, Binding binding, Text control) {
		super(model, control);
		
		this.fieldName = fieldName;
		this.fieldBinding = binding.getFeature();
	}

	@Override
	protected String toString(String value) {
		return value == null ? "" : value;
	}

	@Override
	protected String fromString(String value) {
		return value != null && !value.isEmpty() ? value : null;
	}

	@Override
	protected void ensureChangeSupportAdded() {
		EAttributeChangeSupport changeSupport = new EAttributeChangeSupport(model, FIELD_ATTR, control);
		changeSupport.setFilter(new ExtensionChangeFilter(model, FIELD_ATTR).or(new FeatureChangeFilter(model, FIELD_ATTR)));
		
		EAttributeChangeSupport.ensureAdded(changeSupport, control);
	}

	@Override
	public String getModelValue() {
		
		FieldType field = getFieldType();
		
		if (field != null) {
			Object result = field.eGet(fieldBinding);
			if (result != null) {
				return result.toString();
			}
		}
		
		return "";
	}

	@Override
	public void setModelValue(final String value) {
		
		final boolean create = value != null;
		
		transactionalExcecute(new Runnable() {
			
			@Override
			public void run() {
				FieldType field = getFieldType();
				
				if (create) {
					if (field != null) {
						field.eSet(fieldBinding, value);
					} else {
						field = ModelFactory.eINSTANCE.createFieldType();
						
						field.setName(fieldName);
						field.eSet(fieldBinding, value);
						
						ExtensionUtil.addExtension(model, FIELD_ATTR, field);
					}
				} else {
					ExtensionUtil.removeExtensionByValue(model, field);
				}
			}
		});
	}

	private void transactionalExcecute(final Runnable runnable) {
		TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
			
			@Override
			protected void doExecute() {
				runnable.run();
			}
		});
	}

	protected FieldType getFieldType() {
		List<FieldType> extensions = ExtensionUtil.getExtensions(model, FieldType.class);

		for (FieldType field : extensions) {
			
			if (fieldName.equals(field.getName())) {
				return field;
			}
		}
		
		return null;
	}

}
