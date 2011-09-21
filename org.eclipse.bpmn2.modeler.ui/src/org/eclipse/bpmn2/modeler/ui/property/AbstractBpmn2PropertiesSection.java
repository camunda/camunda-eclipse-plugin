package org.eclipse.bpmn2.modeler.ui.property;

import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public abstract class AbstractBpmn2PropertiesSection extends GFPropertySection {
	
	protected AbstractBpmn2PropertiesComposite composite;
	protected TabbedPropertySheetPage tabbedPropertySheetPage;

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		this.tabbedPropertySheetPage = aTabbedPropertySheetPage;
		super.createControls(parent, aTabbedPropertySheetPage);
		parent.setLayout(new FillLayout());
	}
	
	@Override
	public void aboutToBeShown() {
		super.aboutToBeShown();
		composite = getComposite();
		composite.setSheetPage(tabbedPropertySheetPage);
	}

	/**
	 * The subclass must provide the parent Composite it created in createControls()
	 * so that we can pass the PropertySheetPage down to the Composite. This is
	 * useful for allowing the Composite to resize itself based on the parent's size.
	 * 
	 * @return
	 */
	protected abstract AbstractBpmn2PropertiesComposite getComposite();
	
	@Override
	public void refresh() {
		tabbedPropertySheetPage.resizeScrolledComposite();
	}

	@Override
	public boolean shouldUseExtraSpace() {
		return true;
	}

}
