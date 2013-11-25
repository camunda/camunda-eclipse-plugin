package org.camunda.bpm.modeler.emf.util;

import java.util.List;
import java.util.ListIterator;

import org.camunda.bpm.modeler.core.Activator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.emf.transaction.util.ConditionalRedoCommand;

/**
 * Utilities to work with emf commands in a transactional context.
 * 
 * @author kristin.polenz
 * @author nico.rehwaldt
 */
public class CommandUtil {

	/**
	 * Undoes a command in the context of a given editing domain even if the command is not part of the 
	 * editing history any more.
	 * 
	 * @param editingDomain
	 * @param command
	 */
	public static void undo(TransactionalEditingDomain editingDomain, Command command) {
		CommandStack commandStack = editingDomain.getCommandStack();
		
		// check whether undo command exists in command stack history
		// if we have undo command in command stack history use the command stack to undo it
		// we need to do this to not destroy the command stack history
		if (commandStack.getUndoCommand() != null && commandStack.getUndoCommand().equals(command)) {
			commandStack.undo();
		} else {
			
			// no undo history entry exists, force "undo" via 
			// home baked transaction magic
			InternalTransactionalEditingDomain internalEditingDomain = (InternalTransactionalEditingDomain) editingDomain;
			
			try {
				InternalTransaction transaction = internalEditingDomain.startTransaction(false, null);
				
				try {
					// internal transaction needs to be running to execute this method
					command.undo();
					
					transaction.commit();
				} catch (RollbackException e) {
					transaction.rollback();
					
					throw new IllegalStateException("Failed to undo command", e);
				}
			} catch (InterruptedException e) {
				Activator.logStatus(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Failed to rollback changed", e)); // cannot handle
			}
		}
	}

	/**
	 * Combines all {@link Command}s on the command stack into one un- and redoable command.
	 * 
	 * This method expects commands to be in the correct (first .. last executed) order, if they are still 
	 * part of the editing domains history.
	 * 
	 * @param editingDomain
	 * @param commands
	 */
	public static void squash(TransactionalEditingDomain editingDomain, List<Command> commands) {
		
		// nothing to squash? quick return!
		if (commands.size() < 2) {
			return;
		}
		
		ListIterator<Command> iterator;
		
		// iterate n...1
		iterator = commands.listIterator(commands.size());
		
		// undo all commands
		while (iterator.hasPrevious()) {
			Command command = iterator.previous();
			undo(editingDomain, command);
		}
		
		// compound command comprising all undone commands
		final Command compoundCommand = new ConditionalRedoCommand.Compound(commands);
		
		// execute compound command in a recording manner
		// so that it may later be undone
		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
			@Override
			protected void doExecute() {
				compoundCommand.execute();
			}
		});
	}
}
