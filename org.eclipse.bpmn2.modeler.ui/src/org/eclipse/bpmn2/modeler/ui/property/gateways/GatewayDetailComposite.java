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


package org.eclipse.bpmn2.modeler.ui.property.gateways;

import java.util.List;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.ListCompositeColumnProvider;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.ListCompositeContentProvider;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.TableColumn;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;

public class GatewayDetailComposite extends DefaultDetailComposite {

	private SequenceFlowsListComposite sequenceFlowsList;

	/**
	 * @param section
	 */
	public GatewayDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}
	
	public GatewayDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public AbstractPropertiesProvider getPropertiesProvider(EObject object) {
		if (propertiesProvider == null) {
			propertiesProvider = new AbstractPropertiesProvider(object) {
				// lump all the gateway properties into one composite
				// if a gateway doesn't have one of the attributes listed here,
				// it simply won't be displayed.
				String[] properties = new String[] {
						"gatewayDirection",
						"instantiate",
						"activationCondition",
						"eventGatewayType"
						// note: "default" sequence flow is already being displayed in the SequenceFlow tab
						// so, no need to show it here
				};
				
				@Override
				public String[] getProperties() {
					return properties; 
				}
			};
		}
		return propertiesProvider;
	}

	@Override
	public void cleanBindings() {
		super.cleanBindings();
		sequenceFlowsList = null;
	}

	@Override
	public void createBindings(EObject be) {
		super.createBindings(be);
		Gateway gateway = (Gateway)be;
		sequenceFlowsList = new SequenceFlowsListComposite(this);
		sequenceFlowsList.bindList(gateway, Bpmn2Package.eINSTANCE.getFlowNode_Incoming());
	}
	
	/**
	 * A ListComposite that displays all incoming and outgoing sequence flows for a Gateway
	 */
	public class SequenceFlowsListComposite extends DefaultListComposite {
		public SequenceFlowsListComposite(AbstractBpmn2PropertySection section) {
			super(section, SHOW_DETAILS);
		}

		
		public SequenceFlowsListComposite(Composite parent) {
			super(parent, SHOW_DETAILS);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractListComposite#getContentProvider(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature, org.eclipse.emf.common.util.EList)
		 * 
		 * Create a custom list content provider
		 */
		public ListCompositeContentProvider getContentProvider(EObject object, EStructuralFeature feature, EList<EObject>list) {
			if (contentProvider==null) {
				contentProvider = new SequenceFlowsListContentProvider(this,object);
			}
			return contentProvider;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractListComposite#getColumnProvider(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature)
		 * 
		 * Create a custom column provider
		 */
		public ListCompositeColumnProvider getColumnProvider(EObject object, EStructuralFeature feature) {
			if (columnProvider==null) {
				columnProvider = new SequenceFlowListColumnProvider(this, object);
			}
			return columnProvider;
		}
		
		public void bindList(final EObject theobject, final EStructuralFeature thefeature) {
			super.bindList(theobject, thefeature);
			tableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					StructuredSelection sel = (StructuredSelection)event.getSelection();
					EObject object = (EObject)sel.getFirstElement();
					Diagram diagram = getDiagramEditor().getDiagramTypeProvider().getDiagram();
					List<PictogramElement> pes = Graphiti.getLinkService().getPictogramElements(diagram, object);
					getDiagramEditor().setPictogramElementForSelection(pes.get(0));
					getDiagramEditor().refresh();
				}
			});
		}
	}

	/**
	 * Custom content provider for a gateway's incoming and outgoing sequence flows
	 */
	public class SequenceFlowsListContentProvider extends ListCompositeContentProvider {

		public SequenceFlowsListContentProvider(AbstractListComposite listComposite, EObject object) {
			super(listComposite, object, null, null);
			
			Gateway gateway = (Gateway)object;
			list = new BasicEList<EObject>();
			list.addAll(gateway.getIncoming());
			list.addAll(gateway.getOutgoing());
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return list.toArray();
		}
	}
	
	/**
	 * A custom column provider for sequence flows
	 */
	public class SequenceFlowListColumnProvider extends ListCompositeColumnProvider {

		public SequenceFlowListColumnProvider(AbstractListComposite list, EObject object) {
			super(list, false);
			// add 2 or 3 columns, depending on gateway type
			add(new SequenceFlowListColumn(object,1)); // identifier (from -> to)
			add(new SequenceFlowListColumn(object,2)); // Condition (expression)
			if (object.eClass().getEStructuralFeature("default")!=null) {
				add(new SequenceFlowListColumn(object,3)); // Is Default (boolean)
			}
		}
	}
	
	/**
	 * A custom TableColumn for sequence flows
	 */
	public class SequenceFlowListColumn extends TableColumn {

		int columnIndex;
		
	
		/**
		 * Construct a column by giving it the gateway EObject and column index
		 * @param gateway - the Gateway BPMN2 element
		 * @param columnIndex - a column index between 1 and 3:
		 * 1 = describes the sequence flow by source and target endpoints
		 * 2 = conditional expression for this sequence flow
		 * 3 = true/false if this sequence flow is the default for the gateway
		 */
		public SequenceFlowListColumn(EObject gateway, int columnIndex) {
			super(gateway, (EStructuralFeature)null);
			this.columnIndex = columnIndex;
		}
		
		@Override
		public String getHeaderText() {
			switch (columnIndex) {
			case 1:
				return "Sequence Flow";
			case 2:
				return "Condition";
			case 3:
				return "Is Default";
			}
			return "header " + columnIndex;
		}

		@Override
		public String getText(Object element) {
			SequenceFlow flow = (SequenceFlow)element;
			Gateway gateway = (Gateway)object;
			String text = "";
			switch (columnIndex) {
			case 1:
				text += ModelUtil.getDisplayName(flow.getSourceRef());
				text += " -> ";
				text += ModelUtil.getDisplayName(flow.getTargetRef());
				break;
			case 2:
				text = ModelUtil.getDisplayName(flow.getConditionExpression());
				if (text==null)
					text = "";
				break;
			case 3:
				{
					EStructuralFeature f = gateway.eClass().getEStructuralFeature("default");
					Object defaultFlow = gateway.eGet(f);
					text += (flow == defaultFlow);
				}
				break;
			}
			return text;
		}

		@Override
		public Object getValue(Object element, String property) {
			return null;
		}

		@Override
		public String getProperty() {
			return "column " + columnIndex;
		}
	}
}