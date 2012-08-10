package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import org.eclipse.bpmn2.Property;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.TableColumn;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractListComposite.ListCompositeColumnProvider;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class PropertyListComposite extends DefaultListComposite {

	public PropertyListComposite(AbstractBpmn2PropertySection section) {
		super(section, DEFAULT_STYLE|EDIT_BUTTON);
	}
	
	public PropertyListComposite(Composite parent) {
		this(parent, DEFAULT_STYLE|EDIT_BUTTON);
	}
	
	public PropertyListComposite(Composite parent, int style) {
		super(parent,style);
	}
	
	@Override
	public void bindList(EObject theobject, EStructuralFeature thefeature) {
		super.bindList(theobject, thefeature);
		ModelUtil.setLabel(theobject, thefeature, "Variables");
	}
	
	public ListCompositeColumnProvider getColumnProvider(EObject object, EStructuralFeature feature) {
		if (columnProvider==null) {
			columnProvider = new ListCompositeColumnProvider(this,true);
			columnProvider.add(new TableColumn(object, PACKAGE.getProperty_Name()));
			columnProvider.add(new TableColumn(object, PACKAGE.getItemAwareElement_ItemSubjectRef()));
		}
		return columnProvider;
	}

	@Override
	protected EObject addListItem(EObject object, EStructuralFeature feature) {
		EList<Property> properties = (EList)object.eGet(feature);
		// generate a unique parameter name
		String base = "localVar";
		int suffix = 1;
		String name = base + suffix;
		for (;;) {
			boolean found = false;
			for (Property p : properties) {
				if (name.equals(p.getName()) || name.equals(p.getId())) {
					found = true;
					break;
				}
			}
			if (!found)
				break;
			name = base + ++suffix;
		}
		Property prop  = (Property)super.addListItem(object, feature);
		prop.setName(name);
		return prop;
	}
}