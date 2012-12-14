package org.eclipse.bpmn2.modeler.core.change;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;

/**
 * Abstract change detection support for EObjects.
 * 
 * Works as a post commit resource set change listener.
 * 
 * @author nico.rehwaldt
 */
public abstract class AbstractEObjectChangeSupport implements ResourceSetListener {
	
	protected EObject object;
	
	private boolean registered = false;
	
	protected NotificationFilter filter;

	public AbstractEObjectChangeSupport(EObject object) {
		
		this.object = object;
	}
	
	// event registration ///////////////////////////////////////
	
	public boolean isRegistered() {
		return registered;
	}

	public void unregister() {
		unregisterEditingDomainListener();
		
		registered = false;
	}
	
	public void register() {
		registerEditingDomainListener();
		
		registered = true;
	}

	protected void registerEditingDomainListener() {
		getTransactionalEditingDomain().addResourceSetListener(this);
	}
	
	protected void unregisterEditingDomainListener() {
		TransactionalEditingDomain transactionalEditingDomain = getTransactionalEditingDomain();
		if (transactionalEditingDomain != null) {
			transactionalEditingDomain.removeResourceSetListener(this);
		}
	}
	

	// ResourceSetListener API implementation /////////////////
	
	protected TransactionalEditingDomain getTransactionalEditingDomain() {
		return TransactionUtil.getEditingDomain(object);
	}

	public void setFilter(NotificationFilter filter) {
		this.filter = filter;
	}
	
	@Override
	public NotificationFilter getFilter() {
		return filter;
	}

	@Override
	public final boolean isAggregatePrecommitListener() {
		return false;
	}

	@Override
	public final boolean isPostcommitOnly() {
		return false;
	}

	@Override
	public final boolean isPrecommitOnly() {
		return false;
	}

	@Override
	public abstract void resourceSetChanged(final ResourceSetChangeEvent event);

	@Override
	public final Command transactionAboutToCommit(ResourceSetChangeEvent event) throws RollbackException {
		return null;
	}
}
