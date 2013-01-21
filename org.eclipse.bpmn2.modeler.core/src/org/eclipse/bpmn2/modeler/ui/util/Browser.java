package org.eclipse.bpmn2.modeler.ui.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Browser abstraction
 * 
 * @author nico.rehwaldt
 */
public class Browser {
	
	/**
	 * Open the given url in a browser
	 * @param url
	 * 
	 * @return true if it worked, false otherwise
	 */
	public static boolean open(String url) {
		
		try {
			// Open default external browser
			PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(url));
			return true;
		} catch (PartInitException ex) {
			ex.printStackTrace();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
}
