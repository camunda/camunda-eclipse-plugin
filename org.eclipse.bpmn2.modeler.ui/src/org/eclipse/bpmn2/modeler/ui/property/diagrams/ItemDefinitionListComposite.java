package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import org.eclipse.bpmn2.modeler.ui.property.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.ui.property.DefaultListComposite;
import org.eclipse.bpmn2.modeler.ui.property.TableColumn;
import org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite.ListCompositeColumnProvider;
import org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite.ListCompositeColumnProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

public class ItemDefinitionListComposite extends DefaultListComposite {

	public ItemDefinitionListComposite(AbstractBpmn2PropertySection section, int style) {
		super(section, style);
	}

	public ItemDefinitionListComposite(AbstractBpmn2PropertySection section) {
		super(section, DEFAULT_STYLE|EDIT_BUTTON);
	}

	public ItemDefinitionListComposite(Composite parent, int style) {
		super(parent, style);
	}

	public ItemDefinitionListComposite(Composite parent) {
		super(parent, DEFAULT_STYLE|EDIT_BUTTON);
	}

	public ListCompositeColumnProvider getColumnProvider(EObject object, EStructuralFeature feature) {
		if (columnProvider==null) {
			ListCompositeColumnProvider cp = new ListCompositeColumnProvider(this,true);
			EClass eclass = PACKAGE.getItemDefinition();
			
			cp.add(object,PACKAGE.getItemDefinition_StructureRef());
			cp.add(object,PACKAGE.getItemDefinition_ItemKind());
			cp.add(object,PACKAGE.getItemDefinition_IsCollection());

			columnProvider = cp; 
		}
		return columnProvider;
	}
}
