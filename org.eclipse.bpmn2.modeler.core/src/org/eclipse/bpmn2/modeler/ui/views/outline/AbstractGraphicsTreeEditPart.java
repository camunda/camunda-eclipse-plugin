package org.eclipse.bpmn2.modeler.ui.views.outline;

import java.lang.reflect.Field;

import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.IConstants;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */
public class AbstractGraphicsTreeEditPart extends AbstractTreeEditPart {

	DiagramTreeEditPart diagramEditPart;
	
	public AbstractGraphicsTreeEditPart(DiagramTreeEditPart dep, Object model) {
		super(model);
		diagramEditPart = dep;
	}

	protected void setDiagramEditPart(DiagramTreeEditPart dep) {
		diagramEditPart = dep;
	}
	
	@Override
	public Object getModel() {
		EObject bpmnModel = (EObject)super.getModel();
		PictogramElement pictogramElement = BusinessObjectUtil.getPictogramForElement(bpmnModel);
		
		if (pictogramElement == null) {
			return bpmnModel;
		}else {
			return pictogramElement;
		}
	}

	public Object getBpmnModel() {
		return super.getModel();
	}

	/**
	 * This method is called from refreshVisuals(), to display the image of the
	 * TreeItem.
	 * <p>
	 * By default this method displays the image of the FIRST attribute of the
	 * ModelObject as the TreeItem.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected Image getImage() {
		EObject o = (EObject)getBpmnModel();
		String field = "ICON_" + o.eClass().getName().toUpperCase();
		Field f;
		try {
			f = IConstants.class.getField(field);
			if (f!=null)
				return Activator.getDefault().getImage((String)f.get(null));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * This method is called by refreshVisuals(), to display the text of the
	 * TreeItem.
	 * <p>
	 * By default this method displays the FIRST attribute of the model Object
	 * as the TreeItem.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected String getText() {
		String text = null;
		if (getModel() instanceof EObject) {
			EObject o = (EObject)getBpmnModel();
			text = getText(o);
		}
		return text == null ? "" : text;
	}
	
	protected String getText(EObject o) {
		if (o==null)
			return "";
		String text = ModelUtil.getDisplayName(o);
		if (text==null || text.isEmpty()) {
			EStructuralFeature f = o.eClass().getEStructuralFeature("id");
			if (f!=null)
				text = o.eGet(f).toString();
		}
		return text;
	}
}