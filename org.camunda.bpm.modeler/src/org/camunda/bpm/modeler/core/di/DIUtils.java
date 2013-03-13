/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 *
 * @author Ivar Meikas
 ******************************************************************************/
package org.camunda.bpm.modeler.core.di;

import static org.camunda.bpm.modeler.core.layout.util.ConversionUtil.diPoint;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.modeler.core.layout.util.LayoutUtil;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.core.utils.BusinessObjectUtil;
import org.camunda.bpm.modeler.core.utils.LabelUtil;
import org.camunda.bpm.modeler.core.utils.ModelUtil;
import org.camunda.bpm.modeler.core.utils.ScrollUtil;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNLabel;
import org.eclipse.bpmn2.di.BPMNPlane;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.util.Bpmn2Resource;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.DcFactory;
import org.eclipse.dd.dc.Point;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.datatypes.IRectangle;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.algorithms.styles.Style;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.platform.IDiagramEditor;
import org.eclipse.graphiti.services.Graphiti;

public class DIUtils {

	public static final String IMPORT = "DIUtils.import";
	
	public static BPMNShape getShape(PictogramElement element) {
		return BusinessObjectUtil.getFirstElementOfType(element, BPMNShape.class);
	}

	/**
	 * Update the di of the given pictogram element
	 * 
	 * @param pictogramElement
	 */
	public static void updateDI(PictogramElement pictogramElement) {
		
		if (pictogramElement instanceof Shape) {
			updateDIShape(pictogramElement);
		} else {
			updateDIEdge((Connection) pictogramElement);
		}
	}
	
	public static void updateDIShape(PictogramElement element) {
		updateDIShape(element, getShape(element));
	}
	
	public static BPMNShape updateDIShape(PictogramElement element, BPMNShape bpmnShape) {
		PictogramLink link = element.getLink();
		
		if (link == null) {
			return null;
		}

		if (bpmnShape == null) {
			return null;
		}
		
		Bounds bounds; 
		
		if (LabelUtil.isLabel(element)) {
			bounds = getDiLabelBounds(bpmnShape);
		} else {	
			bounds = bpmnShape.getBounds();
		}
		
		IRectangle rect = LayoutUtil.getAbsoluteBounds((Shape) element);

		bounds.setX(rect.getX());
		bounds.setY(rect.getY());
		
		bounds.setHeight(rect.getHeight());
		bounds.setWidth(rect.getWidth());
		
		if (element instanceof ContainerShape) {
			EList<Shape> children = ((ContainerShape) element).getChildren();
			for (Shape shape : children) {
				if (shape instanceof ContainerShape) {
					updateDIShape(shape);
				}
			}
		}

		updateConnections(element);
		
		return bpmnShape;
	}

	private static Bounds getDiLabelBounds(BPMNShape bpmnShape) {
		BPMNLabel label = bpmnShape.getLabel();
		
		if (label == null) {
			label = BpmnDiFactory.eINSTANCE.createBPMNLabel();
			bpmnShape.setLabel(label);
		}
		
		Bounds bounds = label.getBounds();
		if (bounds == null) {
			bounds = DcFactory.eINSTANCE.createBounds();
			label.setBounds(bounds);
		}
		
		return bounds;
	}
	
	public static void updateDILabel(Shape label, BPMNEdge bpmnEdge) {
		Bounds bpmnLabelBounds = null;
		BPMNLabel bpmnLabel = null;
		
		bpmnLabel = bpmnEdge.getLabel();
		if (bpmnLabel == null) {
			bpmnLabel = BpmnDiFactory.eINSTANCE.createBPMNLabel();
			bpmnEdge.setLabel(bpmnLabel);
		}
		
		bpmnLabelBounds = bpmnLabel.getBounds();
		
		if (bpmnLabelBounds == null) {
			bpmnLabelBounds = DcFactory.eINSTANCE.createBounds();
			bpmnLabel.setBounds(bpmnLabelBounds);
		}
		
		updateDIElementBounds(label, bpmnLabelBounds);
	}
	
