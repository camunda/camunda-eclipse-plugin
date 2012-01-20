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

package org.eclipse.bpmn2.modeler.ui.property.tasks;

import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Assignment;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2TableComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This class renders the property sheet tab for Data I/O Associations (a.k.a. parameter mappings)
 * defined in Activities and ThrowEvents.
 * 
 * The DataInput/OutputAssociation can be used to associate an ItemAwareElement
 * parameter with a DataInput/Output contained in an Activity. The source of such
 * a DataAssociation can be every ItemAwareElement accessible in the
 * current scope, e.g., a Data Object, a Property, or an Expression.
 * 
 * The execution of any Data Associations MUST follow these semantics:
 *  o If the Data Association specifies a “transformation” Expression,
 *    this expression is evaluated and the result is copied to the targetRef.
 *    This operation replaces completely the previous value of the targetRef parameter.
 *  o For each “assignment” parameter specified:
 *    o Evaluate the Assignment’s “from” expression and obtain the *source value*.
 *    o Evaluate the Assignment’s “to” expression and obtain the *target parameter*.
 *      The *target parameter* can be any parameter in the context or a sub-parameter of
 *      it (e.g., a DataObject or a sub-parameter of it).
 *    o Copy the *source value* to the *target parameter*.
 *  o If no “transformation” Expression nor any “assignment” elements are defined
 *    in the Data Association:
 *    o Copy the Data Association “sourceRef” value into the “targetRef.” Only one
 *      sourceRef parameter is allowed in this case.					
 */
public class DataAssociationPropertiesComposite extends DefaultPropertiesComposite implements IResourceChangeListener {
	
	enum MapType {
		None,
		Property,
		Transformation,
		Expression,
		Advanced
	};

	Activity activity;
	ThrowEvent event;
	ItemAwareElement parameter;
	String parameterName;
	DataAssociation association;
	boolean isInput;
	boolean updatingWidgets;
	Button mapPropertyButton;
	Button mapExpressionButton;
	Button mapTransformationButton;
	Button advancedMappingButton;
	// holds the Transformation expression details and Assignments table
	Composite transformationComposite;
	DefaultPropertiesComposite transformationDetailsComposite;
	Composite expressionComposite;
	DefaultPropertiesComposite expressionDetailsComposite;
	AssignmentsTable assignmentsTable;
	// holds the Property details
	Composite propertyComposite;
	DefaultPropertiesComposite propertyDetailsComposite;
	boolean propertyWidgetsShowing = false;
	boolean expressionWidgetsShowing = false;
	boolean transformationWidgetsShowing = false;
	boolean advancedMappingWidgetsShowing = false;
	
	public DataAssociationPropertiesComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public DataAssociationPropertiesComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	protected void cleanBindings() {
		super.cleanBindings();
		mapPropertyButton = null;
		mapExpressionButton = null;
		mapTransformationButton = null;
		advancedMappingButton = null;
		propertyComposite = null;
		propertyDetailsComposite = null;
		transformationComposite = null;
		transformationDetailsComposite = null;
		expressionComposite = null;
		expressionDetailsComposite = null;
		assignmentsTable = null;
//		boolean propertyWidgetsShowing = false;
//		boolean expressionWidgetsShowing = false;
//		boolean transformationWidgetsShowing = false;
//		boolean advancedMappingWidgetsShowing = false;
	}
	
	@Override
	public void createBindings(EObject be) {

		association = null;
		if (be instanceof DataInput) {
			isInput = true;
			parameterName = ((DataInput)be).getName();
		}
		else if (be instanceof DataOutput) {
			isInput = false;
			parameterName = ((DataOutput)be).getName();
		}
		else {
			createWidgets();
			return;
		}
		
		// set section title
		if (getParent() instanceof Section) {
			((Section)getParent()).setText("Parameter \""+parameterName+"\" Mapping");
		}
		parameter = (ItemAwareElement)be;
		
		List<? extends DataAssociation> associations = null;
		EObject container = be.eContainer();
		if (container instanceof InputOutputSpecification) {
			EObject containerContainer = container.eContainer();
			if (containerContainer instanceof Activity) {
				activity = (Activity)containerContainer;
				if (isInput)
					associations = activity.getDataInputAssociations();
				else
					associations = activity.getDataOutputAssociations();
			}
		}
		else if (be instanceof ThrowEvent) {
			event = (ThrowEvent)container;
			if (isInput)
				associations = event.getDataInputAssociation();
		}

		if (associations!=null) {
			for (DataAssociation a : associations) {
				if (isInput) {
					if (a.getTargetRef() == be) {
						association = a;
						break;
					}
				}
				else
				{
					for (ItemAwareElement e : a.getSourceRef()) {
						if (e == be) {
							association = a;
							break;
						}
					}
					if (association!=null)
						break;
				}
			}
		}
		createWidgets();			
		
		PropertyUtil.layoutAllParents(this);
	}

