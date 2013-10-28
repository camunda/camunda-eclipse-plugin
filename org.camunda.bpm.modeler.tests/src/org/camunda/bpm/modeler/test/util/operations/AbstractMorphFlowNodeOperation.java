package org.camunda.bpm.modeler.test.util.operations;

import org.camunda.bpm.modeler.core.utils.ContextUtil;
import org.camunda.bpm.modeler.ui.features.AbstractMorphNodeFeature;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public abstract class AbstractMorphFlowNodeOperation<T extends ICustomFeature, V extends AbstractMorphFlowNodeOperation<T, V>> extends Operation<CustomContext, T> {

	protected final V myself;
	
	public AbstractMorphFlowNodeOperation(IDiagramTypeProvider diagramTypeProvider, PictogramElement element, Class<V> cls) {
		super(diagramTypeProvider);
		
		this.context = createContext();
		this.myself = cls.cast(this);
		
		PictogramElement[] elements = { element };
		this.context.setPictogramElements(elements);
		
	}
	
	@Override
	protected CustomContext createContext() {
		CustomContext context = new CustomContext();
		return context;
	}
	
	public V to(EClass newType) {
		ContextUtil.set(context, AbstractMorphNodeFeature.CREATE_MODE, newType);
		
		return myself;
	}
	

}
