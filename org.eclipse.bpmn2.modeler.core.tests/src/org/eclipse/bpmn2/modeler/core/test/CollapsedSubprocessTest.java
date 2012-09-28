package org.eclipse.bpmn2.modeler.core.test;

import java.util.List;

import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.impl.DocumentRootImpl;
import org.eclipse.bpmn2.modeler.core.test.importer.AbstractImportBpmn2ModelTest;
import org.eclipse.dd.di.DiagramElement;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class CollapsedSubprocessTest extends AbstractImportBpmn2ModelTest {
	
	@Test
	public void testImport() {
		createEditingDomain("org/eclipse/bpmn2/modeler/core/test/bpmn/subprocess.bpmn-1.xml");
		
		DocumentRootImpl documentRoot = (DocumentRootImpl)resource.getContents().get(0);
		
		List<DiagramElement> elements = documentRoot.getDefinitions().getDiagrams().get(0).getPlane().getPlaneElement();
		
		BPMNShape diagramElement = (BPMNShape) elements.get(6);
		Assert.assertEquals("sid-1F5B05C9-35F5-4F54-876D-D7F75E312428_gui", diagramElement.getId());
		Assert.assertFalse(diagramElement.isIsExpanded());
		
		
	}

}
