package org.camunda.bpm.modeler.test.emf.util;

import static org.camunda.bpm.modeler.test.util.TestHelper.createModel;
import static org.camunda.bpm.modeler.test.util.TestHelper.transactionalExecute;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Stack;

import org.camunda.bpm.modeler.emf.util.CommandUtil;
import org.camunda.bpm.modeler.test.util.TestHelper.ModelResources;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.Task;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.junit.Test;

/**
 * Testing the {@link CommandUtil}
 * @author nico.rehwaldt
 */
public class CommandUtilTest {

	@Test
	public void testSquash() throws Exception {
		// given
		ModelResources resources = createModel("org/camunda/bpm/modeler/test/emf/util/CommandUtilTest.testBase.bpmn");
		Resource resource = resources.getResource();
		TransactionalEditingDomain editingDomain = resources.getEditingDomain();
		CommandStack commandStack = editingDomain.getCommandStack();
		
		final Process process = (Process) resource.getEObject("_Process_2");
		
		Stack<Command> commands = new Stack<Command>();
		
		// 3 commands on stack
		// #1
		transactionalExecute(editingDomain, new Runnable() {
			
			@Override
			public void run() {
				process.getFlowElements().add(Bpmn2Factory.eINSTANCE.createTask());
			}
		});
		
		commands.push(commandStack.getMostRecentCommand());
		
		// #2
		transactionalExecute(editingDomain, new Runnable() {
			
			@Override
			public void run() {
				process.getFlowElements().add(Bpmn2Factory.eINSTANCE.createTask());
			}
		});

		commands.push(commandStack.getMostRecentCommand());
		
		// #3
		transactionalExecute(editingDomain, new Runnable() {
			
			@Override
			public void run() {
				process.getFlowElements().add(Bpmn2Factory.eINSTANCE.createTask());
			}
		});

		commands.push(commandStack.getMostRecentCommand());
		
		// when
		CommandUtil.squash(editingDomain, commands);
		
		// then

		///// undo works ////
		
		// undo should be possible
		assertThat(commandStack.canUndo()).isTrue();

		// expect only one command on stack
		commandStack.undo();
		assertThat(commandStack.canUndo()).isFalse();
		
		// changes are actually undone
		assertThat(process.getFlowElements()).isEmpty();
		
		
		///// redo works ////

		// redo should be possible
		assertThat(commandStack.canRedo());
		commandStack.redo();

		assertThat(process.getFlowElements()).hasSize(3);
	}

	@Test
	public void testPartialSquash() throws Exception {
		// given
		ModelResources resources = createModel("org/camunda/bpm/modeler/test/emf/util/CommandUtilTest.testBase.bpmn");
		Resource resource = resources.getResource();
		TransactionalEditingDomain editingDomain = resources.getEditingDomain();
		CommandStack commandStack = editingDomain.getCommandStack();
		
		final Process process = (Process) resource.getEObject("_Process_2");

		Stack<Command> commands = new Stack<Command>();
		
		final Task task = Bpmn2Factory.eINSTANCE.createTask();

		// 3 commands on stack
		// #1
		transactionalExecute(editingDomain, new Runnable() {
			
			@Override
			public void run() {
				process.getFlowElements().add(task);
				task.setCompletionQuantity(0);
				task.setName("");
			}
		});
		
		commands.push(commandStack.getMostRecentCommand());
		
		// #2
		transactionalExecute(editingDomain, new Runnable() {
			
			@Override
			public void run() {
				task.setName("My Task");
			}
		});

		commands.push(commandStack.getMostRecentCommand());
		
		// #3
		transactionalExecute(editingDomain, new Runnable() {
			
			@Override
			public void run() {
				task.setCompletionQuantity(5);
			}
		});

		commands.push(commandStack.getMostRecentCommand());
		
		// when
		CommandUtil.squash(editingDomain, Arrays.asList(commands.get(2)));
		
		// then

		///// undo last works ////
		
		// undo should be possible
		assertThat(commandStack.canUndo()).isTrue();

		// expect 2 commands on stack
		commandStack.undo();
		assertThat(commandStack.canUndo()).isTrue();
		
		// changes are actually undone
		assertThat(task.getName()).isEqualTo("My Task");
		assertThat(task.getCompletionQuantity()).isEqualTo(0);

		// when
		CommandUtil.squash(editingDomain, Arrays.asList(commands.get(0), commands.get(1)));

		// undo should be possible
		assertThat(commandStack.canUndo()).isTrue();

		// expect 1 command on stack
		commandStack.undo();
		assertThat(commandStack.canUndo()).isFalse();
		
		// changes are actually undone
		assertThat(task.getName()).isNull();
		assertThat(task.getCompletionQuantity()).isEqualTo(1);
		
		assertThat(process.getFlowElements()).isEmpty();
	}
}
