package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.validation;

import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

public class UserTaskConstraint extends AbstractModelConstraint {
	private IDiagramProfile profile;
	private String uuid = "uuid";

	@Override
	public IStatus validate(IValidationContext ctx) {
		EObject eObj = ctx.getTarget();
		if (eObj instanceof UserTask) {
			UserTask ut = (UserTask)eObj;
			
			String taskName = null;
			Iterator<FeatureMap.Entry> utiter = ut.getAnyAttribute().iterator();
			boolean foundTaskName = false;
			while (utiter.hasNext()) {
				FeatureMap.Entry entry = utiter.next();
				if (entry.getEStructuralFeature().getName().equals("taskName")) {
					foundTaskName = true;
					taskName = (String) entry.getValue();
					if (taskName==null || taskName.isEmpty()) {
						return ctx.createFailureStatus("User Task has no task name");
					}
				}
			}
			if (!foundTaskName) {
				return ctx.createFailureStatus("User Task has no task name");
			} else {
				// TODO:
				if (taskName != null) {
					String[] packageAssetInfo = ServletUtil.findPackageAndAssetInfo(uuid, profile);
					String taskFormName = taskName + "-taskform";
					if (!ServletUtil.assetExistsInGuvnor(packageAssetInfo[0], taskFormName, profile)) {
						ctx.createFailureStatus("User Task has no task form defined");
					}
				}
			}

			// simulation validation
			if (ut.getExtensionValues() != null && ut.getExtensionValues().size() > 0) {
				boolean foundStaffAvailability = false;
				for (ExtensionAttributeValue extattrval : ut.getExtensionValues()) {
					FeatureMap extensionElements = extattrval.getValue();
					@SuppressWarnings("unchecked")
					List<MetadataType> metadataTypeExtensions = (List<MetadataType>) extensionElements.get(
							DroolsPackage.Literals.DOCUMENT_ROOT__METADATA, true);
					if (metadataTypeExtensions != null && metadataTypeExtensions.size() > 0) {
						MetadataType metaType = metadataTypeExtensions.get(0);
						for (Object metaEntryObj : metaType.getMetaentry()) {
							MetaentryType entry = (MetaentryType) metaEntryObj;
							if (entry.getName() != null && entry.getName().equals("staffavailability")) {
								Float f = new Float(entry.getValue());
								if (f.floatValue() < 0) {
									ctx.createFailureStatus("Staff Availability value must businessObject positive");
								}
								foundStaffAvailability = true;
							}
						}
					}
					if (!foundStaffAvailability) {
						return ctx.createFailureStatus("User Task has no staff availability defined");
					}
				}
			}
		}	
		return ctx.createSuccessStatus();
	}

}
