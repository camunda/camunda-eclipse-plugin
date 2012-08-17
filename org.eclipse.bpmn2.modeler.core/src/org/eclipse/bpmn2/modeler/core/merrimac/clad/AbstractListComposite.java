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

package org.eclipse.bpmn2.modeler.core.merrimac.clad;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.merrimac.IConstants;
import org.eclipse.bpmn2.modeler.core.merrimac.providers.TableCursor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
 

/**
 * @author Bob Brodt
 *
 */
public abstract class AbstractListComposite extends ListAndDetailCompositeBase implements INotifyChangedListener {
	
	public static final int HIDE_TITLE = 1 << 18; // Hide section title - useful if this is the only thing in the PropertySheetTab
	public static final int ADD_BUTTON = 1 << 19; // show "Add" button
	public static final int REMOVE_BUTTON = 1 << 20; // show "Remove" button
	public static final int MOVE_BUTTONS = 1 << 21; // show "Up" and "Down" buttons
	public static final int EDIT_BUTTON = 1 << 23; // show "Edit..." button
	public static final int SHOW_DETAILS = 1 << 24; // create a "Details" section
	public static final int DELETE_BUTTON = 1 << 25; // show "Delete" button - this uses EcoreUtil.delete() to kill the EObject
	public static final int DEFAULT_STYLE = (
			ADD_BUTTON|REMOVE_BUTTON|MOVE_BUTTONS|SHOW_DETAILS);
	public static final int READ_ONLY_STYLE = (
			ADD_BUTTON|REMOVE_BUTTON|MOVE_BUTTONS);
	
	public static final int CUSTOM_STYLES_MASK = (
			HIDE_TITLE|ADD_BUTTON|REMOVE_BUTTON|MOVE_BUTTONS|EDIT_BUTTON|SHOW_DETAILS);
	public static final int CUSTOM_BUTTONS_MASK = (
			ADD_BUTTON|REMOVE_BUTTON|MOVE_BUTTONS|EDIT_BUTTON);

	protected EStructuralFeature feature;
	
	// widgets
	SashForm sashForm;
	protected Section tableSection;
	protected Section detailSection;
	
	protected Table table;
	protected TableViewer tableViewer;
	protected AbstractDetailComposite detailComposite;
	
	boolean removeIsDelete = false;
	Action addAction;
	Action removeAction;
	Action upAction;
	Action downAction;
	Action editAction;
	
	protected ListCompositeColumnProvider columnProvider;
	protected ListCompositeContentProvider contentProvider;
	
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
	
	public EClass getListItemClass() {
		EClass eclass = getListItemClass(businessObject, feature);
		if (eclass==null)
			eclass = (EClass) feature.getEType();
		return eclass;
	}
	
	public EClass getDefaultListItemClass(EObject object, EStructuralFeature feature) {
		EClass lic = getListItemClass(object,feature);
		if (lic!=null)
			return lic;
		lic = (EClass) feature.getEType();
		EClass oc = object.eClass();
		if (oc.isInstance(lic))
			return oc;
		return lic;
	}
	
