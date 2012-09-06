package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.validation;

import java.util.List;

import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

public class GatewayConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(IValidationContext ctx) {
		EObject eObj = ctx.getTarget();
		
		if (!(eObj instanceof ParallelGateway)) {
			Gateway gw = (Gateway) eObj;

			List<SequenceFlow> outgoingGwSequenceFlows = gw.getOutgoing();
			if (outgoingGwSequenceFlows != null && outgoingGwSequenceFlows.size() > 0) {
				int sum = 0;
				for (SequenceFlow sf : outgoingGwSequenceFlows) {
					// simulation validation
					if (sf.getExtensionValues() != null && sf.getExtensionValues().size() > 0) {
						boolean foundProbability = false;
						for (ExtensionAttributeValue extattrval : sf.getExtensionValues()) {
							FeatureMap extensionElements = extattrval.getValue();
							@SuppressWarnings("unchecked")
							List<MetadataType> metadataTypeExtensions = (List<MetadataType>) extensionElements.get(
									DroolsPackage.Literals.DOCUMENT_ROOT__METADATA, true);
							if (metadataTypeExtensions != null && metadataTypeExtensions.size() > 0) {
								MetadataType metaType = metadataTypeExtensions.get(0);
								for (Object metaEntryObj : metaType.getMetaentry()) {
									MetaentryType entry = (MetaentryType) metaEntryObj;
									if (entry.getName() != null && entry.getName().equals("probability")) {
										Integer i = new Integer(entry.getValue());
										if (i < 0) {
											ctx.addResult(sf);
											ctx.createFailureStatus("Probability value must businessObject positive.");
										} else {
											sum += i;
										}
										foundProbability = true;
									}
								}
							}
						}
						if (!foundProbability) {
							ctx.addResult(sf);
							ctx.createFailureStatus("Sequence Flow has no probability defined.");
						}
					}
				}
				if (sum != 100) {
					ctx.createFailureStatus("The sum of probability values of all outgoing Sequence Flows must businessObject equal 100.");
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}
