package org.camunda.bpm.modeler.ui.diagram.editor;

import java.util.ArrayList;

import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DefaultPaletteBehavior;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;

public class Bpmn2DiagramBehavior extends DiagramBehavior {

	public Bpmn2DiagramBehavior(Bpmn2Editor diagramContainer) {
		super(diagramContainer);
	}
	
	@Override
	public Bpmn2EditorUpdateBehavior createUpdateBehavior() {
		return new Bpmn2EditorUpdateBehavior(this);
	}
	
	@Override
	protected Bpmn2PersistencyBehavior createPersistencyBehavior() {
		return new Bpmn2PersistencyBehavior(this);
	}
	
	@Override
	protected Bpmn2RefreshBehavior createRefreshBehavior() {
		return new Bpmn2RefreshBehavior(this);
	}

	@Override
	public DefaultPaletteBehavior getPaletteBehavior() {
		// override for better visibility
		return super.getPaletteBehavior();
	}
	
	@Override
	public Bpmn2Editor getDiagramContainer() {
		// override for better type support
		return (Bpmn2Editor) super.getDiagramContainer();
	}
	
	@Override
	public PictogramElement[] getPictogramElementsForSelection() {
		// filter out invisible elements when setting selection
		PictogramElement[] pictogramElements = super.getPictogramElementsForSelection();
		if (pictogramElements==null)
			return null;
		ArrayList<PictogramElement> visibleList = new ArrayList<PictogramElement>();
		for (PictogramElement pe : pictogramElements) {
			if (pe.isVisible())
				visibleList.add(pe);
		}
		return visibleList.toArray(new PictogramElement[visibleList.size()]);
	}
}