	/**
	 * Create a default ColumnTableProvider if none was set in setTableProvider();
	 * @param object
	 * @param feature
	 * @return
	 */
	public ListCompositeColumnProvider getColumnProvider(EObject object, EStructuralFeature feature) {
		if (columnProvider==null) {
			final EList<EObject> list = (EList<EObject>)object.eGet(feature);
			final EClass listItemClass = getDefaultListItemClass(object, feature);

			boolean canModify;
			if (style==READ_ONLY_STYLE)
				canModify = false;
			else
				canModify = ((style & SHOW_DETAILS)==0 && (style & EDIT_BUTTON)==0)
						|| ((style & SHOW_DETAILS)!=0 && (style & EDIT_BUTTON)!=0);
			columnProvider = new ListCompositeColumnProvider(this, canModify);
			
			// default is to include property name as the first column
			EStructuralFeature nameAttribute = listItemClass.getEStructuralFeature("name");
			if (nameAttribute!=null)
				columnProvider.add(object, listItemClass, nameAttribute);

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
										columnProvider.add(object, listItemClass, f1);
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
						columnProvider.add(object, listItemClass, a1);
				}
				else {
					if (a1!=nameAttribute)
						columnProvider.add(object, listItemClass, a1);
				}
			}
			if (columnProvider.getColumns().size()==0) {
				feature = listItemClass.getEStructuralFeature("id");
				columnProvider.addRaw(object, feature);
			}
		}
		return columnProvider;
	}

	public ListCompositeColumnProvider getColumnProvider() {
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
	
	public ListCompositeContentProvider getContentProvider(EObject object, EStructuralFeature feature, EList<EObject>list) {
		if (contentProvider==null)
			contentProvider = new ListCompositeContentProvider(this, object, feature, list);
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

		setBusinessObject(theobject);
		this.feature = thefeature;
		final EList<EObject> list = (EList<EObject>)businessObject.eGet(feature);
		final EClass listItemClass = getDefaultListItemClass(businessObject,feature);
		final String label = ModelUtil.getLabel(listItemClass);
		final String prefName = "list."+listItemClass.getName()+".expanded";
		
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
			
			tableSection = createListSection(sashForm,label);
			
			if ((style & SHOW_DETAILS)!=0) {
				detailSection = createDetailSection(sashForm, label);
				detailSection.addExpansionListener(new IExpansionListener() {
					
					@Override
					public void expansionStateChanging(ExpansionEvent e) {
						if (!e.getState()) {
							detailSection.setVisible(false);
							if (editAction!=null)
								editAction.setChecked(false);
						}
					}
	
					@Override
					public void expansionStateChanged(ExpansionEvent e) {
						redrawPage();
					}
				});
				
				tableSection.addExpansionListener(new IExpansionListener() {
	
					@Override
					public void expansionStateChanging(ExpansionEvent e) {
						if (!e.getState() && detailSection!=null) {
							detailSection.setVisible(false);
						}
					}
	
					@Override
					public void expansionStateChanged(ExpansionEvent e) {
						preferenceStore.setValue(prefName, e.getState());
						redrawPage();
					}
				});
			
				sashForm.setWeights(new int[] { 1, 2 });
			}					
			else
				sashForm.setWeights(new int[] { 1 });
		}
		else {
			tableSection = createListSection(sashForm,label);
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
				if ((style & SHOW_DETAILS)!=0) {
					if (detailSection!=null && detailSection.isVisible())
						showDetails(true);
					else if ((style & EDIT_BUTTON)==0)
						showDetails(true);
				}
				if (removeAction!=null)
					removeAction.setEnabled(enable);
				if (editAction!=null)
					editAction.setEnabled(enable);
				if (upAction!=null && downAction!=null) {
					int i = table.getSelectionIndex();
					if (i>0)
						upAction.setEnabled(true);
					else
						upAction.setEnabled(false);
					if (i>=0 && i<table.getItemCount()-1)
						downAction.setEnabled(true);
					else
						downAction.setEnabled(false);
				}
			}
		});
		
		tableViewer.setInput(list);
		
		// a TableCursor allows navigation of the table with keys
		TableCursor.create(table, tableViewer);
		redrawPage();
		
		boolean expanded = preferenceStore.getBoolean(prefName);
		if (expanded && tableSection!=null)
			tableSection.setExpanded(true);
	}
	
	private void showDetails(boolean enable) {
		if (detailSection!=null) {
			if (enable) {
	
				IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
				if (selection.getFirstElement() instanceof EObject) {
					EObject o = (EObject)selection.getFirstElement();
					
					if (detailComposite!=null)
						detailComposite.dispose();
					detailComposite = createDetailComposite(detailSection, o.eClass().getInstanceClass());
					detailSection.setClient(detailComposite);
					toolkit.adapt(detailComposite);
	
	//				if (detailSection.getText().isEmpty())
						detailSection.setText(ModelUtil.getLabel(o)+" Details");
					((AbstractDetailComposite)detailComposite).setBusinessObject(o);
					enable = detailComposite.getChildren().length>0;
					tableSection.setExpanded(true);
				}
			}
			detailSection.setVisible(enable);
			detailSection.setExpanded(enable);
			if (editAction!=null)
				editAction.setChecked(enable);

			final EList<EObject> list = (EList<EObject>)businessObject.eGet(feature);
			tableViewer.setInput(list);

			redrawPage();
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
						if (!isModelObjectEnabled(listItemClass, tc.feature)) {
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

	private Section createListSection(Composite parent, String label) {
		Section section = toolkit.createSection(parent,
				ExpandableComposite.TWISTIE |
				ExpandableComposite.COMPACT |
				ExpandableComposite.TITLE_BAR);
		section.setText(label+" List");

		Composite tableComposite = toolkit.createComposite(section, SWT.NONE);
		section.setClient(tableComposite);
		tableComposite.setLayout(new GridLayout(1, false));
		
		table = toolkit.createTable(tableComposite, SWT.FULL_SELECTION | SWT.V_SCROLL);
		GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
		gridData.widthHint = 100;
		gridData.heightHint = 100;
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		
	    ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
	    ToolBar toolbar = toolBarManager.createControl(section);

	    ImageDescriptor id = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/20/add.png");
		if ((style & ADD_BUTTON)!=0) {
			addAction = new Action("Add", id) {
				@Override
				public void run() {
					super.run();
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						@Override
						protected void doExecute() {
							EObject newItem = addListItem(businessObject,feature);
							if (newItem!=null) {
								final EList<EObject> list = (EList<EObject>)businessObject.eGet(feature);
								tableViewer.setInput(list);
								tableViewer.setSelection(new StructuredSelection(newItem));
								showDetails(true);
							}
						}
					});
				}
			};
			toolBarManager.add(addAction);
		}
		
		if ((style & DELETE_BUTTON)!=0 || (style & REMOVE_BUTTON)!=0) {
			
			if ((style & DELETE_BUTTON)!=0) {
				removeIsDelete = true;
				id = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/20/delete.png");
			}
			else {
				id = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/20/remove.png");
			}
			removeAction = new Action(removeIsDelete ? "Delete" : "Remove", id) {
				@Override
				public void run() {
					super.run();
					showDetails(false);
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
						@Override
						protected void doExecute() {
							int i = table.getSelectionIndex();
							Object item;
							if (removeIsDelete)
								item = deleteListItem(businessObject,feature,i);
							else
								item = removeListItem(businessObject,feature,i);
							
							final EList<EObject> list = (EList<EObject>)businessObject.eGet(feature);
							tableViewer.setInput(list);
							if (item!=null) {
								if (i>=list.size())
									i = list.size() - 1;
								if (i>=0)
									tableViewer.setSelection(new StructuredSelection(item));
							}
						}
					});
				}
			};
			toolBarManager.add(removeAction);
			removeAction.setEnabled(false);
		}
		
		if ((style & MOVE_BUTTONS)!=0) {
			id = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/20/up.png");
			upAction = new Action("Move Up", id) {
				@Override
				public void run() {
					super.run();
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
			};
			toolBarManager.add(upAction);
			upAction.setEnabled(false);
	
			id = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/20/down.png");
			downAction = new Action("Move Down", id) {
				@Override
				public void run() {
					super.run();
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
			};
			toolBarManager.add(downAction);
			downAction.setEnabled(false);
		}
		
		if ((style & EDIT_BUTTON)!=0) {
			id = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/20/edit.png");
			editAction = new Action("Edit", id) {
				@Override
				public void run() {
					super.run();
					if ((style & SHOW_DETAILS)!=0) {
						if (!editAction.isChecked()) {
							showDetails(true);
						}
						else {
							showDetails(false);
						}
					}
					else {
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
				}
			};
			toolBarManager.add(editAction);
			editAction.setEnabled(false);
		}
		
	    toolBarManager.update(true);
	    section.setTextClient(toolbar);
	    
	    // hook a resource change listener to this Table Control
	    table.setData(IConstants.NOTIFY_CHANGE_LISTENER_KEY,this);
	    
	    return section;
	}
	
	protected Section createDetailSection(Composite parent, String label) {
		Section section = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED |
				ExpandableComposite.TITLE_BAR);
		section.setText(label+" Details");
		section.setVisible(false);

	    ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
	    ToolBar toolbar = toolBarManager.createControl(section);
	    ImageDescriptor id = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/20/close.png");
	    toolBarManager.add( new Action("Close", id) {
			@Override
			public void run() {
				super.run();
				showDetails(false);
			}
	    });
	    toolBarManager.update(true);
	    section.setTextClient(toolbar);
	    return section;
	}

	@Override
	public void notifyChanged(Notification notification) {
		if (this.businessObject == notification.getNotifier() &&
				this.feature == notification.getFeature()) {
			final EList<EObject> list = (EList<EObject>)businessObject.eGet(feature);
			tableViewer.setInput(list);
		}
	}
}
