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
package org.camunda.bpm.modeler.runtime.engine;

import java.util.Enumeration;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.camunda.bpm.modeler.core.IBpmn2RuntimeExtension;
import org.camunda.bpm.modeler.core.preferences.Bpmn2Preferences;
import org.camunda.bpm.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.camunda.bpm.modeler.ui.wizards.FileService;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.xml.sax.InputSource;

@SuppressWarnings("restriction")
public class ActivitiRuntimeExtension implements IBpmn2RuntimeExtension {

	public static final String PREF_TOGGLE_DIAGRAM_GENERATION = "TOGGLE_DIAGRAM_GENERATION";
	public static final String ENGINE_NAMESPACE = "http://www.activiti.org";
	private static final String ROOT_ELEMENT = "definitions";
	
	private RootElementParser parser;

	/* (non-Javadoc)
	 * Check if the given input file is a drools-generated (jBPM) process file.
	 * 
	 * @see org.camunda.bpm.modeler.IBpmn2RuntimeExtension#isContentForRuntime(org.eclipse.core.resources.IFile)
	 */
  
	@Override
  public boolean isContentForRuntime(IEditorInput input) {
    try {
      InputSource source = new InputSource(FileService.getInputContents(input) );
      parser = new RootElementParser();
      parser.parse(source);
    } catch (AcceptedException e) {
      return true;
    } catch (Exception e) {
    } finally {
      parser = null;
    }

    return false;
  }
	
	 private class RootElementParser extends SAXParser {
	    @Override
	    public void startElement(QName qName, XMLAttributes attributes, Augmentations augmentations)
	        throws XNIException {

	      super.startElement(qName, attributes, augmentations);

	      // search the "definitions" for a drools namespace
	      if (ROOT_ELEMENT.equals(qName.localpart)) {
	        Enumeration<?> e = fNamespaceContext.getAllPrefixes();
	        while (e.hasMoreElements()) {
	          String prefix = (String)e.nextElement();
	          String namespace = fNamespaceContext.getURI(prefix);
	          if (ENGINE_NAMESPACE.equals(namespace))
	            throw new AcceptedException(qName.localpart);
	        }
	        throw new RejectedException();
	      } else {
	        throw new RejectedException();
	      }
	    }
	  }

	  private class AcceptedException extends RuntimeException {
	    public String acceptedRootElement;

	    public AcceptedException(String acceptedRootElement) {
	      this.acceptedRootElement = acceptedRootElement;
	    }

	    private static final long serialVersionUID = 1L;
	  }

	  private class RejectedException extends RuntimeException {
	    private static final long serialVersionUID = 1L;
	  }
	

	@Override
	public Composite getPreferencesComposite(Composite parent, final Bpmn2Preferences preferences) {
		Composite composite = new Composite(parent, SWT.NO_SCROLL);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;

		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));

		Button diagramGeneration = new Button(composite, SWT.CHECK);
		diagramGeneration.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		diagramGeneration.setText("Generate PNG Diagram Image");
		diagramGeneration.setSelection( preferences.getBoolean(PREF_TOGGLE_DIAGRAM_GENERATION, true) );

		diagramGeneration.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				preferences.setBoolean(PREF_TOGGLE_DIAGRAM_GENERATION, !preferences.getBoolean(PREF_TOGGLE_DIAGRAM_GENERATION, true));
			}
		});
		return composite;
	}

	@Override
	public String getTargetNamespace(Bpmn2DiagramType diagramType) {
		return "http://activiti.org/bpmn";
	}

  @Override
  public void initialize(DiagramEditor editor) {
    
  }

}
