package org.camunda.bpm.modeler.ui.property.tabs.builder;

import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.property.tabs.binding.BooleanButtonBinding;
import org.camunda.bpm.modeler.ui.property.tabs.util.HelpText;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.camunda.bpm.modeler.ui.util.Browser;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * 
 * @author nico.rehwaldt
 *
 */
public class ParallelGatewayPropertiesBuilder extends RetryEnabledPropertiesBuilder {

	private static final EStructuralFeature ASYNC_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_Async();
	private static final EStructuralFeature EXCLUSIVE_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_Exclusive();
	
	public ParallelGatewayPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		
		boolean async = (Boolean) bo.eGet(ASYNC_FEATURE);
		boolean exclusive = (Boolean) bo.eGet(EXCLUSIVE_FEATURE);

		// <async-link> <!-- Async link and help text -->
		
		final Button checkbox = createCheckbox("Asynchronous", HelpText.ASYNC_FLAG);

		// </async-link>

		// exclusive jobs flag, default value 'true'
		final Button exclusiveCheckbox = createCheckbox("Exclusive", HelpText.EXCLUSIVE_FLAG);
		new BooleanButtonBinding(bo, EXCLUSIVE_FEATURE, exclusiveCheckbox).establish();
		exclusiveCheckbox.setSelection(exclusive);

		final Text retryText = createRetryCycleText();

		new AsyncFlagButtonBinding(bo, ASYNC_FEATURE, checkbox).establish();

		// initial setup of GUI elements
		retryText.setEnabled(async);
		exclusiveCheckbox.setEnabled(async);

		checkbox.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				boolean selected = checkbox.getSelection();
				retryText.setEnabled(selected);
				exclusiveCheckbox.setEnabled(selected);
			}
		});

	}
	
	/**
	 * 
	 * @return
	 */
	private Button createCheckbox(String label, String helpText) {
		
		Composite composite = PropertyUtil.createStandardComposite(section, parent);
		
		Composite checkBoxComposite = PropertyUtil.createStandardComposite(section, composite);
		checkBoxComposite.setLayoutData(PropertyUtil.getStandardLayout());

		// add label
		PropertyUtil.createLabel(section, composite, label, checkBoxComposite);
		
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Button checkbox = factory.createButton(checkBoxComposite, "", SWT.CHECK);
		
		FormData checkboxFormData = new FormData();
		checkboxFormData.top = new FormAttachment(0);
		checkboxFormData.left = new FormAttachment(0);
		
		checkbox.setLayoutData(checkboxFormData);
		
		Link link = new Link(checkBoxComposite, SWT.NO_BACKGROUND | SWT.NO_FOCUS);
		link.setText(helpText);
		
		factory.adapt(link, false, false);
		
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Browser.open(e.text);
			}
		});
		
		FormData helpTextFormData = new FormData();
		helpTextFormData.left = new FormAttachment(checkbox, 0);
		helpTextFormData.right = new FormAttachment(100, 0);
		
		link.setLayoutData(helpTextFormData);
		
		return checkbox;
	}

	private class AsyncFlagButtonBinding extends BooleanButtonBinding {

		public AsyncFlagButtonBinding(EObject model, EStructuralFeature feature, Button control) {
			super(model, feature, control);
		}
		
		@Override
		public Boolean getModelValue() {
			return (Boolean) bo.eGet(ASYNC_FEATURE);
		}

		@Override
		public void setModelValue(Boolean value) {
			transactionalToggleAsync(value);
		}
	}
	
	// invocation of transactional behavior ///////////////
	
	private void transactionalToggleAsync(boolean async) {
		TransactionalEditingDomain domain = getTransactionalEditingDomain();
		domain.getCommandStack().execute(new ToggleAsyncCommand(domain, async));
	}

	/**
	 * Command which takes of toggling the async flag of the managed 
	 * business object. That may lead to the deletion of the
	 * retry time cycle, too.
	 * 
	 * @author nico.rehwaldt
	 */
	private class ToggleAsyncCommand extends RecordingCommand {
		
		private boolean async;

		public ToggleAsyncCommand(TransactionalEditingDomain domain, boolean async) {
			super(domain);
			
			this.async = async;
		}

		@Override
		protected void doExecute() {
			if (!async) {
				bo.eUnset(ASYNC_FEATURE);
				removeRetryCycle();
				bo.eUnset(EXCLUSIVE_FEATURE);
			} else {
				bo.eSet(ASYNC_FEATURE, async);
			}
		}
	}
}
