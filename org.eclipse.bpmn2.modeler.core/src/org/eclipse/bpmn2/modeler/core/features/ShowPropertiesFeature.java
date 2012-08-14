package org.eclipse.bpmn2.modeler.core.features;

import org.eclipse.bpmn2.modeler.core.IConstants;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditingDialog;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.jface.window.Window;

public class ShowPropertiesFeature extends AbstractCustomFeature {

	private boolean hasDoneChanges = false;

	public ShowPropertiesFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Show Properties";
	}

	@Override
	public String getDescription() {
		return "Display a Property configuration popup dialog for the selected item";
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		EObject businessObject = BusinessObjectUtil.getBusinessObjectForPictogramElement(pes[0]);
		if (pes.length==1) {
			return Bpmn2Preferences.getInstance().hasPopupConfigDialog(businessObject);
		}
		return false;
	}

	@Override
	public boolean isAvailable(IContext context) {
		return true;
	}

	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		DiagramEditor ed = (DiagramEditor)getDiagramEditor();
		ed.setPictogramElementForSelection(pes[0]);
		ed.refresh();
		EObject businessObject = BusinessObjectUtil.getBusinessObjectForPictogramElement(pes[0]);
		ObjectEditingDialog dialog =
				new ObjectEditingDialog(ed, businessObject);
		hasDoneChanges = (dialog.open()==Window.OK);
	}

	@Override
	public boolean hasDoneChanges() {
		return hasDoneChanges;
	}

	@Override
	public String getImageId() {
		return IConstants.ICON_PROPERTIES_16;
	}

}
