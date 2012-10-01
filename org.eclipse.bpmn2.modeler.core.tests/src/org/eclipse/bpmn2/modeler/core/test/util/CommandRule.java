package org.eclipse.bpmn2.modeler.core.test.util;

import java.lang.reflect.Method;

import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractTestCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class CommandRule implements MethodRule {

	@Override
	public Statement apply(final Statement base, final FrameworkMethod method, Object target) {

		DiagramResource resource = method.getAnnotation(DiagramResource.class);
		
		if (resource != null && target instanceof AbstractImportBpmn2ModelTest) {
			final AbstractImportBpmn2ModelTest testCase = (AbstractImportBpmn2ModelTest) target;
			
			String resourceUrl = getResourceUrl(resource, target, method.getMethod());
			
			return new StatementExtension(testCase, resourceUrl, base);
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

	private class StatementExtension extends Statement {
		
		private final AbstractImportBpmn2ModelTest testCase;
		private final String resourceUrl;
		private final Statement base;

		private Throwable thrownException;
		
		private StatementExtension(AbstractImportBpmn2ModelTest testCase, String resourceUrl, Statement base) {
			this.testCase = testCase;
			this.resourceUrl = resourceUrl;
			this.base = base;
		}

		@Override
		public void evaluate() throws Throwable {
			try {
				TransactionalEditingDomain domain = testCase.createEditingDomain(resourceUrl);
				
				domain.getCommandStack().execute(new AbstractTestCommand(testCase, "test.bpmn") {
					
					@Override
					public void test(IDiagramTypeProvider diagramTypeProvider, Diagram diagram) {
						testCase.setDiagramTypeProvider(diagramTypeProvider);
						testCase.setDiagram(diagram);
						
						try {
							base.evaluate();
						} catch (Throwable e) {
							setThrownException(e);
						}
					}
				});
				
				Throwable e = getThrownException();
				if (e != null) {
					throw e;
				}
			} finally {
				testCase.disposeEditingDomain();
			}
		}
		
		public void setThrownException(Throwable thrownException) {
			this.thrownException = thrownException;
		}
		
		public Throwable getThrownException() {
			return thrownException;
		}
	}
}
