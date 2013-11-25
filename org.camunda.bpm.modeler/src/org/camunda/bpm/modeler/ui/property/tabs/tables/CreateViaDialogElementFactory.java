package org.camunda.bpm.modeler.ui.property.tabs.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.camunda.bpm.modeler.emf.util.CommandUtil;
import org.camunda.bpm.modeler.runtime.engine.model.FormFieldType;
import org.camunda.bpm.modeler.runtime.engine.model.ModelFactory;
import org.camunda.bpm.modeler.runtime.engine.model.ModelPackage;
import org.camunda.bpm.modeler.ui.property.tabs.tables.EditableTableDescriptor.ElementFactory;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.window.Window;

/**
 * A {@link ElementFactory} that displays a dialog to the user to
 * edit the newly created element.
 * 
 * Sub-classes must override the methods {@link #editInDialog(T)} to 
 * display the edit dialog and {@link #createType(T)} to create the element.
 * 
 * @author nico.rehwaldt
 *
 * @param <T>
 */
public abstract class CreateViaDialogElementFactory<T> extends ElementFactory<T> {
	
	private final TransactionalEditingDomain editingDomain;

	public CreateViaDialogElementFactory(TransactionalEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	/**
	 * Edit the newly created element in a dialog
	 * @param element
	 * @return
	 */
	protected abstract int editInDialog(T element);
	
	/**
	 * Creates the element
	 * 
	 * @return
	 */
	protected T createType() {
		return null;
	}
	
	@Override
	public T create() {
		T element = createType();

		CommandStack commandStack = editingDomain.getCommandStack();
		Command createCommand = commandStack.getMostRecentCommand();

		int returnCode = editInDialog(element);
		if (returnCode == Window.CANCEL) {
			// execute undo command when creation of new form field was cancelled
			CommandUtil.undo(editingDomain, createCommand);
			return null;
		} else {
			// squash commands (including creation of form field element
			// so that no fine-granular undoes are possible
			Command changeCommand = commandStack.getUndoCommand();
			
			List<Command> commands = new ArrayList<Command>();
			commands.add(createCommand);
			
			if (!changeCommand.equals(createCommand)) {
				commands.add(changeCommand);
			}
			
			CommandUtil.squash(editingDomain, commands);
		}

		return (T) element;
	}
}