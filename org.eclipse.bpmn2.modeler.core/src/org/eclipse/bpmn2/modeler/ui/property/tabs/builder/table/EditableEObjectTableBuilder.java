package org.eclipse.bpmn2.modeler.ui.property.tabs.builder.table;

import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EditableTableDescriptor;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EditableTableDescriptor.CellEditingStrategy;
import org.eclipse.bpmn2.modeler.ui.property.tabs.tables.EditableTableDescriptor.PostAddAction;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for editable tables
 * 
 * @author nico.rehwaldt
 *
 * @param <T>
 */
public class EditableEObjectTableBuilder<T extends EObject> extends EObjectTableBuilder<T> {

	public EditableEObjectTableBuilder(GFPropertySection section, Composite parent, Class<T> cls) {
		super(section, parent, cls);
	}

	@Override
	protected EditableTableDescriptor<T> createTableDescriptor() {
		EditableTableDescriptor<T> tableDescriptor = super.createTableDescriptor();
		
		tableDescriptor.setCellEditingStrategy(CellEditingStrategy.DOUBLE_CLICK);
		tableDescriptor.setPostAddAction(PostAddAction.EDIT);
		
		return tableDescriptor;
	}
}
