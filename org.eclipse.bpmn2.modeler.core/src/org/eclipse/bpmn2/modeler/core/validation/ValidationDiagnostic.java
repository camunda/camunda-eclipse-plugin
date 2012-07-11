package org.eclipse.bpmn2.modeler.core.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

public class ValidationDiagnostic implements Resource.Diagnostic{
	IStatus status;
	private String location;
	
	public ValidationDiagnostic(IStatus status, final Resource resource) {
		this.status = status;
		String path = resource.getURI().toPlatformString(false);
		this.location = URI.createPlatformResourceURI(path).toString();
	}

	@Override
	public String getMessage() {
		return status.getMessage();
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public int getLine() {
		return 0;
	}

	@Override
	public int getColumn() {
		return 0;
	}
}