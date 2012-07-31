package org.eclipse.bpmn2.modeler.ui.property.tasks;

import org.eclipse.bpmn2.Assignment;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.modeler.ui.property.AbstractListComposite;
import org.eclipse.bpmn2.modeler.ui.property.TableColumn;
import org.eclipse.bpmn2.modeler.ui.util.PropertyUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class InputParameterMappingColumn extends TableColumn {

	public IoParameterNameColumn nameColumn;
	
	public InputParameterMappingColumn(AbstractListComposite abstractListComposite, IoParameterNameColumn nameColumn, EObject o, EStructuralFeature f) {
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
		ItemAwareElement dataInput = nameColumn.currentElement;
		for (DataInputAssociation dia : nameColumn.getDataInputAssociations()) {
			if (dia.getTargetRef() == dataInput) {
				if (!dia.getSourceRef().isEmpty()) {
					for (ItemAwareElement elem : dia.getSourceRef()) {
						if (text==null)
							text = PropertyUtil.getDisplayName(elem);
						else
							text += ", " + PropertyUtil.getDisplayName(elem);
					}
				}
				else {
					if (dia.getTransformation()!=null) {
						text = PropertyUtil.getDisplayName(dia.getTransformation());
					}
					if (!dia.getAssignment().isEmpty()) {
						for ( Assignment assign : dia.getAssignment()) {
							String body = PropertyUtil.getDisplayName(assign.getFrom());
							if (text==null)
								text = body;
							else
								text += ",\n" + body;
						}
					}
				}
			}
		}
		return text==null ? "" : text;
	}
}