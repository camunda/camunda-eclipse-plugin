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
 * @author Innar Made
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.property.tasks;

import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.DataInput;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataOutput;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.FormalExpression;
import org.eclipse.bpmn2.InputOutputSpecification;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.runtime.ModelEnablementDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2TableComposite;
import org.eclipse.bpmn2.modeler.ui.property.DefaultPropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.PropertiesCompositeFactory;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.Section;

public class IoParametersPropertySection extends AbstractBpmn2PropertySection {
	static {
		PropertiesCompositeFactory.register(InputOutputSpecification.class, IoParametersPropertiesComposite.class);
		PropertiesCompositeFactory.register(DataInput.class, DataAssociationPropertiesComposite.class);
		PropertiesCompositeFactory.register(DataOutput.class, DataAssociationPropertiesComposite.class);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection#createSectionRoot()
	 */
	@Override
	protected AbstractBpmn2PropertiesComposite createSectionRoot() {
		return new IoParametersPropertiesComposite(this);
	}

	@Override
	public boolean appliesTo(IWorkbenchPart part, ISelection selection) {
		if (super.appliesTo(part, selection)) {
			ModelEnablementDescriptor modelEnablement = getModelEnablement(selection);
			EObject selectionBO = BusinessObjectUtil.getBusinessObjectForSelection(selection);
			EStructuralFeature feature = selectionBO.eClass().getEStructuralFeature("ioSpecification");
			if (feature != null) {
				if (!modelEnablement.isEnabled(selectionBO.eClass(), feature))
					return false;
			}
			return true;
		}
		return false;
	}

	@Override
	protected EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		EObject be = super.getBusinessObjectForPictogramElement(pe);
		if (be!=null) {
			EStructuralFeature feature = be.eClass().getEStructuralFeature("ioSpecification");
			if (feature != null)
				return be;
		}
		return null;
	}

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
	public class DataAssociationPropertiesComposite extends DefaultPropertiesComposite {
		
		Activity activity;
		ThrowEvent event;
		ItemAwareElement parameter;
		String parameterName;
		DataAssociation association;
		boolean isInput;
		boolean updatingWidgets;
		
		Button mapPropertyButton;
		Button mapExpressionButton;
		// holds the Transformation expression details and Assignments table
		Composite expressionComposite;
		DefaultPropertiesComposite transformationDetailsComposite;
		AssignmentsTable assignmentsTable;
		// holds the Property details
		Composite propertyComposite;
		DefaultPropertiesComposite propertyDetailsComposite;

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
			expressionComposite = null;
			transformationDetailsComposite = null;
			assignmentsTable = null;
			propertyComposite = null;
			propertyDetailsComposite = null;
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
		
		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			updateWidgets();
		}

		private void updateWidgets() {
			if (association!=null) {
				updatingWidgets = true;
				if (association.getAssignment().size()>0 || association.getTransformation()!=null) {
					mapExpressionButton.setSelection(true);
					mapPropertyButton.setSelection(false);
					showExpressionWidgets(true);
					showPropertyWidgets(false);
				}
				else if (association.getSourceRef().size()>0) {
					mapExpressionButton.setSelection(false);
					mapPropertyButton.setSelection(true);
					showExpressionWidgets(false);
					showPropertyWidgets(true);
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

						if (associations!=null) {
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
						}
						if (association == null) {
							if (isInput) {
								@SuppressWarnings("restriction")
								TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
								domain.getCommandStack().execute(new RecordingCommand(domain) {
									@Override
									protected void doExecute() {
										DataInputAssociation diAssociation = 
												Bpmn2Factory.eINSTANCE.createDataInputAssociation();
										diAssociation.setTargetRef(parameter);
										activity.getDataInputAssociations().add(diAssociation);
										ModelUtil.setID(diAssociation);
										association = diAssociation;
									}
								});							
							}
							else {
								@SuppressWarnings("restriction")
								TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
								domain.getCommandStack().execute(new RecordingCommand(domain) {
									@Override
									protected void doExecute() {
										DataOutputAssociation doAssociation = 
												Bpmn2Factory.eINSTANCE.createDataOutputAssociation();
										doAssociation.getSourceRef().add(parameter);
										activity.getDataOutputAssociations().add(doAssociation);
										ModelUtil.setID(doAssociation);
										association = doAssociation;
									}
								});							
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
							showExpressionWidgets(false);
							showPropertyWidgets(true);
							PropertyUtil.layoutAllParents(DataAssociationPropertiesComposite.this);
						}
					}
				});
			}
			
			if (mapExpressionButton==null) {
				mapExpressionButton = toolkit.createButton(this, "Map to an Expression", SWT.RADIO);
				mapExpressionButton.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,true,false,3,1));
				
				mapExpressionButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (mapExpressionButton.getSelection()) {
							showExpressionWidgets(true);
							showPropertyWidgets(false);
							PropertyUtil.layoutAllParents(DataAssociationPropertiesComposite.this);
						}
					}
				});
			}
			
			if (association.getAssignment().size()>0 || association.getTransformation()!=null) {
				mapExpressionButton.setSelection(true);
				showPropertyWidgets(false);
				showExpressionWidgets(true);
			}
			else if (association.getSourceRef().size()>0) {
				mapPropertyButton.setSelection(true);
				showExpressionWidgets(false);
				showPropertyWidgets(true);
			}
		}
		
		private void showPropertyWidgets(boolean show) {
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
		}
		
		private void showExpressionWidgets(boolean show) {
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
				if (!updatingWidgets && association.getTransformation()==null) {
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							FormalExpression transformation = Bpmn2Factory.eINSTANCE.createFormalExpression();
							association.setTransformation(transformation);
						}
					});
				}
				if (transformationDetailsComposite==null) {
					transformationDetailsComposite = new DefaultPropertiesComposite(expressionComposite,SWT.NONE);
					transformationDetailsComposite.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
				}
				transformationDetailsComposite.setEObject(getDiagramEditor(), association.getTransformation());
				transformationDetailsComposite.setTitle("Transformation");
				
				if (assignmentsTable!=null)
					assignmentsTable.dispose();
				assignmentsTable = new AssignmentsTable(expressionComposite);
				assignmentsTable.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,1,1));
				assignmentsTable.bindList(association, association.eClass().getEStructuralFeature("assignment"));
				assignmentsTable.setTitle("Assignments");
			}
			else {
				if (expressionComposite!=null) {
					expressionComposite.setVisible(false);
					((GridData)expressionComposite.getLayoutData()).exclude = true;
				}
				
				// remove the Transformation and assignments
				if (!updatingWidgets && (association.getAssignment().size()>0 || association.getTransformation()!=null)) {
					domain.getCommandStack().execute(new RecordingCommand(domain) {
						@Override
						protected void doExecute() {
							if (association.getAssignment().size()>0)
								association.getAssignment().clear();
							if (association.getTransformation()!=null)
								association.setTransformation(null);
						}
					});
				}
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
}
