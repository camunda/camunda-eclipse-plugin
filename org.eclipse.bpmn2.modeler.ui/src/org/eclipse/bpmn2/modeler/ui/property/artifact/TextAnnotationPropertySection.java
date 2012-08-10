package org.eclipse.bpmn2.modeler.ui.property.artifact;

import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.ui.property.PropertiesCompositeFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
/**
 * 
 * @author hien quoc dang
 *
 */
public class TextAnnotationPropertySection extends AbstractBpmn2PropertySection implements ITabbedPropertyConstants{
	static {
		PropertiesCompositeFactory.register(TextAnnotation.class, TextAnnotationDetailComposite.class);
	}

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new TextAnnotationDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new TextAnnotationDetailComposite(parent,style);
	}

	@Override
	protected EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		EObject be = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
		if (be instanceof TextAnnotation)
			return be;
		return null;
	}
}
