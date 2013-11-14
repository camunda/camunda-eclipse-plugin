package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.core.utils.DocumentationUtil;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ModelTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.change.EAttributeChangeSupport;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Documentation;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * Builder for the documentation property
 * 
 * @author roman.smirnov
 * 
 */
public class DocumentationPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	private static final int BOX_HEIGHT = 16;

	private EStructuralFeature DOCUMENTATION_FEATURE = Bpmn2Package.eINSTANCE.getDocumentRoot_Documentation();
	
	private String label = "Documentation";
	
	public DocumentationPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		createAutoResizingMultiText(section, parent, bo);
	}

	private Text createAutoResizingMultiText(GFPropertySection section, final Composite parent, BaseElement bo) {
		
		Composite composite = PropertyUtil.createStandardComposite(section, parent);
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		
		final Text multiText = factory.createText(composite, "", SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL); //$NON-NLS-1$

		final FormData data = PropertyUtil.getStandardLayout();

		data.height = BOX_HEIGHT;
		multiText.setLayoutData(data);

		PropertyUtil.createLabel(section, composite, label, multiText);
		
		ISWTObservableValue multiTextObservable = SWTObservables.observeText(multiText, SWT.Modify);
		multiTextObservable.addValueChangeListener(new IValueChangeListener() {
			
			@Override
			public void handleValueChange(ValueChangeEvent event) {
				
				String text = (String) event.diff.getNewValue();
				int crCount = crCount(text);
				
				int newHeight = BOX_HEIGHT * (crCount + 1);
				if (newHeight != data.height) {
					data.height = newHeight;
					
					relayout();
				}
			}
		});

		DocumentationStringTextBinding documentationTextBinding = new DocumentationStringTextBinding(bo, multiText);
		documentationTextBinding.establish();
		
		return multiText;
	}
	
	public int crCount(String s) {
		int i = -1;
		int c = 0;
		
		do {
			i = s.indexOf(SWT.CR, i + 1);
			if (i != -1) {
				c++;
			}
			
		} while (i != -1);
		
		return c;
	}
	
	/**
	 * Binding for the documentation element
	 * 
	 * @author roman.smirnov
	 */
	private class DocumentationStringTextBinding extends ModelTextBinding<String> {

		public DocumentationStringTextBinding(EObject model, Text control) {
			super(model, control);
		}

		@Override
		protected String toString(String value) {
			if (value == null) {
				return "";
			} else {
				return value;
			}
		}

		@Override
		protected String fromString(String value) {
			if (value == null || value.trim().isEmpty()) {
				return null;
			} else {
				return value;
			}
		}

		@Override
		public String getModelValue() {
			return DocumentationUtil.getDocumentationText(bo);
		}

		@Override
		public void setModelValue(String value) {
			transactionalUpdateDocumenation(value);
		}
		
		@Override
		protected void ensureChangeSupportAdded() {
			EAttributeChangeSupport.ensureAdded(model, DOCUMENTATION_FEATURE, control);
		}
	}

	// invocation of transactional behavior ///////////////
	
	protected void transactionalUpdateDocumenation(String newValue) {
		TransactionalEditingDomain domain = getTransactionalEditingDomain();
		domain.getCommandStack().execute(new UpdateDocumentationCommand(domain, newValue));
	}

	protected TransactionalEditingDomain getTransactionalEditingDomain() {
		return TransactionUtil.getEditingDomain(bo);
	}
	
	// transactional behavior //////////////////////////////
	
	protected void updateDocumentation(String documenation) {
		Documentation documentation = Bpmn2Factory.eINSTANCE.createDocumentation();
		documentation.setText(documenation);

		DocumentationUtil.updateDocumentation(bo, documentation);
	}
	
	protected void removeDocumentation() {
		DocumentationUtil.removeDocumentation(bo);
	}
	
	// commands ///////////////////////////////////////////
	
	/**
	 * Command which takes care of updating the documentation
	 * 
	 * @author roman.smirnov
	 */
	private class UpdateDocumentationCommand extends RecordingCommand {

		private String newValue;
		
		public UpdateDocumentationCommand(TransactionalEditingDomain domain, String newValue) {
			super(domain);
			
			this.newValue = newValue;
		}

		@Override
		protected void doExecute() {

			if (newValue == null || newValue.trim().isEmpty()) {
				removeDocumentation();
			} else {
				updateDocumentation(newValue);
			}
		}
	}
	
}
