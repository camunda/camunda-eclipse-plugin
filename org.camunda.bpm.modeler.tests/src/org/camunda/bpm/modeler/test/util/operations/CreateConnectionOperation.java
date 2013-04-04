package org.camunda.bpm.modeler.test.util.operations;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.location;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

/**
 * Abstract create connection operation
 * 
 * @author nico.rehwaldt
 */
public class CreateConnectionOperation extends Operation<CreateConnectionContext, ICreateConnectionFeature> {

	public CreateConnectionOperation(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
		
		this.context = createContext();
	}

	public CreateConnectionOperation from(Shape source, Anchor sourceAnchor) {
		
		context.setSourceAnchor(sourceAnchor);
		
		ILocation absoluteAnchorLocation = LayoutUtil.getAnchorLocation(sourceAnchor);
		IRectangle absoluteBounds = LayoutUtil.getAbsoluteBounds(source);

		ILocation relativeAnchorLocation = location(
				absoluteAnchorLocation.getX() - absoluteBounds.getX(),
				absoluteAnchorLocation.getY() - absoluteBounds.getY());
		
		return from(source, relativeAnchorLocation);
	}
	
	public CreateConnectionOperation from(PictogramElement source, ILocation sourceLocation) {
		context.setSourcePictogramElement(source);
		context.setSourceLocation(sourceLocation);
		
		return this;
	}

	public CreateConnectionOperation to(Shape target, Anchor targetAnchor) {
		
		context.setTargetAnchor(targetAnchor);
		
		ILocation absoluteAnchorLocation = LayoutUtil.getAnchorLocation(targetAnchor);
		IRectangle absoluteBounds = LayoutUtil.getAbsoluteBounds(target);

		ILocation relativeAnchorLocation = location(
				absoluteAnchorLocation.getX() - absoluteBounds.getX(),
				absoluteAnchorLocation.getY() - absoluteBounds.getY());
		
		return from(target, relativeAnchorLocation);
	}
	
	public CreateConnectionOperation to(PictogramElement target, ILocation targetLocation) {
		context.setSourcePictogramElement(target);
		context.setSourceLocation(targetLocation);
		
		return this;
	}
	
	@Override
	protected CreateConnectionContext createContext() {
		return new CreateConnectionContext();
	}

	@Override
		
	protected ICreateConnectionFeature createFeature(CreateConnectionContext context) {
		ICreateConnectionFeature[] createConnectionFeatures = diagramTypeProvider.getFeatureProvider().getCreateConnectionFeatures();
		for (ICreateConnectionFeature createFeature : createConnectionFeatures) {
			if (createFeature.canStartConnection(context)) {
				return createFeature;
			}
		}
		
		return null;
	}
	
	//// static helper //////////////////////////////////////////////////////////////////
	
	public static CreateConnectionOperation createConnection(IDiagramTypeProvider diagramTypeProvider) {
		return new CreateConnectionOperation(diagramTypeProvider);
	}
}
