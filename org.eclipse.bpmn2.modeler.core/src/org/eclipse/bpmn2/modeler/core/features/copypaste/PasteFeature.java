package org.eclipse.bpmn2.modeler.core.features.copypaste;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IPasteContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.AbstractPasteFeature;

/**
 * Paste flow elements feature
 * 
 * @author nico.rehwaldt
 */
public class PasteFeature extends AbstractPasteFeature {

	public PasteFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canPaste(IPasteContext context) {
		ContainerShape target = getPasteTarget(context);
		if (target == null) {
			return false;
		}
		
		List<PictogramElement> copiedElements = getClipboardContents(context);
		if (copiedElements == null || copiedElements.isEmpty()) {
			return false;
		}
		
		if (CopyAndPasteUtil.getSharedParent(copiedElements) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	private List<PictogramElement> getClipboardContents(IPasteContext context) {

		Object[] objects = getFromClipboard();
		
		if (objects instanceof EObject[]) {
			List<PictogramElement> pictogramElements = new ArrayList<PictogramElement>();
			for (Object o: objects) {
				if (o instanceof PictogramElement) {
					pictogramElements.add((PictogramElement) o);
				}
			}
			return pictogramElements;
		} else {
			return null;
		}
	}

	private ContainerShape getPasteTarget(IPasteContext context) {
		
		PictogramElement[] pictogramElements = context.getPictogramElements();
		
		if (pictogramElements.length > 1) {
			return CopyAndPasteUtil.getSharedParent(pictogramElements);
		}
		
		if (pictogramElements.length == 1) {
			PictogramElement target = pictogramElements[0];
			if (target instanceof ContainerShape) {
				return (ContainerShape) target;
			} else {
				return null;
			}
		}
		
		return getDiagram();
	}

	@Override
	public void paste(IPasteContext context) {
		PictogramElement[] pictogramElements = context.getPictogramElements();
		
		
		System.out.println("WOULD PASTE");
	}
}
