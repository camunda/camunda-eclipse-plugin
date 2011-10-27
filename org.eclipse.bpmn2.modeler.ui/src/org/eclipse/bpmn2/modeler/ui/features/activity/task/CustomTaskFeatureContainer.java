package org.eclipse.bpmn2.modeler.ui.features.activity.task;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.impl.TaskImpl;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AbstractCreateTaskFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.task.ICustomTaskFeature;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.ui.diagram.BPMNFeatureProvider;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IAreaContext;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.impl.DiagramImpl;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.common.util.URI;

public class CustomTaskFeatureContainer extends TaskFeatureContainer implements ICustomTaskFeature {
	
	protected String id;
	protected CustomTaskDescriptor customTaskDescriptor;
	
	/* (non-Javadoc)
	 * Determine if the context applies to this customTask and return the Task object. Return null otherwise.
	 * @param context - the Graphiti context.
	 * 
	 * @see org.eclipse.bpmn2.modeler.core.features.BaseElementFeatureContainer#getApplyObject(org.eclipse.graphiti.features.context.IContext)
	 */
	@Override
	public Object getApplyObject(IContext context) {
		Object id = getId(context);
		if (id==null || !this.id.equals(id)) {
			return null;
		}

		return super.getApplyObject(context);
	}

	@Override
	public boolean canApplyTo(Object o) {
		boolean b1 =  o instanceof TaskImpl;
		boolean b2 = o.getClass().isAssignableFrom(TaskImpl.class);
		return b1 || b2;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.core.features.activity.task.ICustomTaskFeature#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Set this customTask's ID in the given Graphiti context.
	 * 
	 * @param context - if this is a IPictogramElementContext, set the property
	 *                  in the contained PictogramElement's list of properties;
	 *                  otherwise set the Context's property 
	 * @param id - ID of this Custom Task
	 */
	public static void setId(IContext context, String id) {
		
		if (context instanceof IPictogramElementContext) {
			PictogramElement pe = ((IPictogramElementContext)context).getPictogramElement();
			Graphiti.getPeService().setPropertyValue(pe,CUSTOM_TASK_ID,id); 
		}
		else {
			context.putProperty(CUSTOM_TASK_ID, id);
		}
	}
	
	/**
	 * Returns the customTask ID string from the given Graphiti context.
	 * 
	 * @param context
	 * @return - ID string for this customTask.
	 */
	public static String getId(IContext context) {
		Object id = null;
		if (context instanceof IPictogramElementContext) {
			PictogramElement pe = ((IPictogramElementContext)context).getPictogramElement();
			id = Graphiti.getPeService().getPropertyValue(pe,CUSTOM_TASK_ID); 
		}
		else {
			id = context.getProperty(CUSTOM_TASK_ID);
		}
		return (String)id;
	}
	
	/**
	 * Set this customTask's ID string. The ID is defined in the plugin's
	 * extension point contribution to org.eclipse.bpmn2.modeler.custom_task.
	 * This will register the Custom Task with the BPMN Feature Provider.
	 * 
	 * @param fp - Feature Provider (must be a BPMNFeatureProvider)
	 * @param id - the customTask ID string.
	 * @throws Exception
	 *    Custom Task ID can not be null
	 *    The Feature Provider is invalid (not a BPMNFeatureProvider)
	 *    Attempt to add a Custom Feature with a duplicate ID {id}
	 */
	public void setId(IFeatureProvider fp, String id) throws Exception {
		
		// "id" is a required element in the extension point.
		if (id==null || id.isEmpty()) {
			throw new Exception("Custom Task ID can not be null");
		}
		this.id = id;
		
		if (fp instanceof BPMNFeatureProvider) {
			// register this custom customTask ID with the BPMNFeatureProvider;
			// this will allow the feature provider to find the correct feature container class
			// for this custom customTask, instead of the generic "Task" feature container
			BPMNFeatureProvider bfp = (BPMNFeatureProvider)fp;
			try {
				bfp.addFeatureContainer(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
			throw new Exception("The Feature Provider is invalid (not a BPMNFeatureProvider)");
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.features.activity.task.ICustomTaskFeatureContainer#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.features.activity.task.ICustomTaskFeatureContainer#setCustomTaskDescriptor(org.eclipse.bpmn2.modeler.core.preferences.TargetRuntime.CustomTaskDescriptor)
	 */
	@Override
	public void setCustomTaskDescriptor(CustomTaskDescriptor customTaskDescriptor) {
		this.customTaskDescriptor = customTaskDescriptor;
	}
	
	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateCustomTaskFeature(fp);
	}

	/**
	 * @author bbrodt
	 *
	 * Base class for Custom Task Feature construction. Custom Tasks contributed to
	 * the editor MUST subclass this!
	 * 
	 * The Task creation process copies the customTask ID string into the Graphiti create
	 * context during the construction phase, then migrates that ID into the created
	 * PictogramElement. This is necessary because the ID must be associated with the
	 * PE in to allow our BPMNFeatureProvider to correctly identify the Custom Task.
	 */
	public class CreateCustomTaskFeature extends AbstractCreateTaskFeature {

		public CreateCustomTaskFeature(IFeatureProvider fp, String name, String description) {
			super(fp, name, description);
		}

		public CreateCustomTaskFeature(IFeatureProvider fp) {
			super(fp, customTaskDescriptor.getName(), customTaskDescriptor.getDescription());
		}

		@Override
		protected PictogramElement addGraphicalRepresentation(IAreaContext context, Object newObject) {

			// create a new AddContext and copy our ID into it.
			IAddContext addContext = new AddContext(context, newObject);
			setId(addContext, getId());
			
			// create the PE and copy our ID into its properties.
			PictogramElement pe = getFeatureProvider().addIfPossible(addContext);
			Graphiti.getPeService().setPropertyValue(pe,CUSTOM_TASK_ID,id);
			
			return pe;
		}

		@Override
		public boolean canCreate(ICreateContext context) {
			// copy our ID into the CreateContext - this is where it all starts!
			setId(context, id);
			return super.canCreate(context);
		}

		@Override
		protected Task createFlowElement(ICreateContext context) {
			EObject target = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(context.getTargetContainer());
			return (Task)customTaskDescriptor.createObject(target);
		}
		
		@Override
		protected String getStencilImageId() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature#getFlowElementClass()
		 */
		@Override
		public Class getBusinessObjectClass() {
			return Task.class;
		}
		
	}

}
