package org.camunda.bpm.modeler.ui.property.tabs.builder;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ModelRadioBinding;
import org.camunda.bpm.modeler.ui.property.tabs.dialog.ClassChooserDialog;
import org.camunda.bpm.modeler.ui.property.tabs.radio.Radio.RadioGroup;
import org.camunda.bpm.modeler.ui.property.tabs.util.Events;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EStructuralFeature;
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

/**
 * 
 * <p>Builds for a task and a throwing message event the controls to set either a class,
 * an expression or an expression delegate.</p>
 * 
 */
public class ServiceTypeControlsPropertiesBuilder extends AbstractPropertiesBuilder<BaseElement> {

	private static final String[] TYPE_NAMES = new String[] { 
		"Class", "Expression", "Expression Delegate" 
	};
	
	private static final EStructuralFeature[] TYPE_FEATURES = new EStructuralFeature[] { 
		 ModelPackage.eINSTANCE.getDocumentRoot_Class(), 
		 ModelPackage.eINSTANCE.getDocumentRoot_ActExpression(), 
		 ModelPackage.eINSTANCE.getDocumentRoot_DelegateExpression()
	};
	
	private static final EStructuralFeature CLASS_FEATURE = TYPE_FEATURES[0];
	
	private RadioGroup<EStructuralFeature> radioGroup;
	private Map<EStructuralFeature, Text> featureToInputMap; 
	
	/**
	 * Creates a new factory from the given arguments
	 * 
	 * @param parent
	 * @param section
	 * @param bo
	 */
	public ServiceTypeControlsPropertiesBuilder(Composite parent, GFPropertySection section, BaseElement bo) {
		super(parent, section, bo);
		
		this.radioGroup = new RadioGroup<EStructuralFeature>();
		this.featureToInputMap = new HashMap<EStructuralFeature, Text>(); 
	}

	/**
	 * Creates the service task specific controls
	 */
	@Override
	public void create() {
		
		for (int i = 0; i < TYPE_NAMES.length; i++) {
			String name = TYPE_NAMES[i];
			final EStructuralFeature feature = TYPE_FEATURES[i];
			
			final Text text = PropertyUtil.createRadioText(section, parent, name, feature, radioGroup, bo);
			
			featureToInputMap.put(feature, text);
			
			Button radioControl = radioGroup.getRadioControl(feature);
			
			ModelRadioBinding modelRadioBinding = new ModelRadioBinding(bo, feature, TYPE_FEATURES, radioControl) { 
				protected void activateFeature(EStructuralFeature feature) {
					bo.eSet(feature, "");
				};
			};
			
			modelRadioBinding.establish();
		}

		final Text classText = featureToInputMap.get(CLASS_FEATURE);
		
		if (ClassChooserDialog.isJdtAvailable()) {
			addBrowseClassButton(classText);
		}
	}

	private void addBrowseClassButton(final Text classText) {

		Composite classComposite = classText.getParent();
		final Button radioButton = (Button) classComposite.getChildren()[1];
		
		// monkey patching for the win!
		
		final Button btnClassSelect = new Button(classComposite, SWT.NONE);
		btnClassSelect.setText("Choose Class");
		btnClassSelect.setEnabled(radioButton.getSelection());
		
		// customize layout data for text field
		FormData textFormData = (FormData) classText.getLayoutData();
		// make space for button
		textFormData.right = new FormAttachment(100, -90);
		
		FormData btnSelectLayoutData = new FormData();
		btnSelectLayoutData.left = new FormAttachment(classText, 0);
		btnSelectLayoutData.top = new FormAttachment(classText, 0, SWT.CENTER);
		
		classText.setLayoutData(textFormData);
		btnClassSelect.setLayoutData(btnSelectLayoutData);
		
		radioButton.addListener(Events.MODEL_CHANGED, new Listener() {
			
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
}
