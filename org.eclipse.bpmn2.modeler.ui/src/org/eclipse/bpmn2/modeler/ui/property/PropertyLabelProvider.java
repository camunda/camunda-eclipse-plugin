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

import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateConnectionFeature;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2CreateFeature;
import org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNFeatureProvider;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.graphiti.dt.IDiagramType;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.impl.ImageImpl;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.internal.editor.DiagramEditorInternal;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.Workbench;

/**
 * @author Bob Brodt
 *
 */
public class PropertyLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
        BPMN2Editor editor = BPMN2Editor.getEditor( getBusinessObject(element) );
        
        if (editor!=null) {
		    BPMNFeatureProvider fp = (BPMNFeatureProvider)editor.getDiagramTypeProvider().getFeatureProvider();
			PictogramElement pe = getPictogramElement(element);
		    IFeature cf = fp.getCreateFeatureForPictogramElement(pe);
			if (cf instanceof AbstractBpmn2CreateFeature) {
				return GraphitiUi.getImageService().getImageForId(
						((AbstractBpmn2CreateFeature)cf).getCreateImageId());
			}
			if (cf instanceof AbstractBpmn2CreateConnectionFeature) {
				return GraphitiUi.getImageService().getImageForId(
						((AbstractBpmn2CreateConnectionFeature)cf).getCreateImageId());
			}
        }
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		PictogramElement pe = getPictogramElement(element);
		if (pe!=null) {
			EObject be = BusinessObjectUtil.getFirstElementOfType(pe, EObject.class);
			if (be!=null) {
				EStructuralFeature feature = be.eClass().getEStructuralFeature("name");
				if (feature!=null) {
					String name = (String)be.eGet(feature);
					if (name==null || name.isEmpty())
						name = "Unnamed " + be.eClass().getName();
					else
						name = be.eClass().getName() + " \"" + name + "\"";
					return name;
				}
				return be.eClass().getName();
			}
		}
		return super.getText(element);
	}

	EditPart getEditPart(Object element) {
		if (element instanceof IStructuredSelection &&
				((IStructuredSelection) element).isEmpty()==false) {
		
			Object firstElement = ((IStructuredSelection) element).getFirstElement();
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
	
	PictogramElement getPictogramElement(Object element) {
		EditPart editPart = getEditPart(element);
		if (editPart != null && editPart.getModel() instanceof PictogramElement) {
			return (PictogramElement) editPart.getModel();
		}
		return null;
	}
	
	EObject getBusinessObject(Object element) {
		PictogramElement pe = getPictogramElement(element);
		if (pe!=null)
			return BusinessObjectUtil.getFirstElementOfType(pe, EObject.class);
		return null;
	}
}