	public static void updateDILabel(Shape label, BPMNShape bpmnShape) {
		Bounds bpmnLabelBounds = null;
		BPMNLabel bpmnLabel = null;
		
		bpmnLabel = bpmnShape.getLabel();
		if (bpmnLabel == null) {
			bpmnLabel = BpmnDiFactory.eINSTANCE.createBPMNLabel();
			bpmnShape.setLabel(bpmnLabel);
		} 
		
		bpmnLabelBounds = bpmnLabel.getBounds();
		
		if (bpmnLabelBounds == null) {
			bpmnLabelBounds = DcFactory.eINSTANCE.createBounds();
			bpmnLabel.setBounds(bpmnLabelBounds);
		}
		
		updateDIElementBounds(label, bpmnLabelBounds);
	}

	private static void updateDIElementBounds(Shape label, Bounds bpmnLabelBounds) {
		IRectangle labelBounds = LayoutUtil.getAbsoluteBounds(label);
		
		bpmnLabelBounds.setX(labelBounds.getX());
		bpmnLabelBounds.setY(labelBounds.getY());
		bpmnLabelBounds.setWidth(labelBounds.getWidth());
		bpmnLabelBounds.setHeight(labelBounds.getHeight());
	}
	
	public static void updateConnections(PictogramElement element) {
		if (element instanceof Shape) {
			EList<Anchor> anchors = ((Shape) element).getAnchors();
			
			for (Anchor anchor : anchors) {
				List<Connection> connections = Graphiti.getPeService().getAllConnections(anchor);
				for (Connection connection : connections){
					updateDIEdge(connection);
				}
			}
			
			anchors.size();
		}
	}

	public static void updateDIEdge(Connection connection) {
		
		BPMNEdge edge = BusinessObjectUtil.getFirstElementOfType(connection, BPMNEdge.class);
		
		if (edge != null) {
			Point point = DcFactory.eINSTANCE.createPoint();

			List<Point> waypoint = edge.getWaypoint();
			waypoint.clear();

			ILocation loc = LayoutUtil.getVisibleAnchorLocation(connection.getStart(), connection);
			
			point.setX(loc.getX());
			point.setY(loc.getY());
			waypoint.add(point);

			if (connection instanceof FreeFormConnection) {
				FreeFormConnection freeForm = (FreeFormConnection) connection;
				EList<org.eclipse.graphiti.mm.algorithms.styles.Point> bendpoints = freeForm.getBendpoints();
				for (org.eclipse.graphiti.mm.algorithms.styles.Point bp : bendpoints) {
					point = DcFactory.eINSTANCE.createPoint();
					point.setX(bp.getX());
					point.setY(bp.getY());
					waypoint.add(point);
				}
			}

			point = DcFactory.eINSTANCE.createPoint();
			loc = LayoutUtil.getVisibleAnchorLocation(connection.getEnd(), connection);
			
			point.setX(loc.getX());
			point.setY(loc.getY());
			waypoint.add(point);
		}
	}

	public static BPMNEdge createDIEdge(Connection connection, BaseElement connectionElement, Diagram diagram) {

		BPMNDiagram bpmnDiagram = getBPMNDiagram(diagram);
		
		Anchor startAnchor = connection.getStart();
		Anchor endAnchor = connection.getEnd();
		
		BPMNShape sourceBpmnShape = BusinessObjectUtil.getFirstElementOfType(startAnchor.getParent(), BPMNShape.class);
		BPMNShape targetBpmnShape = BusinessObjectUtil.getFirstElementOfType(endAnchor.getParent(), BPMNShape.class);

		BPMNEdge bpmnEdge = BpmnDiFactory.eINSTANCE.createBPMNEdge();
		bpmnEdge.setBpmnElement(connectionElement);

		bpmnEdge.setSourceElement(sourceBpmnShape);
		bpmnEdge.setTargetElement(targetBpmnShape);

		ILocation startLocation = Graphiti.getPeService().getLocationRelativeToDiagram(startAnchor);
		ILocation endLocation = Graphiti.getPeService().getLocationRelativeToDiagram(endAnchor);
		
		bpmnEdge.getWaypoint().add(diPoint(startLocation));
		bpmnEdge.getWaypoint().add(diPoint(endLocation));

		addShape(bpmnEdge, bpmnDiagram);
		
		ModelUtil.setID(bpmnEdge);
		
		return bpmnEdge;
	}
	
