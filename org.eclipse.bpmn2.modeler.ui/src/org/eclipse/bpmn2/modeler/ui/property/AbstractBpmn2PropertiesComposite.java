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
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiPackage;
import org.eclipse.bpmn2.di.provider.BpmnDiItemProviderAdapterFactory;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.preferences.ToolEnablementPreferences;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.providers.ColumnTableProvider;
import org.eclipse.bpmn2.modeler.ui.property.providers.TableCursor;
import org.eclipse.bpmn2.provider.Bpmn2ItemProviderAdapterFactory;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.dd.dc.provider.DcItemProviderAdapterFactory;
import org.eclipse.dd.di.provider.DiItemProviderAdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public abstract class AbstractBpmn2PropertiesComposite extends Composite {

	public final static ComposedAdapterFactory ADAPTER_FACTORY;
	public static final Bpmn2Factory MODEL_FACTORY = Bpmn2Factory.eINSTANCE;
	
	// bitflags for optional components of list features
	public final static int SHOW_LIST_LABEL = 1;
	public final static int EDITABLE_LIST = 2;
	public final static int ORDERED_LIST = 4;
	
	protected TabbedPropertySheetPage tabbedPropertySheetPage;
	protected EObject be;
	protected BPMN2Editor bpmn2Editor;
	protected final DataBindingContext bindingContext;
	protected final ArrayList<Widget> widgets = new ArrayList<Widget>();
	protected final ArrayList<Binding> bindings = new ArrayList<Binding>();
	protected final Composite parent;
	protected final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	protected IProject project;
	protected final AdapterFactoryLabelProvider LABEL_PROVIDER = new AdapterFactoryLabelProvider(ADAPTER_FACTORY);
	protected ModelHandler modelHandler;
	protected BPMNShape shape;

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
		this.parent = parent;
		bindingContext = new DataBindingContext();
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
	
	public void setSheetPage(TabbedPropertySheetPage tabbedPropertySheetPage) {
		this.tabbedPropertySheetPage = tabbedPropertySheetPage;
	}
	
	public final void setEObject(BPMN2Editor bpmn2Editor, final EObject object) {
		String projectName = bpmn2Editor.getDiagramTypeProvider().getDiagram().eResource().getURI().segment(1);
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		setDiagramEditor(bpmn2Editor);
		setEObject(object);
	}

	private final void setDiagramEditor(BPMN2Editor bpmn2Editor) {
		this.bpmn2Editor = bpmn2Editor;
		try {
			modelHandler = ModelHandlerLocator.getModelHandler(bpmn2Editor.getDiagramTypeProvider().getDiagram()
					.eResource());
		} catch (IOException e1) {
			Activator.showErrorWithLogging(e1);
			return;
		}
	}

	protected final void setEObject(final EObject object) {
		be = object;
		cleanBindings();
		if (be != null) {
			createBindings(be);
		}
		parent.getParent().layout(true, true);
		layout(true, true);
	}

	protected void setBusinessObject(EObject object) {
		be = object;
	}
	
	/**
	 * This method is called when setEObject is called and this should recreate all bindings and widgets for the
	 * component.
	 */
	public abstract void createBindings(EObject be);

	protected Text createTextInput(String name, boolean multiLine) {
		createLabel(name);

		int flag = SWT.BORDER;
		if (multiLine) {
			flag |= SWT.BORDER | SWT.WRAP | SWT.MULTI;
		}
		Text text = new Text(this, flag);
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		if (multiLine) {
			data.heightHint = 50;
		}
		text.setLayoutData(data);
		toolkit.adapt(text, true, true);
		widgets.add(text);

		return text;
	}

	protected Text createIntInput(String name) {
		createLabel(name);

		Text text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		toolkit.adapt(text, true, true);
		widgets.add(text);
		return text;
	}

	protected Button createBooleanInput(String name) {
		createLabel(name);

		Button button = new Button(this, SWT.CHECK);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		toolkit.adapt(button, true, true);
		widgets.add(button);
		return button;
	}

	protected Label createLabel(String name) {
		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		toolkit.adapt(label, true, true);
		label.setText(name);
		widgets.add(label);
		return label;
	}
	
	protected Button createPushButton(Composite parent, String name) {
		final Button button = new Button(parent, SWT.PUSH);
		button.setText(name);
		toolkit.adapt(button, true, true);
		widgets.add(button);
		return button;
	}

	/**
	 * Implement this to select attributes to be rendered 
	 * @param attribute
	 * @param object
	 * @return
	 */
	protected abstract boolean canBindAttribute(EObject object, EAttribute attribute);

	protected void bindAttribute(EObject object, EAttribute attribute, ItemProviderAdapter itemProviderAdapter) {
		
		if (!canBindAttribute(object,attribute)) {
			return;
		}
		
		String displayName = getDisplayName(itemProviderAdapter, object, attribute);
		Collection choiceOfValues = getChoiceOfValues(itemProviderAdapter, object, attribute);
		
		if (String.class.equals(attribute.getEType().getInstanceClass())) {
			bindText( object, attribute, createTextInput(displayName, getIsMultiLine(itemProviderAdapter,object,attribute)));
		} else if (boolean.class.equals(attribute.getEType().getInstanceClass())) {
			bindBoolean(object, attribute, createBooleanInput(displayName));
		} else if (int.class.equals(attribute.getEType().getInstanceClass())) {
			bindInt(object, attribute, createIntInput(displayName));
		} else if (choiceOfValues != null) {
			createLabel(displayName);
			createSingleItemEditor(object, attribute, object.eGet(attribute), choiceOfValues);
		} else if ("anyAttribute".equals(attribute.getName())) {
			List<Entry> basicList = ((BasicFeatureMap) object.eGet(attribute)).basicList();
			for (Entry entry : basicList) {
				EStructuralFeature feature = entry.getEStructuralFeature();
				if (Object.class.equals(feature.getEType().getInstanceClass())) {
					Text t = createTextInput(ModelUtil.toDisplayName(feature.getName()), false);
					bindText( object, feature, t);
				}
			}
		}
	}
	
	/**
	 * Implement this to select references to be rendered 
	 * @param object
	 * @param attribute
	 * @return
	 */
	protected abstract boolean canBindReference(EObject object, EReference reference);

	protected void bindReferences(EObject object, ItemProviderAdapter itemProviderAdapter) {
		ToolEnablementPreferences preferences = ToolEnablementPreferences.getPreferences(project);
		
		EList<EReference> eAllContainments = object.eClass().getEAllContainments();
		for (EReference e : object.eClass().getEAllReferences()) {
			if (preferences.isEnabled(object.eClass(), e) && !eAllContainments.contains(e)) {
				IItemPropertyDescriptor propertyDescriptor = itemProviderAdapter.getPropertyDescriptor(object, e);
				if (propertyDescriptor!=null)
					bindReference(object, e, propertyDescriptor.getDisplayName(e));
			}
		}

		if (object instanceof Participant) {
			Diagram diagram = bpmn2Editor.getDiagramTypeProvider().getDiagram();
			if (shape != null && shape.getParticipantBandKind() != null) {
				bindBoolean(shape, shape.eClass().getEStructuralFeature(BpmnDiPackage.BPMN_SHAPE__IS_MESSAGE_VISIBLE),
						createBooleanInput("Is Message Visible"));
			}

		}
	}

	protected void bindReference(EObject object, EReference reference, String name) {
		if ( !canBindReference(object, reference) ) {
			return;
		}
		
		Object eGet = object.eGet(reference);

		createLabel(name);
		if (eGet instanceof List) {
			createListEditor(object, reference, eGet);
		} else {
			createSingleItemEditor(object, reference, eGet, null);
		}
	}

	private void createListEditor(final EObject object, final EReference reference, Object eGet) {

		final Text text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(text, true, true);
		widgets.add(text);

		Button editButton = new Button(this, SWT.NONE);
		editButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		editButton.setText("Edit ...");
		toolkit.adapt(editButton, true, true);
		widgets.add(editButton);

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

	private void createSingleItemEditor(final EObject object, final EStructuralFeature reference, Object eGet, Collection values) {
		final ComboViewer combo = new ComboViewer(this, SWT.BORDER);
		Combo c = combo.getCombo();
		combo.setLabelProvider(LABEL_PROVIDER);
		c.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		toolkit.adapt(c, true, true);
		widgets.add(c);

		List<Object> l = null;

		if (values != null) {
			l = Arrays.asList(values.toArray());
		} else if (modelHandler != null) {
			l = (List<Object>) modelHandler.getAll(reference.getEType().getInstanceClass());
		}

		combo.add("");
		combo.add(l.toArray());
		if (eGet != null) {
			combo.setSelection(new StructuredSelection(eGet));
		}

		combo.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = combo.getSelection();
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

	/**
	 * Implement this to select which lists are rendered
	 * @param feature
	 * @param object
	 * @return
	 */
	protected abstract boolean canBindList(EObject object, EStructuralFeature feature);
	
	/**
	 * Implement this to select which table columns are rendered
	 * @param attribute
	 * @param object
	 * @return
	 */
	protected abstract boolean canBindListColumn(EObject object, EAttribute attribute);
	
	/**
	 * Implement this to select which table columns can be edited
	 * @param element
	 * @param attribute
	 * @param object
	 * @return
	 */
	protected abstract boolean canModifyListColumn(EObject object, EAttribute attribute, Object element);

	/**
	 * Implement this to provide table style flags 
	 * @param object
	 * @param feature
	 * @return
	 */
	protected abstract int getListStyleFlags(EObject object, EStructuralFeature feature);

	protected void bindList(EObject object, EStructuralFeature feature, ItemProviderAdapter itemProviderAdapter) {
		if (!canBindList(object, feature)) {
			return;
		}
		if (!(object.eGet(feature) instanceof EList<?>)) {
			return;
		}
		Class<?> clazz = feature.getEType().getInstanceClass();
		if (!EObject.class.isAssignableFrom(clazz)) {
			return;
		}
		
		final TransactionalEditingDomain editingDomain = bpmn2Editor.getEditingDomain();
		final EList<EObject> list = (EList<EObject>)object.eGet(feature);
		final EClass listItemClass = (EClass) feature.getEType();
		
		////////////////////////////////////////////////////////////
		// Collect columns to be displayed and build column provider
		////////////////////////////////////////////////////////////
		ColumnTableProvider tableProvider = new ColumnTableProvider();
		for (EAttribute a1 : listItemClass.getEAllAttributes()) {
			if (canBindListColumn(listItemClass, a1))
			tableProvider.add(new TableColumn(object,a1));
		}
		if (tableProvider.getColumns().size()==0) {
			return;
		}

		////////////////////////////////////////////////////////////
		// Display table label and draw border around table if the
		// SHOW_LIST_LABEL style flag is set
		////////////////////////////////////////////////////////////
		int span = 3;
		int flags = getListStyleFlags(object,feature);
		int border = SWT.NONE;
		if ((flags & SHOW_LIST_LABEL)!=0) {
			Label label = createLabel(
					ModelUtil.toDisplayName(feature.getName()));
			label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
			span = 2;
			border = SWT.BORDER;
		}
		
		////////////////////////////////////////////////////////////
		// Create a composite to hold the buttons and table
		////////////////////////////////////////////////////////////
		Composite parent = new Composite(this, border);
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, span, 1));
		toolkit.paintBordersFor(parent);
		toolkit.adapt(parent, true, true);
		widgets.add(parent);
		parent.setLayout(new GridLayout(2, false));
		
		////////////////////////////////////////////////////////////
		// Create button section for add/remove/up/down buttons
		////////////////////////////////////////////////////////////
		Composite buttonSection = new Composite(parent, SWT.NONE);
		buttonSection.setLayoutData(new GridData(SWT.TOP, SWT.LEFT, false, false, 1, 1));
		toolkit.adapt(buttonSection, true, true);
		widgets.add(buttonSection);
		buttonSection.setLayout(new FillLayout(SWT.VERTICAL));

		////////////////////////////////////////////////////////////
		// Create table
		// allow table to fill entire width if there are no buttons
		////////////////////////////////////////////////////////////
		span = 2;
		if ((flags & (EDITABLE_LIST | ORDERED_LIST))!=0) {
			span = 1;
		}
		else {
			buttonSection.setVisible(false);
		}
		final Table table = new Table(parent, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.BORDER);
		toolkit.adapt(table, true, true);
		widgets.add(table);
		GridData d = new GridData(SWT.FILL, SWT.FILL, true, true, span, 1);
		d.widthHint = 100;
		d.heightHint = 100;
		table.setLayoutData(d);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		////////////////////////////////////////////////////////////
		// Create buttons for add/remove/up/down
		////////////////////////////////////////////////////////////
		final Button addButton = createPushButton(buttonSection, "Add");

		final Button removeButton = createPushButton(buttonSection, "Remove");
		removeButton.setEnabled(false);

		final Button upButton = createPushButton(buttonSection, "Move Up");
		upButton.setEnabled(false);

		final Button downButton = createPushButton(buttonSection, "Move Down");
		downButton.setEnabled(false);
		
		if ((flags & EDITABLE_LIST)==0) {
			addButton.setVisible(false);
			removeButton.setVisible(false);
		}
		if ((flags & ORDERED_LIST)==0) {
			upButton.setVisible(false);
			downButton.setVisible(false);
		}
		
		////////////////////////////////////////////////////////////
		// Create table viewer and cell editors
		////////////////////////////////////////////////////////////
		final TableViewer tableViewer = new TableViewer(table);
		tableProvider.createTableLayout(table);
		tableProvider.setTableViewer(tableViewer);
		
		tableViewer.setLabelProvider(tableProvider);
		tableViewer.setCellModifier(tableProvider);
		tableViewer.setContentProvider(new ContentProvider(object, list));
		tableViewer.setColumnProperties(tableProvider.getColumnProperties());
		tableViewer.setCellEditors(tableProvider.createCellEditors(table));

		////////////////////////////////////////////////////////////
		// Create handlers
		////////////////////////////////////////////////////////////
		tableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				removeButton.setEnabled(!event.getSelection().isEmpty());
				int i = table.getSelectionIndex();
				if (i>0)
					upButton.setEnabled(!event.getSelection().isEmpty());
				else
					upButton.setEnabled(false);
				if (i<table.getItemCount()-1)
					downButton.setEnabled(!event.getSelection().isEmpty());
				else
					downButton.setEnabled(false);
			}
		});
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
					@Override
					protected void doExecute() {
						EObject newItem = MODEL_FACTORY.create(listItemClass);
						list.add(newItem);
						tableViewer.setInput(list);
						table.setSelection(list.size()-1);
						tabbedPropertySheetPage.resizeScrolledComposite();
					}
				});
			}
		});
		
		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
					@Override
					protected void doExecute() {
						int i = table.getSelectionIndex();
						list.remove(i);
						tableViewer.setInput(list);
						if (i>=list.size())
							i = list.size() - 1;
						if (i>=0)
							table.setSelection(i);
						tabbedPropertySheetPage.resizeScrolledComposite();
					}
				});
			}
		});

		upButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
					@Override
					protected void doExecute() {
						int i = table.getSelectionIndex();
						list.move(i-1, i);
						tableViewer.setInput(list);
						table.setSelection(i-1);
						tabbedPropertySheetPage.resizeScrolledComposite();
					}
				});
			}
		});
		
		downButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
					@Override
					protected void doExecute() {
						int i = table.getSelectionIndex();
						list.move(i+1, i);
						tableViewer.setInput(list);
						table.setSelection(i+1);
						tabbedPropertySheetPage.resizeScrolledComposite();
					}
				});
			}
		});

		tableViewer.setInput(list);
		
		// a TableCursor allows navigation of the table with keys
		TableCursor.create(table, tableViewer);
	}

	public class ContentProvider implements IStructuredContentProvider {
		private EObject parent;
		private EList<EObject> list;
		
		public ContentProvider(EObject p, EList<EObject> l) {
			parent = p;
			list = l;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return list.toArray();
		}
	}
	
	public class TableColumn extends ColumnTableProvider.Column implements ILabelProvider, ICellModifier {
		private TableViewer tableViewer;
		private EAttribute attribute;
		private EObject object;
		
		public TableColumn(EObject o, EAttribute a) {
			object = o;
			attribute = a;
		}
		
		public void setTableViewer(TableViewer t) {
			tableViewer = t;
		}
		
		@Override
		public String getHeaderText() {
			return ModelUtil.toDisplayName(attribute.getName());
		}

		@Override
		public String getProperty() {
			return attribute.getName(); //$NON-NLS-1$
		}

		@Override
		public int getInitialWeight() {
			return 10;
		}

		public String getText(Object element) {
			Object value = ((EObject)element).eGet(attribute);
			return value==null ? "" : value.toString();
		}
		
		public CellEditor createCellEditor (Composite parent) {			
			return new TextCellEditor(parent, SWT.NO_BACKGROUND );
		}
		
		public boolean canModify(Object element, String property) {
			return canModifyListColumn(object, attribute, element);
		}
		
		public void modify(Object element, String property, Object value) {
			final EObject target = (EObject)element;
			final Object newValue = value;
			final Object oldValue = target.eGet(attribute); 
			if (oldValue==null || !oldValue.equals(value)) {
				TransactionalEditingDomain editingDomain = bpmn2Editor.getEditingDomain();
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
					@Override
					protected void doExecute() {
						target.eSet(attribute, newValue);
					}
				});
				if (bpmn2Editor.getDiagnostics()!=null) {
					// revert the change and display error status message.
					bpmn2Editor.showErrorMessage(bpmn2Editor.getDiagnostics().getMessage());
				}
				else
					bpmn2Editor.showErrorMessage(null);
				tableViewer.refresh();
			}
		}

		@Override
		public Object getValue(Object element, String property) {
			return getText(element);
		}
	}

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
			propertyDescriptor.getChoiceOfValues(object);
		}
		return null;
	}
	
	protected void cleanBindings() {
		for (Binding b : bindings) {
			b.dispose();
		}
		bindings.clear();

		for (Widget w : widgets) {
			w.dispose();
		}
		widgets.clear();
	}

}