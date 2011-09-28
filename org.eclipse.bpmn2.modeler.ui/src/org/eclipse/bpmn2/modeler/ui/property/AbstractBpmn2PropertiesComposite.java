/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 * IBM Corporation - http://dev.eclipse.org/viewcvs/viewvc.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet19.java
 *
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.GatewayDirection;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.provider.BpmnDiItemProviderAdapterFactory;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.preferences.ToolEnablementPreferences;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.dd.dc.provider.DcItemProviderAdapterFactory;
import org.eclipse.dd.di.provider.DiItemProviderAdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;


public abstract class AbstractBpmn2PropertiesComposite extends Composite {

	public final static ComposedAdapterFactory ADAPTER_FACTORY;
	public static final Bpmn2Factory MODEL_FACTORY = Bpmn2Factory.eINSTANCE;
	
	protected TabbedPropertySheetPage tabbedPropertySheetPage;
	protected EObject be;
	protected BPMN2Editor bpmn2Editor;
	protected final TrackingFormToolkit toolkit = new TrackingFormToolkit(Display.getCurrent());
	protected IProject project;
	protected ToolEnablementPreferences preferences;
	protected ItemProviderAdapter itemProviderAdapter;
	protected final AdapterFactoryLabelProvider LABEL_PROVIDER = new AdapterFactoryLabelProvider(ADAPTER_FACTORY);
	protected ModelHandler modelHandler;
	protected BPMNShape shape;
	
	private Section attributesSection = null;
	private Composite attributesComposite = null;
	private Section referencesSection = null;
	private Composite referencesComposite = null;
	

	static {
		ADAPTER_FACTORY = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		ADAPTER_FACTORY.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new Bpmn2ItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new BpmnDiItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new DiItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new DcItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
	}

