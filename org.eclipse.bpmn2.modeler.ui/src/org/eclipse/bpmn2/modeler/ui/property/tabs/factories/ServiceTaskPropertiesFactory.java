package org.eclipse.bpmn2.modeler.ui.property.tabs.factories;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpmn2.modeler.runtime.activiti.model.ModelPackage;
import org.eclipse.bpmn2.modeler.ui.property.tabs.swt.Radio;
import org.eclipse.bpmn2.modeler.ui.property.tabs.swt.Radio.RadioGroup;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.ClassChooserDialog;
import org.eclipse.bpmn2.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ServiceTaskPropertiesFactory extends PropertiesFactory {

	private RadioGroup<EStructuralFeature> radioGroup;
	private Map<EStructuralFeature, Text> featureToInputMap; 
	
	public ServiceTaskPropertiesFactory(Composite parent, GFPropertySection section, EObject bo) {
		super(parent, section, bo);
		
		this.radioGroup = new RadioGroup<EStructuralFeature>();
		this.featureToInputMap = new HashMap<EStructuralFeature, Text>(); 
	}

	@Override
	public void create() {
		PropertyUtil.createText(section, parent, "Result Variable", ModelPackage.eINSTANCE.getDocumentRoot_ResultVariableName(), bo);
		
		createServiceTaskTypeControls();
	}

	
	private void createServiceTaskTypeControls() {
		
		String[] names = new String[] { "Class", "Expression", "Expression Delegate" };
		EStructuralFeature[] features = new EStructuralFeature[] { 
			 ModelPackage.eINSTANCE.getDocumentRoot_Class(), 
			 ModelPackage.eINSTANCE.getDocumentRoot_ActExpression(), 
			 ModelPackage.eINSTANCE.getDocumentRoot_DelegateExpression()
		};
		
		EStructuralFeature selected = null;
		
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			EStructuralFeature feature = features[i];
			
			Text text = PropertyUtil.createRadioText(section, parent, name, feature, radioGroup, bo);
			featureToInputMap.put(feature, text);
			
			if (bo.eIsSet(feature)) {
				selected = feature;
			}
		}
		
		Text classText = featureToInputMap.get(features[0]);
		addBrowseClassButton(classText);
		
		radioGroup.addListener(Radio.SELECTION_CHANGED, new Radio.RadioSelectionAdapter<EStructuralFeature>() {
			
			@Override
			public void radioSelectionChanged(Radio.SelectionChangedEvent<EStructuralFeature> event) {
				EStructuralFeature oldType = event.getOldSelection();
				
				Text oldText = featureToInputMap.get(oldType);
				oldText.setText("");
				
				transactionalHandleTypeChange(oldType, event.getNewSelection());
			}
		});
		
		radioGroup.select(selected, true);
	}

	private void addBrowseClassButton(final Text classText) {

		Composite classComposite = classText.getParent();
		final Button radioButton = (Button) classComposite.getChildren()[1];
		
		final Button btnClassSelect = new Button(classComposite, SWT.NONE);
		btnClassSelect.setText("Choose Class");
		btnClassSelect.setEnabled(radioButton.getSelection());
		
		// override layout data for text field
		FormData textFormData = new FormData();
		textFormData.left = new FormAttachment(0, 15);
		textFormData.right = new FormAttachment(100, -90);
		
		FormData btnSelectLayoutData = new FormData();
		btnSelectLayoutData.left = new FormAttachment(classText, 0);
		btnSelectLayoutData.top = new FormAttachment(classText, 0, SWT.CENTER);
		
		classText.setLayoutData(textFormData);
		btnClassSelect.setLayoutData(btnSelectLayoutData);
		
		radioButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				boolean selected = radioButton.getSelection();
				btnClassSelect.setEnabled(selected);
			}
		});
		
		btnClassSelect.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				Shell shell = classText.getShell();
				ClassChooserDialog dialog = new ClassChooserDialog(shell);
				
				// interrupting operation
				String clsName = dialog.chooseClass();
				
				if (clsName != null) {
					classText.setText(clsName);
				}
			}
		});
	}

	protected void transactionalHandleTypeChange(EStructuralFeature oldType, EStructuralFeature newType) {
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(bo);
		domain.getCommandStack().execute(new ToggleFeaturesCommand(domain, bo, oldType, newType));
	}

	// transactional behavior /////////////////////////////
	
	private class ToggleFeaturesCommand extends RecordingCommand {
		
		private EObject object;
		
		private EStructuralFeature oldFeature;
		private EStructuralFeature newFeature;
		
		public ToggleFeaturesCommand(TransactionalEditingDomain domain, EObject object, EStructuralFeature oldFeature, EStructuralFeature newFeature) {
			super(domain);
			
			this.object = object;
			
			this.oldFeature = oldFeature;
			this.newFeature = newFeature;
		}
		
		@Override
		protected void doExecute() {
			object.eUnset(oldFeature);
			object.eSet(newFeature, "");
		}
	}
}
