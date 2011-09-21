package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import java.io.IOException;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.editor.BPMN2Editor;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesComposite;
import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertiesSection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class ProcessDiagramPropertySection extends AbstractBpmn2PropertiesSection {

	private ProcessDiagramPropertyComposite composite;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		composite = new ProcessDiagramPropertyComposite(parent, SWT.BORDER);
	}

	protected AbstractBpmn2PropertiesComposite getComposite() {
		return composite;
	}

	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			EObject be = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			if (be instanceof Participant){
				composite.setEObject((BPMN2Editor) getDiagramEditor(), ((Participant) be).getProcessRef());	
			}else if (be instanceof BPMNDiagram){
				try {
					composite.setEObject((BPMN2Editor) getDiagramEditor(), ModelHandlerLocator.getModelHandler(be.eResource()).getInternalParticipant().getProcessRef());
				} catch (IOException e) {
					Activator.showErrorWithLogging(e);
				}
			}else 
			composite.setEObject((BPMN2Editor) getDiagramEditor(), be);
			
			super.refresh();
		}
	}
}
