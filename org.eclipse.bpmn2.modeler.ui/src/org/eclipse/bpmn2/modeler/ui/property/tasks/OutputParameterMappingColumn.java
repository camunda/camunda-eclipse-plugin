package org.eclipse.bpmn2.modeler.ui.property.tasks;

import org.eclipse.bpmn2.Assignment;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite;
import org.eclipse.bpmn2.modeler.ui.property.TableColumn;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class OutputParameterMappingColumn extends TableColumn {

	public IoParameterNameColumn nameColumn;
	
	public OutputParameterMappingColumn(AbstractListComposite abstractListComposite, IoParameterNameColumn nameColumn, EObject o, EStructuralFeature f) {
		super(abstractListComposite, o, f);
		this.nameColumn = nameColumn;
	}

	@Override
	public String getHeaderText() {
		return "Mapped to";
	}
	
	@Override
	public String getText(Object element) {
		String text = null;
		ItemAwareElement dataOutput = nameColumn.currentElement;
		for (DataOutputAssociation doa : nameColumn.getDataOutputAssociations()) {
			for (ItemAwareElement elem: doa.getSourceRef()) {
				if (elem == dataOutput) {
					if (doa.getTargetRef()!=null)
						text = PropertyUtil.getDisplayName(doa.getTargetRef());
					else {
						if (doa.getTransformation()!=null) {
							text = PropertyUtil.getDisplayName(doa.getTransformation());
						}
						if (!doa.getAssignment().isEmpty()) {
							for ( Assignment assign : doa.getAssignment()) {
								String body = PropertyUtil.getDisplayName(assign.getTo());
								if (text==null)
									text = body;
								else
									text += ",\n" + body;
							}
						}
					}
				}
			}
		}
		return text==null ? "" : text;
	}
}