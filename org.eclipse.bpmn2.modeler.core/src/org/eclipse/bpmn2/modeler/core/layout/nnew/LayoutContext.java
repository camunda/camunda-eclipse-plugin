package org.eclipse.bpmn2.modeler.core.layout.nnew;

/**
 * Context in which a layouting strategy performs its work.
 * 
 * @author nico.rehwaldt
 */
public interface LayoutContext {
	
	/**
	 * Returns true if the connection enclosed in this context is
	 * already laid out and false to indicate that layouting is needed.
	 * 
	 * @return
	 */
	public boolean isLayouted();

	/**
	 * Perform a fix operation to repair the
	 * connection layout after element move or create. 
	 * 
	 * @return true if the repair operation was successful and 
	 *  	   false to indicate that the layout could not be repaired
	 */
	public boolean repair();

	/**
	 * Perform a full layout on the connection.
	 */
	public void layout();
}
