package org.eclipse.bpmn2.modeler.ui.property.diagrams;

import org.eclipse.bpmn2.ResourceRole;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.ListCompositeColumnProvider;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.TableColumn;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class ResourceRoleListComposite extends DefaultListComposite {

	public ResourceRoleListComposite(AbstractBpmn2PropertySection section) {
		super(section, DEFAULT_STYLE|EDIT_BUTTON);
	}
	
	public ResourceRoleListComposite(Composite parent) {
		this(parent, DEFAULT_STYLE|EDIT_BUTTON);
	}
	
	public ResourceRoleListComposite(Composite parent, int style) {
		super(parent,style);
	}
	
	@Override
	public void bindList(EObject theobject, EStructuralFeature thefeature) {
		super.bindList(theobject, thefeature);
		ModelUtil.setLabel(theobject, thefeature, "Roles");
	}
	
	public ListCompositeColumnProvider getColumnProvider(EObject object, EStructuralFeature feature) {
		if (columnProvider==null) {
			columnProvider = new ListCompositeColumnProvider(this,true);
			columnProvider.add(new TableColumn(object, PACKAGE.getResourceRole_Name()));
			columnProvider.add(new TableColumn(object, PACKAGE.getResourceRole_ResourceRef()));
		}
		return columnProvider;
	}

	@Override
	protected EObject addListItem(EObject object, EStructuralFeature feature) {
//		EList<ResourceRole> roles = (EList)object.eGet(feature);
//		// generate a unique parameter name
//		String base = "Role";
//		int suffix = 1;
//		String name = base + suffix;
//		for (;;) {
//			boolean found = false;
//			for (ResourceRole p : roles) {
//				if (name.equals(p.getName()) || name.equals(p.getId())) {
//					found = true;
//					break;
//				}
//			}
//			if (!found)
//				break;
//			name = base + ++suffix;
//		}
		ResourceRole role  = (ResourceRole)super.addListItem(object, feature);
//		role.setName(name);
		return role;
	}
}