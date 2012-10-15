package org.eclipse.bpmn2.modeler.ui.dialog.importer;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class ProblemLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		Problem problem = (Problem) element;
		
		return problem.getSummary();
	}

	@Override
	public Image getImage(Object element) {
		Problem problem = (Problem) element;
		String imageName = problem.isWarning() ? ISharedImages.IMG_OBJS_WARN_TSK : ISharedImages.IMG_OBJS_ERROR_TSK;
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageName);
	}

}