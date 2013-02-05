package org.eclipse.bpmn2.modeler.core.test.util.operations;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AbstractCreateTaskFeature;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMN2FeatureProvider;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.context.impl.CreateContext;

public class AddTaskOperation extends AbstractAddOperation<AbstractCreateTaskFeature<?>, AddTaskOperation> {

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
		
		BPMN2FeatureProvider featureProvider = (BPMN2FeatureProvider) diagramTypeProvider.getFeatureProvider();
		IFeature createFeature = featureProvider.getCreateFeatureForBusinessObject(taskCls);
		
		return (AbstractCreateTaskFeature<?>) createFeature;
	}

	public static AddTaskOperation addTask(IDiagramTypeProvider diagramTypeProvider) {
		return new AddTaskOperation(diagramTypeProvider);
	}
}
