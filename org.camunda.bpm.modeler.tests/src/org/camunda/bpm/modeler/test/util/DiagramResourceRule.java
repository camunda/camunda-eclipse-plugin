package org.camunda.bpm.modeler.test.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.camunda.bpm.modeler.test.AbstractEditorTest;
import org.camunda.bpm.modeler.test.AbstractTestCommand;
import org.camunda.bpm.modeler.test.util.TestHelper.ModelResources;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * A method rule which creates a diagram around a <code>@DiagramResource</code> annotated method.
 * 
 * The test invocation will be transactional (ie. inside a EMF transaction) unless the method
 * is annotated with <code>@NonTransactional</code>. 
 * 
 * @author nico.rehwaldt
 * 
 * @see NonTransactional
 * @see DiagramResource
 */
public class DiagramResourceRule implements MethodRule {
	
	@Override
	public Statement apply(final Statement base, final FrameworkMethod method, Object target) {

		boolean testTransactional = 
			method.getAnnotation(NonTransactional.class) == null &&
			!isAnnotationPresent(NonTransactional.class, target.getClass());
		
		DiagramResource resource = method.getAnnotation(DiagramResource.class);
		
		if (resource != null && target instanceof AbstractEditorTest) {
			final AbstractEditorTest testCase = (AbstractEditorTest) target;
			
			String resourceUrl = getResourceUrl(resource, target, method.getMethod());
			
			return new StatementExtension(testCase, method, resourceUrl, base, testTransactional);
		} else {
			return base;
		}
	}
	
	private String getResourceUrl(DiagramResource resource, Object o, Method m) {
		String url = resource.value();
		if (url == null || url.isEmpty()) {
			url = o.getClass().getName().replaceAll("\\.", "/") + "." + m.getName() + ".bpmn";
		}
		
		return url;
	}

	private boolean isAnnotationPresent(Class<? extends Annotation> cls, Class<?> targetCls) { 
		if (targetCls.equals(Object.class)) {
			return false;
		}
		
		if (targetCls.isAnnotationPresent(NonTransactional.class)) {
			return true;
		} else {
			return isAnnotationPresent(cls, targetCls.getSuperclass());
		}
	}
	
	private class StatementExtension extends Statement {
		
		private final AbstractEditorTest testCase;
		private final FrameworkMethod testMethod;
		
		private final String diagramUrl;
		private final Statement baseStatement;
		
		/**
		 * Whether the test itself should be run transactional
		 */
		private boolean transactional;
		
		private StatementExtension(AbstractEditorTest testCase, FrameworkMethod testMethod, String resourceUrl, Statement baseStatement, boolean transactional) {
			this.testCase = testCase;
			this.testMethod = testMethod;
			
			this.diagramUrl = resourceUrl;
			this.baseStatement = baseStatement;
			this.transactional = transactional;
		}

		@Override
		public void evaluate() throws Throwable {
			File tempDir = testCase.getTempFolder().getRoot();

			ModelResources modelResources = TestHelper.createModel(diagramUrl);
			
			try {
				TransactionalEditingDomain domain = modelResources.getEditingDomain();
				
				AbstractTestCommand command = new AbstractTestCommand(testCase, testMethod.getName(), modelResources, tempDir) {
					
					@Override
					public void execute(IDiagramTypeProvider diagramTypeProvider, Diagram diagram) throws Throwable {
						if (transactional) {
							baseStatement.evaluate();
						}
					}
				};
				
				domain.getCommandStack().execute(command);
				
				Throwable e = command.getRecordedException();
				if (e != null) {
					throw e;
				}
				
				// run test in the non-transactional case
				if (!transactional) {
					baseStatement.evaluate();
				}
			} finally {
				testCase.setEditorResources(null);
				testCase.setModelResources(null);
				
				if (modelResources != null) {
					try {
						modelResources.getEditingDomain().dispose();
					} catch (Exception e) {
						// cannot handle
					}
				}
			}
		}
	}
}
