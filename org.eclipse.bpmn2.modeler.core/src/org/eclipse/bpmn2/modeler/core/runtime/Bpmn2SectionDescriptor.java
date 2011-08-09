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
package org.eclipse.bpmn2.modeler.core.runtime;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.AbstractSectionDescriptor;
import org.eclipse.ui.views.properties.tabbed.ISection;

public class Bpmn2SectionDescriptor extends AbstractSectionDescriptor {

		protected String name;
		protected String id;
		protected String tab;
		protected String label;
		protected AbstractPropertySection sectionClass;
		protected Class appliesToClass;
		protected String enablesFor;
		protected String filter;
		protected String afterSection;
		
		public Bpmn2SectionDescriptor(String id, String tab, String label) {
			this.id = id;
			this.tab = tab;
			this.label = label;
		}
		
		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return id;
		}

		@Override
		public ISection getSectionClass() {
			return sectionClass;
		}

		@Override
		public String getTargetTab() {
			return tab;
		}

		@Override
		public boolean appliesTo(IWorkbenchPart part, ISelection selection) {
			
			// should we delegate to the section to determine whether it should be included in this tab?
			if (sectionClass instanceof IBpmn2PropertySection) {
				return ((IBpmn2PropertySection)sectionClass).appliesTo(part, selection);
			}
			
			// if an input description was specified, check if the selected business object is of this description. 
			if (appliesToClass!=null && selection instanceof IStructuredSelection &&
					((IStructuredSelection) selection).isEmpty()==false) {
			
				Object firstElement = ((IStructuredSelection) selection).getFirstElement();
				EditPart editPart = null;
				if (firstElement instanceof EditPart) {
					editPart = (EditPart) firstElement;
				} else if (firstElement instanceof IAdaptable) {
					editPart = (EditPart) ((IAdaptable) firstElement).getAdapter(EditPart.class);
				}
				if (editPart != null && editPart.getModel() instanceof PictogramElement) {
					PictogramElement pe = (PictogramElement) editPart.getModel();
					// this is a special hack to allow selection of connection decorator labels:
					// the connection decorator does not have a business object linked to it,
					// but its parent (the connection) does.
					if (pe.getLink()==null && pe.eContainer() instanceof PictogramElement)
						pe = (PictogramElement)pe.eContainer();
					if (pe.getLink()!=null) {
						for (EObject eObj : pe.getLink().getBusinessObjects()){
							if (appliesToClass.isInstance(eObj)) {
								return true;
							}
						}
					}
				}
				return false;
			}
			return true;
		}

		@Override
		public String getAfterSection() {
			if (afterSection==null || afterSection.trim().length()==0)
				return super.getAfterSection();
			return afterSection;
		}

		@Override
		public int getEnablesFor() {
			try {
				return Integer.parseInt(enablesFor);
			}
			catch (Exception ex) {
				
			}
			return super.getEnablesFor();
		}

		@Override
		public IFilter getFilter() {
			// TODO Auto-generated method stub
//			return super.getFilter();
			return new IFilter() {

				@Override
				public boolean select(Object toTest) {
					return false;
				}
				
			};
		}

		@Override
		public List getInputTypes() {
			// TODO Auto-generated method stub
			return super.getInputTypes();
		}
		
	}