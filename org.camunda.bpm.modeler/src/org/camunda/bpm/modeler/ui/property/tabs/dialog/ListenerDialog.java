package org.camunda.bpm.modeler.ui.property.tabs.dialog;

import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ScriptType;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ValidatingStringComboBinding;
import org.camunda.bpm.modeler.ui.property.tabs.builder.CamundaScriptPropertiesBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class ListenerDialog<T extends EObject> extends ModelerDialog {

	protected static final int SHELL_WIDTH = 660;
	protected static final int SHELL_HEIGHT = 400;
	
	protected static final String EMPTY_STRING = "";
	
	protected static final String CHOOSE_CLASS_LABEL = "Choose Class";
	
	protected static final String LISTENER_EVENT_LABEL = "Event";
	protected static final String LISTENER_TYPE_LABEL = "Type";
	
	protected static final String LISTENER_TYPE_CLASS_LABEL = "Class";
	protected static final String LISTENER_TYPE_DELEGATE_EXPRESSION_LABEL = "Delegate Expression";
	protected static final String LISTENER_TYPE_EXPRESSION_LABEL = "Expression";
	protected static final String LISTENER_TYPE_SCRIPT_LABEL = "Script";
	
	protected GFPropertySection section;
	
	protected BaseElement bo;
	protected T listener;
	
	/* controls */
	protected Composite contentComposite;
	protected Composite placeHolderComposite;
	
	protected ListenerDialog(Shell parentShell, EObject model) {
		super(parentShell, model);
	}

	public ListenerDialog(GFPropertySection section, Shell parentShell, BaseElement bo, T listener) {
		super(parentShell, listener);
		
		this.section = section;
		this.bo = bo;
		this.listener = listener;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
	   super.configureShell(newShell);
	   
	   newShell.setText(getDialogLabel());

	   newShell.setSize(SHELL_WIDTH, SHELL_HEIGHT);
	   
	   Point shellSize = newShell.getSize();
	   Rectangle screen = newShell.getMonitor().getBounds();
	   
	   int x = ((screen.width - shellSize.x) / 2) + screen.x;
	   int y = ((screen.height - shellSize.y) / 2) + screen.y - 100;
	   
	   if (y <= 0) {
		   y = 50 + screen.y;
	   }
	   
	   newShell.setLocation(x, y);
	}
	
	protected abstract String getDialogLabel();
	
	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.TITLE | SWT.APPLICATION_MODAL);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		// create CANCEL
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		// compute shell size at the end of the dialog creation
		computeSize();
	}
	
	protected void computeSize() {
		Point size = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		getShell().setSize(SHELL_WIDTH, size.y);
	}
	
	protected void relayout(Composite parent) {
		computeSize();

		parent.layout();
		parent.redraw();
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		createEventDropDown(parent);
		createTypeDropDown(parent);

		placeHolderComposite = PropertyUtil.createGridLayoutedComposite(section, parent);
		refreshContent(parent);
		
		return parent;
	}
	
	protected void createEventDropDown(Composite parent) {
		CCombo dropDown = PropertyUtil.createDropDown(section, parent, LISTENER_EVENT_LABEL);
		configureEventDropDown(parent, dropDown, listener);
	}
	
	protected abstract void configureEventDropDown(Composite parent, CCombo dropDown, T listener);
	
	protected void createTypeDropDown(Composite parent) {
		CCombo dropDown = PropertyUtil.createDropDown(section, parent, LISTENER_TYPE_LABEL);
		configureTypeDropDown(parent, dropDown, listener);
	}

	protected void configureTypeDropDown(final Composite parent, CCombo dropDown, T listener) {
		dropDown.add(LISTENER_TYPE_CLASS_LABEL);
		dropDown.add(LISTENER_TYPE_DELEGATE_EXPRESSION_LABEL);
		dropDown.add(LISTENER_TYPE_EXPRESSION_LABEL);
		dropDown.add(LISTENER_TYPE_SCRIPT_LABEL);
		
		ValidatingStringComboBinding typeBinding = new ValidatingStringComboBinding(listener, getListenerFeature(), dropDown) {
			
			@Override
			public String getModelValue() {
				if (model.eIsSet(getListenerClassFeature())) {
					return LISTENER_TYPE_CLASS_LABEL;
					
				} else if (model.eIsSet(getListenerDelegateExpressionFeature())) {
					return LISTENER_TYPE_DELEGATE_EXPRESSION_LABEL;
					
				} else if (model.eIsSet(getListenerExpressionFeature())) {
					return LISTENER_TYPE_EXPRESSION_LABEL;
					
				} else if (model.eIsSet(getListenerScriptFeature())){
					return LISTENER_TYPE_SCRIPT_LABEL;
				}

				return LISTENER_TYPE_CLASS_LABEL;
			}
			
			@Override
			public void setModelValue(final String value) {
				TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
					
					@Override
					protected void doExecute() {
						model.eUnset(getListenerClassFeature());
						model.eUnset(getListenerDelegateExpressionFeature());
						model.eUnset(getListenerExpressionFeature());
						model.eUnset(getListenerScriptFeature());
						
						if (LISTENER_TYPE_CLASS_LABEL.equals(value)) {
							model.eSet(getListenerClassFeature(), EMPTY_STRING);
							
						} else if (LISTENER_TYPE_DELEGATE_EXPRESSION_LABEL.equals(value)) {
							model.eSet(getListenerDelegateExpressionFeature(), EMPTY_STRING);
							
						} else if (LISTENER_TYPE_EXPRESSION_LABEL.equals(value)) {
							model.eSet(getListenerExpressionFeature(), EMPTY_STRING);
							
						} else if (LISTENER_TYPE_SCRIPT_LABEL.equals(value)) {
							ScriptType script = ModelFactory.eINSTANCE.createScriptType();
							model.eSet(getListenerScriptFeature(), script);
						}
					}
				});
			}
		};
		
		typeBinding.establish();
		
		dropDown.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				refreshContent(parent);
			}
		});
	}
	
	protected void refreshContent(Composite parent) {
		if (contentComposite != null && !contentComposite.isDisposed()) {
			contentComposite.dispose();
		}

		contentComposite = PropertyUtil.createGridLayoutedComposite(section, placeHolderComposite);
		
		if (listener.eIsSet(getListenerClassFeature())) {
			createClass(section, contentComposite, listener);
			
		} else if (listener.eIsSet(getListenerDelegateExpressionFeature())) {
			createDelegateExpression(section, contentComposite, listener);

		} else if (listener.eIsSet(getListenerExpressionFeature())) {
			createExpression(section, contentComposite, listener);
			
		} else if (listener.eIsSet(getListenerScriptFeature())){
			createScript(section, contentComposite, listener);

		} else {
			createClass(section, contentComposite, listener);
		}
		
		placeHolderComposite.layout();

		relayout(parent);
	}
	
	protected void createClass(GFPropertySection section, Composite parent, T listener) {
		Text classText = PropertyUtil.createText(section, parent, LISTENER_TYPE_CLASS_LABEL, getListenerClassFeature(), listener);
		
		if (ClassChooserDialog.isJdtAvailable()) {
			addBrowseClassButton(classText);
		}
	}
	
	protected void addBrowseClassButton(final Text classText) {

		Composite classComposite = classText.getParent();
		
		// monkey patching for the win!
		
		Button btnClassSelect = new Button(classComposite, SWT.NONE);
		btnClassSelect.setText(CHOOSE_CLASS_LABEL);
		
		// customize layout data for text field
		FormData textFormData = (FormData) classText.getLayoutData();
		// make space for button
		textFormData.right = new FormAttachment(100, -90);
		
		FormData btnSelectLayoutData = new FormData();
		btnSelectLayoutData.left = new FormAttachment(classText, 0);
		btnSelectLayoutData.top = new FormAttachment(classText, 0, SWT.CENTER);
		
		classText.setLayoutData(textFormData);
		btnClassSelect.setLayoutData(btnSelectLayoutData);
		
		btnClassSelect.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				Shell shell = getShell();

				Composite parent = shell.getParent();
				if (parent instanceof Shell) {
					shell = (Shell) parent;
				}
				
				ClassChooserDialog dialog = new ClassChooserDialog(shell);
				
				// interrupting operation
				String clsName = dialog.chooseClass();
				
				if (clsName != null) {
					classText.setText(clsName);
				}
			}
		});
	}
	
	protected void createExpression(GFPropertySection section, Composite parent, T listener) {
		PropertyUtil.createText(section, parent, LISTENER_TYPE_EXPRESSION_LABEL, getListenerExpressionFeature(), listener);
	}
	
	protected void createDelegateExpression(GFPropertySection section, Composite parent, T listener) {
		PropertyUtil.createText(section, parent, LISTENER_TYPE_DELEGATE_EXPRESSION_LABEL, getListenerDelegateExpressionFeature(), listener);
	}
	
	protected void createScript(GFPropertySection section, Composite parent, T listener) {
		new CamundaScriptPropertiesBuilder(parent, section, bo, listener, getListenerScriptFeature()).create();
	}
	
	protected abstract EStructuralFeature getListenerFeature();
	
	protected abstract EStructuralFeature getListenerClassFeature();
	
	protected abstract EStructuralFeature getListenerDelegateExpressionFeature();
	
	protected abstract EStructuralFeature getListenerExpressionFeature();
	
	protected abstract EStructuralFeature getListenerScriptFeature();
	
}
