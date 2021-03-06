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
 * @author Ivar Meikas
 ******************************************************************************/
package org.camunda.bpm.modeler.core.utils;

import java.util.List;

import org.camunda.bpm.modeler.ui.diagram.editor.Bpmn2Editor;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Conversation;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

public class BusinessObjectUtil {

	@SuppressWarnings("rawtypes")
	public static boolean containsElementOfType(PictogramElement elem, Class clazz) {
		if (elem.getLink() == null) {
			return false;
		}
		// if this is a connection point, look at business objects of the connection
		if (AnchorUtil.isConnectionPoint(elem)) {
			elem = AnchorUtil.getConnectionPointOwner((Shape)elem);
		}
		EList<EObject> businessObjs = elem.getLink().getBusinessObjects();
		for (EObject eObject : businessObjs) {
			if (clazz.isInstance(eObject)) {
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean containsChildElementOfType(PictogramElement root, Class clazz) {
		if (AnchorUtil.isConnectionPoint(root)) {
			root = AnchorUtil.getConnectionPointOwner((Shape)root);
		}
		if (root instanceof ContainerShape) {
			ContainerShape rootContainer = (ContainerShape) root;
			for (Shape currentShape : rootContainer.getChildren()) {
				if (containsElementOfType(currentShape, clazz)) {
					return true;
				}
			}
		}
		return false;
	}

	public static <T extends EObject> T getFirstElementOfType(PictogramElement elem, Class<T> clazz) {
		return getFirstElementOfType(elem, clazz, false);
	}

	public static <T extends EObject> T getFirstElementOfType(PictogramElement elem, Class<T> clazz, boolean searchParents) {
		// first check if its a diagram with pictogram links
		PictogramLink link = elem.getLink();
		
		if (elem instanceof Diagram && link == null) {
			Diagram diagram = (Diagram) elem;
			for (PictogramLink diagramLink : diagram.getPictogramLinks()) {
				T foundInLink = findInLink(diagramLink, clazz);
				if (foundInLink != null) {
					return foundInLink;
				}
			}
		} else {
			T result = null;
			
			if (searchParents) {
				while (elem != null) {
					result = link != null ? findInLink(link, clazz) : null;
					if (result != null) {
						return result;
					}
					
					if (elem.eContainer() instanceof PictogramElement) { 
						elem = (PictogramElement) elem.eContainer();
						link = elem.getLink();
					}
				}
			} else {
				return link != null ? findInLink(link, clazz) : null;
			}
		}
		
		return null;
	}

	private static <T extends EObject> T findInLink(PictogramLink pictogramLink, Class<T> clazz) {
		EList<EObject> businessObjs = pictogramLink.getBusinessObjects();
		for (EObject eObject : businessObjs) {
			if (clazz.isInstance(eObject)) {
				return (T) eObject;
			}
		}
		return null;
	}

	public static BaseElement getFirstBaseElement(PictogramElement pe) {
		return BusinessObjectUtil.getFirstElementOfType(pe, BaseElement.class);
	}

	public static PictogramElement getPictogramElementForEditPart(EditPart editPart) {
		if (editPart == null) {
			return null;
		}
		
		Object model = editPart.getModel();
		if (model == null) {
			return null;
		}
		
		if (model instanceof PictogramElement) {
			return (PictogramElement) model;
		}
		
		if (model instanceof BaseElement) {
			BaseElement baseElement = (BaseElement) model;
			return getLinkingPictogramElement(baseElement);
		}
		
		return null;
	}
	
	public static PictogramElement getPictogramElementForSelection(ISelection selection) {
		EditPart editPart = getEditPartForSelection(selection);
		
		PictogramElement element = getPictogramElementForEditPart(editPart);
		if (element != null) {
			return element;
		}
		
		if (selection instanceof IStructuredSelection) {
			Object o = ((IStructuredSelection) selection).getFirstElement();
			if (o instanceof PictogramElement) {
				return (PictogramElement) o;
			}
		}
		
		return null;
	}

	/**
	 * Returns the pictogram element for a given element
	 * 
	 * The diagram is obtained via the active editor. 
	 * 
	 * @param businessObject
	 * @return
	 */
	public static PictogramElement getLinkingPictogramElement(EObject businessObject) {
		Diagram diagram = Bpmn2Editor.getActiveEditor().getDiagramTypeProvider().getDiagram();
		return getLinkingPictogramElement(businessObject, diagram);
	}
	
	/**
	 * Returns the pictogram element for a given element
	 * 
	 * @param diagram
	 * @param businessObject
	 * @return
	 */
	public static PictogramElement getLinkingPictogramElement(EObject businessObject, Diagram diagram) {
		
		List<PictogramElement> pictogramElements = Graphiti.getLinkService().getPictogramElements(diagram, businessObject);
		if (pictogramElements.isEmpty()) {
			return null;
		} else {
			for (PictogramElement element : pictogramElements) {
				if (element instanceof ContainerShape && !LabelUtil.isLabel(element)) {
					return element;
				}
			}
			return null;
		}
	}

	public static EObject getBusinessObjectForSelection(ISelection selection) {
		PictogramElement pe = getPictogramElementForSelection(selection);
		if (pe!=null)
			return getFirstElementOfType(pe, EObject.class);
		if (selection instanceof IStructuredSelection) {
			Object o = ((IStructuredSelection)selection).getFirstElement();
			if (o instanceof EditPart) {
				o = ((EditPart)o).getModel();
				if (o instanceof EObject)
					return (EObject)o;
			}
		}				
		return null;
	}

	public static EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		if (pe!=null) {
			Object be = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			if (be instanceof EObject) {
				return (EObject) be;
			}
		}
		return null;
	}

	public static EditPart getEditPartForSelection(ISelection selection) {
		
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.isEmpty()) {
				return null;
			}
			
			Object firstElement = structuredSelection.getFirstElement();
			EditPart editPart = null;
			if (firstElement instanceof EditPart) {
				editPart = (EditPart) firstElement;
			} else if (firstElement instanceof IAdaptable) {
				editPart = (EditPart) ((IAdaptable) firstElement).getAdapter(EditPart.class);
			}
			return editPart;
		}
		
		return null;
	}
	
	public static boolean isConnection(Class<?> be) {
		return
				be == SequenceFlow.class ||
				be == Association.class ||
				be == MessageFlow.class ||
				be == DataInputAssociation.class ||
				be == DataOutputAssociation.class ||
				be == Conversation.class;
	}
}