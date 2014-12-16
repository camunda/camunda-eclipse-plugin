package org.camunda.bpm.modeler.ui.property.tabs.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.modeler.runtime.engine.model.EntryType;
import org.camunda.bpm.modeler.runtime.engine.model.ListType;
import org.camunda.bpm.modeler.runtime.engine.model.MapType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.runtime.engine.model.ParameterType;
import org.camunda.bpm.modeler.runtime.engine.model.ScriptType;
import org.camunda.bpm.modeler.ui.property.tabs.binding.StringTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ValidatingStringComboBinding;
import org.camunda.bpm.modeler.ui.property.tabs.binding.ValidatingStringTextBinding;
import org.camunda.bpm.modeler.ui.property.tabs.builder.CamundaScriptPropertiesBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.AbstractDeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.ContentProvider;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EObjectTableBuilder.DeleteRowHandler;
import org.camunda.bpm.modeler.ui.property.tabs.builder.table.EditableEObjectTableBuilder;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EObjectAttributeEditingSupport;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EObjectAttributeTableColumnDescriptor;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EObjectAttributeTableColumnDescriptor.EditingSupportProvider;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.camunda.bpm.modeler.ui.property.tabs.tables.TypedColumnLabelProvider;
import org.camunda.bpm.modeler.ui.property.tabs.util.Events;
import org.camunda.bpm.modeler.ui.property.tabs.util.Events.RowDeleted;
import org.camunda.bpm.modeler.ui.property.tabs.util.HelpText;
import org.camunda.bpm.modeler.ui.property.tabs.util.PropertyUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl.BasicFeatureMapEntry;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Roman Smirnov
 */
public class ParameterDialog extends ModelerDialog {

	protected static final int SHELL_WIDTH = 660;
	protected static final int SHELL_HEIGHT = 400;
	protected static final int TABLE_HEIGHT = 150;
	
	protected static final String EMPTY_STRING = "";
	
	protected static final String DIALOG_LABEL = "Parameter Details";
	protected static final String PARAMETER_NAME_LABEL = "Name";
	protected static final String PARAMETER_TYPE_LABEL = "Type";
	protected static final String PARAMETER_TEXT_TYPE_LABEL = "Text";
	protected static final String PARAMETER_MAP_TYPE_LABEL = "Map";
	protected static final String PARAMETER_LIST_TYPE_LABEL = "List";
	protected static final String PARAMETER_SCRIPT_TYPE_LABEL = "Script";
	
	protected static final String COMPLEX_CONTENT = "<< %s >>";
	protected static final String COMPLEX_CONTENT_MAP = String.format(COMPLEX_CONTENT, PARAMETER_MAP_TYPE_LABEL);
	protected static final String COMPLEX_CONTENT_LIST = String.format(COMPLEX_CONTENT, PARAMETER_LIST_TYPE_LABEL);
	protected static final String COMPLEX_CONTENT_SCRIPT = String.format(COMPLEX_CONTENT, PARAMETER_SCRIPT_TYPE_LABEL);
	
	protected static final String TABLE_NOTE = HelpText.TABLE_HELP + " " + HelpText.TABLE_ELEM_NOT_EDITABLE;
	
	protected static final String[] SUPPORTED_TYPES = new String[] {
		 PARAMETER_TEXT_TYPE_LABEL,
		 PARAMETER_MAP_TYPE_LABEL,
		 PARAMETER_LIST_TYPE_LABEL,
		 PARAMETER_SCRIPT_TYPE_LABEL
	};
	
	protected static final String PARAMETER_TEXT_CONTENT_LABEL = "Content";
	
	protected static final String PARAMETER_MAP_TABLE_LABEL = "Entries";
	
	protected static final String[] PARAMETER_MAP_TABLE_HEADERS = { "key", "value" };
	
