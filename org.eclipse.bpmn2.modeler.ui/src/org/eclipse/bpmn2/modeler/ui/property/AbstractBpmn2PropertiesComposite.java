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

import java.util.ArrayList;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.provider.BpmnDiItemProviderAdapterFactory;
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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.jface.viewers.IStructuredContentProvider;

public abstract class AbstractBpmn2PropertiesComposite extends Composite {

	public final static ComposedAdapterFactory ADAPTER_FACTORY;
	
	// bitflags for optional components of list features
	public final static int SHOW_LIST_LABEL = 1;
	public final static int EDITABLE_LIST = 2;
	public final static int ORDERED_LIST = 4;
	
	protected EObject be;
	protected BPMN2Editor bpmn2Editor;
	protected final DataBindingContext bindingContext;
	protected final ArrayList<Widget> widgets = new ArrayList<Widget>();
	protected final ArrayList<Binding> bindings = new ArrayList<Binding>();
	protected final Composite parent;
	protected final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	protected IProject project;

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

	public final void setEObject(BPMN2Editor bpmn2Editor, final EObject be) {
		String projectName = bpmn2Editor.getDiagramTypeProvider().getDiagram().eResource().getURI().segment(1);
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		setDiagramEditor(bpmn2Editor);
		setEObject(be);
	}

	private final void setDiagramEditor(BPMN2Editor bpmn2Editor) {
		this.bpmn2Editor = bpmn2Editor;
	}

	protected final void setEObject(final EObject be) {
		this.be = be;
		cleanBindings();
		if (be != null) {
			createBindings(be);
		}
		parent.getParent().layout(true, true);
		layout(true, true);
	}

	protected void setBusinessObject(EObject be) {
		this.be = be;
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

	protected void createLabel(String name) {
		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		toolkit.adapt(label, true, true);
		label.setText(name);
		widgets.add(label);
	}
	
	/**
	 * Override this to select attributes to be rendered 
	 * @param attribute
	 * @param object
	 * @return
	 */
	protected boolean canBindAttribute(EAttribute attribute, EObject object) {
		return true;
	}
	
	protected void bindText(final EStructuralFeature a, final Text text) {
		bindText(a, text, be);
	}
	
	protected void bindText(final EStructuralFeature a, final Text text, final EObject object) {

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

	protected void bindBoolean(final EStructuralFeature a, final Button button) {
		bindBoolean(a, button, be);
	}

	protected void bindBoolean(final EStructuralFeature a, final Button button, final EObject object) {
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
	
	protected void bindInt(final EStructuralFeature a, final Text text) {
		bindInt(a, text, be);
	}
	
	protected void bindInt(final EStructuralFeature a, final Text text, final EObject object) {

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
	 * Override this to select which lists are rendered
	 * @param feature
	 * @param object
	 * @return
	 */
	protected boolean canBindList(EStructuralFeature feature, EObject object) {
		if (!(be.eGet(feature) instanceof EList<?>)) {
			return false;
		}
		Class<?> clazz = feature.getEType().getInstanceClass();
		if (!EObject.class.isAssignableFrom(clazz)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Override this to select which table columns are rendered
	 * @param attribute
	 * @param object
	 * @return
	 */
	protected boolean canBindListColumn(EAttribute attribute, EObject object) {
		return true;
	}
	
	protected boolean canModifyListColumn(Object element, EAttribute attribute, EObject object) {
		return true;
	}
	
	protected void bindList(final EStructuralFeature feature, EObject object, int flags) {
		if (!canBindList(feature, object)) {
			return;
		}
		EList<EObject> list = (EList<EObject>)be.eGet(feature);
		
		ColumnTableProvider tableProvider = new ColumnTableProvider();
		EClass cl = (EClass) feature.getEType();
		for (EAttribute a1 : cl.getEAllAttributes()) {
			if (canBindListColumn(a1, cl))
			tableProvider.add(new TableColumn(be,a1));
		}

		if (tableProvider.getColumns().size()==0) {
			return;
		}

		int span = 3;
		if ((flags & SHOW_LIST_LABEL)!=0) {
			createLabel(feature.getName());
			span = 2;
		}
		
		Composite parent = new Composite(this, SWT.BORDER);
		parent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, span, 1));
		toolkit.paintBordersFor(parent);
		toolkit.adapt(parent, true, true);
		widgets.add(parent);
		parent.setLayout(new GridLayout(2, false));

		Composite buttonSection = new Composite(parent, SWT.NONE);
		buttonSection.setLayoutData(new GridData(GridData.BEGINNING));
		toolkit.adapt(buttonSection, true, true);
		widgets.add(buttonSection);
		buttonSection.setLayout(new FillLayout(SWT.VERTICAL));
		
		final Button addButton = new Button(buttonSection, SWT.PUSH);
		addButton.setText("Add");
		toolkit.adapt(addButton, true, true);
		widgets.add(addButton);

		final Button removeButton = new Button(buttonSection, SWT.PUSH);
		removeButton.setText("Remove");
		removeButton.setEnabled(false);
		toolkit.adapt(removeButton, true, true);
		widgets.add(removeButton);

		final Button upButton = new Button(buttonSection, SWT.PUSH);
		upButton.setText("Move up");
		upButton.setEnabled(false);
		toolkit.adapt(upButton, true, true);
		widgets.add(upButton);

		final Button downButton = new Button(buttonSection, SWT.PUSH);
		downButton.setText("Move down");
		downButton.setEnabled(false);
		toolkit.adapt(downButton, true, true);
		widgets.add(downButton);
		
		span = 2;
		if ((flags & (EDITABLE_LIST | ORDERED_LIST))!=0) {
			span = 1;
		}
		else {
			buttonSection.setVisible(false);
		}
		if ((flags & EDITABLE_LIST)==0) {
			addButton.setVisible(false);
			removeButton.setVisible(false);
		}
		if ((flags & ORDERED_LIST)==0) {
			upButton.setVisible(false);
			downButton.setVisible(false);
		}
		
		final Table table = new Table(parent, SWT.FULL_SELECTION | SWT.V_SCROLL);
		toolkit.adapt(table, true, true);
		widgets.add(table);
		table.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, span, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableViewer tableViewer = new TableViewer(table);
		tableProvider.createTableLayout(table);
		
		tableViewer.setLabelProvider(tableProvider);
		tableViewer.setCellModifier(tableProvider);
		tableViewer.setContentProvider(new ContentProvider(object, list));
		tableViewer.setColumnProperties(tableProvider.getColumnProperties());
		tableViewer.setCellEditors(tableProvider.createCellEditors(table));

		tableViewer
				.addPostSelectionChangedListener(new ISelectionChangedListener() {
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
		tableViewer.setInput(list);
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return list.toArray();
		}
		
	}
	
	public class TableColumn extends ColumnTableProvider.Column implements ILabelProvider, ICellModifier {
		private EAttribute attribute;
		private EObject object;
		
		public TableColumn(EObject o, EAttribute a) {
			object = o;
			attribute = a;
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
			return canModifyListColumn(element, attribute, object);
		}
		
		public void modify(Object element, String property, Object value) {
			final EObject target = (EObject)element;
			final Object newValue = value;
			if (!target.eGet(attribute).equals(value)) {
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
			}
		}

		@Override
		public Object getValue(Object element, String property) {
			return getText(element);
		}
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