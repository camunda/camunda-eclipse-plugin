package org.eclipse.bpmn2.modeler.ui.adapters.properties;

import org.eclipse.bpmn2.Import;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.adapters.ObjectDescriptor;
import org.eclipse.emf.common.notify.AdapterFactory;

public class ImportPropertiesAdapter extends ExtendedPropertiesAdapter<Import> {

	public ImportPropertiesAdapter(AdapterFactory adapterFactory, Import object) {
		super(adapterFactory, object);
    	
		setObjectDescriptor(new ObjectDescriptor<Import>(adapterFactory, object) {
			@Override
			public String getDisplayName(Object context) {
				Import imp = adopt(context);
				return imp.getLocation();
			}
			
			@Override
			public String getLabel(Object context) {
				return "Import";
			}
		});
	}

}
