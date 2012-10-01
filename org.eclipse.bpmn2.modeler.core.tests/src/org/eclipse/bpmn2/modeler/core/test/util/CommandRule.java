package org.eclipse.bpmn2.modeler.core.test.util;

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
			
			String resourceUrl = resource.value();
			if (resourceUrl == null || resourceUrl.isEmpty()) {
				resourceUrl = target.getClass().getName().replaceAll("\\.", "/") + "." + method.getName() + ".bpmn";
			}
			
			final String finalResourceUrl = resourceUrl;
			
			return new Statement() {
				
				@Override
				public void evaluate() throws Throwable {
					try {
						TransactionalEditingDomain domain = testCase.createEditingDomain(finalResourceUrl);
						
						domain.getCommandStack().execute(new AbstractTestCommand(testCase, "test.bpmn") {
							
							@Override
							public void test(IDiagramTypeProvider diagramTypeProvider, Diagram diagram) {
								testCase.setDiagramTypeProvider(diagramTypeProvider);
								testCase.setDiagram(diagram);
								
								try {
									base.evaluate();
								} catch (Throwable e) {
									throw new RuntimeException(e);
								}
							}
						});
					} finally {
						testCase.disposeEditingDomain();
					}
				}
			};
		} else {
			return base;
		}
	}
}