	private MapType getMapType() {
		if (association!=null) {
			if (association.getAssignment().size()>1) {
				return MapType.Advanced;
			}
			if (association.getTransformation()!=null) {
				if (association.getAssignment().size()>0) {
					return MapType.Advanced;
				}
				return MapType.Transformation;
			}
			if (association.getAssignment().size()==1) {
				return MapType.Expression;
			}
			if (association.getSourceRef().size()>0) {
				return MapType.Property;
			}
		}
		return MapType.None;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
//		updateWidgets();
	}

	private void updateWidgets() {
		if (association!=null && !updatingWidgets) {
			updatingWidgets = true;

			switch (getMapType()) {
			case None:
				mapPropertyButton.setSelection(false);
				mapTransformationButton.setSelection(false);
				mapExpressionButton.setSelection(false);
				advancedMappingButton.setSelection(false);
				showPropertyWidgets(false);
				showTransformationWidgets(false);
				showExpressionWidgets(false);
				showAdvancedMappingWidgets(false);
				break;
			case Property:
				mapTransformationButton.setSelection(false);
				mapExpressionButton.setSelection(false);
				advancedMappingButton.setSelection(false);
				showTransformationWidgets(false);
				showExpressionWidgets(false);
				showAdvancedMappingWidgets(false);
				
				mapPropertyButton.setSelection(true);
				showPropertyWidgets(true);
				break;
			case Transformation:
				mapPropertyButton.setSelection(false);
				mapExpressionButton.setSelection(false);
				advancedMappingButton.setSelection(false);
				showPropertyWidgets(false);
				showExpressionWidgets(false);
				showAdvancedMappingWidgets(false);

				mapTransformationButton.setSelection(true);
				showTransformationWidgets(true);
				break;
			case Expression:
				mapPropertyButton.setSelection(false);
				mapTransformationButton.setSelection(false);
				advancedMappingButton.setSelection(false);
				showPropertyWidgets(false);
				showTransformationWidgets(false);
				showAdvancedMappingWidgets(false);

				mapExpressionButton.setSelection(true);
				showExpressionWidgets(true);
				break;
			case Advanced:
				mapPropertyButton.setSelection(false);
				mapTransformationButton.setSelection(false);
				mapExpressionButton.setSelection(false);
				showPropertyWidgets(false);
				showTransformationWidgets(false);
				showExpressionWidgets(false);
				
				advancedMappingButton.setSelection(true);
				showAdvancedMappingWidgets(true);
				break;
			}
			PropertyUtil.layoutAllParents(DataAssociationPropertiesComposite.this);
			updatingWidgets = false;
		}
	}
	
