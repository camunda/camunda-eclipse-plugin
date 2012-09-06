package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.validation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.bpmn2.CallActivity;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

public class CallActivityConstraint extends AbstractModelConstraint {
	private IDiagramProfile profile;
	private String uuid = "uuid";

	@Override
	public IStatus validate(IValidationContext ctx) {
		EObject eObj = ctx.getTarget();
		if (eObj instanceof CallActivity) {
			CallActivity ca = (CallActivity) eObj;
			if (ca.getCalledElementRef() == null) {
				ctx.createFailureStatus("Reusable Subprocess has no called element specified.");
			} else {
				String[] packageAssetInfo = ServletUtil.findPackageAndAssetInfo(uuid, profile);
				String packageName = packageAssetInfo[0];
				List<String> allProcessesInPackage = ServletUtil.getAllProcessesInPackage(packageName, profile);
				boolean foundCalledElementProcess = false;
				for (String p : allProcessesInPackage) {
					String processContent = ServletUtil.getProcessSourceContent(packageName, p, profile);
					Pattern pattern = Pattern.compile("<\\S*process[\\s\\S]*id=\"" + ca.getCalledElementRef() + "\"",
							Pattern.MULTILINE);
					Matcher m = pattern.matcher(processContent);
					if (m.find()) {
						foundCalledElementProcess = true;
						break;
					}
				}
				foundCalledElementProcess = true; // TODO: remove this
				if (!foundCalledElementProcess) {
					ctx.createFailureStatus("No existing process with id=" + ca.getCalledElementRef()
							+ " could businessObject found.");
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}
