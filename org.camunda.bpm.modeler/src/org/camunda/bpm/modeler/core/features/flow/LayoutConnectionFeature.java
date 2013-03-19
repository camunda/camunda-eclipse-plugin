package org.camunda.bpm.modeler.core.features.flow;

import static org.camunda.bpm.modeler.core.utils.ContextUtil.is;

import org.camunda.bpm.modeler.core.features.AbstractLayoutBpmnElementFeature;
import org.camunda.bpm.modeler.core.features.PropertyNames;
import org.camunda.bpm.modeler.core.layout.nnew.DefaultLayoutStrategy;
import org.camunda.bpm.modeler.core.layout.nnew.LayoutContext;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;

/**
 * Default layout feature for flows.
 * 
 * @author nico.rehwaldt
 */
public class LayoutConnectionFeature extends AbstractLayoutBpmnElementFeature<FreeFormConnection> implements ILayoutFeature {

	public LayoutConnectionFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canLayout(ILayoutContext context) {
		return true;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		
		FreeFormConnection connection = getLayoutedElement(context);
		
		// (0) check if connection is layouted
		// (1) (true?) repair if needed
		// (2) (false?) 
		// (2.1) set correct anchor points
		// (2.2) set correct bend points

		boolean forceLayout = is(context, PropertyNames.LAYOUT_CONNECTION_FORCE);
		boolean layoutOnRepairFail = is(context, PropertyNames.LAYOUT_CONNECTION_ON_REPAIR_FAIL);
		
		BaseElement model = BusinessObjectUtil.getFirstBaseElement(connection);
		
		System.out.println();
		System.out.println("[reconnect] " + model.getId());
		
		LayoutContext layoutingContext = new DefaultLayoutStrategy().createLayoutingContext(connection, layoutOnRepairFail);
		
		if (forceLayout) {
			System.out.println("[reconnect] forced layout, no repair");
			layoutingContext.layout();
		} else {
			System.out.println("[reconnect] repair");
			boolean repaired = layoutingContext.repair();

			System.out.println("[reconnect] " + (repaired ? "repair success" : "repair failed"));
			
			if (layoutingContext.needsLayout()) {
				System.out.println("[reconnect] repair failed, relayout");
				layoutingContext.layout();
			}
		}
		
		// reposition label after layout
		LabelUtil.repositionConnectionLabel(connection, getFeatureProvider());
	
		updateDi(connection);
		
		return true;
	}
}