	private void createWidgets() {
		
		if (association==null && !(be instanceof DataInput || be instanceof DataOutput)) {
			this.createLabel(this, "The I/O Parameter \""+parameterName+"\" can not have Mappings.");
			return;
		} else {
			EObject container = be.eContainer();
			if (container instanceof InputOutputSpecification) {
				EObject containerContainer = container.eContainer();
				if (containerContainer instanceof Activity) {
					activity = (Activity)containerContainer;
					List<? extends DataAssociation> associations = null;
					if (isInput)
						associations = activity.getDataInputAssociations();
					else
						associations = activity.getDataOutputAssociations();
					for (DataAssociation a : associations) {
						if (isInput) {
							if (a.getTargetRef() == be) {
								association = a;
								break;
							}
							if (a.getSourceRef().isEmpty() || a.getTargetRef() == null) {
								association = a;
								break;
							}
						}
						else
						{
							for (ItemAwareElement e : a.getSourceRef()) {
								if (e == be) {
									association = a;
									break;
								}
								if (a.getSourceRef().isEmpty() || a.getTargetRef() == null) {
									association = a;
									break;
								}
							}
							if (association!=null)
								break;
						}
					}

					if (association == null) {
						// if no DataAssociation was found for this Activity, create a new one
						// and add it to the Activity's DataInput/OutputAssociations list using
						// an InsertionAdapter.
						if (isInput) {
							DataInputAssociation diAssociation = 
									Bpmn2Factory.eINSTANCE.createDataInputAssociation();
							diAssociation.setTargetRef(parameter);
							association = diAssociation;
							association.eAdapters().add(new InsertionAdapter(
									activity,
									Bpmn2Package.eINSTANCE.getActivity_DataInputAssociations(),
									association));
						}
						else {
							DataOutputAssociation doAssociation = 
									Bpmn2Factory.eINSTANCE.createDataOutputAssociation();
							doAssociation.getSourceRef().add(parameter);
							association = doAssociation;
							association.eAdapters().add(new InsertionAdapter(
									activity,
									Bpmn2Package.eINSTANCE.getActivity_DataInputAssociations(),
									association));
						}
					}
				}
			}
		}

		if (mapPropertyButton==null) {
			mapPropertyButton = toolkit.createButton(this, "Map to a Property", SWT.RADIO);
			mapPropertyButton.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,true,false,3,1));

			mapPropertyButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (mapPropertyButton.getSelection()) {
						showTransformationWidgets(false);
						showExpressionWidgets(false);
						showAdvancedMappingWidgets(false);

						showPropertyWidgets(true);
						PropertyUtil.layoutAllParents(DataAssociationPropertiesComposite.this);
					}
				}
			});
		}
		
		if (mapTransformationButton==null) {
			mapTransformationButton = toolkit.createButton(this, "Map a Transformation", SWT.RADIO);
			mapTransformationButton.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,true,false,3,1));
			
			mapTransformationButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (mapTransformationButton.getSelection()) {
						showPropertyWidgets(false);
						showExpressionWidgets(false);
						showAdvancedMappingWidgets(false);

						showTransformationWidgets(true);
						PropertyUtil.layoutAllParents(DataAssociationPropertiesComposite.this);
					}
				}
			});
		}
		
		if (mapExpressionButton==null) {
			mapExpressionButton = toolkit.createButton(this, "Map an Expression", SWT.RADIO);
			mapExpressionButton.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,true,false,3,1));
			
			mapExpressionButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (mapExpressionButton.getSelection()) {
						showPropertyWidgets(false);
						showTransformationWidgets(false);
						showAdvancedMappingWidgets(false);

						showExpressionWidgets(true);
						PropertyUtil.layoutAllParents(DataAssociationPropertiesComposite.this);
					}
				}
			});
		}

		if (advancedMappingButton==null) {
			advancedMappingButton = toolkit.createButton(this, "Advanced Mapping", SWT.RADIO);
			advancedMappingButton.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,true,false,3,1));
			
			advancedMappingButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (advancedMappingButton.getSelection()) {
						showPropertyWidgets(false);
						showTransformationWidgets(false);
						showExpressionWidgets(false);
						
						showAdvancedMappingWidgets(true);
						PropertyUtil.layoutAllParents(DataAssociationPropertiesComposite.this);
					}
				}
			});
		}
		
		updateWidgets();
		
	}
	
	private void showPropertyWidgets(boolean show) {
		if (show != propertyWidgetsShowing) {
			if (show) {
				if (propertyDetailsComposite==null) {
					propertyDetailsComposite = new DefaultPropertiesComposite(this,SWT.BORDER) {
						private AbstractPropertiesProvider propertiesProvider;
	
						@Override
						public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
							if (propertiesProvider == null) {
								propertiesProvider = new AbstractPropertiesProvider(object) {
									@Override
									public String[] getProperties() {
										String[] properties = null;
										if (isInput) {
											properties = new String[] {
													"sourceRef"
											};
										} else {
											properties = new String[] {
													"targetRef"
											};
										}
										return properties; 
									}
								};
							}
							return propertiesProvider;
						}
					};
					propertyDetailsComposite.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
					propertyDetailsComposite.setEObject(getDiagramEditor(), association);
					propertyDetailsComposite.setTitle("Properties");
				}
			}
			else {
				if (propertyDetailsComposite!=null) {
					propertyDetailsComposite.dispose();
					propertyDetailsComposite = null;
				}
			}
			propertyWidgetsShowing = show;
		}
	}

	private void showTransformationWidgets(boolean show) {
		if (show != transformationWidgetsShowing) {
			if (show) {
				if (transformationComposite==null) {
					transformationComposite = toolkit.createComposite(this, SWT.BORDER);
					transformationComposite.setLayout(new GridLayout(1,false));
					transformationComposite.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,true,3,1));
				}
				else {
					transformationComposite.setVisible(true);
					((GridData)transformationComposite.getLayoutData()).exclude = false;
				}
				
				// create a new Transformation FormalExpression
				FormalExpression transformation = association.getTransformation();
				if (!updatingWidgets && transformation==null) {
					transformation = Bpmn2Factory.eINSTANCE.createFormalExpression();
					transformation.eAdapters().add( new InsertionAdapter(association, Bpmn2Package.eINSTANCE.getDataAssociation_Transformation(), transformation) );
				}
				if (transformationDetailsComposite==null) {
					transformationDetailsComposite = new DefaultPropertiesComposite(transformationComposite,SWT.NONE);
				}
				transformationDetailsComposite.setEObject(getDiagramEditor(), transformation);
				transformationDetailsComposite.setTitle("Transformation");
	
			}
			else {
				if (transformationComposite!=null) {
					transformationComposite.setVisible(false);
					((GridData)transformationComposite.getLayoutData()).exclude = true;
				}
				
				// remove the Transformation and assignments
				if (!updatingWidgets && association.getTransformation()!=null) {
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							association.setTransformation(null);
						}
					});
				}
			}
			transformationWidgetsShowing = show;
		}
	}

	private void showExpressionWidgets(boolean show) {
		if (show != expressionWidgetsShowing) {
			if (show) {
				if (expressionComposite==null) {
					expressionComposite = toolkit.createComposite(this, SWT.BORDER);
					expressionComposite.setLayout(new GridLayout(1,false));
					expressionComposite.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,true,3,1));
				}
				else {
					expressionComposite.setVisible(true);
					((GridData)expressionComposite.getLayoutData()).exclude = false;
				}
				
				// create a new Transformation FormalExpression
				FormalExpression expression = null;
				Assignment assignment = null;
				if (association.getAssignment().size()==1) {
					assignment = (Assignment) association.getAssignment().get(0);
					if (isInput)
						expression = (FormalExpression) assignment.getFrom();
					else
						expression = (FormalExpression) assignment.getTo();
				}
				if (!updatingWidgets) {
					if (assignment==null) {
						assignment = Bpmn2Factory.eINSTANCE.createAssignment();
						FormalExpression paramExpression = Bpmn2Factory.eINSTANCE.createFormalExpression();
						paramExpression.setBody(parameter.getId());
						if (isInput)
							assignment.setTo(paramExpression);
						else
							assignment.setFrom(paramExpression);
						assignment.eAdapters().add( new InsertionAdapter(association, Bpmn2Package.eINSTANCE.getDataAssociation_Assignment(), assignment) );
					}
					if (expression==null) {
						expression = Bpmn2Factory.eINSTANCE.createFormalExpression();
						if (isInput)
							expression.eAdapters().add( new InsertionAdapter(assignment, Bpmn2Package.eINSTANCE.getAssignment_From(), expression) );
						else
							expression.eAdapters().add( new InsertionAdapter(assignment, Bpmn2Package.eINSTANCE.getAssignment_To(), expression) );
					}
				}
	
				if (expressionDetailsComposite==null) {
					expressionDetailsComposite = new DefaultPropertiesComposite(expressionComposite,SWT.NONE);
				}
				expressionDetailsComposite.setEObject(getDiagramEditor(), expression);//association.getexpression());
				expressionDetailsComposite.setTitle("Expression");
			}
			else {
				if (expressionComposite!=null) {
					expressionComposite.setVisible(false);
					((GridData)expressionComposite.getLayoutData()).exclude = true;
				}
				
				// remove the Transformation and assignments
				if (!updatingWidgets && association.getAssignment().size()==1) {
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							association.getAssignment().clear();
						}
					});
				}
			}
			expressionWidgetsShowing = show;
		}
	}

	private void showAdvancedMappingWidgets(boolean show) {
		if (show != advancedMappingWidgetsShowing) {
			if (show) {
				if (transformationComposite==null) {
					transformationComposite = toolkit.createComposite(this, SWT.BORDER);
					transformationComposite.setLayout(new GridLayout(1,false));
					transformationComposite.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,true,3,1));
				}
				else {
					transformationComposite.setVisible(true);
					((GridData)transformationComposite.getLayoutData()).exclude = false;
				}
				
				// create a new Transformation FormalExpression
				FormalExpression transformation = association.getTransformation();
				if (!updatingWidgets && transformation==null) {
					transformation = Bpmn2Factory.eINSTANCE.createFormalExpression();
					transformation.eAdapters().add( new InsertionAdapter(association, Bpmn2Package.eINSTANCE.getDataAssociation_Transformation(), transformation) );
				}
	
				if (transformationDetailsComposite==null) {
					transformationDetailsComposite = new DefaultPropertiesComposite(transformationComposite,SWT.NONE);
				}
				transformationDetailsComposite.setEObject(getDiagramEditor(), transformation);//association.getTransformation());
				transformationDetailsComposite.setTitle("Transformation");
				
				if (assignmentsTable!=null)
					assignmentsTable.dispose();
				assignmentsTable = new AssignmentsTable(transformationComposite);
				assignmentsTable.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
				assignmentsTable.bindList(association, association.eClass().getEStructuralFeature("assignment"));
				assignmentsTable.setTitle("Assignments");
			}
			else {
				if (transformationComposite!=null) {
					transformationComposite.setVisible(false);
					((GridData)transformationComposite.getLayoutData()).exclude = true;
				}
				
				if (assignmentsTable!=null) {
					assignmentsTable.setVisible(false);
					((GridData)assignmentsTable.getLayoutData()).exclude = true;
				}
				
				// remove the Transformation and assignments
				if (!updatingWidgets && (association.getAssignment().size()>0 || association.getTransformation()!=null)) {
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							association.getAssignment().clear();
							association.setTransformation(null);
						}
					});
				}
			}
			advancedMappingWidgetsShowing = show;
		}
	}

	public class AssignmentsTable extends AbstractBpmn2TableComposite {

		public AssignmentsTable(Composite parent) {
			super(parent, AbstractBpmn2TableComposite.DEFAULT_STYLE);
		}
		
		@Override
		public AbstractTableColumnProvider getColumnProvider(EObject object, EStructuralFeature feature) {
			columnProvider = new AbstractTableColumnProvider() {
				@Override
				public boolean canModify(EObject object, EStructuralFeature feature, EObject item) {
					return false;
				}
			};
			EClass listItemClass = getListItemClass(object, feature);
			
			columnProvider.add(new AssignmentsTableColumn(object,listItemClass.getEStructuralFeature("to")));
			columnProvider.add(new AssignmentsTableColumn(object,listItemClass.getEStructuralFeature("from")));

			return columnProvider;
		}

		public class AssignmentsTableColumn extends TableColumn {

			public AssignmentsTableColumn(EObject o, EStructuralFeature f) {
				super(o, f);
			}

			public String getText(Object element) {
				Object value = ((EObject)element).eGet(feature);
				if (value==null) {
					return "";
				}
				if (value instanceof FormalExpression) {
					FormalExpression exp = (FormalExpression)value;
					String body = exp.getBody();
					if (body==null||body.isEmpty())
						body = "<empty>";
					return body;
				}
				return value.toString();
			}
			
		}

	}
}