	protected static final EStructuralFeature ENTRY_KEY_FEATURE = ModelPackage.eINSTANCE.getEntryType_Key();
	protected static final EStructuralFeature ENTRY_MIXED_FEATURE = ModelPackage.eINSTANCE.getEntryType_Mixed();
	protected static final EStructuralFeature ENTRY_TEXT_FEATURE = ModelPackage.eINSTANCE.getEntryType_Text();
	protected static final EStructuralFeature ENTRY_MAP_FEATURE = ModelPackage.eINSTANCE.getEntryType_Map();
	protected static final EStructuralFeature ENTRY_LIST_FEATURE = ModelPackage.eINSTANCE.getEntryType_List();
	protected static final EStructuralFeature ENTRY_SCRIPT_FEATURE = ModelPackage.eINSTANCE.getEntryType_Script();
	
	protected static final EStructuralFeature[] PARAMETER_MAP_TABLE_FEATURES = {
		ENTRY_KEY_FEATURE,
		ENTRY_MIXED_FEATURE
	};
	
	protected static final String PARAMETER_LIST_TABLE_LABEL = "Values";
	
	protected static final String[] PARAMETER_LIST_TABLE_HEADERS = { "value" };
	
	protected static final EStructuralFeature PARAMETER_NAME_FEATURE = ModelPackage.eINSTANCE.getParameterType_Name();
	protected static final EStructuralFeature PARAMETER_MIXED_FEATURE = ModelPackage.eINSTANCE.getParameterType_Mixed();
	protected static final EStructuralFeature PARAMETER_TEXT_FEATURE = ModelPackage.eINSTANCE.getParameterType_Text();
	protected static final EStructuralFeature PARAMETER_MAP_FEATURE = ModelPackage.eINSTANCE.getParameterType_Map();
	protected static final EStructuralFeature PARAMETER_LIST_FEATURE = ModelPackage.eINSTANCE.getParameterType_List();
	protected static final EStructuralFeature PARAMETER_SCRIPT_FEATURE = ModelPackage.eINSTANCE.getParameterType_Script();

	protected static final EStructuralFeature LIST_GROUP_FEATURE = ModelPackage.eINSTANCE.getListType_Group();
	
	protected static final EStructuralFeature[] PARAMETER_LIST_TABLE_FEATURES = {
		LIST_GROUP_FEATURE
	};
	
	protected static final Map<String, EStructuralFeature> PARAMETER_TYPE_FEATURE_MAP = new HashMap<String, EStructuralFeature>();
	static {
		PARAMETER_TYPE_FEATURE_MAP.put(PARAMETER_TEXT_TYPE_LABEL, PARAMETER_TEXT_FEATURE);
		PARAMETER_TYPE_FEATURE_MAP.put(PARAMETER_MAP_TYPE_LABEL, PARAMETER_MAP_FEATURE);
		PARAMETER_TYPE_FEATURE_MAP.put(PARAMETER_LIST_TYPE_LABEL, PARAMETER_LIST_FEATURE);
		PARAMETER_TYPE_FEATURE_MAP.put(PARAMETER_SCRIPT_TYPE_LABEL, PARAMETER_SCRIPT_FEATURE);
	}
	
	protected BaseElement bo;
	protected ParameterType parameter;
	
	protected GFPropertySection section;
	
	/* controls */
	protected Text nameText;
	protected CCombo dropDown;
	protected Button okButton;
	protected Composite contentComposite;
	protected Composite placeHolderComposite;
	
	public ParameterDialog(GFPropertySection section, Shell parentShell, BaseElement bo, ParameterType parameter) {
		super(parentShell, parameter);
		
		this.section = section;
		this.bo = bo;
		this.parameter = parameter;
	}

	@Override
	protected void configureShell(final Shell newShell) {
	   super.configureShell(newShell);
	   
	   newShell.setText(DIALOG_LABEL);

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

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.TITLE | SWT.APPLICATION_MODAL);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		// create CANCEL
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

		// disable button when mandatory fields are not set
		okButton.setEnabled(canSubmit());

		// compute shell size at the end of the dialog creation
		computeSize();
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		nameText = createNameText(parent);

