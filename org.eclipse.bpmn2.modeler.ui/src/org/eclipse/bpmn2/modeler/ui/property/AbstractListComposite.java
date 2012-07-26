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

import org.eclipse.bpmn2.modeler.core.utils.ErrorUtils;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.providers.ColumnTableProvider;
import org.eclipse.bpmn2.modeler.ui.property.providers.TableCursor;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
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
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor.EDataTypeCellEditor;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
 

/**
 * @author Bob Brodt
 *
 */
public abstract class AbstractListComposite extends ListAndDetailCompositeBase {
	
	public static final int HIDE_TITLE = 1 << 18; // Hide section title - useful if this is the only thing in the PropertySheetTab
	public static final int ADD_BUTTON = 1 << 19; // show "Add" button
	public static final int REMOVE_BUTTON = 1 << 20; // show "Remove" button
	public static final int MOVE_BUTTONS = 1 << 21; // show "Up" and "Down" buttons
	public static final int EDIT_BUTTON = 1 << 23; // show "Edit..." button
	public static final int SHOW_DETAILS = 1 << 24; // create a "Details" section
	public static final int DELETE_BUTTON = 1 << 25; // show "Delete" button - this uses EcoreUtil.delete() to kill the EObject
	public static final int DEFAULT_STYLE = (
			ADD_BUTTON|REMOVE_BUTTON|MOVE_BUTTONS|SHOW_DETAILS);
	
	public static final int CUSTOM_STYLES_MASK = (
			HIDE_TITLE|ADD_BUTTON|REMOVE_BUTTON|MOVE_BUTTONS|EDIT_BUTTON|SHOW_DETAILS);
	public static final int CUSTOM_BUTTONS_MASK = (
			ADD_BUTTON|REMOVE_BUTTON|MOVE_BUTTONS|EDIT_BUTTON);

//	protected BPMN2Editor bpmn2Editor;

	protected EStructuralFeature feature;
	
	// widgets
	SashForm sashForm;
	Section tableSection;
	protected Section detailSection;
	
	Table table;
	TableViewer tableViewer;
	
	Composite tableAndButtonsComposite;
	Composite buttonsComposite;
	Composite tableComposite;
	AbstractDetailComposite detailComposite;
	
	boolean removeIsDelete = false;
	Button addButton;
	Button removeButton;
	Button upButton;
	Button downButton;
	Button editButton;
	
	protected AbstractTableColumnProvider columnProvider;
	protected TableContentProvider contentProvider;
	
	public AbstractListComposite(AbstractBpmn2PropertySection section) {
		this(section,DEFAULT_STYLE);
	}
	
	public AbstractListComposite(AbstractBpmn2PropertySection section, int style) {
		super(section.getSectionRoot(), style & ~CUSTOM_STYLES_MASK);
		this.style = style;
	}
	
	public AbstractListComposite(final Composite parent, int style) {
		super(parent, style & ~CUSTOM_STYLES_MASK);
		this.style = style;
	}

	abstract public void setListItemClass(EClass clazz);
	
	abstract public EClass getListItemClass(EObject object, EStructuralFeature feature);
	
	public EClass getDefaultListItemClass(EObject object, EStructuralFeature feature) {
		EClass lic = getListItemClass(object,feature);
		if (lic!=null)
			return lic;
		return (EClass) feature.getEType();
	}
	
