package org.camunda.bpm.modeler.ui.property.tabs.builder.table;

import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.CellEditingStrategy;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.PostAddAction;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * Builder for editable tables
 * 
 * @author nico.rehwaldt
 *
 * @param <T>
 */
public class EditableEObjectTableBuilder<T extends EObject> extends EObjectTableBuilder<T> {

	protected EditRowHandler<T> doubleClickEditHandler;

	public EditableEObjectTableBuilder(GFPropertySection section, Composite parent, Class<T> cls) {
		super(section, parent, cls);
	}

	public EditableEObjectTableBuilder<T> doubleClickEditRowHandler(EditRowHandler<T> editHandler) {
		this.doubleClickEditHandler = editHandler;

		return this;
	}

	@Override
	protected EditableTableDescriptor<T> createTableDescriptor() {
		EditableTableDescriptor<T> tableDescriptor = super.createTableDescriptor();

		if (doubleClickEditHandler != null) {
			tableDescriptor.setCellEditingStrategy(CellEditingStrategy.NO_EDIT);
		} else {
			tableDescriptor.setCellEditingStrategy(CellEditingStrategy.DOUBLE_CLICK);
		}
		tableDescriptor.setPostAddAction(PostAddAction.EDIT);

		return tableDescriptor;
	}

	@Override
	protected void establishModelViewBinding(EObject model, TableViewer tableViewer) {
		super.establishModelViewBinding(model, tableViewer);

		if (doubleClickEditHandler != null) {
			tableViewer.addDoubleClickListener(new IDoubleClickListener() {
				@Override
				public void doubleClick(DoubleClickEvent event) {

					ISelection selection = event.getSelection();
					IStructuredSelection structuredSelection = (IStructuredSelection) selection;

					T element = (T) structuredSelection.getFirstElement();
					if (doubleClickEditHandler.canEdit(element)) {
						doubleClickEditHandler.rowEdit(element);
					}
				}
			});
		}
	}
}
