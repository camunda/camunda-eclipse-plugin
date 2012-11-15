package org.eclipse.bpmn2.modeler.ui.features.flow;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.modeler.core.features.BaseElementConnectionFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractAddFlowFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractReconnectFlowFeature;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.features.CreateDataAssociationFeature;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.impl.ReconnectionContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;


public class DataAssociationFeatureContainer extends BaseElementConnectionFeatureContainer {

  @Override
  public boolean canApplyTo(Object o) {
    return super.canApplyTo(o) && o instanceof DataAssociation;
  }
  
  public class AddDataAssocationFeature extends AbstractAddFlowFeature<DataAssociation> {

    public AddDataAssocationFeature(IFeatureProvider fp) {
      super(fp);
    }

    @Override
    protected Polyline createConnectionLine(Connection connection) {
      IPeService peService = Graphiti.getPeService();
      IGaService gaService = Graphiti.getGaService();
      BaseElement be = BusinessObjectUtil.getFirstBaseElement(connection);

      Polyline connectionLine = super.createConnectionLine(connection);
      connectionLine.setLineStyle(LineStyle.DOT);

      ConnectionDecorator endDecorator = peService.createConnectionDecorator(connection, false, 1.0, true);

      int w = 5;
      int l = 10;

      Polyline arrowhead = gaService.createPolyline(endDecorator, new int[] { -l, w, 0, 0, -l, -w });
      StyleUtil.applyStyle(arrowhead, be);
      
      return connectionLine;
    }
    
    @Override
    protected Class< ? extends BaseElement> getBoClass() {
      return DataAssociation.class;
    }
    
  }
  
  public static class ReconnectDataAssociationFeature extends AbstractReconnectFlowFeature {
    
    public ReconnectDataAssociationFeature(IFeatureProvider fp) {
      super(fp);
    }

    @Override
    public boolean canReconnect(IReconnectionContext context) {
      DataAssociation dataAssociation = BusinessObjectUtil.getFirstElementOfType(context.getConnection(), DataAssociation.class);
      BaseElement targetElement = BusinessObjectUtil.getFirstElementOfType(context.getTargetPictogramElement(), BaseElement.class);
      if (dataAssociation instanceof DataInputAssociation) {
        if (context.getReconnectType().equals(ReconnectionContext.RECONNECT_SOURCE)) {
          if (targetElement instanceof ItemAwareElement) {
            return true;
          }
        }
        if (context.getReconnectType().equals(ReconnectionContext.RECONNECT_TARGET)) {
          if (targetElement instanceof Activity || targetElement instanceof ThrowEvent) {
            return true;
          }
        }
        return false;
      }
      if (dataAssociation instanceof DataOutputAssociation) {
        if (context.getReconnectType().equals(ReconnectionContext.RECONNECT_SOURCE)) {
          if (targetElement instanceof Activity || targetElement instanceof CatchEvent) {
            return true;
          }
        }
        if (context.getReconnectType().equals(ReconnectionContext.RECONNECT_TARGET)) {
          if (targetElement instanceof ItemAwareElement) {
            return true;
          }
        }
      }
      return false;
    }

    @Override
    protected Class<? extends EObject> getTargetClass() {
      return BaseElement.class;
    }

    @Override
    protected Class<? extends EObject> getSourceClass() {
      return BaseElement.class;
    }

    @Override
    public void postReconnect(IReconnectionContext context) {
      Anchor oldAnchor = context.getOldAnchor();
      AnchorContainer oldAnchorContainer = oldAnchor.getParent();
      AnchorUtil.deleteConnectionPointIfPossible(getFeatureProvider(), (Shape) oldAnchorContainer);
      
      BPMNEdge edge = BusinessObjectUtil.getFirstElementOfType(context.getConnection(), BPMNEdge.class);
      DiagramElement de = BusinessObjectUtil.getFirstElementOfType(context.getTargetPictogramElement(), DiagramElement.class);
      if (context.getReconnectType().equals(ReconnectionContext.RECONNECT_TARGET)) {
        edge.setTargetElement(de);
      }
      else {
        edge.setSourceElement(de);
      }
      
      DataAssociation dataAssociation = BusinessObjectUtil.getFirstElementOfType(context.getConnection(), DataAssociation.class);
      BaseElement targetElement = BusinessObjectUtil.getFirstElementOfType(context.getTargetPictogramElement(), BaseElement.class);
      if (dataAssociation instanceof DataInputAssociation) {
        if (context.getReconnectType().equals(ReconnectionContext.RECONNECT_SOURCE)) {
          if (targetElement instanceof ItemAwareElement) {
            dataAssociation.getSourceRef().clear();
            dataAssociation.getSourceRef().add((ItemAwareElement) targetElement);
          }
          return;
        }
        if (context.getReconnectType().equals(ReconnectionContext.RECONNECT_TARGET)) {
          if (targetElement instanceof Activity) {
            Activity activity = (Activity) targetElement;
            activity.getDataInputAssociations().add((DataInputAssociation) dataAssociation);
          } else if (targetElement instanceof ThrowEvent) {
            ThrowEvent throwEvent = (ThrowEvent) targetElement;
            throwEvent.getDataInputAssociation().add((DataInputAssociation) dataAssociation);
          }
          return;
        }
      }
      if (dataAssociation instanceof DataOutputAssociation) {
        if (context.getReconnectType().equals(ReconnectionContext.RECONNECT_SOURCE)) {
          if (targetElement instanceof Activity) {
            Activity activity = (Activity) targetElement;
            activity.getDataOutputAssociations().add((DataOutputAssociation) dataAssociation);
          } else if (targetElement instanceof CatchEvent) {
            CatchEvent throwEvent = (CatchEvent) targetElement;
            throwEvent.getDataOutputAssociation().add((DataOutputAssociation) dataAssociation);
          }
          return;
        }
        if (context.getReconnectType().equals(ReconnectionContext.RECONNECT_TARGET)) {
          if (targetElement instanceof ItemAwareElement) {
            dataAssociation.setTargetRef((ItemAwareElement) targetElement);
          }
        }
      }
    }
  } 

  @Override
  public IAddFeature getAddFeature(IFeatureProvider fp) {
    return new AddDataAssocationFeature(fp);
  }

  @Override
  public ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp) {
    return new CreateDataAssociationFeature(fp);
  }
  
  @Override
  public IReconnectionFeature getReconnectionFeature(IFeatureProvider fp) {
    return new ReconnectDataAssociationFeature(fp);
  }

}