	/**
	 * Create a default ColumnTableProvider if none was set in setTableProvider();
	 * @param object
	 * @param feature
	 * @return
	 */
	public AbstractTableColumnProvider getColumnProvider(EObject object, EStructuralFeature feature) {
		if (columnProvider==null) {
			final EList<EObject> list = (EList<EObject>)object.eGet(feature);
			final EClass listItemClass = getDefaultListItemClass(object, feature);

			columnProvider = new AbstractTableColumnProvider() {
				@Override
				public boolean canModify(EObject object, EStructuralFeature feature, EObject item) {
					return false;
				}
			};
			
			// default is to include property name
			EStructuralFeature nameAttribute = listItemClass.getEStructuralFeature("name");
			if (nameAttribute!=null)
				columnProvider.add(new TableColumn(object, nameAttribute));

			for (EAttribute a1 : listItemClass.getEAllAttributes()) {
				if ("anyAttribute".equals(a1.getName())) {
					List<EStructuralFeature> anyAttributes = new ArrayList<EStructuralFeature>();
					// are there any actual "anyAttribute" instances we can look at
					// to get the feature names and types from?
					// TODO: enhance the table to dynamically allow creation of new
					// columns which will be added to the "anyAttributes"
					for (EObject instance : list) {
						if (listItemClass.isInstance(instance)) {
							Object o = instance.eGet(a1);
							if (o instanceof BasicFeatureMap) {
								BasicFeatureMap map = (BasicFeatureMap)o;
								for (Entry entry : map) {
									EStructuralFeature f1 = entry.getEStructuralFeature();
									if (f1 instanceof EAttribute && !anyAttributes.contains(f1)) {
										columnProvider.add(new TableColumn(object,(EAttribute)f1));
										anyAttributes.add(f1);
									}
								}
							}
						}
					}
				}
				else if (FeatureMap.Entry.class.equals(a1.getEType().getInstanceClass())) {
					// TODO: how do we handle these?
					if (a1 instanceof EAttribute)
						columnProvider.add(new TableColumn(object,a1));
				}
				else {
					if (a1!=nameAttribute)
						columnProvider.add(new TableColumn(object,a1));
				}
			}
		}
		return columnProvider;
	}
	
	/**
	 * Override this to create your own Details section. This composite will be displayed
	 * in a twistie section whenever the user selects an item from the table. The section
	 * is automatically hidden when the table is collapsed.
	 * 
	 * @param parent
	 * @param eClass
	 * @return
	 */
	abstract public AbstractDetailComposite createDetailComposite(Composite parent, Class eClass);
	
	public TableContentProvider getContentProvider(EObject object, EStructuralFeature feature, EList<EObject>list) {
		if (contentProvider==null)
			contentProvider = new TableContentProvider(object, feature, list);
		return contentProvider;
	}
	
	/**
	 * Add a new list item. 
	 * @param object
	 * @param feature
	 * @return the new item to be added to the list, or null if item creation failed
	 */
	abstract protected EObject addListItem(EObject object, EStructuralFeature feature);

	/**
	 * Edit the currently selected list item. 
	 * @param object
	 * @param feature
	 * @return the selected item if edit was successful, null if not
	 */
	abstract protected EObject editListItem(EObject object, EStructuralFeature feature);
	
	/**
	 * Remove a list item (does not delete it from the model.) 
	 * @param object
	 * @param feature
	 * @param item
	 * @return the item that follows the one removed, or null if the removed item was at the bottom of the list
	 */
	abstract protected Object removeListItem(EObject object, EStructuralFeature feature, int index);
	
	/**
	 * Remove an item from the list and delete it from model. 
	 * @param object
	 * @param feature
	 * @param index
	 * @return the item that follows the one deleted, or null if the deleted item was at the bottom of the list
	 */
	abstract protected Object deleteListItem(EObject object, EStructuralFeature feature, int index);
	
	/**
	 * Move the currently selected item up in the list.
	 * @param object
	 * @param feature
	 * @param index
	 * @return the selected item if it was moved, null if the item is already at the top of the list.
	 */
	abstract protected Object moveListItemUp(EObject object, EStructuralFeature feature, int index);

	/**
	 * Move the currently selected item down in the list.
	 * @param object
	 * @param feature
	 * @param index
	 * @return the selected item if it was moved, null if the item is already at the bottom of the list.
	 */
	abstract protected Object moveListItemDown(EObject object, EStructuralFeature feature, int index);

