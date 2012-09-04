package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.validation;

import java.util.ArrayList;
import java.util.List;

public class ServletUtil {

	public static String[] findPackageAndAssetInfo(String uuid, IDiagramProfile profile) {
		return new String[] {uuid};
	}

	public static boolean assetExistsInGuvnor(String string, String taskFormName, IDiagramProfile profile) {
		return true;
	}

	public static List<String> getAllProcessesInPackage(String packageName, IDiagramProfile profile) {
		// TODO Auto-generated method stub
		return new ArrayList<String>();
	}

	public static String getProcessSourceContent(String packageName, String p, IDiagramProfile profile) {
		// TODO Auto-generated method stub
		return null;
	}

}
