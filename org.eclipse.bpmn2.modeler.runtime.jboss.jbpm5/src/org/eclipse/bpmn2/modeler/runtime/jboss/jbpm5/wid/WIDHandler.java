/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.wid;

import java.util.Map;

/**
 * @author bfitzpat
 *
 */
public class WIDHandler {
	
	public enum Section {
	    PARAMETERS, RESULTS, DEPENDENCIES 
	}
	
    /**
     * Takes in the String content of a *.wid/*.conf file from jbpm5 and 
     * parses it into a HashMap of WorkItemDefinition classes
     * @param widDefinitions
     * @param content
     * @throws WIDException
     */
    public static void evaluateWorkDefinitions(
    		Map<String, WorkItemDefinition> widDefinitions, String content) throws WIDException {
    	 processWorkDefinitionsContent(widDefinitions, content);
    }
     
    /*
     * Actually does the work to parse the content using RegEx and brute force 
     * @param widDefinitions
     * @param content
     * @throws WIDException
     */
    private static void processWorkDefinitionsContent (Map<String, WorkItemDefinition> widDefinitions, 
    		String content) throws WIDException {
    	 
          if (content == null) {
        	  WIDException widException = 
        			  new WIDException(
        					  "No data passed to WIDHandler.processWorkDefinitionsContent method"); //$NON-NLS-1$
        	  throw widException;
          }
          if (widDefinitions != null) {
        	  widDefinitions.clear();
          }
          
          String strings[] = content.split("[\n]+"); //$NON-NLS-1$
    	  int openBrackets = 0;
    	  WorkItemDefinition currentWid = new WorkItemDefinitionImpl();
    	  
    	  Section current = Section.PARAMETERS;
    	  
          for (int i = 0; i < strings.length; i++) {
        	  String trim = strings[i].trim();
        	  if (trim.length() == 0) continue;
        	  if (trim.startsWith("[") || trim.endsWith("[") || trim.endsWith(":")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		  if (trim.endsWith(":") && i < strings.length - 1) { //$NON-NLS-1$
        			  trim = strings[i].trim() + strings[i+1].trim();
        		  } else {
        			  openBrackets++;
        		  }
        		  String[] nameValue = trim.split("[:]+"); //$NON-NLS-1$
        		  if (nameValue.length == 2) {
        			  String name = nameValue[0].replace('"', ' ').trim();
        			  if (name.equalsIgnoreCase("parameters")) {
	    				  current = Section.PARAMETERS;
	    			  } else if (name.equalsIgnoreCase("results")) {
	    				  current = Section.RESULTS;
	    			  } else if (name.equalsIgnoreCase("dependencies")) {
	    				  current = Section.DEPENDENCIES;
	    			  }
        		  }
        	  }
        	  if (trim.startsWith("]") || trim.endsWith("]")) { //$NON-NLS-1$ //$NON-NLS-2$
        		  openBrackets--;
        		  if (openBrackets == 1) {
	    			  if (currentWid != null && currentWid.getName() != null) {
	    				  widDefinitions.put(currentWid.getName(), currentWid);
	    			  }
	    			  currentWid = new WorkItemDefinitionImpl();
        		  }
        	  }
        	  if (trim.contains(":")) { //$NON-NLS-1$
        		  String[] nameValue = trim.split("[:]+"); //$NON-NLS-1$
        		  if (nameValue.length == 2 || nameValue.length == 3) {
        			  String name = nameValue[0].replace('"', ' ').trim();
        			  int valueIndex = 1;
        			  if (nameValue.length == 3) {
        				  valueIndex = 2;
        				  name = name + ':' + nameValue[1].replace('"', ' ').trim();
        			  }
        			  String value = nameValue[valueIndex].replace('"', ' ').replace(',', ' ').
        					  replace('[',' ').trim();
        			  if (openBrackets == 2 && value.trim().length() > 0) {
        				  if (name.equalsIgnoreCase("name")) { //$NON-NLS-1$
        					  currentWid.setName(value);
        				  } else if (name.equalsIgnoreCase("displayName")) { //$NON-NLS-1$
        					  currentWid.setDispalyName(value);
        				  } else if (name.equalsIgnoreCase("icon")) { //$NON-NLS-1$
        					  currentWid.setIcon(value);
        				  } else if (name.equalsIgnoreCase("customEditor")) { //$NON-NLS-1$
        					  currentWid.setCustomEditor(value);
        				  } else if (name.equalsIgnoreCase("eclipse:customEditor")) { //$NON-NLS-1$
        					  currentWid.setEclipseCustomEditor(value);
        				  }
        			  } else if (openBrackets == 3 && value.trim().length() > 0) {
        				  if (value.startsWith("new") && value.indexOf("(")>0) {
        					  int index = value.indexOf("(");
        					  value = value.substring(3,index).trim();
        				  }
        				  if (current == Section.PARAMETERS)
        					  currentWid.getParameters().put(name, value);
        				  else if (current == Section.RESULTS)
        					  currentWid.getResults().put(name, value);
        			  }
        		  }
        	  }
          }
     }

}