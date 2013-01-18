package org.eclipse.bpmn2.modeler.core.layout.nnew;

/**
 * Context in which a layouting strategy performs its work.
 * 
 * @author nico.rehwaldt
 */
public interface LayoutContext {
	
	/**
	 * Returns true if the connection enclosed in this context is
	 * already laid out and can be repaired and false to indicate that 
	 * initial layouting is needed.
	 * 
	 * @return
	 */
	public boolean isRepairable();
	
	/**
	 * Returns true the context decides to relayout, despite of a possible successful repair
	 * 
	 * @return
	 */
	public boolean needsLayout();

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

	/**
	 * Perform clean up before actual repair starts
	 */
	public void prune();
}
