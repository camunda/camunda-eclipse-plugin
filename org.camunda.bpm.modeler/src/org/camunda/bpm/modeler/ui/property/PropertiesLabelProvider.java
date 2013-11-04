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

package org.camunda.bpm.modeler.ui.property;

import org.camunda.bpm.modeler.core.features.AbstractBpmn2CreateFeature;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.Images;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.ui.diagram.Bpmn2FeatureProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author Bob Brodt
 * 
 */
public class PropertiesLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		
		EObject businessObject = BusinessObjectUtil.getBusinessObjectForSelection((ISelection) element);
		DiagramEditor editor = ModelUtil.getEditor(businessObject);

		if (editor != null) {
			Bpmn2FeatureProvider fp = (Bpmn2FeatureProvider) editor.getDiagramTypeProvider().getFeatureProvider();
			PictogramElement pe = BusinessObjectUtil.getPictogramElementForSelection((ISelection) element);
			IFeature cf = fp.getCreateFeatureForPictogramElement(pe);
			if (cf instanceof AbstractBpmn2CreateFeature) {
				return Images.getById(((AbstractBpmn2CreateFeature<?>) cf).getCreateImageId());
			}
		}
		
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		EObject be = BusinessObjectUtil.getBusinessObjectForSelection((ISelection) element);
		
		if (be != null) {
			return ModelUtil.getDisplayName(be);
		}
		
		PictogramElement pictogramElement = BusinessObjectUtil.getPictogramElementForSelection((ISelection) element);
		if (pictogramElement != null && pictogramElement.getGraphicsAlgorithm() != null) {
			return ModelUtil.getLabel(pictogramElement.getGraphicsAlgorithm());
		}
		
		return super.getText(element);
	}
}
