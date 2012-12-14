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

import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.AbstractSectionDescriptor;
import org.eclipse.ui.views.properties.tabbed.ISection;

public class Bpmn2SectionDescriptor extends AbstractSectionDescriptor {

		protected String id;
		protected String tab;
		
		protected AbstractPropertySection sectionClass;
		
		protected String enablesFor;
		protected String filter;
		
		//////////
		
		protected ISection section;
		
		public Bpmn2SectionDescriptor(String id, ISection section) {
			this.section = section;
			this.id = id;
		}
		
		@Override
		public String getId() {
			return id;
		}

		@Override
		public ISection getSectionClass() {
			return section;
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
			
			return true;
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
			return new IFilter() {

				@Override
				public boolean select(Object toTest) {
					return false;
				}
				
			};
		}

		/**
		 * @param replacedId
		 * @param part
		 * @param selection
		 * @return
		 */
		public boolean doReplaceTab(String replacedId, IWorkbenchPart part, ISelection selection) {
			if (sectionClass instanceof IBpmn2PropertySection) {
				return ((IBpmn2PropertySection)sectionClass).doReplaceTab(replacedId, part, selection);
			}
			return appliesTo(part,selection);
		}
		
	}