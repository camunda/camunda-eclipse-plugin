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
public class ActivityPropertiesBuilder extends RetryEnabledPropertiesBuilder {

	protected static final String ASYNC_BEFORE_LABEL = "Asynchronous Before";
	protected static final String ASYNC_AFTER_LABEL = "Asynchronous After";
	protected static final String EXCLUSIVE_LABEL = "Exclusive";
	
	private static final EStructuralFeature ASYNC_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_Async();
	private static final EStructuralFeature ASYNC_BEFORE_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_AsyncBefore();
	private static final EStructuralFeature ASYNC_AFTER_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_AsyncAfter();
	private static final EStructuralFeature EXCLUSIVE_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_Exclusive();
	
	public ActivityPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
	}

	@Override
	public void create() {
		
		EStructuralFeature asyncBeforeFeature = ASYNC_FEATURE;
		if (bo.eIsSet(ASYNC_BEFORE_FEATURE) && !bo.eIsSet(ASYNC_FEATURE)) {
			asyncBeforeFeature = ASYNC_BEFORE_FEATURE;
		}
		
		boolean asyncBefore = (Boolean) bo.eGet(asyncBeforeFeature);
		boolean asyncAfter = (Boolean) bo.eGet(ASYNC_AFTER_FEATURE);
		boolean exclusive = (Boolean) bo.eGet(EXCLUSIVE_FEATURE);

		// <async-link> <!-- Async link and help text -->
		
		final Button asyncBeforeCheckbox = createCheckbox(ASYNC_BEFORE_LABEL, HelpText.ASYNC_FLAG);
		new AsyncFlagButtonBinding(bo, asyncBeforeFeature, asyncBeforeCheckbox).establish();
		
		final Button asyncAfterCheckbox = createCheckbox(ASYNC_AFTER_LABEL, HelpText.ASYNC_FLAG, HelpText.SUPPORTED_VERSION_NOTE_7_2);
		new AsyncFlagButtonBinding(bo, ASYNC_AFTER_FEATURE, asyncAfterCheckbox).establish();
		
		// </async-link>

		// exclusive jobs flag, default value 'true'
		final Button exclusiveCheckbox = createCheckbox(EXCLUSIVE_LABEL, HelpText.EXCLUSIVE_FLAG);
		new BooleanButtonBinding(bo, EXCLUSIVE_FEATURE, exclusiveCheckbox).establish();
		exclusiveCheckbox.setSelection(exclusive);

		final Text retryText = createRetryCycleText();

		// initial setup of GUI elements
		boolean enable = asyncBefore || asyncAfter;
		exclusiveCheckbox.setEnabled(enable);
		retryText.setEnabled(enable);

		asyncBeforeCheckbox.addListener(SWT.Selection, new AsyncCheckboxListener(asyncBeforeCheckbox, asyncAfterCheckbox, exclusiveCheckbox, retryText));
		asyncAfterCheckbox.addListener(SWT.Selection, new AsyncCheckboxListener(asyncBeforeCheckbox, asyncAfterCheckbox, exclusiveCheckbox, retryText));
	}
	
	/**
	 * 
	 * @return
	 */
	private Button createCheckbox(String label, String helpText) {
		return createCheckbox(label, helpText, null);
	}
		
	private Button createCheckbox(String label, String helpText, String note) {
		
		Composite composite = PropertyUtil.createStandardComposite(section, parent);
		
		Composite checkBoxComposite = PropertyUtil.createStandardComposite(section, composite);
		checkBoxComposite.setLayoutData(PropertyUtil.getStandardLayout());
		
		if (note != null && !note.isEmpty()) {
			PropertyUtil.attachNote(checkBoxComposite, note);
		}

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
			return (Boolean) bo.eGet(feature);
		}

		@Override
		public void setModelValue(Boolean value) {
			transactionalToggleAsync(feature, value);
		}
	}
	
	// invocation of transactional behavior ///////////////
	
	private void transactionalToggleAsync(EStructuralFeature feature, boolean async) {
		TransactionalEditingDomain domain = getTransactionalEditingDomain();
		domain.getCommandStack().execute(new ToggleAsyncCommand(domain, feature, async));
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
		private EStructuralFeature feature;

		public ToggleAsyncCommand(TransactionalEditingDomain domain, EStructuralFeature feature, boolean async) {
			super(domain);
			
			this.feature = feature;
			this.async = async;
		}

		@Override
		protected void doExecute() {
			if (!async) {
				
				if (ASYNC_FEATURE.equals(feature) || ASYNC_BEFORE_FEATURE.equals(feature)) {
					bo.eUnset(ASYNC_FEATURE);
					bo.eUnset(ASYNC_BEFORE_FEATURE);
				} else {
					bo.eUnset(feature);
				}
				
				if (!bo.eIsSet(ASYNC_FEATURE) && !bo.eIsSet(ASYNC_BEFORE_FEATURE) && !bo.eIsSet(ASYNC_AFTER_FEATURE)) {
					removeRetryCycle();
					bo.eUnset(EXCLUSIVE_FEATURE);
				}
				
			} else {
				bo.eSet(feature, async);
			}
		}
	}
	
	private class AsyncCheckboxListener implements Listener {

		protected Button asyncBeforeCheckbox;
		protected Button asyncAfterCheckbox;
		protected Button exclusiveCheckbox;
		protected Text retryText;
		
		public AsyncCheckboxListener(Button asyncBeforeCheckbox, Button asyncAfterCheckbox, Button exclusiveCheckbox, Text retryText) {
			this.asyncBeforeCheckbox = asyncBeforeCheckbox;
			this.asyncAfterCheckbox = asyncAfterCheckbox;
			this.exclusiveCheckbox = exclusiveCheckbox;
			this.retryText = retryText;
		}
		
		@Override
		public void handleEvent(Event event) {
			boolean beforeSelected = asyncBeforeCheckbox.getSelection();
			boolean afterSelected = asyncAfterCheckbox.getSelection();
			
			boolean enable = beforeSelected || afterSelected;
			
			if (exclusiveCheckbox != null) {
				exclusiveCheckbox.setEnabled(enable);
			}

			if (retryText != null) {
				retryText.setEnabled(enable);
			}
		}
	}
}
