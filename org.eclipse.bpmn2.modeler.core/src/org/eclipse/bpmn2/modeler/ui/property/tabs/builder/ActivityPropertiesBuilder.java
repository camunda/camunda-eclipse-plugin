package org.eclipse.bpmn2.modeler.ui.property.tabs.builder;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.tabs.binding.BooleanButtonBinding;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.HelpText;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.modeler.ui.util.Browser;
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
public class ActivityPropertiesBuilder extends RetryEnabledPropertiesBuilder {

	private static final EStructuralFeature ASYNC_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_Async();
	
	public ActivityPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		
		boolean async = (Boolean) bo.eGet(ASYNC_FEATURE);

		// <async-link> <!-- Async link and help text -->
		
		final Button checkbox = createAsyncCheckbox();
		
		// </async-link>
		
		final Text retryText = createRetryCycleText();
		
		new AsyncFlagButtonBinding(bo, ASYNC_FEATURE, checkbox).establish();
		
		// initial setup of GUI elements
		retryText.setEnabled(async);

		checkbox.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				boolean selected = checkbox.getSelection();
				retryText.setEnabled(selected);
			}
		});
	}
	
	/**
	 * 
	 * @return
	 */
	private Button createAsyncCheckbox() {
		
		Composite composite = PropertyUtil.createStandardComposite(section, parent);
		
		Composite asyncComposite = PropertyUtil.createStandardComposite(section, composite);
		asyncComposite.setLayoutData(PropertyUtil.getStandardLayout());

		// add label
		PropertyUtil.createLabel(section, composite, "Asynchronous", asyncComposite);
		
		TabbedPropertySheetWidgetFactory factory = section.getWidgetFactory();
		final Button checkbox = factory.createButton(asyncComposite, "", SWT.CHECK);
		
		FormData checkboxFormData = new FormData();
		checkboxFormData.top = new FormAttachment(0);
		checkboxFormData.left = new FormAttachment(0);
		
		checkbox.setLayoutData(checkboxFormData);
		
		Link link = new Link(asyncComposite, SWT.NO_BACKGROUND | SWT.NO_FOCUS);
		link.setText(HelpText.ASYNC_FLAG);
		
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
			} else {
				bo.eSet(ASYNC_FEATURE, async);
			}
		}
	}
}
