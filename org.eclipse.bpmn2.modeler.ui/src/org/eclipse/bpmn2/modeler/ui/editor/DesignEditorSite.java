package org.eclipse.bpmn2.modeler.ui.editor;

import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DesignEditorSite extends MultiPageEditorSite {

	final BPMN2Editor bpmn2Editor;
	
	public DesignEditorSite(MultiPageEditorPart multiPageEditor, IEditorPart editor) {
		super(multiPageEditor, editor);
		this.bpmn2Editor = (BPMN2Editor) editor;
	}

	@Override
	protected void handleSelectionChanged(SelectionChangedEvent event) {
		ISelectionProvider parentProvider = getMultiPageEditor().getSite()
				.getSelectionProvider();
		if (parentProvider instanceof MultiPageSelectionProvider) {
			SelectionChangedEvent newEvent = getNewEvent(parentProvider, event);
			MultiPageSelectionProvider prov = (MultiPageSelectionProvider) parentProvider;
			prov.fireSelectionChanged(newEvent);
		}
	}

	@Override
	protected void handlePostSelectionChanged(SelectionChangedEvent event) {
		ISelectionProvider parentProvider = getMultiPageEditor().getSite()
				.getSelectionProvider();
		if (parentProvider instanceof MultiPageSelectionProvider) {
			SelectionChangedEvent newEvent = getNewEvent(parentProvider, event);
			MultiPageSelectionProvider prov = (MultiPageSelectionProvider) parentProvider;
			prov.firePostSelectionChanged(newEvent);
		}
	}

	protected SelectionChangedEvent getNewEvent(ISelectionProvider parentProvider, SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection)selection;
			Object o = ss.getFirstElement();
			if (o instanceof Node) {
				selection = getNewSelection((Node)o);
			}
		}
		if (selection!=null)
			return new SelectionChangedEvent(parentProvider, selection);
		return event;
	}

	protected StructuredSelection getNewSelection(Node node) {
		int type =  node.getNodeType();
		if (type==1) {
			// node type = element
			PictogramElement pe = null;
			Element elem = (Element)node;
			String value = elem.getAttribute("bpmnElement");
			if (value!=null) {
				pe = findPictogramElement(value);
			}
			
			if (pe==null) {
				value = elem.getAttribute("id");
				if (value!=null)
					pe = findPictogramElement(value);
			}
			
			if (pe!=null) {
				return new StructuredSelection(pe);
			}
			return getNewSelection(node.getParentNode());
		}
		else if (type==2) {
			// node type = attribute
			// search the attribute's owner
			Attr attr = (Attr)node;
			return getNewSelection(attr.getOwnerElement());
		}
		else if (type==3) {
			// node type = text
			return getNewSelection(node.getParentNode());
		}
		return null;
	}

	protected PictogramElement findPictogramElement(String id) {
		PictogramElement pictogramElement = null;
		if (id!=null) {
			BaseElement be = bpmn2Editor.getModelHandler().findElement(id);
			List<PictogramElement> pes = Graphiti.getLinkService().getPictogramElements(
					bpmn2Editor.getDiagramTypeProvider().getDiagram(), be);
			for (PictogramElement pe : pes) {
				if (pe instanceof ContainerShape) {
					pictogramElement = pe;
				}
				else if (pe instanceof FreeFormConnection) {
					pictogramElement = pe;
				}
			}
		}
		
		return pictogramElement;
	}
}