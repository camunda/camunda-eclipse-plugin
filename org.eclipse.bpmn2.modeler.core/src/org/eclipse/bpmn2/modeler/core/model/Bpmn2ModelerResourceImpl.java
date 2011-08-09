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
/**
 * <copyright>
 * 
 * Copyright (c) 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Reiner Hille-Doering (SAP AG) - initial API and implementation and/or initial documentation
 * 
 * </copyright>
 */
package org.eclipse.bpmn2.modeler.core.model;

import java.util.Iterator;

import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.bpmn2.util.ImportHelper;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource </b> associated with the package.
 * @implements Bpmn2Resource
 * <!-- end-user-doc -->
 * @see org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl
 */
public class Bpmn2ModelerResourceImpl extends Bpmn2ResourceImpl {

	public static final String BPMN2_CONTENT_TYPE_ID = "org.eclipse.bpmn2.content-description.xml";

    /**
     * Creates an instance of the resource.
     * @param uri the URI of the new resource.
     */
    public Bpmn2ModelerResourceImpl(URI uri) {
        super(uri);
    }

    @Override
    protected void prepareSave() {
        EObject cur;
        Definitions thisDefinitions = ImportHelper.getDefinitions(this);
        for (Iterator<EObject> iter = getAllContents(); iter.hasNext();) {
            cur = iter.next();

            setDefaultId(cur);

            for (EObject referenced : cur.eCrossReferences()) {
            	setDefaultId(referenced);
                if (thisDefinitions != null) {
                    Resource refResource = referenced.eResource();
                    if (refResource != null && refResource != this) {
                        createImportIfNecessary(thisDefinitions, refResource);
                    }
                }
            }
        }
    }

    /**
     * Set the ID attribute of cur to a generated ID, if it is not already set.
     * @param obj The object whose ID should be set.
     */
    private void setDefaultId(EObject obj) {
        if (obj.eClass() != null) {
            EStructuralFeature idAttr = obj.eClass().getEIDAttribute();
            if (idAttr != null && !obj.eIsSet(idAttr)) {
            	ModelUtil.setID(obj);
            }
        }
    }
}
