package org.camunda.bpm.modeler.ui.property.tabs.dialog;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Stack;

import org.camunda.bpm.modeler.emf.util.CommandUtil;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.IWorkspaceCommandStack;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * Dialog which handle undo and redo command for the current command stack size
 * 
 * CANCEL button and ESC key close the dialog and nothing was changed 
 * 
 * @author kristin.polenz
 *
 */
public class ModelerDialog extends Dialog {

	private EObject model;

	protected ModelerDialog(Shell parentShell, EObject model) {
		super(parentShell);

		this.model = model;
	}

	private CommandStackListener commandStackListener;

	private TransactionalEditingDomain editingDomain;

	private IHandlerService handlerService;
	private List<IHandlerActivation> actionActivationList = new ArrayList<IHandlerActivation>();

	private Stack<Command> undoStack;

	// CTRL+Z implementation to execute it when
	// the dialog is closed via ESC key or cancel button
	@Override
	public void create() {
		super.create();

		registerUndoListener();

		// create handler to handle undo / redo commands in this dialog
		handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
		// create undo action to activate handler for this action
		UndoAction undoAction = new UndoAction(editingDomain);
		undoAction.setActionDefinitionId(ActionFactory.UNDO.getCommandId());
		// enabled undo action to execute the command
		undoAction.setEnabled(true);

		// create redo action to activate handler for this action
		RedoAction redoAction = new RedoAction(editingDomain);
		redoAction.setActionDefinitionId(ActionFactory.REDO.getCommandId());
		// enabled redo action to execute the command
		redoAction.setEnabled(true);

		// activate handler for the given action
		// note: it is necessary to deactivate the handlers in the dialog close method
		IHandlerActivation undoActionActivation = handlerService.activateHandler(undoAction.getActionDefinitionId(), new ActionHandler(undoAction));
		IHandlerActivation redoActionActivation = handlerService.activateHandler(redoAction.getActionDefinitionId(), new ActionHandler(redoAction));
		actionActivationList.add(undoActionActivation);
		actionActivationList.add(redoActionActivation);
	}

	private void registerUndoListener() {
		undoStack = new Stack<Command>();

		commandStackListener = new CommandStackListener() {
			@Override
			public void commandStackChanged(EventObject event) {
				final IWorkspaceCommandStack transactionalCommandStack = (IWorkspaceCommandStack) event.getSource();

				Command lastCommand = transactionalCommandStack.getMostRecentCommand();
				Command undoCommand = transactionalCommandStack.getUndoCommand();

				boolean isUndo = !lastCommand.equals(undoCommand);
				if (!isUndo) {
					undoStack.push(lastCommand);
				} else {
					if (undoStack.isEmpty()) {
						// close dialog on undo behind open command
						// prevents the user from editing a detached (i.e. newly created and 
						// removed via undo) FormField object

						// redo the command that happened prior to dialog open
						// we need to do this to make sure that the user cannot undo things he did
						// before the dialog got opened (while the dialog is active)
						// must close dialog asynchronously to not interfere with undo action processing
						Display.getCurrent().asyncExec(new Runnable() {
							@Override
							public void run() {

								// remove listener to prepare for close
								removeUndoListener();

								// after this operation the openCommand is on top of the command stack
								transactionalCommandStack.redo();

								// set null so that no undo is done during close
								cancelPressed();
							}
						});

					} else {
						Command commandsTop = undoStack.peek();

						// only pop undo stack if undo (EMF-wise) is actually possible
						// if it is no more possible, a miss match between 
						// the last command (successfully undone by EMF) and the current undoStack top exists
						// we use this miss match to figure out if EMF was able to undo the last command
						if (lastCommand.equals(commandsTop)) {
							undoStack.pop();
						}
					}
				}
			}
		};

		editingDomain = TransactionUtil.getEditingDomain(model);
		CommandStack commandStack = editingDomain.getCommandStack();

		commandStack.addCommandStackListener(commandStackListener);
	}

	@Override
	protected void okPressed() {
		removeUndoListener();
		
		CommandUtil.squash(editingDomain, undoStack);
		super.okPressed();
	}
	
	protected void removeUndoListener() {
		CommandStack commandStack = editingDomain.getCommandStack();
		commandStack.removeCommandStackListener(commandStackListener);
	}

	private void undoChanges() {
		removeUndoListener();

		while (!undoStack.isEmpty()) {
			Command command = undoStack.pop();
			internalUndoChange(command);
		}
	}

	private void internalUndoChange(Command command) {
		CommandUtil.undo(editingDomain, command);
	}

	// deactivate undo / redo action handler
	private void deactivateHandlerActivation() {
		handlerService.deactivateHandlers(actionActivationList);
	}

	@Override
	public boolean close() {
		removeUndoListener();
		deactivateHandlerActivation();
		return super.close();
	}

	@Override
	protected void cancelPressed() {
		undoChanges();
		super.cancelPressed();
	}

	@Override
	protected void handleShellCloseEvent() {
		undoChanges();
		super.handleShellCloseEvent();
	}	
}