	protected int[] buildIndexMap(EObject object, EStructuralFeature feature) {
		EList<EObject> list = (EList<EObject>)object.eGet(feature);
		EClass listItemClass = getListItemClass(object,feature);
		int[] map = null;
		if (listItemClass!=null) {
			int[] tempMap = new int[list.size()];
			int index = 0;
			int realIndex = 0;
			for (EObject o : list) {
				EClass ec = o.eClass();
				if (ec == listItemClass) {
					tempMap[index] = realIndex;
					++index;
				}
				++realIndex;
			}
			map = new int[index];
			for (int i=0; i<index; ++i)
				map[i] = tempMap[i];
		}
		else {
			map = new int[list.size()];
			for (int i=0; i<map.length; ++i)
				map[i] = i;
		}
		return map;
	}
	
	public void setTitle(String title) {
		if (tableSection!=null)
			tableSection.setText(title);
	}
	
	public void bindList(final EObject theobject, final EStructuralFeature thefeature) {
		if (!(theobject.eGet(thefeature) instanceof EList<?>)) {
			return;
		}
//		Class<?> clazz = thefeature.getEType().getInstanceClass();
//		if (!EObject.class.isAssignableFrom(clazz)) {
//			return;
//		}

		final BPMN2Editor bpmn2Editor = getDiagramEditor();
		setBusinessObject(theobject);
		this.feature = thefeature;
		final EList<EObject> list = (EList<EObject>)businessObject.eGet(feature);
		final EClass listItemClass = getDefaultListItemClass(businessObject,feature);
		final String label = PropertyUtil.getLabel(listItemClass);
		
		////////////////////////////////////////////////////////////
		// Collect columns to be displayed and build column provider
		////////////////////////////////////////////////////////////
		if (createColumnProvider(businessObject, feature) <= 0)
			return;

		////////////////////////////////////////////////////////////
		// SashForm contains the table section and a possible
		// details section
		////////////////////////////////////////////////////////////
		if ((style & HIDE_TITLE)==0 || (style & SHOW_DETAILS)!=0) {
			// display title in the table section and/or show a details section
			// SHOW_DETAILS forces drawing of a section title
			sashForm = new SashForm(this, SWT.NONE);
			sashForm.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
			
			tableSection = toolkit.createSection(sashForm,
					ExpandableComposite.TWISTIE |
					ExpandableComposite.COMPACT |
					ExpandableComposite.TITLE_BAR);
			tableSection.setText(label+" List");

			tableComposite = toolkit.createComposite(tableSection, SWT.NONE);
			tableSection.setClient(tableComposite);
			tableComposite.setLayout(new GridLayout(3, false));
			createTableAndButtons(tableComposite,style);
			
			if ((style & SHOW_DETAILS)!=0) {
				detailSection = toolkit.createSection(sashForm,
						ExpandableComposite.TWISTIE |
						ExpandableComposite.EXPANDED |
						ExpandableComposite.TITLE_BAR);
				detailSection.setText(label+" Details");

				detailSection.setVisible(false);

				tableSection.addExpansionListener(new IExpansionListener() {
	
					@Override
					public void expansionStateChanging(ExpansionEvent e) {
						if (!e.getState() && detailSection!=null) {
							detailSection.setVisible(false);
						}
					}
	
					@Override
					public void expansionStateChanged(ExpansionEvent e) {
						preferenceStore.setValue("table."+listItemClass.getName()+".expanded", e.getState());
						redrawPage();
					}
					
				});
			
				detailSection.addExpansionListener(new IExpansionListener() {
	
					@Override
					public void expansionStateChanging(ExpansionEvent e) {
						if (!e.getState()) {
							detailSection.setVisible(false);
						}
					}
	
					@Override
					public void expansionStateChanged(ExpansionEvent e) {
						redrawPage();
					}
				});
				sashForm.setWeights(new int[] { 1, 2 });
			}					
			else
				sashForm.setWeights(new int[] { 1 });
		}
		else {
			createTableAndButtons(this,style);
		}
		
		////////////////////////////////////////////////////////////
		// Create table viewer and cell editors
		////////////////////////////////////////////////////////////
		tableViewer = new TableViewer(table);
		columnProvider.createTableLayout(table);
		columnProvider.setTableViewer(tableViewer);
		
		tableViewer.setLabelProvider(columnProvider);
		tableViewer.setCellModifier(columnProvider);
		tableViewer.setContentProvider(getContentProvider(businessObject,feature,list));
		tableViewer.setColumnProperties(columnProvider.getColumnProperties());
		tableViewer.setCellEditors(columnProvider.createCellEditors(table));

		////////////////////////////////////////////////////////////
		// Create handlers
		////////////////////////////////////////////////////////////
		tableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				boolean enable = !event.getSelection().isEmpty();
				if (detailSection!=null) {
					if (enable) {
						IStructuredSelection sel = (IStructuredSelection) event.getSelection();

						if (sel.getFirstElement() instanceof EObject) {
							EObject o = (EObject)sel.getFirstElement();
							
							if (detailComposite!=null)
								detailComposite.dispose();
							detailComposite = createDetailComposite(detailSection, o.eClass().getInstanceClass());
							detailSection.setClient(detailComposite);
							toolkit.adapt(detailComposite);

//							if (detailSection.getText().isEmpty())
								detailSection.setText(PropertyUtil.getLabel(o)+" Details");
							((AbstractDetailComposite)detailComposite).setBusinessObject(o);
							enable = detailComposite.getChildren().length>0;
						}
					}
					detailSection.setVisible(enable);
					detailSection.setExpanded(enable);
					PropertyUtil.recursivelayout(detailSection);
//					sashForm.layout(true);
//					redrawPage();
				}
				if (removeButton!=null)
					removeButton.setEnabled(enable);
				if (editButton!=null)
					editButton.setEnabled(enable);
				if (upButton!=null && downButton!=null) {
					int i = table.getSelectionIndex();
					if (i>0)
						upButton.setEnabled(true);
					else
						upButton.setEnabled(false);
					if (i>=0 && i<table.getItemCount()-1)
						downButton.setEnabled(true);
					else
						downButton.setEnabled(false);
				}
			}
		});
		
		if (addButton!=null) {
			addButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						@Override
						protected void doExecute() {
							EObject newItem = addListItem(businessObject,feature);
							if (newItem!=null) {
								final EList<EObject> list = (EList<EObject>)businessObject.eGet(feature);
								tableViewer.setInput(list);
								tableViewer.setSelection(new StructuredSelection(newItem));
							}
						}
					});
				}
			});
		}

		if (removeButton!=null) {
			removeButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						@Override
						protected void doExecute() {
							int i = table.getSelectionIndex();
							Object item;
							if (removeIsDelete)
								item = deleteListItem(businessObject,feature,i);
							else
								item = removeListItem(businessObject,feature,i);
							
							if (item!=null) {
								final EList<EObject> list = (EList<EObject>)businessObject.eGet(feature);
								tableViewer.setInput(list);
								if (i>=list.size())
									i = list.size() - 1;
								if (i>=0)
									tableViewer.setSelection(new StructuredSelection(item));
							}
						}
					});
				}
			});
		}

		if (upButton!=null && downButton!=null) {
			upButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						@Override
						protected void doExecute() {
							int i = table.getSelectionIndex();
							Object item = moveListItemUp(businessObject,feature,i);
							final EList<EObject> list = (EList<EObject>)businessObject.eGet(feature);
							tableViewer.setInput(list);
							tableViewer.setSelection(new StructuredSelection(item));
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
							Object item = moveListItemDown(businessObject,feature,i);
							final EList<EObject> list = (EList<EObject>)businessObject.eGet(feature);
							tableViewer.setInput(list);
							tableViewer.setSelection(new StructuredSelection(item));
						}
					});
				}
			});
		}

		if (editButton!=null) {
			editButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						@Override
						protected void doExecute() {
							EObject newItem = editListItem(businessObject,feature);
							if (newItem!=null) {
								final EList<EObject> list = (EList<EObject>)businessObject.eGet(feature);
								tableViewer.setInput(list);
								tableViewer.setSelection(new StructuredSelection(newItem));
							}
						}
					});
				}
			});
		}
		
		tableViewer.setInput(list);
		
		// a TableCursor allows navigation of the table with keys
		TableCursor.create(table, tableViewer);
		redrawPage();
		
		boolean expanded = preferenceStore.getBoolean("table."+listItemClass.getName()+".expanded");
		if (expanded && tableSection!=null)
			tableSection.setExpanded(true);
	}
	
	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event) {
		List<Notification> notifications = event.getNotifications();
		try {
			final EList<EObject> list = (EList<EObject>)businessObject.eGet(feature);
			for (Notification notification : notifications) {
				tableViewer.setInput(list);
			}
		}
		catch (Exception e) {
			// silently ignore :-o
		}
	}
	
	/**
	 * @param theobject
	 * @param thefeature
	 * @return
	 */
	protected int createColumnProvider(EObject theobject, EStructuralFeature thefeature) {
		if (columnProvider==null) {
			EClass listItemClass = getDefaultListItemClass(theobject,thefeature);
			columnProvider = getColumnProvider(theobject, thefeature);
			// remove disabled columns
			List<TableColumn> removed = new ArrayList<TableColumn>();
			for (TableColumn tc : (List<TableColumn>)columnProvider.getColumns()) {
				if (tc.feature!=null) {
					if (!"id".equals(tc.feature.getName())) {
						if (!modelEnablement.isEnabled(listItemClass, tc.feature)) {
							removed.add(tc);
						}
					}
				}
			}
			if (removed.size()>0) {
				for (TableColumn tc : removed)
					columnProvider.remove(tc);
			}
		}
		return columnProvider.getColumns().size();
	}

	protected void redrawPage() {
		if (propertySection!=null)
			propertySection.layout();
		else {
			PropertyUtil.recursivelayout(this);
			getParent().layout();
		}
	}

	private void createTableAndButtons(Composite parent, int style) {

		GridData gridData;
		
		////////////////////////////////////////////////////////////
		// Create a composite to hold the buttons and table
		////////////////////////////////////////////////////////////
		tableAndButtonsComposite = toolkit.createComposite(parent, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, true, 3, 1);
		tableAndButtonsComposite.setLayoutData(gridData);
		tableAndButtonsComposite.setLayout(new GridLayout(2, false));
		
		////////////////////////////////////////////////////////////
		// Create button section for add/remove/up/down buttons
		////////////////////////////////////////////////////////////
		buttonsComposite = toolkit.createComposite(tableAndButtonsComposite);
		buttonsComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		buttonsComposite.setLayout(new FillLayout(SWT.VERTICAL));

		////////////////////////////////////////////////////////////
		// Create table
		// allow table to fill entire width if there are no buttons
		////////////////////////////////////////////////////////////
		int span = 2;
		if ((style & CUSTOM_BUTTONS_MASK)!=0) {
			span = 1;
		}
		else {
			buttonsComposite.setVisible(false);
		}
		table = toolkit.createTable(tableAndButtonsComposite, SWT.FULL_SELECTION | SWT.V_SCROLL);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, true, span, 1);
		gridData.widthHint = 100;
		gridData.heightHint = 100;
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		////////////////////////////////////////////////////////////
		// Create buttons for add/remove/up/down
		////////////////////////////////////////////////////////////
		if ((style & ADD_BUTTON)!=0) {
			addButton = toolkit.createButton(buttonsComposite, "Add", SWT.PUSH);
		}

		if ((style & DELETE_BUTTON)!=0) {
			removeIsDelete = true;
			removeButton = toolkit.createButton(buttonsComposite, "Delete", SWT.PUSH);
			removeButton.setEnabled(false);
		}
		else if ((style & REMOVE_BUTTON)!=0) {
			removeButton = toolkit.createButton(buttonsComposite, "Remove", SWT.PUSH);
			removeButton.setEnabled(false);
		}
		
		if ((style & MOVE_BUTTONS)!=0) {
			upButton = toolkit.createButton(buttonsComposite, "Up", SWT.PUSH);
			upButton.setEnabled(false);
	
			downButton = toolkit.createButton(buttonsComposite, "Down", SWT.PUSH);
			downButton.setEnabled(false);
		}
		
		if ((style & EDIT_BUTTON)!=0) {
			editButton = toolkit.createButton(buttonsComposite, "Edit...", SWT.PUSH);
			editButton.setEnabled(false);
		}

		
	}
	
	public class TableContentProvider implements IStructuredContentProvider {
		protected EObject object;
		protected EStructuralFeature feature;
		protected EList<EObject> list;
		
		public TableContentProvider(EObject object, EStructuralFeature feature, EList<EObject> list) {
			this.object = object;
			this.feature = feature;
			this.list = list;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			EClass listItemClass = getListItemClass(object,feature);
			if (listItemClass==null) {
				// display all items in the list that are subclasses of listItemClass
				return list.toArray();
			}
			else {
				// we're only interested in display specific EClass instances
				List<EObject> elements = new ArrayList<EObject>();
				for (EObject o : list) {
					EClass ec = o.eClass();
					if (ec == listItemClass)
						elements.add(o);
				}
				return elements.toArray(new EObject[elements.size()]);
			}
		}
	}
	
	public class TableColumn extends ColumnTableProvider.Column implements ILabelProvider, ICellModifier {
		protected TableViewer tableViewer;
		protected EStructuralFeature feature;
		protected EObject object;
		
		public TableColumn(EObject o, EStructuralFeature f) {
			object = o;
			feature = f;
		}
		
		public void setTableViewer(TableViewer t) {
			tableViewer = t;
		}
		
		@Override
		public String getHeaderText() {
			if (feature!=null) {
				if (feature.eContainer() instanceof EClass) {
					return PropertyUtil.getLabel(feature.eContainer(), feature);
				}
				return ModelUtil.toDisplayName(feature.getName());
			}
			return "";
		}

		@Override
		public String getProperty() {
			if (feature!=null)
				return feature.getName(); //$NON-NLS-1$
			return "";
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
			if (feature!=null) {
				EClassifier ec = feature.getEType();
				if (ec instanceof EDataType) {
					return new EDataTypeCellEditor((EDataType)ec, parent);
				}
			}
			return null;
		}
		
		public boolean canModify(Object element, String property) {
			return columnProvider.canModify(object, feature, (EObject)element);
		}

		public void modify(Object element, String property, Object value) {
			BPMN2Editor bpmn2Editor = getDiagramEditor();
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
					ErrorUtils.showErrorMessage(bpmn2Editor.getDiagnostics().getMessage());
				}
				else
					ErrorUtils.showErrorMessage(null);
				tableViewer.refresh();
			}
		}

		@Override
		public Object getValue(Object element, String property) {
			return getText(element);
		}
	}

	public abstract class AbstractTableColumnProvider extends ColumnTableProvider {
		
		/**
		 * Implement this to select which columns are editable
		 * @param object - the list object
		 * @param feature - the feature of the item contained in the list
		 * @param item - the selected item in the list
		 * @return true to allow editing
		 */
		public abstract boolean canModify(EObject object, EStructuralFeature feature, EObject item);
	}
}
