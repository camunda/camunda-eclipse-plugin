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
package org.eclipse.bpmn2.modeler.core.utils;

import java.util.List;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Conversation;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
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
		return getFirstElementOfType(elem,clazz,false);
	}

	@SuppressWarnings("unchecked")
	public static <T extends EObject> T getFirstElementOfType(PictogramElement elem, Class<T> clazz, boolean searchParents) {
		// first check if its a diagram with pictogram links
		if ( (elem instanceof Diagram) && elem.getLink() == null) {
			
			Diagram diagram = (Diagram) elem;
			for (PictogramLink link : diagram.getPictogramLinks()) {
				T foundInLink = findInLink(link, clazz);
				if (foundInLink != null) {
					return foundInLink;
				}
			}
		}
		else if (elem.getLink() == null) {
			if (searchParents) {
				while (elem!=null && elem.getLink()==null && elem.eContainer() instanceof PictogramElement)
					elem = (PictogramElement)elem.eContainer();
			}
			if (elem==null || elem.getLink() == null)
				return null;
		}else {
			T foundInLink = findInLink(elem.getLink(), clazz);
			if (foundInLink != null) {
				return foundInLink;
			}
		}
		// if this is a connection point, look at business objects of the connection
		if (AnchorUtil.isConnectionPoint(elem)) {
			elem = AnchorUtil.getConnectionPointOwner((Shape)elem);
		}
		
		return null;
	}

	private static <T extends EObject> T findInLink(PictogramLink pictogramLink,
			Class<T> clazz) {
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

	public static PictogramElement getPictogramElementForSelection(ISelection selection) {
		EditPart editPart = getEditPartForSelection(selection);
		if (editPart != null && editPart.getModel() instanceof PictogramElement) {
			return (PictogramElement) editPart.getModel();
		}
		
		if (editPart != null && editPart.getModel() instanceof BaseElement) {
			BaseElement baseElement = (BaseElement) editPart.getModel();
			return getLinkingPictogramElement(baseElement);
		}
		
		if (selection instanceof IStructuredSelection) {
			Object o = ((IStructuredSelection)selection).getFirstElement();
			if (o instanceof PictogramElement)
				return (PictogramElement)o;
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
		Diagram diagram = BPMN2Editor.getActiveEditor().getDiagramTypeProvider().getDiagram();
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
		if (selection instanceof IStructuredSelection &&
				((IStructuredSelection) selection).isEmpty()==false) {
		
			Object firstElement = ((IStructuredSelection) selection).getFirstElement();
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