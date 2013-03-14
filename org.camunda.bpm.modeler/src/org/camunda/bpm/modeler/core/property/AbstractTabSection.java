package org.camunda.bpm.modeler.core.property;

import org.camunda.bpm.modeler.ui.editor.BPMN2Editor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * Base class for tabs provided by the editor
 * 
 * @author nico.rehwaldt
 */
public abstract class AbstractTabSection extends GFPropertySection implements ITabbedPropertyConstants {

	protected Composite parent;	
	protected TabbedPropertySheetPage page;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage page) {
		
		super.createControls(parent, page);
		
		this.parent = parent;
		this.page = page;
	}
	
	public Composite createPropertiesComposite(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		
		super.createControls(parent, tabbedPropertySheetPage);
		
		EObject businessObject = getBusinessObject();
		
		return createCompositeForObject(parent, businessObject);
	}
	
	/**
	 * Create composite for the given businessObject under the specified parent
	 * 
	 * @param parent
	 * @param businessObject
	 * @return
	 */
	protected abstract Composite createCompositeForObject(Composite parent, EObject businessObject);

	/**
	 * Returns the business object for the current selection
	 * 
	 * @return
	 */
	private EObject getBusinessObject() {
		
		PictogramElement pictogramElement = getSelectedPictogramElement();
		
		EObject object = Graphiti.getLinkService()
				.getBusinessObjectForLinkedPictogramElement(pictogramElement);
		
		// handle case that business object or the pictogram element have been deleted
		// when multiple objects are selected
		if (object == null) {
			pictogramElement = BPMN2Editor.getActiveEditor().getDiagramTypeProvider().getDiagram();
			
			object = Graphiti.getLinkService()
					.getBusinessObjectForLinkedPictogramElement(pictogramElement);
		}
		
		return object;
	}

	private void resetParentComposite() {
		if (parent == null || parent.isDisposed()) {
			return;
		}
		
		Composite parentsParent = parent.getParent();
		
		Composite oldParent = parent;
		oldParent.dispose();
		
		parent = getWidgetFactory().createFlatFormComposite(parentsParent);
		
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, GridData.CENTER, true, false));
	}
	
	@Override
	public void refresh() {
		resetParentComposite();
		
		createPropertiesComposite(parent, page);
		
		redraw(); // otherwise the composite will not properly redrawn after saving some changes, SWT sucks
	}
	
	private void redraw() {
		Composite propertiesContainer = parent.getParent();
		propertiesContainer.layout();
		
		propertiesContainer.redraw(); // otherwise the composite will not properly redrawn, SWT sucks		
	}
	
	@Override
	public void dispose() {
		resetParentComposite();
	}
}
