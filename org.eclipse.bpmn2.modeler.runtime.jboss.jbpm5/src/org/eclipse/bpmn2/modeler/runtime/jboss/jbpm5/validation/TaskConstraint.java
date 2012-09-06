package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.validation;

import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.Task;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

public class TaskConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(IValidationContext ctx) {
		EObject eObj = ctx.getTarget();
		if (eObj instanceof Task) {
			Task ta = (Task) eObj;

			if (ta.getExtensionValues() != null && ta.getExtensionValues().size() > 0) {
				boolean foundDistributionType = false;
				String distributionTypeValue = "";
				boolean foundDuration = false;
				boolean foundTimeUnits = false;
				boolean foundRange = false;
				boolean foundStandardDeviation = false;
				for (ExtensionAttributeValue extattrval : ta.getExtensionValues()) {
					FeatureMap extensionElements = extattrval.getValue();
					@SuppressWarnings("unchecked")
					List<MetadataType> metadataTypeExtensions = (List<MetadataType>) extensionElements.get(
							DroolsPackage.Literals.DOCUMENT_ROOT__METADATA, true);

					if (metadataTypeExtensions != null && metadataTypeExtensions.size() > 0) {
						MetadataType metaType = metadataTypeExtensions.get(0);
						for (Object metaEntryObj : metaType.getMetaentry()) {
							MetaentryType entry = (MetaentryType) metaEntryObj;
							if (entry.getName() != null && entry.getName().equals("costpertimeunit")) {
								Float f = new Float(entry.getValue());
								if (f.floatValue() < 0) {
									ctx.createFailureStatus("Cost per Time Unit value must businessObject positive");
								}
							}
							if (entry.getName() != null && entry.getName().equals("distributiontype")) {
								foundDistributionType = true;
								distributionTypeValue = entry.getValue();
							}
							if (entry.getName() != null && entry.getName().equals("duration")) {
								Float f = new Float(entry.getValue());
								if (f.floatValue() < 0) {
									ctx.createFailureStatus("Duration value must businessObject positive");
								}
								foundDuration = true;
							}
							if (entry.getName() != null && entry.getName().equals("timeunit")) {
								foundTimeUnits = true;
							}
							if (entry.getName() != null && entry.getName().equals("workinghours")) {
								Float f = new Float(entry.getValue());
								if (f.floatValue() < 0) {
									ctx.createFailureStatus("Working Hours value must businessObject positive");
								}
							}
							if (entry.getName() != null && entry.getName().equals("range")) {
								Float f = new Float(entry.getValue());
								if (f.floatValue() < 0) {
									ctx.createFailureStatus("Range value must businessObject positive");
								}
								foundRange = true;
							}
							if (entry.getName() != null && entry.getName().equals("standarddeviation")) {
								foundStandardDeviation = true;
							}
						}
					}

				}
				if (!foundDistributionType) {
					ctx.createFailureStatus("Task has no distribution type defined");
				}
				if (!foundDuration) {
					ctx.createFailureStatus("Task has no duration defined");
				}
				if (!foundTimeUnits) {
					ctx.createFailureStatus("Task has no Time Units defined");
				}
				if (foundDistributionType) {
					if ((distributionTypeValue.equals("random") || distributionTypeValue.equals("uniform"))
							&& !foundRange) {
						ctx.createFailureStatus("Task has no Range defined");
					}
					if (distributionTypeValue.equals("normal") && !foundStandardDeviation) {
						ctx.createFailureStatus("Task has no Standard Deviation defined");
					}
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}
