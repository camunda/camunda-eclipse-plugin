package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import java.io.IOException;

import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class DataItemsPropertySection extends AbstractBpmn2PropertySection {

	public DataItemsPropertySection() {
	}

	@Override
	protected AbstractBpmn2PropertiesComposite createSectionRoot() {
		return new DataItemsPropertiesComposite(this);
	}

	@Override
	protected EObject getBusinessObjectForPictogramElement(PictogramElement pe) {
		EObject be = super.getBusinessObjectForPictogramElement(pe);
		if (be instanceof BPMNDiagram) {
//			EList<EObject> contents = be.eResource().getContents();
//			if (!contents.isEmpty() && contents.get(0) instanceof DocumentRoot) {
//				return contents.get(0);
//			}
			try {
				return ModelHandlerLocator.getModelHandler(be.eResource()).getDefinitions();
			} catch (IOException e) {
				Activator.showErrorWithLogging(e);
			}
		}
		return null;
	}

}
