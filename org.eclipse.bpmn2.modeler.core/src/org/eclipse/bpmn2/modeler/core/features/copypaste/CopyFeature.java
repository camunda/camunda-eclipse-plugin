package org.eclipse.bpmn2.modeler.core.features.copypaste;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICopyContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.AbstractCopyFeature;

/**
 * Copy flow elements feature
 * 
 * @author nico.rehwaldt
 */
public class CopyFeature extends AbstractCopyFeature {

	public CopyFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canCopy(ICopyContext context) {
		
		PictogramElement[] pictogramElements = context.getPictogramElements();

		if (pictogramElements.length == 0) {
			return false;
		}
		
		// we may copy IF
		// * Copied shapes have the same parent
		// * Copied shapes are shapes
		// * Copied shapes link to flow elements
		ContainerShape sharedParent = CopyAndPasteUtil.getSharedParent(pictogramElements);
		if (sharedParent == null) {
			return false;
		}
		
		for (PictogramElement pictogramElement : pictogramElements) {
			BaseElement businessObject = BusinessObjectUtil.getFirstBaseElement(pictogramElement);
			
			if (// businessObject instanceof Lane ||
				// businessObject instanceof Participant || 
				businessObject instanceof FlowElement)  {
				continue;
			} else {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void copy(ICopyContext context) {

		// put stuff to clipboard
		putToClipboard(context.getPictogramElements());
	}
}
