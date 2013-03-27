Jubula GUI Test for Camunda Modeler
===================================

Scope
-----
This project allows you to perform functional UI tests for a RCP application of the camunda Modeler. The test suite has a focus on the modelers properties panel and basic menu functionality for diagrams.


Install Jubula
--------------
Download and install the Jubula Standalone Edition from the [Jubula Project Site](http://www.eclipse.org/jubula/download.php).
For this project we are using Jubula-Version 1.3.0 with a Oracle 11g data base.
Import the project xml file _bpmn2_modeler.xml_ via `Test --> Import --> Browse File`. 


Building the AUT
----------------
Create a RCP application of the BPMN 2.0 Modeler (for more information please read the [RCP manual](https://github.com/camunda/camunda-modeler/blob/develop/CONTRIBUTING.md).  
Alternatively you can download a preconfigured RCP application [here](https://fox.camunda.com/download/bpmn2_modeler_rcp.zip).


Setup the AUT
-------------

###AUT directory structure

	dev/Jubula/AUT/bpmn2_modeler/
		eclipse/
			configuration/						-- Edit configuration.ini here
			plugins/							-- Add org.eclipse.jubula.rc.rcp here
		workspace/								-- Eclipse workspace
			.metadata/
				.plugins/
					org.eclipse.e4.workbench/	-- Change workbench.xmi here
			JubulaTestProject/					-- Eclipse project containing all *.bpmn diagrams
The bpmn2_modeler project has the following working directory: `C:\dev\Jubula\AUT\bpmn2_modeler\eclipse\`. You can change AUT working directory in Jubula under `Test --> Properties --> AUTs`.

###AUT perspective
As we are using absolute coordinates for our tests you have to adjust the Eclipse perspective of the AUT. 
In the folder _org.eclipse.e4.workbench/workbench.xmi_ we provide a suitable Eclipse perspective called _Jubula Test_.
Replace the folder _workbench.xmi_ in the AUT's and use the _Jubula Test_ perspective `Window --> Open Perspectiv --> Jubula Test`.

Please note that the perspective is modified for a screen resolution of 1366x768 pixel. If you are using a different resolution adjust the AUT's Properties Panel according to the following screen shot.

![AUT workbench perspective](https://raw.github.com/camunda/bpmn2-modeler/develop/org.camunda.bpm.modeler.tests.functional/documentation/images/Eclipse_perspective.png)

###Test project and template diagrams
The folder _template diagrams_ provides all necessary diagrams to run the Jubula tests. Create a new _Java Project_ with your AUT and import the template diagrams. 