	public static BPMNShape createDIShape(BaseElement elem, IRectangle bounds, Diagram diagram) {

		BPMNDiagram bpmnDiagram = getBPMNDiagram(diagram);
		
		BPMNShape bpmnShape = null;

		bpmnShape = BpmnDiFactory.eINSTANCE.createBPMNShape();
		
		Bounds diBounds = DcFactory.eINSTANCE.createBounds();
		diBounds.setX(bounds.getX());
		diBounds.setY(bounds.getY());
		diBounds.setWidth(bounds.getWidth());
		diBounds.setHeight(bounds.getHeight());
		
		bpmnShape.setBounds(diBounds);
		bpmnShape.setBpmnElement(elem);
		
		Bpmn2Preferences.getInstance(bpmnDiagram.eResource()).applyBPMNDIDefaults(bpmnShape, null);

		ModelUtil.setID(bpmnShape);
		
		addShape(bpmnShape, bpmnDiagram);

		return bpmnShape;
	}

	protected static BPMNDiagram getBPMNDiagram(Diagram diagram) {
		return BusinessObjectUtil.getFirstElementOfType(diagram, BPMNDiagram.class);
	}

	public static void addShape(DiagramElement elem, BPMNDiagram bpmnDiagram) {
		List<DiagramElement> elements = bpmnDiagram.getPlane().getPlaneElement();
		elements.add(elem);
	}
	
	public static DiagramElement findDiagramElement(List<BPMNDiagram> diagrams, BaseElement bpmnElement) {
		for (BPMNDiagram d : diagrams) {
			BPMNPlane plane = d.getPlane();
			List<DiagramElement> planeElements = plane.getPlaneElement();
			return findPlaneElement(planeElements, bpmnElement);
		}
		return null;
	}

	public static DiagramElement findPlaneElement(List<DiagramElement> planeElements, BaseElement bpmnElement) {
		for (DiagramElement de : planeElements) {
			if (de instanceof BPMNShape) {
				if (bpmnElement == ((BPMNShape)de).getBpmnElement())
					return de;
			}
			if (de instanceof BPMNEdge) {
				if (bpmnElement == ((BPMNEdge)de).getBpmnElement())
					return de;
			}
			else if (de instanceof BPMNPlane) {
				return findPlaneElement(((BPMNPlane)de).getPlaneElement(), bpmnElement);
			}
		}
		return null;
	}

