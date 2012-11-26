package org.eclipse.bpmn2.modeler.ui.property.tabs;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class GeneralTabSection extends GFPropertySection implements
		ITabbedPropertyConstants {
	
	private Composite parentComposite;
	private TabbedPropertySheetPage page;

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		this.parentComposite = parent;
		this.page = aTabbedPropertySheetPage;
	}
	
	public Composite createPropertiesComposite (Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		
		PictogramElement pe = getSelectedPictogramElement();
		final EObject bo = Graphiti.getLinkService()
				.getBusinessObjectForLinkedPictogramElement(pe);
		
		return new BpmnPropertyCompositeFactory(this, parent).createCompositeForBpmnElement(bo);
	}
	
	private void rebuildParentComposite() {
		Composite oldComposite = parentComposite;
		parentComposite = getWidgetFactory().createFlatFormComposite(parentComposite.getParent());
		oldComposite.dispose();
		
		parentComposite.setLayout(new GridLayout(1, false));
		parentComposite.setLayoutData(new GridData(SWT.FILL, GridData.CENTER, true, false));
		
		internalRefresh();
	}
	
	@Override
	public void refresh() {
		rebuildParentComposite();
		createPropertiesComposite(parentComposite, page);

		internalRefresh(); // otherwise the composite will not properly redrawn after saving some changes, SWT sucks
	}
	
	private void internalRefresh() {
		Composite parent = parentComposite.getParent();
		parent.layout();
		
		parent.redraw(); // otherwise the composite will not properly redrawn, SWT sucks		
	}
}
