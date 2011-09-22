/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.ui.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.providers.ColumnTableProvider;
import org.eclipse.bpmn2.modeler.ui.property.providers.TableCursor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor.EDataTypeCellEditor;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author Bob Brodt
 *
 */
public abstract class AbstractBpmn2TableComposite extends Composite {

	public static final Bpmn2Factory MODEL_FACTORY = Bpmn2Factory.eINSTANCE;
	
	protected final TrackingFormToolkit toolkit = new TrackingFormToolkit(Display.getCurrent());
	protected BPMN2Editor bpmn2Editor;
	protected TabbedPropertySheetPage tabbedPropertySheetPage;
	protected int style;
	
	public AbstractBpmn2TableComposite(Composite parent, int style) {
		super(parent, style & ~SWT.BUTTON_MASK);
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		this.style = style;
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(3, false));
	}
	
	public void setDiagramEditor(BPMN2Editor bpmn2Editor) {
		this.bpmn2Editor = bpmn2Editor;
	}
	
	public void setSheetPage(TabbedPropertySheetPage tabbedPropertySheetPage) {
		this.tabbedPropertySheetPage = tabbedPropertySheetPage;
	}

	/**
	 * Implement this to select which features are rendered
	 * @param object - the list object
	 * @param feature - the feature of the item in the list (column)
	 * @return true to render the column
	 */
	protected abstract boolean canBind(EObject object, EStructuralFeature feature);
	
	/**
	 * Implement this to select which columns are editable
	 * @param object - the list object
	 * @param feature - the feature of the item contained in the list
	 * @param item - the selected item in the list
	 * @return true to allow editing
	 */
	protected abstract boolean canModify(EObject object, EStructuralFeature feature, EObject item);

	/**
	 * Override this if construction of new list items needs special handling. 
	 * @param object
	 * @param feature
	 * @return
	 */
	protected EObject addListItem(EObject object, EStructuralFeature feature) {
		EClass listItemClass = (EClass) feature.getEType();
		EList<EObject> list = (EList<EObject>)object.eGet(feature);
		EObject newItem = MODEL_FACTORY.create(listItemClass);
		list.add(newItem);
		ModelUtil.addID(newItem);
		return newItem;
	}
	
	/**
	 * Override this if removal of list items needs special handling. 
	 * @param object
	 * @param feature
	 * @param item
	 * @return
	 */
	protected boolean removeListItem(EObject object, EStructuralFeature feature, Object item) {
		EList<EObject> list = (EList<EObject>)object.eGet(feature);
		list.remove(item);
		return true;
	}

	protected void bindList(final EObject object, final EStructuralFeature feature, ItemProviderAdapter itemProviderAdapter) {
		if (!canBind(object, feature)) {
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
			if ("anyAttribute".equals(a1.getName())) {
				List<EStructuralFeature> anyAttributes = new ArrayList<EStructuralFeature>();
				// are there any actual "anyAttribute" instances we can look at
				// to get the feature names and types from?
				// TODO: enhance the table to dynamically allow creation of new
				// columns which will be added to the "anyAttributes"
				for (EObject instance : list) {
					Object o = instance.eGet(a1);
					if (o instanceof BasicFeatureMap) {
						BasicFeatureMap map = (BasicFeatureMap)o;
						for (Entry entry : map) {
							EStructuralFeature f1 = entry.getEStructuralFeature();
							if (f1 instanceof EAttribute && !anyAttributes.contains(f1)) {
								if (canBind(listItemClass, f1)) {
									tableProvider.add(new TableColumn(object,(EAttribute)f1));
								}
								anyAttributes.add(f1);
							}
						}
					}
				}
			}
			else if (FeatureMap.Entry.class.equals(a1.getEType().getInstanceClass())) {
				// TODO: how do we handle these?
				// System.out.println("FeatureMapEntry: "+listItemClass.getName()+"."+a1.getName());
			}
			else if (canBind(listItemClass, a1)) {
				tableProvider.add(new TableColumn(object,a1));
			}
		}
		if (tableProvider.getColumns().size()==0) {
			return;
		}

		////////////////////////////////////////////////////////////
		// Display table label and draw border around table if the
		// SWT.TITLE style flag is set
		////////////////////////////////////////////////////////////
		GridData gridData;
		int span = 3;
		int border = SWT.NONE;
		if ((style & SWT.TITLE)!=0) {
			Label label = toolkit.createLabel(this, ModelUtil.toDisplayName(feature.getName()));
			label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
			span = 2;
			border = SWT.BORDER;
		}

		////////////////////////////////////////////////////////////
		// Create a composite to hold the buttons and table
		////////////////////////////////////////////////////////////
		Composite tableAndButtonsSection = toolkit.createComposite(this, border);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true, span, 1);
		tableAndButtonsSection.setLayoutData(gridData);
		tableAndButtonsSection.setLayout(new GridLayout(2, false));
		
		////////////////////////////////////////////////////////////
		// Create button section for add/remove/up/down buttons
		////////////////////////////////////////////////////////////
		Composite buttonSection = toolkit.createComposite(tableAndButtonsSection);
		buttonSection.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		buttonSection.setLayout(new FillLayout(SWT.VERTICAL));

		////////////////////////////////////////////////////////////
		// Create table
		// allow table to fill entire width if there are no buttons
		////////////////////////////////////////////////////////////
		span = 2;
		if ((style & SWT.BUTTON_MASK)!=0) {
			span = 1;
		}
		else {
			buttonSection.setVisible(false);
		}
		final Table table = toolkit.createTable(tableAndButtonsSection, SWT.FULL_SELECTION | SWT.V_SCROLL);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true, span, 1);
		gridData.widthHint = 100;
		gridData.heightHint = 100;
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		////////////////////////////////////////////////////////////
		// Create buttons for add/remove/up/down
		////////////////////////////////////////////////////////////
		final Button addButton = toolkit.createPushButton(buttonSection, "Add");

		final Button removeButton = toolkit.createPushButton(buttonSection, "Remove");
		removeButton.setEnabled(false);

		final Button upButton = toolkit.createPushButton(buttonSection, "Move Up");
		upButton.setEnabled(false);

		final Button downButton = toolkit.createPushButton(buttonSection, "Move Down");
		downButton.setEnabled(false);

		final Button editButton = toolkit.createPushButton(buttonSection, "Edit...");
		editButton.setEnabled(false);
		
		if ((style & SWT.BUTTON1)==0)
			addButton.setVisible(false);
		if ((style & SWT.BUTTON2)==0)
			removeButton.setVisible(false);
		if ((style & SWT.BUTTON3)==0)
			upButton.setVisible(false);
		if ((style & SWT.BUTTON4)==0)
			downButton.setVisible(false);
		if ((style & SWT.BUTTON5)==0)
			editButton.setVisible(false);
		
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
		// add a resource set listener to update the treeviewer when
		// when something interesting happens
		////////////////////////////////////////////////////////////
		final ResourceSetListenerImpl domainListener = new ResourceSetListenerImpl() {
			@Override
			public void resourceSetChanged(ResourceSetChangeEvent event) {
				List<Notification> notifications = event.getNotifications();
				try {
					for (Notification notification : notifications) {
						tableViewer.setInput(list);
					}
				}
				catch (Exception e) {
					// silently ignore :-o
				}
			}
		};
		bpmn2Editor.getEditingDomain().addResourceSetListener(domainListener);
		table.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				bpmn2Editor.getEditingDomain().removeResourceSetListener(domainListener);
			}
		});

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
						EObject newItem = addListItem(object,feature);
						if (newItem!=null) {
							tableViewer.setInput(list);
							tableViewer.setSelection(new StructuredSelection(newItem));
							tabbedPropertySheetPage.resizeScrolledComposite();
						}
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
						if (removeListItem(object,feature,list.get(i))) {
							tableViewer.setInput(list);
							if (i>=list.size())
								i = list.size() - 1;
							if (i>=0)
								tableViewer.setSelection(new StructuredSelection(list.get(i)));
							tabbedPropertySheetPage.resizeScrolledComposite();
						}
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
						tableViewer.setSelection(new StructuredSelection(list.get(i-1)));
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
						tableViewer.setSelection(new StructuredSelection(list.get(i+1)));
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
		private EStructuralFeature feature;
		private EObject object;
		
		public TableColumn(EObject o, EAttribute a) {
			object = o;
			feature = a;
		}
		
		public void setTableViewer(TableViewer t) {
			tableViewer = t;
		}
		
		@Override
		public String getHeaderText() {
			return ModelUtil.toDisplayName(feature.getName());
		}

		@Override
		public String getProperty() {
			return feature.getName(); //$NON-NLS-1$
		}

		@Override
		public int getInitialWeight() {
			return 10;
		}

		public String getText(Object element) {
			Object value = ((EObject)element).eGet(feature);
			return value==null ? "" : value.toString();
		}
		
		public CellEditor createCellEditor (Composite parent) {			
			EClassifier ec = feature.getEType();
			if (ec instanceof EDataType) {
				return new EDataTypeCellEditor((EDataType)ec, parent);
			}
			return null;
		}
		
		public boolean canModify(Object element, String property) {
			return AbstractBpmn2TableComposite.this.canModify(object, feature, (EObject)element);
		}

		public void modify(Object element, String property, Object value) {
			final EObject target = (EObject)element;
			final Object newValue = value;
			final Object oldValue = target.eGet(feature); 
			if (oldValue==null || !oldValue.equals(value)) {
				TransactionalEditingDomain editingDomain = bpmn2Editor.getEditingDomain();
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
					@Override
					protected void doExecute() {
						target.eSet(feature, newValue);
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

}
