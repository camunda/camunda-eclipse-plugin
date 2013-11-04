package org.camunda.bpm.modeler.core.features.rules;

import org.eclipse.graphiti.features.context.IContext;

public abstract class RuleOperations {
	
	/**
	 * Indicates which side / of a bidirectional directed
	 * operation a {@link Algorithm} applies to.
	 * 
	 * @author nico.rehwaldt
	 */
	protected enum Side {
		FROM, 
		TO
	};
	
	/**
	 * TODO: Refactor or streamline with other stuff.
	 * 
	 * @author nico.rehwaldt
	 */
	public interface Algorithm<T extends IContext> {
		
		boolean appliesTo(T context);

		public boolean canExecute(T context);
		
		public void execute(T context);
	}
	
	public static class FromModelOperation<T extends IContext> {
		
		public Algorithm<T> from;
	
		public FromModelOperation(Algorithm<T> from) {
			this.from = from;
		}
	
		public boolean canExecute(T context) {
			return !isEmpty() && from.canExecute(context);
		}
	
		public void execute(T context) {
			from.execute(context);
		}
	
		public boolean isEmpty() {
			return from == null;
		}
	}
	
	public static class ToModelOperation<T extends IContext> {
		
		public Algorithm<T> to;
	
		public ToModelOperation(Algorithm<T> to) {
			this.to = to;
		}
	
		public boolean canExecute(T context) {
			return !isEmpty() && to.canExecute(context);
		}
	
		public void execute(T context) {
			to.execute(context);
		}
	
		public boolean isEmpty() {
			return to == null;
		}
	}
	
	public static class FromToModelOperation<T extends IContext> extends ToModelOperation<T> {
		
		public Algorithm<T> from;

		public FromToModelOperation(Algorithm<T> from, Algorithm<T> to) {
			super(to);
			this.from = from;
		}

		public boolean canExecute(T context) {
			return !isEmpty() && from.canExecute(context) && super.canExecute(context);
		}

		public void execute(T context) {
			from.execute(context);
			super.execute(context);
		}

		public boolean isEmpty() {
			return from == null || super.isEmpty();
		}
	}
}
