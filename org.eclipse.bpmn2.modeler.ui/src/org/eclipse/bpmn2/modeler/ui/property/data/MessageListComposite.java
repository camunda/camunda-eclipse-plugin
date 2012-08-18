package org.eclipse.bpmn2.modeler.ui.property.data;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.ListCompositeColumnProvider;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.TableColumn;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class MessageListComposite extends DefaultListComposite {

	public MessageListComposite(AbstractBpmn2PropertySection section, int style) {
		super(section, style);
		// TODO Auto-generated constructor stub
	}

	public MessageListComposite(AbstractBpmn2PropertySection section) {
		super(section,
				AbstractListComposite.SHOW_DETAILS |
				AbstractListComposite.ADD_BUTTON |
				AbstractListComposite.MOVE_BUTTONS |
				AbstractListComposite.DELETE_BUTTON);
	}

	public MessageListComposite(Composite parent, int style) {
		super(parent,
				AbstractListComposite.SHOW_DETAILS |
				AbstractListComposite.ADD_BUTTON |
				AbstractListComposite.MOVE_BUTTONS |
				AbstractListComposite.DELETE_BUTTON);
	}
	
	@Override
	public ListCompositeColumnProvider getColumnProvider(EObject object, EStructuralFeature feature) {
		if (columnProvider==null) {
			columnProvider = new ListCompositeColumnProvider(this, false);
			columnProvider.add(new TableColumn(object,Bpmn2Package.eINSTANCE.getMessage_Name()));
			columnProvider.add(new TableColumn(object,Bpmn2Package.eINSTANCE.getMessage_ItemRef()));
		}
		return columnProvider;
	}
}
