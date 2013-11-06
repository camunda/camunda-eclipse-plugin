package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.core.features.activity.task.AbstractCreateTaskFeature;
import org.camunda.bpm.modeler.ui.diagram.Bpmn2FeatureProvider;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Task;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddTaskOperation extends AbstractAddShapeOperation<AbstractCreateTaskFeature<?>, AddTaskOperation> {

	private Class<?> taskCls = Task.class;
	
	public AddTaskOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider, AddTaskOperation.class);
	}
	
	public <T extends Task> AddTaskOperation ofType(Class<T> businessObjectCls) {
		this.taskCls = businessObjectCls;
		return this;
	}
	
	@Override
	protected AbstractCreateTaskFeature<?> createFeature(CreateContext context) {
		
		Bpmn2FeatureProvider featureProvider = (Bpmn2FeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider
				.getCreateFeatureForBusinessObject((EClass) Bpmn2Package.eINSTANCE.getEClassifier(taskCls.getSimpleName()));
		
		return (AbstractCreateTaskFeature<?>) createFeature;
	}

	public static AddTaskOperation addTask(IDiagramTypeProvider diagramTypeProvider) {
		return new AddTaskOperation(diagramTypeProvider);
	}
}