	/**
	 * Return the Graphiti Diagram for the given BPMNDiagram. If one does not exist, create it.
	 * 
	 * @param editor
	 * @param bpmnDiagram
	 * @return
	 */
	public static Diagram getOrCreateDiagram(final IDiagramEditor editor, final BPMNDiagram bpmnDiagram) {
		// do we need to create a new Diagram or is this already in the model?
		Diagram diagram = findDiagram(editor, bpmnDiagram);
		if (diagram!=null) {
			// already exists
			return diagram;
		}

		// create a new one
		IDiagramTypeProvider dtp = editor.getDiagramTypeProvider();
		String typeId = dtp.getDiagram().getDiagramTypeId();
		
		String name = bpmnDiagram.getName();
		if(name == null) {
			name = bpmnDiagram.getId();
		}
		
		final Diagram newDiagram = Graphiti.getCreateService().createDiagram(typeId, name, true);
		final IFeatureProvider featureProvider = dtp.getFeatureProvider();
		final Resource resource = dtp.getDiagram().eResource();
		TransactionalEditingDomain domain = editor.getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			protected void doExecute() {
				resource.getContents().add(newDiagram);
				newDiagram.setActive(true);
				featureProvider.link(newDiagram, bpmnDiagram);
			}
		});
		return newDiagram;
	}
	
	/**
	 * Find the Graphiti Diagram that corresponds to the given BPMNDiagram object.
	 * 
	 * @param editor
	 * @param bpmnDiagram
	 * @return
	 */
	public static Diagram findDiagram(final IDiagramEditor editor, final BPMNDiagram bpmnDiagram) {
		ResourceSet resourceSet = editor.getEditingDomain().getResourceSet();
		if (resourceSet!=null) {
			return findDiagram(resourceSet, bpmnDiagram);
		}
		return null;
	}
	
	public static Diagram findDiagram(ResourceSet resourceSet, final BPMNDiagram bpmnDiagram) {
		if (resourceSet!=null) {
			for (Resource r : resourceSet.getResources()) {
				for (EObject o : r.getContents()) {
					if (o instanceof Diagram) {
						Diagram diagram = (Diagram) o;
						if (BusinessObjectUtil.getFirstElementOfType(diagram, BPMNDiagram.class) == bpmnDiagram) {
							return diagram;
						}
					}
				}
			}
		}
		return null;
	}
	
	public static void deleteDiagram(final IDiagramEditor editor, final BPMNDiagram bpmnDiagram) {
		Diagram diagram = DIUtils.findDiagram(editor, bpmnDiagram);
		if (diagram!=null) {
			List<EObject> list = new ArrayList<EObject>();
			TreeIterator<EObject> iter = diagram.eAllContents();
			while (iter.hasNext()) {
				EObject o = iter.next();
				if (o instanceof PictogramLink) {
					((PictogramLink)o).getBusinessObjects().clear();
					if (!list.contains(o))
						list.add(o);
				}
				else if (o instanceof Color) {
					if (!list.contains(o))
						list.add(o);
				}
				else if (o instanceof Font) {
					if (!list.contains(o))
						list.add(o);
				}
				else if (o instanceof Style) {
					if (!list.contains(o))
						list.add(o);
				}
			}
			for (EObject o : list)
				EcoreUtil.delete(o);
			
			EcoreUtil.delete(diagram);
			EcoreUtil.delete(bpmnDiagram);
		}	
	}
	
	public static BPMNDiagram findBPMNDiagram(final IDiagramEditor editor, final BaseElement baseElement, boolean deep) {
		if (baseElement!=null) {
			ResourceSet resourceSet = editor.getResourceSet();
			if (resourceSet!=null) {
				for (Resource r : resourceSet.getResources()) {
					if (r instanceof Bpmn2Resource) {
						for (EObject o : r.getContents()) {
							if (o instanceof DocumentRoot) {
								DocumentRoot root = (DocumentRoot)o;
								Definitions defs = root.getDefinitions();
								for (BPMNDiagram d : defs.getDiagrams()) {
									BPMNDiagram bpmnDiagram = (BPMNDiagram)d;
									BaseElement bpmnElement = bpmnDiagram.getPlane().getBpmnElement();
									if (bpmnElement == baseElement)
										return bpmnDiagram;
									if (deep) {
										for (DiagramElement de : bpmnDiagram.getPlane().getPlaneElement()) {
											if (de instanceof BPMNShape)
												bpmnElement = ((BPMNShape)de).getBpmnElement();
											else if (de instanceof BPMNEdge)
												bpmnElement = ((BPMNEdge)de).getBpmnElement();
											else
												continue;
											if (bpmnElement == baseElement)
												return bpmnDiagram;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public static BPMNDiagram createBPMNDiagram(Definitions definitions, BaseElement container) {
		
		Resource resource = definitions.eResource();
        BPMNDiagram bpmnDiagram = BpmnDiFactory.eINSTANCE.createBPMNDiagram();
		ModelUtil.setID(bpmnDiagram, resource);
        bpmnDiagram.setName(ModelUtil.getDisplayName(container));

		BPMNPlane plane = BpmnDiFactory.eINSTANCE.createBPMNPlane();
		ModelUtil.setID(plane, resource);
		plane.setBpmnElement(container);
		
		bpmnDiagram.setPlane(plane);
		
		// this has to happen last because the IResourceChangeListener in the DesignEditor
		// looks for add/remove to Definitions.diagrams
        definitions.getDiagrams().add(bpmnDiagram);

		return bpmnDiagram;
	}
}