		dropDown = createTypeDropDown(parent);
		
		// create place holder composite for value mapping table which is empty
		placeHolderComposite = PropertyUtil.createGridLayoutedComposite(section, parent);
		refreshContent(parent);
		
		return parent;
	}
	
	protected Text createNameText(Composite parent) {
		Text text = PropertyUtil.createUnboundText(section, parent, PARAMETER_NAME_LABEL);
		
		ValidatingStringTextBinding nameBinding = new ValidatingStringTextBinding(parameter, PARAMETER_NAME_FEATURE, text) {
			
			@Override
			public String getModelValue() {
				String value = (String) super.getModelValue();
				if (value == null || isEmptyValue(value)) {
					showError(HelpText.MANDATORY_VALUE);
					return null;
				}
				return value;
			}
			
		};
		
		nameBinding.addErrorCode(100);
		nameBinding.addErrorCode(101);

		nameBinding.setMandatory(true);
		nameBinding.establish();
	
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				okButton.setEnabled(canSubmit());
			}
		});
		
		return text;
	}
	
	protected CCombo createTypeDropDown(final Composite parent) {
		// create editable drop down
		CCombo dropDown = PropertyUtil.createDropDown(section, parent, PARAMETER_TYPE_LABEL);
		
		for (int i=0; i < SUPPORTED_TYPES.length; i++) {
			dropDown.add(SUPPORTED_TYPES[i]);
		}
		
		ValidatingStringComboBinding typeBinding = new ValidatingStringComboBinding(parameter, PARAMETER_MIXED_FEATURE, dropDown) {
			
			@Override
			public String getModelValue() {
				if (model.eIsSet(PARAMETER_LIST_FEATURE)) {
					return PARAMETER_LIST_TYPE_LABEL;
				} else if (model.eIsSet(PARAMETER_MAP_FEATURE)) {
					return PARAMETER_MAP_TYPE_LABEL;
				} else if (model.eIsSet(PARAMETER_SCRIPT_FEATURE)) {
					return PARAMETER_SCRIPT_TYPE_LABEL;
				} else {
					return PARAMETER_TEXT_TYPE_LABEL;
				}
			}
			
			@Override
			public void setModelValue(final String value) {
				TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
					
					@Override
					protected void doExecute() {
						FeatureMap mixed = getMixed(model);
						mixed.clear();						
						
						EObject newObject = null;
						if (PARAMETER_LIST_TYPE_LABEL.equals(value)) {
							newObject = createListType();
						} else if (PARAMETER_MAP_TYPE_LABEL.equals(value)) {
							newObject = createMapType();
						} else if (PARAMETER_SCRIPT_TYPE_LABEL.equals(value)) {
							newObject = createScriptType();
						}
						
						if (!PARAMETER_TEXT_TYPE_LABEL.equals(value) && newObject != null) {
							EStructuralFeature feature = PARAMETER_TYPE_FEATURE_MAP.get(value);
							model.eSet(feature, newObject);
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
		
		return dropDown;
	}

	protected void createMapTable(final GFPropertySection section, final Composite parent, final ParameterType parameter) {
		
		// composite for table
		final Composite composite = PropertyUtil.createStandardComposite(section, parent);
		
		ElementFactory<EntryType> elementFactory = new ElementFactory<EntryType>() {
			
			@Override
			public EntryType create() {
				final EntryType newEntry = createEntryType();

				TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(parameter);
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {

					@Override
					protected void doExecute() {
						MapType map = parameter.getMap();
						if (map != null) {
							EList<EntryType> entries = map.getEntries();
							
							if (entries != null) {
								entries.add(newEntry);
							}
						}
					}
				});
				
				return newEntry;
			}
		};
		
		ContentProvider<EntryType> contentProvider = new ContentProvider<EntryType>() {

			@Override
			public List<EntryType> getContents() {
				List<EntryType> contents = new ArrayList<EntryType>();
				
				MapType map = parameter.getMap();
				
				if (map != null) {
					
					EList<EntryType> entries = map.getEntries();
					
					if (entries != null && !entries.isEmpty()) {
						contents.addAll(entries);
					}
				}
				
				return contents;
			}
		};
		
		final EditingSupportProvider editSupportProvider = new EditingSupportProvider() {
			
			@Override
			public EditingSupport getEditingSupport(TableViewer viewer, final EStructuralFeature feature) {
				
				return new EObjectAttributeEditingSupport<EntryType>(viewer, feature) {
					
					@Override
					protected boolean canEdit(Object element) {
						if (feature.equals(ENTRY_KEY_FEATURE)) {
							return true;
						}
						
						EntryType entry = (EntryType) element;
						
						if (entry.eIsSet(ENTRY_LIST_FEATURE)) {
							return false;
						} else
							
						if (entry.eIsSet(ENTRY_MAP_FEATURE)) {
							return false;
						} else
							
						if (entry.eIsSet(ENTRY_SCRIPT_FEATURE)) {
							return false;
						}
						
						return true;
					}
					
					@Override
					protected String getValue(EntryType element) {
						if (feature.equals(ENTRY_KEY_FEATURE)) {
							return super.getValue(element);
						}
						
						String value = (String) element.eGet(ENTRY_TEXT_FEATURE);
						
						if (value != null) {
							return value.trim();
						}
						
						return EMPTY_STRING;
					}
					
					@Override
					protected void setValue(final EntryType element, final Object value) {
						if (feature.equals(ENTRY_KEY_FEATURE)) {
							super.setValue(element, value);
							return;
						}
							
						TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(parameter);
						editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
							
							@Override
							protected void doExecute() {
								element.eSet(ENTRY_TEXT_FEATURE, value);
							};
							
						});
							
					}

				};
			}
		};
		
		final DeleteRowHandler<EntryType> deleteRowHandler = new AbstractDeleteRowHandler<EntryType>() {

			@Override
			public void rowDeleted(final EntryType element) {
				TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(parameter);
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {

					@Override
					protected void doExecute() {
						MapType map = parameter.getMap();
						
						if (map != null) {
							EList<EntryType> entries = map.getEntries();
							
							if (entries != null && !entries.isEmpty()) {
								entries.remove(element);
							}
						}
					}
				});
			}
		};
		
		EditableEObjectTableBuilder<EntryType> builder = new EditableEObjectTableBuilder<EntryType>(section, composite, null) {
			
			@Override
			protected EObjectAttributeTableColumnDescriptor<EntryType> createAttributeTableColumnDescriptor(final EStructuralFeature columnFeature, String columnLabel, int weight) {
				EObjectAttributeTableColumnDescriptor<EntryType> columnDescriptor = new EObjectAttributeTableColumnDescriptor<EntryType>(columnFeature, columnLabel, 30) {
					
					@Override
					public ColumnLabelProvider getColumnLabelProvider() {
						ColumnLabelProvider labelProvider = new TypedColumnLabelProvider<EntryType>() {

							@Override
							public String getText(EntryType element) {
								if (columnFeature.equals(ENTRY_KEY_FEATURE)) {
									Object value = element.eGet(columnFeature);
									if (value == null) {
										return EMPTY_STRING;
									} else {
										return String.valueOf(element.eGet(columnFeature));
									}
								}
									
								if (element.eIsSet(ENTRY_LIST_FEATURE)) {
									return COMPLEX_CONTENT_LIST;
								} else if (element.eIsSet(ENTRY_MAP_FEATURE)) {
									return COMPLEX_CONTENT_MAP;
								} else if (element.eIsSet(ENTRY_SCRIPT_FEATURE)) {
									return COMPLEX_CONTENT_SCRIPT;
								} else {
									String value = (String) element.eGet(ENTRY_TEXT_FEATURE);
									
									if (value != null) {
										return value.trim();
									}
								}
									
								return EMPTY_STRING;
							}
						};

						return labelProvider;
					}
					
				};
				
				columnDescriptor.setEditingSupportProvider(editSupportProvider);
				return columnDescriptor;
			}
		};
		
		builder
			.contentProvider(contentProvider)
			.editingSupportProvider(editSupportProvider)
			.elementFactory(elementFactory)
			.deleteRowHandler(deleteRowHandler)
			.columnFeatures(PARAMETER_MAP_TABLE_FEATURES)
			.columnLabels(PARAMETER_MAP_TABLE_HEADERS)
			.model(parameter)
			.note(TABLE_NOTE);
	
	
		final TableViewer viewer = builder.build();
	
		// table composite
		final Composite tableComposite = viewer.getTable().getParent();
		setHeight(tableComposite);
		
		// create label
		PropertyUtil.createLabel(section, composite, PARAMETER_MAP_TABLE_LABEL, tableComposite);
	}

	protected <T extends EObject> void createListTable(final GFPropertySection section, final Composite parent, final ParameterType parameter) {
		
		// composite for table
		final Composite composite = PropertyUtil.createStandardComposite(section, parent);
		
		final ElementFactory<T> elementFactory = new ElementFactory<T>() {
			
			@Override
			public T create() {
				TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(parameter);
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {

					@Override
					protected void doExecute() {
						ListType list = parameter.getList();
						if (list != null) {
							EList<String> values = list.getValues();
							
							if (values != null) {
								values.add(EMPTY_STRING);
							}
						}
					}
				});
				
				return null;
			}
		};
		
		ContentProvider<T> contentProvider = new ContentProvider<T>() {

			@Override
			@SuppressWarnings("unchecked")
			public List<T> getContents() {
				 ListType list = parameter.getList();

				 if (list != null) {
					
					return (List<T>) list.getGroup();
				}
				
				return null;
			}
		};
		
		final EditingSupportProvider editSupportProvider = new EditingSupportProvider() {
			
			@Override
			public EditingSupport getEditingSupport(TableViewer viewer, final EStructuralFeature feature) {
				
				return new EObjectAttributeEditingSupport<T>(viewer, feature) {
					
					@Override
					protected boolean canEdit(Object element) {
						if (element instanceof FeatureMap.Entry) {
							FeatureMap.Entry entry = (FeatureMap.Entry) element;
							Object value = entry.getValue();
							return value instanceof String;
						}
						return false;
					}
					
					@Override
					@SuppressWarnings("unchecked")
					protected Object getValue(Object element) {
						Object elem = element;
						
						if (elem instanceof FeatureMap.Entry) {
							FeatureMap.Entry entry = (FeatureMap.Entry) elem;
							
							elem = entry.getValue();
							
							if (elem instanceof String) {
								// this happens for example in such a case:
								// <camunda:list>
								//   <camunda:value>ABC</camunda:value>
								// <camunda:list>
								//
								// then "ABC" will be returned
								return (String) elem;
							}
						}
						
						return getValue((T) elem);
					}
					
					@Override
					protected String getValue(T element) {
						return EMPTY_STRING;
					}
					
					@Override
					protected void setValue(Object element, final Object value) {
						if (element instanceof FeatureMap.Entry) {
							
							final FeatureMap.Entry entry = (FeatureMap.Entry) element;
							
							TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(parameter);
							editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {

								@Override
								protected void doExecute() {
									FeatureMap.Entry.Internal newEntry = ((BasicFeatureMapEntry) entry).createEntry(value);
									
									ListType list = parameter.getList();
									
									if (list != null) {
										FeatureMap group = list.getGroup();
										
										if (group != null && !group.isEmpty()) {
											int idx = group.lastIndexOf(entry);		
											if (idx != -1) {
												group.remove(idx);
												group.add(idx, newEntry);
											} else {
												group.add(newEntry);
											}
										}
									}
								}
							});
						}
					}
					
					@Override
					protected void setValue(final T element, final Object value) {
						// nothing to do!
					}
					
					@Override
					protected void saveCellEditorValue(CellEditor cellEditor, ViewerCell cell) {
						if (!cellEditor.isDirty()) {
							return;
						}

						Object element = cell.getElement();
						Object oldValue = getValue(element);
						
						callSuperCellEditorValue(cellEditor, cell);
						
						Object newValue = getValue(element);
						
						getTableViewer().getTable().notifyListeners(Events.CELL_VALUE_CHANGED, new Events.CellValueChanged<T>(cell, null, newValue, oldValue));
					}

				};
			}
		};
		
		Listener deleteRowListener = new Listener() {

			@Override
			@SuppressWarnings("unchecked")
			public void handleEvent(Event e) {
				Events.RowDeleted<T> event = (RowDeleted<T>) e;
				
				Object element = event.getRemovedElement();
				
				if (element instanceof FeatureMap.Entry) {
					final FeatureMap.Entry entry = (FeatureMap.Entry) element;
					
					TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(parameter);
					editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {

						@Override
						protected void doExecute() {
							ListType list = parameter.getList();
							
							if (list != null) {
								FeatureMap group = list.getGroup();
								
								if (group != null && !group.isEmpty()) {
									group.remove(entry);
								}
							}
						}
					});
				} 
			}
		};
		
		EditableEObjectTableBuilder<T> builder = new EditableEObjectTableBuilder<T>(section, composite, null) {
			
			@Override
			protected EObjectAttributeTableColumnDescriptor<T> createAttributeTableColumnDescriptor(final EStructuralFeature columnFeature, String columnLabel, int weight) {
				EObjectAttributeTableColumnDescriptor<T> columnDescriptor = new EObjectAttributeTableColumnDescriptor<T>(columnFeature, columnLabel, 30) {
					
					@Override
					public ColumnLabelProvider getColumnLabelProvider() {
						ColumnLabelProvider labelProvider = new TypedColumnLabelProvider<T>() {

							@Override
							public String getText(Object element) {
								Object elem = element;
								
								if (elem instanceof FeatureMap.Entry) {
									FeatureMap.Entry entry = (FeatureMap.Entry) elem;
									
									elem = entry.getValue();
									
									if (elem instanceof String) {
										// this happens for example in such a case:
										// <camunda:list>
										//   <camunda:value>ABC</camunda:value>
										// <camunda:list>
										//
										// then "ABC" will be returned
										return (String) elem;
									}
								}
								
								return super.getText(elem);
							}
							
							@Override
							public String getText(T element) {
								
								if (element instanceof ListType) {
									return COMPLEX_CONTENT_LIST;
								} else if (element instanceof MapType) {
									return COMPLEX_CONTENT_MAP;
								} else if (element instanceof ScriptType) {
									return COMPLEX_CONTENT_SCRIPT;
								}
								
								return EMPTY_STRING;
							}
						};

						return labelProvider;
					}
					
				};
				
				columnDescriptor.setEditingSupportProvider(editSupportProvider);
				return columnDescriptor;
			}
			
			@Override
			protected EditableTableDescriptor<T> createTableDescriptor() {
				EditableTableDescriptor<T> tableDescriptor = new EditableTableDescriptor<T>() {
					
					@Override
					protected AddStrategy<T> createAddStrategy(TableViewer viewer, ElementFactory<T> elementFactory) {
						return new AddAndEditStrategy<T>(viewer, elementFactory) {
							
							@Override
							public T performAdd() {
								super.performAdd();
								
								// set focus on new element if possible
								ListType list = parameter.getList();
								if (list != null) {
									FeatureMap group = list.getGroup();
									if (group != null && !group.isEmpty()) {
										
										int idx = group.size() - 1;
										
										final Entry element = group.get(idx);
										
										if (element != null) {
											Display.getCurrent().asyncExec(new Runnable() {
												
												@Override
												public void run() {
													viewer.editElement(element, 0);
												}
											});
										}
									}
								}
								
								return null;
							}
							
						};
					}
					
				};
				tableDescriptor.setElementFactory(elementFactory);
				
				return tableDescriptor;
			}
		};
		
		builder
			.contentProvider(contentProvider)
			.editingSupportProvider(editSupportProvider)
			.elementFactory(elementFactory)
			.deleteRowListener(deleteRowListener)
			.columnFeatures(PARAMETER_LIST_TABLE_FEATURES)
			.columnLabels(PARAMETER_LIST_TABLE_HEADERS)
			.model(parameter)
			.note(TABLE_NOTE);
	
	
		final TableViewer viewer = builder.build();
	
		// table composite
		final Composite tableComposite = viewer.getTable().getParent();
		setHeight(tableComposite);
	
		// create label
		PropertyUtil.createLabel(section, composite, PARAMETER_LIST_TABLE_LABEL, tableComposite);
	}
	
	protected void createScript(GFPropertySection section, Composite parent, ParameterType parameter) {
		new CamundaScriptPropertiesBuilder(parent, section, bo, parameter, PARAMETER_SCRIPT_FEATURE).create();
	}
	
	protected void createTextContent(GFPropertySection section, Composite parent, final ParameterType parameter) {
		Composite composite = PropertyUtil.createStandardComposite(section, parent);

		Text text = PropertyUtil.createSimpleMultiText(section, composite, EMPTY_STRING);
		setHeight(text);
		
		StringTextBinding binding = new StringTextBinding(parameter, PARAMETER_TEXT_FEATURE, text) {
			
			@Override
			public String getModelValue() {
				return parameter.getText();
			}
			
			@Override
			public void setModelValue(final String value) {
				TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(model);
				editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
					
					@Override
					protected void doExecute() {
						if (value != null && !value.isEmpty()) {
							parameter.setText(value);
						} else {
							FeatureMap mixed = parameter.getMixed();
							if (mixed != null && !mixed.isEmpty()) {
								mixed.clear();
							}
						}
					}
				});
			}
		};

		binding.establish();
		
		PropertyUtil.createLabel(section, composite, PARAMETER_TEXT_CONTENT_LABEL, text);
	}
	
	protected void refreshContent(Composite parent) {
		if (contentComposite != null && !contentComposite.isDisposed()) {
			contentComposite.dispose();
		}

		contentComposite = PropertyUtil.createGridLayoutedComposite(section, placeHolderComposite);
		
		if (parameter.eIsSet(PARAMETER_LIST_FEATURE)) {
			createListTable(section, contentComposite, parameter);
			
		} else if (parameter.eIsSet(PARAMETER_MAP_FEATURE)) {
			createMapTable(section, contentComposite, parameter);
			
		} else if (parameter.eIsSet(PARAMETER_SCRIPT_FEATURE)) {
			createScript(section, contentComposite, parameter);
			
		} else {
			createTextContent(section, contentComposite, parameter);
		}

		placeHolderComposite.layout();

		relayout(parent);
		
	}
	
	protected boolean canSubmit() {
		if (parameter.eGet(PARAMETER_NAME_FEATURE) != null
				&& nameText.getText() != null
				&& !nameText.getText().isEmpty()) {
			return true;
		} else {
			return false;
		}
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
	
	protected ScriptType createScriptType() {
		return ModelFactory.eINSTANCE.createScriptType();
	}
	
	protected MapType createMapType() {
		return ModelFactory.eINSTANCE.createMapType();
	}
	
	protected EntryType createEntryType() {
		return ModelFactory.eINSTANCE.createEntryType();
	}
	
	protected ListType createListType() {
		return ModelFactory.eINSTANCE.createListType();
	}
	
	protected FeatureMap getMixed(EObject model) {
		return (FeatureMap) model.eGet(PARAMETER_MIXED_FEATURE);
	}
	
	protected void setHeight(Control control) {
		Object data = control.getLayoutData();
		
		if (data instanceof FormData) {
			FormData layout = (FormData) data;
			layout.height = TABLE_HEIGHT;
		}
	}
}