	/**
	 * NB! Must call setEObject for updating contents and rebuild the UI.
	 * 
	 * @param parent
	 * @param style
	 */
	public AbstractBpmn2PropertiesComposite(Composite parent, int style) {
		super(parent, style);
//		this.parent = parent;
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(3, false));
	}
	
	public TrackingFormToolkit getToolkit() {
		return toolkit;
	}
	
	public void setSheetPage(TabbedPropertySheetPage tabbedPropertySheetPage) {
		this.tabbedPropertySheetPage = tabbedPropertySheetPage;
	}
	
	public TabbedPropertySheetPage getSheetPage() {
		return tabbedPropertySheetPage;
	}
	
	public final void setEObject(BPMN2Editor bpmn2Editor, final EObject object) {
		String projectName = bpmn2Editor.getDiagramTypeProvider().getDiagram().eResource().getURI().segment(1);
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		preferences = ToolEnablementPreferences.getPreferences(project);
		setDiagramEditor(bpmn2Editor);
		setEObject(object);
	}
	
	public final EObject getEObject() {
		return be;
	}

	protected final void setEObject(final EObject object) {
		if (be==object)
			return;
		cleanBindings();
		be = object;
		if (be != null) {
			setBusinessObject(object);
			itemProviderAdapter = (ItemProviderAdapter) new Bpmn2ItemProviderAdapterFactory().adapt(
					be, ItemProviderAdapter.class);
			createBindings(be);
		}
		this.getParent().getParent().layout(true, true);
		layout(true, true);
	}

	protected void setBusinessObject(EObject object) {
		be = object;
		setDiagramEditor(BPMN2Editor.getEditor(be));
	}
	
	protected final void setDiagramEditor(BPMN2Editor bpmn2Editor) {
		this.bpmn2Editor = bpmn2Editor;
		try {
			modelHandler = ModelHandlerLocator.getModelHandler(bpmn2Editor.getDiagramTypeProvider().getDiagram()
					.eResource());
		} catch (IOException e1) {
			Activator.showErrorWithLogging(e1);
		}
	}
	
	public BPMN2Editor getDiagramEditor() {
		return bpmn2Editor;
	}

	protected void cleanBindings() {
		toolkit.disposeWidgets();
		if (itemProviderAdapter!=null) {
			itemProviderAdapter.dispose();
			itemProviderAdapter = null;
		}
	}

	protected Composite getAttributesParent() {
		if (attributesSection==null || attributesSection.isDisposed()) {
			attributesSection = toolkit.createSection(this, "Attributes");
			attributesSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
			attributesComposite = toolkit.createComposite(attributesSection);
			attributesSection.setClient(attributesComposite);
			attributesComposite.setLayout(new GridLayout(3,false));
//			toolkit.track(attributesComposite);
		}
		return attributesComposite;
	}

	protected Composite getReferencesParent() {
		if (referencesSection==null || referencesSection.isDisposed()) {
			referencesSection = toolkit.createSection(this, "References");
			referencesSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
			referencesComposite = toolkit.createComposite(referencesSection);
			referencesSection.setClient(referencesComposite);
			referencesComposite.setLayout(new GridLayout(3,false));
//			toolkit.track(referencesComposite);
		}
		return referencesComposite;
	}

	/**
	 * This method is called when setEObject is called and this should recreate all bindings and widgets for the
	 * component.
	 */
	public abstract void createBindings(EObject be);
	
	protected Text createTextInput(Composite parent, String name, boolean multiLine) {
		createLabel(parent,name);

		int flag = SWT.NONE;
		if (multiLine) {
			flag |= SWT.WRAP | SWT.MULTI;
		}
		Text text = toolkit.createText(parent, "", flag);
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		if (multiLine) {
			data.heightHint = 50;
		}
		text.setLayoutData(data);

		return text;
	}

	protected Text createIntInput(Composite parent, String name) {
		createLabel(parent,name);

		Text text = toolkit.createText(parent, "");
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		return text;
	}

	protected Button createBooleanInput(Composite parent, String name) {
		createLabel(parent,name);

		Button button = toolkit.createButton(parent, "", SWT.CHECK);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		return button;
	}

	protected Label createLabel(Composite parent, String name) {
		Label label = toolkit.createLabel(parent, name);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		return label;
	}

	protected StyledText createDescription(Composite parent, String description) {
		StyledText styledText = toolkit.createDescription(parent, description);
		
		GridData d = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
		d.horizontalIndent = 4;
		d.verticalIndent = 4;
		d.heightHint = 4 * toolkit.getDescriptionFont().getFontData()[0].getHeight();
		d.widthHint = 100;
		styledText.setLayoutData(d);

		return styledText;
	}

	protected void bindAttribute(EObject object, String name) {
		EStructuralFeature feature = ((EObject)object).eClass().getEStructuralFeature(name);
		if (feature instanceof EAttribute) {
			bindAttribute(object,(EAttribute)feature);
		}
	}
	
	protected void bindAttribute(EObject object, EAttribute attribute) {

		if (preferences.isEnabled(object.eClass(), attribute)) {

			Composite parent = getAttributesParent();
			
			String displayName = getDisplayName(itemProviderAdapter, object, attribute);
			Collection choiceOfValues = getChoiceOfValues(itemProviderAdapter, object, attribute);
			
			if (String.class.equals(attribute.getEType().getInstanceClass())) {
				bindText(object, attribute, createTextInput(parent, displayName, getIsMultiLine(itemProviderAdapter,object,attribute)));
			} else if (boolean.class.equals(attribute.getEType().getInstanceClass())) {
				bindBoolean(object, attribute, createBooleanInput(parent, displayName));
			} else if (int.class.equals(attribute.getEType().getInstanceClass())) {
				bindInt(object, attribute, createIntInput(parent, displayName));
			} else if (choiceOfValues != null) {
				createLabel(parent, displayName);
				createSingleItemEditor(parent, object, attribute, object.eGet(attribute), choiceOfValues);
			} else if ("anyAttribute".equals(attribute.getName())) {
				List<Entry> basicList = ((BasicFeatureMap) object.eGet(attribute)).basicList();
				for (Entry entry : basicList) {
					EStructuralFeature feature = entry.getEStructuralFeature();
					if (Object.class.equals(feature.getEType().getInstanceClass())) {
						Text t = createTextInput(parent, ModelUtil.toDisplayName(feature.getName()), false);
						bindText( object, feature, t);
					}
				}
			}
		}
	}
	
	protected void bindReference(EObject object, String name) {
		EStructuralFeature feature = ((EObject)object).eClass().getEStructuralFeature(name);
		if (feature instanceof EReference) {
			bindReference(object,(EReference)feature);
		}
	}

	protected void bindReference(EObject object, EReference reference) {

		if (preferences.isEnabled(object.eClass(), reference)) {

			Composite parent = getReferencesParent();
			
			Object eGet = object.eGet(reference);
			String displayName = getDisplayName(itemProviderAdapter, object, reference);
	
			createLabel(parent, displayName);
			if (eGet instanceof List) {
				createListEditor(parent, object, reference, eGet);
			} else {
				createSingleItemEditor(parent, object, reference, eGet, null);
			}
		}
	}

	private void createListEditor(Composite parent, final EObject object, final EReference reference, Object eGet) {

		final Text text = toolkit.createText(parent, "");
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button editButton = toolkit.createButton(parent, "Edit...", SWT.PUSH);
		editButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		final List<EObject> refs = (List<EObject>) eGet;
		updateTextField(refs, text);

		SelectionAdapter editListener = new SelectionAdapter() {

			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<EObject> l = null;

				if (modelHandler != null) {
					l = (List<EObject>) modelHandler.getAll(reference.getEType().getInstanceClass());
				}

				FeatureEditorDialog featureEditorDialog = new FeatureEditorDialog(getShell(), LABEL_PROVIDER, object,
						reference, "Select elements", l);

				if (featureEditorDialog.open() == Window.OK) {

					updateEObject(refs, (EList<EObject>) featureEditorDialog.getResult());
					updateTextField(refs, text);
				}
			}

			public void updateEObject(final List<EObject> refs, final EList<EObject> result) {
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {

						if (result == null) {
							refs.clear();
							return;
						}
						refs.retainAll(result);
						for (EObject di : result) {
							if (!refs.contains(di)) {
								refs.add(di);
							}
						}
					}
				});
			}
		};
		editButton.addSelectionListener(editListener);
	}

	private void createSingleItemEditor(Composite parent, final EObject object, final EStructuralFeature reference, Object eGet, Collection values) {
		final ComboViewer comboViewer = toolkit.createComboViewer(parent, LABEL_PROVIDER, SWT.NONE);
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		List<Object> l = null;

		if (values != null) {
			l = Arrays.asList(values.toArray());
		} else if (modelHandler != null) {
			l = (List<Object>) modelHandler.getAll(reference.getEType().getInstanceClass());
		}

		comboViewer.add("");
		comboViewer.add(l.toArray());
		if (eGet != null) {
			comboViewer.setSelection(new StructuredSelection(eGet));
		}

		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = comboViewer.getSelection();
				if (selection instanceof StructuredSelection) {
					Object firstElement = ((StructuredSelection) selection).getFirstElement();
					if (firstElement instanceof EObject) {
						updateEObject(firstElement);
					} else if (firstElement instanceof GatewayDirection) {
						updateGatewayDirection(firstElement);
					} else {
						updateEObject(null);
					}
				}
			}

			public void updateEObject(final Object result) {
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						object.eSet(reference, result);
					}
				});
			}
			
			public void updateGatewayDirection(final Object result) {
				TransactionalEditingDomain domain = bpmn2Editor.getEditingDomain();
				domain.getCommandStack().execute(new RecordingCommand(domain) {
					@Override
					protected void doExecute() {
						GatewayDirection direction = (GatewayDirection) result;
						object.eSet(reference, direction);
					}
				});
			}
			
		});
	}

	private void updateTextField(final List<EObject> refs, Text text) {
		String listText = "";
		if (refs != null) {
			for (int i = 0; i < refs.size() - 1; i++) {
				listText += LABEL_PROVIDER.getText(refs.get(i)) + ", ";
			}
			if (refs.size() > 0) {
				listText += LABEL_PROVIDER.getText(refs.get(refs.size() - 1));
			}
		}

		text.setText(listText);
	}

	public void setShape(BPMNShape shape) {
		this.shape = shape;
	}
	
	protected void bindText(final EObject object, final EStructuralFeature a, final Text text) {

		Object eGet = object.eGet(a);
		if (eGet != null) {
			text.setText(eGet.toString());
		}

		IObservableValue textObserver = SWTObservables.observeText(text, SWT.Modify);
		textObserver.addValueChangeListener(new IValueChangeListener() {

			@SuppressWarnings("restriction")
			@Override
			public void handleValueChange(final ValueChangeEvent e) {

				if (!text.getText().equals(object.eGet(a))) {
					TransactionalEditingDomain editingDomain = bpmn2Editor.getEditingDomain();
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						@Override
						protected void doExecute() {
							object.eSet(a, e.diff.getNewValue());
						}
					});
					if (bpmn2Editor.getDiagnostics()!=null) {
						// revert the change and display error status message.
						text.setText((String) object.eGet(a));
						bpmn2Editor.showErrorMessage(bpmn2Editor.getDiagnostics().getMessage());
					}
					else
						bpmn2Editor.showErrorMessage(null);
				}
			}
		});
		
		text.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				bpmn2Editor.showErrorMessage(null);
			}
		});
	}

	protected void bindBoolean(final EObject object, final EStructuralFeature a, final Button button) {
		
		button.setSelection((Boolean) object.eGet(a));
		IObservableValue buttonObserver = SWTObservables.observeSelection(button);
		buttonObserver.addValueChangeListener(new IValueChangeListener() {
			
			@SuppressWarnings("restriction")
			@Override
			public void handleValueChange(ValueChangeEvent event) {

				if (!object.eGet(a).equals(button.getSelection())) {
					TransactionalEditingDomain editingDomain = bpmn2Editor.getEditingDomain();
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						@Override
						protected void doExecute() {
							object.eSet(a, button.getSelection());
						}
					});
					
					if (bpmn2Editor.getDiagnostics()!=null) {
						// revert the change and display error status message.
						button.setSelection((Boolean) object.eGet(a));
						bpmn2Editor.showErrorMessage(bpmn2Editor.getDiagnostics().getMessage());
					}
					else
						bpmn2Editor.showErrorMessage(null);
				}
			}
		});
		
		button.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				bpmn2Editor.showErrorMessage(null);
			}
		});
	}
	
	protected void bindInt(final EObject object, final EStructuralFeature a, final Text text) {

		text.addVerifyListener(new VerifyListener() {

			/**
			 * taken from
			 * http://dev.eclipse.org/viewcvs/viewvc.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets
			 * /Snippet19.java?view=co
			 */
			@Override
			public void verifyText(VerifyEvent e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});

		Object eGet = object.eGet(a);
		if (eGet != null) {
			text.setText(eGet.toString());
		}

		IObservableValue textObserveTextObserveWidget = SWTObservables.observeText(text, SWT.Modify);
		textObserveTextObserveWidget.addValueChangeListener(new IValueChangeListener() {
			@Override
			public void handleValueChange(ValueChangeEvent event) {

				try {
					final int i = Integer.parseInt(text.getText());
					if (!object.eGet(a).equals(i)) {
						setFeatureValue(i);
					}
				} catch (NumberFormatException e) {
					text.setText((String) object.eGet(a));
					Activator.logError(e);
				}
			}

			@SuppressWarnings("restriction")
			private void setFeatureValue(final int i) {
				RecordingCommand command = new RecordingCommand(bpmn2Editor.getEditingDomain()) {
					@Override
					protected void doExecute() {
						object.eSet(a, i);
					}
				};
				bpmn2Editor.getEditingDomain().getCommandStack().execute(command);
				if (bpmn2Editor.getDiagnostics()!=null) {
					// revert the change and display error status message.
					text.setText((String) object.eGet(a));
					bpmn2Editor.showErrorMessage(bpmn2Editor.getDiagnostics().getMessage());
				}
				else
					bpmn2Editor.showErrorMessage(null);
			}
		});

		
		text.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				bpmn2Editor.showErrorMessage(null);
			}
		});
	}

	protected void bindList(EObject object, String name) {
		EStructuralFeature feature = ((EObject)object).eClass().getEStructuralFeature(name);
		if (feature !=null && object.eGet(feature) instanceof EList) {
			bindList(object,feature);
		}
	}
	
	protected void bindList(final EObject object, final EStructuralFeature feature) {

		if (preferences.isEnabled(object.eClass(), feature)) {

			AbstractBpmn2TableComposite tableComposite = new AbstractBpmn2TableComposite(this, AbstractBpmn2TableComposite.DEFAULT_STYLE);
			toolkit.track(tableComposite);
			tableComposite.bindList(object, feature, itemProviderAdapter);
		}
	}
	
	// TODO: create an adapter for this stuff in the AdapterRegistry
	private String getDisplayName(ItemProviderAdapter itemProviderAdapter, EObject object, EStructuralFeature feature) {
		IItemPropertyDescriptor propertyDescriptor = null;
		if (itemProviderAdapter!=null)
			propertyDescriptor = itemProviderAdapter.getPropertyDescriptor(object, feature);
		
		String displayName;

		if (propertyDescriptor!=null) {
			displayName = propertyDescriptor.getDisplayName(object);
		}
		else {
			displayName = ModelUtil.toDisplayName(feature.getName());
		}
		return displayName;
	}

	private boolean getIsMultiLine(ItemProviderAdapter itemProviderAdapter, EObject object, EStructuralFeature feature) {
		IItemPropertyDescriptor propertyDescriptor = null;
		if (itemProviderAdapter!=null)
			propertyDescriptor = itemProviderAdapter.getPropertyDescriptor(object, feature);
		
		boolean isMultiLine = false;

		if (propertyDescriptor!=null) {
			isMultiLine = propertyDescriptor.isMultiLine(object);
		}
		return isMultiLine;
	}

	private Collection getChoiceOfValues(ItemProviderAdapter itemProviderAdapter, EObject object, EStructuralFeature feature) {
		IItemPropertyDescriptor propertyDescriptor = null;
		if (itemProviderAdapter!=null)
			propertyDescriptor = itemProviderAdapter.getPropertyDescriptor(object, feature);
		
		if (propertyDescriptor!=null) {
			return propertyDescriptor.getChoiceOfValues(object);
		}
		return null;
	}

}