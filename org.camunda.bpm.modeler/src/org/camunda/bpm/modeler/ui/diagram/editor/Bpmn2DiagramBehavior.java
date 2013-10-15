package org.camunda.bpm.modeler.ui.diagram.editor;

import java.util.ArrayList;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DefaultPersistencyBehavior;
import org.eclipse.graphiti.ui.editor.DefaultUpdateBehavior;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditorContextMenuProvider;
import org.eclipse.graphiti.ui.editor.IDiagramContainerUI;
import org.eclipse.graphiti.ui.platform.IConfigurationProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;

public class Bpmn2DiagramBehavior extends DiagramBehavior {

	public Bpmn2DiagramBehavior(IDiagramContainerUI diagramContainer) {
		super(diagramContainer);
	}
	
	@Override
	public DefaultUpdateBehavior createUpdateBehavior() {
		return new Bpmn2EditorUpdateBehavior(this);
	}
	
	@Override
	protected DefaultPersistencyBehavior createPersistencyBehavior() {
		return new Bpmn2PersistencyBehavior(this);
	}
	
	@Override
	protected ContextMenuProvider createContextMenuProvider() {
		return new DiagramEditorContextMenuProvider(getDiagramContainer().getGraphicalViewer(), getDiagramContainer().getActionRegistry(), (IConfigurationProvider) getConfigurationProvider()) {
			@Override
			public void buildContextMenu(IMenuManager manager) {
				super.buildContextMenu(manager);
				IAction action = getDiagramContainer().getActionRegistry().getAction("show.or.hide.source.view");
				action.setText(action.getText());
				manager.add(action);
			}
		};
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
