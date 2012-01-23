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
 * @author Bob Brodt
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.ui.adapters.properties;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.DataAssociation;
import org.eclipse.bpmn2.ItemAwareElement;
import org.eclipse.bpmn2.modeler.ui.adapters.Bpmn2FeatureDescriptor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * @author Bob Brodt
 *
 */
public class DataAssociationPropertiesAdapter extends Bpmn2ExtendedPropertiesAdapter {

	/**
	 * @param adapterFactory
	 * @param object
	 */
	public DataAssociationPropertiesAdapter(AdapterFactory adapterFactory, EObject object) {
		super(adapterFactory, object);

    	final EStructuralFeature ref = Bpmn2Package.eINSTANCE.getDataAssociation_SourceRef();
    	setFeatureDescriptor(ref,
			new Bpmn2FeatureDescriptor(adapterFactory,object,ref) {

				@Override
				public void setValue(EObject context, final Object value) {
					final DataAssociation association = context instanceof DataAssociation ?
							(DataAssociation)context :
							(DataAssociation)this.object;
					
					TransactionalEditingDomain editingDomain = getEditingDomain(object);
					if (association.getSourceRef().size()==0) {
						if (editingDomain == null) {
							association.getSourceRef().add((ItemAwareElement)value);
						} else {
							editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
								@Override
								protected void doExecute() {
									association.getSourceRef().add((ItemAwareElement)value);
								}
							});
						}
					}
					else {
						if (editingDomain == null) {
							association.getSourceRef().set(0,(ItemAwareElement)value);
						} else {
							editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
								@Override
								protected void doExecute() {
									association.getSourceRef().set(0,(ItemAwareElement)value);
								}
							});
						}
					}
				}
    		}
    	);
	}

}
